/*    */ package com.francobm.magicosmetics.database;
/*    */ 
/*    */ import com.zaxxer.hikari.HikariConfig;
/*    */ import com.zaxxer.hikari.HikariDataSource;
/*    */ import org.bukkit.Bukkit;
/*    */ 
/*    */ public class HikariCP
/*    */ {
/*    */   private HikariDataSource hikariDataSource;
/*    */   protected String hostname;
/*    */   protected int port;
/*    */   protected String database;
/*    */   protected String username;
/*    */   protected String password;
/*    */   protected String options;
/*    */   
/*    */   public HikariCP(String hostname, int port, String username, String password, String database, String options) {
/* 18 */     this.hostname = hostname;
/* 19 */     this.port = port;
/* 20 */     this.username = username;
/* 21 */     this.database = database;
/* 22 */     this.password = password;
/* 23 */     this.options = options;
/*    */   }
/*    */ 
/*    */   
/*    */   public HikariCP() {}
/*    */   
/*    */   public void setProperties(SQL sql) {
/* 30 */     HikariConfig config = new HikariConfig();
/*    */     try {
/* 32 */       if (sql.getDatabaseType() == DatabaseType.MYSQL) {
/*    */         
/* 34 */         String mysql = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?" + this.options;
/* 35 */         config.setJdbcUrl(mysql);
/* 36 */         config.setUsername(this.username);
/* 37 */         config.setPassword(this.password);
/* 38 */         config.addDataSourceProperty("cachePrepStmts", "true");
/* 39 */         config.addDataSourceProperty("prepStmtCacheSize", "250");
/* 40 */         config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
/* 41 */         config.setMaximumPoolSize(10);
/* 42 */         config.setConnectionTimeout(30000L);
/* 43 */         config.setMaxLifetime(1800000L);
/* 44 */         config.setValidationTimeout(5000L);
/* 45 */         config.setIdleTimeout(600000L);
/*    */       }
/*    */       else {
/*    */         
/* 49 */         String sqlite = "jdbc:sqlite:" + String.valueOf(((SQLite)sql).getFileSQL());
/* 50 */         config.setJdbcUrl(sqlite);
/* 51 */         config.setDriverClassName("org.sqlite.JDBC");
/*    */       } 
/* 53 */       this.hikariDataSource = new HikariDataSource(config);
/* 54 */     } catch (Exception e) {
/* 55 */       Bukkit.getLogger().warning("Problem with HikariCP:" + e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public HikariDataSource getHikariDataSource() {
/* 60 */     return this.hikariDataSource;
/*    */   }
/*    */   
/*    */   public void close() {
/* 64 */     if (this.hikariDataSource != null && !this.hikariDataSource.isClosed())
/* 65 */       this.hikariDataSource.close(); 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\HikariCP.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */