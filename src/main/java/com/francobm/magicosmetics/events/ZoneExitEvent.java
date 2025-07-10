/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.cache.Zone;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ public class ZoneExitEvent
/*    */   extends PlayerEvent
/*    */   implements Cancellable {
/*    */   private final Zone zone;
/*    */   private final Reason reason;
/* 14 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private boolean isCancelled;
/*    */   
/*    */   public ZoneExitEvent(Player player, Zone zone, Reason reason) {
/* 18 */     super(player);
/* 19 */     this.zone = zone;
/* 20 */     this.reason = reason;
/* 21 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 26 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 30 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Zone getZone() {
/* 34 */     return this.zone;
/*    */   }
/*    */   
/*    */   public Reason getReason() {
/* 38 */     return this.reason;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 43 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 48 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\ZoneExitEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */