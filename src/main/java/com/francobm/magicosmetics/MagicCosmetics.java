package com.francobm.magicosmetics;

import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.SprayKeys;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.User;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.cache.ZoneAction;
import com.francobm.magicosmetics.cache.ZoneActions;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.items.Items;
import com.francobm.magicosmetics.commands.Command;
import com.francobm.magicosmetics.database.MySQL;
import com.francobm.magicosmetics.database.SQL;
import com.francobm.magicosmetics.database.SQLite;
import com.francobm.magicosmetics.files.FileCosmetics;
import com.francobm.magicosmetics.files.FileCreator;
import com.francobm.magicosmetics.listeners.CitizensListener;
import com.francobm.magicosmetics.listeners.EntityListener;
import com.francobm.magicosmetics.listeners.InventoryListener;
import com.francobm.magicosmetics.listeners.ItemsAdderListener;
import com.francobm.magicosmetics.listeners.MultiverseCListener;
import com.francobm.magicosmetics.listeners.PlayerListener;
import com.francobm.magicosmetics.listeners.ProxyListener;
import com.francobm.magicosmetics.loaders.NPCsLoader;
import com.francobm.magicosmetics.managers.CosmeticsManager;
import com.francobm.magicosmetics.managers.ZonesManager;
import com.francobm.magicosmetics.nms.v1_16_R3.VersionHandler;
import com.francobm.magicosmetics.nms.v1_17_R1.VersionHandler;
import com.francobm.magicosmetics.nms.v1_18_R1.VersionHandler;
import com.francobm.magicosmetics.nms.v1_18_R2.VersionHandler;
import com.francobm.magicosmetics.nms.v1_19_R1.VersionHandler;
import com.francobm.magicosmetics.nms.v1_19_R2.VersionHandler;
import com.francobm.magicosmetics.nms.v1_19_R3.VersionHandler;
import com.francobm.magicosmetics.nms.v1_20_R1.VersionHandler;
import com.francobm.magicosmetics.nms.v1_20_R2.VersionHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.VersionHandler;
import com.francobm.magicosmetics.nms.v1_20_R4.VersionHandler;
import com.francobm.magicosmetics.nms.v1_21_R1.VersionHandler;
import com.francobm.magicosmetics.nms.version.Version;
import com.francobm.magicosmetics.provider.ItemsAdder;
import com.francobm.magicosmetics.provider.LuckPerms;
import com.francobm.magicosmetics.provider.MagicCrates;
import com.francobm.magicosmetics.provider.MagicGestures;
import com.francobm.magicosmetics.provider.ModelEngine;
import com.francobm.magicosmetics.provider.ModelEngine3;
import com.francobm.magicosmetics.provider.ModelEngine4;
import com.francobm.magicosmetics.provider.NewOraxen;
import com.francobm.magicosmetics.provider.Oraxen;
import com.francobm.magicosmetics.provider.PlaceholderAPI;
import com.francobm.magicosmetics.provider.WorldGuard;
import com.francobm.magicosmetics.provider.citizens.Citizens;
import com.francobm.magicosmetics.provider.husksync.HuskSync;
import com.francobm.magicosmetics.provider.znpcplus.ZNPCsPlus;
import com.francobm.magicosmetics.utils.MathUtils;
import com.francobm.magicosmetics.utils.Utils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public final class MagicCosmetics extends JavaPlugin {
  private static MagicCosmetics instance;
  
  private FileCreator config;
  
  private FileCreator messages;
  
  private FileCosmetics cosmetics;
  
  private FileCreator menus;
  
  private FileCreator zones;
  
  private FileCreator tokens;
  
  private FileCreator sounds;
  
  private FileCreator npcs;
  
  private NPCsLoader NPCsLoader;
  
  private SQL sql;
  
  public String prefix;
  
  private CosmeticsManager cosmeticsManager;
  
  private ZonesManager zonesManager;
  
  private Version version;
  
  public boolean wkasdwk;
  
  private List<BossBar> bossBar;
  
  public ModelEngine modelEngine;
  
  public ItemsAdder itemsAdder;
  
  public Oraxen oraxen;
  
  private User user;
  
  public PlaceholderAPI placeholderAPI;
  
  public GameMode gameMode = null;
  
  public boolean equipMessage;
  
  public Citizens citizens;
  
  private ZNPCsPlus zNPCsPlus;
  
  public String ava = "";
  
  public String unAva = "";
  
  public String equip = "";
  
  public BarColor bossBarColor = BarColor.YELLOW;
  
  public double balloonRotation = 0.0D;
  
  private boolean permissions = false;
  
  private boolean zoneHideItems = true;
  
  private SprayKeys sprayKey;
  
  private int sprayStayTime = 60;
  
  private int sprayCooldown = 5;
  
  public LuckPerms luckPerms;
  
  private boolean placeholders;
  
  private String mainMenu = "hat";
  
  public int saveDataDelay;
  
  private ZoneActions zoneActions;
  
  private String luckPermsServer;
  
  private String onExecuteCosmetics;
  
  private MagicCrates magicCrates;
  
  private MagicGestures magicGestures;
  
  private List<String> worldsBlacklist;
  
  private WorldGuard worldGuard;
  
  private HuskSync huskSync;
  
  private boolean proxy;
  
  private boolean showAllCosmeticsInMenu;
  
  public void onLoad() {
    if (getServer().getPluginManager().getPlugin("WorldGuard") != null)
      this.worldGuard = new WorldGuard(this); 
  }
  
  public void onEnable() {
    instance = this;
    switch (Utils.getVersion()) {
      case "1.16.5":
        this.version = (Version)new VersionHandler();
        break;
      case "1.17":
      case "1.17.1":
        this.version = (Version)new VersionHandler();
        break;
      case "1.18":
      case "1.18.1":
        this.version = (Version)new VersionHandler();
        break;
      case "1.18.2":
        this.version = (Version)new VersionHandler();
        break;
      case "1.19":
      case "1.19.1":
      case "1.19.2":
        this.version = (Version)new VersionHandler();
        break;
      case "1.19.3":
        this.version = (Version)new VersionHandler();
        break;
      case "1.19.4":
        this.version = (Version)new VersionHandler();
        break;
      case "1.20":
      case "1.20.1":
        this.version = (Version)new VersionHandler();
        break;
      case "1.20.2":
        this.version = (Version)new VersionHandler();
        break;
      case "1.20.3":
      case "1.20.4":
        this.version = (Version)new VersionHandler();
        break;
      case "1.20.5":
      case "1.20.6":
        this.version = (Version)new VersionHandler();
        break;
      case "1.21":
      case "1.21.1":
        this.version = (Version)new VersionHandler();
        break;
    } 
    if (this.version == null) {
      getLogger().severe(Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog") + Utils.getVersion());
      getServer().getPluginManager().disablePlugin((Plugin)this);
      return;
    } 
    getLogger().info(Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog") + Utils.getVersion());
    this.bossBar = new ArrayList<>();
    this.config = new FileCreator((Plugin)this, "config");
    this.messages = new FileCreator((Plugin)this, "messages");
    this.cosmetics = new FileCosmetics();
    this.menus = new FileCreator((Plugin)this, "menus");
    this.zones = new FileCreator((Plugin)this, "zones");
    this.tokens = new FileCreator((Plugin)this, "tokens");
    this.sounds = new FileCreator((Plugin)this, "sounds");
    this.npcs = new FileCreator((Plugin)this, "npcs");
    this.NPCsLoader = new NPCsLoader();
    createDefaultSpray();
    if (getCosmetic())
      return; 
    if (getServer().getPluginManager().getPlugin("HuskSync") != null)
      this.huskSync = new HuskSync(); 
    if (getServer().getPluginManager().getPlugin("ItemsAdder") != null && Utils.existPluginClass("dev.lone.itemsadder.api.FontImages.FontImageWrapper"))
      this.itemsAdder = new ItemsAdder(); 
    if (getServer().getPluginManager().getPlugin("Oraxen") != null)
      if (Utils.existPluginClass("io.th0rgal.oraxen.api.OraxenItems")) {
        this.oraxen = (Oraxen)new NewOraxen();
        this.oraxen.register();
      } else {
        getLogger().warning("This version of Oraxen lacks classes needed to use the api.");
      }  
    if (getServer().getPluginManager().isPluginEnabled("ModelEngine")) {
      String version = getServer().getPluginManager().getPlugin("ModelEngine").getDescription().getVersion().split("\\.")[0];
      if (version.equalsIgnoreCase("R3")) {
        this.modelEngine = (ModelEngine)new ModelEngine3();
        getLogger().info("ModelEngine 3.0.0 found, using old model engine");
      } else {
        this.modelEngine = (ModelEngine)new ModelEngine4();
        getLogger().info("ModelEngine 4 found, using new model engine");
      } 
    } 
    if (getServer().getPluginManager().getPlugin("Citizens") != null)
      this.citizens = new Citizens(); 
    if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
      this.placeholderAPI = new PlaceholderAPI(); 
    if (getServer().getPluginManager().isPluginEnabled("LuckPerms"))
      this.luckPerms = new LuckPerms(); 
    if (getServer().getPluginManager().isPluginEnabled("MagicCrates"))
      this.magicCrates = new MagicCrates(); 
    if (getServer().getPluginManager().isPluginEnabled("MagicGestures"))
      this.magicGestures = new MagicGestures(); 
    if (!isItemsAdder()) {
      Cosmetic.loadCosmetics();
      Color.loadColors();
      Items.loadItems();
      Zone.loadZones();
      Token.loadTokens();
      Sound.loadSounds();
      Menu.loadMenus();
    } 
    this.cosmeticsManager = new CosmeticsManager();
    this.zonesManager = new ZonesManager();
    registerData();
    this.cosmeticsManager.runTasks();
    registerCommands();
    registerListeners();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player == null || !player.isOnline())
        continue; 
      this.sql.loadPlayerAsync(player);
    } 
  }
  
  public void registerData() {
    if (this.config.getBoolean("MySQL.enabled")) {
      this.sql = (SQL)new MySQL();
    } else {
      this.sql = (SQL)new SQLite();
    } 
    for (BossBar bar : this.bossBar)
      bar.removeAll(); 
    this.bossBar.clear();
    for (String lines : this.messages.getStringList("bossbar")) {
      if (isItemsAdder())
        lines = this.itemsAdder.replaceFontImages(lines); 
      if (isOraxen())
        lines = this.oraxen.replaceFontImages(lines); 
      BossBar boss = getServer().createBossBar(lines, this.bossBarColor, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
      boss.setVisible(true);
      this.bossBar.add(boss);
    } 
    this.showAllCosmeticsInMenu = this.config.getBoolean("show-all-cosmetics-in-menu", true);
    this.ava = getInstance().getMessages().getString("edge.available");
    this.unAva = getInstance().getMessages().getString("edge.unavailable");
    this.equip = getInstance().getMessages().getString("edge.equip");
    if (isItemsAdder()) {
      this.ava = this.itemsAdder.replaceFontImages(this.ava);
      this.unAva = this.itemsAdder.replaceFontImages(this.unAva);
      this.equip = this.itemsAdder.replaceFontImages(this.equip);
    } 
    if (isOraxen()) {
      this.ava = this.oraxen.replaceFontImages(this.ava);
      this.unAva = this.oraxen.replaceFontImages(this.unAva);
      this.equip = this.oraxen.replaceFontImages(this.equip);
    } 
    this.prefix = this.messages.getString("prefix");
    if (this.config.contains("leave-wardrobe-gamemode"))
      try {
        this.gameMode = GameMode.valueOf(this.config.getString("leave-wardrobe-gamemode").toUpperCase());
      } catch (IllegalArgumentException exception) {
        getLogger().severe("Gamemode in config path: leave-wardrobe-gamemode Not Found!");
      }  
    if (this.config.contains("main-menu"))
      this.mainMenu = this.config.getString("main-menu"); 
    if (this.config.contains("placeholder-api"))
      this.placeholders = this.config.getBoolean("placeholder-api"); 
    if (this.config.contains("permissions"))
      setPermissions(this.config.getBoolean("permissions")); 
    this.equipMessage = false;
    if (this.config.contains("equip-message"))
      this.equipMessage = this.config.getBoolean("equip-message"); 
    if (this.config.contains("zones-hide-items"))
      this.zoneHideItems = this.config.getBoolean("zones-hide-items"); 
    if (this.config.contains("bossbar-color"))
      try {
        this.bossBarColor = BarColor.valueOf(this.config.getString("bossbar-color").toUpperCase());
      } catch (IllegalArgumentException exception) {
        this.bossBarColor = BarColor.YELLOW;
        getLogger().severe("Bossbar color in config path: bossbar-color Not Valid!");
      }  
    if (this.config.contains("proxy"))
      this.proxy = this.config.getBoolean("proxy"); 
    if (this.config.contains("spray-key"))
      try {
        this.sprayKey = SprayKeys.valueOf(this.config.getString("spray-key").toUpperCase());
      } catch (IllegalArgumentException exception) {
        getLogger().severe("Spray key in config path: spray-key Not Valid!");
      }  
    if (this.config.contains("spray-stay-time"))
      this.sprayStayTime = this.config.getInt("spray-stay-time"); 
    if (this.config.contains("spray-cooldown"))
      this.sprayCooldown = this.config.getInt("spray-cooldown"); 
    this.saveDataDelay = 300;
    if (this.config.contains("save-data-delay"))
      this.saveDataDelay = this.config.getInt("save-data-delay"); 
    if (this.config.contains("luckperms-server"))
      this.luckPermsServer = this.config.getString("luckperms-server"); 
    if (this.config.contains("on_execute_cosmetics"))
      this.onExecuteCosmetics = this.config.getString("on_execute_cosmetics"); 
    if (this.config.contains("worlds-blacklist"))
      this.worldsBlacklist = this.config.getStringListWF("worlds-blacklist"); 
    this.balloonRotation = this.config.getDouble("balloons-rotation");
    ZoneAction onEnter = null;
    ZoneAction onExit = null;
    if (this.zoneActions != null) {
      this.zoneActions.getOnEnter().setCommands(this.zones.getStringList("on_enter.commands"));
      this.zoneActions.getOnExit().setCommands(this.zones.getStringList("on_exit.commands"));
      this.zoneActions.setEnabled(this.config.getBoolean("zones-actions"));
      zoneActionsListener();
    } else {
      if (this.zones.contains("on_enter.commands"))
        onEnter = new ZoneAction("onEnter", this.zones.getStringList("on_enter.commands")); 
      if (this.zones.contains("on_exit.commands"))
        onExit = new ZoneAction("onEnter", this.zones.getStringList("on_exit.commands")); 
      this.zoneActions = new ZoneActions(onEnter, onExit);
      this.zoneActions.setEnabled(getConfig().getBoolean("zones-actions"));
    } 
  }
  
  public void registerListeners() {
    getServer().getPluginManager().registerEvents((Listener)new EntityListener(), (Plugin)this);
    getServer().getPluginManager().registerEvents((Listener)new InventoryListener(), (Plugin)this);
    getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
    if (isItemsAdder())
      getServer().getPluginManager().registerEvents((Listener)new ItemsAdderListener(), (Plugin)this); 
    if (isCitizens())
      getServer().getPluginManager().registerEvents((Listener)new CitizensListener(), (Plugin)this); 
    if (isHuskSync())
      getServer().getPluginManager().registerEvents((Listener)this.huskSync, (Plugin)this); 
    if (this.worldGuard != null)
      getServer().getPluginManager().registerEvents((Listener)this.worldGuard, (Plugin)this); 
    if (getServer().getPluginManager().isPluginEnabled("Multiverse-Core"))
      getServer().getPluginManager().registerEvents((Listener)new MultiverseCListener(), (Plugin)this); 
    zoneActionsListener();
    if (isProxy()) {
      getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
      getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
    } 
  }
  
  private void checkIfProxy() {
    Path spigotPath = Paths.get("spigot.yml", new String[0]);
    if (Files.exists(spigotPath, new java.nio.file.LinkOption[0]) && YamlConfiguration.loadConfiguration(spigotPath.toFile()).getBoolean("settings.bungeecord")) {
      getLogger().info("Enabling BungeeMode!");
      setProxy(true);
      getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
      getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
      return;
    } 
    Path oldPaperPath = Paths.get("paper.yml", new String[0]);
    if (Utils.isPaper()) {
      if (Files.exists(oldPaperPath, new java.nio.file.LinkOption[0]) && YamlConfiguration.loadConfiguration(oldPaperPath.toFile()).getBoolean("settings.velocity-support.enabled")) {
        getLogger().info("Enabling VelocityMode!");
        setProxy(true);
        getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
        return;
      } 
      YamlConfiguration config = Utils.getPaperConfig(getServer());
      if (config != null && (config.getBoolean("settings.velocity-support.enabled") || config.getBoolean("proxies.velocity.enabled"))) {
        getLogger().info("Enabling VelocityMode!");
        setProxy(true);
        getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
      } 
    } 
  }
  
  public void zoneActionsListener() {
    if (this.zoneActions.isEnabled()) {
      if (HandlerList.getRegisteredListeners((Plugin)this).stream().anyMatch(registeredListener -> registeredListener.getListener().equals(getZoneActions().getZoneListener())))
        return; 
      getServer().getPluginManager().registerEvents((Listener)getZoneActions().getZoneListener(), (Plugin)this);
      return;
    } 
    HandlerList.unregisterAll((Listener)getZoneActions().getZoneListener());
  }
  
  public void registerCommands() {
    getCommand("magicosmetics").setExecutor((CommandExecutor)new Command());
    getCommand("magicosmetics").setTabCompleter((TabCompleter)new Command());
  }
  
  public void onDisable() {
    if (this.proxy) {
      getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this);
      getServer().getMessenger().unregisterOutgoingPluginChannel((Plugin)this);
    } 
    if (this.cosmeticsManager != null)
      this.cosmeticsManager.cancelTasks(); 
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player == null || !player.isOnline())
        continue; 
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
      if (!playerData.isZone())
        continue; 
      playerData.exitZoneSync();
    } 
    this.sql.savePlayers();
    if (this.bossBar != null) {
      for (BossBar bar : this.bossBar)
        bar.removeAll(); 
      this.bossBar.clear();
    } 
    this.NPCsLoader.save();
  }
  
  public boolean isProxy() {
    return this.proxy;
  }
  
  public void setProxy(boolean proxy) {
    this.proxy = proxy;
  }
  
  public static MagicCosmetics getInstance() {
    return instance;
  }
  
  public FileCreator getConfig() {
    return this.config;
  }
  
  public FileCreator getMessages() {
    return this.messages;
  }
  
  public FileCosmetics getCosmetics() {
    return this.cosmetics;
  }
  
  public FileCreator getMenus() {
    return this.menus;
  }
  
  public FileCreator getZones() {
    return this.zones;
  }
  
  public FileCreator getTokens() {
    return this.tokens;
  }
  
  public SQL getSql() {
    return this.sql;
  }
  
  public CosmeticsManager getCosmeticsManager() {
    return this.cosmeticsManager;
  }
  
  public ZonesManager getZonesManager() {
    return this.zonesManager;
  }
  
  public Version getVersion() {
    return this.version;
  }
  
  public boolean getCosmetic() {
    MathUtils.floor(1.0F, 2.0F);
    User user = getUser();
    if (user == null) {
      getLogger().warning("Your user does not exist, how strange isn't it...?");
      return true;
    } 
    getLogger().info(" ");
    getLogger().info("Welcome " + user.getName() + "!");
    getLogger().info("Thank you for using MagicCosmetics =)!");
    getLogger().info(" ");
    return false;
  }
  
  public FileCreator getSounds() {
    return this.sounds;
  }
  
  public List<BossBar> getBossBar() {
    return this.bossBar;
  }
  
  public ModelEngine getModelEngine() {
    return this.modelEngine;
  }
  
  public boolean isModelEngine() {
    return (this.modelEngine != null);
  }
  
  public ItemsAdder getItemsAdder() {
    return this.itemsAdder;
  }
  
  public boolean isItemsAdder() {
    return (this.itemsAdder != null);
  }
  
  public Oraxen getOraxen() {
    return this.oraxen;
  }
  
  public boolean isOraxen() {
    return (this.oraxen != null);
  }
  
  public PlaceholderAPI getPlaceholderAPI() {
    return this.placeholderAPI;
  }
  
  public boolean isPlaceholderAPI() {
    return (this.placeholderAPI != null);
  }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public boolean isCitizens() {
    return (this.citizens != null);
  }
  
  public Citizens getCitizens() {
    return this.citizens;
  }
  
  public ZNPCsPlus getzNPCsPlus() {
    return this.zNPCsPlus;
  }
  
  public boolean isPermissions() {
    return this.permissions;
  }
  
  public void createDefaultSpray() {
    File file = new File(getDataFolder(), "sprays");
    if (file.exists())
      return; 
    new FileCreator((Plugin)this, "sprays/first", ".png", getDataFolder());
  }
  
  public void setPermissions(boolean permissions) {
    this.permissions = permissions;
  }
  
  public SprayKeys getSprayKey() {
    return this.sprayKey;
  }
  
  public void setSprayKey(SprayKeys sprayKey) {
    this.sprayKey = sprayKey;
  }
  
  public int getSprayStayTime() {
    return this.sprayStayTime;
  }
  
  public void setSprayStayTime(int sprayStayTime) {
    this.sprayStayTime = sprayStayTime;
  }
  
  public int getSprayCooldown() {
    return this.sprayCooldown;
  }
  
  public void setSprayCooldown(int sprayCooldown) {
    this.sprayCooldown = sprayCooldown;
  }
  
  public boolean isZoneHideItems() {
    return this.zoneHideItems;
  }
  
  public void setZoneHideItems(boolean zoneHideItems) {
    this.zoneHideItems = zoneHideItems;
  }
  
  public LuckPerms getLuckPerms() {
    return this.luckPerms;
  }
  
  public boolean isLuckPerms() {
    return (this.luckPerms != null);
  }
  
  public void setPlaceholders(boolean placeholders) {
    this.placeholders = placeholders;
  }
  
  public boolean isPlaceholders() {
    return this.placeholders;
  }
  
  public String getMainMenu() {
    return this.mainMenu;
  }
  
  public void setMainMenu(String mainMenu) {
    this.mainMenu = mainMenu;
  }
  
  public ZoneActions getZoneActions() {
    return this.zoneActions;
  }
  
  public void setZoneActions(ZoneActions zoneActions) {
    this.zoneActions = zoneActions;
  }
  
  public String getLuckPermsServer() {
    return this.luckPermsServer;
  }
  
  public void setLuckPermsServer(String luckPermsServer) {
    this.luckPermsServer = luckPermsServer;
  }
  
  public String getOnExecuteCosmetics() {
    return this.onExecuteCosmetics;
  }
  
  public void setOnExecuteCosmetics(String onExecuteCosmetics) {
    this.onExecuteCosmetics = onExecuteCosmetics;
  }
  
  public FileCreator getNPCs() {
    return this.npcs;
  }
  
  public NPCsLoader getNPCsLoader() {
    return this.NPCsLoader;
  }
  
  public MagicCrates getMagicCrates() {
    return this.magicCrates;
  }
  
  public MagicGestures getMagicGestures() {
    return this.magicGestures;
  }
  
  public List<String> getWorldsBlacklist() {
    return this.worldsBlacklist;
  }
  
  public HuskSync getHuskSync() {
    return this.huskSync;
  }
  
  public boolean isHuskSync() {
    return (this.huskSync != null);
  }
  
  public boolean isShowAllCosmeticsInMenu() {
    return this.showAllCosmeticsInMenu;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\MagicCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */