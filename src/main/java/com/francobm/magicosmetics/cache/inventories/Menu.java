/*     */ package com.francobm.magicosmetics.cache.inventories;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.Panel;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.BagMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.BalloonMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.ColoredMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.HatMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.TokenMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.WStickMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class Menu implements InventoryHolder {
/*  18 */   public static Map<String, Menu> inventories = new HashMap<>();
/*  19 */   private static final Map<String, Panel> panels = new HashMap<>();
/*     */   protected final String id;
/*     */   protected PlayerData playerData;
/*     */   protected final ContentMenu contentMenu;
/*     */   private String permission;
/*     */   
/*     */   public Menu(String id, ContentMenu contentMenu) {
/*  26 */     this.id = id;
/*  27 */     this.contentMenu = contentMenu;
/*  28 */     this.playerData = null;
/*  29 */     this.permission = "";
/*     */   }
/*     */   
/*     */   public Menu(PlayerData playerData, Menu menu) {
/*  33 */     this.id = menu.id;
/*  34 */     this.contentMenu = new ContentMenu(menu.getContentMenu().getTitle(), menu.getContentMenu().getSize(), menu.getContentMenu().getInventoryType(), menu.getContentMenu().getSlotMenu(), menu.getContentMenu().getPreviewSlot(), menu.getContentMenu().getResultSlot());
/*  35 */     this.permission = menu.permission;
/*  36 */     this.playerData = playerData;
/*     */   }
/*     */   
/*     */   public static Panel getPanel(String id) {
/*  40 */     return panels.get(id);
/*     */   }
/*     */   
/*     */   public void open() {
/*  44 */     if (this.playerData == null)
/*  45 */       return;  if (MagicCosmetics.getInstance().isPlaceholderAPI()) {
/*  46 */       getContentMenu().createInventory(this, MagicCosmetics.getInstance().getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), getContentMenu().getTitle()));
/*     */     } else {
/*  48 */       getContentMenu().createInventory(this);
/*     */     } 
/*  50 */     this.playerData.getOfflinePlayer().getPlayer().openInventory(getInventory());
/*  51 */     setItems();
/*     */   }
/*     */   
/*     */   public static void loadMenus() {
/*  55 */     inventories.clear();
/*  56 */     panels.clear();
/*  57 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  58 */     FileCreator menu = plugin.getMenus();
/*  59 */     int menus_count = 0;
/*  60 */     for (String key : menu.getConfigurationSection("menus.panels").getKeys(false)) {
/*  61 */       String character = menu.getString("menus.panels." + key);
/*  62 */       if (plugin.isItemsAdder()) {
/*  63 */         character = plugin.getItemsAdder().replaceFontImageWithoutColor(character);
/*     */       }
/*  65 */       if (plugin.isOraxen()) {
/*  66 */         character = plugin.getOraxen().replaceFontImages(character);
/*     */       }
/*  68 */       panels.put(key, new Panel(key, character));
/*     */     } 
/*  70 */     for (String key : menu.getConfigurationSection("menus").getKeys(false)) {
/*  71 */       HatMenu hatMenu; BagMenu bagMenu; WStickMenu wStickMenu; BalloonMenu balloonMenu; SprayMenu sprayMenu; FreeMenu freeMenu; ColoredMenu coloredMenu; FreeColoredMenu freeColoredMenu; TokenMenu tokenMenu; if (!menu.contains("menus." + key + ".title"))
/*  72 */         continue;  String perm = "";
/*  73 */       String title = "";
/*  74 */       int size = 0;
/*  75 */       InventoryType inventoryType = null;
/*  76 */       Map<Integer, SlotMenu> slotMenus = new HashMap<>();
/*     */       
/*  78 */       int startSlot = 0;
/*  79 */       int endSlot = 0;
/*  80 */       int pagesSlot = 0;
/*  81 */       Set<Integer> backButton = new HashSet<>();
/*  82 */       Set<Integer> nextButton = new HashSet<>();
/*  83 */       int previewSlot = 0;
/*  84 */       int resultSlot = 0;
/*  85 */       List<Integer> slotsUnavailable = new ArrayList<>();
/*  86 */       List<String> unavailableColors = new ArrayList<>();
/*  87 */       Items containItem = null;
/*  88 */       boolean drag = false;
/*  89 */       if (menu.contains("menus." + key + ".permission")) {
/*  90 */         perm = menu.getString("menus." + key + ".permission");
/*     */       }
/*  92 */       if (menu.contains("menus." + key + ".title")) {
/*  93 */         title = menu.getString("menus." + key + ".title");
/*  94 */         if (plugin.isItemsAdder()) {
/*  95 */           title = plugin.getItemsAdder().replaceFontImages(title);
/*     */         }
/*  97 */         if (plugin.isOraxen()) {
/*  98 */           title = plugin.getOraxen().replaceFontImages(title);
/*     */         }
/*     */       } 
/* 101 */       if (menu.contains("menus." + key + ".size")) {
/* 102 */         size = menu.getInt("menus." + key + ".size");
/*     */       }
/* 104 */       if (menu.contains("menus." + key + ".type")) {
/* 105 */         String type = menu.getString("menus." + key + ".type");
/*     */         try {
/* 107 */           inventoryType = InventoryType.valueOf(type);
/* 108 */         } catch (IllegalArgumentException exception) {
/* 109 */           plugin.getLogger().warning("Menu id '" + key + "' type: " + type + " Not Found.");
/*     */         } 
/*     */       } 
/* 112 */       if (inventoryType == null)
/*     */         continue; 
/* 114 */       if (menu.contains("menus." + key + ".start-slot")) {
/* 115 */         startSlot = menu.getInt("menus." + key + ".start-slot");
/*     */       }
/* 117 */       if (menu.contains("menus." + key + ".end-slot")) {
/* 118 */         endSlot = menu.getInt("menus." + key + ".end-slot");
/*     */       }
/* 120 */       if (menu.contains("menus." + key + ".pages-slot")) {
/* 121 */         pagesSlot = menu.getInt("menus." + key + ".pages-slot");
/*     */       }
/* 123 */       if (menu.contains("menus." + key + ".back-button-slot")) {
/* 124 */         backButton = menu.getIntegerSet("menus." + key + ".back-button-slot");
/*     */       }
/* 126 */       if (menu.contains("menus." + key + ".next-button-slot")) {
/* 127 */         nextButton = menu.getIntegerSet("menus." + key + ".next-button-slot");
/*     */       }
/* 129 */       if (menu.contains("menus." + key + ".unavailable-slots")) {
/* 130 */         slotsUnavailable = menu.getIntegerList("menus." + key + ".unavailable-slots");
/*     */       }
/* 132 */       if (menu.contains("menus." + key + ".unavailable-colors")) {
/* 133 */         unavailableColors = menu.getStringListWithComma("menus." + key + ".unavailable-colors");
/*     */       }
/* 135 */       if (menu.contains("menus." + key + ".preview-slot")) {
/* 136 */         previewSlot = menu.getInt("menus." + key + ".preview-slot");
/*     */       }
/* 138 */       if (menu.contains("menus." + key + ".result-slot")) {
/* 139 */         resultSlot = menu.getInt("menus." + key + ".result-slot");
/*     */       }
/* 141 */       if (menu.contains("menus." + key + ".result-slot")) {
/* 142 */         resultSlot = menu.getInt("menus." + key + ".result-slot");
/*     */       }
/* 144 */       if (menu.contains("menus." + key + ".contains-item")) {
/* 145 */         containItem = Items.getItem(menu.getString("menus." + key + ".contains-item"));
/*     */       }
/* 147 */       if (menu.contains("menus." + key + ".drag")) {
/* 148 */         drag = menu.getBoolean("menus." + key + ".drag");
/*     */       }
/*     */       
/* 151 */       for (String slot : menu.getConfigurationSection("menus." + key).getKeys(false)) {
/* 152 */         if (!menu.contains("menus." + key + "." + slot + ".slot"))
/* 153 */           continue;  int itemSlot = 0;
/* 154 */         Items item = null;
/* 155 */         List<ActionType> actionType = new ArrayList<>();
/* 156 */         Sound sound = null;
/* 157 */         List<String> commands = new ArrayList<>();
/* 158 */         String open_menu = "";
/* 159 */         String permission = "";
/* 160 */         if (menu.contains("menus." + key + "." + slot + ".slot")) {
/* 161 */           itemSlot = menu.getInt("menus." + key + "." + slot + ".slot");
/*     */         }
/* 163 */         if (menu.contains("menus." + key + "." + slot + ".item")) {
/* 164 */           String itemName = menu.getString("menus." + key + "." + slot + ".item");
/* 165 */           item = Items.getItem(itemName);
/*     */         } 
/* 167 */         if (menu.contains("menus." + key + "." + slot + ".action.type")) {
/* 168 */           String type = menu.getString("menus." + key + "." + slot + ".action.type");
/*     */           try {
/* 170 */             actionType.add(ActionType.valueOf(type.toUpperCase()));
/* 171 */           } catch (IllegalArgumentException exception) {
/* 172 */             plugin.getLogger().warning("Menu id '" + key + "' with slot '" + slot + "' Action " + type + " Not Found");
/*     */           } 
/*     */         } 
/* 175 */         if (menu.contains("menus." + key + "." + slot + ".action.types")) {
/* 176 */           List<String> types = menu.getStringListWF("menus." + key + "." + slot + ".action.types");
/* 177 */           for (String type : types) {
/*     */             try {
/* 179 */               actionType.add(ActionType.valueOf(type.toUpperCase()));
/* 180 */             } catch (IllegalArgumentException exception) {
/* 181 */               plugin.getLogger().warning("Menu id '" + key + "' with slot '" + slot + "' Action " + type + " Not Found");
/*     */             } 
/*     */           } 
/*     */         } 
/* 185 */         if (menu.contains("menus." + key + "." + slot + ".action.commands")) {
/* 186 */           commands = menu.getStringList("menus." + key + "." + slot + ".action.commands");
/*     */         }
/* 188 */         if (menu.contains("menus." + key + "." + slot + ".action.menu")) {
/* 189 */           open_menu = menu.getString("menus." + key + "." + slot + ".action.menu");
/*     */         }
/* 191 */         if (menu.contains("menus." + key + "." + slot + ".permission")) {
/* 192 */           permission = menu.getString("menus." + key + "." + slot + ".permission");
/*     */         }
/* 194 */         if (menu.contains("menus." + key + "." + slot + ".sound")) {
/* 195 */           String s = menu.getString("menus." + key + "." + slot + ".sound");
/* 196 */           sound = Sound.getSound(s);
/*     */         } 
/* 198 */         slotMenus.put(Integer.valueOf(itemSlot), new SlotMenu(itemSlot, item, commands, open_menu, sound, permission, actionType));
/*     */       } 
/* 200 */       ContentMenu contentMenu = new ContentMenu(title, size, inventoryType, slotMenus, previewSlot, resultSlot);
/* 201 */       switch (inventoryType) {
/*     */         case HAT:
/* 203 */           hatMenu = new HatMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 204 */           hatMenu.setPermission(perm);
/* 205 */           inventories.put(key, hatMenu);
/*     */           break;
/*     */         case BAG:
/* 208 */           bagMenu = new BagMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 209 */           bagMenu.setPermission(perm);
/* 210 */           inventories.put(key, bagMenu);
/*     */           break;
/*     */         case WALKING_STICK:
/* 213 */           wStickMenu = new WStickMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 214 */           wStickMenu.setPermission(perm);
/* 215 */           inventories.put(key, wStickMenu);
/*     */           break;
/*     */         case BALLOON:
/* 218 */           balloonMenu = new BalloonMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 219 */           balloonMenu.setPermission(perm);
/* 220 */           inventories.put(key, balloonMenu);
/*     */           break;
/*     */         case SPRAY:
/* 223 */           sprayMenu = new SprayMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 224 */           sprayMenu.setPermission(perm);
/* 225 */           inventories.put(key, sprayMenu);
/*     */           break;
/*     */         case FREE:
/* 228 */           freeMenu = new FreeMenu(key, contentMenu);
/* 229 */           freeMenu.setPermission(perm);
/* 230 */           inventories.put(key, freeMenu);
/*     */           break;
/*     */         case COLORED:
/* 233 */           coloredMenu = new ColoredMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable);
/* 234 */           coloredMenu.setPermission(perm);
/* 235 */           inventories.put(key, coloredMenu);
/*     */           break;
/*     */         case FREE_COLORED:
/* 238 */           freeColoredMenu = new FreeColoredMenu(key, contentMenu, startSlot, endSlot, backButton, nextButton, pagesSlot, slotsUnavailable, containItem, unavailableColors);
/* 239 */           freeColoredMenu.setPermission(perm);
/* 240 */           inventories.put(key, freeColoredMenu);
/*     */           break;
/*     */         case TOKEN:
/* 243 */           tokenMenu = new TokenMenu(key, contentMenu, drag);
/* 244 */           tokenMenu.setPermission(perm);
/* 245 */           inventories.put(key, tokenMenu);
/*     */           break;
/*     */       } 
/* 248 */       menus_count++;
/*     */     } 
/* 250 */     MagicCosmetics.getInstance().getLogger().info("Registered menus: " + menus_count);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 254 */     return this.id;
/*     */   }
/*     */   
/*     */   public ContentMenu getContentMenu() {
/* 258 */     return this.contentMenu;
/*     */   }
/*     */   
/*     */   public abstract void handleMenu(InventoryClickEvent paramInventoryClickEvent);
/*     */   
/*     */   public abstract void setItems();
/*     */   
/*     */   public void setItemInMenu(SlotMenu slotMenu) {
/* 266 */     if (this.contentMenu == null)
/* 267 */       return;  if (slotMenu.getItems() == null) {
/* 268 */       MagicCosmetics.getInstance().getLogger().info("Slot: " + slotMenu.getSlot() + " is Null!");
/*     */       return;
/*     */     } 
/* 271 */     Items items = slotMenu.getItems();
/* 272 */     items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 273 */     slotMenu.setItems(items);
/* 274 */     this.contentMenu.getInventory().setItem(slotMenu.getSlot(), slotMenu.getItems().getItemStack());
/*     */   }
/*     */   
/*     */   public void resetItems(List<Integer> ignoredSlots) {
/* 278 */     for (SlotMenu slots : getContentMenu().getSlotMenu().values()) {
/* 279 */       if (ignoredSlots.contains(Integer.valueOf(slots.getSlot())))
/* 280 */         continue;  this.contentMenu.getInventory().setItem(slots.getSlot(), XMaterial.AIR.parseItem());
/*     */     } 
/* 282 */     this.contentMenu.resetSlotMenu(ignoredSlots);
/*     */   }
/*     */   
/*     */   public void setItemInPaginatedMenu(SlotMenu slotMenu, int page, int index, String endsWith) {
/* 286 */     if (this.contentMenu == null)
/* 287 */       return;  if (slotMenu.getItems() == null) {
/* 288 */       MagicCosmetics.getInstance().getLogger().info("Slot: " + slotMenu.getSlot() + " is Null!");
/*     */       return;
/*     */     } 
/* 291 */     if (!slotMenu.getItems().getId().endsWith(endsWith)) {
/* 292 */       setItemInMenu(slotMenu);
/*     */       return;
/*     */     } 
/* 295 */     if (slotMenu.getItems().getItemStack() == null) {
/* 296 */       MagicCosmetics.getInstance().getLogger().info("Slot: " + slotMenu.getSlot() + " is Null!");
/*     */       return;
/*     */     } 
/* 299 */     if (!slotMenu.getItems().getId().equalsIgnoreCase("" + page + index + page + index))
/*     */       return; 
/* 301 */     this.contentMenu.getInventory().setItem(slotMenu.getSlot(), slotMenu.getItems().getItemStack());
/*     */   }
/*     */ 
/*     */   
/*     */   public Inventory getInventory() {
/* 306 */     return this.contentMenu.getInventory();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 311 */     return "Menu{id='" + this.id + "', playerCache=" + String.valueOf(this.playerData) + ", contentMenu=" + String.valueOf(this.contentMenu) + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPanel(int slot) {
/* 319 */     int panelId = 0;
/* 320 */     if (slot >= 0 && slot < 9) {
/* 321 */       panelId = 1;
/*     */     }
/* 323 */     if (slot >= 9 && slot < 18) {
/* 324 */       panelId = 2;
/*     */     }
/* 326 */     if (slot >= 18 && slot < 27) {
/* 327 */       panelId = 3;
/*     */     }
/* 329 */     if (slot >= 27 && slot < 36) {
/* 330 */       panelId = 4;
/*     */     }
/* 332 */     if (slot >= 36 && slot < 45) {
/* 333 */       panelId = 5;
/*     */     }
/* 335 */     if (slot >= 45 && slot < 54) {
/* 336 */       panelId = 6;
/*     */     }
/* 338 */     Panel panel = getPanel(String.valueOf(panelId));
/* 339 */     if (panel == null) return ""; 
/* 340 */     return panel.getCharacter();
/*     */   }
/*     */   
/*     */   public String getPermission() {
/* 344 */     return this.permission;
/*     */   }
/*     */   
/*     */   public void setPermission(String permission) {
/* 348 */     this.permission = permission;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\Menu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */