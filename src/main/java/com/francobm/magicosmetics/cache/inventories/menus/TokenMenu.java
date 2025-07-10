package com.francobm.magicosmetics.cache.inventories.menus;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.inventories.ActionType;
import com.francobm.magicosmetics.cache.inventories.ContentMenu;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.inventories.SlotMenu;
import com.francobm.magicosmetics.cache.items.Items;
import com.francobm.magicosmetics.utils.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class TokenMenu extends Menu {
  private boolean drag;
  
  private ItemStack itemStack;
  
  public TokenMenu(String id, ContentMenu contentMenu, boolean drag) {
    super(id, contentMenu);
    this.drag = drag;
  }
  
  public TokenMenu(PlayerData playerData, Menu menu) {
    super(playerData, menu);
    this.drag = ((TokenMenu)menu).isDrag();
  }
  
  public TokenMenu getClone(PlayerData playerData) {
    TokenMenu tokenMenuClone = new TokenMenu(getId(), getContentMenu().getClone(), isDrag());
    tokenMenuClone.playerData = playerData;
    return tokenMenuClone;
  }
  
  public void handleMenu(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (isDrag()) {
      if (event.getClickedInventory() == null)
        return; 
      int i = event.getSlot();
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
      if (getContentMenu().getPreviewSlot() == i) {
        if (event.getCursor().getType() != XMaterial.AIR.parseMaterial()) {
          Token token = Token.getTokenByItem(event.getCursor());
          if (token == null) {
            token = Token.getOldTokenByItem(event.getCursor());
            if (token == null) {
              event.setCancelled(true);
              return;
            } 
            this.itemStack = event.getCursor().clone();
            Items items1 = new Items(event.getCursor());
            items1.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
            SlotMenu slotMenu2 = new SlotMenu(getContentMenu().getPreviewSlot(), items1, "", new ActionType[0]);
            slotMenu2.setSound(Sound.getSound("on_click_token"));
            slotMenu2.playSound(player);
            getContentMenu().addSlotMenu(slotMenu2);
            items1 = new Items(token.getItemStack().clone());
            slotMenu2 = new SlotMenu(getContentMenu().getResultSlot(), items1, token, event.getCursor(), new ActionType[0]);
            slotMenu2.setSound(Sound.getSound("on_click_token_result"));
            getContentMenu().addSlotMenu(slotMenu2);
            setItemInMenu(slotMenu2);
            return;
          } 
          this.itemStack = event.getCursor().clone();
          Items items = new Items(token.getItemStack().clone());
          items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
          SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), items, "", new ActionType[0]);
          slotMenu1.setSound(Sound.getSound("on_click_token"));
          getContentMenu().addSlotMenu(slotMenu1);
          slotMenu1.playSound(player);
          items = new Items(Cosmetic.getCloneCosmetic(token.getCosmetic()).getItemStack());
          slotMenu1 = new SlotMenu(getContentMenu().getResultSlot(), items, token, null, new ActionType[0]);
          slotMenu1.setSound(Sound.getSound("on_click_token_result"));
          getContentMenu().addSlotMenu(slotMenu1);
          setItemInMenu(slotMenu1);
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
      if (getContentMenu().getResultSlot() == i) {
        if (event.getCurrentItem() == null) {
          event.setCancelled(true);
          return;
        } 
        event.setCancelled(true);
        SlotMenu slotMenu1 = getContentMenu().getSlotMenuBySlot(i);
        if (slotMenu1 == null)
          return; 
        Token token = slotMenu1.getToken();
        if (slotMenu1.getOldToken() != null) {
          this.itemStack = null;
          slotMenu1.playSound(player);
          slotMenu1.action(player, ActionType.UPDATE_OLD_TOKEN);
          getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
          getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
          event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
          event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
          return;
        } 
        boolean redeem = Token.removeToken(player, this.itemStack);
        if (!redeem)
          return; 
        if (this.itemStack.getAmount() > token.getItemStack().getAmount()) {
          ItemStack newItem = token.getItemStack().clone();
          newItem.setAmount(this.itemStack.getAmount() - token.getItemStack().getAmount());
          player.getInventory().addItem(new ItemStack[] { newItem });
        } 
        this.itemStack = null;
        slotMenu1.playSound(player);
        getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
        getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
        event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
        event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
        MagicCosmetics.getInstance().getCosmeticsManager().changeCosmetic(player, token.getCosmetic(), token.getTokenType());
        return;
      } 
      event.setCancelled(true);
      return;
    } 
    int slot = event.getSlot();
    SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
    if (slotMenu == null)
      return; 
    slotMenu.action(player);
  }
  
  public void setItems() {
    setup();
    for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values())
      setItemInMenu(slotMenu); 
  }
  
  private void setup() {
    if (isDrag())
      return; 
    ItemStack itemToken = this.playerData.getTokenInPlayer();
    if (itemToken == null)
      return; 
    Token token = Token.getTokenByItem(itemToken);
    if (token == null) {
      token = Token.getOldTokenByItem(itemToken);
      if (token == null) {
        getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
        getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
        return;
      } 
      Items items1 = new Items(itemToken);
      items1.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
      SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), items1, "", new ActionType[] { ActionType.CLOSE_MENU });
      slotMenu1.setSound(Sound.getSound("on_click_token"));
      getContentMenu().addSlotMenu(slotMenu1);
      items1 = new Items(token.getItemStack());
      slotMenu1 = new SlotMenu(getContentMenu().getResultSlot(), items1, token, itemToken, new ActionType[] { ActionType.UPDATE_OLD_TOKEN });
      slotMenu1.setSound(Sound.getSound("on_click_token_result"));
      getContentMenu().addSlotMenu(slotMenu1);
      return;
    } 
    Items items = new Items(token.getItemStack());
    items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
    SlotMenu slotMenu = new SlotMenu(getContentMenu().getPreviewSlot(), items, "", new ActionType[] { ActionType.CLOSE_MENU });
    slotMenu.setSound(Sound.getSound("on_click_token"));
    getContentMenu().addSlotMenu(slotMenu);
    items = new Items(Cosmetic.getCloneCosmetic(token.getCosmetic()).getItemStack());
    slotMenu = new SlotMenu(getContentMenu().getResultSlot(), items, token, null, new ActionType[] { ActionType.REMOVE_TOKEN_ADD_COSMETIC });
    slotMenu.setSound(Sound.getSound("on_click_token_result"));
    getContentMenu().addSlotMenu(slotMenu);
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
  
  public void setDrag(boolean drag) {
    this.drag = drag;
  }
  
  public boolean isDrag() {
    return this.drag;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\TokenMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */