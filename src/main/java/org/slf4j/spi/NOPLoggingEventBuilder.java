package org.slf4j.spi;

import java.util.function.Supplier;
import org.slf4j.Marker;

public class NOPLoggingEventBuilder implements LoggingEventBuilder {
  static final NOPLoggingEventBuilder SINGLETON = new NOPLoggingEventBuilder();
  
  public static LoggingEventBuilder singleton() {
    return SINGLETON;
  }
  
  public LoggingEventBuilder addMarker(Marker marker) {
    return singleton();
  }
  
  public LoggingEventBuilder addArgument(Object p) {
    return singleton();
  }
  
  public LoggingEventBuilder addArgument(Supplier<Object> objectSupplier) {
    return singleton();
  }
  
  public LoggingEventBuilder addKeyValue(String key, Object value) {
    return singleton();
  }
  
  public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
    return singleton();
  }
  
  public LoggingEventBuilder setCause(Throwable cause) {
    return singleton();
  }
  
  public void log(String message) {}
  
  public void log(Supplier<String> messageSupplier) {}
  
  public void log(String message, Object arg) {}
  
  public void log(String message, Object arg0, Object arg1) {}
  
  public void log(String message, Object... args) {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\NOPLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */