/*    */ package com.francobm.magicosmetics.listeners;
/*    */ 
/*    */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*    */ import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
/*    */ import com.francobm.magicosmetics.cache.inventories.menus.TokenMenu;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*    */ import org.bukkit.event.inventory.InventoryDragEvent;
/*    */ import org.bukkit.event.inventory.InventoryType;
/*    */ import org.bukkit.inventory.InventoryHolder;
/*    */ 
/*    */ public class InventoryListener
/*    */   implements Listener {
/*    */   @EventHandler
/*    */   public void onDrag(InventoryDragEvent event) {
/* 18 */     InventoryHolder holder = event.getInventory().getHolder();
/* 19 */     if (holder instanceof FreeColoredMenu) {
/* 20 */       event.setCancelled(true);
/*    */     }
/* 22 */     if (holder instanceof TokenMenu) {
/* 23 */       TokenMenu menu = (TokenMenu)holder;
/* 24 */       if (!menu.isDrag())
/* 25 */         return;  event.setCancelled(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onClick(InventoryClickEvent event) {
/* 31 */     InventoryHolder holder = event.getInventory().getHolder();
/* 32 */     if (holder instanceof FreeColoredMenu) {
/* 33 */       FreeColoredMenu menu = (FreeColoredMenu)holder;
/* 34 */       menu.handleMenu(event);
/*    */       return;
/*    */     } 
/* 37 */     if (holder instanceof TokenMenu) {
/* 38 */       TokenMenu menu = (TokenMenu)holder;
/* 39 */       if (menu.isDrag()) {
/* 40 */         menu.handleMenu(event);
/*    */         return;
/*    */       } 
/*    */     } 
/* 44 */     if (holder instanceof Menu) {
/* 45 */       event.setCancelled(true);
/* 46 */       if (event.getCurrentItem() == null)
/* 47 */         return;  if (event.getClickedInventory() == null)
/* 48 */         return;  if (event.getClickedInventory().getType() == InventoryType.PLAYER)
/* 49 */         return;  Menu menu = (Menu)holder;
/* 50 */       menu.handleMenu(event);
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onClose(InventoryCloseEvent event) {
/* 56 */     InventoryHolder holder = event.getInventory().getHolder();
/* 57 */     if (holder instanceof FreeColoredMenu) {
/* 58 */       FreeColoredMenu menu = (FreeColoredMenu)holder;
/* 59 */       menu.returnItem();
/*    */     } 
/* 61 */     if (holder instanceof TokenMenu) {
/* 62 */       TokenMenu menu = (TokenMenu)holder;
/* 63 */       menu.returnItem();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\InventoryListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */