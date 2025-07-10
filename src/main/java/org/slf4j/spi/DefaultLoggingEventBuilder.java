/*     */ package org.slf4j.spi;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.DefaultLoggingEvent;
/*     */ import org.slf4j.event.KeyValuePair;
/*     */ import org.slf4j.event.Level;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.LoggingEventAware;
/*     */ 
/*     */ public class DefaultLoggingEventBuilder
/*     */   implements LoggingEventBuilder
/*     */ {
/*     */   DefaultLoggingEvent loggingEvent;
/*     */   Logger logger;
/*     */   
/*     */   public DefaultLoggingEventBuilder(Logger logger, Level level) {
/*  19 */     this.logger = logger;
/*  20 */     this.loggingEvent = new DefaultLoggingEvent(level, logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addMarker(Marker marker) {
/*  32 */     this.loggingEvent.addMarker(marker);
/*  33 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setCause(Throwable t) {
/*  38 */     this.loggingEvent.setThrowable(t);
/*  39 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Object p) {
/*  44 */     this.loggingEvent.addArgument(p);
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Supplier<Object> objectSupplier) {
/*  50 */     this.loggingEvent.addArgument(objectSupplier.get());
/*  51 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message) {
/*  56 */     this.loggingEvent.setMessage(message);
/*  57 */     innerLog((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg) {
/*  63 */     this.loggingEvent.setMessage(message);
/*  64 */     this.loggingEvent.addArgument(arg);
/*  65 */     innerLog((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg0, Object arg1) {
/*  70 */     this.loggingEvent.setMessage(message);
/*  71 */     this.loggingEvent.addArgument(arg0);
/*  72 */     this.loggingEvent.addArgument(arg1);
/*  73 */     innerLog((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object... args) {
/*  78 */     this.loggingEvent.setMessage(message);
/*  79 */     this.loggingEvent.addArguments(args);
/*     */     
/*  81 */     innerLog((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */   
/*     */   private void innerLog(LoggingEvent logggingEvent) {
/*  85 */     if (this.logger instanceof LoggingEventAware) {
/*  86 */       ((LoggingEventAware)this.logger).log(logggingEvent);
/*     */     } else {
/*  88 */       logViaPublicLoggerAPI(logggingEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logViaPublicLoggerAPI(LoggingEvent logggingEvent) {
/*  93 */     Object[] argArray = logggingEvent.getArgumentArray();
/*  94 */     int argLen = (argArray == null) ? 0 : argArray.length;
/*     */     
/*  96 */     Throwable t = logggingEvent.getThrowable();
/*  97 */     int tLen = (t == null) ? 0 : 1;
/*     */     
/*  99 */     String msg = logggingEvent.getMessage();
/*     */     
/* 101 */     Object[] combinedArguments = new Object[argLen + tLen];
/*     */     
/* 103 */     if (argArray != null) {
/* 104 */       System.arraycopy(argArray, 0, combinedArguments, 0, argLen);
/*     */     }
/* 106 */     if (t != null) {
/* 107 */       combinedArguments[argLen] = t;
/*     */     }
/*     */     
/* 110 */     msg = mergeMarkersAndKeyValuePairs(logggingEvent, msg);
/*     */     
/* 112 */     switch (logggingEvent.getLevel()) {
/*     */       case TRACE:
/* 114 */         this.logger.trace(msg, combinedArguments);
/*     */         break;
/*     */       case null:
/* 117 */         this.logger.debug(msg, combinedArguments);
/*     */         break;
/*     */       case INFO:
/* 120 */         this.logger.info(msg, combinedArguments);
/*     */         break;
/*     */       case WARN:
/* 123 */         this.logger.warn(msg, combinedArguments);
/*     */         break;
/*     */       case ERROR:
/* 126 */         this.logger.error(msg, combinedArguments);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mergeMarkersAndKeyValuePairs(LoggingEvent logggingEvent, String msg) {
/* 141 */     StringBuilder sb = null;
/*     */     
/* 143 */     if (this.loggingEvent.getMarkers() != null) {
/* 144 */       sb = new StringBuilder();
/* 145 */       for (Marker marker : logggingEvent.getMarkers()) {
/* 146 */         sb.append(marker);
/* 147 */         sb.append(' ');
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     if (logggingEvent.getKeyValuePairs() != null) {
/* 152 */       if (sb == null) {
/* 153 */         sb = new StringBuilder();
/*     */       }
/* 155 */       for (KeyValuePair kvp : logggingEvent.getKeyValuePairs()) {
/* 156 */         sb.append(kvp.key);
/* 157 */         sb.append('=');
/* 158 */         sb.append(kvp.value);
/* 159 */         sb.append(' ');
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     if (sb != null) {
/* 164 */       sb.append(msg);
/* 165 */       return sb.toString();
/*     */     } 
/* 167 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(Supplier<String> messageSupplier) {
/* 174 */     if (messageSupplier == null) {
/* 175 */       log((String)null);
/*     */     } else {
/* 177 */       log(messageSupplier.get());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Object value) {
/* 183 */     this.loggingEvent.addKeyValue(key, value);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
/* 189 */     this.loggingEvent.addKeyValue(key, value.get());
/* 190 */     return this;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\DefaultLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */