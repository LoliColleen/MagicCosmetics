/*     */ package com.francobm.magicosmetics.cache.items;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.Color;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.SecondaryColor;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.enchantments.Enchantment;
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
/*     */ public class Items {
/*  25 */   public static Map<String, Items> items = new HashMap<>();
/*     */   private final String id;
/*     */   private final List<String> loreAvailable;
/*     */   private final List<String> loreUnselect;
/*     */   private final List<String> loreUnavailable;
/*     */   private final ItemStack itemStack;
/*     */   private Color dyeColor;
/*     */   
/*     */   public Items(String id, ItemStack itemStack, List<String> loreAvailable, List<String> loreUnselect, List<String> loreUnavailable) {
/*  34 */     this.id = id;
/*  35 */     this.itemStack = itemStack;
/*  36 */     this.loreAvailable = loreAvailable;
/*  37 */     this.loreUnselect = loreUnselect;
/*  38 */     this.loreUnavailable = loreUnavailable;
/*     */   }
/*     */   
/*     */   public Items(ItemStack itemStack) {
/*  42 */     this.id = (new Random()).toString();
/*  43 */     this.itemStack = itemStack;
/*  44 */     this.loreUnselect = new ArrayList<>();
/*  45 */     this.loreAvailable = new ArrayList<>();
/*  46 */     this.loreUnavailable = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public Items(String id, ItemStack itemStack) {
/*  50 */     this.id = id;
/*  51 */     this.itemStack = itemStack;
/*  52 */     this.loreUnselect = new ArrayList<>();
/*  53 */     this.loreAvailable = new ArrayList<>();
/*  54 */     this.loreUnavailable = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public static Items getItem(String id) {
/*  58 */     return items.get(id);
/*     */   }
/*     */   
/*     */   public static void loadItems() {
/*  62 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  63 */     items.clear();
/*  64 */     FileCreator menu = MagicCosmetics.getInstance().getMenus();
/*  65 */     int items_count = 0;
/*  66 */     for (String key : menu.getConfigurationSection("items").getKeys(false)) {
/*  67 */       String display = "";
/*  68 */       String material = "";
/*  69 */       ItemStack itemStack = null;
/*  70 */       List<String> loreAvailable = null;
/*  71 */       List<String> loreUnselect = null;
/*  72 */       List<String> loreUnavailable = null;
/*  73 */       int amount = 0;
/*  74 */       boolean glow = false;
/*  75 */       int modelData = 0;
/*  76 */       if (menu.contains("items." + key + ".item.display")) {
/*  77 */         display = menu.getString("items." + key + ".item.display");
/*     */       }
/*  79 */       if (menu.contains("items." + key + ".item.material")) {
/*  80 */         material = menu.getString("items." + key + ".item.material");
/*     */         try {
/*  82 */           itemStack = XMaterial.valueOf(material.toUpperCase()).parseItem();
/*  83 */         } catch (IllegalArgumentException exception) {
/*  84 */           plugin.getLogger().warning("Item '" + key + "' material: " + material + " Not Found.");
/*     */         } 
/*     */       } 
/*  87 */       List<String> lore = null;
/*  88 */       if (menu.contains("items." + key + ".item.lore")) {
/*  89 */         lore = new ArrayList<>();
/*  90 */         for (String l : menu.getStringList("items." + key + ".item.lore")) {
/*  91 */           lore.add(l
/*  92 */               .replace("%hats_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.HAT)))
/*  93 */               .replace("%bags_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.BAG)))
/*  94 */               .replace("%wsticks_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.WALKING_STICK)))
/*  95 */               .replace("%balloons_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.BALLOON)))
/*  96 */               .replace("%sprays_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.SPRAY))));
/*     */         }
/*     */       } 
/*  99 */       if (menu.contains("items." + key + ".item.lore-available")) {
/* 100 */         loreAvailable = menu.getStringList("items." + key + ".item.lore-available");
/*     */       }
/* 102 */       if (menu.contains("items." + key + ".item.lore-unavailable")) {
/* 103 */         loreUnavailable = menu.getStringList("items." + key + ".item.lore-unavailable");
/*     */       }
/* 105 */       if (menu.contains("items." + key + ".item.lore-selected")) {
/* 106 */         loreAvailable = menu.getStringList("items." + key + ".item.lore-selected");
/*     */       }
/* 108 */       if (menu.contains("items." + key + ".item.lore-notselected")) {
/* 109 */         loreUnselect = menu.getStringList("items." + key + ".item.lore-notselected");
/*     */       }
/* 111 */       if (menu.contains("items." + key + ".item.amount")) {
/* 112 */         amount = menu.getInt("items." + key + ".item.amount");
/*     */       }
/* 114 */       if (menu.contains("items." + key + ".item.glow")) {
/* 115 */         glow = menu.getBoolean("items." + key + ".item.glow");
/*     */       }
/* 117 */       if (menu.contains("items." + key + ".item.modeldata")) {
/* 118 */         modelData = menu.getInt("items." + key + ".item.modeldata");
/*     */       }
/* 120 */       String texture = "";
/* 121 */       if (menu.contains("items." + key + ".item.texture")) {
/* 122 */         texture = menu.getString("items." + key + ".item.texture");
/*     */       }
/* 124 */       if (menu.contains("items." + key + ".item.item-adder")) {
/* 125 */         if (!plugin.isItemsAdder()) {
/* 126 */           plugin.getLogger().warning("Item Adder plugin Not Found skipping Menu Item '" + key + "'");
/*     */           continue;
/*     */         } 
/* 129 */         String id = menu.getString("items." + key + ".item.item-adder");
/* 130 */         if (plugin.getItemsAdder().getCustomStack(id) == null) {
/* 131 */           plugin.getLogger().warning("IA Item: '" + id + "' Not Found skipping...");
/*     */           continue;
/*     */         } 
/* 134 */         itemStack = plugin.getItemsAdder().getCustomItemStack(id).clone();
/* 135 */         modelData = -1;
/*     */       } 
/* 137 */       if (menu.contains("items." + key + ".item.oraxen")) {
/* 138 */         if (!plugin.isOraxen()) {
/* 139 */           plugin.getLogger().warning("Oraxen plugin Not Found skipping Menu Item '" + key + "'");
/*     */           continue;
/*     */         } 
/* 142 */         String id = menu.getString("items." + key + ".item.oraxen");
/* 143 */         ItemStack oraxen = plugin.getOraxen().getItemStackById(id);
/* 144 */         if (oraxen == null) {
/* 145 */           plugin.getLogger().warning("Oraxen item:  '" + id + "' Not Found skipping...");
/*     */           continue;
/*     */         } 
/* 148 */         itemStack = oraxen.clone();
/* 149 */         modelData = -1;
/*     */       } 
/* 151 */       itemStack.setAmount(amount);
/* 152 */       ItemMeta itemMeta = itemStack.getItemMeta();
/* 153 */       itemMeta.setDisplayName(display);
/* 154 */       itemMeta.setLore(lore);
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (glow) {
/* 159 */         itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
/* 160 */         itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
/*     */       } 
/* 162 */       if (modelData != -1) {
/* 163 */         itemMeta.setCustomModelData(Integer.valueOf(modelData));
/*     */       }
/* 165 */       itemStack.setItemMeta(itemMeta);
/* 166 */       if (itemStack.getType() == Material.PLAYER_HEAD) {
/* 167 */         itemStack = plugin.getVersion().getCustomHead(itemStack, texture);
/*     */       }
/* 169 */       items.put(key, new Items(key, itemStack, loreAvailable, loreUnselect, loreUnavailable));
/* 170 */       items_count++;
/*     */     } 
/* 172 */     plugin.getLogger().info("Registered items: " + items_count);
/*     */   }
/*     */   
/*     */   public ItemStack addLore() {
/* 176 */     if (this.itemStack == null) return null; 
/* 177 */     ItemStack itemStack = this.itemStack.clone();
/* 178 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 179 */     if (itemMeta == null) return null; 
/* 180 */     if (itemMeta.getLore() != null) {
/* 181 */       List<String> lore = itemMeta.getLore();
/* 182 */       if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 183 */         lore.addAll(getLoreAvailable());
/*     */       }
/* 185 */       itemMeta.setLore(lore);
/*     */     } else {
/* 187 */       itemMeta.setLore(getLoreAvailable());
/*     */     } 
/* 189 */     return itemStack;
/*     */   }
/*     */   
/*     */   public Items addVariable(String variable, String string) {
/* 193 */     if (this.itemStack == null) return this; 
/* 194 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/* 195 */     if (itemMeta == null) return this; 
/* 196 */     if (itemMeta.hasDisplayName()) {
/* 197 */       itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, string));
/*     */     }
/* 199 */     List<String> lore = new ArrayList<>();
/* 200 */     if (itemMeta.getLore() != null) {
/* 201 */       for (String l : itemMeta.getLore()) {
/* 202 */         lore.add(l.replace(variable, string));
/*     */       }
/*     */     }
/* 205 */     itemMeta.setLore(lore);
/* 206 */     this.itemStack.setItemMeta(itemMeta);
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public Items addVariable(String variable, CosmeticType cosmeticType) {
/* 211 */     if (this.itemStack == null) return this; 
/* 212 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/* 213 */     List<String> lore = new ArrayList<>();
/* 214 */     FileCreator messages = MagicCosmetics.getInstance().getMessages();
/* 215 */     if (itemMeta == null) return this; 
/* 216 */     switch (cosmeticType) {
/*     */       case HAT:
/* 218 */         if (itemMeta.hasDisplayName()) {
/* 219 */           itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.hat")));
/*     */         }
/* 221 */         if (itemMeta.getLore() != null) {
/* 222 */           for (String l : itemMeta.getLore()) {
/* 223 */             lore.add(l.replace(variable, messages.getString("types.hat")));
/*     */           }
/*     */         }
/*     */         break;
/*     */       case BAG:
/* 228 */         if (itemMeta.hasDisplayName()) {
/* 229 */           itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.bag")));
/*     */         }
/* 231 */         if (itemMeta.getLore() != null) {
/* 232 */           for (String l : itemMeta.getLore()) {
/* 233 */             lore.add(l.replace(variable, messages.getString("types.bag")));
/*     */           }
/*     */         }
/*     */         break;
/*     */       case WALKING_STICK:
/* 238 */         if (itemMeta.hasDisplayName()) {
/* 239 */           itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.wstick")));
/*     */         }
/* 241 */         if (itemMeta.getLore() != null) {
/* 242 */           for (String l : itemMeta.getLore()) {
/* 243 */             lore.add(l.replace(variable, messages.getString("types.wstick")));
/*     */           }
/*     */         }
/*     */         break;
/*     */       case BALLOON:
/* 248 */         if (itemMeta.hasDisplayName()) {
/* 249 */           itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.balloon")));
/*     */         }
/* 251 */         if (itemMeta.getLore() != null) {
/* 252 */           for (String l : itemMeta.getLore()) {
/* 253 */             lore.add(l.replace(variable, messages.getString("types.balloon")));
/*     */           }
/*     */         }
/*     */         break;
/*     */       case SPRAY:
/* 258 */         if (itemMeta.hasDisplayName()) {
/* 259 */           itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.spray")));
/*     */         }
/* 261 */         if (itemMeta.getLore() != null) {
/* 262 */           for (String l : itemMeta.getLore()) {
/* 263 */             lore.add(l.replace(variable, messages.getString("types.spray")));
/*     */           }
/*     */         }
/*     */         break;
/*     */     } 
/* 268 */     itemMeta.setLore(lore);
/* 269 */     this.itemStack.setItemMeta(itemMeta);
/* 270 */     return this;
/*     */   }
/*     */   
/*     */   public Items addVariable(String variable, int number) {
/* 274 */     if (this.itemStack == null) return this; 
/* 275 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/* 276 */     if (itemMeta == null) return this; 
/* 277 */     if (itemMeta.hasDisplayName()) {
/* 278 */       itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, String.valueOf(number)));
/*     */     }
/* 280 */     List<String> lore = new ArrayList<>();
/* 281 */     if (itemMeta.getLore() != null) {
/* 282 */       for (String l : itemMeta.getLore()) {
/* 283 */         lore.add(l.replace(variable, String.valueOf(number)));
/*     */       }
/*     */     }
/* 286 */     itemMeta.setLore(lore);
/* 287 */     this.itemStack.setItemMeta(itemMeta);
/* 288 */     return this;
/*     */   }
/*     */   
/*     */   public Items addPlaceHolder(Player player) {
/* 292 */     if (this.itemStack == null) return this; 
/* 293 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 294 */     if (!plugin.isPlaceholderAPI()) return this; 
/* 295 */     ItemMeta itemMeta = this.itemStack.getItemMeta();
/* 296 */     if (itemMeta == null) return this; 
/* 297 */     itemMeta.setDisplayName(plugin.getPlaceholderAPI().setPlaceholders(player, itemMeta.getDisplayName()));
/* 298 */     if (itemMeta.getLore() != null) {
/* 299 */       itemMeta.setLore(plugin.getPlaceholderAPI().setPlaceholders(player, itemMeta.getLore()));
/*     */     }
/* 301 */     this.itemStack.setItemMeta(itemMeta);
/* 302 */     return this;
/*     */   }
/*     */   
/*     */   public ItemStack addVariableItem(String variable, int number) {
/* 306 */     if (this.itemStack == null) return null; 
/* 307 */     ItemStack itemStack = this.itemStack.clone();
/* 308 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 309 */     if (itemMeta == null) return itemStack; 
/* 310 */     itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, String.valueOf(number)));
/* 311 */     List<String> lore = new ArrayList<>();
/* 312 */     if (itemMeta.getLore() != null) {
/* 313 */       for (String l : itemMeta.getLore()) {
/* 314 */         lore.add(l.replace(variable, String.valueOf(number)));
/*     */       }
/*     */     }
/* 317 */     itemMeta.setLore(lore);
/* 318 */     itemStack.setItemMeta(itemMeta);
/* 319 */     return itemStack;
/*     */   }
/*     */   
/*     */   public boolean isHead() {
/* 323 */     if (this.itemStack == null) return false; 
/* 324 */     return (this.itemStack.getType() == XMaterial.PLAYER_HEAD.parseMaterial());
/*     */   }
/*     */   
/*     */   public ItemStack copyItem(Color color, Color compare) {
/* 328 */     if (this.itemStack == null) return null; 
/* 329 */     ItemStack itemStack = color.getPrimaryItem().clone();
/* 330 */     itemStack.setAmount(this.itemStack.getAmount());
/* 331 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 332 */     if (itemMeta == null) return itemStack; 
/* 333 */     if (itemMeta.hasLore()) {
/* 334 */       List<String> lore = new ArrayList<>();
/* 335 */       if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 336 */         if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 337 */           lore.addAll(getLoreAvailable());
/* 338 */           if (itemMeta.hasLore()) {
/* 339 */             lore.addAll(itemMeta.getLore());
/*     */           }
/*     */         } 
/* 342 */       } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 343 */         lore.addAll(getLoreUnselect());
/* 344 */         if (itemMeta.hasLore()) {
/* 345 */           lore.addAll(itemMeta.getLore());
/*     */         }
/*     */       } 
/* 348 */       itemMeta.setLore(lore);
/*     */     } 
/* 350 */     itemStack.setItemMeta(itemMeta);
/* 351 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack copyItem(PlayerData playerData, Cosmetic cosmetic, ItemStack head) {
/* 355 */     if (this.itemStack == null) return null; 
/* 356 */     ItemStack itemStack = head.clone();
/* 357 */     if (!isHead()) return itemStack; 
/* 358 */     itemStack.setAmount(this.itemStack.getAmount());
/* 359 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 360 */     if (itemMeta == null) return itemStack; 
/* 361 */     if (this.itemStack.getItemMeta() == null) return itemStack; 
/* 362 */     if (MagicCosmetics.getInstance().isPermissions()) {
/* 363 */       if (!cosmetic.hasPermission(playerData.getOfflinePlayer().getPlayer())) {
/* 364 */         if (itemMeta.getLore() != null) {
/* 365 */           List<String> lore = itemMeta.getLore();
/* 366 */           lore.addAll(this.loreUnavailable);
/* 367 */           itemMeta.setLore(lore);
/*     */         } else {
/* 369 */           itemMeta.setLore(this.loreUnavailable);
/*     */         }
/*     */       
/* 372 */       } else if (itemMeta.getLore() != null) {
/* 373 */         List<String> lore = itemMeta.getLore();
/* 374 */         lore.addAll(this.loreAvailable);
/* 375 */         itemMeta.setLore(lore);
/*     */       } else {
/* 377 */         itemMeta.setLore(this.loreAvailable);
/*     */       }
/*     */     
/*     */     }
/* 381 */     else if (playerData.getCosmeticById(cosmetic.getId()) == null) {
/* 382 */       if (itemMeta.getLore() != null) {
/* 383 */         List<String> lore = itemMeta.getLore();
/* 384 */         lore.addAll(this.loreUnavailable);
/* 385 */         itemMeta.setLore(lore);
/*     */       } else {
/* 387 */         itemMeta.setLore(this.loreUnavailable);
/*     */       }
/*     */     
/* 390 */     } else if (itemMeta.getLore() != null) {
/* 391 */       List<String> lore = itemMeta.getLore();
/* 392 */       lore.addAll(this.loreAvailable);
/* 393 */       itemMeta.setLore(lore);
/*     */     } else {
/* 395 */       itemMeta.setLore(this.loreAvailable);
/*     */     } 
/*     */ 
/*     */     
/* 399 */     itemMeta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 400 */     if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 401 */       itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */     }
/* 403 */     itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE });
/* 404 */     itemMeta.setUnbreakable(this.itemStack.getItemMeta().isUnbreakable());
/* 405 */     if (!cosmetic.isTexture() && 
/* 406 */       itemStack.getType() == Material.PLAYER_HEAD) {
/* 407 */       SkullMeta skullMeta = (SkullMeta)itemMeta;
/* 408 */       skullMeta.setOwningPlayer(playerData.getOfflinePlayer());
/* 409 */       itemStack.setItemMeta((ItemMeta)skullMeta);
/* 410 */       return itemStack;
/*     */     } 
/*     */     
/* 413 */     itemStack.setItemMeta(itemMeta);
/* 414 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack colorItem(Color color, Color compare) {
/* 418 */     if (this.itemStack == null) return null; 
/* 419 */     ItemStack itemStack = this.itemStack.clone();
/* 420 */     itemStack.setAmount(this.itemStack.getAmount());
/* 421 */     if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 422 */       LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
/* 423 */       if (meta == null) return itemStack; 
/* 424 */       meta.setColor(color.getPrimaryColor());
/* 425 */       if (this.itemStack.getItemMeta() != null) {
/* 426 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 427 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 429 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 430 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 431 */           if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 432 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 433 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 436 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 437 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 440 */           meta.setLore(lore);
/*     */         }
/* 442 */         else if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 443 */           meta.setLore(getLoreAvailable());
/*     */         } else {
/* 445 */           meta.setLore(getLoreUnselect());
/*     */         } 
/*     */         
/* 448 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 449 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 450 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 453 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
/* 454 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 456 */     if (itemStack.getItemMeta() instanceof PotionMeta) {
/* 457 */       PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
/* 458 */       if (meta == null) return itemStack; 
/* 459 */       meta.setColor(color.getPrimaryColor());
/* 460 */       if (this.itemStack.getItemMeta() != null) {
/* 461 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 462 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 464 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 465 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 466 */           if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 467 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 468 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 471 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 472 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 475 */           meta.setLore(lore);
/*     */         }
/* 477 */         else if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 478 */           meta.setLore(getLoreAvailable());
/*     */         } else {
/* 480 */           meta.setLore(getLoreUnselect());
/*     */         } 
/*     */         
/* 483 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 484 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 485 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 488 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 489 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 491 */     if (itemStack.getItemMeta() instanceof MapMeta) {
/* 492 */       MapMeta meta = (MapMeta)itemStack.getItemMeta();
/* 493 */       if (meta == null) return itemStack; 
/* 494 */       meta.setColor(color.getPrimaryColor());
/* 495 */       if (this.itemStack.getItemMeta() != null) {
/* 496 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 497 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 499 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 500 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 501 */           if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 502 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 503 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 506 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 507 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 510 */           meta.setLore(lore);
/*     */         }
/* 512 */         else if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 513 */           meta.setLore(getLoreAvailable());
/*     */         } else {
/* 515 */           meta.setLore(getLoreUnselect());
/*     */         } 
/*     */         
/* 518 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 519 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 520 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 523 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 524 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 526 */     if (itemStack.getItemMeta() instanceof FireworkEffectMeta) {
/* 527 */       FireworkEffectMeta meta = (FireworkEffectMeta)itemStack.getItemMeta();
/* 528 */       if (meta == null) return itemStack; 
/* 529 */       meta.setEffect(FireworkEffect.builder().withColor(color.getPrimaryColor()).build());
/* 530 */       if (this.itemStack.getItemMeta() != null) {
/* 531 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 532 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 534 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 535 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 536 */           if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 537 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 538 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 541 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 542 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 545 */           meta.setLore(lore);
/*     */         }
/* 547 */         else if (color.getId().equalsIgnoreCase(compare.getId())) {
/* 548 */           meta.setLore(getLoreAvailable());
/*     */         } else {
/* 550 */           meta.setLore(getLoreUnselect());
/*     */         } 
/*     */         
/* 553 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 554 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 555 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 558 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 559 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 561 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack colorItem(Player player, SecondaryColor color, SecondaryColor compare) {
/* 565 */     if (this.itemStack == null) return null; 
/* 566 */     ItemStack itemStack = this.itemStack.clone();
/* 567 */     itemStack.setAmount(this.itemStack.getAmount());
/* 568 */     if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 569 */       LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
/* 570 */       if (meta == null) return itemStack; 
/* 571 */       meta.setColor(color.getColor());
/* 572 */       if (this.itemStack.getItemMeta() != null) {
/* 573 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 574 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 576 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 577 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 578 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 579 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 580 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 583 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 584 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 587 */           if (!color.hasPermission(player))
/* 588 */             lore.addAll(getLoreUnavailable()); 
/* 589 */           meta.setLore(lore);
/*     */         } else {
/* 591 */           List<String> lore = new ArrayList<>();
/* 592 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 593 */             lore.addAll(getLoreAvailable());
/*     */           } else {
/* 595 */             lore.addAll(getLoreUnselect());
/*     */           } 
/* 597 */           if (!color.hasPermission(player))
/* 598 */             lore.addAll(getLoreUnavailable()); 
/* 599 */           meta.setLore(lore);
/*     */         } 
/* 601 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 602 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 603 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 606 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
/* 607 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 609 */     if (itemStack.getItemMeta() instanceof PotionMeta) {
/* 610 */       PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
/* 611 */       if (meta == null) return itemStack; 
/* 612 */       meta.setColor(color.getColor());
/* 613 */       if (this.itemStack.getItemMeta() != null) {
/* 614 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 615 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 617 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 618 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 619 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 620 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 621 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 624 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 625 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 628 */           if (!color.hasPermission(player))
/* 629 */             lore.addAll(getLoreUnavailable()); 
/* 630 */           meta.setLore(lore);
/*     */         } else {
/* 632 */           List<String> lore = new ArrayList<>();
/* 633 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 634 */             lore.addAll(getLoreAvailable());
/*     */           } else {
/* 636 */             lore.addAll(getLoreUnselect());
/*     */           } 
/* 638 */           if (!color.hasPermission(player))
/* 639 */             lore.addAll(getLoreUnavailable()); 
/* 640 */           meta.setLore(lore);
/*     */         } 
/* 642 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 643 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 644 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 647 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 648 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 650 */     if (itemStack.getItemMeta() instanceof MapMeta) {
/* 651 */       MapMeta meta = (MapMeta)itemStack.getItemMeta();
/* 652 */       if (meta == null) return itemStack; 
/* 653 */       meta.setColor(color.getColor());
/* 654 */       if (this.itemStack.getItemMeta() != null) {
/* 655 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 656 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 658 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 659 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 660 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 661 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 662 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 665 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 666 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 669 */           if (!color.hasPermission(player))
/* 670 */             lore.addAll(getLoreUnavailable()); 
/* 671 */           meta.setLore(lore);
/*     */         } else {
/* 673 */           List<String> lore = new ArrayList<>();
/* 674 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 675 */             lore.addAll(getLoreAvailable());
/*     */           } else {
/* 677 */             lore.addAll(getLoreUnselect());
/*     */           } 
/* 679 */           if (!color.hasPermission(player))
/* 680 */             lore.addAll(getLoreUnavailable()); 
/* 681 */           meta.setLore(lore);
/*     */         } 
/* 683 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 684 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 685 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 688 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 689 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 691 */     if (itemStack.getItemMeta() instanceof FireworkEffectMeta) {
/* 692 */       FireworkEffectMeta meta = (FireworkEffectMeta)itemStack.getItemMeta();
/* 693 */       if (meta == null) return itemStack; 
/* 694 */       meta.setEffect(FireworkEffect.builder().withColor(color.getColor()).build());
/* 695 */       if (this.itemStack.getItemMeta() != null) {
/* 696 */         if (this.itemStack.getItemMeta().hasDisplayName()) {
/* 697 */           meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName());
/*     */         }
/* 699 */         if (this.itemStack.getItemMeta().getLore() != null) {
/* 700 */           List<String> lore = this.itemStack.getItemMeta().getLore();
/* 701 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 702 */             if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
/* 703 */               lore.addAll(getLoreAvailable());
/*     */             }
/*     */           }
/* 706 */           else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
/* 707 */             lore.addAll(getLoreUnselect());
/*     */           } 
/*     */           
/* 710 */           if (!color.hasPermission(player))
/* 711 */             lore.addAll(getLoreUnavailable()); 
/* 712 */           meta.setLore(lore);
/*     */         } else {
/* 714 */           List<String> lore = new ArrayList<>();
/* 715 */           if (color.getColor().asRGB() == compare.getColor().asRGB()) {
/* 716 */             lore.addAll(getLoreAvailable());
/*     */           } else {
/* 718 */             lore.addAll(getLoreUnselect());
/*     */           } 
/* 720 */           if (!color.hasPermission(player))
/* 721 */             lore.addAll(getLoreUnavailable()); 
/* 722 */           meta.setLore(lore);
/*     */         } 
/* 724 */         meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
/* 725 */         if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
/* 726 */           meta.addEnchant(Enchantment.DURABILITY, 1, true);
/*     */         }
/*     */       } 
/* 729 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 730 */       itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 732 */     return itemStack;
/*     */   }
/*     */   
/*     */   public Items coloredItem(Color color) {
/* 736 */     setDyeColor(color);
/* 737 */     if (this.itemStack == null) return this; 
/* 738 */     if (this.itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 739 */       LeatherArmorMeta meta = (LeatherArmorMeta)this.itemStack.getItemMeta();
/* 740 */       if (meta == null) return this; 
/* 741 */       meta.setColor(color);
/* 742 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
/* 743 */       this.itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 745 */     if (this.itemStack.getItemMeta() instanceof PotionMeta) {
/* 746 */       PotionMeta meta = (PotionMeta)this.itemStack.getItemMeta();
/* 747 */       if (meta == null) return this; 
/* 748 */       meta.setColor(color);
/* 749 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 750 */       this.itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 752 */     if (this.itemStack.getItemMeta() instanceof MapMeta) {
/* 753 */       MapMeta meta = (MapMeta)this.itemStack.getItemMeta();
/* 754 */       if (meta == null) return this; 
/* 755 */       meta.setColor(color);
/* 756 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 757 */       this.itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 759 */     if (this.itemStack.getItemMeta() instanceof FireworkEffectMeta) {
/* 760 */       FireworkEffectMeta meta = (FireworkEffectMeta)this.itemStack.getItemMeta();
/* 761 */       if (meta == null) return this; 
/* 762 */       meta.setEffect(FireworkEffect.builder().withColor(color).build());
/* 763 */       meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
/* 764 */       this.itemStack.setItemMeta((ItemMeta)meta);
/*     */     } 
/* 766 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isColored(ItemStack itemStack) {
/* 770 */     if (itemStack == null) return false; 
/* 771 */     if (this.itemStack == null) return false; 
/* 772 */     ItemMeta thisMeta = this.itemStack.getItemMeta();
/* 773 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 774 */     if (thisMeta == null || itemMeta == null) return false; 
/* 775 */     if (thisMeta.getLore() == null || itemMeta.getLore() == null) return false; 
/* 776 */     return containsLore(itemMeta.getLore(), thisMeta.getLore());
/*     */   }
/*     */   
/*     */   public boolean containsLore(List<String> lore, List<String> containsLore) {
/* 780 */     if (containsLore == null || containsLore.isEmpty()) return false; 
/* 781 */     for (String l : containsLore) {
/* 782 */       if (!lore.contains(l))
/* 783 */         continue;  return true;
/*     */     } 
/* 785 */     return false;
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 789 */     if (this.itemStack.getItemMeta() == null) return null; 
/* 790 */     ItemMeta meta = this.itemStack.getItemMeta();
/* 791 */     if (meta == null) return null; 
/* 792 */     if (this.itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 793 */       return ((LeatherArmorMeta)meta).getColor();
/*     */     }
/* 795 */     if (this.itemStack.getItemMeta() instanceof PotionMeta) {
/* 796 */       return ((PotionMeta)meta).getColor();
/*     */     }
/* 798 */     if (this.itemStack.getItemMeta() instanceof MapMeta) {
/* 799 */       return ((MapMeta)meta).getColor();
/*     */     }
/* 801 */     if (this.itemStack.getItemMeta() instanceof FireworkEffectMeta) {
/* 802 */       return ((FireworkEffectMeta)meta).getEffect().getColors().get(0);
/*     */     }
/* 804 */     return null;
/*     */   }
/*     */   
/*     */   public List<String> getLoreAvailable() {
/* 808 */     return this.loreAvailable;
/*     */   }
/*     */   
/*     */   public List<String> getLoreUnavailable() {
/* 812 */     return this.loreUnavailable;
/*     */   }
/*     */   
/*     */   public List<String> getLoreUnselect() {
/* 816 */     return this.loreUnselect;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 820 */     return this.id;
/*     */   }
/*     */   
/*     */   public ItemStack getItemStack() {
/* 824 */     return this.itemStack;
/*     */   }
/*     */   
/*     */   public Color getDyeColor() {
/* 828 */     return this.dyeColor;
/*     */   }
/*     */   
/*     */   public void setDyeColor(Color dyeColor) {
/* 832 */     this.dyeColor = dyeColor;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\items\Items.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */