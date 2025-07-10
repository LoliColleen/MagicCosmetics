package org.slf4j;

public class LoggerFactoryFriend {
  public static void reset() {
    LoggerFactory.reset();
  }
  
  public static void setDetectLoggerNameMismatch(boolean enabled) {
    LoggerFactory.DETECT_LOGGER_NAME_MISMATCH = enabled;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\LoggerFactoryFriend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */