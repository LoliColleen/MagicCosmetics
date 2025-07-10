package com.francobm.magicosmetics.cache.inventories.menus;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.SecondaryColor;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.inventories.ActionType;
import com.francobm.magicosmetics.cache.inventories.ContentMenu;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.inventories.PaginatedMenu;
import com.francobm.magicosmetics.cache.inventories.SlotMenu;
import com.francobm.magicosmetics.cache.items.Items;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class ColoredMenu extends PaginatedMenu {
  private Color color;
  
  private SecondaryColor secondaryColor;
  
  private Cosmetic cosmetic;
  
  public ColoredMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
    super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
  }
  
  public ColoredMenu(String id, ContentMenu contentMenu) {
    super(id, contentMenu);
  }
  
  public ColoredMenu(PlayerData playerData, Menu menu, Color color, Cosmetic cosmetic) {
    super(playerData, menu);
    this.color = color;
    this.cosmetic = cosmetic;
    this.secondaryColor = color.getSecondaryColors().get(0);
  }
  
  public ColoredMenu getClone(PlayerData playerData, Color color, Cosmetic cosmetic) {
    ColoredMenu coloredMenu = new ColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable());
    coloredMenu.playerData = playerData;
    coloredMenu.setColor(color);
    coloredMenu.cosmetic = cosmetic;
    coloredMenu.secondaryColor = color.getSecondaryColors().get(0);
    return coloredMenu;
  }
  
  public void handleMenu(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    int slot = event.getSlot();
    SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
    if (slotMenu == null)
      return; 
    if (slotMenu.getItems().getId().endsWith("_colored")) {
      setSecondaryColor(slotMenu.getItems().getColor());
      setItems();
    } 
    if (getBackSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
      slotMenu.playSound(player);
      if (this.page == 0)
        return; 
      this.page--;
      open();
      return;
    } 
    if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
      slotMenu.playSound(player);
      if (this.index + 1 >= this.color.getSecondaryColors().size())
        return; 
      this.page++;
      open();
      return;
    } 
    slotMenu.action(player);
  }
  
  public void setItems() {
    getContentMenu().getSlots().resetSlots();
    if (!getBackSlot().isEmpty())
      for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) {
        SlotMenu s;
        int slot = ((Integer)iterator.next()).intValue();
        if (this.page == 0) {
          s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } else {
          s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } 
        s.setSound(Sound.getSound("on_click_back_page"));
        getContentMenu().addSlotMenu(s);
      }  
    if (getPagesSlot() != -1)
      getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU })); 
    String sPrimaryColor = setup();
    StringBuilder title = new StringBuilder();
    title.append(getContentMenu().getTitle());
    title.append(sPrimaryColor);
    String[] selected = getSelectedList();
    if (!this.color.getSecondaryColors().isEmpty()) {
      int a = 0;
      for (int i = 0; i < getMaxItemsPerPage(); i++) {
        this.index = getMaxItemsPerPage() * this.page + i;
        if (this.index >= this.color.getSecondaryColors().size())
          break; 
        SecondaryColor dyeColor = this.color.getSecondaryColors().get(this.index);
        int slot = getStartSlot() + i + a;
        if (dyeColor != null) {
          if (getSecondaryColor() == null && 
            i == 0)
            setSecondaryColor(dyeColor); 
          while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
            slot++;
            a++;
          } 
          Cosmetic cosmetic = Cosmetic.getCloneCosmetic(this.cosmetic.getId());
          cosmetic.setColor(dyeColor.getColor());
          if (!this.color.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) || !dyeColor.hasPermission(this.playerData.getOfflinePlayer().getPlayer()))
            cosmetic.setColorBlocked(true); 
          Items items = new Items("" + getPage() + this.index + "_colored", Items.getItem("color-template").colorItem(this.playerData.getOfflinePlayer().getPlayer(), dyeColor, this.secondaryColor));
          items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
          if (dyeColor.getColor().asRGB() == this.secondaryColor.getColor().asRGB())
            title.append(selected[i]); 
          Items resultItem = new Items(cosmetic.getItemColor());
          SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
          result.setSound(Sound.getSound("on_click_cosmetic_preview"));
          if (i == 0)
            getContentMenu().addSlotMenu(result); 
          SlotMenu slotMenu = new SlotMenu(slot, items, result, new ActionType[] { ActionType.ADD_ITEM_MENU });
          slotMenu.setSound(Sound.getSound("on_click_item_colored"));
          getContentMenu().addSlotMenu(slotMenu);
          setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_colored");
        } 
      } 
    } 
    if (!getNextSlot().isEmpty())
      for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) {
        SlotMenu s;
        int slot = ((Integer)iterator.next()).intValue();
        if (this.index + 1 >= this.color.getSecondaryColors().size()) {
          s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } else {
          s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } 
        s.setSound(Sound.getSound("on_click_next_page"));
        getContentMenu().addSlotMenu(s);
      }  
    for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values())
      setItemInPaginatedMenu(slotMenu, -1, -1, "_colored"); 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (plugin.isPlaceholderAPI()) {
      plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
      return;
    } 
    plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
  }
  
  private String setup() {
    String title = "";
    Items items = new Items(this.cosmetic.getItemStack());
    items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
    int previewSlot = getContentMenu().getPreviewSlot();
    if (previewSlot != -1) {
      SlotMenu slotMenu = new SlotMenu(previewSlot, items, "", new ActionType[] { ActionType.CLOSE_MENU });
      getContentMenu().addSlotMenu(slotMenu);
    } 
    for (Color color : Color.colors.values()) {
      if (color.isPrimaryItem()) {
        items = new Items(color.getId(), Items.getItem("color-template").copyItem(color, this.color));
      } else {
        items = new Items(color.getId(), Items.getItem("color-template").colorItem(color, this.color));
      } 
      if (color.getId().equalsIgnoreCase(this.color.getId()))
        title = color.getSelect(); 
      if (!color.getName().isEmpty()) {
        ItemMeta itemMeta = items.getItemStack().getItemMeta();
        if (itemMeta != null)
          itemMeta.setDisplayName(color.getName()); 
        items.getItemStack().setItemMeta(itemMeta);
      } 
      SlotMenu slotMenu = new SlotMenu(color.getSlot(), items, getId() + "|" + getId() + "|" + items.getId(), new ActionType[] { ActionType.OPEN_MENU });
      slotMenu.setSound(Sound.getSound("on_click_item_colored"));
      getContentMenu().addSlotMenu(slotMenu);
    } 
    return title;
  }
  
  public void setSecondaryColor(Color color) {
    this.secondaryColor = new SecondaryColor(color);
  }
  
  public void setSecondaryColor(SecondaryColor color) {
    this.secondaryColor = color;
  }
  
  public Cosmetic getCosmetic() {
    return this.cosmetic;
  }
  
  public Color getColor() {
    return this.color;
  }
  
  public SecondaryColor getSecondaryColor() {
    return this.secondaryColor;
  }
  
  public String[] getSelectedList() {
    return this.color.getRow().getSelected().split(",");
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\ColoredMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */