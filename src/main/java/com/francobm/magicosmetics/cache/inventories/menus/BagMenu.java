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
/*     */ public class BagMenu extends PaginatedMenu {
/*     */   public BagMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  22 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*     */   }
/*     */   
/*     */   public BagMenu(String id, ContentMenu contentMenu) {
/*  26 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public BagMenu(PlayerData playerData, Menu menu) {
/*  30 */     super(playerData, menu);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  35 */     Player player = (Player)event.getWhoClicked();
/*  36 */     int slot = event.getSlot();
/*  37 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/*  38 */     if (slotMenu == null)
/*  39 */       return;  if (slotMenu.getItems().getId().endsWith("_bag") && 
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
/*  61 */       slotMenu.playSound(player);
/*     */       
/*     */       return;
/*     */     } 
/*  65 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/*  66 */       slotMenu.playSound(player);
/*  67 */       int cosmetics = Cosmetic.getCosmeticCount(CosmeticType.BAG);
/*  68 */       if (this.index + 1 >= cosmetics) {
/*     */         return;
/*     */       }
/*     */       
/*  72 */       this.page++;
/*  73 */       open();
/*     */       return;
/*     */     } 
/*  76 */     slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/*  81 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  82 */     getContentMenu().getSlots().resetSlots();
/*  83 */     StringBuilder title = new StringBuilder();
/*     */     
/*  85 */     title.append(getContentMenu().getTitle());
/*  86 */     List<Cosmetic> cosmetics = Cosmetic.getCosmeticsUnHideByType(CosmeticType.BAG);
/*  87 */     if (!getBackSlot().isEmpty()) {
/*  88 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/*     */         
/*  90 */         if (this.page == 0) {
/*  91 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*  93 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/*  95 */         s.setSound(Sound.getSound("on_click_back_page"));
/*  96 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/*  99 */     if (getPagesSlot() != -1) {
/* 100 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/* 102 */     if (!cosmetics.isEmpty()) {
/* 103 */       int a = 0;
/* 104 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 105 */         SlotMenu slotMenu; this.index = getMaxItemsPerPage() * this.page + i;
/* 106 */         if (this.index >= cosmetics.size())
/* 107 */           break;  Cosmetic cosmetic = cosmetics.get(this.index);
/* 108 */         int slot = getStartSlot() + i + a;
/* 109 */         if (cosmetic == null)
/* 110 */           continue;  while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 111 */           slot++;
/* 112 */           a++;
/*     */         } 
/* 114 */         title.append(getContentMenu().getSlots().isSlot(slot));
/* 115 */         Items items = new Items("" + getPage() + this.index + "_bag", Items.getItem("bag-template").copyItem(this.playerData, cosmetic, cosmetic.getItemStack()));
/*     */         
/* 117 */         items.addVariable("%equip%", (this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.getMessages().getString("equip") : plugin.getMessages().getString("unequip"));
/* 118 */         items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 119 */         if (plugin.isPermissions()) {
/* 120 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 121 */           if (cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer())) {
/* 122 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 124 */             if (!this.showAllCosmeticsInMenu)
/* 125 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } else {
/* 128 */           items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", (this.playerData.getCosmeticById(cosmetic.getId()) != null) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
/* 129 */           if (this.playerData.getCosmeticById(cosmetic.getId()) != null) {
/* 130 */             title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
/*     */           } else {
/* 132 */             if (!this.showAllCosmeticsInMenu)
/* 133 */               continue;  title.append(plugin.unAva);
/*     */           } 
/*     */         } 
/* 136 */         title.append(getPanel(slot));
/* 137 */         if (this.playerData.getBag() != null) {
/* 138 */           if (this.playerData.getBag().getId().equalsIgnoreCase(cosmetic.getId())) {
/* 139 */             slotMenu = new SlotMenu(slot, items, Collections.singletonList("magiccos unset " + cosmetic.getId()), new ActionType[] { ActionType.PLAYER_COMMAND });
/*     */           }
/* 141 */           else if (cosmetic.isColored()) {
/* 142 */             slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */           } else {
/* 144 */             slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */           }
/*     */         
/*     */         }
/* 148 */         else if (cosmetic.isColored()) {
/* 149 */           slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/* 151 */           slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
/*     */         } 
/*     */         
/* 154 */         slotMenu.setSound(Sound.getSound("on_click_cosmetic"));
/* 155 */         slotMenu.setTempCosmetic(cosmetic);
/* 156 */         Token token = Token.getTokenByCosmetic(cosmetic.getId());
/* 157 */         slotMenu.setExchangeable((token != null && token.isExchangeable()));
/* 158 */         getContentMenu().addSlotMenu(slotMenu);
/* 159 */         setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_bag"); continue;
/*     */       } 
/*     */     } 
/* 162 */     if (!getNextSlot().isEmpty())
/*     */     {
/* 164 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 165 */         if (this.index + 1 >= cosmetics.size()) {
/* 166 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } else {
/*     */           
/* 169 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
/*     */         } 
/* 171 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 172 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 175 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 176 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_bag");
/*     */     }
/* 178 */     if (plugin.isPlaceholderAPI()) {
/* 179 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 182 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\BagMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */