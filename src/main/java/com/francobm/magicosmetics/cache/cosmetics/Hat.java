package com.francobm.magicosmetics.cache.cosmetics;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.utils.DefaultAttributes;
import com.francobm.magicosmetics.utils.Utils;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Hat extends Cosmetic implements CosmeticInventory {
  private boolean overlaps;
  
  private double offSetY;
  
  private ItemStack currentItemSaved = null;
  
  private ItemStack combinedItem = null;
  
  private boolean hasDropped;
  
  public Hat(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, boolean overlaps, String permission, boolean texture, boolean hideMenu, boolean useEmote, double offSetY, NamespacedKey namespacedKey) {
    super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
    this.overlaps = overlaps;
    this.offSetY = offSetY;
  }
  
  protected void updateCosmetic(Cosmetic cosmetic) {
    super.updateCosmetic(cosmetic);
    Hat hat = (Hat)cosmetic;
    this.overlaps = hat.overlaps;
    this.offSetY = hat.offSetY;
  }
  
  public boolean updateProperties() {
    boolean result = super.updateProperties();
    if (result)
      update(); 
    return result;
  }
  
  public void update() {
    if (isHideCosmetic())
      return; 
    if (this.lendEntity != null) {
      lendToEntity();
      return;
    } 
    if (!this.overlaps) {
      ItemStack itemStack1 = this.player.getInventory().getHelmet();
      if (this.currentItemSaved != null) {
        this.player.getInventory().setHelmet(this.currentItemSaved);
        return;
      } 
      if (itemStack1 == null || itemStack1.getType().isAir() || isCosmetic(itemStack1)) {
        this.currentItemSaved = null;
        this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
        return;
      } 
      this.currentItemSaved = itemStack1;
      return;
    } 
    if (this.currentItemSaved != null) {
      this.combinedItem = combinedItems(this.currentItemSaved);
      this.player.getInventory().setHelmet(this.combinedItem);
      return;
    } 
    ItemStack itemStack = this.player.getInventory().getHelmet();
    if (itemStack == null || itemStack.getType().isAir() || isCosmetic(itemStack)) {
      this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
      return;
    } 
    this.combinedItem = combinedItems(itemStack);
    this.player.getInventory().setHelmet(this.combinedItem);
  }
  
  public ItemStack changeItem(ItemStack originalItem) {
    if (isCosmetic(originalItem))
      return null; 
    if (!this.overlaps) {
      ItemStack itemStack;
      if (originalItem == null && (
        this.currentItemSaved == null || this.currentItemSaved.getType().isAir())) {
        this.currentItemSaved = null;
        this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
        return null;
      } 
      if (this.player.getInventory().getHelmet() != null && this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
        itemStack = this.player.getInventory().getHelmet().clone();
      } else {
        itemStack = (this.currentItemSaved != null) ? this.currentItemSaved.clone() : null;
      } 
      this.currentItemSaved = originalItem;
      this.player.getInventory().setHelmet(this.currentItemSaved);
      return itemStack;
    } 
    ItemStack helmet = (this.currentItemSaved != null) ? MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone()) : null;
    this.combinedItem = combinedItems(originalItem);
    this.player.getInventory().setHelmet(this.combinedItem);
    return helmet;
  }
  
  public void leftItem() {
    if (this.currentItemSaved == null)
      return; 
    if (!this.overlaps) {
      if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir())
        return; 
      if (isCosmetic(this.player.getInventory().getHelmet()))
        return; 
      if (this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
        this.player.setItemOnCursor(this.currentItemSaved.clone());
      } else {
        this.player.setItemOnCursor(this.player.getInventory().getHelmet().clone());
      } 
      this.currentItemSaved = null;
      this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
      return;
    } 
    ItemStack itemSavedUpdated = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
    this.player.setItemOnCursor(itemSavedUpdated);
    this.currentItemSaved = null;
    this.combinedItem = null;
    this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
  }
  
  public ItemStack leftItemAndGet() {
    if (this.currentItemSaved == null)
      return null; 
    if (!this.overlaps) {
      ItemStack itemStack;
      if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir())
        return null; 
      if (isCosmetic(this.player.getInventory().getHelmet()))
        return null; 
      if (this.player.getInventory().getHelmet().isSimilar(this.currentItemSaved)) {
        itemStack = this.currentItemSaved.clone();
      } else {
        itemStack = this.player.getInventory().getHelmet().clone();
      } 
      this.currentItemSaved = null;
      this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
      return itemStack;
    } 
    ItemStack getItem = MagicCosmetics.getInstance().getVersion().getItemSavedWithNBTsUpdated(this.combinedItem, this.currentItemSaved.clone());
    this.currentItemSaved = null;
    this.combinedItem = null;
    this.player.getInventory().setHelmet(getItemPlaceholders(this.player));
    return getItem;
  }
  
  public void dropItem(boolean all) {
    if (this.currentItemSaved == null)
      return; 
    if (!this.overlaps) {
      if (this.player.getInventory().getHelmet() == null || this.player.getInventory().getHelmet().getType().isAir())
        return; 
      if (isCosmetic(this.player.getInventory().getHelmet()))
        return; 
      int i = this.currentItemSaved.getAmount();
      if (!all) {
        this.currentItemSaved.setAmount(i - 1);
      } else {
        this.currentItemSaved = null;
      } 
      return;
    } 
    ItemStack getItem = this.currentItemSaved.clone();
    int amount = getItem.getAmount();
    if (!all) {
      getItem.setAmount(1);
      this.currentItemSaved.setAmount(amount - 1);
    } else {
      getItem.setAmount(amount);
      this.currentItemSaved = null;
    } 
    Location location = this.player.getEyeLocation();
    location.setY(location.getY() - 0.30000001192092896D);
    Item itemEntity = this.player.getWorld().dropItem(location, getItem);
    itemEntity.setThrower(this.player.getUniqueId());
    itemEntity.setVelocity(Utils.getItemDropVelocity(this.player));
    itemEntity.setPickupDelay(40);
  }
  
  private ItemStack combinedItems(ItemStack originalItem) {
    this.currentItemSaved = originalItem;
    ItemStack cosmeticItem = getItemPlaceholders(this.player);
    if (this.currentItemSaved == null)
      return cosmeticItem; 
    ItemMeta cosmeticMeta = cosmeticItem.getItemMeta();
    ItemMeta itemSaveMeta = this.currentItemSaved.hasItemMeta() ? this.currentItemSaved.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.currentItemSaved.getType());
    if (cosmeticMeta == null || itemSaveMeta == null)
      return cosmeticItem; 
    if (!itemSaveMeta.getItemFlags().isEmpty())
      cosmeticMeta.addItemFlags((ItemFlag[])itemSaveMeta.getItemFlags().toArray((Object[])new ItemFlag[0])); 
    itemSaveMeta.getEnchants().forEach((enchantment, level) -> cosmeticMeta.addEnchant(enchantment, level.intValue(), true));
    List<String> lore = cosmeticMeta.hasLore() ? cosmeticMeta.getLore() : new ArrayList<>();
    if (itemSaveMeta.getLore() != null && !itemSaveMeta.getLore().isEmpty()) {
      lore.add("");
      lore.addAll(itemSaveMeta.getLore());
    } 
    cosmeticMeta.setLore(lore);
    Multimap<Attribute, AttributeModifier> attributes = (itemSaveMeta.getAttributeModifiers() == null) ? DefaultAttributes.defaultsOf(this.currentItemSaved) : itemSaveMeta.getAttributeModifiers();
    cosmeticMeta.setAttributeModifiers(attributes);
    cosmeticItem.setItemMeta(cosmeticMeta);
    cosmeticItem = MagicCosmetics.getInstance().getVersion().getItemWithNBTsCopy(this.currentItemSaved, cosmeticItem);
    return cosmeticItem;
  }
  
  public void lendToEntity() {
    if (this.lendEntity.getEquipment() == null)
      return; 
    if (this.lendEntity.getEquipment().getHelmet() != null && this.lendEntity.getEquipment().getHelmet().isSimilar(getItemColor(this.player)))
      return; 
    this.lendEntity.getEquipment().setHelmet(getItemColor(this.player));
  }
  
  public void hide(Player player) {}
  
  public void show(Player player) {}
  
  public void setHideCosmetic(boolean hideCosmetic) {
    super.setHideCosmetic(hideCosmetic);
    if (hideCosmetic) {
      remove();
    } else {
      update();
    } 
  }
  
  public void remove() {
    if (!this.overlaps) {
      if (this.currentItemSaved == null)
        this.player.getInventory().setHelmet(null); 
      return;
    } 
    if (this.currentItemSaved != null) {
      this.player.getInventory().setHelmet(this.currentItemSaved.clone());
      this.currentItemSaved = null;
      return;
    } 
    this.player.getInventory().setHelmet(null);
  }
  
  public void forceRemove() {
    this.currentItemSaved = null;
  }
  
  public void clearClose() {
    if (!this.overlaps) {
      if (this.currentItemSaved == null)
        this.player.getInventory().setHelmet(null); 
      return;
    } 
    if (this.currentItemSaved != null) {
      this.player.getInventory().setHelmet(this.currentItemSaved.clone());
      this.currentItemSaved = null;
      return;
    } 
    this.player.getInventory().setHelmet(null);
  }
  
  public boolean isOverlaps() {
    return this.overlaps;
  }
  
  public double getOffSetY() {
    return this.offSetY;
  }
  
  public ItemStack getCurrentItemSaved() {
    return this.currentItemSaved;
  }
  
  public void setCurrentItemSaved(ItemStack currentItemSaved) {
    this.currentItemSaved = currentItemSaved;
  }
  
  public boolean isHasDropped() {
    return this.hasDropped;
  }
  
  public void setHasDropped(boolean hasDropped) {
    this.hasDropped = hasDropped;
  }
  
  public ItemStack getEquipment() {
    return this.player.getInventory().getHelmet();
  }
  
  public void spawn(Player player) {}
  
  public void despawn(Player player) {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\Hat.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */