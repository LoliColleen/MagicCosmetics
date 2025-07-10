/*     */ package com.francobm.magicosmetics.cache.inventories.menus;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.Color;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.SecondaryColor;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.inventories.ActionType;
/*     */ import com.francobm.magicosmetics.cache.inventories.ContentMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.cache.inventories.SlotMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class ColoredMenu extends PaginatedMenu {
/*     */   private Color color;
/*     */   
/*     */   public ColoredMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  24 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*     */   }
/*     */   private SecondaryColor secondaryColor; private Cosmetic cosmetic;
/*     */   public ColoredMenu(String id, ContentMenu contentMenu) {
/*  28 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public ColoredMenu(PlayerData playerData, Menu menu, Color color, Cosmetic cosmetic) {
/*  32 */     super(playerData, menu);
/*  33 */     this.color = color;
/*  34 */     this.cosmetic = cosmetic;
/*  35 */     this.secondaryColor = color.getSecondaryColors().get(0);
/*     */   }
/*     */   
/*     */   public ColoredMenu getClone(PlayerData playerData, Color color, Cosmetic cosmetic) {
/*  39 */     ColoredMenu coloredMenu = new ColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable());
/*  40 */     coloredMenu.playerData = playerData;
/*  41 */     coloredMenu.setColor(color);
/*  42 */     coloredMenu.cosmetic = cosmetic;
/*  43 */     coloredMenu.secondaryColor = color.getSecondaryColors().get(0);
/*  44 */     return coloredMenu;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  49 */     Player player = (Player)event.getWhoClicked();
/*  50 */     int slot = event.getSlot();
/*  51 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/*  52 */     if (slotMenu == null)
/*  53 */       return;  if (slotMenu.getItems().getId().endsWith("_colored")) {
/*  54 */       setSecondaryColor(slotMenu.getItems().getColor());
/*  55 */       setItems();
/*     */     } 
/*  57 */     if (getBackSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  58 */       slotMenu.playSound(player);
/*  59 */       if (this.page == 0) {
/*     */         return;
/*     */       }
/*     */       
/*  63 */       this.page--;
/*  64 */       open();
/*     */       return;
/*     */     } 
/*  67 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  68 */       slotMenu.playSound(player);
/*  69 */       if (this.index + 1 >= this.color.getSecondaryColors().size()) {
/*     */         return;
/*     */       }
/*     */       
/*  73 */       this.page++;
/*  74 */       open();
/*     */       return;
/*     */     } 
/*  77 */     slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/*  82 */     getContentMenu().getSlots().resetSlots();
/*  83 */     if (!getBackSlot().isEmpty())
/*     */     {
/*  85 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/*  86 */         if (this.page == 0) {
/*  87 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*  89 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/*  91 */         s.setSound(Sound.getSound("on_click_back_page"));
/*  92 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/*  95 */     if (getPagesSlot() != -1) {
/*  96 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/*  98 */     String sPrimaryColor = setup();
/*  99 */     StringBuilder title = new StringBuilder();
/* 100 */     title.append(getContentMenu().getTitle());
/* 101 */     title.append(sPrimaryColor);
/* 102 */     String[] selected = getSelectedList();
/* 103 */     if (!this.color.getSecondaryColors().isEmpty()) {
/* 104 */       int a = 0;
/* 105 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 106 */         this.index = getMaxItemsPerPage() * this.page + i;
/* 107 */         if (this.index >= this.color.getSecondaryColors().size())
/* 108 */           break;  SecondaryColor dyeColor = this.color.getSecondaryColors().get(this.index);
/* 109 */         int slot = getStartSlot() + i + a;
/* 110 */         if (dyeColor != null) {
/* 111 */           if (getSecondaryColor() == null && 
/* 112 */             i == 0) {
/* 113 */             setSecondaryColor(dyeColor);
/*     */           }
/*     */           
/* 116 */           while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 117 */             slot++;
/* 118 */             a++;
/*     */           } 
/* 120 */           Cosmetic cosmetic = Cosmetic.getCloneCosmetic(this.cosmetic.getId());
/* 121 */           cosmetic.setColor(dyeColor.getColor());
/* 122 */           if (!this.color.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) || !dyeColor.hasPermission(this.playerData.getOfflinePlayer().getPlayer()))
/* 123 */             cosmetic.setColorBlocked(true); 
/* 124 */           Items items = new Items("" + getPage() + this.index + "_colored", Items.getItem("color-template").colorItem(this.playerData.getOfflinePlayer().getPlayer(), dyeColor, this.secondaryColor));
/* 125 */           items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 126 */           if (dyeColor.getColor().asRGB() == this.secondaryColor.getColor().asRGB())
/*     */           {
/* 128 */             title.append(selected[i]);
/*     */           }
/* 130 */           Items resultItem = new Items(cosmetic.getItemColor());
/* 131 */           SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/* 132 */           result.setSound(Sound.getSound("on_click_cosmetic_preview"));
/* 133 */           if (i == 0) {
/* 134 */             getContentMenu().addSlotMenu(result);
/*     */           }
/* 136 */           SlotMenu slotMenu = new SlotMenu(slot, items, result, new ActionType[] { ActionType.ADD_ITEM_MENU });
/* 137 */           slotMenu.setSound(Sound.getSound("on_click_item_colored"));
/* 138 */           getContentMenu().addSlotMenu(slotMenu);
/* 139 */           setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_colored");
/*     */         } 
/*     */       } 
/* 142 */     }  if (!getNextSlot().isEmpty())
/*     */     {
/* 144 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 145 */         if (this.index + 1 >= this.color.getSecondaryColors().size()) {
/* 146 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*     */           
/* 149 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 151 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 152 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 155 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 156 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_colored");
/*     */     }
/*     */     
/* 159 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 160 */     if (plugin.isPlaceholderAPI()) {
/* 161 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 164 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */   
/*     */   private String setup() {
/* 168 */     String title = "";
/* 169 */     Items items = new Items(this.cosmetic.getItemStack());
/* 170 */     items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 171 */     int previewSlot = getContentMenu().getPreviewSlot();
/*     */     
/* 173 */     if (previewSlot != -1) {
/* 174 */       SlotMenu slotMenu = new SlotMenu(previewSlot, items, "", new ActionType[] { ActionType.CLOSE_MENU });
/* 175 */       getContentMenu().addSlotMenu(slotMenu);
/*     */     } 
/* 177 */     for (Color color : Color.colors.values()) {
/* 178 */       if (color.isPrimaryItem()) {
/* 179 */         items = new Items(color.getId(), Items.getItem("color-template").copyItem(color, this.color));
/*     */       } else {
/* 181 */         items = new Items(color.getId(), Items.getItem("color-template").colorItem(color, this.color));
/* 182 */       }  if (color.getId().equalsIgnoreCase(this.color.getId())) {
/* 183 */         title = color.getSelect();
/*     */       }
/* 185 */       if (!color.getName().isEmpty()) {
/* 186 */         ItemMeta itemMeta = items.getItemStack().getItemMeta();
/* 187 */         if (itemMeta != null) {
/* 188 */           itemMeta.setDisplayName(color.getName());
/*     */         }
/* 190 */         items.getItemStack().setItemMeta(itemMeta);
/*     */       } 
/* 192 */       SlotMenu slotMenu = new SlotMenu(color.getSlot(), items, getId() + "|" + getId() + "|" + items.getId(), new ActionType[] { ActionType.OPEN_MENU });
/* 193 */       slotMenu.setSound(Sound.getSound("on_click_item_colored"));
/* 194 */       getContentMenu().addSlotMenu(slotMenu);
/*     */     } 
/* 196 */     return title;
/*     */   }
/*     */   
/*     */   public void setSecondaryColor(Color color) {
/* 200 */     this.secondaryColor = new SecondaryColor(color);
/*     */   }
/*     */   
/*     */   public void setSecondaryColor(SecondaryColor color) {
/* 204 */     this.secondaryColor = color;
/*     */   }
/*     */   
/*     */   public Cosmetic getCosmetic() {
/* 208 */     return this.cosmetic;
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 212 */     return this.color;
/*     */   }
/*     */   
/*     */   public SecondaryColor getSecondaryColor() {
/* 216 */     return this.secondaryColor;
/*     */   }
/*     */   
/*     */   public String[] getSelectedList() {
/* 220 */     return this.color.getRow().getSelected().split(",");
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 224 */     this.color = color;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\ColoredMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */