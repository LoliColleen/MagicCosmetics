package com.francobm.magicosmetics.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

public class HikariCP {
  private HikariDataSource hikariDataSource;
  
  protected String hostname;
  
  protected int port;
  
  protected String database;
  
  protected String username;
  
  protected String password;
  
  protected String options;
  
  public HikariCP(String hostname, int port, String username, String password, String database, String options) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.database = database;
    this.password = password;
    this.options = options;
  }
  
  public HikariCP() {}
  
  public void setProperties(SQL sql) {
    HikariConfig config = new HikariConfig();
    try {
      if (sql.getDatabaseType() == DatabaseType.MYSQL) {
        String mysql = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?" + this.options;
        config.setJdbcUrl(mysql);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000L);
        config.setMaxLifetime(1800000L);
        config.setValidationTimeout(5000L);
        config.setIdleTimeout(600000L);
      } else {
        String sqlite = "jdbc:sqlite:" + String.valueOf(((SQLite)sql).getFileSQL());
        config.setJdbcUrl(sqlite);
        config.setDriverClassName("org.sqlite.JDBC");
      } 
      this.hikariDataSource = new HikariDataSource(config);
    } catch (Exception e) {
      Bukkit.getLogger().warning("Problem with HikariCP:" + e.getMessage());
    } 
  }
  
  public HikariDataSource getHikariDataSource() {
    return this.hikariDataSource;
  }
  
  public void close() {
    if (this.hikariDataSource != null && !this.hikariDataSource.isClosed())
      this.hikariDataSource.close(); 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\HikariCP.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */