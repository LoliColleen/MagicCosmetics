/*     */ package com.francobm.magicosmetics.api;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Spray;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.BackPackEngine;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.balloons.BalloonEngine;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.balloons.BalloonIA;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.utils.OffsetModel;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.attribute.AttributeModifier;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.FireworkEffectMeta;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.LeatherArmorMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.inventory.meta.PotionMeta;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ 
/*     */ public abstract class Cosmetic {
/*  36 */   public static Map<String, Cosmetic> cosmetics = new LinkedHashMap<>();
/*     */   private final String id;
/*     */   private String name;
/*     */   protected ItemStack itemStack;
/*     */   private int modelData;
/*     */   private final CosmeticType cosmeticType;
/*     */   private final boolean colored;
/*     */   private boolean defaultColor;
/*     */   private Color color;
/*     */   private String permission;
/*     */   private boolean texture;
/*     */   private boolean hideMenu;
/*     */   private boolean hideCosmetic;
/*     */   private boolean useEmote;
/*     */   private boolean colorBlocked;
/*     */   protected LivingEntity lendEntity;
/*     */   protected boolean removedLendEntity;
/*     */   protected Player player;
/*     */   protected NamespacedKey namespacedKey;
/*     */   
/*     */   public Cosmetic(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, String permission, boolean texture, boolean hideMenu, boolean useEmote, NamespacedKey namespacedKey) {
/*  57 */     this.id = id;
/*  58 */     this.name = name;
/*  59 */     this.itemStack = itemStack;
/*  60 */     this.modelData = modelData;
/*  61 */     this.colored = colored;
/*  62 */     this.cosmeticType = cosmeticType;
/*  63 */     this.color = color;
/*  64 */     this.permission = permission;
/*  65 */     this.texture = texture;
/*  66 */     this.hideMenu = hideMenu;
/*  67 */     this.hideCosmetic = false;
/*  68 */     this.useEmote = useEmote;
/*  69 */     this.namespacedKey = namespacedKey;
/*     */   }
/*     */   
/*     */   public Cosmetic(String id, String name, String permission) {
/*  73 */     this.id = id;
/*  74 */     this.name = name;
/*  75 */     this.permission = permission;
/*  76 */     this.itemStack = null;
/*  77 */     this.modelData = 0;
/*  78 */     this.colored = false;
/*  79 */     this.cosmeticType = null;
/*  80 */     this.color = null;
/*  81 */     this.texture = false;
/*  82 */     this.hideMenu = false;
/*  83 */     this.hideCosmetic = false;
/*  84 */     this.useEmote = false;
/*     */   }
/*     */   
/*     */   protected void updateCosmetic(Cosmetic cosmetic) {
/*  88 */     this.name = cosmetic.name;
/*  89 */     this.itemStack = cosmetic.itemStack;
/*  90 */     this.modelData = cosmetic.modelData;
/*  91 */     this.permission = cosmetic.permission;
/*  92 */     this.texture = cosmetic.texture;
/*  93 */     this.hideMenu = cosmetic.hideMenu;
/*  94 */     this.hideCosmetic = cosmetic.hideCosmetic;
/*  95 */     this.useEmote = cosmetic.useEmote;
/*  96 */     this.namespacedKey = cosmetic.namespacedKey;
/*     */   }
/*     */   
/*     */   public boolean updateProperties() {
/* 100 */     Cosmetic cosmetic = getCosmetic(this.id);
/* 101 */     if (cosmetic == null) return false; 
/* 102 */     updateCosmetic(cosmetic);
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public static List<Cosmetic> getCosmeticsUnHideByType(CosmeticType cosmeticType) {
/* 107 */     List<Cosmetic> cosmetics2 = new ArrayList<>();
/* 108 */     for (String id : cosmetics.keySet()) {
/* 109 */       if (id.isEmpty())
/* 110 */         continue;  Cosmetic cosmetic = getCloneCosmetic(id);
/* 111 */       if (cosmetic == null || 
/* 112 */         cosmetic.isHideMenu() || 
/* 113 */         cosmetic.getCosmeticType() != cosmeticType)
/* 114 */         continue;  cosmetics2.add(cosmetic);
/*     */     } 
/* 116 */     return cosmetics2;
/*     */   }
/*     */   
/*     */   public static Set<Cosmetic> getSetCosmeticsHideByType(CosmeticType cosmeticType) {
/* 120 */     Set<Cosmetic> cosmetics2 = new HashSet<>();
/* 121 */     for (String id : cosmetics.keySet()) {
/* 122 */       if (id.isEmpty())
/* 123 */         continue;  Cosmetic cosmetic = getCloneCosmetic(id);
/* 124 */       if (cosmetic == null || 
/* 125 */         cosmetic.isHideMenu() || 
/* 126 */         cosmetic.getCosmeticType() != cosmeticType)
/* 127 */         continue;  cosmetics2.add(cosmetic);
/*     */     } 
/* 129 */     return cosmetics2;
/*     */   }
/*     */   
/*     */   public static Set<Cosmetic> getCosmeticsByType(CosmeticType cosmeticType) {
/* 133 */     Set<Cosmetic> cosmetics2 = new HashSet<>();
/* 134 */     for (String id : cosmetics.keySet()) {
/* 135 */       if (id.isEmpty())
/* 136 */         continue;  Cosmetic cosmetic = getCloneCosmetic(id);
/* 137 */       if (cosmetic == null || 
/* 138 */         cosmetic.getCosmeticType() != cosmeticType)
/* 139 */         continue;  cosmetics2.add(cosmetic);
/*     */     } 
/* 141 */     return cosmetics2;
/*     */   }
/*     */   
/*     */   public static int getCosmeticCount(CosmeticType cosmeticType) {
/* 145 */     int i = 0;
/* 146 */     for (Cosmetic cosmetic : cosmetics.values()) {
/* 147 */       if (cosmetic.getCosmeticType() != cosmeticType)
/* 148 */         continue;  i++;
/*     */     } 
/* 150 */     return i;
/*     */   }
/*     */   
/*     */   public static Cosmetic getCosmetic(String id) {
/* 154 */     return cosmetics.get(id); } public static Cosmetic getCloneCosmetic(String id) { Hat hat1; Bag bag1; WStick wStick1; Balloon balloon1; Spray spray1; Hat hat; Bag bag;
/*     */     WStick wStick;
/*     */     Balloon balloon;
/*     */     Spray spray;
/* 158 */     Cosmetic cosmetic = getCosmetic(id);
/* 159 */     if (cosmetic == null) return null; 
/* 160 */     Cosmetic cosmec = null;
/* 161 */     switch (cosmetic.getCosmeticType()) {
/*     */       case HAT:
/* 163 */         hat = (Hat)cosmetic;
/* 164 */         hat1 = new Hat(hat.getId(), hat.getName(), hat.getItemStack().clone(), hat.getModelData(), hat.isColored(), hat.getCosmeticType(), hat.getColor(), hat.isOverlaps(), hat.getPermission(), hat.isTexture(), hat.isHideMenu(), hat.isUseEmote(), hat.getOffSetY(), hat.getNamespacedKey());
/*     */         break;
/*     */       case BAG:
/* 167 */         bag = (Bag)cosmetic;
/* 168 */         bag1 = new Bag(bag.getId(), bag.getName(), bag.getItemStack().clone(), bag.getModelData(), bag.getBagForMe(), bag.isColored(), bag.getSpace(), bag.getCosmeticType(), bag.getColor(), bag.getDistance(), bag.getPermission(), bag.isTexture(), bag.isHideMenu(), bag.getHeight(), bag.isUseEmote(), (bag.getBackPackEngine() != null) ? bag.getBackPackEngine().getClone() : null, bag.getNamespacedKey());
/*     */         break;
/*     */       case WALKING_STICK:
/* 171 */         wStick = (WStick)cosmetic;
/* 172 */         wStick1 = new WStick(wStick.getId(), wStick.getName(), wStick.getItemStack().clone(), wStick.getModelData(), wStick.isColored(), wStick.getCosmeticType(), wStick.getColor(), wStick.getPermission(), wStick.isTexture(), wStick.isOverlaps(), wStick.isHideMenu(), wStick.isUseEmote(), wStick.getNamespacedKey());
/*     */         break;
/*     */       case BALLOON:
/* 175 */         balloon = (Balloon)cosmetic;
/* 176 */         balloon1 = new Balloon(balloon.getId(), balloon.getName(), balloon.getItemStack().clone(), balloon.getModelData(), balloon.isColored(), balloon.getSpace(), balloon.getCosmeticType(), balloon.getColor(), balloon.isRotation(), balloon.getRotationType(), (balloon.getBalloonEngine() != null) ? balloon.getBalloonEngine().getClone() : null, (balloon.getBalloonIA() != null) ? balloon.getBalloonIA().getClone() : null, balloon.getDistance(), balloon.getPermission(), balloon.isTexture(), balloon.isBigHead(), balloon.isHideMenu(), balloon.isInvisibleLeash(), balloon.isUseEmote(), balloon.isInstantFollow(), balloon.getNamespacedKey());
/*     */         break;
/*     */       case SPRAY:
/* 179 */         spray = (Spray)cosmetic;
/* 180 */         spray1 = new Spray(spray.getId(), spray.getName(), spray.getItemStack().clone(), spray.getModelData(), spray.isColored(), spray.getCosmeticType(), spray.getColor(), spray.getPermission(), spray.isTexture(), spray.getImage(), spray.isItemImage(), spray.isHideMenu(), spray.isUseEmote(), spray.getNamespacedKey());
/*     */         break;
/*     */     } 
/* 183 */     return (Cosmetic)spray1; }
/*     */ 
/*     */   
/*     */   public static void loadCosmetics() {
/* 187 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 188 */     cosmetics.clear();
/* 189 */     FileCosmetics cosmeticsFiles = plugin.getCosmetics();
/* 190 */     int cosmetics_count = 0;
/* 191 */     for (FileCreator cosmeticsConf : cosmeticsFiles.getFiles().values()) {
/* 192 */       plugin.getLogger().info("Loading cosmetics from file: " + cosmeticsConf.getFileName());
/* 193 */       if (!cosmeticsConf.contains("cosmetics"))
/* 194 */         continue;  for (String key : cosmeticsConf.getConfigurationSection("cosmetics").getKeys(false)) {
/* 195 */         Hat hat; Bag bag; WStick wStick; Balloon balloon; Spray spray; String name = "";
/* 196 */         ItemStack itemStack = null;
/* 197 */         CosmeticType cosmeticType = null;
/* 198 */         boolean colored = false;
/* 199 */         Color color = null;
/* 200 */         String type = "";
/* 201 */         double space = 0.0D;
/* 202 */         boolean overlaps = false;
/* 203 */         BalloonEngine balloonEngine = null;
/* 204 */         BalloonIA balloonIA = null;
/* 205 */         BackPackEngine backPackEngine = null;
/* 206 */         boolean rotation = false;
/* 207 */         RotationType rotationType = null;
/* 208 */         int modelData = 0;
/* 209 */         ItemStack bagForMe = null;
/* 210 */         float height = 0.0F;
/* 211 */         double distance = 800.0D;
/* 212 */         String permission = "";
/* 213 */         BufferedImage image = null;
/* 214 */         boolean itemImage = false;
/* 215 */         boolean isTexture = false;
/* 216 */         boolean bigHead = false;
/* 217 */         boolean hideMenu = false;
/* 218 */         boolean useEmote = false;
/* 219 */         double offsetY = 0.0D;
/* 220 */         boolean invisibleLeash = false;
/* 221 */         boolean instantFollow = false;
/* 222 */         if (cosmeticsConf.contains("cosmetics." + key + ".permission")) {
/* 223 */           permission = cosmeticsConf.getString("cosmetics." + key + ".permission");
/*     */         }
/* 225 */         if (cosmeticsConf.contains("cosmetics." + key + ".url")) {
/* 226 */           String url = cosmeticsConf.getString("cosmetics." + key + ".url");
/* 227 */           image = Utils.getImage(url);
/* 228 */           if (image == null) {
/* 229 */             plugin.getLogger().warning("Could not load Spray image from url: " + url);
/*     */             continue;
/*     */           } 
/*     */         } 
/* 233 */         if (cosmeticsConf.contains("cosmetics." + key + ".item")) {
/* 234 */           List<String> lore = null;
/* 235 */           boolean unbreakable = false;
/* 236 */           boolean glow = false;
/* 237 */           boolean hide_attributes = false;
/* 238 */           String texture = "";
/* 239 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.texture")) {
/* 240 */             texture = cosmeticsConf.getString("cosmetics." + key + ".item.texture");
/* 241 */             isTexture = true;
/*     */           } 
/* 243 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.display")) {
/* 244 */             name = cosmeticsConf.getString("cosmetics." + key + ".item.display");
/* 245 */             if (plugin.isItemsAdder()) {
/* 246 */               name = plugin.getItemsAdder().replaceFontImages(name);
/*     */             }
/*     */           } 
/* 249 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.material")) {
/* 250 */             String item = cosmeticsConf.getString("cosmetics." + key + ".item.material");
/*     */             try {
/* 252 */               itemStack = XMaterial.valueOf(item.toUpperCase()).parseItem();
/* 253 */             } catch (IllegalArgumentException exception) {
/* 254 */               plugin.getLogger().warning("Item Material '" + item + "' in Cosmetic '" + key + "' Not Found!");
/*     */             } 
/*     */           } 
/* 257 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.lore")) {
/* 258 */             lore = cosmeticsConf.getStringList("cosmetics." + key + ".item.lore");
/* 259 */             if (plugin.isItemsAdder()) {
/* 260 */               List<String> lore2 = new ArrayList<>();
/* 261 */               for (String l : lore) {
/* 262 */                 lore2.add(plugin.getItemsAdder().replaceFontImages(l));
/*     */               }
/* 264 */               lore.clear();
/* 265 */               lore.addAll(lore2);
/*     */             } 
/*     */           } 
/* 268 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.glow")) {
/* 269 */             glow = cosmeticsConf.getBoolean("cosmetics." + key + ".item.glow");
/*     */           }
/* 271 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.hide-attributes")) {
/* 272 */             hide_attributes = cosmeticsConf.getBoolean("cosmetics." + key + ".item.hide-attributes");
/*     */           }
/* 274 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.unbreakable")) {
/* 275 */             unbreakable = cosmeticsConf.getBoolean("cosmetics." + key + ".item.unbreakable");
/*     */           }
/* 277 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.modeldata")) {
/* 278 */             modelData = cosmeticsConf.getInt("cosmetics." + key + ".item.modeldata");
/*     */           }
/* 280 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.color")) {
/* 281 */             String hex = cosmeticsConf.getStringWF("cosmetics." + key + ".item.color");
/* 282 */             if (hex != null) {
/* 283 */               color = Utils.hex2Rgb(hex);
/*     */             }
/*     */           } 
/* 286 */           if (cosmeticsConf.contains("cosmetics." + key + ".height")) {
/* 287 */             height = (float)cosmeticsConf.getDouble("cosmetics." + key + ".height");
/*     */           }
/* 289 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.item-adder")) {
/* 290 */             if (!plugin.isItemsAdder()) {
/* 291 */               plugin.getLogger().warning("Item Adder plugin Not Found skipping cosmetic '" + key + "'");
/*     */               continue;
/*     */             } 
/* 294 */             String id = cosmeticsConf.getString("cosmetics." + key + ".item.item-adder");
/* 295 */             ItemStack ia = plugin.getItemsAdder().getCustomItemStack(id);
/* 296 */             if (ia == null) {
/* 297 */               plugin.getLogger().warning("IA Item: '" + id + "' Not Found skipping...");
/*     */               continue;
/*     */             } 
/* 300 */             itemStack = ia.clone();
/* 301 */             modelData = -1;
/*     */           } 
/* 303 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.oraxen")) {
/* 304 */             if (!plugin.isOraxen()) {
/* 305 */               plugin.getLogger().warning("Oraxen plugin Not Found skipping cosmetic '" + key + "'");
/*     */               continue;
/*     */             } 
/* 308 */             String id = cosmeticsConf.getString("cosmetics." + key + ".item.oraxen");
/* 309 */             ItemStack oraxen = plugin.getOraxen().getItemStackById(id);
/* 310 */             if (oraxen == null) {
/* 311 */               plugin.getLogger().warning("Oraxen item:  '" + id + "' Not Found skipping...");
/*     */               continue;
/*     */             } 
/* 314 */             itemStack = oraxen.clone();
/* 315 */             modelData = -1;
/*     */           } 
/* 317 */           if (itemStack == null) {
/*     */             continue;
/*     */           }
/* 320 */           ItemMeta itemMeta1 = itemStack.getItemMeta();
/* 321 */           itemMeta1.setDisplayName(name);
/* 322 */           itemMeta1.setLore(lore);
/* 323 */           if (Utils.isNewerThan1206()) {
/* 324 */             itemMeta1.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("foo", 0.0D, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
/*     */           }
/*     */           
/* 327 */           if (glow) {
/* 328 */             itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
/* 329 */             itemMeta1.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
/*     */           } 
/* 331 */           if (hide_attributes) {
/* 332 */             itemMeta1.addItemFlags(ItemFlag.values());
/*     */           }
/* 334 */           itemMeta1.setUnbreakable(unbreakable);
/* 335 */           if (modelData != -1) {
/* 336 */             itemMeta1.setCustomModelData(Integer.valueOf(modelData));
/*     */           }
/* 338 */           itemStack.setItemMeta(itemMeta1);
/* 339 */           if (itemStack.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
/* 340 */             itemStack = plugin.getVersion().getCustomHead(itemStack, texture);
/*     */           }
/* 342 */           if (cosmeticsConf.contains("cosmetics." + key + ".item.for-me")) {
/* 343 */             int datamodel = cosmeticsConf.getInt("cosmetics." + key + ".item.for-me");
/* 344 */             if (datamodel != 0) {
/* 345 */               bagForMe = itemStack.clone();
/* 346 */               ItemMeta itemMeta2 = bagForMe.getItemMeta();
/* 347 */               itemMeta2.setCustomModelData(Integer.valueOf(datamodel));
/* 348 */               bagForMe.setItemMeta(itemMeta2);
/*     */             } else {
/* 350 */               String id = cosmeticsConf.getString("cosmetics." + key + ".item.for-me");
/* 351 */               if (id.startsWith("item-adder")) {
/* 352 */                 if (!plugin.isItemsAdder()) {
/* 353 */                   plugin.getLogger().warning("ItemsAdder plugin not found, skipping cosmetic");
/*     */                   continue;
/*     */                 } 
/* 356 */                 String ia = id.split(";")[1];
/* 357 */                 ItemStack item_ia = plugin.getItemsAdder().getCustomItemStack(ia);
/* 358 */                 if (item_ia == null) {
/* 359 */                   plugin.getLogger().warning("IA Item: '" + ia + "' Not Found skipping...");
/*     */                   continue;
/*     */                 } 
/* 362 */                 bagForMe = itemStack.clone();
/* 363 */                 bagForMe.setType(item_ia.getType());
/* 364 */                 ItemMeta itemMeta2 = bagForMe.getItemMeta();
/* 365 */                 itemMeta2.setCustomModelData(Integer.valueOf(item_ia.getItemMeta().getCustomModelData()));
/* 366 */                 bagForMe.setItemMeta(itemMeta2);
/* 367 */               } else if (id.startsWith("oraxen")) {
/* 368 */                 if (!plugin.isOraxen()) {
/* 369 */                   plugin.getLogger().warning("Oraxen plugin not found, skipping cosmetic");
/*     */                   continue;
/*     */                 } 
/* 372 */                 String oraxen = id.split(";")[1];
/* 373 */                 ItemStack item_orax = plugin.getOraxen().getItemStackById(oraxen);
/* 374 */                 if (item_orax == null) {
/* 375 */                   plugin.getLogger().warning("Oraxen Item: '" + oraxen + "' Not Found skipping...");
/*     */                   continue;
/*     */                 } 
/* 378 */                 bagForMe = itemStack.clone();
/* 379 */                 bagForMe.setType(item_orax.getType());
/* 380 */                 ItemMeta itemMeta2 = bagForMe.getItemMeta();
/* 381 */                 itemMeta2.setCustomModelData(Integer.valueOf(item_orax.getItemMeta().getCustomModelData()));
/* 382 */                 bagForMe.setItemMeta(itemMeta2);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 388 */         if (cosmeticsConf.contains("cosmetics." + key + ".item-image")) {
/* 389 */           itemImage = cosmeticsConf.getBoolean("cosmetics." + key + ".item-image");
/*     */         }
/* 391 */         if (cosmeticsConf.contains("cosmetics." + key + ".type")) {
/* 392 */           type = cosmeticsConf.getString("cosmetics." + key + ".type");
/*     */           try {
/* 394 */             cosmeticType = CosmeticType.valueOf(type.toUpperCase());
/* 395 */           } catch (IllegalArgumentException exception) {
/* 396 */             plugin.getLogger().warning("Cosmetic Type: " + type + " Not Found.");
/*     */             return;
/*     */           } 
/*     */         } 
/* 400 */         if (cosmeticsConf.contains("cosmetics." + key + ".colored")) {
/* 401 */           colored = cosmeticsConf.getBoolean("cosmetics." + key + ".colored");
/*     */         }
/* 403 */         if (cosmeticsConf.contains("cosmetics." + key + ".hide-menu")) {
/* 404 */           hideMenu = cosmeticsConf.getBoolean("cosmetics." + key + ".hide-menu");
/*     */         }
/* 406 */         if (cosmeticsConf.contains("cosmetics." + key + ".use-emote")) {
/* 407 */           useEmote = cosmeticsConf.getBoolean("cosmetics." + key + ".use-emote");
/*     */         }
/* 409 */         if (cosmeticsConf.contains("cosmetics." + key + ".offset-y")) {
/* 410 */           offsetY = cosmeticsConf.getDouble("cosmetics." + key + ".offset-y");
/*     */         }
/* 412 */         if (cosmeticsConf.contains("cosmetics." + key + ".big-head")) {
/* 413 */           bigHead = cosmeticsConf.getBoolean("cosmetics." + key + ".big-head");
/*     */         }
/* 415 */         if (cosmeticsConf.contains("cosmetics." + key + ".rotation")) {
/* 416 */           rotation = cosmeticsConf.getBoolean("cosmetics." + key + ".rotation.enabled");
/* 417 */           String rotType = cosmeticsConf.getString("cosmetics." + key + ".rotation.type");
/*     */           try {
/* 419 */             rotationType = RotationType.valueOf(rotType.toUpperCase());
/* 420 */           } catch (IllegalArgumentException exception) {
/* 421 */             plugin.getLogger().warning("Cosmetic Type: " + type + " Rotation Type Not Found.");
/*     */           } 
/*     */         } 
/* 424 */         if (cosmeticsConf.contains("cosmetics." + key + ".space")) {
/* 425 */           space = cosmeticsConf.getDouble("cosmetics." + key + ".space");
/*     */         }
/* 427 */         if (cosmeticsConf.contains("cosmetics." + key + ".overlaps")) {
/* 428 */           overlaps = cosmeticsConf.getBoolean("cosmetics." + key + ".overlaps");
/*     */         }
/* 430 */         if (cosmeticsConf.contains("cosmetics." + key + ".distance")) {
/* 431 */           distance = cosmeticsConf.getDouble("cosmetics." + key + ".distance");
/*     */         }
/* 433 */         if (cosmeticsConf.contains("cosmetics." + key + ".invisible-leash")) {
/* 434 */           invisibleLeash = cosmeticsConf.getBoolean("cosmetics." + key + ".invisible-leash");
/*     */         }
/* 436 */         if (cosmeticsConf.contains("cosmetics." + key + ".instant-follow")) {
/* 437 */           instantFollow = cosmeticsConf.getBoolean("cosmetics." + key + ".instant-follow");
/*     */         }
/* 439 */         if (cosmeticsConf.contains("cosmetics." + key + ".meg.model")) {
/* 440 */           if (!plugin.isModelEngine()) {
/* 441 */             plugin.getLogger().warning("Model Engine plugin Not Found skipping cosmetic '" + key + "'");
/*     */             continue;
/*     */           } 
/* 444 */           String modelId = cosmeticsConf.getString("cosmetics." + key + ".meg.model");
/* 445 */           List<String> colorableParts = cosmeticsConf.getStringListWF("cosmetics." + key + ".meg.colorable-parts");
/* 446 */           String walk_animation = cosmeticsConf.getString("cosmetics." + key + ".meg.animations.walk");
/* 447 */           String idle_animation = cosmeticsConf.getString("cosmetics." + key + ".meg.animations.idle");
/* 448 */           OffsetModel offsetModel = cosmeticsConf.getOffseTModel("cosmetics." + key + ".meg.offset");
/* 449 */           if (cosmeticType == CosmeticType.BAG) {
/* 450 */             PositionModelType positionModelType = cosmeticsConf.getPositionModelType("cosmetics." + key + ".meg.position");
/* 451 */             backPackEngine = new BackPackEngine(modelId, colorableParts, idle_animation, distance, offsetModel, positionModelType);
/*     */           } else {
/* 453 */             balloonEngine = new BalloonEngine(modelId, colorableParts, walk_animation, idle_animation, distance, offsetModel);
/*     */           } 
/*     */         } 
/* 456 */         if (cosmeticsConf.contains("cosmetics." + key + ".ia.model")) {
/* 457 */           if (!plugin.isItemsAdder()) {
/* 458 */             plugin.getLogger().warning("ItemsAdder plugin Not Found skipping cosmetic '" + key + "'");
/*     */             continue;
/*     */           } 
/* 461 */           String modelId = cosmeticsConf.getString("cosmetics." + key + ".ia.model");
/* 462 */           if (!plugin.getItemsAdder().existModel(modelId)) {
/* 463 */             plugin.getLogger().warning("ItemsAdder model Not Found skipping cosmetic '" + key + "'");
/*     */             continue;
/*     */           } 
/* 466 */           List<String> colorableParts = cosmeticsConf.getStringListWF("cosmetics." + key + ".ia.colorable-parts");
/* 467 */           String walk_animation = cosmeticsConf.getString("cosmetics." + key + ".ia.animations.walk");
/* 468 */           String idle_animation = cosmeticsConf.getString("cosmetics." + key + ".ia.animations.idle");
/* 469 */           balloonIA = new BalloonIA(modelId, colorableParts, walk_animation, idle_animation, distance);
/*     */         } 
/* 471 */         if (cosmeticType == null) {
/*     */           return;
/*     */         }
/* 474 */         NamespacedKey namespacedKey = new NamespacedKey((Plugin)plugin, "cosmetic");
/*     */         
/* 476 */         ItemMeta itemMeta = itemStack.getItemMeta();
/* 477 */         if (itemMeta != null) {
/* 478 */           itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
/* 479 */           itemStack.setItemMeta(itemMeta);
/*     */         } 
/* 481 */         switch (cosmeticType) {
/*     */           case HAT:
/* 483 */             hat = new Hat(key, name, itemStack, modelData, colored, cosmeticType, color, overlaps, permission, isTexture, hideMenu, useEmote, offsetY, namespacedKey);
/* 484 */             if (color != null) {
/* 485 */               hat.setDefaultColor(true);
/*     */             }
/* 487 */             cosmetics.put(key, hat);
/*     */             break;
/*     */           case BAG:
/* 490 */             bag = new Bag(key, name, itemStack, modelData, bagForMe, colored, space, cosmeticType, color, distance, permission, isTexture, hideMenu, height, useEmote, backPackEngine, namespacedKey);
/* 491 */             if (color != null) {
/* 492 */               bag.setDefaultColor(true);
/*     */             }
/* 494 */             cosmetics.put(key, bag);
/*     */             break;
/*     */           case WALKING_STICK:
/* 497 */             wStick = new WStick(key, name, itemStack, modelData, colored, cosmeticType, color, permission, isTexture, overlaps, hideMenu, useEmote, namespacedKey);
/* 498 */             if (color != null) {
/* 499 */               wStick.setDefaultColor(true);
/*     */             }
/* 501 */             cosmetics.put(key, wStick);
/*     */             break;
/*     */           case BALLOON:
/* 504 */             balloon = new Balloon(key, name, itemStack, modelData, colored, space, cosmeticType, color, rotation, rotationType, balloonEngine, balloonIA, distance, permission, isTexture, bigHead, hideMenu, invisibleLeash, useEmote, instantFollow, namespacedKey);
/* 505 */             if (color != null) {
/* 506 */               balloon.setDefaultColor(true);
/*     */             }
/* 508 */             cosmetics.put(key, balloon);
/*     */             break;
/*     */           case SPRAY:
/* 511 */             spray = new Spray(key, name, itemStack, modelData, colored, cosmeticType, color, permission, isTexture, image, itemImage, hideMenu, useEmote, namespacedKey);
/* 512 */             if (color != null) {
/* 513 */               spray.setDefaultColor(true);
/*     */             }
/* 515 */             cosmetics.put(key, spray);
/*     */             break;
/*     */         } 
/* 518 */         cosmetics_count++;
/*     */       } 
/* 520 */       if (plugin.getConfig().contains("order-cosmetics")) {
/* 521 */         List<Cosmetic> list; int order = plugin.getConfig().getInt("order-cosmetics");
/* 522 */         switch (order) {
/*     */ 
/*     */           
/*     */           case 1:
/* 526 */             cosmetics = new TreeMap<>(cosmetics);
/*     */             break;
/*     */           case 2:
/* 529 */             list = new ArrayList<>(cosmetics.values());
/* 530 */             Collections.shuffle(list);
/* 531 */             cosmetics = new LinkedHashMap<>();
/* 532 */             list.forEach(cosmetic -> cosmetics.put(cosmetic.getId(), cosmetic));
/*     */             break;
/*     */         } 
/*     */       } 
/* 536 */       plugin.getLogger().info("Registered cosmetics: " + cosmetics_count);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getId() {
/* 541 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 545 */     return this.name;
/*     */   }
/*     */   
/*     */   public ItemStack getItemStack() {
/* 549 */     if (isDefaultColor()) {
/* 550 */       return getItemColor();
/*     */     }
/* 552 */     return this.itemStack;
/*     */   }
/*     */   
/*     */   public int getModelData() {
/* 556 */     return this.modelData;
/*     */   }
/*     */   
/*     */   public CosmeticType getCosmeticType() {
/* 560 */     return this.cosmeticType;
/*     */   }
/*     */   
/*     */   public boolean isColored() {
/* 564 */     return this.colored;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 583 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 587 */     this.color = color;
/*     */   }
/*     */   
/*     */   public boolean isCosmetic(ItemStack itemStack) {
/* 591 */     if (itemStack == null) return false; 
/* 592 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 593 */     if (itemMeta == null) return false; 
/* 594 */     return itemMeta.getPersistentDataContainer().has(this.namespacedKey, PersistentDataType.STRING);
/*     */   }
/*     */   
/*     */   public ItemStack getItemPlaceholders(Player player) {
/* 598 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 599 */     if (!plugin.isPlaceholders()) return getItemColor(player); 
/* 600 */     ItemStack itemStack = getItemColor(player);
/* 601 */     ItemStack itemClone = itemStack.clone();
/* 602 */     if (!plugin.isPlaceholderAPI()) return itemStack; 
/* 603 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 604 */     if (itemMeta == null) return getItemColor(player); 
/* 605 */     if (itemMeta.hasDisplayName()) {
/* 606 */       itemMeta.setDisplayName(plugin.getPlaceholderAPI().setPlaceholders(player, itemClone.getItemMeta().getDisplayName()));
/*     */     }
/* 608 */     if (itemMeta.hasLore()) {
/* 609 */       itemMeta.setLore(plugin.getPlaceholderAPI().setPlaceholders(player, itemClone.getItemMeta().getLore()));
/*     */     }
/* 611 */     itemStack.setItemMeta(itemMeta);
/* 612 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack getItemColor() {
/* 616 */     if (this.itemStack == null) return null; 
/* 617 */     ItemStack itemStack = this.itemStack.clone();
/* 618 */     if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 619 */       LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemStack.getItemMeta();
/* 620 */       if (this.color != null) {
/* 621 */         itemMeta.setColor(this.color);
/*     */       }
/* 623 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 624 */       return itemStack;
/*     */     } 
/* 626 */     if (itemStack.getItemMeta() instanceof PotionMeta) {
/* 627 */       PotionMeta itemMeta = (PotionMeta)itemStack.getItemMeta();
/* 628 */       if (this.color != null) {
/* 629 */         itemMeta.setColor(this.color);
/*     */       }
/* 631 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 632 */       return itemStack;
/*     */     } 
/* 634 */     if (itemStack.getItemMeta() instanceof MapMeta) {
/* 635 */       MapMeta itemMeta = (MapMeta)itemStack.getItemMeta();
/* 636 */       if (this.color != null) {
/* 637 */         itemMeta.setColor(this.color);
/*     */       }
/* 639 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 640 */       return itemStack;
/*     */     } 
/* 642 */     if (itemStack.getItemMeta() instanceof FireworkEffectMeta) {
/* 643 */       FireworkEffectMeta itemMeta = (FireworkEffectMeta)itemStack.getItemMeta();
/* 644 */       if (this.color != null)
/* 645 */         itemMeta.setEffect(FireworkEffect.builder().withColor(this.color).build()); 
/* 646 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/*     */     } 
/* 648 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack getItemColor(Player player) {
/* 652 */     if (isTexture()) return getItemColor(); 
/* 653 */     ItemStack itemStack = getItemColor();
/* 654 */     if (itemStack.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) return itemStack; 
/* 655 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 656 */     skullMeta.setOwningPlayer((OfflinePlayer)player);
/* 657 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 658 */     return itemStack;
/*     */   }
/*     */   
/*     */   public String getPermission() {
/* 662 */     return this.permission;
/*     */   }
/*     */   
/*     */   public boolean hasPermission(Player player) {
/* 666 */     if (this.permission == null || this.permission.isEmpty()) return false; 
/* 667 */     return player.hasPermission(this.permission);
/*     */   }
/*     */   
/*     */   public boolean isTexture() {
/* 671 */     return this.texture;
/*     */   }
/*     */   
/*     */   public void setDefaultColor(boolean defaultColor) {
/* 675 */     this.defaultColor = defaultColor;
/*     */   }
/*     */   
/*     */   public boolean isDefaultColor() {
/* 679 */     return this.defaultColor;
/*     */   }
/*     */   
/*     */   public boolean isHideMenu() {
/* 683 */     return this.hideMenu;
/*     */   }
/*     */   
/*     */   public boolean isHideCosmetic() {
/* 687 */     return this.hideCosmetic;
/*     */   }
/*     */   
/*     */   public void setHideCosmetic(boolean hideCosmetic) {
/* 691 */     this.hideCosmetic = hideCosmetic;
/*     */   }
/*     */   
/*     */   public boolean isUseEmote() {
/* 695 */     return this.useEmote;
/*     */   }
/*     */   
/*     */   public boolean isColorBlocked() {
/* 699 */     return this.colorBlocked;
/*     */   }
/*     */   
/*     */   public void setColorBlocked(boolean colorBlocked) {
/* 703 */     this.colorBlocked = colorBlocked;
/*     */   }
/*     */   
/*     */   public void setPlayer(Player player) {
/* 707 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void setLendEntity(LivingEntity lendEntity) {
/* 711 */     this.lendEntity = lendEntity;
/* 712 */     setRemovedLendEntity((lendEntity == null));
/*     */   }
/*     */   
/*     */   public Player getPlayer() {
/* 716 */     return this.player;
/*     */   }
/*     */   
/*     */   public LivingEntity getLendEntity() {
/* 720 */     return this.lendEntity;
/*     */   }
/*     */   
/*     */   public boolean isRemovedLendEntity() {
/* 724 */     return this.removedLendEntity;
/*     */   }
/*     */   
/*     */   public void setRemovedLendEntity(boolean removedLendEntity) {
/* 728 */     this.removedLendEntity = removedLendEntity;
/*     */   }
/*     */   
/*     */   public NamespacedKey getNamespacedKey() {
/* 732 */     return this.namespacedKey;
/*     */   }
/*     */   
/*     */   public abstract void spawn(Player paramPlayer);
/*     */   
/*     */   public abstract void despawn(Player paramPlayer);
/*     */   
/*     */   public abstract void update();
/*     */   
/*     */   public abstract void remove();
/*     */   
/*     */   public abstract void clearClose();
/*     */   
/*     */   public abstract void lendToEntity();
/*     */   
/*     */   public abstract void hide(Player paramPlayer);
/*     */   
/*     */   public abstract void show(Player paramPlayer);
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\api\Cosmetic.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */