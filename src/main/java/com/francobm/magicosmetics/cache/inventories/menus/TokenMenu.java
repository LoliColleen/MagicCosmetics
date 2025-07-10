/*     */ package com.francobm.magicosmetics.cache.inventories.menus;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.Token;
/*     */ import com.francobm.magicosmetics.cache.inventories.ActionType;
/*     */ import com.francobm.magicosmetics.cache.inventories.ContentMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.cache.inventories.SlotMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class TokenMenu
/*     */   extends Menu {
/*     */   private boolean drag;
/*     */   private ItemStack itemStack;
/*     */   
/*     */   public TokenMenu(String id, ContentMenu contentMenu, boolean drag) {
/*  26 */     super(id, contentMenu);
/*  27 */     this.drag = drag;
/*     */   }
/*     */ 
/*     */   
/*     */   public TokenMenu(PlayerData playerData, Menu menu) {
/*  32 */     super(playerData, menu);
/*  33 */     this.drag = ((TokenMenu)menu).isDrag();
/*     */   }
/*     */   
/*     */   public TokenMenu getClone(PlayerData playerData) {
/*  37 */     TokenMenu tokenMenuClone = new TokenMenu(getId(), getContentMenu().getClone(), isDrag());
/*  38 */     tokenMenuClone.playerData = playerData;
/*  39 */     return tokenMenuClone;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  44 */     Player player = (Player)event.getWhoClicked();
/*  45 */     if (isDrag()) {
/*  46 */       if (event.getClickedInventory() == null)
/*  47 */         return;  int i = event.getSlot();
/*  48 */       if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
/*  49 */         if (event.getClick() != ClickType.LEFT) {
/*  50 */           event.setCancelled(true);
/*     */           return;
/*     */         } 
/*     */         return;
/*     */       } 
/*  55 */       if (event.getClick() != ClickType.LEFT) {
/*  56 */         event.setCancelled(true);
/*     */         return;
/*     */       } 
/*  59 */       if (getContentMenu().getPreviewSlot() == i) {
/*  60 */         if (event.getCursor().getType() != XMaterial.AIR.parseMaterial()) {
/*  61 */           Token token = Token.getTokenByItem(event.getCursor());
/*  62 */           if (token == null) {
/*  63 */             token = Token.getOldTokenByItem(event.getCursor());
/*  64 */             if (token == null) {
/*  65 */               event.setCancelled(true);
/*     */               return;
/*     */             } 
/*  68 */             this.itemStack = event.getCursor().clone();
/*  69 */             Items items1 = new Items(event.getCursor());
/*  70 */             items1.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/*  71 */             SlotMenu slotMenu2 = new SlotMenu(getContentMenu().getPreviewSlot(), items1, "", new ActionType[0]);
/*  72 */             slotMenu2.setSound(Sound.getSound("on_click_token"));
/*  73 */             slotMenu2.playSound(player);
/*  74 */             getContentMenu().addSlotMenu(slotMenu2);
/*     */             
/*  76 */             items1 = new Items(token.getItemStack().clone());
/*  77 */             slotMenu2 = new SlotMenu(getContentMenu().getResultSlot(), items1, token, event.getCursor(), new ActionType[0]);
/*  78 */             slotMenu2.setSound(Sound.getSound("on_click_token_result"));
/*  79 */             getContentMenu().addSlotMenu(slotMenu2);
/*  80 */             setItemInMenu(slotMenu2);
/*     */             return;
/*     */           } 
/*  83 */           this.itemStack = event.getCursor().clone();
/*  84 */           Items items = new Items(token.getItemStack().clone());
/*  85 */           items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/*  86 */           SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), items, "", new ActionType[0]);
/*  87 */           slotMenu1.setSound(Sound.getSound("on_click_token"));
/*  88 */           getContentMenu().addSlotMenu(slotMenu1);
/*  89 */           slotMenu1.playSound(player);
/*     */           
/*  91 */           items = new Items(Cosmetic.getCloneCosmetic(token.getCosmetic()).getItemStack());
/*  92 */           slotMenu1 = new SlotMenu(getContentMenu().getResultSlot(), items, token, null, new ActionType[0]);
/*  93 */           slotMenu1.setSound(Sound.getSound("on_click_token_result"));
/*  94 */           getContentMenu().addSlotMenu(slotMenu1);
/*  95 */           setItemInMenu(slotMenu1);
/*     */           return;
/*     */         } 
/*  98 */         if (event.getCurrentItem() == null)
/*  99 */           return;  this.itemStack = null;
/* 100 */         getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 101 */         getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/* 102 */         event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
/*     */         return;
/*     */       } 
/* 105 */       if (getContentMenu().getResultSlot() == i) {
/* 106 */         if (event.getCurrentItem() == null) {
/* 107 */           event.setCancelled(true);
/*     */           return;
/*     */         } 
/* 110 */         event.setCancelled(true);
/* 111 */         SlotMenu slotMenu1 = getContentMenu().getSlotMenuBySlot(i);
/* 112 */         if (slotMenu1 == null)
/* 113 */           return;  Token token = slotMenu1.getToken();
/* 114 */         if (slotMenu1.getOldToken() != null) {
/* 115 */           this.itemStack = null;
/* 116 */           slotMenu1.playSound(player);
/* 117 */           slotMenu1.action(player, ActionType.UPDATE_OLD_TOKEN);
/* 118 */           getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 119 */           getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/* 120 */           event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
/* 121 */           event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
/*     */           return;
/*     */         } 
/* 124 */         boolean redeem = Token.removeToken(player, this.itemStack);
/* 125 */         if (!redeem)
/* 126 */           return;  if (this.itemStack.getAmount() > token.getItemStack().getAmount()) {
/* 127 */           ItemStack newItem = token.getItemStack().clone();
/* 128 */           newItem.setAmount(this.itemStack.getAmount() - token.getItemStack().getAmount());
/* 129 */           player.getInventory().addItem(new ItemStack[] { newItem });
/*     */         } 
/* 131 */         this.itemStack = null;
/* 132 */         slotMenu1.playSound(player);
/* 133 */         getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 134 */         getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/* 135 */         event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
/* 136 */         event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
/* 137 */         MagicCosmetics.getInstance().getCosmeticsManager().changeCosmetic(player, token.getCosmetic(), token.getTokenType());
/*     */         return;
/*     */       } 
/* 140 */       event.setCancelled(true);
/*     */       return;
/*     */     } 
/* 143 */     int slot = event.getSlot();
/* 144 */     SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/* 145 */     if (slotMenu == null)
/* 146 */       return;  slotMenu.action(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems() {
/* 151 */     setup();
/* 152 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 153 */       setItemInMenu(slotMenu);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setup() {
/* 158 */     if (isDrag())
/* 159 */       return;  ItemStack itemToken = this.playerData.getTokenInPlayer();
/* 160 */     if (itemToken == null)
/* 161 */       return;  Token token = Token.getTokenByItem(itemToken);
/* 162 */     if (token == null) {
/* 163 */       token = Token.getOldTokenByItem(itemToken);
/* 164 */       if (token == null) {
/* 165 */         getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 166 */         getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/*     */         return;
/*     */       } 
/* 169 */       Items items1 = new Items(itemToken);
/* 170 */       items1.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 171 */       SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), items1, "", new ActionType[] { ActionType.CLOSE_MENU });
/* 172 */       slotMenu1.setSound(Sound.getSound("on_click_token"));
/* 173 */       getContentMenu().addSlotMenu(slotMenu1);
/* 174 */       items1 = new Items(token.getItemStack());
/* 175 */       slotMenu1 = new SlotMenu(getContentMenu().getResultSlot(), items1, token, itemToken, new ActionType[] { ActionType.UPDATE_OLD_TOKEN });
/* 176 */       slotMenu1.setSound(Sound.getSound("on_click_token_result"));
/* 177 */       getContentMenu().addSlotMenu(slotMenu1);
/*     */       return;
/*     */     } 
/* 180 */     Items items = new Items(token.getItemStack());
/* 181 */     items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 182 */     SlotMenu slotMenu = new SlotMenu(getContentMenu().getPreviewSlot(), items, "", new ActionType[] { ActionType.CLOSE_MENU });
/* 183 */     slotMenu.setSound(Sound.getSound("on_click_token"));
/* 184 */     getContentMenu().addSlotMenu(slotMenu);
/* 185 */     items = new Items(Cosmetic.getCloneCosmetic(token.getCosmetic()).getItemStack());
/* 186 */     slotMenu = new SlotMenu(getContentMenu().getResultSlot(), items, token, null, new ActionType[] { ActionType.REMOVE_TOKEN_ADD_COSMETIC });
/* 187 */     slotMenu.setSound(Sound.getSound("on_click_token_result"));
/* 188 */     getContentMenu().addSlotMenu(slotMenu);
/*     */   }
/*     */   
/*     */   public void returnItem() {
/* 192 */     if (this.itemStack == null)
/* 193 */       return;  Player player = this.playerData.getOfflinePlayer().getPlayer();
/* 194 */     if (player == null)
/* 195 */       return;  if (player.getInventory().firstEmpty() == -1) {
/* 196 */       player.getWorld().dropItem(player.getLocation(), this.itemStack);
/* 197 */       this.itemStack = null;
/* 198 */       getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 199 */       getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/*     */       return;
/*     */     } 
/* 202 */     player.getInventory().addItem(new ItemStack[] { this.itemStack });
/* 203 */     this.itemStack = null;
/* 204 */     getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 205 */     getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/*     */   }
/*     */   
/*     */   public void setDrag(boolean drag) {
/* 209 */     this.drag = drag;
/*     */   }
/*     */   
/*     */   public boolean isDrag() {
/* 213 */     return this.drag;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\TokenMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */