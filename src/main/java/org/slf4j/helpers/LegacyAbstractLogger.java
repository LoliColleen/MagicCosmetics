/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.Marker;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LegacyAbstractLogger
/*    */   extends AbstractLogger
/*    */ {
/*    */   private static final long serialVersionUID = -7041884104854048950L;
/*    */   
/*    */   public boolean isTraceEnabled(Marker marker) {
/* 13 */     return isTraceEnabled();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isDebugEnabled(Marker marker) {
/* 19 */     return isDebugEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInfoEnabled(Marker marker) {
/* 24 */     return isInfoEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWarnEnabled(Marker marker) {
/* 29 */     return isWarnEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isErrorEnabled(Marker marker) {
/* 34 */     return isErrorEnabled();
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\LegacyAbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */