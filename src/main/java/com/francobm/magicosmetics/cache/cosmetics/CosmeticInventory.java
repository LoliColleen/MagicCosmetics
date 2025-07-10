package com.francobm.magicosmetics.cache.cosmetics;

import org.bukkit.inventory.ItemStack;

public interface CosmeticInventory {
  ItemStack changeItem(ItemStack paramItemStack);
  
  void leftItem();
  
  ItemStack leftItemAndGet();
  
  ItemStack getCurrentItemSaved();
  
  void setCurrentItemSaved(ItemStack paramItemStack);
  
  boolean isOverlaps();
  
  void dropItem(boolean paramBoolean);
  
  ItemStack getEquipment();
  
  void forceRemove();
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\CosmeticInventory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */