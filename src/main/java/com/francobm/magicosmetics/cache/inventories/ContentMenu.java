package com.francobm.magicosmetics.cache.inventories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ContentMenu {
  private Inventory inventory;
  
  private String title;
  
  private final int size;
  
  private final InventoryType inventoryType;
  
  private final Map<Integer, SlotMenu> slotMenu;
  
  private final int previewSlot;
  
  private final int resultSlot;
  
  private final Slots slots;
  
  public ContentMenu(String title, int size, InventoryType inventoryType, Map<Integer, SlotMenu> slotMenu) {
    this.title = title;
    this.size = size;
    this.inventoryType = inventoryType;
    this.slotMenu = slotMenu;
    this.resultSlot = 0;
    this.previewSlot = 0;
    this.slots = new Slots();
  }
  
  public ContentMenu(String title, int size, InventoryType inventoryType, Map<Integer, SlotMenu> slotMenu, int previewSlot, int resultSlot) {
    this.title = title;
    this.size = size;
    this.inventoryType = inventoryType;
    this.slotMenu = slotMenu;
    this.previewSlot = previewSlot;
    this.resultSlot = resultSlot;
    this.slots = new Slots();
  }
  
  public ContentMenu(String title, int size, InventoryType inventoryType) {
    this.title = title;
    this.size = size;
    this.inventoryType = inventoryType;
    this.slotMenu = new HashMap<>();
    this.previewSlot = 0;
    this.resultSlot = 0;
    this.slots = new Slots();
  }
  
  public ContentMenu getClone() {
    Map<Integer, SlotMenu> slotMenus = new HashMap<>(this.slotMenu);
    return new ContentMenu(this.title, this.size, this.inventoryType, slotMenus, this.previewSlot, this.resultSlot);
  }
  
  public void createInventory(InventoryHolder inventoryHolder) {
    this.inventory = Bukkit.createInventory(inventoryHolder, 9 * this.size, this.title);
  }
  
  public void createInventory(InventoryHolder inventoryHolder, String title) {
    this.inventory = Bukkit.createInventory(inventoryHolder, 9 * this.size, title);
  }
  
  public Inventory getInventory() {
    return this.inventory;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public InventoryType getInventoryType() {
    return this.inventoryType;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public Map<Integer, SlotMenu> getSlotMenu() {
    return this.slotMenu;
  }
  
  public SlotMenu getSlotMenuBySlot(int slot) {
    return this.slotMenu.get(Integer.valueOf(slot));
  }
  
  public void removeSlotMenu(int slot) {
    this.slotMenu.remove(Integer.valueOf(slot));
  }
  
  public void resetSlotMenu(List<Integer> ignoredSlots) {
    Iterator<SlotMenu> slotMenus = this.slotMenu.values().iterator();
    while (slotMenus.hasNext()) {
      SlotMenu slotMenu = slotMenus.next();
      if (ignoredSlots.contains(Integer.valueOf(slotMenu.getSlot())))
        continue; 
      slotMenus.remove();
    } 
  }
  
  public void addSlotMenu(SlotMenu slotMenu) {
    this.slotMenu.put(Integer.valueOf(slotMenu.getSlot()), slotMenu);
  }
  
  public int getResultSlot() {
    return this.resultSlot;
  }
  
  public int getPreviewSlot() {
    return this.previewSlot;
  }
  
  public Slots getSlots() {
    return this.slots;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\ContentMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */