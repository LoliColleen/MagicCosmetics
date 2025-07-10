package org.slf4j.event;

public enum Level {
  ERROR(40, "ERROR"),
  WARN(30, "WARN"),
  INFO(20, "INFO"),
  DEBUG(10, "DEBUG"),
  TRACE(0, "TRACE");
  
  private int levelInt;
  
  private String levelStr;
  
  Level(int i, String s) {
    this.levelInt = i;
    this.levelStr = s;
  }
  
  public int toInt() {
    return this.levelInt;
  }
  
  public static Level intToLevel(int levelInt) {
    switch (levelInt) {
      case 0:
        return TRACE;
      case 10:
        return DEBUG;
      case 20:
        return INFO;
      case 30:
        return WARN;
      case 40:
        return ERROR;
    } 
    throw new IllegalArgumentException("Level integer [" + levelInt + "] not recognized.");
  }
  
  public String toString() {
    return this.levelStr;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\event\Level.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */