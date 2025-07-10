package com.francobm.magicosmetics.cache.inventories.menus;

import com.francobm.magicosmetics.MagicCosmetics;
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
import com.francobm.magicosmetics.utils.Utils;
import com.francobm.magicosmetics.utils.XMaterial;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FreeColoredMenu extends PaginatedMenu {
  private Color color;
  
  private SecondaryColor secondaryColor;
  
  private ItemStack itemStack;
  
  private Items containItem;
  
  private List<String> unavailableColors;
  
  public FreeColoredMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable, Items containItem, List<String> unavailableColors) {
    super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
    this.containItem = containItem;
    this.unavailableColors = unavailableColors;
  }
  
  public FreeColoredMenu(String id, ContentMenu contentMenu) {
    super(id, contentMenu);
  }
  
  public FreeColoredMenu(PlayerData playerData, Menu menu, Color color) {
    super(playerData, menu);
    this.color = color;
    this.containItem = ((FreeColoredMenu)menu).getContainItem();
    this.secondaryColor = color.getSecondaryColors().get(0);
  }
  
  public FreeColoredMenu getClone(PlayerData playerData, Color color, ItemStack itemStack) {
    FreeColoredMenu freeColoredMenu = new FreeColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable(), getContainItem(), getUnavailableColors());
    freeColoredMenu.playerData = playerData;
    freeColoredMenu.setColor(color);
    freeColoredMenu.itemStack = itemStack;
    freeColoredMenu.secondaryColor = color.getSecondaryColors().get(0);
    return freeColoredMenu;
  }
  
  public FreeColoredMenu getClone(PlayerData playerData, Color color) {
    FreeColoredMenu freeColoredMenu = new FreeColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable(), getContainItem(), getUnavailableColors());
    freeColoredMenu.playerData = playerData;
    freeColoredMenu.setColor(color);
    freeColoredMenu.secondaryColor = color.getSecondaryColors().get(0);
    return freeColoredMenu;
  }
  
  public void handleMenu(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (event.getClickedInventory() == null)
      return; 
    int slot = event.getSlot();
    if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
      if (event.getClick() != ClickType.LEFT) {
        event.setCancelled(true);
        return;
      } 
      return;
    } 
    if (event.getClick() != ClickType.LEFT) {
      event.setCancelled(true);
      return;
    } 
    if (getContentMenu().getPreviewSlot() == slot) {
      if (event.getCursor().getType() != XMaterial.AIR.parseMaterial()) {
        if (this.containItem != null) {
          if (this.containItem.isColored(event.getCursor())) {
            this.itemStack = event.getCursor().clone();
            SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), new Items(this.itemStack), "", new ActionType[0]);
            getContentMenu().addSlotMenu(slotMenu1);
            setResultItem();
            return;
          } 
          event.setCancelled(true);
          return;
        } 
        if (Utils.isDyeable(event.getCursor()) && this.color.hasPermission(player)) {
          this.itemStack = event.getCursor().clone();
          SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), new Items(this.itemStack), "", new ActionType[0]);
          getContentMenu().addSlotMenu(slotMenu1);
          setResultItem();
          return;
        } 
        event.setCancelled(true);
        return;
      } 
      if (event.getCurrentItem() == null)
        return; 
      this.itemStack = null;
      getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
      getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
      event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
      return;
    } 
    if (getContentMenu().getResultSlot() == slot) {
      if (event.getCurrentItem() == null) {
        event.setCancelled(true);
        return;
      } 
      this.itemStack = null;
      getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
      getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
      event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
      return;
    } 
    event.setCancelled(true);
    if (event.getCurrentItem() == null)
      return; 
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
      setSecondaryColor((SecondaryColor)null);
      setItems();
      return;
    } 
    if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
      slotMenu.playSound(player);
      if (this.index + 1 >= this.color.getSecondaryColors().size())
        return; 
      this.page++;
      setSecondaryColor((SecondaryColor)null);
      setItems();
      return;
    } 
    slotMenu.action(player);
  }
  
  public void setResultItem() {
    if (this.itemStack == null)
      return; 
    Items resultItem = (new Items(this.itemStack.clone())).coloredItem(this.secondaryColor.getColor());
    SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, "", new ActionType[0]);
    result.setSound(Sound.getSound("on_click_cosmetic_preview"));
    getContentMenu().addSlotMenu(result);
    setItemInMenu(result);
    for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
      if (!slotMenu.getItems().getId().endsWith("_colored"))
        continue; 
      Color color = slotMenu.getSlotMenu().getItems().getDyeColor();
      slotMenu.getSlotMenu().setItems((new Items(this.itemStack.clone())).coloredItem(color));
    } 
  }
  
  public void setItems() {
    resetItems(Arrays.asList(new Integer[] { Integer.valueOf(getContentMenu().getPreviewSlot()), Integer.valueOf(getContentMenu().getResultSlot()) }));
    getContentMenu().getSlots().resetSlots();
    if (!getBackSlot().isEmpty())
      for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) {
        SlotMenu s;
        int slot = ((Integer)iterator.next()).intValue();
        if (this.page == 0) {
          s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.REFRESH });
        } else {
          s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.REFRESH });
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
          Items resultItem;
          if (getSecondaryColor() == null && 
            i == 0)
            setSecondaryColor(dyeColor); 
          while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
            slot++;
            a++;
          } 
          Items items = new Items("" + getPage() + this.index + "_colored", Items.getItem("color-template").colorItem(this.playerData.getOfflinePlayer().getPlayer(), dyeColor, this.secondaryColor));
          items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
          if (dyeColor.getColor().asRGB() == this.secondaryColor.getColor().asRGB())
            title.append(selected[i]); 
          if (this.itemStack == null) {
            resultItem = (new Items(XMaterial.AIR.parseItem())).coloredItem(dyeColor.getColor());
          } else {
            resultItem = (new Items(this.itemStack.clone())).coloredItem(dyeColor.getColor());
          } 
          SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, "", new ActionType[0]);
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
          s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.REFRESH });
        } else {
          s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.REFRESH });
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
    for (Color color : Color.colors.values()) {
      Items items;
      if (this.unavailableColors.contains(color.getId()))
        continue; 
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
      SlotMenu slotMenu = new SlotMenu(color.getSlot(), items, getId() + "|" + getId(), new ActionType[] { ActionType.OPEN_MENU });
      slotMenu.setSound(Sound.getSound("on_click_item_colored"));
      getContentMenu().addSlotMenu(slotMenu);
    } 
    return title;
  }
  
  public void setSecondaryColor(Color secondaryColor) {
    this.secondaryColor = new SecondaryColor(secondaryColor);
  }
  
  public void setSecondaryColor(SecondaryColor secondaryColor) {
    this.secondaryColor = secondaryColor;
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
  
  public Items getContainItem() {
    return this.containItem;
  }
  
  public Color getColor() {
    return this.color;
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
  
  public SecondaryColor getSecondaryColor() {
    return this.secondaryColor;
  }
  
  public String[] getSelectedList() {
    return this.color.getRow().getSelected().split(",");
  }
  
  public void returnItem() {
    if (this.itemStack == null)
      return; 
    Player player = this.playerData.getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (player.getInventory().firstEmpty() == -1) {
      player.getWorld().dropItem(player.getLocation(), this.itemStack);
      this.itemStack = null;
      getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
      getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
      return;
    } 
    player.getInventory().addItem(new ItemStack[] { this.itemStack });
    this.itemStack = null;
    getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
    getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
  }
  
  public List<String> getUnavailableColors() {
    return this.unavailableColors;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\FreeColoredMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */