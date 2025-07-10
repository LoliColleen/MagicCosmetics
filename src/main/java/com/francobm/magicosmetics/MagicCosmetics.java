/*     */ package com.francobm.magicosmetics;
/*     */ import com.francobm.magicosmetics.api.SprayKeys;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.User;
/*     */ import com.francobm.magicosmetics.cache.Zone;
/*     */ import com.francobm.magicosmetics.cache.ZoneAction;
/*     */ import com.francobm.magicosmetics.cache.ZoneActions;
/*     */ import com.francobm.magicosmetics.commands.Command;
/*     */ import com.francobm.magicosmetics.database.MySQL;
/*     */ import com.francobm.magicosmetics.database.SQL;
/*     */ import com.francobm.magicosmetics.files.FileCosmetics;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.listeners.InventoryListener;
/*     */ import com.francobm.magicosmetics.listeners.MultiverseCListener;
/*     */ import com.francobm.magicosmetics.listeners.ProxyListener;
/*     */ import com.francobm.magicosmetics.loaders.NPCsLoader;
/*     */ import com.francobm.magicosmetics.managers.CosmeticsManager;
/*     */ import com.francobm.magicosmetics.managers.ZonesManager;
/*     */ import com.francobm.magicosmetics.nms.v1_17_R1.VersionHandler;
/*     */ import com.francobm.magicosmetics.nms.v1_18_R1.VersionHandler;
/*     */ import com.francobm.magicosmetics.nms.v1_20_R1.VersionHandler;
/*     */ import com.francobm.magicosmetics.nms.v1_20_R3.VersionHandler;
/*     */ import com.francobm.magicosmetics.nms.v1_21_R1.VersionHandler;
/*     */ import com.francobm.magicosmetics.nms.version.Version;
/*     */ import com.francobm.magicosmetics.provider.ItemsAdder;
/*     */ import com.francobm.magicosmetics.provider.LuckPerms;
/*     */ import com.francobm.magicosmetics.provider.MagicCrates;
/*     */ import com.francobm.magicosmetics.provider.MagicGestures;
/*     */ import com.francobm.magicosmetics.provider.ModelEngine;
/*     */ import com.francobm.magicosmetics.provider.Oraxen;
/*     */ import com.francobm.magicosmetics.provider.PlaceholderAPI;
/*     */ import com.francobm.magicosmetics.provider.WorldGuard;
/*     */ import com.francobm.magicosmetics.provider.citizens.Citizens;
/*     */ import com.francobm.magicosmetics.provider.husksync.HuskSync;
/*     */ import com.francobm.magicosmetics.provider.znpcplus.ZNPCsPlus;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.boss.BarColor;
/*     */ import org.bukkit.boss.BossBar;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.RegisteredListener;
/*     */ import org.bukkit.plugin.messaging.PluginMessageListener;
/*     */ 
/*     */ public final class MagicCosmetics extends JavaPlugin {
/*     */   private static MagicCosmetics instance;
/*     */   private FileCreator config;
/*     */   private FileCreator messages;
/*     */   private FileCosmetics cosmetics;
/*     */   private FileCreator menus;
/*     */   private FileCreator zones;
/*     */   private FileCreator tokens;
/*     */   private FileCreator sounds;
/*     */   private FileCreator npcs;
/*     */   private NPCsLoader NPCsLoader;
/*     */   private SQL sql;
/*  70 */   public GameMode gameMode = null; public String prefix; private CosmeticsManager cosmeticsManager; private ZonesManager zonesManager; private Version version; public boolean wkasdwk; private List<BossBar> bossBar; public ModelEngine modelEngine; public ItemsAdder itemsAdder; public Oraxen oraxen; private User user; public PlaceholderAPI placeholderAPI;
/*     */   public boolean equipMessage;
/*     */   public Citizens citizens;
/*     */   private ZNPCsPlus zNPCsPlus;
/*  74 */   public String ava = "";
/*  75 */   public String unAva = "";
/*  76 */   public String equip = "";
/*  77 */   public BarColor bossBarColor = BarColor.YELLOW;
/*  78 */   public double balloonRotation = 0.0D;
/*     */   private boolean permissions = false;
/*     */   private boolean zoneHideItems = true;
/*     */   private SprayKeys sprayKey;
/*  82 */   private int sprayStayTime = 60;
/*  83 */   private int sprayCooldown = 5;
/*     */   public LuckPerms luckPerms;
/*     */   private boolean placeholders;
/*  86 */   private String mainMenu = "hat";
/*     */   
/*     */   public int saveDataDelay;
/*     */   private ZoneActions zoneActions;
/*     */   private String luckPermsServer;
/*     */   private String onExecuteCosmetics;
/*     */   private MagicCrates magicCrates;
/*     */   private MagicGestures magicGestures;
/*     */   private List<String> worldsBlacklist;
/*     */   private WorldGuard worldGuard;
/*     */   private HuskSync huskSync;
/*     */   private boolean proxy;
/*     */   private boolean showAllCosmeticsInMenu;
/*     */   
/*     */   public void onLoad() {
/* 101 */     if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
/* 102 */       this.worldGuard = new WorldGuard(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 108 */     instance = this;
/* 109 */     switch (Utils.getVersion()) {
/*     */       case "1.16.5":
/* 111 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.17":
/*     */       case "1.17.1":
/* 115 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.18":
/*     */       case "1.18.1":
/* 119 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.18.2":
/* 122 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.19":
/*     */       case "1.19.1":
/*     */       case "1.19.2":
/* 127 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.19.3":
/* 130 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.19.4":
/* 133 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.20":
/*     */       case "1.20.1":
/* 137 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.20.2":
/* 140 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.20.3":
/*     */       case "1.20.4":
/* 144 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.20.5":
/*     */       case "1.20.6":
/* 148 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */       case "1.21":
/*     */       case "1.21.1":
/* 152 */         this.version = (Version)new VersionHandler();
/*     */         break;
/*     */     } 
/*     */     
/* 156 */     if (this.version == null) {
/* 157 */       getLogger().severe(Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog") + Utils.getVersion());
/* 158 */       getServer().getPluginManager().disablePlugin((Plugin)this);
/*     */       return;
/*     */     } 
/* 161 */     getLogger().info(Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog") + Utils.getVersion());
/* 162 */     this.bossBar = new ArrayList<>();
/* 163 */     this.config = new FileCreator((Plugin)this, "config");
/* 164 */     this.messages = new FileCreator((Plugin)this, "messages");
/* 165 */     this.cosmetics = new FileCosmetics();
/* 166 */     this.menus = new FileCreator((Plugin)this, "menus");
/* 167 */     this.zones = new FileCreator((Plugin)this, "zones");
/* 168 */     this.tokens = new FileCreator((Plugin)this, "tokens");
/* 169 */     this.sounds = new FileCreator((Plugin)this, "sounds");
/* 170 */     this.npcs = new FileCreator((Plugin)this, "npcs");
/* 171 */     this.NPCsLoader = new NPCsLoader();
/* 172 */     createDefaultSpray();
/* 173 */     if (getCosmetic())
/*     */       return; 
/* 175 */     if (getServer().getPluginManager().getPlugin("HuskSync") != null) {
/* 176 */       this.huskSync = new HuskSync();
/*     */     }
/*     */     
/* 179 */     if (getServer().getPluginManager().getPlugin("ItemsAdder") != null && Utils.existPluginClass("dev.lone.itemsadder.api.FontImages.FontImageWrapper")) {
/* 180 */       this.itemsAdder = new ItemsAdder();
/*     */     }
/*     */     
/* 183 */     if (getServer().getPluginManager().getPlugin("Oraxen") != null) {
/* 184 */       if (Utils.existPluginClass("io.th0rgal.oraxen.api.OraxenItems")) {
/* 185 */         this.oraxen = (Oraxen)new NewOraxen();
/* 186 */         this.oraxen.register();
/*     */       } else {
/* 188 */         getLogger().warning("This version of Oraxen lacks classes needed to use the api.");
/*     */       } 
/*     */     }
/*     */     
/* 192 */     if (getServer().getPluginManager().isPluginEnabled("ModelEngine")) {
/* 193 */       String version = getServer().getPluginManager().getPlugin("ModelEngine").getDescription().getVersion().split("\\.")[0];
/* 194 */       if (version.equalsIgnoreCase("R3")) {
/* 195 */         this.modelEngine = (ModelEngine)new ModelEngine3();
/* 196 */         getLogger().info("ModelEngine 3.0.0 found, using old model engine");
/*     */       } else {
/* 198 */         this.modelEngine = (ModelEngine)new ModelEngine4();
/* 199 */         getLogger().info("ModelEngine 4 found, using new model engine");
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     if (getServer().getPluginManager().getPlugin("Citizens") != null) {
/* 204 */       this.citizens = new Citizens();
/*     */     }
/*     */     
/* 207 */     if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
/* 208 */       this.placeholderAPI = new PlaceholderAPI();
/*     */     }
/* 210 */     if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
/* 211 */       this.luckPerms = new LuckPerms();
/*     */     }
/*     */     
/* 214 */     if (getServer().getPluginManager().isPluginEnabled("MagicCrates")) {
/* 215 */       this.magicCrates = new MagicCrates();
/*     */     }
/*     */     
/* 218 */     if (getServer().getPluginManager().isPluginEnabled("MagicGestures")) {
/* 219 */       this.magicGestures = new MagicGestures();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     if (!isItemsAdder()) {
/* 228 */       Cosmetic.loadCosmetics();
/* 229 */       Color.loadColors();
/* 230 */       Items.loadItems();
/* 231 */       Zone.loadZones();
/* 232 */       Token.loadTokens();
/* 233 */       Sound.loadSounds();
/* 234 */       Menu.loadMenus();
/*     */     } 
/*     */     
/* 237 */     this.cosmeticsManager = new CosmeticsManager();
/* 238 */     this.zonesManager = new ZonesManager();
/* 239 */     registerData();
/* 240 */     this.cosmeticsManager.runTasks();
/* 241 */     registerCommands();
/* 242 */     registerListeners();
/* 243 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 244 */       if (player == null || !player.isOnline())
/* 245 */         continue;  this.sql.loadPlayerAsync(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerData() {
/* 250 */     if (this.config.getBoolean("MySQL.enabled")) {
/* 251 */       this.sql = (SQL)new MySQL();
/*     */     } else {
/* 253 */       this.sql = (SQL)new SQLite();
/*     */     } 
/* 255 */     for (BossBar bar : this.bossBar) {
/* 256 */       bar.removeAll();
/*     */     }
/* 258 */     this.bossBar.clear();
/*     */     
/* 260 */     for (String lines : this.messages.getStringList("bossbar")) {
/* 261 */       if (isItemsAdder())
/* 262 */         lines = this.itemsAdder.replaceFontImages(lines); 
/* 263 */       if (isOraxen())
/* 264 */         lines = this.oraxen.replaceFontImages(lines); 
/* 265 */       BossBar boss = getServer().createBossBar(lines, this.bossBarColor, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
/* 266 */       boss.setVisible(true);
/* 267 */       this.bossBar.add(boss);
/*     */     } 
/*     */     
/* 270 */     this.showAllCosmeticsInMenu = this.config.getBoolean("show-all-cosmetics-in-menu", true);
/*     */     
/* 272 */     this.ava = getInstance().getMessages().getString("edge.available");
/* 273 */     this.unAva = getInstance().getMessages().getString("edge.unavailable");
/* 274 */     this.equip = getInstance().getMessages().getString("edge.equip");
/* 275 */     if (isItemsAdder()) {
/* 276 */       this.ava = this.itemsAdder.replaceFontImages(this.ava);
/* 277 */       this.unAva = this.itemsAdder.replaceFontImages(this.unAva);
/* 278 */       this.equip = this.itemsAdder.replaceFontImages(this.equip);
/*     */     } 
/* 280 */     if (isOraxen()) {
/* 281 */       this.ava = this.oraxen.replaceFontImages(this.ava);
/* 282 */       this.unAva = this.oraxen.replaceFontImages(this.unAva);
/* 283 */       this.equip = this.oraxen.replaceFontImages(this.equip);
/*     */     } 
/* 285 */     this.prefix = this.messages.getString("prefix");
/* 286 */     if (this.config.contains("leave-wardrobe-gamemode")) {
/*     */       try {
/* 288 */         this.gameMode = GameMode.valueOf(this.config.getString("leave-wardrobe-gamemode").toUpperCase());
/* 289 */       } catch (IllegalArgumentException exception) {
/* 290 */         getLogger().severe("Gamemode in config path: leave-wardrobe-gamemode Not Found!");
/*     */       } 
/*     */     }
/* 293 */     if (this.config.contains("main-menu"))
/* 294 */       this.mainMenu = this.config.getString("main-menu"); 
/* 295 */     if (this.config.contains("placeholder-api")) {
/* 296 */       this.placeholders = this.config.getBoolean("placeholder-api");
/*     */     }
/* 298 */     if (this.config.contains("permissions")) {
/* 299 */       setPermissions(this.config.getBoolean("permissions"));
/*     */     }
/* 301 */     this.equipMessage = false;
/* 302 */     if (this.config.contains("equip-message")) {
/* 303 */       this.equipMessage = this.config.getBoolean("equip-message");
/*     */     }
/* 305 */     if (this.config.contains("zones-hide-items")) {
/* 306 */       this.zoneHideItems = this.config.getBoolean("zones-hide-items");
/*     */     }
/* 308 */     if (this.config.contains("bossbar-color")) {
/*     */       try {
/* 310 */         this.bossBarColor = BarColor.valueOf(this.config.getString("bossbar-color").toUpperCase());
/* 311 */       } catch (IllegalArgumentException exception) {
/* 312 */         this.bossBarColor = BarColor.YELLOW;
/* 313 */         getLogger().severe("Bossbar color in config path: bossbar-color Not Valid!");
/*     */       } 
/*     */     }
/* 316 */     if (this.config.contains("proxy")) {
/* 317 */       this.proxy = this.config.getBoolean("proxy");
/*     */     }
/* 319 */     if (this.config.contains("spray-key")) {
/*     */       try {
/* 321 */         this.sprayKey = SprayKeys.valueOf(this.config.getString("spray-key").toUpperCase());
/* 322 */       } catch (IllegalArgumentException exception) {
/* 323 */         getLogger().severe("Spray key in config path: spray-key Not Valid!");
/*     */       } 
/*     */     }
/* 326 */     if (this.config.contains("spray-stay-time")) {
/* 327 */       this.sprayStayTime = this.config.getInt("spray-stay-time");
/*     */     }
/* 329 */     if (this.config.contains("spray-cooldown")) {
/* 330 */       this.sprayCooldown = this.config.getInt("spray-cooldown");
/*     */     }
/* 332 */     this.saveDataDelay = 300;
/* 333 */     if (this.config.contains("save-data-delay")) {
/* 334 */       this.saveDataDelay = this.config.getInt("save-data-delay");
/*     */     }
/* 336 */     if (this.config.contains("luckperms-server"))
/* 337 */       this.luckPermsServer = this.config.getString("luckperms-server"); 
/* 338 */     if (this.config.contains("on_execute_cosmetics"))
/* 339 */       this.onExecuteCosmetics = this.config.getString("on_execute_cosmetics"); 
/* 340 */     if (this.config.contains("worlds-blacklist"))
/* 341 */       this.worldsBlacklist = this.config.getStringListWF("worlds-blacklist"); 
/* 342 */     this.balloonRotation = this.config.getDouble("balloons-rotation");
/* 343 */     ZoneAction onEnter = null;
/* 344 */     ZoneAction onExit = null;
/* 345 */     if (this.zoneActions != null) {
/* 346 */       this.zoneActions.getOnEnter().setCommands(this.zones.getStringList("on_enter.commands"));
/* 347 */       this.zoneActions.getOnExit().setCommands(this.zones.getStringList("on_exit.commands"));
/* 348 */       this.zoneActions.setEnabled(this.config.getBoolean("zones-actions"));
/* 349 */       zoneActionsListener();
/*     */     } else {
/* 351 */       if (this.zones.contains("on_enter.commands"))
/* 352 */         onEnter = new ZoneAction("onEnter", this.zones.getStringList("on_enter.commands")); 
/* 353 */       if (this.zones.contains("on_exit.commands"))
/* 354 */         onExit = new ZoneAction("onEnter", this.zones.getStringList("on_exit.commands")); 
/* 355 */       this.zoneActions = new ZoneActions(onEnter, onExit);
/* 356 */       this.zoneActions.setEnabled(getConfig().getBoolean("zones-actions"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerListeners() {
/* 361 */     getServer().getPluginManager().registerEvents((Listener)new EntityListener(), (Plugin)this);
/* 362 */     getServer().getPluginManager().registerEvents((Listener)new InventoryListener(), (Plugin)this);
/* 363 */     getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
/* 364 */     if (isItemsAdder()) {
/* 365 */       getServer().getPluginManager().registerEvents((Listener)new ItemsAdderListener(), (Plugin)this);
/*     */     }
/* 367 */     if (isCitizens()) {
/* 368 */       getServer().getPluginManager().registerEvents((Listener)new CitizensListener(), (Plugin)this);
/*     */     }
/* 370 */     if (isHuskSync()) {
/* 371 */       getServer().getPluginManager().registerEvents((Listener)this.huskSync, (Plugin)this);
/*     */     }
/* 373 */     if (this.worldGuard != null) {
/* 374 */       getServer().getPluginManager().registerEvents((Listener)this.worldGuard, (Plugin)this);
/*     */     }
/* 376 */     if (getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
/* 377 */       getServer().getPluginManager().registerEvents((Listener)new MultiverseCListener(), (Plugin)this);
/*     */     }
/* 379 */     zoneActionsListener();
/* 380 */     if (isProxy()) {
/* 381 */       getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
/* 382 */       getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkIfProxy() {
/* 388 */     Path spigotPath = Paths.get("spigot.yml", new String[0]);
/* 389 */     if (Files.exists(spigotPath, new java.nio.file.LinkOption[0]) && YamlConfiguration.loadConfiguration(spigotPath.toFile()).getBoolean("settings.bungeecord")) {
/* 390 */       getLogger().info("Enabling BungeeMode!");
/* 391 */       setProxy(true);
/* 392 */       getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
/* 393 */       getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
/*     */       return;
/*     */     } 
/* 396 */     Path oldPaperPath = Paths.get("paper.yml", new String[0]);
/* 397 */     if (Utils.isPaper()) {
/* 398 */       if (Files.exists(oldPaperPath, new java.nio.file.LinkOption[0]) && YamlConfiguration.loadConfiguration(oldPaperPath.toFile()).getBoolean("settings.velocity-support.enabled")) {
/* 399 */         getLogger().info("Enabling VelocityMode!");
/* 400 */         setProxy(true);
/* 401 */         getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
/* 402 */         getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
/*     */         return;
/*     */       } 
/* 405 */       YamlConfiguration config = Utils.getPaperConfig(getServer());
/* 406 */       if (config != null && (config.getBoolean("settings.velocity-support.enabled") || config.getBoolean("proxies.velocity.enabled"))) {
/* 407 */         getLogger().info("Enabling VelocityMode!");
/* 408 */         setProxy(true);
/* 409 */         getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "mc:player", (PluginMessageListener)new ProxyListener());
/* 410 */         getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "mc:player");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void zoneActionsListener() {
/* 416 */     if (this.zoneActions.isEnabled()) {
/* 417 */       if (HandlerList.getRegisteredListeners((Plugin)this).stream().anyMatch(registeredListener -> registeredListener.getListener().equals(getZoneActions().getZoneListener())))
/* 418 */         return;  getServer().getPluginManager().registerEvents((Listener)getZoneActions().getZoneListener(), (Plugin)this);
/*     */       return;
/*     */     } 
/* 421 */     HandlerList.unregisterAll((Listener)getZoneActions().getZoneListener());
/*     */   }
/*     */   
/*     */   public void registerCommands() {
/* 425 */     getCommand("magicosmetics").setExecutor((CommandExecutor)new Command());
/* 426 */     getCommand("magicosmetics").setTabCompleter((TabCompleter)new Command());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 432 */     if (this.proxy) {
/* 433 */       getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this);
/* 434 */       getServer().getMessenger().unregisterOutgoingPluginChannel((Plugin)this);
/*     */     } 
/* 436 */     if (this.cosmeticsManager != null) {
/* 437 */       this.cosmeticsManager.cancelTasks();
/*     */     }
/* 439 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 440 */       if (player == null || !player.isOnline())
/* 441 */         continue;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 442 */       if (!playerData.isZone())
/* 443 */         continue;  playerData.exitZoneSync();
/*     */     } 
/* 445 */     this.sql.savePlayers();
/* 446 */     if (this.bossBar != null) {
/* 447 */       for (BossBar bar : this.bossBar) {
/* 448 */         bar.removeAll();
/*     */       }
/* 450 */       this.bossBar.clear();
/*     */     } 
/* 452 */     this.NPCsLoader.save();
/*     */   }
/*     */   
/*     */   public boolean isProxy() {
/* 456 */     return this.proxy;
/*     */   }
/*     */   
/*     */   public void setProxy(boolean proxy) {
/* 460 */     this.proxy = proxy;
/*     */   }
/*     */   
/*     */   public static MagicCosmetics getInstance() {
/* 464 */     return instance;
/*     */   }
/*     */   
/*     */   public FileCreator getConfig() {
/* 468 */     return this.config;
/*     */   }
/*     */   
/*     */   public FileCreator getMessages() {
/* 472 */     return this.messages;
/*     */   }
/*     */   
/*     */   public FileCosmetics getCosmetics() {
/* 476 */     return this.cosmetics;
/*     */   }
/*     */   
/*     */   public FileCreator getMenus() {
/* 480 */     return this.menus;
/*     */   }
/*     */   
/*     */   public FileCreator getZones() {
/* 484 */     return this.zones;
/*     */   }
/*     */   
/*     */   public FileCreator getTokens() {
/* 488 */     return this.tokens;
/*     */   }
/*     */   
/*     */   public SQL getSql() {
/* 492 */     return this.sql;
/*     */   }
/*     */   
/*     */   public CosmeticsManager getCosmeticsManager() {
/* 496 */     return this.cosmeticsManager;
/*     */   }
/*     */   
/*     */   public ZonesManager getZonesManager() {
/* 500 */     return this.zonesManager;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/* 504 */     return this.version;
/*     */   }
/*     */   
/*     */   public boolean getCosmetic() {
/* 508 */     MathUtils.floor(1.0F, 2.0F);
/* 509 */     User user = getUser();
/* 510 */     if (user == null) {
/* 511 */       getLogger().warning("Your user does not exist, how strange isn't it...?");
/* 512 */       return true;
/*     */     } 
/* 514 */     getLogger().info(" ");
/* 515 */     getLogger().info("Welcome " + user.getName() + "!");
/* 516 */     getLogger().info("Thank you for using MagicCosmetics =)!");
/* 517 */     getLogger().info(" ");
/* 518 */     return false;
/*     */   }
/*     */   
/*     */   public FileCreator getSounds() {
/* 522 */     return this.sounds;
/*     */   }
/*     */   
/*     */   public List<BossBar> getBossBar() {
/* 526 */     return this.bossBar;
/*     */   }
/*     */   
/*     */   public ModelEngine getModelEngine() {
/* 530 */     return this.modelEngine;
/*     */   }
/*     */   
/*     */   public boolean isModelEngine() {
/* 534 */     return (this.modelEngine != null);
/*     */   }
/*     */   
/*     */   public ItemsAdder getItemsAdder() {
/* 538 */     return this.itemsAdder;
/*     */   }
/*     */   
/*     */   public boolean isItemsAdder() {
/* 542 */     return (this.itemsAdder != null);
/*     */   }
/*     */   
/*     */   public Oraxen getOraxen() {
/* 546 */     return this.oraxen;
/*     */   }
/*     */   
/*     */   public boolean isOraxen() {
/* 550 */     return (this.oraxen != null);
/*     */   }
/*     */   
/*     */   public PlaceholderAPI getPlaceholderAPI() {
/* 554 */     return this.placeholderAPI;
/*     */   }
/*     */   
/*     */   public boolean isPlaceholderAPI() {
/* 558 */     return (this.placeholderAPI != null);
/*     */   }
/*     */   
/*     */   public User getUser() {
/* 562 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(User user) {
/* 566 */     this.user = user;
/*     */   }
/*     */   
/*     */   public boolean isCitizens() {
/* 570 */     return (this.citizens != null);
/*     */   }
/*     */   
/*     */   public Citizens getCitizens() {
/* 574 */     return this.citizens;
/*     */   }
/*     */   
/*     */   public ZNPCsPlus getzNPCsPlus() {
/* 578 */     return this.zNPCsPlus;
/*     */   }
/*     */   
/*     */   public boolean isPermissions() {
/* 582 */     return this.permissions;
/*     */   }
/*     */   
/*     */   public void createDefaultSpray() {
/* 586 */     File file = new File(getDataFolder(), "sprays");
/* 587 */     if (file.exists())
/* 588 */       return;  new FileCreator((Plugin)this, "sprays/first", ".png", getDataFolder());
/*     */   }
/*     */   
/*     */   public void setPermissions(boolean permissions) {
/* 592 */     this.permissions = permissions;
/*     */   }
/*     */   
/*     */   public SprayKeys getSprayKey() {
/* 596 */     return this.sprayKey;
/*     */   }
/*     */   
/*     */   public void setSprayKey(SprayKeys sprayKey) {
/* 600 */     this.sprayKey = sprayKey;
/*     */   }
/*     */   
/*     */   public int getSprayStayTime() {
/* 604 */     return this.sprayStayTime;
/*     */   }
/*     */   
/*     */   public void setSprayStayTime(int sprayStayTime) {
/* 608 */     this.sprayStayTime = sprayStayTime;
/*     */   }
/*     */   
/*     */   public int getSprayCooldown() {
/* 612 */     return this.sprayCooldown;
/*     */   }
/*     */   
/*     */   public void setSprayCooldown(int sprayCooldown) {
/* 616 */     this.sprayCooldown = sprayCooldown;
/*     */   }
/*     */   
/*     */   public boolean isZoneHideItems() {
/* 620 */     return this.zoneHideItems;
/*     */   }
/*     */   
/*     */   public void setZoneHideItems(boolean zoneHideItems) {
/* 624 */     this.zoneHideItems = zoneHideItems;
/*     */   }
/*     */   
/*     */   public LuckPerms getLuckPerms() {
/* 628 */     return this.luckPerms;
/*     */   }
/*     */   
/*     */   public boolean isLuckPerms() {
/* 632 */     return (this.luckPerms != null);
/*     */   }
/*     */   
/*     */   public void setPlaceholders(boolean placeholders) {
/* 636 */     this.placeholders = placeholders;
/*     */   }
/*     */   
/*     */   public boolean isPlaceholders() {
/* 640 */     return this.placeholders;
/*     */   }
/*     */   
/*     */   public String getMainMenu() {
/* 644 */     return this.mainMenu;
/*     */   }
/*     */   
/*     */   public void setMainMenu(String mainMenu) {
/* 648 */     this.mainMenu = mainMenu;
/*     */   }
/*     */   
/*     */   public ZoneActions getZoneActions() {
/* 652 */     return this.zoneActions;
/*     */   }
/*     */   
/*     */   public void setZoneActions(ZoneActions zoneActions) {
/* 656 */     this.zoneActions = zoneActions;
/*     */   }
/*     */   
/*     */   public String getLuckPermsServer() {
/* 660 */     return this.luckPermsServer;
/*     */   }
/*     */   
/*     */   public void setLuckPermsServer(String luckPermsServer) {
/* 664 */     this.luckPermsServer = luckPermsServer;
/*     */   }
/*     */   
/*     */   public String getOnExecuteCosmetics() {
/* 668 */     return this.onExecuteCosmetics;
/*     */   }
/*     */   
/*     */   public void setOnExecuteCosmetics(String onExecuteCosmetics) {
/* 672 */     this.onExecuteCosmetics = onExecuteCosmetics;
/*     */   }
/*     */   
/*     */   public FileCreator getNPCs() {
/* 676 */     return this.npcs;
/*     */   }
/*     */   
/*     */   public NPCsLoader getNPCsLoader() {
/* 680 */     return this.NPCsLoader;
/*     */   }
/*     */   
/*     */   public MagicCrates getMagicCrates() {
/* 684 */     return this.magicCrates;
/*     */   }
/*     */   
/*     */   public MagicGestures getMagicGestures() {
/* 688 */     return this.magicGestures;
/*     */   }
/*     */   
/*     */   public List<String> getWorldsBlacklist() {
/* 692 */     return this.worldsBlacklist;
/*     */   }
/*     */   
/*     */   public HuskSync getHuskSync() {
/* 696 */     return this.huskSync;
/*     */   }
/*     */   
/*     */   public boolean isHuskSync() {
/* 700 */     return (this.huskSync != null);
/*     */   }
/*     */   
/*     */   public boolean isShowAllCosmeticsInMenu() {
/* 704 */     return this.showAllCosmeticsInMenu;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\MagicCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */