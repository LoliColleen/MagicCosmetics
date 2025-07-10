package org.slf4j.event;

import java.util.List;
import org.slf4j.Marker;

public interface LoggingEvent {
  Level getLevel();
  
  String getLoggerName();
  
  String getMessage();
  
  List<Object> getArguments();
  
  Object[] getArgumentArray();
  
  List<Marker> getMarkers();
  
  List<KeyValuePair> getKeyValuePairs();
  
  Throwable getThrowable();
  
  long getTimeStamp();
  
  String getThreadName();
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\event\LoggingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */