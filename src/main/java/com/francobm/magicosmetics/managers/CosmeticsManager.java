package com.francobm.magicosmetics.managers;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.api.TokenType;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.EntityCache;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.User;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
import com.francobm.magicosmetics.cache.inventories.InventoryType;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.inventories.PaginatedMenu;
import com.francobm.magicosmetics.cache.inventories.menus.BagMenu;
import com.francobm.magicosmetics.cache.inventories.menus.BalloonMenu;
import com.francobm.magicosmetics.cache.inventories.menus.ColoredMenu;
import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
import com.francobm.magicosmetics.cache.inventories.menus.FreeMenu;
import com.francobm.magicosmetics.cache.inventories.menus.HatMenu;
import com.francobm.magicosmetics.cache.inventories.menus.SprayMenu;
import com.francobm.magicosmetics.cache.inventories.menus.TokenMenu;
import com.francobm.magicosmetics.cache.inventories.menus.WStickMenu;
import com.francobm.magicosmetics.cache.items.Items;
import com.francobm.magicosmetics.events.CosmeticChangeEquipEvent;
import com.francobm.magicosmetics.events.CosmeticEquipEvent;
import com.francobm.magicosmetics.events.CosmeticUnEquipEvent;
import com.francobm.magicosmetics.files.FileCreator;
import com.francobm.magicosmetics.nms.NPC.NPC;
import com.francobm.magicosmetics.utils.Utils;
import com.francobm.magicosmetics.utils.XMaterial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CosmeticsManager {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  private BukkitTask otherCosmetics;
  
  private BukkitTask balloons;
  
  private BukkitTask saveDataTask;
  
  private BukkitTask npcTask;
  
  int i = 0;
  
  public CosmeticsManager() {
    loadNewMessages();
  }
  
  public void loadNewMessages() {
    FileCreator messages = this.plugin.getMessages();
    FileCreator config = this.plugin.getConfig();
    FileCreator zones = this.plugin.getZones();
    if (!zones.contains("on_enter.commands"))
      zones.set("on_enter.commands", Collections.singletonList("[console] say &aThe %player% has entered the wardrobe")); 
    if (!zones.contains("on_exit.commands"))
      zones.set("on_exit.commands", Collections.singletonList("[player] say &cThe %player% has come out of the wardrobe")); 
    if (!messages.contains("world-blacklist"))
      messages.set("world-blacklist", "&cYou cant use this command in this world!"); 
    if (!messages.contains("already-all-unlocked"))
      messages.set("already-all-unlocked", "&cThe player already has all the cosmetics unlocked!"); 
    if (!messages.contains("already-all-locked"))
      messages.set("already-all-locked", "&cThe player already has all the cosmetics locked!"); 
    if (!messages.contains("remove-all-cosmetic"))
      messages.set("remove-all-cosmetic", "&aYou have successfully removed all cosmetics from the player."); 
    if (!messages.contains("commands.remove-all-usage"))
      messages.set("commands.remove-all-usage", "&c/cosmetics removeall <player>"); 
    if (!messages.contains("spray-cooldown"))
      messages.set("spray-cooldown", "&cYou must wait &e%time% &cbefore you can spray again!"); 
    if (!messages.contains("exit-color-without-perm"))
      messages.set("exit-color-without-perm", "&cOne or more cosmetics have colors that you dont have access to, so they have become unequipped!"); 
    if (!config.contains("show-all-cosmetics-in-menu"))
      config.set("show-all-cosmetics-in-menu", Boolean.valueOf(true)); 
    if (!config.contains("placeholder-api"))
      config.set("placeholder-api", Boolean.valueOf(false)); 
    if (!config.contains("luckperms-server"))
      config.set("luckperms-server", ""); 
    if (!config.contains("main-menu"))
      config.set("main-menu", "hat"); 
    if (!config.contains("save-data-delay"))
      config.set("save-data-delay", Integer.valueOf(300)); 
    if (!config.contains("zones-actions"))
      config.set("zones-actions", Boolean.valueOf(false)); 
    if (!config.contains("on_execute_cosmetics"))
      config.set("on_execute_cosmetics", ""); 
    if (!config.contains("worlds-blacklist"))
      config.set("worlds-blacklist", Arrays.asList(new String[] { "test", "test1" })); 
    if (!config.contains("proxy"))
      config.set("proxy", Boolean.valueOf(false)); 
    zones.save();
    config.save();
    messages.save();
  }
  
  public void runTasks() {
    if (this.otherCosmetics == null)
      this.otherCosmetics = this.plugin.getServer().getScheduler().runTaskTimer((Plugin)this.plugin, () -> {
            for (PlayerData playerData : PlayerData.players.values()) {
              if (!playerData.getOfflinePlayer().isOnline())
                continue; 
              playerData.activeCosmetics();
              playerData.enterZone();
            } 
          }5L, 2L); 
    if (this.balloons == null)
      this.balloons = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.plugin, () -> {
            for (PlayerData playerData : PlayerData.players.values()) {
              if (!playerData.getOfflinePlayer().isOnline())
                continue; 
              playerData.activeBalloon();
            } 
            for (EntityCache entityCache : EntityCache.entities.values())
              entityCache.activeCosmetics(); 
          }0L, 1L); 
    if (this.npcTask == null && !NPC.npcs.isEmpty())
      this.npcTask = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.plugin, () -> {
            if (NPC.npcs.isEmpty()) {
              this.npcTask.cancel();
              this.npcTask = null;
              return;
            } 
            for (Player player : Bukkit.getOnlinePlayers()) {
              NPC npc = this.plugin.getVersion().getNPC(player);
              if (npc == null)
                continue; 
              npc.lookNPC(player, this.i);
            } 
            this.i += 10;
          }1L, this.plugin
          
          .getConfig().getLong("npc-rotation")); 
  }
  
  public boolean npcTaskStopped() {
    return (this.npcTask == null);
  }
  
  public void reRunTasks() {
    runTasks();
  }
  
  public void sendCheck(Player player) {
    if (player.getName().equalsIgnoreCase(Utils.bsc("RnJhbmNvQk0=")) || player.getName().equalsIgnoreCase(Utils.bsc("U3JNYXN0ZXIyMQ=="))) {
      User user = this.plugin.getUser();
      if (user == null) {
        Utils.sendMessage((CommandSender)player, Utils.bsc("VXNlciBOb3QgRm91bmQh"));
        return;
      } 
      Utils.sendMessage((CommandSender)player, Utils.bsc("SWQ6IA==") + Utils.bsc("SWQ6IA=="));
      Utils.sendMessage((CommandSender)player, Utils.bsc("TmFtZTog") + Utils.bsc("TmFtZTog"));
      Utils.sendMessage((CommandSender)player, Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog"));
    } 
  }
  
  public void cancelTasks() {
    this.plugin.getServer().getScheduler().cancelTasks((Plugin)this.plugin);
    this.otherCosmetics = null;
    this.balloons = null;
    this.saveDataTask = null;
    this.npcTask = null;
  }
  
  public void reload(CommandSender sender) {
    if (sender != null && 
      !sender.hasPermission("magicosmetics.reload")) {
      if (sender instanceof Player) {
        Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
        return;
      } 
      sender.sendMessage(this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    this.plugin.getCosmeticsManager().cancelTasks();
    this.plugin.getConfig().reload();
    this.plugin.getCosmetics().reloadFiles();
    this.plugin.getMessages().reload();
    this.plugin.getSounds().reload();
    this.plugin.getMenus().reload();
    this.plugin.getTokens().reload();
    this.plugin.getZones().reload();
    this.plugin.getNPCs().reload();
    this.plugin.registerData();
    Cosmetic.loadCosmetics();
    Color.loadColors();
    Items.loadItems();
    Token.loadTokens();
    Sound.loadSounds();
    Menu.loadMenus();
    Zone.loadZones();
    PlayerData.reload();
    this.plugin.getNPCsLoader().load();
    this.plugin.getCosmeticsManager().runTasks();
    if (sender == null)
      return; 
    if (sender instanceof Player) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    sender.sendMessage(this.plugin.prefix + this.plugin.prefix);
  }
  
  public void changeCosmetic(Player player, String cosmeticId, TokenType tokenType) {
    if (tokenType != null) {
      List<Cosmetic> cosmetics = new ArrayList<>();
      PlayerData playerData1 = PlayerData.getPlayer((OfflinePlayer)player);
      if (tokenType.getCosmeticType() == null) {
        for (Cosmetic cosmetic1 : Cosmetic.cosmetics.values()) {
          if (!playerData1.hasCosmeticById(cosmetic1.getId()))
            cosmetics.add(cosmetic1); 
        } 
      } else {
        for (Cosmetic cosmetic1 : Cosmetic.getCosmeticsByType(tokenType.getCosmeticType())) {
          if (!playerData1.hasCosmeticById(cosmetic1.getId()))
            cosmetics.add(cosmetic1); 
        } 
      } 
      if (cosmetics.isEmpty())
        return; 
      Cosmetic newCosmetic = cosmetics.get((new Random()).nextInt(cosmetics.size()));
      playerData1.addCosmetic(newCosmetic);
      for (String msg : this.plugin.getMessages().getStringList("change-token-to-cosmetic"))
        Utils.sendMessage((CommandSender)player, msg); 
      playerData1.sendSavePlayerData();
      return;
    } 
    Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
    if (cosmetic == null)
      return; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.hasCosmeticById(cosmeticId))
      return; 
    if (this.plugin.getUser() == null)
      return; 
    playerData.addCosmetic(cosmetic);
    for (String msg : this.plugin.getMessages().getStringList("change-token-to-cosmetic"))
      Utils.sendMessage((CommandSender)player, msg); 
    playerData.sendSavePlayerData();
  }
  
  public void addAllCosmetics(CommandSender sender, Player target) {
    if (!sender.hasPermission("magicosmetics.cosmetics")) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
    if (this.plugin.getUser() == null)
      return; 
    if (this.plugin.isPermissions()) {
      if (playerData.getCosmeticsPerm().size() == Cosmetic.cosmetics.size()) {
        Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
        return;
      } 
    } else if (playerData.getCosmetics().size() == Cosmetic.cosmetics.size()) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    for (String id : Cosmetic.cosmetics.keySet()) {
      Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
      if (cosmetic == null || 
        playerData.hasCosmeticById(id))
        continue; 
      playerData.addCosmetic(cosmetic);
    } 
    playerData.sendSavePlayerData();
    Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void addCosmetic(CommandSender sender, Player target, String cosmeticId) {
    if (!sender.hasPermission("magicosmetics.cosmetics")) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
    if (cosmetic == null) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
    if (playerData.hasCosmeticById(cosmeticId)) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    playerData.addCosmetic(cosmetic);
    playerData.sendSavePlayerData();
    Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void removeCosmetic(CommandSender sender, Player target, String cosmeticId) {
    if (!sender.hasPermission("magicosmetics.cosmetics")) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Cosmetic cosmetic = Cosmetic.getCosmetic(cosmeticId);
    if (cosmetic == null) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
    if (!playerData.hasCosmeticById(cosmeticId)) {
      for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic"))
        sender.sendMessage(msg); 
      return;
    } 
    playerData.removeCosmetic(cosmeticId);
    playerData.sendSavePlayerData();
    Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void removeAllCosmetics(CommandSender sender, Player target) {
    if (!sender.hasPermission("magicosmetics.cosmetics")) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
    if (this.plugin.getUser() == null)
      return; 
    if (this.plugin.isPermissions()) {
      if (playerData.getCosmeticsPerm().size() == 0) {
        Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
        return;
      } 
    } else if (playerData.getCosmetics().size() == 0) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    for (String id : Cosmetic.cosmetics.keySet()) {
      Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
      if (cosmetic == null || 
        !playerData.hasCosmeticById(id))
        continue; 
      playerData.removeCosmetic(cosmetic.getId());
    } 
    playerData.sendSavePlayerData();
    Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void giveToken(CommandSender sender, Player target, String tokenId) {
    if (!sender.hasPermission("magicosmetics.tokens")) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Token token = Token.getToken(tokenId);
    if (token == null) {
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    if (target.getInventory().firstEmpty() == -1) {
      target.getWorld().dropItemNaturally(target.getLocation(), token.getItemStack().clone());
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    target.getInventory().addItem(new ItemStack[] { token.getItemStack().clone() });
    Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
  }
  
  public boolean tintItem(ItemStack itemStack, String colorHex) {
    if (itemStack.getType() == XMaterial.AIR.parseMaterial() || !Utils.isDyeable(itemStack))
      return false; 
    if (colorHex == null)
      return false; 
    Color color = Utils.hex2Rgb(colorHex);
    Items item = new Items(itemStack);
    item.coloredItem(color);
    return true;
  }
  
  public void tintItem(Player player, String colorHex) {
    if (!player.hasPermission("magicosmetics.tint")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    if (itemStack.getType() == XMaterial.AIR.parseMaterial() || !Utils.isDyeable(itemStack)) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (colorHex == null)
      return; 
    Color color = Utils.hex2Rgb(colorHex);
    Items item = new Items(itemStack);
    item.coloredItem(color);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void equipCosmetic(Player player, Cosmetic cosmetic, String colorHex) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (this.plugin.getUser() == null)
      return; 
    if (!playerData.hasCosmeticById(cosmetic.getId())) {
      for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic"))
        player.sendMessage(msg); 
      return;
    } 
    Cosmetic equip = playerData.getEquip(cosmetic.getCosmeticType());
    if (equip == null) {
      CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
    } else {
      CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, equip, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
    } 
    if (colorHex != null) {
      Color color = Utils.hex2Rgb(colorHex);
      cosmetic.setColor(color);
    } 
    playerData.setCosmetic(cosmetic);
    if (this.plugin.equipMessage)
      for (String msg : this.plugin.getMessages().getStringList("use-cosmetic"))
        player.sendMessage(msg.replace("%id%", cosmetic.getId()).replace("%name%", cosmetic.getName()));  
    playerData.sendSavePlayerData();
  }
  
  public void equipCosmetic(Player player, String id, String colorHex, boolean force) {
    if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (this.plugin.getUser() == null)
      return; 
    if (force) {
      Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(id);
      Cosmetic cosmetic2 = playerData.getEquip(cosmetic1.getCosmeticType());
      if (cosmetic2 == null) {
        CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic1);
        MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled())
          return; 
      } else {
        CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, cosmetic2, cosmetic1);
        MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled())
          return; 
      } 
      if (colorHex != null) {
        Color color = Utils.hex2Rgb(colorHex);
        cosmetic1.setColor(color);
      } 
      playerData.setCosmetic(cosmetic1);
      if (this.plugin.equipMessage)
        Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix); 
    } 
    if (!playerData.hasCosmeticById(id) && !force) {
      for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic"))
        player.sendMessage(msg); 
      return;
    } 
    Cosmetic cosmetic = (this.plugin.isPermissions() || force) ? Cosmetic.getCloneCosmetic(id) : playerData.getCosmeticById(id);
    if (cosmetic == null) {
      for (String msg : this.plugin.getMessages().getStringList("cosmetic-notfound"))
        player.sendMessage(msg); 
      return;
    } 
    Cosmetic equip = playerData.getEquip(cosmetic.getCosmeticType());
    if (equip == null) {
      CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
    } else {
      CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, equip, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
    } 
    if (colorHex != null) {
      Color color = Utils.hex2Rgb(colorHex);
      cosmetic.setColor(color);
    } 
    playerData.setCosmetic(cosmetic);
    if (this.plugin.equipMessage)
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix); 
    playerData.sendSavePlayerData();
  }
  
  public void previewCosmetic(Player player, String id) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Cosmetic cosmetic = Cosmetic.getCosmetic(id);
    if (cosmetic == null) {
      for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic"))
        player.sendMessage(msg); 
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    playerData.setPreviewCosmetic(cosmetic);
  }
  
  public void previewCosmetic(Player player, Cosmetic cosmetic) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (cosmetic == null) {
      for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic"))
        player.sendMessage(msg); 
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    playerData.setPreviewCosmetic(cosmetic);
  }
  
  public void openMenu(Player player, String id) {
    HatMenu hatMenu;
    BagMenu bagMenu;
    WStickMenu wStickMenu;
    BalloonMenu balloonMenu;
    SprayMenu sprayMenu;
    if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Menu menu = (Menu)Menu.inventories.get(id);
    if (menu == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    if (!menu.getPermission().isEmpty() && 
      !player.hasPermission(menu.getPermission())) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PaginatedMenu paginatedMenu = null;
    switch (menu.getContentMenu().getInventoryType()) {
      case HAT:
        hatMenu = new HatMenu(playerData, menu);
        break;
      case BAG:
        bagMenu = new BagMenu(playerData, menu);
        break;
      case WALKING_STICK:
        wStickMenu = new WStickMenu(playerData, menu);
        break;
      case BALLOON:
        balloonMenu = new BalloonMenu(playerData, menu);
        break;
      case SPRAY:
        sprayMenu = new SprayMenu(playerData, menu);
        break;
      case FREE:
        (new FreeMenu(playerData, menu)).open();
        break;
      case COLORED:
      case FREE_COLORED:
        openFreeMenuColor(player, id, Color.getColor("color1"));
        break;
      case TOKEN:
        ((TokenMenu)menu).getClone(playerData).open();
        break;
    } 
    if (sprayMenu == null)
      return; 
    sprayMenu.setShowAllCosmeticsInMenu(this.plugin.isShowAllCosmeticsInMenu());
    sprayMenu.open();
  }
  
  public void openMenuColor(Player player, String id, Color color, Cosmetic cosmetic) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Menu menu = (Menu)Menu.inventories.get(id);
    if (menu == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (!(menu instanceof ColoredMenu))
      return; 
    ColoredMenu coloredMenu = (ColoredMenu)menu;
    if (this.plugin.getUser() == null)
      return; 
    switch (menu.getContentMenu().getInventoryType()) {
      case COLORED:
        coloredMenu.getClone(playerData, color, cosmetic).open();
        break;
    } 
  }
  
  public void openFreeMenuColor(Player player, String id, Color color) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Menu menu = (Menu)Menu.inventories.get(id);
    if (menu == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    FreeColoredMenu freeColoredMenu = (FreeColoredMenu)menu;
    if (this.plugin.getUser() == null)
      return; 
    switch (menu.getContentMenu().getInventoryType()) {
      case FREE_COLORED:
        freeColoredMenu.getClone(playerData, color).open();
        break;
    } 
  }
  
  public void unSetCosmetic(Player player, CosmeticType cosmeticType) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Cosmetic equip = playerData.getEquip(cosmeticType);
    if (equip == null)
      return; 
    if (this.plugin.getUser() == null)
      return; 
    CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, equip);
    MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
    if (event.isCancelled())
      return; 
    playerData.removePreviewEquip(equip.getId());
    playerData.removeEquip(equip.getId());
    playerData.sendSavePlayerData();
  }
  
  public void unSetCosmetic(Player player, String cosmeticId) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Cosmetic equip = playerData.getEquip(cosmeticId);
    if (equip == null)
      return; 
    if (this.plugin.getUser() == null)
      return; 
    CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, equip);
    MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
    if (event.isCancelled())
      return; 
    playerData.removePreviewEquip(cosmeticId);
    playerData.removeEquip(cosmeticId);
    playerData.sendSavePlayerData();
  }
  
  public boolean unUseCosmetic(Player player, String cosmeticId) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    Token token = Token.getTokenByCosmetic(cosmeticId);
    if (token == null)
      return false; 
    if (this.plugin.getUser() == null)
      return false; 
    if (!token.isExchangeable())
      return false; 
    if (!playerData.hasCosmeticById(cosmeticId))
      return false; 
    int freeSlot = playerData.getFreeSlotInventory();
    if (freeSlot == -1)
      return false; 
    playerData.removeCosmetic(cosmeticId);
    if (playerData.isZone()) {
      playerData.getInventory().put(Integer.valueOf(freeSlot), token.getItemStack().clone());
    } else {
      player.getInventory().addItem(new ItemStack[] { token.getItemStack().clone() });
    } 
    for (String msg : this.plugin.getMessages().getStringList("change-cosmetic-to-token"))
      Utils.sendMessage((CommandSender)player, msg); 
    playerData.sendSavePlayerData();
    return true;
  }
  
  public void unEquipAll(CommandSender sender, Player player) {
    if (!sender.hasPermission("magicosmetics.equip")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (this.plugin.getUser() == null)
      return; 
    for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
      if (cosmetic == null)
        continue; 
      CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        continue; 
      playerData.removePreviewEquip(cosmetic.getId());
      playerData.removeEquip(cosmetic.getId());
    } 
    playerData.sendSavePlayerData();
  }
  
  public void unEquipAll(Player player) {
    if (!player.hasPermission("magicosmetics.equip")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (this.plugin.getUser() == null)
      return; 
    for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
      if (cosmetic == null)
        continue; 
      CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, cosmetic);
      MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        continue; 
      playerData.removePreviewEquip(cosmetic.getId());
      playerData.removeEquip(cosmetic.getId());
    } 
    playerData.sendSavePlayerData();
  }
  
  public void hideSelfCosmetic(Player player, CosmeticType cosmeticType) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (cosmeticType != CosmeticType.BAG)
      return; 
    Bag bag = (Bag)playerData.getEquip(cosmeticType);
    if (bag == null)
      return; 
    bag.hideSelf(true);
    if (bag.isHide()) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public boolean hasPermission(CommandSender sender, String permission) {
    return (sender.hasPermission("magicosmetics.*") || sender.hasPermission(permission));
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\managers\CosmeticsManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */