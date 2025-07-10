package com.francobm.magicosmetics.cache.inventories.menus;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.inventories.ActionType;
import com.francobm.magicosmetics.cache.inventories.ContentMenu;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.inventories.PaginatedMenu;
import com.francobm.magicosmetics.cache.inventories.SlotMenu;
import com.francobm.magicosmetics.cache.items.Items;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HatMenu extends PaginatedMenu {
  public HatMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
    super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
  }
  
  public HatMenu(String id, ContentMenu contentMenu) {
    super(id, contentMenu);
  }
  
  public HatMenu(PlayerData playerData, Menu menu) {
    super(playerData, menu);
  }
  
  public void handleMenu(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    int slot = event.getSlot();
    SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
    if (slotMenu == null)
      return; 
    if (slotMenu.getItems().getId().endsWith("_hat") && 
      slotMenu.isExchangeable()) {
      Cosmetic cosmetic = slotMenu.getTempCosmetic();
      if (cosmetic != null && this.playerData.hasCosmeticById(cosmetic.getId()) && 
        event.getClick() == ClickType.SHIFT_LEFT) {
        if (!slotMenu.action(player, ActionType.REMOVE_COSMETIC_ADD_TOKEN))
          return; 
        setItems();
        return;
      } 
      slotMenu.action(player);
      return;
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
      int cosmetics = Cosmetic.getCosmeticCount(CosmeticType.HAT);
      if (this.index + 1 >= cosmetics)
        return; 
      this.page++;
      open();
      return;
    } 
    slotMenu.action(player);
  }
  
  public void setItems() {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    getContentMenu().getSlots().resetSlots();
    StringBuilder title = new StringBuilder();
    title.append(getContentMenu().getTitle());
    List<Cosmetic> cosmetics = Cosmetic.getCosmeticsUnHideByType(CosmeticType.HAT);
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
    if (!cosmetics.isEmpty()) {
      int a = 0;
      for (int i = 0; i < getMaxItemsPerPage(); i++) {
        SlotMenu slotMenu;
        this.index = getMaxItemsPerPage() * this.page + i;
        if (this.index >= cosmetics.size())
          break; 
        Cosmetic cosmetic = cosmetics.get(this.index);
        int slot = getStartSlot() + i + a;
        if (cosmetic == null)
          continue; 
        while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
          slot++;
          a++;
        } 
        title.append(getContentMenu().getSlots().isSlot(slot));
        Items items = new Items("" + getPage() + this.index + "_hat", Items.getItem("hat-template").copyItem(this.playerData, cosmetic, cosmetic.getItemStack()));
        items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
        items.addVariable("%equip%", (this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.getMessages().getString("equip") : plugin.getMessages().getString("unequip"));
        if (plugin.isPermissions()) {
          items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer()) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
          if (cosmetic.hasPermission(this.playerData.getOfflinePlayer().getPlayer())) {
            title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
          } else {
            if (!this.showAllCosmeticsInMenu)
              continue; 
            title.append(plugin.unAva);
          } 
        } else {
          items.addVariable("%name%", cosmetic.getName()).addVariable("%available%", (this.playerData.getCosmeticById(cosmetic.getId()) != null) ? plugin.getMessages().getString("available") : plugin.getMessages().getString("unavailable")).addVariable("%type%", cosmetic.getCosmeticType());
          if (this.playerData.getCosmeticById(cosmetic.getId()) != null) {
            title.append((this.playerData.getEquip(cosmetic.getId()) != null) ? plugin.equip : plugin.ava);
          } else {
            if (!this.showAllCosmeticsInMenu)
              continue; 
            title.append(plugin.unAva);
          } 
        } 
        title.append(getPanel(slot));
        if (this.playerData.getHat() != null) {
          if (this.playerData.getHat().getId().equalsIgnoreCase(cosmetic.getId())) {
            slotMenu = new SlotMenu(slot, items, Collections.singletonList("magiccos unset " + cosmetic.getId()), new ActionType[] { ActionType.PLAYER_COMMAND });
          } else if (cosmetic.isColored()) {
            slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
          } else {
            slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
          } 
        } else if (cosmetic.isColored()) {
          slotMenu = new SlotMenu(slot, items, "colored|color1|" + cosmetic.getId(), new ActionType[] { ActionType.OPEN_MENU });
        } else {
          slotMenu = new SlotMenu(slot, items, cosmetic, new ActionType[] { ActionType.PREVIEW_ITEM });
        } 
        slotMenu.setSound(Sound.getSound("on_click_cosmetic"));
        slotMenu.setTempCosmetic(cosmetic);
        Token token = Token.getTokenByCosmetic(cosmetic.getId());
        slotMenu.setExchangeable((token != null && token.isExchangeable()));
        getContentMenu().addSlotMenu(slotMenu);
        setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_hat");
        continue;
      } 
    } 
    if (!getNextSlot().isEmpty())
      for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) {
        SlotMenu s;
        int slot = ((Integer)iterator.next()).intValue();
        if (this.index + 1 >= cosmetics.size()) {
          s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } else {
          s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.OPEN_MENU });
        } 
        s.setSound(Sound.getSound("on_click_next_page"));
        getContentMenu().addSlotMenu(s);
      }  
    for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values())
      setItemInPaginatedMenu(slotMenu, -1, -1, "_hat"); 
    if (plugin.isPlaceholderAPI()) {
      plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
      return;
    } 
    plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\HatMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */