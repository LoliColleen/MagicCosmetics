/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.api.Cosmetic;
/*    */ import com.francobm.magicosmetics.api.CosmeticType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class CosmeticInventoryUpdateEvent
/*    */   extends PlayerEvent {
/* 12 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   
/*    */   private final CosmeticType cosmeticType;
/*    */   
/*    */   public CosmeticInventoryUpdateEvent(Player player, CosmeticType cosmeticType, Cosmetic cosmetic, ItemStack itemToChange) {
/* 17 */     super(player);
/* 18 */     this.cosmeticType = cosmeticType;
/* 19 */     this.cosmetic = cosmetic;
/* 20 */     this.itemToChange = itemToChange;
/*    */   }
/*    */   private final Cosmetic cosmetic; private final ItemStack itemToChange;
/*    */   
/*    */   public HandlerList getHandlers() {
/* 25 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 29 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Cosmetic getCosmetic() {
/* 33 */     return this.cosmetic;
/*    */   }
/*    */   
/*    */   public CosmeticType getCosmeticType() {
/* 37 */     return this.cosmeticType;
/*    */   }
/*    */   
/*    */   public ItemStack getItemToChange() {
/* 41 */     return this.itemToChange;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\CosmeticInventoryUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */