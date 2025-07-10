/*     */ package com.francobm.magicosmetics.cache.inventories;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.Color;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.SecondaryColor;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.Token;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ public class SlotMenu
/*     */ {
/*     */   private final int slot;
/*     */   private Items items;
/*     */   private final List<ActionType> actionType;
/*     */   private final List<String> commands;
/*     */   private final String menu;
/*     */   private final SlotMenu slotMenu;
/*     */   
/*     */   public SlotMenu(int slot, Items items, List<String> commands, String menu, SlotMenu slotMenu, Cosmetic cosmetic, Token token, Sound sound, ActionType... actionType) {
/*  34 */     this.slot = slot;
/*  35 */     this.items = items;
/*  36 */     this.actionType = Arrays.asList(actionType);
/*  37 */     this.commands = commands;
/*  38 */     this.menu = menu;
/*  39 */     this.slotMenu = slotMenu;
/*  40 */     this.cosmetic = cosmetic;
/*  41 */     this.token = token;
/*  42 */     this.sound = sound;
/*     */   }
/*     */   private final Cosmetic cosmetic; private final Token token; private Sound sound; private String permission; private boolean exchangeable; private Cosmetic tempCosmetic; private ItemStack oldToken;
/*     */   public SlotMenu(int slot, Items items, List<String> commands, String menu, ActionType... actionType) {
/*  46 */     this.slot = slot;
/*  47 */     this.items = items;
/*  48 */     this.actionType = Arrays.asList(actionType);
/*  49 */     this.commands = commands;
/*  50 */     this.menu = menu;
/*  51 */     this.slotMenu = null;
/*  52 */     this.cosmetic = null;
/*  53 */     this.token = null;
/*  54 */     this.sound = null;
/*  55 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, List<String> commands, String menu, Sound sound, String permission, List<ActionType> actionTypes) {
/*  59 */     this.slot = slot;
/*  60 */     this.items = items;
/*  61 */     this.actionType = actionTypes;
/*  62 */     this.commands = commands;
/*  63 */     this.menu = menu;
/*  64 */     this.slotMenu = null;
/*  65 */     this.cosmetic = null;
/*  66 */     this.token = null;
/*  67 */     this.sound = sound;
/*  68 */     this.permission = permission;
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, List<String> commands, ActionType... actionType) {
/*  72 */     this.slot = slot;
/*  73 */     this.items = items;
/*  74 */     this.actionType = Arrays.asList(actionType);
/*  75 */     this.commands = commands;
/*  76 */     this.menu = "";
/*  77 */     this.slotMenu = null;
/*  78 */     this.cosmetic = null;
/*  79 */     this.token = null;
/*  80 */     this.sound = null;
/*  81 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, String menu, ActionType... actionType) {
/*  85 */     this.slot = slot;
/*  86 */     this.items = items;
/*  87 */     this.actionType = Arrays.asList(actionType);
/*  88 */     this.commands = new ArrayList<>();
/*  89 */     this.menu = menu;
/*  90 */     this.slotMenu = null;
/*  91 */     this.cosmetic = null;
/*  92 */     this.token = null;
/*  93 */     this.sound = null;
/*  94 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, SlotMenu slotMenu, ActionType... actionType) {
/*  98 */     this.slot = slot;
/*  99 */     this.items = items;
/* 100 */     this.actionType = Arrays.asList(actionType);
/* 101 */     this.commands = new ArrayList<>();
/* 102 */     this.menu = "";
/* 103 */     this.slotMenu = slotMenu;
/* 104 */     this.cosmetic = null;
/* 105 */     this.token = null;
/* 106 */     this.sound = null;
/* 107 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, Cosmetic cosmetic, ActionType... actionType) {
/* 111 */     this.slot = slot;
/* 112 */     this.items = items;
/* 113 */     this.actionType = Arrays.asList(actionType);
/* 114 */     this.commands = new ArrayList<>();
/* 115 */     this.menu = "";
/* 116 */     this.slotMenu = null;
/* 117 */     this.cosmetic = cosmetic;
/* 118 */     this.token = null;
/* 119 */     this.sound = null;
/* 120 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public SlotMenu(int slot, Items items, Token token, ItemStack oldToken, ActionType... actionType) {
/* 124 */     this.slot = slot;
/* 125 */     this.items = items;
/* 126 */     this.actionType = Arrays.asList(actionType);
/* 127 */     this.commands = new ArrayList<>();
/* 128 */     this.menu = "";
/* 129 */     this.slotMenu = null;
/* 130 */     this.cosmetic = null;
/* 131 */     this.oldToken = oldToken;
/* 132 */     this.token = token;
/* 133 */     this.sound = null;
/* 134 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public int getSlot() {
/* 138 */     return this.slot;
/*     */   }
/*     */   
/*     */   public Items getItems() {
/* 142 */     return this.items;
/*     */   }
/*     */   
/*     */   public List<ActionType> getActionType() {
/* 146 */     return this.actionType;
/*     */   }
/*     */   
/*     */   public List<String> getCommands() {
/* 150 */     return this.commands;
/*     */   }
/*     */   
/*     */   public void action(Player player) {
/* 154 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 155 */     if (!this.permission.isEmpty() && 
/* 156 */       !player.hasPermission(this.permission)) {
/* 157 */       player.sendMessage(plugin.prefix + plugin.prefix);
/*     */       
/*     */       return;
/*     */     } 
/* 161 */     playSound(player);
/* 162 */     for (ActionType actionType : this.actionType) {
/* 163 */       switch (actionType) {
/*     */         case OPEN_MENU:
/* 165 */           openMenu(player);
/*     */         
/*     */         case CLOSE_MENU:
/* 168 */           closeMenu(player);
/*     */         
/*     */         case PLAYER_COMMAND:
/*     */         case CONSOLE_COMMAND:
/*     */         case COMMAND:
/* 173 */           runCommands(player);
/*     */         
/*     */         case ADD_ITEM_MENU:
/* 176 */           addItemMenu(player);
/*     */         
/*     */         case PREVIEW_ITEM:
/* 179 */           previewItem(player);
/*     */         
/*     */         case REMOVE_TOKEN_ADD_COSMETIC:
/* 182 */           removeTokenAddCosmetic(player);
/*     */         
/*     */         case UPDATE_OLD_TOKEN:
/* 185 */           updateOldToken(player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Player player, ActionType actionType) {
/*     */     boolean allow;
/* 194 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 195 */     if (!this.permission.isEmpty() && 
/* 196 */       !player.hasPermission(this.permission)) {
/* 197 */       player.sendMessage(plugin.prefix + plugin.prefix);
/* 198 */       return false;
/*     */     } 
/*     */     
/* 201 */     switch (actionType) {
/*     */       case OPEN_MENU:
/* 203 */         openMenu(player);
/*     */         break;
/*     */       case CLOSE_MENU:
/* 206 */         closeMenu(player);
/*     */         break;
/*     */       case REFRESH:
/* 209 */         refreshMenu(player);
/*     */         break;
/*     */       case PLAYER_COMMAND:
/*     */       case CONSOLE_COMMAND:
/*     */       case COMMAND:
/* 214 */         runCommands(player);
/*     */         break;
/*     */       case ADD_ITEM_MENU:
/* 217 */         addItemMenu(player);
/*     */         break;
/*     */       case PREVIEW_ITEM:
/* 220 */         previewItem(player);
/*     */         break;
/*     */       case REMOVE_TOKEN_ADD_COSMETIC:
/* 223 */         removeTokenAddCosmetic(player);
/*     */         break;
/*     */       case REMOVE_COSMETIC_ADD_TOKEN:
/* 226 */         allow = removeCosmeticAddToken(player);
/* 227 */         if (allow)
/* 228 */           playSound(player); 
/* 229 */         return allow;
/*     */       case UPDATE_OLD_TOKEN:
/* 231 */         updateOldToken(player);
/*     */         break;
/*     */     } 
/* 234 */     playSound(player);
/* 235 */     return true;
/*     */   }
/*     */   
/*     */   private void runCommands(Player player) {
/* 239 */     for (String command : this.commands) {
/* 240 */       command = command.replace("%player%", player.getName());
/* 241 */       for (ActionType actionType : this.actionType) {
/* 242 */         switch (actionType) {
/*     */           case CONSOLE_COMMAND:
/*     */           case COMMAND:
/* 245 */             Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
/* 246 */             if (command.startsWith("magiccos unset ")) {
/* 247 */               refreshMenu(player);
/*     */             }
/*     */           
/*     */           case PLAYER_COMMAND:
/* 251 */             player.performCommand(command);
/* 252 */             if (command.startsWith("magiccos unset ")) {
/* 253 */               refreshMenu(player);
/*     */             }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeTokenAddCosmetic(Player player) {
/* 262 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 263 */     if (playerData.removeTokenInPlayer()) {
/* 264 */       MagicCosmetics.getInstance().getCosmeticsManager().changeCosmetic(player, this.token.getCosmetic(), this.token.getTokenType());
/*     */     }
/* 266 */     closeMenu(player);
/*     */   }
/*     */   
/*     */   private void updateOldToken(Player player) {
/* 270 */     for (int i = 0; i < this.oldToken.getAmount(); i++) {
/* 271 */       player.getInventory().addItem(new ItemStack[] { this.token.getItemStack() });
/*     */     } 
/* 273 */     player.getInventory().removeItem(new ItemStack[] { this.oldToken });
/* 274 */     closeMenu(player);
/*     */   }
/*     */   
/*     */   private boolean removeCosmeticAddToken(Player player) {
/* 278 */     if (this.tempCosmetic == null) return false; 
/* 279 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 280 */     if (!playerData.hasCosmeticById(this.tempCosmetic.getId())) return false; 
/* 281 */     return MagicCosmetics.getInstance().getCosmeticsManager().unUseCosmetic(player, this.tempCosmetic.getId());
/*     */   }
/*     */   
/*     */   private void previewItem(Player player) {
/* 285 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 286 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 287 */     if (plugin.isPermissions()) {
/* 288 */       if (!this.cosmetic.hasPermission(player)) {
/* 289 */         if (playerData.isZone()) {
/* 290 */           MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic);
/*     */         }
/* 292 */         closeMenu(player);
/*     */         return;
/*     */       } 
/* 295 */       if (playerData.isZone()) {
/* 296 */         MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic);
/*     */       }
/* 298 */       if (!this.cosmetic.isColorBlocked()) {
/* 299 */         MagicCosmetics.getInstance().getCosmeticsManager().equipCosmetic(player, this.cosmetic, null);
/*     */       }
/* 301 */       closeMenu(player);
/*     */       return;
/*     */     } 
/* 304 */     if (playerData.getCosmeticById(this.cosmetic.getId()) == null) {
/* 305 */       if (playerData.isZone()) {
/* 306 */         MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic);
/*     */       }
/* 308 */       closeMenu(player);
/*     */       return;
/*     */     } 
/* 311 */     playerData.removeCosmetic(this.cosmetic.getId());
/* 312 */     playerData.addCosmetic(this.cosmetic);
/* 313 */     if (!this.cosmetic.isColorBlocked()) {
/* 314 */       MagicCosmetics.getInstance().getCosmeticsManager().equipCosmetic(player, this.cosmetic.getId(), null, false);
/*     */     }
/* 316 */     if (playerData.isZone()) {
/* 317 */       MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic);
/*     */     }
/* 319 */     closeMenu(player);
/*     */   }
/*     */   
/*     */   private void addItemMenu(Player player) {
/* 323 */     InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
/* 324 */     if (holder instanceof Menu) {
/* 325 */       Menu menu = (Menu)holder;
/* 326 */       menu.getContentMenu().addSlotMenu(this.slotMenu);
/* 327 */       menu.setItemInMenu(this.slotMenu);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void playSound(Player player) {
/* 332 */     if (this.sound == null) {
/*     */       return;
/*     */     }
/* 335 */     Utils.sendSound(player, this.sound);
/*     */   }
/*     */   
/*     */   private void refreshMenu(Player player) {
/* 339 */     if (player.getOpenInventory().getTopInventory().getHolder() instanceof Menu) {
/* 340 */       Menu menu = (Menu)player.getOpenInventory().getTopInventory().getHolder();
/* 341 */       menu.setItems();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void openMenu(Player player) {
/* 346 */     String[] split = this.menu.split("\\|");
/* 347 */     if (split.length == 3) {
/* 348 */       Color color = Color.getColor(split[1]);
/* 349 */       Cosmetic cosmetic = Cosmetic.getCloneCosmetic(split[2]);
/* 350 */       if (color == null) {
/* 351 */         MagicCosmetics.getInstance().getLogger().info("Color Null");
/*     */         return;
/*     */       } 
/* 354 */       if (cosmetic == null) {
/* 355 */         MagicCosmetics.getInstance().getLogger().info("Cosmetic Null");
/*     */         return;
/*     */       } 
/* 358 */       MagicCosmetics.getInstance().getCosmeticsManager().openMenuColor(player, split[0], color, cosmetic);
/*     */       return;
/*     */     } 
/* 361 */     if (split.length == 2) {
/* 362 */       if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof FreeColoredMenu))
/* 363 */         return;  FreeColoredMenu menu = (FreeColoredMenu)player.getOpenInventory().getTopInventory().getHolder();
/* 364 */       Color color = Color.getColor(split[1]);
/* 365 */       if (color == null) {
/* 366 */         MagicCosmetics.getInstance().getLogger().info("Color Null");
/*     */         return;
/*     */       } 
/* 369 */       menu.setPage(0);
/* 370 */       menu.setColor(color);
/* 371 */       menu.setSecondaryColor((SecondaryColor)null);
/* 372 */       menu.setItems();
/*     */       return;
/*     */     } 
/* 375 */     MagicCosmetics.getInstance().getCosmeticsManager().openMenu(player, this.menu);
/*     */   }
/*     */   
/*     */   public void setSound(Sound sound) {
/* 379 */     this.sound = sound;
/*     */   }
/*     */   
/*     */   public Sound getSound() {
/* 383 */     return this.sound;
/*     */   }
/*     */   
/*     */   private void closeMenu(Player player) {
/* 387 */     player.closeInventory();
/*     */   }
/*     */   
/*     */   public String getMenu() {
/* 391 */     return this.menu;
/*     */   }
/*     */   
/*     */   public SlotMenu getSlotMenu() {
/* 395 */     return this.slotMenu;
/*     */   }
/*     */   
/*     */   public String getPermission() {
/* 399 */     return this.permission;
/*     */   }
/*     */   
/*     */   public void setPermission(String permission) {
/* 403 */     this.permission = permission;
/*     */   }
/*     */   
/*     */   public void setItems(Items items) {
/* 407 */     this.items = items;
/*     */   }
/*     */   
/*     */   public Token getToken() {
/* 411 */     return this.token;
/*     */   }
/*     */   
/*     */   public ItemStack getOldToken() {
/* 415 */     return this.oldToken;
/*     */   }
/*     */   
/*     */   public void setExchangeable(boolean exchangeable) {
/* 419 */     this.exchangeable = exchangeable;
/*     */   }
/*     */   
/*     */   public boolean isExchangeable() {
/* 423 */     return this.exchangeable;
/*     */   }
/*     */   
/*     */   public void setTempCosmetic(Cosmetic tempCosmetic) {
/* 427 */     this.tempCosmetic = tempCosmetic;
/*     */   }
/*     */   
/*     */   public Cosmetic getTempCosmetic() {
/* 431 */     return this.tempCosmetic;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\SlotMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */