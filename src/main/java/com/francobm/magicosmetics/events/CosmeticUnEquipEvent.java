/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.api.Cosmetic;
/*    */ import com.francobm.magicosmetics.api.CosmeticType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CosmeticUnEquipEvent
/*    */   extends PlayerEvent
/*    */   implements Cancellable
/*    */ {
/*    */   private final Cosmetic cosmetic;
/*    */   private final CosmeticType cosmeticType;
/* 18 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private boolean isCancelled;
/*    */   
/*    */   public CosmeticUnEquipEvent(Player player, Cosmetic cosmetic, CosmeticType cosmeticType) {
/* 22 */     super(player);
/* 23 */     this.cosmetic = cosmetic;
/* 24 */     this.cosmeticType = cosmeticType;
/* 25 */     this.isCancelled = false;
/*    */   }
/*    */   
/*    */   public CosmeticUnEquipEvent(Player player, Cosmetic cosmetic) {
/* 29 */     super(player);
/* 30 */     this.cosmetic = cosmetic;
/* 31 */     this.cosmeticType = cosmetic.getCosmeticType();
/* 32 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 37 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 41 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Cosmetic getCosmetic() {
/* 45 */     return this.cosmetic;
/*    */   }
/*    */   
/*    */   public CosmeticType getCosmeticType() {
/* 49 */     return this.cosmeticType;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 54 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 59 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\CosmeticUnEquipEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */