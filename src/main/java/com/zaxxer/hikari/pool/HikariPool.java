/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.HikariPoolMXBean;
/*     */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.metrics.PoolStats;
/*     */ import com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
/*     */ import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.ConcurrentBag;
/*     */ import com.zaxxer.hikari.util.SuspendResumeLock;
/*     */ import com.zaxxer.hikari.util.UtilityElf;
/*     */ import io.micrometer.core.instrument.MeterRegistry;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransientConnectionException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public final class HikariPool
/*     */   extends PoolBase
/*     */   implements HikariPoolMXBean, ConcurrentBag.IBagStateListener
/*     */ {
/*  58 */   private final Logger logger = LoggerFactory.getLogger(HikariPool.class);
/*     */   
/*     */   public static final int POOL_NORMAL = 0;
/*     */   
/*     */   public static final int POOL_SUSPENDED = 1;
/*     */   
/*     */   public static final int POOL_SHUTDOWN = 2;
/*     */   public volatile int poolState;
/*  66 */   private final long aliveBypassWindowMs = Long.getLong("com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L)).longValue();
/*  67 */   private final long housekeepingPeriodMs = Long.getLong("com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L)).longValue();
/*     */   
/*     */   private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
/*     */   
/*     */   private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
/*  72 */   private final PoolEntryCreator poolEntryCreator = new PoolEntryCreator();
/*  73 */   private final PoolEntryCreator postFillPoolEntryCreator = new PoolEntryCreator("After adding ");
/*  74 */   private final AtomicInteger addConnectionQueueDepth = new AtomicInteger();
/*     */ 
/*     */   
/*     */   private final ThreadPoolExecutor addConnectionExecutor;
/*     */   
/*     */   private final ThreadPoolExecutor closeConnectionExecutor;
/*     */   
/*     */   private final ConcurrentBag<PoolEntry> connectionBag;
/*     */   
/*     */   private final ProxyLeakTaskFactory leakTaskFactory;
/*     */   
/*     */   private final SuspendResumeLock suspendResumeLock;
/*     */   
/*     */   private final ScheduledExecutorService houseKeepingExecutorService;
/*     */   
/*     */   private ScheduledFuture<?> houseKeeperTask;
/*     */ 
/*     */   
/*     */   public HikariPool(HikariConfig config) {
/*  93 */     super(config);
/*     */     
/*  95 */     this.connectionBag = new ConcurrentBag(this);
/*  96 */     this.suspendResumeLock = config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;
/*     */     
/*  98 */     this.houseKeepingExecutorService = initializeHouseKeepingExecutorService();
/*     */     
/* 100 */     checkFailFast();
/*     */     
/* 102 */     if (config.getMetricsTrackerFactory() != null) {
/* 103 */       setMetricsTrackerFactory(config.getMetricsTrackerFactory());
/*     */     } else {
/*     */       
/* 106 */       setMetricRegistry(config.getMetricRegistry());
/*     */     } 
/*     */     
/* 109 */     setHealthCheckRegistry(config.getHealthCheckRegistry());
/*     */     
/* 111 */     handleMBeans(this, true);
/*     */     
/* 113 */     ThreadFactory threadFactory = config.getThreadFactory();
/*     */     
/* 115 */     int maxPoolSize = config.getMaximumPoolSize();
/* 116 */     LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(16);
/* 117 */     this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(addConnectionQueue, this.poolName + " connection adder", threadFactory, new CustomDiscardPolicy());
/* 118 */     this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(maxPoolSize, this.poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
/*     */     
/* 120 */     this.leakTaskFactory = new ProxyLeakTaskFactory(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
/*     */     
/* 122 */     this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, this.housekeepingPeriodMs, TimeUnit.MILLISECONDS);
/*     */     
/* 124 */     if (Boolean.getBoolean("com.zaxxer.hikari.blockUntilFilled") && config.getInitializationFailTimeout() > 1L) {
/* 125 */       this.addConnectionExecutor.setMaximumPoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
/* 126 */       this.addConnectionExecutor.setCorePoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
/*     */       
/* 128 */       long startTime = ClockSource.currentTime();
/* 129 */       while (ClockSource.elapsedMillis(startTime) < config.getInitializationFailTimeout() && getTotalConnections() < config.getMinimumIdle()) {
/* 130 */         UtilityElf.quietlySleep(TimeUnit.MILLISECONDS.toMillis(100L));
/*     */       }
/*     */       
/* 133 */       this.addConnectionExecutor.setCorePoolSize(1);
/* 134 */       this.addConnectionExecutor.setMaximumPoolSize(1);
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
/*     */   public Connection getConnection() throws SQLException {
/* 146 */     return getConnection(this.connectionTimeout);
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
/*     */   public Connection getConnection(long hardTimeout) throws SQLException {
/* 158 */     this.suspendResumeLock.acquire();
/* 159 */     long startTime = ClockSource.currentTime();
/*     */     
/*     */     try {
/* 162 */       long timeout = hardTimeout;
/*     */       do {
/* 164 */         PoolEntry poolEntry = (PoolEntry)this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
/* 165 */         if (poolEntry == null) {
/*     */           break;
/*     */         }
/*     */         
/* 169 */         long now = ClockSource.currentTime();
/* 170 */         if (poolEntry.isMarkedEvicted() || (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) > this.aliveBypassWindowMs && isConnectionDead(poolEntry.connection))) {
/* 171 */           closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? "(connection was evicted)" : "(connection is dead)");
/* 172 */           timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
/*     */         } else {
/*     */           
/* 175 */           this.metricsTracker.recordBorrowStats(poolEntry, startTime);
/* 176 */           return poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry));
/*     */         } 
/* 178 */       } while (timeout > 0L);
/*     */       
/* 180 */       this.metricsTracker.recordBorrowTimeoutStats(startTime);
/* 181 */       throw createTimeoutException(startTime);
/*     */     }
/* 183 */     catch (InterruptedException e) {
/* 184 */       Thread.currentThread().interrupt();
/* 185 */       throw new SQLException(this.poolName + " - Interrupted during connection acquisition", e);
/*     */     } finally {
/*     */       
/* 188 */       this.suspendResumeLock.release();
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
/*     */   public synchronized void shutdown() throws InterruptedException {
/*     */     try {
/* 201 */       this.poolState = 2;
/*     */       
/* 203 */       if (this.addConnectionExecutor == null) {
/*     */         return;
/*     */       }
/*     */       
/* 207 */       logPoolState(new String[] { "Before shutdown " });
/*     */       
/* 209 */       if (this.houseKeeperTask != null) {
/* 210 */         this.houseKeeperTask.cancel(false);
/* 211 */         this.houseKeeperTask = null;
/*     */       } 
/*     */       
/* 214 */       softEvictConnections();
/*     */       
/* 216 */       this.addConnectionExecutor.shutdown();
/* 217 */       if (!this.addConnectionExecutor.awaitTermination(getLoginTimeout(), TimeUnit.SECONDS)) {
/* 218 */         this.logger.warn("Timed-out waiting for add connection executor to shutdown");
/*     */       }
/*     */       
/* 221 */       destroyHouseKeepingExecutorService();
/*     */       
/* 223 */       this.connectionBag.close();
/*     */       
/* 225 */       ThreadPoolExecutor assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config
/* 226 */           .getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
/*     */       try {
/* 228 */         long start = ClockSource.currentTime();
/*     */         do {
/* 230 */           abortActiveConnections(assassinExecutor);
/* 231 */           softEvictConnections();
/* 232 */         } while (getTotalConnections() > 0 && ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(10L));
/*     */       } finally {
/*     */         
/* 235 */         assassinExecutor.shutdown();
/* 236 */         if (!assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
/* 237 */           this.logger.warn("Timed-out waiting for connection assassin to shutdown");
/*     */         }
/*     */       } 
/*     */       
/* 241 */       shutdownNetworkTimeoutExecutor();
/* 242 */       this.closeConnectionExecutor.shutdown();
/* 243 */       if (!this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
/* 244 */         this.logger.warn("Timed-out waiting for close connection executor to shutdown");
/*     */       }
/*     */     } finally {
/*     */       
/* 248 */       logPoolState(new String[] { "After shutdown " });
/* 249 */       handleMBeans(this, false);
/* 250 */       this.metricsTracker.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evictConnection(Connection connection) {
/* 261 */     ProxyConnection proxyConnection = (ProxyConnection)connection;
/* 262 */     proxyConnection.cancelLeakTask();
/*     */     
/*     */     try {
/* 265 */       softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
/*     */     }
/* 267 */     catch (SQLException sQLException) {}
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
/*     */   public void setMetricRegistry(Object metricRegistry) {
/* 281 */     if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry")) {
/* 282 */       setMetricsTrackerFactory((MetricsTrackerFactory)new CodahaleMetricsTrackerFactory((MetricRegistry)metricRegistry));
/*     */     }
/* 284 */     else if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
/* 285 */       setMetricsTrackerFactory((MetricsTrackerFactory)new MicrometerMetricsTrackerFactory((MeterRegistry)metricRegistry));
/*     */     } else {
/*     */       
/* 288 */       setMetricsTrackerFactory((MetricsTrackerFactory)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
/* 299 */     if (metricsTrackerFactory != null) {
/* 300 */       this.metricsTracker = new PoolBase.MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), getPoolStats()));
/*     */     } else {
/*     */       
/* 303 */       this.metricsTracker = new PoolBase.NopMetricsTrackerDelegate();
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
/*     */   public void setHealthCheckRegistry(Object healthCheckRegistry) {
/* 315 */     if (healthCheckRegistry != null) {
/* 316 */       CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)healthCheckRegistry);
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
/*     */   public void addBagItem(int waiting) {
/* 328 */     int queueDepth = this.addConnectionQueueDepth.get();
/* 329 */     int countToAdd = waiting - queueDepth;
/* 330 */     if (countToAdd >= 0) {
/* 331 */       this.addConnectionQueueDepth.incrementAndGet();
/* 332 */       this.addConnectionExecutor.submit(this.poolEntryCreator);
/*     */     } else {
/*     */       
/* 335 */       this.logger.debug("{} - Add connection elided, waiting={}, adders pending/running={}", new Object[] { this.poolName, Integer.valueOf(waiting), Integer.valueOf(queueDepth) });
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
/*     */   public int getActiveConnections() {
/* 347 */     return this.connectionBag.getCount(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIdleConnections() {
/* 354 */     return this.connectionBag.getCount(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalConnections() {
/* 361 */     return this.connectionBag.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreadsAwaitingConnection() {
/* 368 */     return this.connectionBag.getWaitingThreadCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void softEvictConnections() {
/* 375 */     this.connectionBag.values().forEach(poolEntry -> softEvictConnection(poolEntry, "(connection evicted)", false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void suspendPool() {
/* 382 */     if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
/* 383 */       throw new IllegalStateException(this.poolName + " - is not suspendable");
/*     */     }
/* 385 */     if (this.poolState != 1) {
/* 386 */       this.suspendResumeLock.suspend();
/* 387 */       this.poolState = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resumePool() {
/* 395 */     if (this.poolState == 1) {
/* 396 */       this.poolState = 0;
/* 397 */       fillPool(false);
/* 398 */       this.suspendResumeLock.resume();
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
/*     */   void logPoolState(String... prefix) {
/* 413 */     if (this.logger.isDebugEnabled()) {
/* 414 */       this.logger.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", new Object[] { this.poolName, 
/* 415 */             (prefix.length > 0) ? prefix[0] : "", 
/* 416 */             Integer.valueOf(getTotalConnections()), Integer.valueOf(getActiveConnections()), Integer.valueOf(getIdleConnections()), Integer.valueOf(getThreadsAwaitingConnection()) });
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
/*     */   void recycle(PoolEntry poolEntry) {
/* 428 */     this.metricsTracker.recordConnectionUsage(poolEntry);
/*     */     
/* 430 */     this.connectionBag.requite(poolEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeConnection(PoolEntry poolEntry, String closureReason) {
/* 441 */     if (this.connectionBag.remove(poolEntry)) {
/* 442 */       Connection connection = poolEntry.close();
/* 443 */       this.closeConnectionExecutor.execute(() -> {
/*     */             quietlyCloseConnection(connection, closureReason);
/*     */             if (this.poolState == 0) {
/*     */               fillPool(false);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int[] getPoolStateCounts() {
/* 455 */     return this.connectionBag.getStateCounts();
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
/*     */   private PoolEntry createPoolEntry() {
/*     */     try {
/* 470 */       PoolEntry poolEntry = newPoolEntry();
/*     */       
/* 472 */       long maxLifetime = this.config.getMaxLifetime();
/* 473 */       if (maxLifetime > 0L) {
/*     */         
/* 475 */         long variance = (maxLifetime > 10000L) ? ThreadLocalRandom.current().nextLong(maxLifetime / 40L) : 0L;
/* 476 */         long lifetime = maxLifetime - variance;
/* 477 */         poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(new MaxLifetimeTask(poolEntry), lifetime, TimeUnit.MILLISECONDS));
/*     */       } 
/*     */       
/* 480 */       long keepaliveTime = this.config.getKeepaliveTime();
/* 481 */       if (keepaliveTime > 0L) {
/*     */         
/* 483 */         long variance = ThreadLocalRandom.current().nextLong(keepaliveTime / 10L);
/* 484 */         long heartbeatTime = keepaliveTime - variance;
/* 485 */         poolEntry.setKeepalive(this.houseKeepingExecutorService.scheduleWithFixedDelay(new KeepaliveTask(poolEntry), heartbeatTime, heartbeatTime, TimeUnit.MILLISECONDS));
/*     */       } 
/*     */       
/* 488 */       return poolEntry;
/*     */     }
/* 490 */     catch (ConnectionSetupException e) {
/* 491 */       if (this.poolState == 0) {
/* 492 */         this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, e.getCause());
/* 493 */         this.lastConnectionFailure.set(e);
/*     */       }
/*     */     
/* 496 */     } catch (Exception e) {
/* 497 */       if (this.poolState == 0) {
/* 498 */         this.logger.debug("{} - Cannot acquire connection from data source", this.poolName, e);
/*     */       }
/*     */     } 
/*     */     
/* 502 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void fillPool(boolean isAfterAdd) {
/* 510 */     int queueDepth = this.addConnectionQueueDepth.get();
/* 511 */     int countToAdd = this.connectionBag.getWaitingThreadCount() - queueDepth;
/*     */ 
/*     */     
/* 514 */     boolean shouldAdd = (getTotalConnections() < this.config.getMaximumPoolSize() && (getIdleConnections() < this.config.getMinimumIdle() || countToAdd > getIdleConnections()));
/*     */     
/* 516 */     if (shouldAdd) {
/* 517 */       this.addConnectionQueueDepth.incrementAndGet();
/* 518 */       this.addConnectionExecutor.submit(isAfterAdd ? this.postFillPoolEntryCreator : this.poolEntryCreator);
/*     */     }
/* 520 */     else if (isAfterAdd) {
/* 521 */       this.logger.debug("{} - Fill pool skipped, pool has sufficient level or currently being filled (queueDepth={}).", this.poolName, Integer.valueOf(queueDepth));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abortActiveConnections(ExecutorService assassinExecutor) {
/* 532 */     for (PoolEntry poolEntry : this.connectionBag.values(1)) {
/* 533 */       Connection connection = poolEntry.close();
/*     */       try {
/* 535 */         connection.abort(assassinExecutor);
/*     */       }
/* 537 */       catch (Throwable e) {
/* 538 */         quietlyCloseConnection(connection, "(connection aborted during shutdown)");
/*     */       } finally {
/*     */         
/* 541 */         this.connectionBag.remove(poolEntry);
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
/*     */   private void checkFailFast() {
/* 554 */     long initializationTimeout = this.config.getInitializationFailTimeout();
/* 555 */     if (initializationTimeout < 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 559 */     long startTime = ClockSource.currentTime();
/*     */     do {
/* 561 */       PoolEntry poolEntry = createPoolEntry();
/* 562 */       if (poolEntry != null) {
/* 563 */         if (this.config.getMinimumIdle() > 0) {
/* 564 */           this.connectionBag.add(poolEntry);
/* 565 */           this.logger.info("{} - Added connection {}", this.poolName, poolEntry.connection);
/*     */         } else {
/*     */           
/* 568 */           quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 574 */       if (getLastConnectionFailure() instanceof PoolBase.ConnectionSetupException) {
/* 575 */         throwPoolInitializationException(getLastConnectionFailure().getCause());
/*     */       }
/*     */       
/* 578 */       UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
/* 579 */     } while (ClockSource.elapsedMillis(startTime) < initializationTimeout);
/*     */     
/* 581 */     if (initializationTimeout > 0L) {
/* 582 */       throwPoolInitializationException(getLastConnectionFailure());
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
/*     */   private void throwPoolInitializationException(Throwable t) {
/* 594 */     this.logger.error("{} - Exception during pool initialization.", this.poolName, t);
/* 595 */     destroyHouseKeepingExecutorService();
/* 596 */     throw new PoolInitializationException(t);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean softEvictConnection(PoolEntry poolEntry, String reason, boolean owner) {
/* 614 */     poolEntry.markEvicted();
/* 615 */     if (owner || this.connectionBag.reserve(poolEntry)) {
/* 616 */       closeConnection(poolEntry, reason);
/* 617 */       return true;
/*     */     } 
/*     */     
/* 620 */     return false;
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
/*     */   private ScheduledExecutorService initializeHouseKeepingExecutorService() {
/* 632 */     if (this.config.getScheduledExecutor() == null) {
/* 633 */       ThreadFactory threadFactory = Optional.<ThreadFactory>ofNullable(this.config.getThreadFactory()).orElseGet(() -> new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper", true));
/* 634 */       ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
/* 635 */       executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 636 */       executor.setRemoveOnCancelPolicy(true);
/* 637 */       return executor;
/*     */     } 
/*     */     
/* 640 */     return this.config.getScheduledExecutor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroyHouseKeepingExecutorService() {
/* 649 */     if (this.config.getScheduledExecutor() == null) {
/* 650 */       this.houseKeepingExecutorService.shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PoolStats getPoolStats() {
/* 661 */     return new PoolStats(TimeUnit.SECONDS.toMillis(1L))
/*     */       {
/*     */         protected void update() {
/* 664 */           this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
/* 665 */           this.idleConnections = HikariPool.this.getIdleConnections();
/* 666 */           this.totalConnections = HikariPool.this.getTotalConnections();
/* 667 */           this.activeConnections = HikariPool.this.getActiveConnections();
/* 668 */           this.maxConnections = HikariPool.this.config.getMaximumPoolSize();
/* 669 */           this.minConnections = HikariPool.this.config.getMinimumIdle();
/*     */         }
/*     */       };
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
/*     */ 
/*     */   
/*     */   private SQLException createTimeoutException(long startTime) {
/* 688 */     logPoolState(new String[] { "Timeout failure " });
/* 689 */     this.metricsTracker.recordConnectionTimeout();
/*     */     
/* 691 */     String sqlState = null;
/* 692 */     Exception originalException = getLastConnectionFailure();
/* 693 */     if (originalException instanceof SQLException) {
/* 694 */       sqlState = ((SQLException)originalException).getSQLState();
/*     */     }
/* 696 */     SQLTransientConnectionException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + this.poolName + "ms.", sqlState, originalException);
/* 697 */     if (originalException instanceof SQLException) {
/* 698 */       connectionException.setNextException((SQLException)originalException);
/*     */     }
/*     */     
/* 701 */     return connectionException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class PoolEntryCreator
/*     */     implements Callable<Boolean>
/*     */   {
/*     */     private final String loggingPrefix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PoolEntryCreator() {
/* 718 */       this(null);
/*     */     }
/*     */ 
/*     */     
/*     */     PoolEntryCreator(String loggingPrefix) {
/* 723 */       this.loggingPrefix = loggingPrefix;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean call() {
/* 729 */       long backoffMs = 10L;
/* 730 */       boolean added = false;
/*     */       try {
/* 732 */         while (shouldContinueCreating()) {
/* 733 */           PoolEntry poolEntry = HikariPool.this.createPoolEntry();
/* 734 */           if (poolEntry != null) {
/* 735 */             added = true;
/* 736 */             backoffMs = 10L;
/* 737 */             HikariPool.this.connectionBag.add(poolEntry);
/* 738 */             HikariPool.this.logger.debug("{} - Added connection {}", HikariPool.this.poolName, poolEntry.connection);
/*     */           } else {
/* 740 */             backoffMs = Math.min(TimeUnit.SECONDS.toMillis(5L), backoffMs * 2L);
/* 741 */             if (this.loggingPrefix != null) {
/* 742 */               HikariPool.this.logger.debug("{} - Connection add failed, sleeping with backoff: {}ms", HikariPool.this.poolName, Long.valueOf(backoffMs));
/*     */             }
/*     */           } 
/* 745 */           UtilityElf.quietlySleep(backoffMs);
/*     */         } 
/*     */       } finally {
/*     */         
/* 749 */         HikariPool.this.addConnectionQueueDepth.decrementAndGet();
/* 750 */         if (added && this.loggingPrefix != null) HikariPool.this.logPoolState(new String[] { this.loggingPrefix });
/*     */       
/*     */       } 
/*     */       
/* 754 */       return Boolean.FALSE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private synchronized boolean shouldContinueCreating() {
/* 764 */       return (HikariPool.this.poolState == 0 && HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this
/* 765 */         .getIdleConnections() < HikariPool.this.config.getMinimumIdle() || HikariPool.this.connectionBag.getWaitingThreadCount() > HikariPool.this.getIdleConnections()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HouseKeeper
/*     */     implements Runnable
/*     */   {
/* 774 */     private volatile long previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.housekeepingPeriodMs);
/*     */     
/* 776 */     private final AtomicReferenceFieldUpdater<PoolBase, String> catalogUpdater = AtomicReferenceFieldUpdater.newUpdater(PoolBase.class, String.class, "catalog");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 783 */         HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
/* 784 */         HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
/* 785 */         HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
/*     */         
/* 787 */         if (HikariPool.this.config.getCatalog() != null && !HikariPool.this.config.getCatalog().equals(HikariPool.this.catalog)) {
/* 788 */           this.catalogUpdater.set(HikariPool.this, HikariPool.this.config.getCatalog());
/*     */         }
/*     */         
/* 791 */         long idleTimeout = HikariPool.this.config.getIdleTimeout();
/* 792 */         long now = ClockSource.currentTime();
/*     */ 
/*     */         
/* 795 */         if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.housekeepingPeriodMs)) {
/* 796 */           HikariPool.this.logger.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, 
/* 797 */               ClockSource.elapsedDisplayString(this.previous, now));
/* 798 */           this.previous = now;
/* 799 */           HikariPool.this.softEvictConnections();
/*     */           return;
/*     */         } 
/* 802 */         if (now > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.housekeepingPeriodMs / 2L))
/*     */         {
/* 804 */           HikariPool.this.logger.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
/*     */         }
/*     */         
/* 807 */         this.previous = now;
/*     */         
/* 809 */         String afterPrefix = "Pool ";
/* 810 */         if (idleTimeout > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
/* 811 */           HikariPool.this.logPoolState(new String[] { "Before cleanup " });
/* 812 */           afterPrefix = "After cleanup  ";
/*     */           
/* 814 */           List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
/* 815 */           int toRemove = notInUse.size() - HikariPool.this.config.getMinimumIdle();
/* 816 */           for (PoolEntry entry : notInUse) {
/* 817 */             if (toRemove > 0 && ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
/* 818 */               HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
/* 819 */               toRemove--;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 824 */         HikariPool.this.logPoolState(new String[] { afterPrefix });
/*     */         
/* 826 */         HikariPool.this.fillPool(true);
/*     */       }
/* 828 */       catch (Exception e) {
/* 829 */         HikariPool.this.logger.error("Unexpected exception in housekeeping task", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CustomDiscardPolicy
/*     */     implements RejectedExecutionHandler
/*     */   {
/*     */     public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
/* 838 */       HikariPool.this.addConnectionQueueDepth.decrementAndGet();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class MaxLifetimeTask
/*     */     implements Runnable
/*     */   {
/*     */     private final PoolEntry poolEntry;
/*     */     
/*     */     MaxLifetimeTask(PoolEntry poolEntry) {
/* 848 */       this.poolEntry = poolEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 853 */       if (HikariPool.this.softEvictConnection(this.poolEntry, "(connection has passed maxLifetime)", false)) {
/* 854 */         HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class KeepaliveTask
/*     */     implements Runnable
/*     */   {
/*     */     private final PoolEntry poolEntry;
/*     */     
/*     */     KeepaliveTask(PoolEntry poolEntry) {
/* 865 */       this.poolEntry = poolEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 870 */       if (HikariPool.this.connectionBag.reserve(this.poolEntry)) {
/* 871 */         if (HikariPool.this.isConnectionDead(this.poolEntry.connection)) {
/* 872 */           HikariPool.this.softEvictConnection(this.poolEntry, "(connection is dead)", true);
/* 873 */           HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
/*     */         } else {
/*     */           
/* 876 */           HikariPool.this.connectionBag.unreserve(this.poolEntry);
/* 877 */           HikariPool.this.logger.debug("{} - keepalive: connection {} is alive", HikariPool.this.poolName, this.poolEntry.connection);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PoolInitializationException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916520L;
/*     */ 
/*     */ 
/*     */     
/*     */     public PoolInitializationException(Throwable t) {
/* 893 */       super("Failed to initialize pool: " + t.getMessage(), t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\HikariPool.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */