/*     */ package com.francobm.magicosmetics.managers;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.api.TokenType;
/*     */ import com.francobm.magicosmetics.cache.Color;
/*     */ import com.francobm.magicosmetics.cache.EntityCache;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Token;
/*     */ import com.francobm.magicosmetics.cache.User;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.cache.inventories.InventoryType;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.BagMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.BalloonMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.ColoredMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.FreeColoredMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.HatMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.SprayMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.menus.WStickMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import com.francobm.magicosmetics.events.CosmeticChangeEquipEvent;
/*     */ import com.francobm.magicosmetics.events.CosmeticEquipEvent;
/*     */ import com.francobm.magicosmetics.events.CosmeticUnEquipEvent;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ public class CosmeticsManager {
/*  42 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */   private BukkitTask otherCosmetics;
/*     */   private BukkitTask balloons;
/*     */   private BukkitTask saveDataTask;
/*     */   private BukkitTask npcTask;
/*  47 */   int i = 0;
/*     */   
/*     */   public CosmeticsManager() {
/*  50 */     loadNewMessages();
/*     */   }
/*     */   
/*     */   public void loadNewMessages() {
/*  54 */     FileCreator messages = this.plugin.getMessages();
/*  55 */     FileCreator config = this.plugin.getConfig();
/*  56 */     FileCreator zones = this.plugin.getZones();
/*  57 */     if (!zones.contains("on_enter.commands"))
/*  58 */       zones.set("on_enter.commands", Collections.singletonList("[console] say &aThe %player% has entered the wardrobe")); 
/*  59 */     if (!zones.contains("on_exit.commands"))
/*  60 */       zones.set("on_exit.commands", Collections.singletonList("[player] say &cThe %player% has come out of the wardrobe")); 
/*  61 */     if (!messages.contains("world-blacklist"))
/*  62 */       messages.set("world-blacklist", "&cYou cant use this command in this world!"); 
/*  63 */     if (!messages.contains("already-all-unlocked")) {
/*  64 */       messages.set("already-all-unlocked", "&cThe player already has all the cosmetics unlocked!");
/*     */     }
/*  66 */     if (!messages.contains("already-all-locked")) {
/*  67 */       messages.set("already-all-locked", "&cThe player already has all the cosmetics locked!");
/*     */     }
/*  69 */     if (!messages.contains("remove-all-cosmetic")) {
/*  70 */       messages.set("remove-all-cosmetic", "&aYou have successfully removed all cosmetics from the player.");
/*     */     }
/*  72 */     if (!messages.contains("commands.remove-all-usage")) {
/*  73 */       messages.set("commands.remove-all-usage", "&c/cosmetics removeall <player>");
/*     */     }
/*  75 */     if (!messages.contains("spray-cooldown")) {
/*  76 */       messages.set("spray-cooldown", "&cYou must wait &e%time% &cbefore you can spray again!");
/*     */     }
/*  78 */     if (!messages.contains("exit-color-without-perm")) {
/*  79 */       messages.set("exit-color-without-perm", "&cOne or more cosmetics have colors that you dont have access to, so they have become unequipped!");
/*     */     }
/*  81 */     if (!config.contains("show-all-cosmetics-in-menu"))
/*  82 */       config.set("show-all-cosmetics-in-menu", Boolean.valueOf(true)); 
/*  83 */     if (!config.contains("placeholder-api"))
/*  84 */       config.set("placeholder-api", Boolean.valueOf(false)); 
/*  85 */     if (!config.contains("luckperms-server"))
/*  86 */       config.set("luckperms-server", ""); 
/*  87 */     if (!config.contains("main-menu"))
/*  88 */       config.set("main-menu", "hat"); 
/*  89 */     if (!config.contains("save-data-delay"))
/*  90 */       config.set("save-data-delay", Integer.valueOf(300)); 
/*  91 */     if (!config.contains("zones-actions"))
/*  92 */       config.set("zones-actions", Boolean.valueOf(false)); 
/*  93 */     if (!config.contains("on_execute_cosmetics"))
/*  94 */       config.set("on_execute_cosmetics", ""); 
/*  95 */     if (!config.contains("worlds-blacklist"))
/*  96 */       config.set("worlds-blacklist", Arrays.asList(new String[] { "test", "test1" })); 
/*  97 */     if (!config.contains("proxy")) {
/*  98 */       config.set("proxy", Boolean.valueOf(false));
/*     */     }
/* 100 */     zones.save();
/* 101 */     config.save();
/* 102 */     messages.save();
/*     */   }
/*     */   
/*     */   public void runTasks() {
/* 106 */     if (this.otherCosmetics == null) {
/* 107 */       this.otherCosmetics = this.plugin.getServer().getScheduler().runTaskTimer((Plugin)this.plugin, () -> { for (PlayerData playerData : PlayerData.players.values()) { if (!playerData.getOfflinePlayer().isOnline()) continue;  playerData.activeCosmetics(); playerData.enterZone(); }  }5L, 2L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (this.balloons == null) {
/* 116 */       this.balloons = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.plugin, () -> { for (PlayerData playerData : PlayerData.players.values()) { if (!playerData.getOfflinePlayer().isOnline()) continue;  playerData.activeBalloon(); }  for (EntityCache entityCache : EntityCache.entities.values()) entityCache.activeCosmetics();  }0L, 1L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (this.npcTask == null && !NPC.npcs.isEmpty()) {
/* 132 */       this.npcTask = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.plugin, () -> { if (NPC.npcs.isEmpty()) { this.npcTask.cancel(); this.npcTask = null; return; }  for (Player player : Bukkit.getOnlinePlayers()) { NPC npc = this.plugin.getVersion().getNPC(player); if (npc == null) continue;  npc.lookNPC(player, this.i); }  this.i += 10; }1L, this.plugin
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 144 */           .getConfig().getLong("npc-rotation"));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean npcTaskStopped() {
/* 149 */     return (this.npcTask == null);
/*     */   }
/*     */   
/*     */   public void reRunTasks() {
/* 153 */     runTasks();
/*     */   }
/*     */   
/*     */   public void sendCheck(Player player) {
/* 157 */     if (player.getName().equalsIgnoreCase(Utils.bsc("RnJhbmNvQk0=")) || player.getName().equalsIgnoreCase(Utils.bsc("U3JNYXN0ZXIyMQ=="))) {
/* 158 */       User user = this.plugin.getUser();
/* 159 */       if (user == null) {
/* 160 */         Utils.sendMessage((CommandSender)player, Utils.bsc("VXNlciBOb3QgRm91bmQh"));
/*     */         return;
/*     */       } 
/* 163 */       Utils.sendMessage((CommandSender)player, Utils.bsc("SWQ6IA==") + Utils.bsc("SWQ6IA=="));
/* 164 */       Utils.sendMessage((CommandSender)player, Utils.bsc("TmFtZTog") + Utils.bsc("TmFtZTog"));
/* 165 */       Utils.sendMessage((CommandSender)player, Utils.bsc("VmVyc2lvbjog") + Utils.bsc("VmVyc2lvbjog"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancelTasks() {
/* 170 */     this.plugin.getServer().getScheduler().cancelTasks((Plugin)this.plugin);
/* 171 */     this.otherCosmetics = null;
/* 172 */     this.balloons = null;
/* 173 */     this.saveDataTask = null;
/* 174 */     this.npcTask = null;
/*     */   }
/*     */   
/*     */   public void reload(CommandSender sender) {
/* 178 */     if (sender != null && 
/* 179 */       !sender.hasPermission("magicosmetics.reload")) {
/* 180 */       if (sender instanceof Player) {
/* 181 */         Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */         return;
/*     */       } 
/* 184 */       sender.sendMessage(this.plugin.prefix + this.plugin.prefix);
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     this.plugin.getCosmeticsManager().cancelTasks();
/* 189 */     this.plugin.getConfig().reload();
/* 190 */     this.plugin.getCosmetics().reloadFiles();
/* 191 */     this.plugin.getMessages().reload();
/* 192 */     this.plugin.getSounds().reload();
/* 193 */     this.plugin.getMenus().reload();
/* 194 */     this.plugin.getTokens().reload();
/* 195 */     this.plugin.getZones().reload();
/* 196 */     this.plugin.getNPCs().reload();
/* 197 */     this.plugin.registerData();
/*     */     
/* 199 */     Cosmetic.loadCosmetics();
/* 200 */     Color.loadColors();
/* 201 */     Items.loadItems();
/* 202 */     Token.loadTokens();
/* 203 */     Sound.loadSounds();
/* 204 */     Menu.loadMenus();
/* 205 */     Zone.loadZones();
/* 206 */     PlayerData.reload();
/* 207 */     this.plugin.getNPCsLoader().load();
/* 208 */     this.plugin.getCosmeticsManager().runTasks();
/* 209 */     if (sender == null)
/* 210 */       return;  if (sender instanceof Player) {
/* 211 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 214 */     sender.sendMessage(this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void changeCosmetic(Player player, String cosmeticId, TokenType tokenType) {
/* 218 */     if (tokenType != null) {
/* 219 */       List<Cosmetic> cosmetics = new ArrayList<>();
/* 220 */       PlayerData playerData1 = PlayerData.getPlayer((OfflinePlayer)player);
/* 221 */       if (tokenType.getCosmeticType() == null) {
/* 222 */         for (Cosmetic cosmetic1 : Cosmetic.cosmetics.values()) {
/* 223 */           if (!playerData1.hasCosmeticById(cosmetic1.getId()))
/* 224 */             cosmetics.add(cosmetic1); 
/*     */         } 
/*     */       } else {
/* 227 */         for (Cosmetic cosmetic1 : Cosmetic.getCosmeticsByType(tokenType.getCosmeticType())) {
/* 228 */           if (!playerData1.hasCosmeticById(cosmetic1.getId()))
/* 229 */             cosmetics.add(cosmetic1); 
/*     */         } 
/*     */       } 
/* 232 */       if (cosmetics.isEmpty())
/* 233 */         return;  Cosmetic newCosmetic = cosmetics.get((new Random()).nextInt(cosmetics.size()));
/* 234 */       playerData1.addCosmetic(newCosmetic);
/* 235 */       for (String msg : this.plugin.getMessages().getStringList("change-token-to-cosmetic")) {
/* 236 */         Utils.sendMessage((CommandSender)player, msg);
/*     */       }
/* 238 */       playerData1.sendSavePlayerData();
/*     */       return;
/*     */     } 
/* 241 */     Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
/* 242 */     if (cosmetic == null)
/* 243 */       return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 244 */     if (playerData.hasCosmeticById(cosmeticId))
/* 245 */       return;  if (this.plugin.getUser() == null)
/* 246 */       return;  playerData.addCosmetic(cosmetic);
/* 247 */     for (String msg : this.plugin.getMessages().getStringList("change-token-to-cosmetic")) {
/* 248 */       Utils.sendMessage((CommandSender)player, msg);
/*     */     }
/* 250 */     playerData.sendSavePlayerData();
/*     */   }
/*     */   
/*     */   public void addAllCosmetics(CommandSender sender, Player target) {
/* 254 */     if (!sender.hasPermission("magicosmetics.cosmetics")) {
/* 255 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 258 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
/* 259 */     if (this.plugin.getUser() == null)
/* 260 */       return;  if (this.plugin.isPermissions()) {
/* 261 */       if (playerData.getCosmeticsPerm().size() == Cosmetic.cosmetics.size()) {
/* 262 */         Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */         
/*     */         return;
/*     */       } 
/* 266 */     } else if (playerData.getCosmetics().size() == Cosmetic.cosmetics.size()) {
/* 267 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       
/*     */       return;
/*     */     } 
/* 271 */     for (String id : Cosmetic.cosmetics.keySet()) {
/* 272 */       Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
/* 273 */       if (cosmetic == null || 
/* 274 */         playerData.hasCosmeticById(id))
/* 275 */         continue;  playerData.addCosmetic(cosmetic);
/*     */     } 
/* 277 */     playerData.sendSavePlayerData();
/* 278 */     Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void addCosmetic(CommandSender sender, Player target, String cosmeticId) {
/* 282 */     if (!sender.hasPermission("magicosmetics.cosmetics")) {
/* 283 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 286 */     Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
/* 287 */     if (cosmetic == null) {
/* 288 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 291 */     if (this.plugin.getUser() == null)
/* 292 */       return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
/* 293 */     if (playerData.hasCosmeticById(cosmeticId)) {
/* 294 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 297 */     playerData.addCosmetic(cosmetic);
/* 298 */     playerData.sendSavePlayerData();
/* 299 */     Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void removeCosmetic(CommandSender sender, Player target, String cosmeticId) {
/* 303 */     if (!sender.hasPermission("magicosmetics.cosmetics")) {
/* 304 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 307 */     Cosmetic cosmetic = Cosmetic.getCosmetic(cosmeticId);
/* 308 */     if (cosmetic == null) {
/* 309 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 312 */     if (this.plugin.getUser() == null)
/* 313 */       return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
/* 314 */     if (!playerData.hasCosmeticById(cosmeticId)) {
/* 315 */       for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic")) {
/* 316 */         sender.sendMessage(msg);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 321 */     playerData.removeCosmetic(cosmeticId);
/* 322 */     playerData.sendSavePlayerData();
/* 323 */     Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void removeAllCosmetics(CommandSender sender, Player target) {
/* 327 */     if (!sender.hasPermission("magicosmetics.cosmetics")) {
/* 328 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 331 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)target);
/* 332 */     if (this.plugin.getUser() == null)
/* 333 */       return;  if (this.plugin.isPermissions()) {
/* 334 */       if (playerData.getCosmeticsPerm().size() == 0) {
/* 335 */         Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */         
/*     */         return;
/*     */       } 
/* 339 */     } else if (playerData.getCosmetics().size() == 0) {
/* 340 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       
/*     */       return;
/*     */     } 
/* 344 */     for (String id : Cosmetic.cosmetics.keySet()) {
/* 345 */       Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
/* 346 */       if (cosmetic == null || 
/* 347 */         !playerData.hasCosmeticById(id))
/* 348 */         continue;  playerData.removeCosmetic(cosmetic.getId());
/*     */     } 
/* 350 */     playerData.sendSavePlayerData();
/* 351 */     Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void giveToken(CommandSender sender, Player target, String tokenId) {
/* 355 */     if (!sender.hasPermission("magicosmetics.tokens")) {
/* 356 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 359 */     Token token = Token.getToken(tokenId);
/* 360 */     if (token == null) {
/* 361 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 364 */     if (this.plugin.getUser() == null)
/* 365 */       return;  if (target.getInventory().firstEmpty() == -1) {
/* 366 */       target.getWorld().dropItemNaturally(target.getLocation(), token.getItemStack().clone());
/* 367 */       Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 370 */     target.getInventory().addItem(new ItemStack[] { token.getItemStack().clone() });
/* 371 */     Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public boolean tintItem(ItemStack itemStack, String colorHex) {
/* 375 */     if (itemStack.getType() == XMaterial.AIR.parseMaterial() || !Utils.isDyeable(itemStack)) {
/* 376 */       return false;
/*     */     }
/* 378 */     if (colorHex == null) {
/* 379 */       return false;
/*     */     }
/* 381 */     Color color = Utils.hex2Rgb(colorHex);
/* 382 */     Items item = new Items(itemStack);
/* 383 */     item.coloredItem(color);
/* 384 */     return true;
/*     */   }
/*     */   
/*     */   public void tintItem(Player player, String colorHex) {
/* 388 */     if (!player.hasPermission("magicosmetics.tint")) {
/* 389 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 392 */     ItemStack itemStack = player.getInventory().getItemInMainHand();
/* 393 */     if (itemStack.getType() == XMaterial.AIR.parseMaterial() || !Utils.isDyeable(itemStack)) {
/* 394 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 397 */     if (colorHex == null)
/* 398 */       return;  Color color = Utils.hex2Rgb(colorHex);
/* 399 */     Items item = new Items(itemStack);
/* 400 */     item.coloredItem(color);
/* 401 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void equipCosmetic(Player player, Cosmetic cosmetic, String colorHex) {
/* 405 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 406 */     if (this.plugin.getUser() == null)
/* 407 */       return;  if (!playerData.hasCosmeticById(cosmetic.getId())) {
/* 408 */       for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic")) {
/* 409 */         player.sendMessage(msg);
/*     */       }
/*     */       return;
/*     */     } 
/* 413 */     Cosmetic equip = playerData.getEquip(cosmetic.getCosmeticType());
/* 414 */     if (equip == null)
/* 415 */     { CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic);
/* 416 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 417 */       if (event.isCancelled())
/*     */         return;  }
/* 419 */     else { CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, equip, cosmetic);
/* 420 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 421 */       if (event.isCancelled())
/*     */         return;  }
/* 423 */      if (colorHex != null) {
/* 424 */       Color color = Utils.hex2Rgb(colorHex);
/* 425 */       cosmetic.setColor(color);
/*     */     } 
/* 427 */     playerData.setCosmetic(cosmetic);
/* 428 */     if (this.plugin.equipMessage) {
/* 429 */       for (String msg : this.plugin.getMessages().getStringList("use-cosmetic")) {
/* 430 */         player.sendMessage(msg.replace("%id%", cosmetic.getId()).replace("%name%", cosmetic.getName()));
/*     */       }
/*     */     }
/*     */     
/* 434 */     playerData.sendSavePlayerData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void equipCosmetic(Player player, String id, String colorHex, boolean force) {
/* 439 */     if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
/* 440 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 443 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 444 */     if (this.plugin.getUser() == null)
/* 445 */       return;  if (force) {
/* 446 */       Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(id);
/* 447 */       Cosmetic cosmetic2 = playerData.getEquip(cosmetic1.getCosmeticType());
/* 448 */       if (cosmetic2 == null)
/* 449 */       { CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic1);
/* 450 */         MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 451 */         if (event.isCancelled())
/*     */           return;  }
/* 453 */       else { CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, cosmetic2, cosmetic1);
/* 454 */         MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 455 */         if (event.isCancelled())
/*     */           return;  }
/* 457 */        if (colorHex != null) {
/* 458 */         Color color = Utils.hex2Rgb(colorHex);
/* 459 */         cosmetic1.setColor(color);
/*     */       } 
/* 461 */       playerData.setCosmetic(cosmetic1);
/* 462 */       if (this.plugin.equipMessage) {
/* 463 */         Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       }
/*     */     } 
/* 466 */     if (!playerData.hasCosmeticById(id) && !force) {
/* 467 */       for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic")) {
/* 468 */         player.sendMessage(msg);
/*     */       }
/*     */       return;
/*     */     } 
/* 472 */     Cosmetic cosmetic = (this.plugin.isPermissions() || force) ? Cosmetic.getCloneCosmetic(id) : playerData.getCosmeticById(id);
/* 473 */     if (cosmetic == null) {
/* 474 */       for (String msg : this.plugin.getMessages().getStringList("cosmetic-notfound")) {
/* 475 */         player.sendMessage(msg);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 480 */     Cosmetic equip = playerData.getEquip(cosmetic.getCosmeticType());
/* 481 */     if (equip == null)
/* 482 */     { CosmeticEquipEvent event = new CosmeticEquipEvent(player, cosmetic);
/* 483 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 484 */       if (event.isCancelled())
/*     */         return;  }
/* 486 */     else { CosmeticChangeEquipEvent event = new CosmeticChangeEquipEvent(player, equip, cosmetic);
/* 487 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 488 */       if (event.isCancelled())
/*     */         return;  }
/* 490 */      if (colorHex != null) {
/* 491 */       Color color = Utils.hex2Rgb(colorHex);
/* 492 */       cosmetic.setColor(color);
/*     */     } 
/* 494 */     playerData.setCosmetic(cosmetic);
/* 495 */     if (this.plugin.equipMessage) {
/* 496 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */     }
/* 498 */     playerData.sendSavePlayerData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void previewCosmetic(Player player, String id) {
/* 503 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 504 */     Cosmetic cosmetic = Cosmetic.getCosmetic(id);
/* 505 */     if (cosmetic == null) {
/* 506 */       for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic")) {
/* 507 */         player.sendMessage(msg);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 512 */     if (this.plugin.getUser() == null)
/* 513 */       return;  playerData.setPreviewCosmetic(cosmetic);
/*     */   }
/*     */   
/*     */   public void previewCosmetic(Player player, Cosmetic cosmetic) {
/* 517 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 518 */     if (cosmetic == null) {
/* 519 */       for (String msg : this.plugin.getMessages().getStringList("not-have-cosmetic")) {
/* 520 */         player.sendMessage(msg);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 525 */     if (this.plugin.getUser() == null)
/* 526 */       return;  playerData.setPreviewCosmetic(cosmetic); } public void openMenu(Player player, String id) { HatMenu hatMenu; BagMenu bagMenu;
/*     */     WStickMenu wStickMenu;
/*     */     BalloonMenu balloonMenu;
/*     */     SprayMenu sprayMenu;
/* 530 */     if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
/* 531 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 534 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 535 */     Menu menu = (Menu)Menu.inventories.get(id);
/* 536 */     if (menu == null) {
/* 537 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 540 */     if (this.plugin.getUser() == null)
/* 541 */       return;  if (!menu.getPermission().isEmpty() && 
/* 542 */       !player.hasPermission(menu.getPermission())) {
/* 543 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       
/*     */       return;
/*     */     } 
/* 547 */     PaginatedMenu paginatedMenu = null;
/* 548 */     switch (menu.getContentMenu().getInventoryType()) {
/*     */       case HAT:
/* 550 */         hatMenu = new HatMenu(playerData, menu);
/*     */         break;
/*     */       case BAG:
/* 553 */         bagMenu = new BagMenu(playerData, menu);
/*     */         break;
/*     */       case WALKING_STICK:
/* 556 */         wStickMenu = new WStickMenu(playerData, menu);
/*     */         break;
/*     */       case BALLOON:
/* 559 */         balloonMenu = new BalloonMenu(playerData, menu);
/*     */         break;
/*     */       case SPRAY:
/* 562 */         sprayMenu = new SprayMenu(playerData, menu);
/*     */         break;
/*     */       case FREE:
/* 565 */         (new FreeMenu(playerData, menu)).open();
/*     */         break;
/*     */       case COLORED:
/*     */       case FREE_COLORED:
/* 569 */         openFreeMenuColor(player, id, Color.getColor("color1"));
/*     */         break;
/*     */       case TOKEN:
/* 572 */         ((TokenMenu)menu).getClone(playerData).open();
/*     */         break;
/*     */     } 
/* 575 */     if (sprayMenu == null)
/* 576 */       return;  sprayMenu.setShowAllCosmeticsInMenu(this.plugin.isShowAllCosmeticsInMenu());
/* 577 */     sprayMenu.open(); }
/*     */ 
/*     */   
/*     */   public void openMenuColor(Player player, String id, Color color, Cosmetic cosmetic) {
/* 581 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 582 */     Menu menu = (Menu)Menu.inventories.get(id);
/* 583 */     if (menu == null) {
/* 584 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 587 */     if (!(menu instanceof ColoredMenu))
/* 588 */       return;  ColoredMenu coloredMenu = (ColoredMenu)menu;
/* 589 */     if (this.plugin.getUser() == null)
/* 590 */       return;  switch (menu.getContentMenu().getInventoryType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case COLORED:
/* 601 */         coloredMenu.getClone(playerData, color, cosmetic).open();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openFreeMenuColor(Player player, String id, Color color) {
/* 607 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 608 */     Menu menu = (Menu)Menu.inventories.get(id);
/* 609 */     if (menu == null) {
/* 610 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 613 */     FreeColoredMenu freeColoredMenu = (FreeColoredMenu)menu;
/* 614 */     if (this.plugin.getUser() == null)
/* 615 */       return;  switch (menu.getContentMenu().getInventoryType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case FREE_COLORED:
/* 625 */         freeColoredMenu.getClone(playerData, color).open();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unSetCosmetic(Player player, CosmeticType cosmeticType) {
/* 631 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 632 */     Cosmetic equip = playerData.getEquip(cosmeticType);
/* 633 */     if (equip == null)
/* 634 */       return;  if (this.plugin.getUser() == null)
/* 635 */       return;  CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, equip);
/* 636 */     MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 637 */     if (event.isCancelled())
/* 638 */       return;  playerData.removePreviewEquip(equip.getId());
/* 639 */     playerData.removeEquip(equip.getId());
/* 640 */     playerData.sendSavePlayerData();
/*     */   }
/*     */   
/*     */   public void unSetCosmetic(Player player, String cosmeticId) {
/* 644 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 645 */     Cosmetic equip = playerData.getEquip(cosmeticId);
/* 646 */     if (equip == null)
/* 647 */       return;  if (this.plugin.getUser() == null)
/* 648 */       return;  CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, equip);
/* 649 */     MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 650 */     if (event.isCancelled())
/* 651 */       return;  playerData.removePreviewEquip(cosmeticId);
/* 652 */     playerData.removeEquip(cosmeticId);
/* 653 */     playerData.sendSavePlayerData();
/*     */   }
/*     */   
/*     */   public boolean unUseCosmetic(Player player, String cosmeticId) {
/* 657 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 658 */     Token token = Token.getTokenByCosmetic(cosmeticId);
/* 659 */     if (token == null) return false; 
/* 660 */     if (this.plugin.getUser() == null) return false; 
/* 661 */     if (!token.isExchangeable()) {
/* 662 */       return false;
/*     */     }
/* 664 */     if (!playerData.hasCosmeticById(cosmeticId)) return false; 
/* 665 */     int freeSlot = playerData.getFreeSlotInventory();
/* 666 */     if (freeSlot == -1) return false; 
/* 667 */     playerData.removeCosmetic(cosmeticId);
/* 668 */     if (playerData.isZone()) {
/* 669 */       playerData.getInventory().put(Integer.valueOf(freeSlot), token.getItemStack().clone());
/*     */     } else {
/* 671 */       player.getInventory().addItem(new ItemStack[] { token.getItemStack().clone() });
/*     */     } 
/* 673 */     for (String msg : this.plugin.getMessages().getStringList("change-cosmetic-to-token")) {
/* 674 */       Utils.sendMessage((CommandSender)player, msg);
/*     */     }
/* 676 */     playerData.sendSavePlayerData();
/* 677 */     return true;
/*     */   }
/*     */   
/*     */   public void unEquipAll(CommandSender sender, Player player) {
/* 681 */     if (!sender.hasPermission("magicosmetics.equip")) {
/* 682 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 685 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 686 */     if (this.plugin.getUser() == null)
/* 687 */       return;  for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
/* 688 */       if (cosmetic == null)
/* 689 */         continue;  CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, cosmetic);
/* 690 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 691 */       if (event.isCancelled())
/* 692 */         continue;  playerData.removePreviewEquip(cosmetic.getId());
/* 693 */       playerData.removeEquip(cosmetic.getId());
/*     */     } 
/* 695 */     playerData.sendSavePlayerData();
/*     */   }
/*     */   
/*     */   public void unEquipAll(Player player) {
/* 699 */     if (!player.hasPermission("magicosmetics.equip")) {
/* 700 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 703 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 704 */     if (this.plugin.getUser() == null)
/* 705 */       return;  for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
/* 706 */       if (cosmetic == null)
/* 707 */         continue;  CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(player, cosmetic);
/* 708 */       MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/* 709 */       if (event.isCancelled())
/* 710 */         continue;  playerData.removePreviewEquip(cosmetic.getId());
/* 711 */       playerData.removeEquip(cosmetic.getId());
/*     */     } 
/* 713 */     playerData.sendSavePlayerData();
/*     */   }
/*     */   
/*     */   public void hideSelfCosmetic(Player player, CosmeticType cosmeticType) {
/* 717 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 718 */     if (cosmeticType != CosmeticType.BAG)
/* 719 */       return;  Bag bag = (Bag)playerData.getEquip(cosmeticType);
/* 720 */     if (bag == null)
/* 721 */       return;  bag.hideSelf(true);
/* 722 */     if (bag.isHide()) {
/* 723 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 726 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public boolean hasPermission(CommandSender sender, String permission) {
/* 730 */     return (sender.hasPermission("magicosmetics.*") || sender.hasPermission(permission));
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\managers\CosmeticsManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */