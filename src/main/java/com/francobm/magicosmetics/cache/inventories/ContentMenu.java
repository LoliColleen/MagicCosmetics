/*     */ package com.francobm.magicosmetics.cache.inventories;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ 
/*     */ public class ContentMenu
/*     */ {
/*     */   private Inventory inventory;
/*     */   private String title;
/*     */   private final int size;
/*     */   private final InventoryType inventoryType;
/*     */   private final Map<Integer, SlotMenu> slotMenu;
/*     */   private final int previewSlot;
/*     */   private final int resultSlot;
/*     */   private final Slots slots;
/*     */   
/*     */   public ContentMenu(String title, int size, InventoryType inventoryType, Map<Integer, SlotMenu> slotMenu) {
/*  23 */     this.title = title;
/*  24 */     this.size = size;
/*  25 */     this.inventoryType = inventoryType;
/*  26 */     this.slotMenu = slotMenu;
/*  27 */     this.resultSlot = 0;
/*  28 */     this.previewSlot = 0;
/*  29 */     this.slots = new Slots();
/*     */   }
/*     */   
/*     */   public ContentMenu(String title, int size, InventoryType inventoryType, Map<Integer, SlotMenu> slotMenu, int previewSlot, int resultSlot) {
/*  33 */     this.title = title;
/*  34 */     this.size = size;
/*  35 */     this.inventoryType = inventoryType;
/*  36 */     this.slotMenu = slotMenu;
/*  37 */     this.previewSlot = previewSlot;
/*  38 */     this.resultSlot = resultSlot;
/*  39 */     this.slots = new Slots();
/*     */   }
/*     */   
/*     */   public ContentMenu(String title, int size, InventoryType inventoryType) {
/*  43 */     this.title = title;
/*  44 */     this.size = size;
/*  45 */     this.inventoryType = inventoryType;
/*  46 */     this.slotMenu = new HashMap<>();
/*  47 */     this.previewSlot = 0;
/*  48 */     this.resultSlot = 0;
/*  49 */     this.slots = new Slots();
/*     */   }
/*     */   
/*     */   public ContentMenu getClone() {
/*  53 */     Map<Integer, SlotMenu> slotMenus = new HashMap<>(this.slotMenu);
/*  54 */     return new ContentMenu(this.title, this.size, this.inventoryType, slotMenus, this.previewSlot, this.resultSlot);
/*     */   }
/*     */   
/*     */   public void createInventory(InventoryHolder inventoryHolder) {
/*  58 */     this.inventory = Bukkit.createInventory(inventoryHolder, 9 * this.size, this.title);
/*     */   }
/*     */   
/*     */   public void createInventory(InventoryHolder inventoryHolder, String title) {
/*  62 */     this.inventory = Bukkit.createInventory(inventoryHolder, 9 * this.size, title);
/*     */   }
/*     */   
/*     */   public Inventory getInventory() {
/*  66 */     return this.inventory;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  70 */     return this.title;
/*     */   }
/*     */   
/*     */   public int getSize() {
/*  74 */     return this.size;
/*     */   }
/*     */   
/*     */   public InventoryType getInventoryType() {
/*  78 */     return this.inventoryType;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/*  82 */     this.title = title;
/*     */   }
/*     */   
/*     */   public Map<Integer, SlotMenu> getSlotMenu() {
/*  86 */     return this.slotMenu;
/*     */   }
/*     */   
/*     */   public SlotMenu getSlotMenuBySlot(int slot) {
/*  90 */     return this.slotMenu.get(Integer.valueOf(slot));
/*     */   }
/*     */   
/*     */   public void removeSlotMenu(int slot) {
/*  94 */     this.slotMenu.remove(Integer.valueOf(slot));
/*     */   }
/*     */   
/*     */   public void resetSlotMenu(List<Integer> ignoredSlots) {
/*  98 */     Iterator<SlotMenu> slotMenus = this.slotMenu.values().iterator();
/*  99 */     while (slotMenus.hasNext()) {
/* 100 */       SlotMenu slotMenu = slotMenus.next();
/* 101 */       if (ignoredSlots.contains(Integer.valueOf(slotMenu.getSlot())))
/* 102 */         continue;  slotMenus.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addSlotMenu(SlotMenu slotMenu) {
/* 107 */     this.slotMenu.put(Integer.valueOf(slotMenu.getSlot()), slotMenu);
/*     */   }
/*     */   
/*     */   public int getResultSlot() {
/* 111 */     return this.resultSlot;
/*     */   }
/*     */   
/*     */   public int getPreviewSlot() {
/* 115 */     return this.previewSlot;
/*     */   }
/*     */   
/*     */   public Slots getSlots() {
/* 119 */     return this.slots;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\ContentMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */