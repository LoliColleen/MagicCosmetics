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
/*     */ public class HatMenu extends PaginatedMenu {
/*     */   public HatMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  22 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*     */   }
/*     */   
/*     */   public HatMenu(String id, ContentMenu contentMenu) {
/*  26 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public HatMenu(PlayerData playerData, Menu menu) {
/*  30 */     super(playerData, menu);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  35 */     Player player = (Player)event.getWhoClicked();
/*  36 */     int slot = event.getSlot();
/*  37 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/*  38 */     if (slotMenu == null)
/*  39 */       return;  if (slotMenu.getItems().getId().endsWith("_hat") && 
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
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (getBackSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  63 */       slotMenu.playSound(player);
/*  64 */       if (this.page == 0) {
/*     */         return;
/*     */       }
/*     */       
/*  68 */       this.page--;
/*  69 */       open();
/*     */       return;
/*     */     } 
/*  72 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  73 */       slotMenu.playSound(player);
/*  74 */       int cosmetics = Cosmetic.getCosmeticCount(CosmeticType.HAT);
/*  75 */       if (this.index + 1 >= cosmetics) {
/*     */         return;
/*     */       }
/*     */       
/*  79 */       this.page++;
/*  80 */       open();
/*     */       return;
/*     */     } 
/*  83 */     slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/*  88 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  89 */     getContentMenu().getSlots().resetSlots();
/*  90 */     StringBuilder title = new StringBuilder();
/*     */     
/*  92 */     title.append(getContentMenu().getTitle());
/*  93 */     List<Cosmetic> cosmetics = Cosmetic.getCosmeticsUnHideByType(CosmeticType.HAT);
/*  94 */     if (!getBackSlot().isEmpty())
/*     */     {
/*  96 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/*  97 */         if (this.page == 0) {
/*  98 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/* 100 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 102 */         s.setSound(Sound.getSound("on_click_back_page"));
/* 103 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 106 */     if (getPagesSlot() != -1) {
/* 107 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/* 109 */     if (!cosmetics.isEmpty()) {
/* 110 */       int a = 0;
/* 111 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 112 */         SlotMenu slotMenu; this.index = getMaxItemsPerPage() * this.page + i;
/* 113 */         if (this.index >= cosmetics.size())
/* 114 */           break;  Cosmetic cosmetic = cosmetics.get(this.index);
/* 115 */         int slot = getStartSlot() + i + a;
/* 116 */         if (cosmetic == null)
/* 117 */           continue;  while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 118 */           slot++;
/* 119 */           a++;
/*     */         } 
/* 121 */         title.append(getContentMenu().getSlots().isSlot(slot));
/* 122 */         Items items = new Items("" + getPage() + this.index + "_hat", Items.getItem("hat-template").copyItem(this.playerData, cosmetic, cosmetic.getItemStack()));
/*     */         
/* 124 */         items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 125 */         items.addVariable("%equip%", (this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.getMessages().getString("equip") : plugin.getMessages().getString("unequip"));
/* 126 */         if (plugin.isPermissions()) {
/* 127 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 128 */           if (cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer())) {
/* 129 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 131 */             if (!this.showAllCosmeticsInMenu)
/* 132 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } else {
/* 135 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", (this.playerData.getCosmeticById(cosmetic.getId()) != null) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 136 */           if (this.playerData.getCosmeticById(cosmetic.getId()) != null) {
/* 137 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 139 */             if (!this.showAllCosmeticsInMenu)
/* 140 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } 
/* 143 */         title.append(getPanel(slot));
/* 144 */         if (this.playerData.getHat() != null) {
/* 145 */           if (this.playerData.getHat().getId().equalsIgnoreCase(cosmetic.getId())) {
/* 146 */             slotMenu = new SlotMenu(slot, items, Collections.singletonList("magiccos unset " + cosmetic.getId()), new ActionType[] { ActionType.PLAYER_COMMAND });
/*     */           }
/* 148 */           else if (cosmetic.isColored()) {
/* 149 */             slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */           } else {
/* 151 */             slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */           }
/*     */         
/*     */         }
/* 155 */         else if (cosmetic.isColored()) {
/* 156 */           slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/* 158 */           slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */         } 
/*     */         
/* 161 */         slotMenu.setSound(Sound.getSound("on_click_cosmetic"));
/* 162 */         slotMenu.setTempCosmetic(cosmetic);
/* 163 */         Token token = Token.getTokenByCosmetic(cosmetic.getId());
/* 164 */         slotMenu.setExchangeable((token != null && token.isExchangeable()));
/* 165 */         getContentMenu().addSlotMenu(slotMenu);
/* 166 */         setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_hat"); continue;
/*     */       } 
/*     */     } 
/* 169 */     if (!getNextSlot().isEmpty())
/*     */     {
/* 171 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 172 */         if (this.index + 1 >= cosmetics.size()) {
/* 173 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*     */           
/* 176 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 178 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 179 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 182 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 183 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_hat");
/*     */     }
/* 185 */     if (plugin.isPlaceholderAPI()) {
/* 186 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 189 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\HatMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */