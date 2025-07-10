package org.slf4j.spi;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.DefaultLoggingEvent;
import org.slf4j.event.KeyValuePair;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.event.LoggingEventAware;

public class DefaultLoggingEventBuilder implements LoggingEventBuilder {
  DefaultLoggingEvent loggingEvent;
  
  Logger logger;
  
  public DefaultLoggingEventBuilder(Logger logger, Level level) {
    this.logger = logger;
    this.loggingEvent = new DefaultLoggingEvent(level, logger);
  }
  
  public LoggingEventBuilder addMarker(Marker marker) {
    this.loggingEvent.addMarker(marker);
    return this;
  }
  
  public LoggingEventBuilder setCause(Throwable t) {
    this.loggingEvent.setThrowable(t);
    return this;
  }
  
  public LoggingEventBuilder addArgument(Object p) {
    this.loggingEvent.addArgument(p);
    return this;
  }
  
  public LoggingEventBuilder addArgument(Supplier<Object> objectSupplier) {
    this.loggingEvent.addArgument(objectSupplier.get());
    return this;
  }
  
  public void log(String message) {
    this.loggingEvent.setMessage(message);
    innerLog((LoggingEvent)this.loggingEvent);
  }
  
  public void log(String message, Object arg) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArgument(arg);
    innerLog((LoggingEvent)this.loggingEvent);
  }
  
  public void log(String message, Object arg0, Object arg1) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArgument(arg0);
    this.loggingEvent.addArgument(arg1);
    innerLog((LoggingEvent)this.loggingEvent);
  }
  
  public void log(String message, Object... args) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArguments(args);
    innerLog((LoggingEvent)this.loggingEvent);
  }
  
  private void innerLog(LoggingEvent logggingEvent) {
    if (this.logger instanceof LoggingEventAware) {
      ((LoggingEventAware)this.logger).log(logggingEvent);
    } else {
      logViaPublicLoggerAPI(logggingEvent);
    } 
  }
  
  private void logViaPublicLoggerAPI(LoggingEvent logggingEvent) {
    Object[] argArray = logggingEvent.getArgumentArray();
    int argLen = (argArray == null) ? 0 : argArray.length;
    Throwable t = logggingEvent.getThrowable();
    int tLen = (t == null) ? 0 : 1;
    String msg = logggingEvent.getMessage();
    Object[] combinedArguments = new Object[argLen + tLen];
    if (argArray != null)
      System.arraycopy(argArray, 0, combinedArguments, 0, argLen); 
    if (t != null)
      combinedArguments[argLen] = t; 
    msg = mergeMarkersAndKeyValuePairs(logggingEvent, msg);
    switch (logggingEvent.getLevel()) {
      case TRACE:
        this.logger.trace(msg, combinedArguments);
        break;
      case null:
        this.logger.debug(msg, combinedArguments);
        break;
      case INFO:
        this.logger.info(msg, combinedArguments);
        break;
      case WARN:
        this.logger.warn(msg, combinedArguments);
        break;
      case ERROR:
        this.logger.error(msg, combinedArguments);
        break;
    } 
  }
  
  private String mergeMarkersAndKeyValuePairs(LoggingEvent logggingEvent, String msg) {
    StringBuilder sb = null;
    if (this.loggingEvent.getMarkers() != null) {
      sb = new StringBuilder();
      for (Marker marker : logggingEvent.getMarkers()) {
        sb.append(marker);
        sb.append(' ');
      } 
    } 
    if (logggingEvent.getKeyValuePairs() != null) {
      if (sb == null)
        sb = new StringBuilder(); 
      for (KeyValuePair kvp : logggingEvent.getKeyValuePairs()) {
        sb.append(kvp.key);
        sb.append('=');
        sb.append(kvp.value);
        sb.append(' ');
      } 
    } 
    if (sb != null) {
      sb.append(msg);
      return sb.toString();
    } 
    return msg;
  }
  
  public void log(Supplier<String> messageSupplier) {
    if (messageSupplier == null) {
      log((String)null);
    } else {
      log(messageSupplier.get());
    } 
  }
  
  public LoggingEventBuilder addKeyValue(String key, Object value) {
    this.loggingEvent.addKeyValue(key, value);
    return this;
  }
  
  public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
    this.loggingEvent.addKeyValue(key, value.get());
    return this;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\DefaultLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */