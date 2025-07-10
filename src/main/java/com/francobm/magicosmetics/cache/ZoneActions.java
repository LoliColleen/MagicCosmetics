/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ import com.francobm.magicosmetics.listeners.ZoneListener;
/*    */ 
/*    */ 
/*    */ public class ZoneActions
/*    */ {
/*    */   private boolean enabled;
/*    */   private final ZoneAction onEnter;
/*    */   private final ZoneAction onExit;
/*    */   private final ZoneListener zoneListener;
/*    */   
/*    */   public ZoneActions(ZoneAction onEnter, ZoneAction onExit) {
/* 14 */     this.onEnter = onEnter;
/* 15 */     this.onExit = onExit;
/* 16 */     this.enabled = false;
/* 17 */     this.zoneListener = new ZoneListener();
/*    */   }
/*    */   
/*    */   public ZoneAction getOnEnter() {
/* 21 */     return this.onEnter;
/*    */   }
/*    */   
/*    */   public ZoneAction getOnExit() {
/* 25 */     return this.onExit;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 29 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setEnabled(boolean enabled) {
/* 33 */     this.enabled = enabled;
/*    */   }
/*    */   
/*    */   public ZoneListener getZoneListener() {
/* 37 */     return this.zoneListener;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\ZoneActions.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */