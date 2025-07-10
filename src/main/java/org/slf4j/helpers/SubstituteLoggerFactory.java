/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.event.SubstituteLoggingEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubstituteLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   volatile boolean postInitialization = false;
/* 47 */   final Map<String, SubstituteLogger> loggers = new ConcurrentHashMap<>();
/*    */   
/* 49 */   final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue = new LinkedBlockingQueue<>();
/*    */   
/*    */   public synchronized Logger getLogger(String name) {
/* 52 */     SubstituteLogger logger = this.loggers.get(name);
/* 53 */     if (logger == null) {
/* 54 */       logger = new SubstituteLogger(name, this.eventQueue, this.postInitialization);
/* 55 */       this.loggers.put(name, logger);
/*    */     } 
/* 57 */     return logger;
/*    */   }
/*    */   
/*    */   public List<String> getLoggerNames() {
/* 61 */     return new ArrayList<>(this.loggers.keySet());
/*    */   }
/*    */   
/*    */   public List<SubstituteLogger> getLoggers() {
/* 65 */     return new ArrayList<>(this.loggers.values());
/*    */   }
/*    */   
/*    */   public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
/* 69 */     return this.eventQueue;
/*    */   }
/*    */   
/*    */   public void postInitialization() {
/* 73 */     this.postInitialization = true;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 77 */     this.loggers.clear();
/* 78 */     this.eventQueue.clear();
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\SubstituteLoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */