/*    */ package com.zaxxer.hikari;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SQLExceptionOverride
/*    */ {
/*    */   public enum Override
/*    */   {
/* 15 */     CONTINUE_EVICT,
/* 16 */     DO_NOT_EVICT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Override adjudicate(SQLException sqlException) {
/* 28 */     return Override.CONTINUE_EVICT;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\SQLExceptionOverride.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */