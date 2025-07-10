/*    */ package com.francobm.magicosmetics.cache.inventories.menus;
/*    */ 
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import com.francobm.magicosmetics.cache.inventories.ContentMenu;
/*    */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*    */ import com.francobm.magicosmetics.cache.inventories.SlotMenu;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ 
/*    */ public class FreeMenu
/*    */   extends Menu {
/*    */   public FreeMenu(String id, ContentMenu contentMenu) {
/* 13 */     super(id, contentMenu);
/*    */   }
/*    */   
/*    */   public FreeMenu(PlayerData playerData, Menu menu) {
/* 17 */     super(playerData, menu);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleMenu(InventoryClickEvent event) {
/* 22 */     Player player = (Player)event.getWhoClicked();
/* 23 */     int slot = event.getSlot();
/* 24 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/* 25 */     if (slotMenu == null)
/* 26 */       return;  slotMenu.action(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setItems() {
/* 31 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 32 */       slotMenu.getItems().addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 33 */       setItemInMenu(slotMenu);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\FreeMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */