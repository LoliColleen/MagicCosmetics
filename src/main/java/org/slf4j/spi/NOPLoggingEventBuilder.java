/*    */ package org.slf4j.spi;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.slf4j.Marker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NOPLoggingEventBuilder
/*    */   implements LoggingEventBuilder
/*    */ {
/* 17 */   static final NOPLoggingEventBuilder SINGLETON = new NOPLoggingEventBuilder();
/*    */   
/*    */   public static LoggingEventBuilder singleton() {
/* 20 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addMarker(Marker marker) {
/* 25 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addArgument(Object p) {
/* 30 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addArgument(Supplier<Object> objectSupplier) {
/* 35 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addKeyValue(String key, Object value) {
/* 40 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
/* 45 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder setCause(Throwable cause) {
/* 50 */     return singleton();
/*    */   }
/*    */   
/*    */   public void log(String message) {}
/*    */   
/*    */   public void log(Supplier<String> messageSupplier) {}
/*    */   
/*    */   public void log(String message, Object arg) {}
/*    */   
/*    */   public void log(String message, Object arg0, Object arg1) {}
/*    */   
/*    */   public void log(String message, Object... args) {}
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\NOPLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */