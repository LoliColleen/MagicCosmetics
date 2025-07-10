/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.cache.Zone;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ public class ZoneEnterEvent
/*    */   extends PlayerEvent implements Cancellable {
/*    */   private final Zone zone;
/* 12 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private boolean isCancelled;
/*    */   
/*    */   public ZoneEnterEvent(Player player, Zone zone) {
/* 16 */     super(player);
/* 17 */     this.zone = zone;
/* 18 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 23 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 27 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Zone getZone() {
/* 31 */     return this.zone;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 36 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 41 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\ZoneEnterEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */