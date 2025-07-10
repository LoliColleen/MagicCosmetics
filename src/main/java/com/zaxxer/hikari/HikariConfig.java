/*      */ package com.zaxxer.hikari;
/*      */ 
/*      */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*      */ import com.zaxxer.hikari.util.PropertyElf;
/*      */ import com.zaxxer.hikari.util.UtilityElf;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HikariConfig
/*      */   implements HikariConfigMXBean
/*      */ {
/*   48 */   private static final Logger LOGGER = LoggerFactory.getLogger(HikariConfig.class);
/*      */   
/*   50 */   private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
/*   51 */   private static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
/*   52 */   private static final long VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
/*   53 */   private static final long SOFT_TIMEOUT_FLOOR = Long.getLong("com.zaxxer.hikari.timeoutMs.floor", 250L).longValue();
/*   54 */   private static final long IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
/*   55 */   private static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean unitTest = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   private Properties dataSourceProperties = new Properties();
/*  111 */   private Properties healthCheckProperties = new Properties();
/*      */   
/*  113 */   private volatile int minIdle = -1;
/*  114 */   private volatile int maxPoolSize = -1;
/*  115 */   private volatile long maxLifetime = MAX_LIFETIME;
/*  116 */   private volatile long connectionTimeout = CONNECTION_TIMEOUT;
/*  117 */   private volatile long validationTimeout = VALIDATION_TIMEOUT;
/*  118 */   private volatile long idleTimeout = IDLE_TIMEOUT;
/*  119 */   private long initializationFailTimeout = 1L; private boolean isAutoCommit = true; private static final long DEFAULT_KEEPALIVE_TIME = 0L; private static final int DEFAULT_POOL_SIZE = 10; private volatile String catalog; private volatile long leakDetectionThreshold; private volatile String username; private volatile String password;
/*      */   private String connectionInitSql;
/*  121 */   private long keepaliveTime = 0L; private String connectionTestQuery; private String dataSourceClassName; private String dataSourceJndiName; private String driverClassName; private String exceptionOverrideClassName; private String jdbcUrl;
/*      */   public HikariConfig() {
/*  123 */     String systemProp = System.getProperty("hikaricp.configurationFile");
/*  124 */     if (systemProp != null)
/*  125 */       loadProperties(systemProp); 
/*      */   }
/*      */   private String poolName; private String schema; private String transactionIsolationName; private boolean isReadOnly; private boolean isIsolateInternalQueries; private boolean isRegisterMbeans; private boolean isAllowPoolSuspension; private DataSource dataSource;
/*      */   private ThreadFactory threadFactory;
/*      */   private ScheduledExecutorService scheduledExecutor;
/*      */   private MetricsTrackerFactory metricsTrackerFactory;
/*      */   private Object metricRegistry;
/*      */   private Object healthCheckRegistry;
/*      */   private volatile boolean sealed;
/*      */   
/*      */   public HikariConfig(Properties properties) {
/*  136 */     this();
/*  137 */     PropertyElf.setTargetFromProperties(this, properties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HikariConfig(String propertyFileName) {
/*  149 */     this();
/*      */     
/*  151 */     loadProperties(propertyFileName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalog() {
/*  162 */     return this.catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatalog(String catalog) {
/*  169 */     this.catalog = catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionTimeout() {
/*  177 */     return this.connectionTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTimeout(long connectionTimeoutMs) {
/*  184 */     if (connectionTimeoutMs == 0L) {
/*  185 */       this.connectionTimeout = 2147483647L;
/*      */     } else {
/*  187 */       if (connectionTimeoutMs < SOFT_TIMEOUT_FLOOR) {
/*  188 */         throw new IllegalArgumentException("connectionTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
/*      */       }
/*      */       
/*  191 */       this.connectionTimeout = connectionTimeoutMs;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleTimeout() {
/*  199 */     return this.idleTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleTimeout(long idleTimeoutMs) {
/*  206 */     if (idleTimeoutMs < 0L) {
/*  207 */       throw new IllegalArgumentException("idleTimeout cannot be negative");
/*      */     }
/*  209 */     this.idleTimeout = idleTimeoutMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLeakDetectionThreshold() {
/*  216 */     return this.leakDetectionThreshold;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
/*  223 */     this.leakDetectionThreshold = leakDetectionThresholdMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMaxLifetime() {
/*  230 */     return this.maxLifetime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLifetime(long maxLifetimeMs) {
/*  237 */     this.maxLifetime = maxLifetimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaximumPoolSize() {
/*  244 */     return this.maxPoolSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaximumPoolSize(int maxPoolSize) {
/*  251 */     if (maxPoolSize < 1) {
/*  252 */       throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
/*      */     }
/*  254 */     this.maxPoolSize = maxPoolSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMinimumIdle() {
/*  261 */     return this.minIdle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMinimumIdle(int minIdle) {
/*  268 */     if (minIdle < 0) {
/*  269 */       throw new IllegalArgumentException("minimumIdle cannot be negative");
/*      */     }
/*  271 */     this.minIdle = minIdle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPassword() {
/*  280 */     return this.password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassword(String password) {
/*  290 */     this.password = password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUsername() {
/*  300 */     return this.username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsername(String username) {
/*  311 */     this.username = username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getValidationTimeout() {
/*  318 */     return this.validationTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValidationTimeout(long validationTimeoutMs) {
/*  325 */     if (validationTimeoutMs < SOFT_TIMEOUT_FLOOR) {
/*  326 */       throw new IllegalArgumentException("validationTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
/*      */     }
/*      */     
/*  329 */     this.validationTimeout = validationTimeoutMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionTestQuery() {
/*  343 */     return this.connectionTestQuery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTestQuery(String connectionTestQuery) {
/*  355 */     checkIfSealed();
/*  356 */     this.connectionTestQuery = connectionTestQuery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionInitSql() {
/*  367 */     return this.connectionInitSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionInitSql(String connectionInitSql) {
/*  379 */     checkIfSealed();
/*  380 */     this.connectionInitSql = connectionInitSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataSource getDataSource() {
/*  391 */     return this.dataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSource(DataSource dataSource) {
/*  402 */     checkIfSealed();
/*  403 */     this.dataSource = dataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDataSourceClassName() {
/*  413 */     return this.dataSourceClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSourceClassName(String className) {
/*  423 */     checkIfSealed();
/*  424 */     this.dataSourceClassName = className;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDataSourceProperty(String propertyName, Object value) {
/*  442 */     checkIfSealed();
/*  443 */     this.dataSourceProperties.put(propertyName, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDataSourceJNDI() {
/*  448 */     return this.dataSourceJndiName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceJNDI(String jndiDataSource) {
/*  453 */     checkIfSealed();
/*  454 */     this.dataSourceJndiName = jndiDataSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getDataSourceProperties() {
/*  459 */     return this.dataSourceProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceProperties(Properties dsProperties) {
/*  464 */     checkIfSealed();
/*  465 */     this.dataSourceProperties.putAll(dsProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDriverClassName() {
/*  470 */     return this.driverClassName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDriverClassName(String driverClassName) {
/*  475 */     checkIfSealed();
/*      */     
/*  477 */     Class<?> driverClass = attemptFromContextLoader(driverClassName);
/*      */     try {
/*  479 */       if (driverClass == null) {
/*  480 */         driverClass = getClass().getClassLoader().loadClass(driverClassName);
/*  481 */         LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*      */       } 
/*  483 */     } catch (ClassNotFoundException e) {
/*  484 */       LOGGER.error("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*      */     } 
/*      */     
/*  487 */     if (driverClass == null) {
/*  488 */       throw new RuntimeException("Failed to load driver class " + driverClassName + " in either of HikariConfig class loader or Thread context classloader");
/*      */     }
/*      */     
/*      */     try {
/*  492 */       driverClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  493 */       this.driverClassName = driverClassName;
/*      */     }
/*  495 */     catch (Exception e) {
/*  496 */       throw new RuntimeException("Failed to instantiate class " + driverClassName, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getJdbcUrl() {
/*  502 */     return this.jdbcUrl;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setJdbcUrl(String jdbcUrl) {
/*  507 */     checkIfSealed();
/*  508 */     this.jdbcUrl = jdbcUrl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoCommit() {
/*  518 */     return this.isAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoCommit(boolean isAutoCommit) {
/*  528 */     checkIfSealed();
/*  529 */     this.isAutoCommit = isAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowPoolSuspension() {
/*  539 */     return this.isAllowPoolSuspension;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
/*  551 */     checkIfSealed();
/*  552 */     this.isAllowPoolSuspension = isAllowPoolSuspension;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getInitializationFailTimeout() {
/*  564 */     return this.initializationFailTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitializationFailTimeout(long initializationFailTimeout) {
/*  602 */     checkIfSealed();
/*  603 */     this.initializationFailTimeout = initializationFailTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIsolateInternalQueries() {
/*  614 */     return this.isIsolateInternalQueries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsolateInternalQueries(boolean isolate) {
/*  625 */     checkIfSealed();
/*  626 */     this.isIsolateInternalQueries = isolate;
/*      */   }
/*      */ 
/*      */   
/*      */   public MetricsTrackerFactory getMetricsTrackerFactory() {
/*  631 */     return this.metricsTrackerFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
/*  636 */     if (this.metricRegistry != null) {
/*  637 */       throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
/*      */     }
/*      */     
/*  640 */     this.metricsTrackerFactory = metricsTrackerFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getMetricRegistry() {
/*  650 */     return this.metricRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMetricRegistry(Object metricRegistry) {
/*  660 */     if (this.metricsTrackerFactory != null) {
/*  661 */       throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
/*      */     }
/*      */     
/*  664 */     if (metricRegistry != null) {
/*  665 */       metricRegistry = getObjectOrPerformJndiLookup(metricRegistry);
/*      */       
/*  667 */       if (!UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry") && 
/*  668 */         !UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
/*  669 */         throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  673 */     this.metricRegistry = metricRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getHealthCheckRegistry() {
/*  684 */     return this.healthCheckRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHealthCheckRegistry(Object healthCheckRegistry) {
/*  695 */     checkIfSealed();
/*      */     
/*  697 */     if (healthCheckRegistry != null) {
/*  698 */       healthCheckRegistry = getObjectOrPerformJndiLookup(healthCheckRegistry);
/*      */       
/*  700 */       if (!(healthCheckRegistry instanceof com.codahale.metrics.health.HealthCheckRegistry)) {
/*  701 */         throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  705 */     this.healthCheckRegistry = healthCheckRegistry;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getHealthCheckProperties() {
/*  710 */     return this.healthCheckProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHealthCheckProperties(Properties healthCheckProperties) {
/*  715 */     checkIfSealed();
/*  716 */     this.healthCheckProperties.putAll(healthCheckProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addHealthCheckProperty(String key, String value) {
/*  721 */     checkIfSealed();
/*  722 */     this.healthCheckProperties.setProperty(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getKeepaliveTime() {
/*  732 */     return this.keepaliveTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepaliveTime(long keepaliveTimeMs) {
/*  742 */     this.keepaliveTime = keepaliveTimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  752 */     return this.isReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean readOnly) {
/*  762 */     checkIfSealed();
/*  763 */     this.isReadOnly = readOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegisterMbeans() {
/*  774 */     return this.isRegisterMbeans;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRegisterMbeans(boolean register) {
/*  784 */     checkIfSealed();
/*  785 */     this.isRegisterMbeans = register;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPoolName() {
/*  792 */     return this.poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolName(String poolName) {
/*  803 */     checkIfSealed();
/*  804 */     this.poolName = poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScheduledExecutorService getScheduledExecutor() {
/*  814 */     return this.scheduledExecutor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScheduledExecutor(ScheduledExecutorService executor) {
/*  824 */     checkIfSealed();
/*  825 */     this.scheduledExecutor = executor;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTransactionIsolation() {
/*  830 */     return this.transactionIsolationName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchema() {
/*  840 */     return this.schema;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSchema(String schema) {
/*  850 */     checkIfSealed();
/*  851 */     this.schema = schema;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getExceptionOverrideClassName() {
/*  862 */     return this.exceptionOverrideClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExceptionOverrideClassName(String exceptionOverrideClassName) {
/*  873 */     checkIfSealed();
/*      */     
/*  875 */     Class<?> overrideClass = attemptFromContextLoader(exceptionOverrideClassName);
/*      */     try {
/*  877 */       if (overrideClass == null) {
/*  878 */         overrideClass = getClass().getClassLoader().loadClass(exceptionOverrideClassName);
/*  879 */         LOGGER.debug("SQLExceptionOverride class {} found in the HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
/*      */       } 
/*  881 */     } catch (ClassNotFoundException e) {
/*  882 */       LOGGER.error("Failed to load SQLExceptionOverride class {} from HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
/*      */     } 
/*      */     
/*  885 */     if (overrideClass == null) {
/*  886 */       throw new RuntimeException("Failed to load SQLExceptionOverride class " + exceptionOverrideClassName + " in either of HikariConfig class loader or Thread context classloader");
/*      */     }
/*      */     
/*      */     try {
/*  890 */       overrideClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  891 */       this.exceptionOverrideClassName = exceptionOverrideClassName;
/*      */     }
/*  893 */     catch (Exception e) {
/*  894 */       throw new RuntimeException("Failed to instantiate class " + exceptionOverrideClassName, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionIsolation(String isolationLevel) {
/*  907 */     checkIfSealed();
/*  908 */     this.transactionIsolationName = isolationLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ThreadFactory getThreadFactory() {
/*  918 */     return this.threadFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThreadFactory(ThreadFactory threadFactory) {
/*  928 */     checkIfSealed();
/*  929 */     this.threadFactory = threadFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   void seal() {
/*  934 */     this.sealed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyStateTo(HikariConfig other) {
/*  944 */     for (Field field : HikariConfig.class.getDeclaredFields()) {
/*  945 */       if (!Modifier.isFinal(field.getModifiers())) {
/*  946 */         field.setAccessible(true);
/*      */         try {
/*  948 */           field.set(other, field.get(this));
/*      */         }
/*  950 */         catch (Exception e) {
/*  951 */           throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  956 */     other.sealed = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> attemptFromContextLoader(String driverClassName) {
/*  964 */     ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
/*  965 */     if (threadContextClassLoader != null) {
/*      */       try {
/*  967 */         Class<?> driverClass = threadContextClassLoader.loadClass(driverClassName);
/*  968 */         LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
/*  969 */         return driverClass;
/*  970 */       } catch (ClassNotFoundException e) {
/*  971 */         LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", new Object[] { driverClassName, threadContextClassLoader, 
/*  972 */               getClass().getClassLoader() });
/*      */       } 
/*      */     }
/*      */     
/*  976 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() {
/*  982 */     if (this.poolName == null) {
/*  983 */       this.poolName = generatePoolName();
/*      */     }
/*  985 */     else if (this.isRegisterMbeans && this.poolName.contains(":")) {
/*  986 */       throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  991 */     this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
/*  992 */     this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
/*  993 */     this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
/*  994 */     this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
/*  995 */     this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
/*  996 */     this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
/*  997 */     this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
/*  998 */     this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
/*      */ 
/*      */     
/* 1001 */     if (this.dataSource != null) {
/* 1002 */       if (this.dataSourceClassName != null) {
/* 1003 */         LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
/*      */       }
/*      */     }
/* 1006 */     else if (this.dataSourceClassName != null) {
/* 1007 */       if (this.driverClassName != null) {
/* 1008 */         LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
/*      */ 
/*      */         
/* 1011 */         throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
/*      */       } 
/* 1013 */       if (this.jdbcUrl != null) {
/* 1014 */         LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
/*      */       }
/*      */     }
/* 1017 */     else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
/*      */ 
/*      */       
/* 1020 */       if (this.driverClassName != null) {
/* 1021 */         LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
/* 1022 */         throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
/*      */       } 
/*      */       
/* 1025 */       LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
/* 1026 */       throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
/*      */     } 
/*      */     
/* 1029 */     validateNumerics();
/*      */     
/* 1031 */     if (LOGGER.isDebugEnabled() || unitTest) {
/* 1032 */       logConfiguration();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void validateNumerics() {
/* 1038 */     if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
/* 1039 */       LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, Long.valueOf(MAX_LIFETIME));
/* 1040 */       this.maxLifetime = MAX_LIFETIME;
/*      */     } 
/*      */ 
/*      */     
/* 1044 */     if (this.keepaliveTime != 0L && this.keepaliveTime < TimeUnit.SECONDS.toMillis(30L)) {
/* 1045 */       LOGGER.warn("{} - keepaliveTime is less than 30000ms, disabling it.", this.poolName);
/* 1046 */       this.keepaliveTime = 0L;
/*      */     } 
/*      */ 
/*      */     
/* 1050 */     if (this.keepaliveTime != 0L && this.maxLifetime != 0L && this.keepaliveTime >= this.maxLifetime) {
/* 1051 */       LOGGER.warn("{} - keepaliveTime is greater than or equal to maxLifetime, disabling it.", this.poolName);
/* 1052 */       this.keepaliveTime = 0L;
/*      */     } 
/*      */     
/* 1055 */     if (this.leakDetectionThreshold > 0L && !unitTest && (
/* 1056 */       this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || (this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L))) {
/* 1057 */       LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
/* 1058 */       this.leakDetectionThreshold = 0L;
/*      */     } 
/*      */ 
/*      */     
/* 1062 */     if (this.connectionTimeout < SOFT_TIMEOUT_FLOOR) {
/* 1063 */       LOGGER.warn("{} - connectionTimeout is less than {}ms, setting to {}ms.", new Object[] { this.poolName, Long.valueOf(SOFT_TIMEOUT_FLOOR), Long.valueOf(CONNECTION_TIMEOUT) });
/* 1064 */       this.connectionTimeout = CONNECTION_TIMEOUT;
/*      */     } 
/*      */     
/* 1067 */     if (this.validationTimeout < SOFT_TIMEOUT_FLOOR) {
/* 1068 */       LOGGER.warn("{} - validationTimeout is less than {}ms, setting to {}ms.", new Object[] { this.poolName, Long.valueOf(SOFT_TIMEOUT_FLOOR), Long.valueOf(VALIDATION_TIMEOUT) });
/* 1069 */       this.validationTimeout = VALIDATION_TIMEOUT;
/*      */     } 
/*      */     
/* 1072 */     if (this.maxPoolSize < 1) {
/* 1073 */       this.maxPoolSize = 10;
/*      */     }
/*      */     
/* 1076 */     if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
/* 1077 */       this.minIdle = this.maxPoolSize;
/*      */     }
/*      */     
/* 1080 */     if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L && this.minIdle < this.maxPoolSize) {
/* 1081 */       LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
/* 1082 */       this.idleTimeout = 0L;
/*      */     }
/* 1084 */     else if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L) && this.minIdle < this.maxPoolSize) {
/* 1085 */       LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, Long.valueOf(IDLE_TIMEOUT));
/* 1086 */       this.idleTimeout = IDLE_TIMEOUT;
/*      */     }
/* 1088 */     else if (this.idleTimeout != IDLE_TIMEOUT && this.idleTimeout != 0L && this.minIdle == this.maxPoolSize) {
/* 1089 */       LOGGER.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", this.poolName);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIfSealed() {
/* 1095 */     if (this.sealed) throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
/*      */   
/*      */   }
/*      */   
/*      */   private void logConfiguration() {
/* 1100 */     LOGGER.debug("{} - configuration:", this.poolName);
/* 1101 */     TreeSet<String> propertyNames = new TreeSet<>(PropertyElf.getPropertyNames(HikariConfig.class));
/* 1102 */     for (String prop : propertyNames) {
/*      */       try {
/* 1104 */         Object value = PropertyElf.getProperty(prop, this);
/* 1105 */         if ("dataSourceProperties".equals(prop)) {
/* 1106 */           Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
/* 1107 */           dsProps.setProperty("password", "<masked>");
/* 1108 */           value = dsProps;
/*      */         } 
/*      */         
/* 1111 */         if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
/* 1112 */           value = "infinite";
/*      */         }
/* 1114 */         else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
/* 1115 */           value = "default";
/*      */         }
/* 1117 */         else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
/* 1118 */           value = "internal";
/*      */         }
/* 1120 */         else if (prop.contains("jdbcUrl") && value instanceof String) {
/* 1121 */           value = ((String)value).replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
/*      */         }
/* 1123 */         else if (prop.contains("password")) {
/* 1124 */           value = "<masked>";
/*      */         }
/* 1126 */         else if (value instanceof String) {
/* 1127 */           value = "\"" + value + "\"";
/*      */         }
/* 1129 */         else if (value == null) {
/* 1130 */           value = "none";
/*      */         } 
/* 1132 */         LOGGER.debug("{}{}", (prop + "................................................").substring(0, 32), value);
/*      */       }
/* 1134 */       catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadProperties(String propertyFileName) {
/* 1142 */     File propFile = new File(propertyFileName); 
/* 1143 */     try { InputStream is = propFile.isFile() ? new FileInputStream(propFile) : getClass().getResourceAsStream(propertyFileName); 
/* 1144 */       try { if (is != null) {
/* 1145 */           Properties props = new Properties();
/* 1146 */           props.load(is);
/* 1147 */           PropertyElf.setTargetFromProperties(this, props);
/*      */         } else {
/*      */           
/* 1150 */           throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
/*      */         } 
/* 1152 */         if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/* 1153 */           try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException io)
/* 1154 */     { throw new RuntimeException("Failed to read property file", io); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private String generatePoolName() {
/* 1160 */     String prefix = "HikariPool-";
/*      */     
/*      */     try {
/* 1163 */       synchronized (System.getProperties()) {
/* 1164 */         String next = String.valueOf(Integer.getInteger("com.zaxxer.hikari.pool_number", 0).intValue() + 1);
/* 1165 */         System.setProperty("com.zaxxer.hikari.pool_number", next);
/* 1166 */         return "HikariPool-" + next;
/*      */       } 
/* 1168 */     } catch (AccessControlException e) {
/*      */ 
/*      */       
/* 1171 */       ThreadLocalRandom random = ThreadLocalRandom.current();
/* 1172 */       StringBuilder buf = new StringBuilder("HikariPool-");
/*      */       
/* 1174 */       for (int i = 0; i < 4; i++) {
/* 1175 */         buf.append(ID_CHARACTERS[random.nextInt(62)]);
/*      */       }
/*      */       
/* 1178 */       LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
/*      */       
/* 1180 */       return buf.toString();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Object getObjectOrPerformJndiLookup(Object object) {
/* 1186 */     if (object instanceof String) {
/*      */       try {
/* 1188 */         InitialContext initCtx = new InitialContext();
/* 1189 */         return initCtx.lookup((String)object);
/*      */       }
/* 1191 */       catch (NamingException e) {
/* 1192 */         throw new IllegalArgumentException(e);
/*      */       } 
/*      */     }
/* 1195 */     return object;
/*      */   }
/*      */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\HikariConfig.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */