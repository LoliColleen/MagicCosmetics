/*     */ package com.francobm.magicosmetics.cache.inventories.menus;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.Token;
/*     */ import com.francobm.magicosmetics.cache.inventories.ActionType;
/*     */ import com.francobm.magicosmetics.cache.inventories.ContentMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.cache.inventories.SlotMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ 
/*     */ public class WStickMenu extends PaginatedMenu {
/*     */   public WStickMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  22 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*     */   }
/*     */   
/*     */   public WStickMenu(String id, ContentMenu contentMenu) {
/*  26 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public WStickMenu(PlayerData playerData, Menu menu) {
/*  30 */     super(playerData, menu);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  35 */     Player player = (Player)event.getWhoClicked();
/*  36 */     int slot = event.getSlot();
/*  37 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/*  38 */     if (slotMenu == null)
/*  39 */       return;  if (slotMenu.getItems().getId().endsWith("_wstick") && 
/*  40 */       slotMenu.isExchangeable()) {
/*  41 */       Cosmetic cosmetic = slotMenu.getTempCosmetic();
/*  42 */       if (cosmetic != null && this.playerData.hasCosmeticById(cosmetic.getId()) && 
/*  43 */         event.getClick() == ClickType.SHIFT_LEFT) {
/*  44 */         if (!slotMenu.action(player, ActionType.REMOVE_COSMETIC_ADD_TOKEN))
/*  45 */           return;  setItems();
/*     */         
/*     */         return;
/*     */       } 
/*  49 */       slotMenu.action(player);
/*     */       
/*     */       return;
/*     */     } 
/*  53 */     if (getBackSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  54 */       slotMenu.playSound(player);
/*  55 */       if (this.page == 0) {
/*     */         return;
/*     */       }
/*     */       
/*  59 */       this.page--;
/*  60 */       open();
/*     */       return;
/*     */     } 
/*  63 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  64 */       slotMenu.playSound(player);
/*  65 */       int cosmetics = Cosmetic.getCosmeticCount(CosmeticType.WALKING_STICK);
/*  66 */       if (this.index + 1 >= cosmetics) {
/*     */         return;
/*     */       }
/*     */       
/*  70 */       this.page++;
/*  71 */       open();
/*     */       return;
/*     */     } 
/*  74 */     slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/*  79 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  80 */     getContentMenu().getSlots().resetSlots();
/*  81 */     StringBuilder title = new StringBuilder();
/*     */     
/*  83 */     title.append(getContentMenu().getTitle());
/*  84 */     List<Cosmetic> cosmetics = Cosmetic.getCosmeticsUnHideByType(CosmeticType.WALKING_STICK);
/*  85 */     if (!getBackSlot().isEmpty())
/*     */     {
/*  87 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/*  88 */         if (this.page == 0) {
/*  89 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*  91 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/*  93 */         s.setSound(Sound.getSound("on_click_back_page"));
/*  94 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/*  97 */     if (getPagesSlot() != -1) {
/*  98 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/* 100 */     if (!cosmetics.isEmpty()) {
/* 101 */       int a = 0;
/* 102 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 103 */         SlotMenu slotMenu; this.index = getMaxItemsPerPage() * this.page + i;
/* 104 */         if (this.index >= cosmetics.size())
/* 105 */           break;  Cosmetic cosmetic = cosmetics.get(this.index);
/* 106 */         int slot = getStartSlot() + i + a;
/* 107 */         if (cosmetic == null)
/* 108 */           continue;  while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 109 */           slot++;
/* 110 */           a++;
/*     */         } 
/* 112 */         title.append(getContentMenu().getSlots().isSlot(slot));
/* 113 */         Items items = new Items("" + getPage() + this.index + "_wstick", Items.getItem("wstick-template").copyItem(this.playerData, cosmetic, cosmetic.getItemStack()));
/*     */         
/* 115 */         items.addVariable("%equip%", (this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.getMessages().getString("equip") : plugin.getMessages().getString("unequip"));
/* 116 */         items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 117 */         if (plugin.isPermissions()) {
/* 118 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 119 */           if (cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer())) {
/* 120 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 122 */             if (!this.showAllCosmeticsInMenu)
/* 123 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } else {
/* 126 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", (this.playerData.getCosmeticById(cosmetic.getId()) != null) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 127 */           if (this.playerData.getCosmeticById(cosmetic.getId()) != null) {
/* 128 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 130 */             if (!this.showAllCosmeticsInMenu)
/* 131 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } 
/* 134 */         title.append(getPanel(slot));
/* 135 */         if (this.playerData.getWStick() != null) {
/* 136 */           if (this.playerData.getWStick().getId().equalsIgnoreCase(cosmetic.getId())) {
/* 137 */             slotMenu = new SlotMenu(slot, items, Collections.singletonList("magiccos unset " + cosmetic.getId()), new ActionType[] { ActionType.PLAYER_COMMAND });
/*     */           }
/* 139 */           else if (cosmetic.isColored()) {
/* 140 */             slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */           } else {
/* 142 */             slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */           }
/*     */         
/*     */         }
/* 146 */         else if (cosmetic.isColored()) {
/* 147 */           slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/* 149 */           slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */         } 
/*     */         
/* 152 */         slotMenu.setSound(Sound.getSound("on_click_cosmetic"));
/* 153 */         slotMenu.setTempCosmetic(cosmetic);
/* 154 */         Token token = Token.getTokenByCosmetic(cosmetic.getId());
/* 155 */         slotMenu.setExchangeable((token != null && token.isExchangeable()));
/* 156 */         getContentMenu().addSlotMenu(slotMenu);
/* 157 */         setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_wstick"); continue;
/*     */       } 
/*     */     } 
/* 160 */     if (!getNextSlot().isEmpty())
/*     */     {
/* 162 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 163 */         if (this.index + 1 >= cosmetics.size()) {
/* 164 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*     */           
/* 167 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 169 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 170 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 173 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 174 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_wstick");
/*     */     }
/* 176 */     if (plugin.isPlaceholderAPI()) {
/* 177 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 180 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\WStickMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */