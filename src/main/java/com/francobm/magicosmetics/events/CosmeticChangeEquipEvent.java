/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.api.Cosmetic;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CosmeticChangeEquipEvent
/*    */   extends PlayerEvent
/*    */   implements Cancellable
/*    */ {
/*    */   private final Cosmetic newCosmetic;
/*    */   private final Cosmetic oldCosmetic;
/* 17 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private boolean isCancelled;
/*    */   
/*    */   public CosmeticChangeEquipEvent(Player player, Cosmetic oldCosmetic, Cosmetic newCosmetic) {
/* 21 */     super(player);
/* 22 */     this.oldCosmetic = oldCosmetic;
/* 23 */     this.newCosmetic = newCosmetic;
/* 24 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 29 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 33 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Cosmetic getOldCosmetic() {
/* 37 */     return this.oldCosmetic;
/*    */   }
/*    */   
/*    */   public Cosmetic getNewCosmetic() {
/* 41 */     return this.newCosmetic;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 46 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 51 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\CosmeticChangeEquipEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */