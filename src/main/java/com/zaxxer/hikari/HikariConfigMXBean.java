package com.zaxxer.hikari;

public interface HikariConfigMXBean {
  long getConnectionTimeout();
  
  void setConnectionTimeout(long paramLong);
  
  long getValidationTimeout();
  
  void setValidationTimeout(long paramLong);
  
  long getIdleTimeout();
  
  void setIdleTimeout(long paramLong);
  
  long getLeakDetectionThreshold();
  
  void setLeakDetectionThreshold(long paramLong);
  
  long getMaxLifetime();
  
  void setMaxLifetime(long paramLong);
  
  int getMinimumIdle();
  
  void setMinimumIdle(int paramInt);
  
  int getMaximumPoolSize();
  
  void setMaximumPoolSize(int paramInt);
  
  void setPassword(String paramString);
  
  void setUsername(String paramString);
  
  String getPoolName();
  
  String getCatalog();
  
  void setCatalog(String paramString);
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\HikariConfigMXBean.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */