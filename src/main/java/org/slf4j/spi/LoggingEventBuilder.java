package org.slf4j.spi;

import java.util.function.Supplier;
import org.slf4j.Marker;

public interface LoggingEventBuilder {
  LoggingEventBuilder setCause(Throwable paramThrowable);
  
  LoggingEventBuilder addMarker(Marker paramMarker);
  
  LoggingEventBuilder addArgument(Object paramObject);
  
  LoggingEventBuilder addArgument(Supplier<Object> paramSupplier);
  
  LoggingEventBuilder addKeyValue(String paramString, Object paramObject);
  
  LoggingEventBuilder addKeyValue(String paramString, Supplier<Object> paramSupplier);
  
  void log(String paramString);
  
  void log(String paramString, Object paramObject);
  
  void log(String paramString, Object paramObject1, Object paramObject2);
  
  void log(String paramString, Object... paramVarArgs);
  
  void log(Supplier<String> paramSupplier);
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\LoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */