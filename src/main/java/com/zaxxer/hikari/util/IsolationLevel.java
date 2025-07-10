package com.zaxxer.hikari.util;

public enum IsolationLevel {
  TRANSACTION_NONE(0),
  TRANSACTION_READ_UNCOMMITTED(1),
  TRANSACTION_READ_COMMITTED(2),
  TRANSACTION_REPEATABLE_READ(4),
  TRANSACTION_SERIALIZABLE(8),
  TRANSACTION_SQL_SERVER_SNAPSHOT_ISOLATION_LEVEL(4096);
  
  private final int levelId;
  
  IsolationLevel(int levelId) {
    this.levelId = levelId;
  }
  
  public int getLevelId() {
    return this.levelId;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikar\\util\IsolationLevel.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */