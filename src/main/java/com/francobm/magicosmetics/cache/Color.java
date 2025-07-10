/*     */ package com.francobm.magicosmetics.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.DyeColor;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class Color
/*     */ {
/*  19 */   public static Map<String, Color> colors = new HashMap<>();
/*  20 */   private static final Map<String, Row> rows = new HashMap<>();
/*     */   private final String id;
/*     */   private final String name;
/*     */   private final String permission;
/*     */   private final org.bukkit.Color primaryColor;
/*     */   private final ItemStack primaryItem;
/*     */   private final String select;
/*     */   private final boolean withRow;
/*     */   private final List<SecondaryColor> secondaryColors;
/*     */   private final int slot;
/*     */   
/*     */   public Color(String id, String name, String permission, org.bukkit.Color primaryColor, ItemStack primaryItem, String select, boolean withRow, List<SecondaryColor> secondaryColors, int slot) {
/*  32 */     this.id = id;
/*  33 */     this.name = name;
/*  34 */     this.permission = permission;
/*  35 */     this.primaryColor = primaryColor;
/*  36 */     this.primaryItem = primaryItem;
/*  37 */     this.select = select;
/*  38 */     this.withRow = withRow;
/*  39 */     this.secondaryColors = secondaryColors;
/*  40 */     this.slot = slot;
/*     */   }
/*     */   
/*     */   public static Row getRow(String id) {
/*  44 */     return rows.get(id);
/*     */   }
/*     */   
/*     */   public static Color getColor(String id) {
/*  48 */     return colors.get(id);
/*     */   }
/*     */   
/*     */   public String getPermission() {
/*  52 */     return this.permission;
/*     */   }
/*     */   
/*     */   public static void loadColors() {
/*  56 */     colors.clear();
/*  57 */     rows.clear();
/*  58 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  59 */     int colors_count = 0;
/*  60 */     if (!plugin.getMenus().contains("colors"))
/*  61 */       return;  if (plugin.getMenus().contains("colors.rows")) {
/*  62 */       for (String key : plugin.getMenus().getConfigurationSection("colors.rows").getKeys(false)) {
/*  63 */         if (!plugin.getMenus().contains("colors.rows." + key))
/*  64 */           continue;  String character = plugin.getMenus().getString("colors.rows." + key + ".character");
/*  65 */         String selected = plugin.getMenus().getString("colors.rows." + key + ".selected");
/*  66 */         if (plugin.isItemsAdder()) {
/*  67 */           character = plugin.getItemsAdder().replaceFontImages(character);
/*  68 */           selected = plugin.getItemsAdder().replaceFontImages(selected);
/*     */         } 
/*  70 */         if (plugin.isOraxen()) {
/*  71 */           character = plugin.getOraxen().replaceFontImages(character);
/*  72 */           selected = plugin.getOraxen().replaceFontImages(selected);
/*     */         } 
/*  74 */         rows.put(key, new Row(key, character, selected));
/*     */       } 
/*     */     }
/*  77 */     for (String key : plugin.getMenus().getConfigurationSection("colors").getKeys(false)) {
/*  78 */       if (!plugin.getMenus().contains("colors." + key + ".name"))
/*  79 */         continue;  int slot = 0;
/*  80 */       String name = "";
/*  81 */       String permission = "";
/*  82 */       org.bukkit.Color primaryColor = null;
/*  83 */       ItemStack primaryItem = null;
/*  84 */       String select = "";
/*  85 */       boolean withRow = true;
/*  86 */       List<SecondaryColor> secondaryColors = new ArrayList<>();
/*  87 */       if (plugin.getMenus().contains("colors." + key + ".name")) {
/*  88 */         name = plugin.getMenus().getString("colors." + key + ".name");
/*     */       }
/*  90 */       if (plugin.getMenus().contains("colors." + key + ".permission")) {
/*  91 */         permission = plugin.getMenus().getString("colors." + key + ".permission");
/*     */       }
/*  93 */       if (plugin.getMenus().contains("colors." + key + ".primary-item")) {
/*  94 */         String displayName = "";
/*  95 */         List<String> lore = null;
/*  96 */         boolean unbreakable = false;
/*  97 */         boolean glow = false;
/*  98 */         boolean hide_attributes = false;
/*  99 */         int modelData = -1;
/* 100 */         String texture = "";
/* 101 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.texture")) {
/* 102 */           texture = plugin.getMenus().getString("colors." + key + ".primary-item.texture");
/*     */         }
/* 104 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.display")) {
/* 105 */           displayName = plugin.getMenus().getString("colors." + key + ".primary-item.display");
/* 106 */           if (plugin.isItemsAdder())
/* 107 */             displayName = plugin.getItemsAdder().replaceFontImages(displayName); 
/* 108 */           if (plugin.isOraxen())
/* 109 */             displayName = plugin.getOraxen().replaceFontImages(displayName); 
/*     */         } 
/* 111 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.material")) {
/* 112 */           String item = plugin.getMenus().getString("colors." + key + ".primary-item.material");
/*     */           try {
/* 114 */             primaryItem = XMaterial.valueOf(item.toUpperCase()).parseItem();
/* 115 */           } catch (IllegalArgumentException exception) {
/* 116 */             plugin.getLogger().warning("Primary Item Material '" + item + "' in Color '" + key + "' Not Found!");
/*     */           } 
/*     */         } 
/* 119 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.lore")) {
/* 120 */           lore = plugin.getMenus().getStringList("colors." + key + ".primary-item.lore");
/* 121 */           if (plugin.isItemsAdder()) {
/* 122 */             List<String> lore2 = new ArrayList<>();
/* 123 */             for (String l : lore) {
/* 124 */               lore2.add(plugin.getItemsAdder().replaceFontImages(l));
/*     */             }
/* 126 */             lore.clear();
/* 127 */             lore.addAll(lore2);
/*     */           } 
/* 129 */           if (plugin.isOraxen()) {
/* 130 */             List<String> lore2 = new ArrayList<>();
/* 131 */             for (String l : lore) {
/* 132 */               lore2.add(plugin.getOraxen().replaceFontImages(l));
/*     */             }
/* 134 */             lore.clear();
/* 135 */             lore.addAll(lore2);
/*     */           } 
/*     */         } 
/* 138 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.glow")) {
/* 139 */           glow = plugin.getMenus().getBoolean("colors." + key + ".primary-item.glow");
/*     */         }
/* 141 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.hide-attributes")) {
/* 142 */           hide_attributes = plugin.getMenus().getBoolean("colors." + key + ".primary-item.hide-attributes");
/*     */         }
/* 144 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.unbreakable")) {
/* 145 */           unbreakable = plugin.getMenus().getBoolean("colors." + key + ".primary-item.unbreakable");
/*     */         }
/* 147 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.modeldata")) {
/* 148 */           modelData = plugin.getMenus().getInt("colors." + key + ".primary-item.modeldata");
/*     */         }
/* 150 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.item-adder")) {
/* 151 */           if (!plugin.isItemsAdder()) {
/* 152 */             plugin.getLogger().warning("Item Adder plugin Not Found skipping color '" + key + "'");
/*     */             continue;
/*     */           } 
/* 155 */           String id = plugin.getMenus().getString("colors." + key + ".primary-item.item-adder");
/* 156 */           ItemStack ia = plugin.getItemsAdder().getCustomItemStack(id);
/* 157 */           if (ia == null) {
/* 158 */             plugin.getLogger().warning("IA Item: '" + id + "' Not Found skipping...");
/*     */             continue;
/*     */           } 
/* 161 */           primaryItem = ia.clone();
/* 162 */           modelData = -1;
/*     */         } 
/* 164 */         if (plugin.getMenus().contains("colors." + key + ".primary-item.oraxen")) {
/* 165 */           if (!plugin.isOraxen()) {
/* 166 */             plugin.getLogger().warning("Oraxen plugin Not Found skipping color '" + key + "'");
/*     */             continue;
/*     */           } 
/* 169 */           String id = plugin.getMenus().getString("colors." + key + ".primary-item.oraxen");
/* 170 */           ItemStack oraxen = plugin.getOraxen().getItemStackById(id);
/* 171 */           if (oraxen == null) {
/* 172 */             plugin.getLogger().warning("Oraxen item:  '" + id + "' Not Found skipping...");
/*     */             continue;
/*     */           } 
/* 175 */           primaryItem = oraxen.clone();
/* 176 */           modelData = -1;
/*     */         } 
/* 178 */         ItemMeta itemMeta = primaryItem.getItemMeta();
/* 179 */         itemMeta.setDisplayName(displayName);
/* 180 */         itemMeta.setLore(lore);
/* 181 */         if (glow) {
/* 182 */           primaryItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
/* 183 */           itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
/*     */         } 
/* 185 */         if (hide_attributes) {
/* 186 */           itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE });
/*     */         }
/* 188 */         itemMeta.setUnbreakable(unbreakable);
/* 189 */         if (modelData != -1) {
/* 190 */           itemMeta.setCustomModelData(Integer.valueOf(modelData));
/*     */         }
/* 192 */         primaryItem.setItemMeta(itemMeta);
/* 193 */         if (primaryItem.getType() == XMaterial.PLAYER_HEAD.parseMaterial() && texture != null) {
/* 194 */           primaryItem = plugin.getVersion().getCustomHead(primaryItem, texture);
/*     */         }
/*     */       } 
/* 197 */       if (plugin.getMenus().contains("colors." + key + ".primary-color")) {
/* 198 */         String color = plugin.getMenus().getString("colors." + key + ".primary-color");
/*     */         try {
/* 200 */           primaryColor = DyeColor.valueOf(color).getColor();
/* 201 */         } catch (IllegalArgumentException exception) {
/* 202 */           plugin.getLogger().warning("Primary Color: '" + color + "' Not Found Parsing to Hex Color...");
/*     */           try {
/* 204 */             primaryColor = Utils.hex2Rgb(color);
/* 205 */           } catch (IllegalArgumentException ex) {
/* 206 */             plugin.getLogger().warning("Primary Color Hex: " + color + " Not Found Skipping...");
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/* 211 */       if (plugin.getMenus().contains("colors." + key + ".select")) {
/* 212 */         select = plugin.getMenus().getString("colors." + key + ".select");
/* 213 */         if (plugin.isItemsAdder()) {
/* 214 */           select = plugin.getItemsAdder().replaceFontImages(select);
/*     */         }
/* 216 */         if (plugin.isOraxen()) {
/* 217 */           select = plugin.getOraxen().replaceFontImages(select);
/*     */         }
/*     */       } 
/* 220 */       if (plugin.getMenus().contains("colors." + key + ".with-row")) {
/* 221 */         withRow = plugin.getMenus().getBoolean("colors." + key + ".with-row");
/*     */       }
/* 223 */       if (plugin.getMenus().contains("colors." + key + ".secondary-colors")) {
/* 224 */         secondaryColors = plugin.getMenus().getSecondaryColor("colors." + key + ".secondary-colors");
/*     */       }
/* 226 */       if (plugin.getMenus().contains("colors." + key + ".slot")) {
/* 227 */         slot = plugin.getMenus().getInt("colors." + key + ".slot");
/*     */       }
/*     */       
/* 230 */       colors.put(key, new Color(key, name, permission, primaryColor, primaryItem, select, withRow, secondaryColors, slot));
/* 231 */       colors_count++;
/*     */     } 
/* 233 */     MagicCosmetics.getInstance().getLogger().info("Registered colors: " + colors_count);
/*     */   }
/*     */   
/*     */   public ItemStack getPrimaryItem() {
/* 237 */     return this.primaryItem;
/*     */   }
/*     */   
/*     */   public boolean hasPermission(Player player) {
/* 241 */     if (this.permission.isEmpty()) return true; 
/* 242 */     return player.hasPermission(this.permission);
/*     */   }
/*     */   
/*     */   public boolean isPrimaryItem() {
/* 246 */     return (this.primaryItem != null);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 250 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 254 */     return this.name;
/*     */   }
/*     */   
/*     */   public org.bukkit.Color getPrimaryColor() {
/* 258 */     return this.primaryColor;
/*     */   }
/*     */   
/*     */   public List<SecondaryColor> getSecondaryColors() {
/* 262 */     return this.secondaryColors;
/*     */   }
/*     */   
/*     */   public int getSlot() {
/* 266 */     return this.slot;
/*     */   }
/*     */   
/*     */   public String getSelectWithRow() {
/* 270 */     Row row = getRow(String.valueOf(this.slot % 9));
/* 271 */     return (row == null) ? this.select : (row.getCharacter() + row.getCharacter());
/*     */   }
/*     */   
/*     */   public String getSelect() {
/* 275 */     if (this.withRow) {
/* 276 */       return getSelectWithRow();
/*     */     }
/* 278 */     return this.select;
/*     */   }
/*     */   
/*     */   public Row getRow() {
/* 282 */     return getRow(String.valueOf(this.slot % 9));
/*     */   }
/*     */   
/*     */   public boolean isWithRow() {
/* 286 */     return this.withRow;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\Color.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */