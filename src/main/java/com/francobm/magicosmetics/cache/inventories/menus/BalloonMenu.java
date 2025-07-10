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
/*     */ public class BalloonMenu extends PaginatedMenu {
/*     */   public BalloonMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  22 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*     */   }
/*     */   
/*     */   public BalloonMenu(String id, ContentMenu contentMenu) {
/*  26 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public BalloonMenu(PlayerData playerData, Menu menu) {
/*  30 */     super(playerData, menu);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  35 */     Player player = (Player)event.getWhoClicked();
/*  36 */     int slot = event.getSlot();
/*  37 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/*  38 */     if (slotMenu == null)
/*  39 */       return;  if (slotMenu.getItems().getId().endsWith("_balloon") && 
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
/*     */       
/*     */       return;
/*     */     } 
/*  64 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  65 */       int cosmetics = Cosmetic.getCosmeticCount(CosmeticType.BALLOON);
/*  66 */       slotMenu.playSound(player);
/*  67 */       if (this.index + 1 >= cosmetics) {
/*     */         return;
/*     */       }
/*     */       
/*  71 */       this.page++;
/*  72 */       open();
/*     */       return;
/*     */     } 
/*  75 */     slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/*  80 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  81 */     getContentMenu().getSlots().resetSlots();
/*  82 */     StringBuilder title = new StringBuilder();
/*     */     
/*  84 */     title.append(getContentMenu().getTitle());
/*  85 */     List<Cosmetic> cosmetics = Cosmetic.getCosmeticsUnHideByType(CosmeticType.BALLOON);
/*  86 */     if (!getBackSlot().isEmpty())
/*     */     {
/*  88 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/*  89 */         if (this.page == 0) {
/*  90 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*  92 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/*  94 */         s.setSound(Sound.getSound("on_click_back_page"));
/*  95 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/*  98 */     if (getPagesSlot() != -1) {
/*  99 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/* 101 */     if (!cosmetics.isEmpty()) {
/* 102 */       int a = 0;
/* 103 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 104 */         SlotMenu slotMenu; this.index = getMaxItemsPerPage() * this.page + i;
/* 105 */         if (this.index >= cosmetics.size())
/* 106 */           break;  Cosmetic cosmetic = cosmetics.get(this.index);
/* 107 */         int slot = getStartSlot() + i + a;
/* 108 */         if (cosmetic == null)
/* 109 */           continue;  while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 110 */           slot++;
/* 111 */           a++;
/*     */         } 
/* 113 */         title.append(getContentMenu().getSlots().isSlot(slot));
/* 114 */         Items items = new Items("" + getPage() + this.index + "_balloon", Items.getItem("balloon-template").copyItem(this.playerData, cosmetic, cosmetic.getItemStack()));
/*     */         
/* 116 */         items.addVariable("%equip%", (this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.getMessages().getString("equip") : plugin.getMessages().getString("unequip"));
/* 117 */         items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 118 */         if (plugin.isPermissions()) {
/* 119 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 120 */           if (cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer())) {
/* 121 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 123 */             if (!this.showAllCosmeticsInMenu)
/* 124 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } else {
/* 127 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", (this.playerData.getCosmeticById(cosmetic.getId()) != null) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 128 */           if (this.playerData.getCosmeticById(cosmetic.getId()) != null) {
/* 129 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 131 */             if (!this.showAllCosmeticsInMenu)
/* 132 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } 
/* 135 */         title.append(getPanel(slot));
/* 136 */         if (this.playerData.getBalloon() != null) {
/* 137 */           if (this.playerData.getBalloon().getId().equalsIgnoreCase(cosmetic.getId())) {
/* 138 */             slotMenu = new SlotMenu(slot, items, Collections.singletonList("magiccos unset " + cosmetic.getId()), new ActionType[] { ActionType.PLAYER_COMMAND });
/*     */           }
/* 140 */           else if (cosmetic.isColored()) {
/* 141 */             slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */           } else {
/* 143 */             slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */           }
/*     */         
/*     */         }
/* 147 */         else if (cosmetic.isColored()) {
/* 148 */           slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/* 150 */           slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */         } 
/*     */         
/* 153 */         slotMenu.setSound(Sound.getSound("on_click_cosmetic"));
/* 154 */         slotMenu.setTempCosmetic(cosmetic);
/* 155 */         Token token = Token.getTokenByCosmetic(cosmetic.getId());
/* 156 */         slotMenu.setExchangeable((token != null && token.isExchangeable()));
/* 157 */         getContentMenu().addSlotMenu(slotMenu);
/* 158 */         setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_balloon"); continue;
/*     */       } 
/*     */     } 
/* 161 */     if (!getNextSlot().isEmpty())
/*     */     {
/* 163 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 164 */         if (this.index + 1 >= cosmetics.size()) {
/* 165 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*     */           
/* 168 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 170 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 171 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 174 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 175 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_balloon");
/*     */     }
/* 177 */     if (plugin.isPlaceholderAPI()) {
/* 178 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 181 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\BalloonMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */