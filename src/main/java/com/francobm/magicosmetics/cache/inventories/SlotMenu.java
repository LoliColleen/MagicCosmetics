package com.francobm.magicosmetics.cache.inventories;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.SecondaryColor;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
import com.francobm.magicosmetics.cache.items.Items;
import com.francobm.magicosmetics.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SlotMenu {
  private final int slot;
  
  private Items items;
  
  private final List<ActionType> actionType;
  
  private final List<String> commands;
  
  private final String menu;
  
  private final SlotMenu slotMenu;
  
  private final Cosmetic cosmetic;
  
  private final Token token;
  
  private Sound sound;
  
  private String permission;
  
  private boolean exchangeable;
  
  private Cosmetic tempCosmetic;
  
  private ItemStack oldToken;
  
  public SlotMenu(int slot, Items items, List<String> commands, String menu, SlotMenu slotMenu, Cosmetic cosmetic, Token token, Sound sound, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = commands;
    this.menu = menu;
    this.slotMenu = slotMenu;
    this.cosmetic = cosmetic;
    this.token = token;
    this.sound = sound;
  }
  
  public SlotMenu(int slot, Items items, List<String> commands, String menu, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = commands;
    this.menu = menu;
    this.slotMenu = null;
    this.cosmetic = null;
    this.token = null;
    this.sound = null;
    this.permission = "";
  }
  
  public SlotMenu(int slot, Items items, List<String> commands, String menu, Sound sound, String permission, List<ActionType> actionTypes) {
    this.slot = slot;
    this.items = items;
    this.actionType = actionTypes;
    this.commands = commands;
    this.menu = menu;
    this.slotMenu = null;
    this.cosmetic = null;
    this.token = null;
    this.sound = sound;
    this.permission = permission;
  }
  
  public SlotMenu(int slot, Items items, List<String> commands, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = commands;
    this.menu = "";
    this.slotMenu = null;
    this.cosmetic = null;
    this.token = null;
    this.sound = null;
    this.permission = "";
  }
  
  public SlotMenu(int slot, Items items, String menu, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = new ArrayList<>();
    this.menu = menu;
    this.slotMenu = null;
    this.cosmetic = null;
    this.token = null;
    this.sound = null;
    this.permission = "";
  }
  
  public SlotMenu(int slot, Items items, SlotMenu slotMenu, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = new ArrayList<>();
    this.menu = "";
    this.slotMenu = slotMenu;
    this.cosmetic = null;
    this.token = null;
    this.sound = null;
    this.permission = "";
  }
  
  public SlotMenu(int slot, Items items, Cosmetic cosmetic, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = new ArrayList<>();
    this.menu = "";
    this.slotMenu = null;
    this.cosmetic = cosmetic;
    this.token = null;
    this.sound = null;
    this.permission = "";
  }
  
  public SlotMenu(int slot, Items items, Token token, ItemStack oldToken, ActionType... actionType) {
    this.slot = slot;
    this.items = items;
    this.actionType = Arrays.asList(actionType);
    this.commands = new ArrayList<>();
    this.menu = "";
    this.slotMenu = null;
    this.cosmetic = null;
    this.oldToken = oldToken;
    this.token = token;
    this.sound = null;
    this.permission = "";
  }
  
  public int getSlot() {
    return this.slot;
  }
  
  public Items getItems() {
    return this.items;
  }
  
  public List<ActionType> getActionType() {
    return this.actionType;
  }
  
  public List<String> getCommands() {
    return this.commands;
  }
  
  public void action(Player player) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (!this.permission.isEmpty() && 
      !player.hasPermission(this.permission)) {
      player.sendMessage(plugin.prefix + plugin.prefix);
      return;
    } 
    playSound(player);
    for (ActionType actionType : this.actionType) {
      switch (actionType) {
        case OPEN_MENU:
          openMenu(player);
        case CLOSE_MENU:
          closeMenu(player);
        case PLAYER_COMMAND:
        case CONSOLE_COMMAND:
        case COMMAND:
          runCommands(player);
        case ADD_ITEM_MENU:
          addItemMenu(player);
        case PREVIEW_ITEM:
          previewItem(player);
        case REMOVE_TOKEN_ADD_COSMETIC:
          removeTokenAddCosmetic(player);
        case UPDATE_OLD_TOKEN:
          updateOldToken(player);
      } 
    } 
  }
  
  public boolean action(Player player, ActionType actionType) {
    boolean allow;
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (!this.permission.isEmpty() && 
      !player.hasPermission(this.permission)) {
      player.sendMessage(plugin.prefix + plugin.prefix);
      return false;
    } 
    switch (actionType) {
      case OPEN_MENU:
        openMenu(player);
        break;
      case CLOSE_MENU:
        closeMenu(player);
        break;
      case REFRESH:
        refreshMenu(player);
        break;
      case PLAYER_COMMAND:
      case CONSOLE_COMMAND:
      case COMMAND:
        runCommands(player);
        break;
      case ADD_ITEM_MENU:
        addItemMenu(player);
        break;
      case PREVIEW_ITEM:
        previewItem(player);
        break;
      case REMOVE_TOKEN_ADD_COSMETIC:
        removeTokenAddCosmetic(player);
        break;
      case REMOVE_COSMETIC_ADD_TOKEN:
        allow = removeCosmeticAddToken(player);
        if (allow)
          playSound(player); 
        return allow;
      case UPDATE_OLD_TOKEN:
        updateOldToken(player);
        break;
    } 
    playSound(player);
    return true;
  }
  
  private void runCommands(Player player) {
    for (String command : this.commands) {
      command = command.replace("%player%", player.getName());
      for (ActionType actionType : this.actionType) {
        switch (actionType) {
          case CONSOLE_COMMAND:
          case COMMAND:
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
            if (command.startsWith("magiccos unset "))
              refreshMenu(player); 
          case PLAYER_COMMAND:
            player.performCommand(command);
            if (command.startsWith("magiccos unset "))
              refreshMenu(player); 
        } 
      } 
    } 
  }
  
  private void removeTokenAddCosmetic(Player player) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.removeTokenInPlayer())
      MagicCosmetics.getInstance().getCosmeticsManager().changeCosmetic(player, this.token.getCosmetic(), this.token.getTokenType()); 
    closeMenu(player);
  }
  
  private void updateOldToken(Player player) {
    for (int i = 0; i < this.oldToken.getAmount(); i++) {
      player.getInventory().addItem(new ItemStack[] { this.token.getItemStack() });
    } 
    player.getInventory().removeItem(new ItemStack[] { this.oldToken });
    closeMenu(player);
  }
  
  private boolean removeCosmeticAddToken(Player player) {
    if (this.tempCosmetic == null)
      return false; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (!playerData.hasCosmeticById(this.tempCosmetic.getId()))
      return false; 
    return MagicCosmetics.getInstance().getCosmeticsManager().unUseCosmetic(player, this.tempCosmetic.getId());
  }
  
  private void previewItem(Player player) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (plugin.isPermissions()) {
      if (!this.cosmetic.hasPermission(player)) {
        if (playerData.isZone())
          MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic); 
        closeMenu(player);
        return;
      } 
      if (playerData.isZone())
        MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic); 
      if (!this.cosmetic.isColorBlocked())
        MagicCosmetics.getInstance().getCosmeticsManager().equipCosmetic(player, this.cosmetic, null); 
      closeMenu(player);
      return;
    } 
    if (playerData.getCosmeticById(this.cosmetic.getId()) == null) {
      if (playerData.isZone())
        MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic); 
      closeMenu(player);
      return;
    } 
    playerData.removeCosmetic(this.cosmetic.getId());
    playerData.addCosmetic(this.cosmetic);
    if (!this.cosmetic.isColorBlocked())
      MagicCosmetics.getInstance().getCosmeticsManager().equipCosmetic(player, this.cosmetic.getId(), null, false); 
    if (playerData.isZone())
      MagicCosmetics.getInstance().getCosmeticsManager().previewCosmetic(player, this.cosmetic); 
    closeMenu(player);
  }
  
  private void addItemMenu(Player player) {
    InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
    if (holder instanceof Menu) {
      Menu menu = (Menu)holder;
      menu.getContentMenu().addSlotMenu(this.slotMenu);
      menu.setItemInMenu(this.slotMenu);
    } 
  }
  
  public void playSound(Player player) {
    if (this.sound == null)
      return; 
    Utils.sendSound(player, this.sound);
  }
  
  private void refreshMenu(Player player) {
    if (player.getOpenInventory().getTopInventory().getHolder() instanceof Menu) {
      Menu menu = (Menu)player.getOpenInventory().getTopInventory().getHolder();
      menu.setItems();
    } 
  }
  
  private void openMenu(Player player) {
    String[] split = this.menu.split("\\|");
    if (split.length == 3) {
      Color color = Color.getColor(split[1]);
      Cosmetic cosmetic = Cosmetic.getCloneCosmetic(split[2]);
      if (color == null) {
        MagicCosmetics.getInstance().getLogger().info("Color Null");
        return;
      } 
      if (cosmetic == null) {
        MagicCosmetics.getInstance().getLogger().info("Cosmetic Null");
        return;
      } 
      MagicCosmetics.getInstance().getCosmeticsManager().openMenuColor(player, split[0], color, cosmetic);
      return;
    } 
    if (split.length == 2) {
      if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof FreeColoredMenu))
        return; 
      FreeColoredMenu menu = (FreeColoredMenu)player.getOpenInventory().getTopInventory().getHolder();
      Color color = Color.getColor(split[1]);
      if (color == null) {
        MagicCosmetics.getInstance().getLogger().info("Color Null");
        return;
      } 
      menu.setPage(0);
      menu.setColor(color);
      menu.setSecondaryColor((SecondaryColor)null);
      menu.setItems();
      return;
    } 
    MagicCosmetics.getInstance().getCosmeticsManager().openMenu(player, this.menu);
  }
  
  public void setSound(Sound sound) {
    this.sound = sound;
  }
  
  public Sound getSound() {
    return this.sound;
  }
  
  private void closeMenu(Player player) {
    player.closeInventory();
  }
  
  public String getMenu() {
    return this.menu;
  }
  
  public SlotMenu getSlotMenu() {
    return this.slotMenu;
  }
  
  public String getPermission() {
    return this.permission;
  }
  
  public void setPermission(String permission) {
    this.permission = permission;
  }
  
  public void setItems(Items items) {
    this.items = items;
  }
  
  public Token getToken() {
    return this.token;
  }
  
  public ItemStack getOldToken() {
    return this.oldToken;
  }
  
  public void setExchangeable(boolean exchangeable) {
    this.exchangeable = exchangeable;
  }
  
  public boolean isExchangeable() {
    return this.exchangeable;
  }
  
  public void setTempCosmetic(Cosmetic tempCosmetic) {
    this.tempCosmetic = tempCosmetic;
  }
  
  public Cosmetic getTempCosmetic() {
    return this.tempCosmetic;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\SlotMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */