/*    */ package org.slf4j.event;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import org.slf4j.Marker;
/*    */ import org.slf4j.helpers.LegacyAbstractLogger;
/*    */ import org.slf4j.helpers.SubstituteLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventRecodingLogger
/*    */   extends LegacyAbstractLogger
/*    */ {
/*    */   private static final long serialVersionUID = -176083308134819629L;
/*    */   String name;
/*    */   SubstituteLogger logger;
/*    */   Queue<SubstituteLoggingEvent> eventQueue;
/*    */   static final boolean RECORD_ALL_EVENTS = true;
/*    */   
/*    */   public EventRecodingLogger(SubstituteLogger logger, Queue<SubstituteLoggingEvent> eventQueue) {
/* 31 */     this.logger = logger;
/* 32 */     this.name = logger.getName();
/* 33 */     this.eventQueue = eventQueue;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 37 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isTraceEnabled() {
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isDebugEnabled() {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isInfoEnabled() {
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isWarnEnabled() {
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isErrorEnabled() {
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] args, Throwable throwable) {
/* 63 */     SubstituteLoggingEvent loggingEvent = new SubstituteLoggingEvent();
/* 64 */     loggingEvent.setTimeStamp(System.currentTimeMillis());
/* 65 */     loggingEvent.setLevel(level);
/* 66 */     loggingEvent.setLogger(this.logger);
/* 67 */     loggingEvent.setLoggerName(this.name);
/* 68 */     if (marker != null) {
/* 69 */       loggingEvent.addMarker(marker);
/*    */     }
/* 71 */     loggingEvent.setMessage(msg);
/* 72 */     loggingEvent.setThreadName(Thread.currentThread().getName());
/*    */     
/* 74 */     loggingEvent.setArgumentArray(args);
/* 75 */     loggingEvent.setThrowable(throwable);
/*    */     
/* 77 */     this.eventQueue.add(loggingEvent);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getFullyQualifiedCallerName() {
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\event\EventRecodingLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */