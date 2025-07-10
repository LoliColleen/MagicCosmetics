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
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class WStick
/*     */   extends Cosmetic
/*     */   implements CosmeticInventory {
/*     */   private boolean overlaps;
/*  27 */   private ItemStack currentItemSaved = null;
/*  28 */   private ItemStack combinedItem = null;
/*     */   private boolean hasDropped;
/*     */   
/*     */   public WStick(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, String permission, boolean texture, boolean overlaps, boolean hideMenu, boolean useEmote, NamespacedKey namespacedKey) {
/*  32 */     super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
/*  33 */     this.overlaps = overlaps;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateCosmetic(Cosmetic cosmetic) {
/*  38 */     super.updateCosmetic(cosmetic);
/*  39 */     WStick wStick = (WStick)cosmetic;
/*  40 */     this.overlaps = wStick.overlaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateProperties() {
/*  45 */     boolean result = super.updateProperties();
/*  46 */     if (result)
/*  47 */       update(); 
/*  48 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  53 */     if (this.lendEntity != null) {
/*  54 */       lendToEntity();
/*     */       return;
/*     */     } 
/*  57 */     if (isHideCosmetic()) {
/*     */       return;
/*     */     }
/*  60 */     if (!this.overlaps) {
/*  61 */       ItemStack itemStack1 = this.player.getInventory().getItemInOffHand();
/*  62 */       if (this.currentItemSaved != null) {
/*  63 */         this.player.getInventory().setItemInOffHand(this.currentItemSaved);
/*     */         return;
/*     */       } 
/*  66 */       if (itemStack1.getType().isAir() || isCosmetic(itemStack1)) {
/*     */         
/*  68 */         this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/*     */         return;
/*     */       } 
/*  71 */       this.currentItemSaved = itemStack1;
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     if (this.currentItemSaved != null) {
/*  76 */       this.combinedItem = combinedItems(this.currentItemSaved);
/*  77 */       this.player.getInventory().setItemInOffHand(this.combinedItem);
/*     */       return;
/*     */     } 
/*  80 */     ItemStack itemStack = this.player.getInventory().getItemInOffHand();
/*  81 */     if (itemStack.getType().isAir() || isCosmetic(itemStack)) {
/*     */       
/*  83 */       this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     ItemStack offHand = this.player.getInventory().getItemInOffHand();
/*  88 */     this.combinedItem = combinedItems(offHand);
/*  89 */     this.player.getInventory().setItemInOffHand(this.combinedItem);
/*     */   }
/*     */   
/*     */   public ItemStack changeItem(ItemStack originalItem) {
/*  93 */     if (isCosmetic(originalItem)) return null; 
/*  94 */     if (!this.overlaps) {
/*  95 */       ItemStack itemStack; if (originalItem == null && (
/*  96 */         this.currentItemSaved == null || this.currentItemSaved.getType().isAir())) {
/*  97 */         this.currentItemSaved = null;
/*  98 */         this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/*  99 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 103 */       if (this.player.getInventory().getItemInOffHand().isSimilar(this.currentItemSaved)) {
/* 104 */         itemStack = this.player.getInventory().getItemInOffHand().clone();
/*     */       } else {
/* 106 */         itemStack = (this.currentItemSaved != null) ? this.currentItemSaved.clone() : null;
/* 107 */       }  this.currentItemSaved = originalItem;
/* 108 */       this.player.getInventory().setItemInOffHand(this.currentItemSaved);
/* 109 */       return itemStack;
/*     */     } 
/* 111 */     ItemStack offhand = (this.currentItemSaved != null) ? MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone()) : null;
/* 112 */     this.combinedItem = combinedItems(originalItem);
/* 113 */     this.player.getInventory().setItemInOffHand(this.combinedItem);
/* 114 */     return offhand;
/*     */   }
/*     */   
/*     */   public void leftItem() {
/* 118 */     if (this.currentItemSaved == null)
/* 119 */       return;  if (!this.overlaps) {
/* 120 */       if (this.player.getInventory().getItemInOffHand().getType().isAir())
/* 121 */         return;  if (isCosmetic(this.player.getInventory().getItemInOffHand()))
/* 122 */         return;  if (this.player.getInventory().getItemInOffHand().equals(this.currentItemSaved)) {
/* 123 */         this.player.setItemOnCursor(this.currentItemSaved.clone());
/*     */       } else {
/* 125 */         this.player.setItemOnCursor(this.player.getInventory().getItemInOffHand().clone());
/* 126 */       }  this.currentItemSaved = null;
/* 127 */       this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/*     */       return;
/*     */     } 
/* 130 */     ItemStack itemSavedUpdated = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
/* 131 */     this.player.setItemOnCursor(itemSavedUpdated);
/* 132 */     this.currentItemSaved = null;
/* 133 */     this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack leftItemAndGet() {
/* 138 */     if (this.currentItemSaved == null) return null; 
/* 139 */     if (!this.overlaps) {
/* 140 */       ItemStack itemStack; if (this.player.getInventory().getItemInOffHand().getType().isAir()) return null; 
/* 141 */       if (isCosmetic(this.player.getInventory().getItemInOffHand())) return null;
/*     */       
/* 143 */       if (this.player.getInventory().getItemInOffHand().equals(this.currentItemSaved)) {
/* 144 */         itemStack = this.currentItemSaved.clone();
/*     */       } else {
/* 146 */         itemStack = this.player.getInventory().getItemInOffHand().clone();
/* 147 */       }  this.currentItemSaved = null;
/* 148 */       this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/* 149 */       return itemStack;
/*     */     } 
/* 151 */     ItemStack getItem = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
/* 152 */     this.currentItemSaved = null;
/* 153 */     this.player.getInventory().setItemInOffHand(getItemPlaceholders(this.player));
/* 154 */     return getItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(boolean all) {
/* 159 */     if (this.currentItemSaved == null)
/* 160 */       return;  if (!this.overlaps) {
/* 161 */       if (this.player.getInventory().getItemInOffHand().getType().isAir())
/* 162 */         return;  if (isCosmetic(this.player.getInventory().getItemInOffHand()))
/* 163 */         return;  int i = this.currentItemSaved.getAmount();
/* 164 */       if (!all) {
/* 165 */         this.currentItemSaved.setAmount(i - 1);
/*     */       } else {
/* 167 */         this.currentItemSaved = null;
/*     */       } 
/*     */       return;
/*     */     } 
/* 171 */     ItemStack getItem = this.currentItemSaved.clone();
/* 172 */     int amount = getItem.getAmount();
/* 173 */     if (!all) {
/* 174 */       getItem.setAmount(1);
/* 175 */       this.currentItemSaved.setAmount(amount - 1);
/*     */     } else {
/* 177 */       getItem.setAmount(amount);
/* 178 */       this.currentItemSaved = null;
/*     */     } 
/* 180 */     Location location = this.player.getEyeLocation();
/* 181 */     location.setY(location.getY() - 0.30000001192092896D);
/* 182 */     Item itemEntity = this.player.getWorld().dropItem(location, getItem);
/* 183 */     itemEntity.setThrower(this.player.getUniqueId());
/* 184 */     itemEntity.setVelocity(Utils.getItemDropVelocity(this.player));
/* 185 */     itemEntity.setPickupDelay(40);
/*     */   }
/*     */   
/*     */   private ItemStack combinedItems(ItemStack originalItem) {
/* 189 */     this.currentItemSaved = originalItem;
/* 190 */     ItemStack cosmeticItem = getItemPlaceholders(this.player);
/* 191 */     if (this.currentItemSaved == null) return cosmeticItem; 
/* 192 */     ItemMeta cosmeticMeta = cosmeticItem.getItemMeta();
/* 193 */     ItemMeta itemSaveMeta = this.currentItemSaved.hasItemMeta() ? this.currentItemSaved.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.currentItemSaved.getType());
/* 194 */     if (cosmeticMeta == null || itemSaveMeta == null) return cosmeticItem; 
/* 195 */     if (!itemSaveMeta.getItemFlags().isEmpty())
/* 196 */       cosmeticMeta.addItemFlags((ItemFlag[])itemSaveMeta.getItemFlags().toArray((Object[])new ItemFlag[0])); 
/* 197 */     List<String> lore = cosmeticMeta.hasLore() ? cosmeticMeta.getLore() : new ArrayList<>();
/* 198 */     if (itemSaveMeta.getLore() != null && !itemSaveMeta.getLore().isEmpty()) {
/* 199 */       lore.add("");
/* 200 */       lore.addAll(itemSaveMeta.getLore());
/*     */     } 
/* 202 */     cosmeticMeta.setLore(lore);
/*     */     
/* 204 */     Multimap<Attribute, AttributeModifier> attributes = (itemSaveMeta.getAttributeModifiers() == null) ? DefaultAttributes.defaultsOf(this.currentItemSaved) : itemSaveMeta.getAttributeModifiers();
/* 205 */     cosmeticMeta.setAttributeModifiers(attributes);
/* 206 */     cosmeticItem.setItemMeta(cosmeticMeta);
/* 207 */     cosmeticItem = MagicCosmetics.getInstance().getVersion().getItemWithNBTsCopy(this.currentItemSaved, cosmeticItem);
/* 208 */     return cosmeticItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void lendToEntity() {
/* 213 */     if (this.lendEntity.getEquipment() == null)
/* 214 */       return;  if (!this.lendEntity.getEquipment().getItemInOffHand().getType().isAir() && this.lendEntity.getEquipment().getItemInOffHand().isSimilar(getItemColor(this.player)))
/* 215 */       return;  this.lendEntity.getEquipment().setItemInOffHand(getItemColor(this.player));
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
/* 230 */     super.setHideCosmetic(hideCosmetic);
/* 231 */     if (hideCosmetic) {
/* 232 */       remove();
/*     */     } else {
/* 234 */       update();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove() {
/* 239 */     if (!this.overlaps) {
/* 240 */       if (this.currentItemSaved == null)
/* 241 */         this.player.getInventory().setItemInOffHand(null); 
/*     */       return;
/*     */     } 
/* 244 */     if (this.currentItemSaved != null) {
/*     */       
/* 246 */       this.player.getInventory().setItemInOffHand(this.currentItemSaved.clone());
/* 247 */       this.currentItemSaved = null;
/*     */       return;
/*     */     } 
/* 250 */     this.player.getInventory().setItemInOffHand(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forceRemove() {
/* 255 */     this.currentItemSaved = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearClose() {
/* 260 */     if (!this.overlaps) {
/* 261 */       if (this.currentItemSaved == null)
/* 262 */         this.player.getInventory().setItemInOffHand(null); 
/*     */       return;
/*     */     } 
/* 265 */     if (this.currentItemSaved != null) {
/*     */       
/* 267 */       this.player.getInventory().setItemInOffHand(this.currentItemSaved.clone());
/* 268 */       this.currentItemSaved = null;
/*     */       return;
/*     */     } 
/* 271 */     this.player.getInventory().setItemInOffHand(null);
/*     */   }
/*     */   
/*     */   public boolean isOverlaps() {
/* 275 */     return this.overlaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCurrentItemSaved() {
/* 280 */     return this.currentItemSaved;
/*     */   }
/*     */   
/*     */   public void setCurrentItemSaved(ItemStack currentItemSaved) {
/* 284 */     this.currentItemSaved = currentItemSaved;
/*     */   }
/*     */   
/*     */   public boolean isHasDropped() {
/* 288 */     return this.hasDropped;
/*     */   }
/*     */   
/*     */   public void setHasDropped(boolean hasDropped) {
/* 292 */     this.hasDropped = hasDropped;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getEquipment() {
/* 297 */     return this.player.getInventory().getItemInOffHand();
/*     */   }
/*     */   
/*     */   public void spawn(Player player) {}
/*     */   
/*     */   public void despawn(Player player) {}
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\WStick.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */