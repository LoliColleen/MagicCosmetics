/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.ConcurrentBag;
/*     */ import com.zaxxer.hikari.util.FastList;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ final class PoolEntry
/*     */   implements ConcurrentBag.IConcurrentBagEntry
/*     */ {
/*  39 */   private static final Logger LOGGER = LoggerFactory.getLogger(PoolEntry.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private volatile int state = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   Connection connection;
/*     */ 
/*     */ 
/*     */   
/*     */   long lastAccessed;
/*     */ 
/*     */ 
/*     */   
/*     */   long lastBorrowed;
/*     */ 
/*     */   
/*  61 */   private static final AtomicIntegerFieldUpdater<PoolEntry> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(PoolEntry.class, "state");
/*     */   private volatile boolean evict;
/*     */   private volatile ScheduledFuture<?> endOfLife;
/*     */   
/*     */   PoolEntry(Connection connection, PoolBase pool, boolean isReadOnly, boolean isAutoCommit) {
/*  66 */     this.connection = connection;
/*  67 */     this.hikariPool = (HikariPool)pool;
/*  68 */     this.isReadOnly = isReadOnly;
/*  69 */     this.isAutoCommit = isAutoCommit;
/*  70 */     this.lastAccessed = ClockSource.currentTime();
/*  71 */     this.openStatements = new FastList(Statement.class, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   private volatile ScheduledFuture<?> keepalive;
/*     */   private final FastList<Statement> openStatements;
/*     */   
/*     */   void recycle() {
/*  79 */     if (this.connection != null) {
/*  80 */       this.lastAccessed = ClockSource.currentTime();
/*  81 */       this.hikariPool.recycle(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final HikariPool hikariPool;
/*     */   
/*     */   private final boolean isReadOnly;
/*     */   private final boolean isAutoCommit;
/*     */   
/*     */   void setFutureEol(ScheduledFuture<?> endOfLife) {
/*  92 */     this.endOfLife = endOfLife;
/*     */   }
/*     */   
/*     */   public void setKeepalive(ScheduledFuture<?> keepalive) {
/*  96 */     this.keepalive = keepalive;
/*     */   }
/*     */ 
/*     */   
/*     */   Connection createProxyConnection(ProxyLeakTask leakTask) {
/* 101 */     return ProxyFactory.getProxyConnection(this, this.connection, this.openStatements, leakTask, this.isReadOnly, this.isAutoCommit);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetConnectionState(ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
/* 106 */     this.hikariPool.resetConnectionState(this.connection, proxyConnection, dirtyBits);
/*     */   }
/*     */ 
/*     */   
/*     */   String getPoolName() {
/* 111 */     return this.hikariPool.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isMarkedEvicted() {
/* 116 */     return this.evict;
/*     */   }
/*     */ 
/*     */   
/*     */   void markEvicted() {
/* 121 */     this.evict = true;
/*     */   }
/*     */ 
/*     */   
/*     */   void evict(String closureReason) {
/* 126 */     this.hikariPool.closeConnection(this, closureReason);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long getMillisSinceBorrowed() {
/* 132 */     return ClockSource.elapsedMillis(this.lastBorrowed);
/*     */   }
/*     */ 
/*     */   
/*     */   PoolBase getPoolBase() {
/* 137 */     return this.hikariPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     long now = ClockSource.currentTime();
/* 145 */     return "" + this.connection + ", accessed " + this.connection + " ago, " + 
/* 146 */       ClockSource.elapsedDisplayString(this.lastAccessed, now);
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
/*     */   public int getState() {
/* 158 */     return stateUpdater.get(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compareAndSet(int expect, int update) {
/* 165 */     return stateUpdater.compareAndSet(this, expect, update);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(int update) {
/* 172 */     stateUpdater.set(this, update);
/*     */   }
/*     */ 
/*     */   
/*     */   Connection close() {
/* 177 */     ScheduledFuture<?> eol = this.endOfLife;
/* 178 */     if (eol != null && !eol.isDone() && !eol.cancel(false)) {
/* 179 */       LOGGER.warn("{} - maxLifeTime expiration task cancellation unexpectedly returned false for connection {}", getPoolName(), this.connection);
/*     */     }
/*     */     
/* 182 */     ScheduledFuture<?> ka = this.keepalive;
/* 183 */     if (ka != null && !ka.isDone() && !ka.cancel(false)) {
/* 184 */       LOGGER.warn("{} - keepalive task cancellation unexpectedly returned false for connection {}", getPoolName(), this.connection);
/*     */     }
/*     */     
/* 187 */     Connection con = this.connection;
/* 188 */     this.connection = null;
/* 189 */     this.endOfLife = null;
/* 190 */     this.keepalive = null;
/* 191 */     return con;
/*     */   }
/*     */ 
/*     */   
/*     */   private String stateToString() {
/* 196 */     switch (this.state) {
/*     */       case 1:
/* 198 */         return "IN_USE";
/*     */       case 0:
/* 200 */         return "NOT_IN_USE";
/*     */       case -1:
/* 202 */         return "REMOVED";
/*     */       case -2:
/* 204 */         return "RESERVED";
/*     */     } 
/* 206 */     return "Invalid";
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\PoolEntry.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */