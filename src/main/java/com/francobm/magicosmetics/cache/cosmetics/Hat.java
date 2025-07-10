/*     */ package com.francobm.magicosmetics.cache.cosmetics;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.utils.DefaultAttributes;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeModifier;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class Hat
/*     */   extends Cosmetic implements CosmeticInventory {
/*     */   private boolean overlaps;
/*     */   private double offSetY;
/*  28 */   private ItemStack currentItemSaved = null;
/*  29 */   private ItemStack combinedItem = null;
/*     */   private boolean hasDropped;
/*     */   
/*     */   public Hat(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, boolean overlaps, String permission, boolean texture, boolean hideMenu, boolean useEmote, double offSetY, NamespacedKey namespacedKey) {
/*  33 */     super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
/*  34 */     this.overlaps = overlaps;
/*  35 */     this.offSetY = offSetY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateCosmetic(Cosmetic cosmetic) {
/*  40 */     super.updateCosmetic(cosmetic);
/*  41 */     Hat hat = (Hat)cosmetic;
/*  42 */     this.overlaps = hat.overlaps;
/*  43 */     this.offSetY = hat.offSetY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateProperties() {
/*  48 */     boolean result = super.updateProperties();
/*  49 */     if (result)
/*  50 */       update(); 
/*  51 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  56 */     if (isHideCosmetic()) {
/*     */       return;
/*     */     }
/*  59 */     if (this.lendEntity != null) {
/*  60 */       lendToEntity();
/*     */       return;
/*     */     } 
/*  63 */     if (!this.overlaps) {
/*  64 */       ItemStack itemStack1 = this.player.getInventory().getHelmet();
/*  65 */       if (this.currentItemSaved != null) {
/*  66 */         this.player.getInventory().setHelmet(this.currentItemSaved);
/*     */         return;
/*     */       } 
/*  69 */       if (itemStack1 == null || itemStack1.getType().isAir() || isCosmetic(itemStack1)) {
/*     */         
/*  71 */         this.currentItemSaved = null;
/*  72 */         this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/*     */         return;
/*     */       } 
/*  75 */       this.currentItemSaved = itemStack1;
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     if (this.currentItemSaved != null) {
/*  80 */       this.combinedItem = combinedItems(this.currentItemSaved);
/*  81 */       this.player.getInventory().setHelmet(this.combinedItem);
/*     */       return;
/*     */     } 
/*  84 */     ItemStack itemStack = this.player.getInventory().getHelmet();
/*  85 */     if (itemStack == null || itemStack.getType().isAir() || isCosmetic(itemStack)) {
/*     */       
/*  87 */       this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/*     */       return;
/*     */     } 
/*  90 */     this.combinedItem = combinedItems(itemStack);
/*  91 */     this.player.getInventory().setHelmet(this.combinedItem);
/*     */   }
/*     */   
/*     */   public ItemStack changeItem(ItemStack originalItem) {
/*  95 */     if (isCosmetic(originalItem)) return null; 
/*  96 */     if (!this.overlaps) {
/*  97 */       ItemStack itemStack; if (originalItem == null && (
/*  98 */         this.currentItemSaved == null || this.currentItemSaved.getType().isAir())) {
/*  99 */         this.currentItemSaved = null;
/* 100 */         this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/* 101 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 105 */       if (this.player.getInventory().getHelmet() != null && this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
/* 106 */         itemStack = this.player.getInventory().getHelmet().clone();
/*     */       } else {
/* 108 */         itemStack = (this.currentItemSaved != null) ? this.currentItemSaved.clone() : null;
/*     */       } 
/* 110 */       this.currentItemSaved = originalItem;
/* 111 */       this.player.getInventory().setHelmet(this.currentItemSaved);
/* 112 */       return itemStack;
/*     */     } 
/* 114 */     ItemStack helmet = (this.currentItemSaved != null) ? MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone()) : null;
/* 115 */     this.combinedItem = combinedItems(originalItem);
/* 116 */     this.player.getInventory().setHelmet(this.combinedItem);
/* 117 */     return helmet;
/*     */   }
/*     */   
/*     */   public void leftItem() {
/* 121 */     if (this.currentItemSaved == null)
/* 122 */       return;  if (!this.overlaps) {
/* 123 */       if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir())
/* 124 */         return;  if (isCosmetic(this.player.getInventory().getHelmet()))
/* 125 */         return;  if (this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
/* 126 */         this.player.setItemOnCursor(this.currentItemSaved.clone());
/*     */       } else {
/* 128 */         this.player.setItemOnCursor(this.player.getInventory().getHelmet().clone());
/* 129 */       }  this.currentItemSaved = null;
/* 130 */       this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/*     */       return;
/*     */     } 
/* 133 */     ItemStack itemSavedUpdated = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
/* 134 */     this.player.setItemOnCursor(itemSavedUpdated);
/* 135 */     this.currentItemSaved = null;
/* 136 */     this.combinedItem = null;
/* 137 */     this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack leftItemAndGet() {
/* 142 */     if (this.currentItemSaved == null) return null; 
/* 143 */     if (!this.overlaps) {
/* 144 */       ItemStack itemStack; if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir()) return null; 
/* 145 */       if (isCosmetic(this.player.getInventory().getHelmet())) return null;
/*     */       
/* 147 */       if (this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
/* 148 */         itemStack = this.currentItemSaved.clone();
/*     */       } else {
/* 150 */         itemStack = this.player.getInventory().getHelmet().clone();
/* 151 */       }  this.currentItemSaved = null;
/* 152 */       this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/* 153 */       return itemStack;
/*     */     } 
/* 155 */     ItemStack getItem = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
/* 156 */     this.currentItemSaved = null;
/* 157 */     this.combinedItem = null;
/* 158 */     this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
/* 159 */     return getItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(boolean all) {
/* 164 */     if (this.currentItemSaved == null)
/*     */       return; 
/* 166 */     if (!this.overlaps) {
/* 167 */       if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir())
/* 168 */         return;  if (isCosmetic(this.player.getInventory().getHelmet()))
/* 169 */         return;  int i = this.currentItemSaved.getAmount();
/* 170 */       if (!all) {
/* 171 */         this.currentItemSaved.setAmount(i - 1);
/*     */       } else {
/* 173 */         this.currentItemSaved = null;
/*     */       } 
/*     */       return;
/*     */     } 
/* 177 */     ItemStack getItem = this.currentItemSaved.clone();
/* 178 */     int amount = getItem.getAmount();
/* 179 */     if (!all) {
/* 180 */       getItem.setAmount(1);
/* 181 */       this.currentItemSaved.setAmount(amount - 1);
/*     */     } else {
/* 183 */       getItem.setAmount(amount);
/* 184 */       this.currentItemSaved = null;
/*     */     } 
/* 186 */     Location location = this.player.getEyeLocation();
/* 187 */     location.setY(location.getY() - 0.30000001192092896D);
/* 188 */     Item itemEntity = this.player.getWorld().dropItem(location, getItem);
/* 189 */     itemEntity.setThrower(this.player.getUniqueId());
/* 190 */     itemEntity.setVelocity(Utils.getItemDropVelocity(this.player));
/* 191 */     itemEntity.setPickupDelay(40);
/*     */   }
/*     */   
/*     */   private ItemStack combinedItems(ItemStack originalItem) {
/* 195 */     this.currentItemSaved = originalItem;
/* 196 */     ItemStack cosmeticItem = getItemPlaceholders(this.player);
/* 197 */     if (this.currentItemSaved == null) return cosmeticItem; 
/* 198 */     ItemMeta cosmeticMeta = cosmeticItem.getItemMeta();
/* 199 */     ItemMeta itemSaveMeta = this.currentItemSaved.hasItemMeta() ? this.currentItemSaved.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.currentItemSaved.getType());
/* 200 */     if (cosmeticMeta == null || itemSaveMeta == null) return cosmeticItem; 
/* 201 */     if (!itemSaveMeta.getItemFlags().isEmpty())
/* 202 */       cosmeticMeta.addItemFlags((ItemFlag[])itemSaveMeta.getItemFlags().toArray((Object[])new ItemFlag[0])); 
/* 203 */     itemSaveMeta.getEnchants().forEach((enchantment, level) -> cosmeticMeta.addEnchant(enchantment, level.intValue(), true));
/* 204 */     List<String> lore = cosmeticMeta.hasLore() ? cosmeticMeta.getLore() : new ArrayList<>();
/* 205 */     if (itemSaveMeta.getLore() != null && !itemSaveMeta.getLore().isEmpty()) {
/* 206 */       lore.add("");
/* 207 */       lore.addAll(itemSaveMeta.getLore());
/*     */     } 
/* 209 */     cosmeticMeta.setLore(lore);
/*     */     
/* 211 */     Multimap<Attribute, AttributeModifier> attributes = (itemSaveMeta.getAttributeModifiers() == null) ? DefaultAttributes.defaultsOf(this.currentItemSaved) : itemSaveMeta.getAttributeModifiers();
/* 212 */     cosmeticMeta.setAttributeModifiers(attributes);
/* 213 */     cosmeticItem.setItemMeta(cosmeticMeta);
/* 214 */     cosmeticItem = MagicCosmetics.getInstance().getVersion().getItemWithNBTsCopy(this.currentItemSaved, cosmeticItem);
/* 215 */     return cosmeticItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void lendToEntity() {
/* 220 */     if (this.lendEntity.getEquipment() == null)
/* 221 */       return;  if (this.lendEntity.getEquipment().getHelmet() != null && this.lendEntity.getEquipment().getHelmet().isSimilar(getItemColor(this.player)))
/* 222 */       return;  this.lendEntity.getEquipment().setHelmet(getItemColor(this.player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hide(Player player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void show(Player player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHideCosmetic(boolean hideCosmetic) {
/* 237 */     super.setHideCosmetic(hideCosmetic);
/* 238 */     if (hideCosmetic) {
/* 239 */       remove();
/*     */     } else {
/* 241 */       update();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove() {
/* 246 */     if (!this.overlaps) {
/* 247 */       if (this.currentItemSaved == null)
/* 248 */         this.player.getInventory().setHelmet(null); 
/*     */       return;
/*     */     } 
/* 251 */     if (this.currentItemSaved != null) {
/*     */       
/* 253 */       this.player.getInventory().setHelmet(this.currentItemSaved.clone());
/* 254 */       this.currentItemSaved = null;
/*     */       return;
/*     */     } 
/* 257 */     this.player.getInventory().setHelmet(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forceRemove() {
/* 262 */     this.currentItemSaved = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearClose() {
/* 267 */     if (!this.overlaps) {
/* 268 */       if (this.currentItemSaved == null)
/* 269 */         this.player.getInventory().setHelmet(null); 
/*     */       return;
/*     */     } 
/* 272 */     if (this.currentItemSaved != null) {
/*     */       
/* 274 */       this.player.getInventory().setHelmet(this.currentItemSaved.clone());
/* 275 */       this.currentItemSaved = null;
/*     */       return;
/*     */     } 
/* 278 */     this.player.getInventory().setHelmet(null);
/*     */   }
/*     */   
/*     */   public boolean isOverlaps() {
/* 282 */     return this.overlaps;
/*     */   }
/*     */   
/*     */   public double getOffSetY() {
/* 286 */     return this.offSetY;
/*     */   }
/*     */   
/*     */   public ItemStack getCurrentItemSaved() {
/* 290 */     return this.currentItemSaved;
/*     */   }
/*     */   
/*     */   public void setCurrentItemSaved(ItemStack currentItemSaved) {
/* 294 */     this.currentItemSaved = currentItemSaved;
/*     */   }
/*     */   
/*     */   public boolean isHasDropped() {
/* 298 */     return this.hasDropped;
/*     */   }
/*     */   
/*     */   public void setHasDropped(boolean hasDropped) {
/* 302 */     this.hasDropped = hasDropped;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getEquipment() {
/* 307 */     return this.player.getInventory().getHelmet();
/*     */   }
/*     */   
/*     */   public void spawn(Player player) {}
/*     */   
/*     */   public void despawn(Player player) {}
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\Hat.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */