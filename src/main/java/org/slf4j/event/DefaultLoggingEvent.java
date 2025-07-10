/*     */ package org.slf4j.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
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
/*     */ 
/*     */ public class DefaultLoggingEvent
/*     */   implements LoggingEvent
/*     */ {
/*     */   Logger logger;
/*     */   Level level;
/*     */   String message;
/*     */   List<Marker> markers;
/*     */   List<Object> arguments;
/*     */   List<KeyValuePair> keyValuePairs;
/*     */   Throwable throwable;
/*     */   String threadName;
/*     */   long timeStamp;
/*     */   
/*     */   public DefaultLoggingEvent(Level level, Logger logger) {
/*  33 */     this.logger = logger;
/*  34 */     this.level = level;
/*     */   }
/*     */   
/*     */   public void addMarker(Marker marker) {
/*  38 */     if (this.markers == null) {
/*  39 */       this.markers = new ArrayList<>(2);
/*     */     }
/*  41 */     this.markers.add(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Marker> getMarkers() {
/*  46 */     return this.markers;
/*     */   }
/*     */   
/*     */   public void addArgument(Object p) {
/*  50 */     getNonNullArguments().add(p);
/*     */   }
/*     */   
/*     */   public void addArguments(Object... args) {
/*  54 */     getNonNullArguments().addAll(Arrays.asList(args));
/*     */   }
/*     */   
/*     */   private List<Object> getNonNullArguments() {
/*  58 */     if (this.arguments == null) {
/*  59 */       this.arguments = new ArrayList(3);
/*     */     }
/*  61 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> getArguments() {
/*  66 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getArgumentArray() {
/*  71 */     if (this.arguments == null)
/*  72 */       return null; 
/*  73 */     return this.arguments.toArray();
/*     */   }
/*     */   
/*     */   public void addKeyValue(String key, Object value) {
/*  77 */     getNonnullKeyValuePairs().add(new KeyValuePair(key, value));
/*     */   }
/*     */   
/*     */   private List<KeyValuePair> getNonnullKeyValuePairs() {
/*  81 */     if (this.keyValuePairs == null) {
/*  82 */       this.keyValuePairs = new ArrayList<>(4);
/*     */     }
/*  84 */     return this.keyValuePairs;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<KeyValuePair> getKeyValuePairs() {
/*  89 */     return this.keyValuePairs;
/*     */   }
/*     */   
/*     */   public void setThrowable(Throwable cause) {
/*  93 */     this.throwable = cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  98 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 103 */     return this.logger.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 108 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 112 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 118 */     return this.throwable;
/*     */   }
/*     */   public String getThreadName() {
/* 121 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public long getTimeStamp() {
/* 125 */     return this.timeStamp;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\event\DefaultLoggingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */