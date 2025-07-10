/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.SQLExceptionOverride;
/*     */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.DriverDataSource;
/*     */ import com.zaxxer.hikari.util.PropertyElf;
/*     */ import com.zaxxer.hikari.util.UtilityElf;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransientConnectionException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
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
/*     */ abstract class PoolBase
/*     */ {
/*  53 */   private final Logger logger = LoggerFactory.getLogger(PoolBase.class);
/*     */   
/*     */   public final HikariConfig config;
/*     */   
/*     */   IMetricsTrackerDelegate metricsTracker;
/*     */   
/*     */   protected final String poolName;
/*     */   
/*     */   volatile String catalog;
/*     */   
/*     */   final AtomicReference<Exception> lastConnectionFailure;
/*     */   
/*     */   long connectionTimeout;
/*     */   long validationTimeout;
/*     */   SQLExceptionOverride exceptionOverride;
/*  68 */   private static final String[] RESET_STATES = new String[] { "readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema" };
/*     */   
/*     */   private static final int UNINITIALIZED = -1;
/*     */   
/*     */   private static final int TRUE = 1;
/*     */   
/*     */   private static final int FALSE = 0;
/*     */   
/*     */   private int networkTimeout;
/*     */   
/*     */   private int isNetworkTimeoutSupported;
/*     */   private int isQueryTimeoutSupported;
/*     */   private int defaultTransactionIsolation;
/*     */   private int transactionIsolation;
/*     */   private Executor netTimeoutExecutor;
/*     */   private DataSource dataSource;
/*     */   private final String schema;
/*     */   private final boolean isReadOnly;
/*     */   private final boolean isAutoCommit;
/*     */   private final boolean isUseJdbc4Validation;
/*     */   private final boolean isIsolateInternalQueries;
/*     */   private volatile boolean isValidChecked;
/*     */   
/*     */   PoolBase(HikariConfig config) {
/*  92 */     this.config = config;
/*     */     
/*  94 */     this.networkTimeout = -1;
/*  95 */     this.catalog = config.getCatalog();
/*  96 */     this.schema = config.getSchema();
/*  97 */     this.isReadOnly = config.isReadOnly();
/*  98 */     this.isAutoCommit = config.isAutoCommit();
/*  99 */     this.exceptionOverride = (SQLExceptionOverride)UtilityElf.createInstance(config.getExceptionOverrideClassName(), SQLExceptionOverride.class, new Object[0]);
/* 100 */     this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
/*     */     
/* 102 */     this.isQueryTimeoutSupported = -1;
/* 103 */     this.isNetworkTimeoutSupported = -1;
/* 104 */     this.isUseJdbc4Validation = (config.getConnectionTestQuery() == null);
/* 105 */     this.isIsolateInternalQueries = config.isIsolateInternalQueries();
/*     */     
/* 107 */     this.poolName = config.getPoolName();
/* 108 */     this.connectionTimeout = config.getConnectionTimeout();
/* 109 */     this.validationTimeout = config.getValidationTimeout();
/* 110 */     this.lastConnectionFailure = new AtomicReference<>();
/*     */     
/* 112 */     initializeDataSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     return this.poolName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void recycle(PoolEntry paramPoolEntry);
/*     */ 
/*     */ 
/*     */   
/*     */   void quietlyCloseConnection(Connection connection, String closureReason) {
/* 130 */     if (connection != null) {
/*     */       try {
/* 132 */         this.logger.debug("{} - Closing connection {}: {}", new Object[] { this.poolName, connection, closureReason });
/*     */ 
/*     */         
/* 135 */         try { Connection connection1 = connection; 
/* 136 */           try { setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
/* 137 */             if (connection1 != null) connection1.close();  } catch (Throwable throwable) { if (connection1 != null) try { connection1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException) {}
/*     */ 
/*     */       
/*     */       }
/* 141 */       catch (Exception e) {
/* 142 */         this.logger.debug("{} - Closing connection {} failed", new Object[] { this.poolName, connection, e });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConnectionDead(Connection connection) {
/*     */     try {
/*     */       
/* 151 */       try { setNetworkTimeout(connection, this.validationTimeout);
/*     */         
/* 153 */         int validationSeconds = (int)Math.max(1000L, this.validationTimeout) / 1000;
/*     */         
/* 155 */         if (this.isUseJdbc4Validation) {
/* 156 */           return !connection.isValid(validationSeconds);
/*     */         }
/*     */         
/* 159 */         Statement statement = connection.createStatement(); 
/* 160 */         try { if (this.isNetworkTimeoutSupported != 1) {
/* 161 */             setQueryTimeout(statement, validationSeconds);
/*     */           }
/*     */           
/* 164 */           statement.execute(this.config.getConnectionTestQuery());
/* 165 */           if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null)
/*     */             try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */          }
/* 168 */       finally { setNetworkTimeout(connection, this.networkTimeout);
/*     */         
/* 170 */         if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 171 */           connection.rollback();
/*     */         } }
/*     */ 
/*     */       
/* 175 */       return false;
/*     */     }
/* 177 */     catch (Exception e) {
/* 178 */       this.lastConnectionFailure.set(e);
/* 179 */       this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", new Object[] { this.poolName, connection, e
/* 180 */             .getMessage() });
/* 181 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Exception getLastConnectionFailure() {
/* 187 */     return this.lastConnectionFailure.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataSource getUnwrappedDataSource() {
/* 192 */     return this.dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PoolEntry newPoolEntry() throws Exception {
/* 201 */     return new PoolEntry(newConnection(), this, this.isReadOnly, this.isAutoCommit);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetConnectionState(Connection connection, ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
/* 206 */     int resetBits = 0;
/*     */     
/* 208 */     if ((dirtyBits & 0x1) != 0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
/* 209 */       connection.setReadOnly(this.isReadOnly);
/* 210 */       resetBits |= 0x1;
/*     */     } 
/*     */     
/* 213 */     if ((dirtyBits & 0x2) != 0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
/* 214 */       connection.setAutoCommit(this.isAutoCommit);
/* 215 */       resetBits |= 0x2;
/*     */     } 
/*     */     
/* 218 */     if ((dirtyBits & 0x4) != 0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
/* 219 */       connection.setTransactionIsolation(this.transactionIsolation);
/* 220 */       resetBits |= 0x4;
/*     */     } 
/*     */     
/* 223 */     if ((dirtyBits & 0x8) != 0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
/* 224 */       connection.setCatalog(this.catalog);
/* 225 */       resetBits |= 0x8;
/*     */     } 
/*     */     
/* 228 */     if ((dirtyBits & 0x10) != 0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
/* 229 */       setNetworkTimeout(connection, this.networkTimeout);
/* 230 */       resetBits |= 0x10;
/*     */     } 
/*     */     
/* 233 */     if ((dirtyBits & 0x20) != 0 && this.schema != null && !this.schema.equals(proxyConnection.getSchemaState())) {
/* 234 */       connection.setSchema(this.schema);
/* 235 */       resetBits |= 0x20;
/*     */     } 
/*     */     
/* 238 */     if (resetBits != 0 && this.logger.isDebugEnabled()) {
/* 239 */       this.logger.debug("{} - Reset ({}) on connection {}", new Object[] { this.poolName, stringFromResetBits(resetBits), connection });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void shutdownNetworkTimeoutExecutor() {
/* 245 */     if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
/* 246 */       ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   long getLoginTimeout() {
/*     */     try {
/* 253 */       return (this.dataSource != null) ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
/* 254 */     } catch (SQLException e) {
/* 255 */       return TimeUnit.SECONDS.toSeconds(5L);
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
/*     */ 
/*     */   
/*     */   void handleMBeans(HikariPool hikariPool, boolean register) {
/* 270 */     if (!this.config.isRegisterMbeans()) {
/*     */       return;
/*     */     }
/*     */     try {
/*     */       ObjectName beanConfigName, beanPoolName;
/* 275 */       MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
/*     */ 
/*     */       
/* 278 */       if ("true".equals(System.getProperty("hikaricp.jmx.register2.0"))) {
/* 279 */         beanConfigName = new ObjectName("com.zaxxer.hikari:type=PoolConfig,name=" + this.poolName);
/* 280 */         beanPoolName = new ObjectName("com.zaxxer.hikari:type=Pool,name=" + this.poolName);
/*     */       } else {
/* 282 */         beanConfigName = new ObjectName("com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
/* 283 */         beanPoolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
/*     */       } 
/* 285 */       if (register) {
/* 286 */         if (!mBeanServer.isRegistered(beanConfigName)) {
/* 287 */           mBeanServer.registerMBean(this.config, beanConfigName);
/* 288 */           mBeanServer.registerMBean(hikariPool, beanPoolName);
/*     */         } else {
/* 290 */           this.logger.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
/*     */         }
/*     */       
/* 293 */       } else if (mBeanServer.isRegistered(beanConfigName)) {
/* 294 */         mBeanServer.unregisterMBean(beanConfigName);
/* 295 */         mBeanServer.unregisterMBean(beanPoolName);
/*     */       }
/*     */     
/* 298 */     } catch (Exception e) {
/* 299 */       this.logger.warn("{} - Failed to {} management beans.", new Object[] { this.poolName, register ? "register" : "unregister", e });
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
/*     */   private void initializeDataSource() {
/* 312 */     String jdbcUrl = this.config.getJdbcUrl();
/* 313 */     String username = this.config.getUsername();
/* 314 */     String password = this.config.getPassword();
/* 315 */     String dsClassName = this.config.getDataSourceClassName();
/* 316 */     String driverClassName = this.config.getDriverClassName();
/* 317 */     String dataSourceJNDI = this.config.getDataSourceJNDI();
/* 318 */     Properties dataSourceProperties = this.config.getDataSourceProperties();
/*     */     
/* 320 */     DataSource dataSource = this.config.getDataSource();
/* 321 */     if (dsClassName != null && dataSource == null) {
/* 322 */       dataSource = (DataSource)UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
/* 323 */       PropertyElf.setTargetFromProperties(dataSource, dataSourceProperties);
/*     */     } else {
/* 325 */       DriverDataSource driverDataSource; if (jdbcUrl != null && dataSource == null) {
/* 326 */         driverDataSource = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
/*     */       }
/* 328 */       else if (dataSourceJNDI != null && driverDataSource == null) {
/*     */         try {
/* 330 */           InitialContext ic = new InitialContext();
/* 331 */           dataSource = (DataSource)ic.lookup(dataSourceJNDI);
/* 332 */         } catch (NamingException e) {
/* 333 */           throw new HikariPool.PoolInitializationException(e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 337 */     if (dataSource != null) {
/* 338 */       setLoginTimeout(dataSource);
/* 339 */       createNetworkTimeoutExecutor(dataSource, dsClassName, jdbcUrl);
/*     */     } 
/*     */     
/* 342 */     this.dataSource = dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection newConnection() throws Exception {
/* 352 */     long start = ClockSource.currentTime();
/*     */     
/* 354 */     Connection connection = null;
/*     */     try {
/* 356 */       String username = this.config.getUsername();
/* 357 */       String password = this.config.getPassword();
/*     */       
/* 359 */       connection = (username == null) ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password);
/* 360 */       if (connection == null) {
/* 361 */         throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
/*     */       }
/*     */       
/* 364 */       setupConnection(connection);
/* 365 */       this.lastConnectionFailure.set(null);
/* 366 */       return connection;
/*     */     }
/* 368 */     catch (Exception e) {
/* 369 */       if (connection != null) {
/* 370 */         quietlyCloseConnection(connection, "(Failed to create/setup connection)");
/*     */       }
/* 372 */       else if (getLastConnectionFailure() == null) {
/* 373 */         this.logger.debug("{} - Failed to create/setup connection: {}", this.poolName, e.getMessage());
/*     */       } 
/*     */       
/* 376 */       this.lastConnectionFailure.set(e);
/* 377 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 381 */       if (this.metricsTracker != null) {
/* 382 */         this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
/*     */       }
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
/*     */   private void setupConnection(Connection connection) throws ConnectionSetupException {
/*     */     try {
/* 396 */       if (this.networkTimeout == -1) {
/* 397 */         this.networkTimeout = getAndSetNetworkTimeout(connection, this.validationTimeout);
/*     */       } else {
/*     */         
/* 400 */         setNetworkTimeout(connection, this.validationTimeout);
/*     */       } 
/*     */       
/* 403 */       if (connection.isReadOnly() != this.isReadOnly) {
/* 404 */         connection.setReadOnly(this.isReadOnly);
/*     */       }
/*     */       
/* 407 */       if (connection.getAutoCommit() != this.isAutoCommit) {
/* 408 */         connection.setAutoCommit(this.isAutoCommit);
/*     */       }
/*     */       
/* 411 */       checkDriverSupport(connection);
/*     */       
/* 413 */       if (this.transactionIsolation != this.defaultTransactionIsolation) {
/* 414 */         connection.setTransactionIsolation(this.transactionIsolation);
/*     */       }
/*     */       
/* 417 */       if (this.catalog != null) {
/* 418 */         connection.setCatalog(this.catalog);
/*     */       }
/*     */       
/* 421 */       if (this.schema != null) {
/* 422 */         connection.setSchema(this.schema);
/*     */       }
/*     */       
/* 425 */       executeSql(connection, this.config.getConnectionInitSql(), true);
/*     */       
/* 427 */       setNetworkTimeout(connection, this.networkTimeout);
/*     */     }
/* 429 */     catch (SQLException e) {
/* 430 */       throw new ConnectionSetupException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDriverSupport(Connection connection) throws SQLException {
/* 441 */     if (!this.isValidChecked) {
/* 442 */       checkValidationSupport(connection);
/* 443 */       checkDefaultIsolation(connection);
/*     */       
/* 445 */       this.isValidChecked = true;
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
/*     */   private void checkValidationSupport(Connection connection) throws SQLException {
/*     */     try {
/* 458 */       if (this.isUseJdbc4Validation) {
/* 459 */         connection.isValid(1);
/*     */       } else {
/*     */         
/* 462 */         executeSql(connection, this.config.getConnectionTestQuery(), false);
/*     */       }
/*     */     
/* 465 */     } catch (Exception|AbstractMethodError e) {
/* 466 */       this.logger.error("{} - Failed to execute{} connection test query ({}).", new Object[] { this.poolName, this.isUseJdbc4Validation ? " isValid() for connection, configure" : "", e.getMessage() });
/* 467 */       throw e;
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
/*     */   private void checkDefaultIsolation(Connection connection) throws SQLException {
/*     */     try {
/* 480 */       this.defaultTransactionIsolation = connection.getTransactionIsolation();
/* 481 */       if (this.transactionIsolation == -1) {
/* 482 */         this.transactionIsolation = this.defaultTransactionIsolation;
/*     */       }
/*     */     }
/* 485 */     catch (SQLException e) {
/* 486 */       this.logger.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, e.getMessage());
/* 487 */       if (e.getSQLState() != null && !e.getSQLState().startsWith("08")) {
/* 488 */         throw e;
/*     */       }
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
/*     */   private void setQueryTimeout(Statement statement, int timeoutSec) {
/* 501 */     if (this.isQueryTimeoutSupported != 0) {
/*     */       try {
/* 503 */         statement.setQueryTimeout(timeoutSec);
/* 504 */         this.isQueryTimeoutSupported = 1;
/*     */       }
/* 506 */       catch (Exception e) {
/* 507 */         if (this.isQueryTimeoutSupported == -1) {
/* 508 */           this.isQueryTimeoutSupported = 0;
/* 509 */           this.logger.info("{} - Failed to set query timeout for statement. ({})", this.poolName, e.getMessage());
/*     */         } 
/*     */       } 
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
/*     */   
/*     */   private int getAndSetNetworkTimeout(Connection connection, long timeoutMs) {
/* 525 */     if (this.isNetworkTimeoutSupported != 0) {
/*     */       try {
/* 527 */         int originalTimeout = connection.getNetworkTimeout();
/* 528 */         connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
/* 529 */         this.isNetworkTimeoutSupported = 1;
/* 530 */         return originalTimeout;
/*     */       }
/* 532 */       catch (Exception|AbstractMethodError e) {
/* 533 */         if (this.isNetworkTimeoutSupported == -1) {
/* 534 */           this.isNetworkTimeoutSupported = 0;
/*     */           
/* 536 */           this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, e.getMessage());
/* 537 */           if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
/* 538 */             this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           }
/* 540 */           else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
/* 541 */             this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 547 */     return 0;
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
/*     */   
/*     */   private void setNetworkTimeout(Connection connection, long timeoutMs) throws SQLException {
/* 560 */     if (this.isNetworkTimeoutSupported == 1) {
/* 561 */       connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
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
/*     */   
/*     */   private void executeSql(Connection connection, String sql, boolean isCommit) throws SQLException {
/* 575 */     if (sql != null) {
/* 576 */       Statement statement = connection.createStatement();
/*     */       
/* 578 */       try { statement.execute(sql);
/* 579 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null)
/*     */           try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 581 */        if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 582 */         if (isCommit) {
/* 583 */           connection.commit();
/*     */         } else {
/*     */           
/* 586 */           connection.rollback();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createNetworkTimeoutExecutor(DataSource dataSource, String dsClassName, String jdbcUrl) {
/* 595 */     if ((dsClassName != null && dsClassName.contains("Mysql")) || (jdbcUrl != null && jdbcUrl
/* 596 */       .contains("mysql")) || (dataSource != null && dataSource
/* 597 */       .getClass().getName().contains("Mysql"))) {
/* 598 */       this.netTimeoutExecutor = new SynchronousExecutor();
/*     */     } else {
/*     */       
/* 601 */       ThreadFactory threadFactory = this.config.getThreadFactory();
/* 602 */       threadFactory = (threadFactory != null) ? threadFactory : (ThreadFactory)new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true);
/* 603 */       ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
/* 604 */       executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
/* 605 */       executor.allowCoreThreadTimeOut(true);
/* 606 */       this.netTimeoutExecutor = executor;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLoginTimeout(DataSource dataSource) {
/* 617 */     if (this.connectionTimeout != 2147483647L) {
/*     */       try {
/* 619 */         dataSource.setLoginTimeout(Math.max(1, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
/*     */       }
/* 621 */       catch (Exception e) {
/* 622 */         this.logger.info("{} - Failed to set login timeout for data source. ({})", this.poolName, e.getMessage());
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   private String stringFromResetBits(int bits) {
/* 639 */     StringBuilder sb = new StringBuilder();
/* 640 */     for (int ndx = 0; ndx < RESET_STATES.length; ndx++) {
/* 641 */       if ((bits & 1 << ndx) != 0) {
/* 642 */         sb.append(RESET_STATES[ndx]).append(", ");
/*     */       }
/*     */     } 
/*     */     
/* 646 */     sb.setLength(sb.length() - 2);
/* 647 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConnectionSetupException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916521L;
/*     */ 
/*     */ 
/*     */     
/*     */     ConnectionSetupException(Throwable t) {
/* 660 */       super(t);
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
/*     */   private static class SynchronousExecutor
/*     */     implements Executor
/*     */   {
/*     */     public void execute(Runnable command) {
/*     */       try {
/* 676 */         command.run();
/*     */       }
/* 678 */       catch (Exception t) {
/* 679 */         LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", command, t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface IMetricsTrackerDelegate
/*     */     extends AutoCloseable
/*     */   {
/*     */     default void recordConnectionUsage(PoolEntry poolEntry) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionCreated(long connectionCreatedMillis) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowTimeoutStats(long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowStats(PoolEntry poolEntry, long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionTimeout() {}
/*     */     
/*     */     default void close() {}
/*     */   }
/*     */   
/*     */   static class MetricsTrackerDelegate
/*     */     implements IMetricsTrackerDelegate
/*     */   {
/*     */     final IMetricsTracker tracker;
/*     */     
/*     */     MetricsTrackerDelegate(IMetricsTracker tracker) {
/* 711 */       this.tracker = tracker;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionUsage(PoolEntry poolEntry) {
/* 717 */       this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionCreated(long connectionCreatedMillis) {
/* 723 */       this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowTimeoutStats(long startTime) {
/* 729 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
/* 735 */       long now = ClockSource.currentTime();
/* 736 */       poolEntry.lastBorrowed = now;
/* 737 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordConnectionTimeout() {
/* 742 */       this.tracker.recordConnectionTimeout();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 748 */       this.tracker.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate {}
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\PoolBase.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */