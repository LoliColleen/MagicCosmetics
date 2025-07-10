/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.SQLExceptionOverride;
/*     */ import com.zaxxer.hikari.util.FastList;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProxyConnection
/*     */   implements Connection
/*     */ {
/*     */   static final int DIRTY_BIT_READONLY = 1;
/*     */   static final int DIRTY_BIT_AUTOCOMMIT = 2;
/*     */   static final int DIRTY_BIT_ISOLATION = 4;
/*     */   static final int DIRTY_BIT_CATALOG = 8;
/*     */   static final int DIRTY_BIT_NETTIMEOUT = 16;
/*     */   static final int DIRTY_BIT_SCHEMA = 32;
/*  69 */   private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConnection.class);
/*     */   
/*  71 */   private static final Set<String> ERROR_STATES = new HashSet<>(); static {
/*  72 */     ERROR_STATES.add("0A000");
/*  73 */     ERROR_STATES.add("57P01");
/*  74 */     ERROR_STATES.add("57P02");
/*  75 */     ERROR_STATES.add("57P03");
/*  76 */     ERROR_STATES.add("01002");
/*  77 */     ERROR_STATES.add("JZ0C0");
/*  78 */     ERROR_STATES.add("JZ0C1");
/*     */   }
/*  80 */   protected Connection delegate; private final PoolEntry poolEntry; private final ProxyLeakTask leakTask; private static final Set<Integer> ERROR_CODES = new HashSet<>(); private final FastList<Statement> openStatements; private int dirtyBits; private boolean isCommitStateDirty; static {
/*  81 */     ERROR_CODES.add(Integer.valueOf(500150));
/*  82 */     ERROR_CODES.add(Integer.valueOf(2399));
/*     */   }
/*     */   private boolean isReadOnly; private boolean isAutoCommit;
/*     */   private int networkTimeout;
/*     */   private int transactionIsolation;
/*     */   private String dbcatalog;
/*     */   private String dbschema;
/*     */   
/*     */   protected ProxyConnection(PoolEntry poolEntry, Connection connection, FastList<Statement> openStatements, ProxyLeakTask leakTask, boolean isReadOnly, boolean isAutoCommit) {
/*  91 */     this.poolEntry = poolEntry;
/*  92 */     this.delegate = connection;
/*  93 */     this.openStatements = openStatements;
/*  94 */     this.leakTask = leakTask;
/*  95 */     this.isReadOnly = isReadOnly;
/*  96 */     this.isAutoCommit = isAutoCommit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 103 */     return getClass().getSimpleName() + "@" + getClass().getSimpleName() + " wrapping " + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean getAutoCommitState() {
/* 112 */     return this.isAutoCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getCatalogState() {
/* 117 */     return this.dbcatalog;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getSchemaState() {
/* 122 */     return this.dbschema;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getTransactionIsolationState() {
/* 127 */     return this.transactionIsolation;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean getReadOnlyState() {
/* 132 */     return this.isReadOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getNetworkTimeoutState() {
/* 137 */     return this.networkTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final PoolEntry getPoolEntry() {
/* 146 */     return this.poolEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException sqle) {
/* 152 */     boolean evict = false;
/* 153 */     SQLException nse = sqle;
/* 154 */     SQLExceptionOverride exceptionOverride = (this.poolEntry.getPoolBase()).exceptionOverride;
/* 155 */     for (int depth = 0; this.delegate != ClosedConnection.CLOSED_CONNECTION && nse != null && depth < 10; depth++) {
/* 156 */       String sqlState = nse.getSQLState();
/* 157 */       if ((sqlState != null && sqlState.startsWith("08")) || nse instanceof java.sql.SQLTimeoutException || ERROR_STATES
/*     */         
/* 159 */         .contains(sqlState) || ERROR_CODES
/* 160 */         .contains(Integer.valueOf(nse.getErrorCode()))) {
/*     */         
/* 162 */         if (exceptionOverride != null && exceptionOverride.adjudicate(nse) == SQLExceptionOverride.Override.DO_NOT_EVICT) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 167 */         evict = true;
/*     */         
/*     */         break;
/*     */       } 
/* 171 */       nse = nse.getNextException();
/*     */     } 
/*     */ 
/*     */     
/* 175 */     if (evict) {
/* 176 */       SQLException exception = (nse != null) ? nse : sqle;
/* 177 */       LOGGER.warn("{} - Connection {} marked as broken because of SQLSTATE({}), ErrorCode({})", new Object[] { this.poolEntry
/* 178 */             .getPoolName(), this.delegate, exception.getSQLState(), Integer.valueOf(exception.getErrorCode()), exception });
/* 179 */       this.leakTask.cancel();
/* 180 */       this.poolEntry.evict("(connection is broken)");
/* 181 */       this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */     } 
/*     */     
/* 184 */     return sqle;
/*     */   }
/*     */ 
/*     */   
/*     */   final synchronized void untrackStatement(Statement statement) {
/* 189 */     this.openStatements.remove(statement);
/*     */   }
/*     */ 
/*     */   
/*     */   final void markCommitStateDirty() {
/* 194 */     if (!this.isAutoCommit) {
/* 195 */       this.isCommitStateDirty = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void cancelLeakTask() {
/* 201 */     this.leakTask.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized <T extends Statement> T trackStatement(T statement) {
/* 206 */     this.openStatements.add(statement);
/*     */     
/* 208 */     return statement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void closeStatements() {
/* 214 */     int size = this.openStatements.size();
/* 215 */     if (size > 0) {
/* 216 */       for (int i = 0; i < size && this.delegate != ClosedConnection.CLOSED_CONNECTION; i++) { try {
/* 217 */           Statement ignored = (Statement)this.openStatements.get(i);
/*     */           
/* 219 */           if (ignored != null) ignored.close(); 
/* 220 */         } catch (SQLException e) {
/* 221 */           LOGGER.warn("{} - Connection {} marked as broken because of an exception closing open statements during Connection.close()", this.poolEntry
/* 222 */               .getPoolName(), this.delegate);
/* 223 */           this.leakTask.cancel();
/* 224 */           this.poolEntry.evict("(exception closing Statements during Connection.close())");
/* 225 */           this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */         }  }
/*     */ 
/*     */       
/* 229 */       this.openStatements.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void close() throws SQLException {
/* 242 */     closeStatements();
/*     */     
/* 244 */     if (this.delegate != ClosedConnection.CLOSED_CONNECTION) {
/* 245 */       this.leakTask.cancel();
/*     */       
/*     */       try {
/* 248 */         if (this.isCommitStateDirty && !this.isAutoCommit) {
/* 249 */           this.delegate.rollback();
/* 250 */           LOGGER.debug("{} - Executed rollback on connection {} due to dirty commit state on close().", this.poolEntry.getPoolName(), this.delegate);
/*     */         } 
/*     */         
/* 253 */         if (this.dirtyBits != 0) {
/* 254 */           this.poolEntry.resetConnectionState(this, this.dirtyBits);
/*     */         }
/*     */         
/* 257 */         this.delegate.clearWarnings();
/*     */       }
/* 259 */       catch (SQLException e) {
/*     */         
/* 261 */         if (!this.poolEntry.isMarkedEvicted()) {
/* 262 */           throw checkException(e);
/*     */         }
/*     */       } finally {
/*     */         
/* 266 */         this.delegate = ClosedConnection.CLOSED_CONNECTION;
/* 267 */         this.poolEntry.recycle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 277 */     return (this.delegate == ClosedConnection.CLOSED_CONNECTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 284 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int concurrency) throws SQLException {
/* 291 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int concurrency, int holdability) throws SQLException {
/* 298 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 306 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int concurrency) throws SQLException {
/* 313 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
/* 320 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 327 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
/* 334 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, autoGeneratedKeys)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency) throws SQLException {
/* 341 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
/* 348 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency, holdability)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
/* 355 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, columnIndexes)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
/* 362 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(sql, columnNames)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 369 */     markCommitStateDirty();
/* 370 */     return ProxyFactory.getProxyDatabaseMetaData(this, this.delegate.getMetaData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() throws SQLException {
/* 377 */     this.delegate.commit();
/* 378 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {
/* 385 */     this.delegate.rollback();
/* 386 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {
/* 393 */     this.delegate.rollback(savepoint);
/* 394 */     this.isCommitStateDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean autoCommit) throws SQLException {
/* 401 */     this.delegate.setAutoCommit(autoCommit);
/* 402 */     this.isAutoCommit = autoCommit;
/* 403 */     this.dirtyBits |= 0x2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) throws SQLException {
/* 410 */     this.delegate.setReadOnly(readOnly);
/* 411 */     this.isReadOnly = readOnly;
/* 412 */     this.isCommitStateDirty = false;
/* 413 */     this.dirtyBits |= 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int level) throws SQLException {
/* 420 */     this.delegate.setTransactionIsolation(level);
/* 421 */     this.transactionIsolation = level;
/* 422 */     this.dirtyBits |= 0x4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(String catalog) throws SQLException {
/* 429 */     this.delegate.setCatalog(catalog);
/* 430 */     this.dbcatalog = catalog;
/* 431 */     this.dirtyBits |= 0x8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
/* 438 */     this.delegate.setNetworkTimeout(executor, milliseconds);
/* 439 */     this.networkTimeout = milliseconds;
/* 440 */     this.dirtyBits |= 0x10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) throws SQLException {
/* 447 */     this.delegate.setSchema(schema);
/* 448 */     this.dbschema = schema;
/* 449 */     this.dirtyBits |= 0x20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 456 */     return (iface.isInstance(this.delegate) || (this.delegate != null && this.delegate.isWrapperFor(iface)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> iface) throws SQLException {
/* 464 */     if (iface.isInstance(this.delegate)) {
/* 465 */       return (T)this.delegate;
/*     */     }
/* 467 */     if (this.delegate != null) {
/* 468 */       return this.delegate.unwrap(iface);
/*     */     }
/*     */     
/* 471 */     throw new SQLException("Wrapped connection is not an instance of " + iface);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ClosedConnection
/*     */   {
/* 480 */     static final Connection CLOSED_CONNECTION = getClosedConnection();
/*     */ 
/*     */     
/*     */     private static Connection getClosedConnection() {
/* 484 */       InvocationHandler handler = (proxy, method, args) -> {
/*     */           String methodName = method.getName();
/*     */           
/*     */           if ("isClosed".equals(methodName)) {
/*     */             return Boolean.TRUE;
/*     */           }
/*     */           
/*     */           if ("isValid".equals(methodName)) {
/*     */             return Boolean.FALSE;
/*     */           }
/*     */           if ("abort".equals(methodName)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("close".equals(methodName)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("toString".equals(methodName)) {
/*     */             return ClosedConnection.class.getCanonicalName();
/*     */           }
/*     */           throw new SQLException("Connection is closed");
/*     */         };
/* 505 */       return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\ProxyConnection.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */