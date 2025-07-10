/*     */ package org.slf4j.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ 
/*     */ 
/*     */ public class SubstituteLoggingEvent
/*     */   implements LoggingEvent
/*     */ {
/*     */   Level level;
/*     */   List<Marker> markers;
/*     */   String loggerName;
/*     */   SubstituteLogger logger;
/*     */   String threadName;
/*     */   String message;
/*     */   Object[] argArray;
/*     */   List<KeyValuePair> keyValuePairList;
/*     */   long timeStamp;
/*     */   Throwable throwable;
/*     */   
/*     */   public Level getLevel() {
/*  25 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/*  29 */     this.level = level;
/*     */   }
/*     */   
/*     */   public List<Marker> getMarkers() {
/*  33 */     return this.markers;
/*     */   }
/*     */   
/*     */   public void addMarker(Marker marker) {
/*  37 */     if (marker == null) {
/*     */       return;
/*     */     }
/*  40 */     if (this.markers == null) {
/*  41 */       this.markers = new ArrayList<>(2);
/*     */     }
/*     */     
/*  44 */     this.markers.add(marker);
/*     */   }
/*     */   
/*     */   public String getLoggerName() {
/*  48 */     return this.loggerName;
/*     */   }
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/*  52 */     this.loggerName = loggerName;
/*     */   }
/*     */   
/*     */   public SubstituteLogger getLogger() {
/*  56 */     return this.logger;
/*     */   }
/*     */   
/*     */   public void setLogger(SubstituteLogger logger) {
/*  60 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  64 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/*  68 */     this.message = message;
/*     */   }
/*     */   
/*     */   public Object[] getArgumentArray() {
/*  72 */     return this.argArray;
/*     */   }
/*     */   
/*     */   public void setArgumentArray(Object[] argArray) {
/*  76 */     this.argArray = argArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> getArguments() {
/*  81 */     if (this.argArray == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     return Arrays.asList(this.argArray);
/*     */   }
/*     */   
/*     */   public long getTimeStamp() {
/*  88 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   public void setTimeStamp(long timeStamp) {
/*  92 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/*  96 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public void setThreadName(String threadName) {
/* 100 */     this.threadName = threadName;
/*     */   }
/*     */   
/*     */   public Throwable getThrowable() {
/* 104 */     return this.throwable;
/*     */   }
/*     */   
/*     */   public void setThrowable(Throwable throwable) {
/* 108 */     this.throwable = throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<KeyValuePair> getKeyValuePairs() {
/* 114 */     return this.keyValuePairList;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\event\SubstituteLoggingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */