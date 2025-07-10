/*      */ package com.francobm.magicosmetics.cache;
/*      */ import com.francobm.magicosmetics.MagicCosmetics;
/*      */ import com.francobm.magicosmetics.api.Cosmetic;
/*      */ import com.francobm.magicosmetics.api.CosmeticType;
/*      */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*      */ import com.francobm.magicosmetics.cache.cosmetics.Spray;
/*      */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*      */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*      */ import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
/*      */ import com.francobm.magicosmetics.events.PlayerChangeBlacklistEvent;
/*      */ import com.francobm.magicosmetics.events.ZoneExitEvent;
/*      */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*      */ import com.francobm.magicosmetics.utils.Utils;
/*      */ import com.francobm.magicosmetics.utils.XMaterial;
/*      */ import com.google.common.io.ByteArrayDataOutput;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.OfflinePlayer;
/*      */ import org.bukkit.boss.BossBar;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.Event;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ 
/*      */ public class PlayerData {
/*   31 */   public static Map<UUID, PlayerData> players = new HashMap<>();
/*      */   private OfflinePlayer offlinePlayer;
/*      */   private final UUID uniqueId;
/*      */   private final String name;
/*      */   private Hat hat;
/*      */   private Cosmetic bag;
/*      */   private WStick wStick;
/*      */   private Cosmetic balloon;
/*      */   private Cosmetic spray;
/*      */   private final Map<String, Cosmetic> cosmetics;
/*      */   private Cosmetic previewHat;
/*      */   private Cosmetic previewBag;
/*      */   private Cosmetic previewWStick;
/*      */   private Cosmetic previewBalloon;
/*      */   private Cosmetic previewSpray;
/*      */   private boolean isZone;
/*      */   private boolean sneak;
/*      */   private boolean spectator;
/*      */   private Zone zone;
/*      */   private final Map<Integer, ItemStack> inventory;
/*      */   private GameMode gameMode;
/*      */   private float speedFly;
/*      */   private boolean hideCosmetics;
/*      */   private boolean hasInBlackList;
/*      */   private final IRangeManager rangeManager;
/*      */   
/*      */   public PlayerData(UUID uniqueId, String name, IRangeManager rangeManager) {
/*   58 */     this.uniqueId = uniqueId;
/*   59 */     this.name = name;
/*   60 */     this.hat = null;
/*   61 */     this.bag = null;
/*   62 */     this.wStick = null;
/*   63 */     this.balloon = null;
/*   64 */     this.cosmetics = new HashMap<>();
/*   65 */     this.previewHat = null;
/*   66 */     this.previewBag = null;
/*   67 */     this.previewWStick = null;
/*   68 */     this.previewBalloon = null;
/*   69 */     this.isZone = false;
/*   70 */     this.sneak = false;
/*   71 */     this.spectator = false;
/*   72 */     this.zone = null;
/*   73 */     this.inventory = new HashMap<>();
/*   74 */     this.offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
/*   75 */     this.rangeManager = rangeManager;
/*      */   }
/*      */   
/*      */   public static PlayerData getPlayer(OfflinePlayer player) {
/*   79 */     if (!players.containsKey(player.getUniqueId())) {
/*   80 */       PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName(), null);
/*   81 */       players.put(player.getUniqueId(), playerData);
/*   82 */       return playerData;
/*      */     } 
/*   84 */     return players.get(player.getUniqueId());
/*      */   }
/*      */   
/*      */   public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
/*   88 */     this.offlinePlayer = offlinePlayer;
/*      */   }
/*      */   
/*      */   public static void reload() {
/*   92 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*   93 */       PlayerData playerData = getPlayer((OfflinePlayer)player);
/*   94 */       playerData.updateCosmetics();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateCosmetics() {
/*   99 */     clearCosmeticsInUse(true);
/*  100 */     if (this.hat != null && !this.hat.updateProperties()) {
/*  101 */       removeHat();
/*      */     }
/*  103 */     if (this.wStick != null && !this.wStick.updateProperties()) {
/*  104 */       removeWStick();
/*      */     }
/*  106 */     if (this.balloon != null && !this.balloon.updateProperties()) {
/*  107 */       removeBalloon();
/*      */     }
/*  109 */     if (this.spray != null && !this.spray.updateProperties()) {
/*  110 */       removeSpray();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCosmeticCount(CosmeticType cosmeticType) {
/*  127 */     int i = 0;
/*  128 */     if (MagicCosmetics.getInstance().isPermissions()) {
/*  129 */       for (Cosmetic cosmetic : Cosmetic.cosmetics.values()) {
/*  130 */         if (!cosmetic.hasPermission(getOfflinePlayer().getPlayer()) || 
/*  131 */           cosmetic.getCosmeticType() != cosmeticType)
/*  132 */           continue;  i++;
/*      */       } 
/*  134 */       return i;
/*      */     } 
/*  136 */     for (Cosmetic cosmetic : this.cosmetics.values()) {
/*  137 */       if (cosmetic.getCosmeticType() != cosmeticType)
/*  138 */         continue;  i++;
/*      */     } 
/*  140 */     return i;
/*      */   }
/*      */   
/*      */   public Cosmetic getPreviewBalloon() {
/*  144 */     return this.previewBalloon;
/*      */   }
/*      */   
/*      */   public void setPreviewBalloon(Cosmetic previewBalloon) {
/*  148 */     this.previewBalloon = previewBalloon;
/*      */   }
/*      */   
/*      */   public Cosmetic getPreviewSpray() {
/*  152 */     return this.previewSpray;
/*      */   }
/*      */   
/*      */   public void setPreviewSpray(Cosmetic previewSpray) {
/*  156 */     this.previewSpray = previewSpray;
/*      */   }
/*      */   
/*      */   public Cosmetic getPreviewHat() {
/*  160 */     return this.previewHat;
/*      */   }
/*      */   
/*      */   public void setPreviewHat(Cosmetic previewHat) {
/*  164 */     this.previewHat = previewHat;
/*      */   }
/*      */   
/*      */   public Cosmetic getPreviewBag() {
/*  168 */     return this.previewBag;
/*      */   }
/*      */   
/*      */   public void setPreviewBag(Cosmetic previewBag) {
/*  172 */     this.previewBag = previewBag;
/*      */   }
/*      */   
/*      */   public Cosmetic getPreviewWStick() {
/*  176 */     return this.previewWStick;
/*      */   }
/*      */   
/*      */   public void setPreviewWStick(Cosmetic previewWStick) {
/*  180 */     this.previewWStick = previewWStick;
/*      */   }
/*      */   
/*      */   public static void removePlayer(PlayerData player) {
/*  184 */     players.remove(player.getUniqueId());
/*      */   }
/*      */   
/*      */   public Hat getHat() {
/*  188 */     return this.hat;
/*      */   }
/*      */   
/*      */   public void setHat(Hat hat) {
/*  192 */     this.hat = hat;
/*  193 */     if (this.hat == null)
/*  194 */       return;  this.hat.setPlayer(this.offlinePlayer.getPlayer());
/*  195 */     activeHat();
/*      */   }
/*      */   
/*      */   public Cosmetic getBag() {
/*  199 */     return this.bag;
/*      */   }
/*      */   
/*      */   public void setBag(Cosmetic bag) {
/*  203 */     this.bag = bag;
/*  204 */     if (this.bag == null)
/*  205 */       return;  this.bag.setPlayer(this.offlinePlayer.getPlayer());
/*      */   }
/*      */   
/*      */   public WStick getWStick() {
/*  209 */     return this.wStick;
/*      */   }
/*      */   
/*      */   public void setWStick(WStick wStick) {
/*  213 */     this.wStick = wStick;
/*  214 */     if (this.wStick == null)
/*  215 */       return;  this.wStick.setPlayer(this.offlinePlayer.getPlayer());
/*  216 */     activeWStick();
/*      */   }
/*      */   
/*      */   public Cosmetic getBalloon() {
/*  220 */     return this.balloon;
/*      */   }
/*      */   
/*      */   public void setBalloon(Cosmetic balloon) {
/*  224 */     this.balloon = balloon;
/*  225 */     if (this.balloon == null)
/*  226 */       return;  this.balloon.setPlayer(this.offlinePlayer.getPlayer());
/*      */   }
/*      */   
/*      */   public Cosmetic getSpray() {
/*  230 */     return this.spray;
/*      */   }
/*      */   
/*      */   public void setSpray(Cosmetic spray) {
/*  234 */     this.spray = spray;
/*  235 */     if (this.spray == null)
/*  236 */       return;  this.spray.setPlayer(this.offlinePlayer.getPlayer());
/*      */   }
/*      */   
/*      */   public void removeCosmetic(String cosmeticId) {
/*  240 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  241 */     Cosmetic cosmetic = getCosmeticById(cosmeticId);
/*  242 */     if (cosmetic == null) {
/*  243 */       cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
/*  244 */       if (cosmetic == null)
/*      */         return; 
/*  246 */     }  CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(getOfflinePlayer().getPlayer(), cosmetic);
/*  247 */     MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
/*  248 */     if (event.isCancelled())
/*  249 */       return;  if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
/*  250 */       removeEquip(cosmeticId);
/*  251 */       removePreviewEquip(cosmeticId);
/*  252 */       plugin.getLuckPerms().removePermission(getUniqueId(), cosmetic.getPermission());
/*      */       return;
/*      */     } 
/*  255 */     removeEquip(cosmeticId);
/*  256 */     removePreviewEquip(cosmeticId);
/*  257 */     this.cosmetics.remove(cosmeticId);
/*      */   }
/*      */   
/*      */   public void setCosmetic(Cosmetic cosmetic) {
/*  261 */     if (cosmetic == null)
/*      */       return; 
/*  263 */     switch (cosmetic.getCosmeticType()) {
/*      */       case HAT:
/*  265 */         clearHat();
/*  266 */         setHat((Hat)cosmetic);
/*      */         break;
/*      */       case BAG:
/*  269 */         clearBag();
/*  270 */         setBag(cosmetic);
/*      */         break;
/*      */       case WALKING_STICK:
/*  273 */         clearWStick();
/*  274 */         setWStick((WStick)cosmetic);
/*      */         break;
/*      */       case BALLOON:
/*  277 */         clearBalloon();
/*  278 */         setBalloon(cosmetic);
/*      */         break;
/*      */       case SPRAY:
/*  281 */         clearSpray();
/*  282 */         setSpray(cosmetic);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setCosmetic(CosmeticType cosmeticType, Cosmetic cosmetic) {
/*  288 */     switch (cosmeticType) {
/*      */       case HAT:
/*  290 */         clearHat();
/*  291 */         setHat((Hat)cosmetic);
/*      */         break;
/*      */       case BAG:
/*  294 */         clearBag();
/*  295 */         setBag(cosmetic);
/*      */         break;
/*      */       case WALKING_STICK:
/*  298 */         clearWStick();
/*  299 */         setWStick((WStick)cosmetic);
/*      */         break;
/*      */       case BALLOON:
/*  302 */         clearBalloon();
/*  303 */         setBalloon(cosmetic);
/*      */         break;
/*      */       case SPRAY:
/*  306 */         clearSpray();
/*  307 */         setSpray(cosmetic);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setPreviewCosmetic(Cosmetic cosmetic) {
/*  313 */     if (cosmetic == null)
/*      */       return; 
/*  315 */     switch (cosmetic.getCosmeticType()) {
/*      */       case HAT:
/*  317 */         clearPreviewHat();
/*  318 */         setPreviewHat(cosmetic);
/*  319 */         activePreviewHat();
/*      */         break;
/*      */       case BAG:
/*  322 */         clearPreviewBag();
/*  323 */         setPreviewBag(cosmetic);
/*  324 */         activePreviewBag();
/*      */         break;
/*      */       case WALKING_STICK:
/*  327 */         clearPreviewWStick();
/*  328 */         setPreviewWStick(cosmetic);
/*  329 */         activePreviewWStick();
/*      */         break;
/*      */       case BALLOON:
/*  332 */         clearPreviewBalloon();
/*  333 */         setPreviewBalloon(cosmetic);
/*  334 */         activePreviewBalloon();
/*      */         break;
/*      */       case SPRAY:
/*  337 */         clearPreviewSpray();
/*  338 */         setPreviewSpray(cosmetic);
/*  339 */         activePreviewSpray();
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public UUID getUniqueId() {
/*  345 */     return this.uniqueId;
/*      */   }
/*      */   
/*      */   public OfflinePlayer getOfflinePlayer() {
/*  349 */     return this.offlinePlayer;
/*      */   }
/*      */   
/*      */   public Map<String, Cosmetic> getCosmetics() {
/*  353 */     return this.cosmetics;
/*      */   }
/*      */   
/*      */   public List<Cosmetic> getCosmeticsPerm() {
/*  357 */     List<Cosmetic> cosmetics = new ArrayList<>();
/*  358 */     for (Cosmetic cosmetic : Cosmetic.cosmetics.values()) {
/*  359 */       if (!cosmetic.hasPermission(getOfflinePlayer().getPlayer()))
/*  360 */         continue;  cosmetics.add(cosmetic);
/*      */     } 
/*  362 */     return cosmetics;
/*      */   }
/*      */   
/*      */   public Cosmetic getCosmeticByName(String name) {
/*  366 */     for (Cosmetic cosmetic : this.cosmetics.values()) {
/*  367 */       if (cosmetic.getName().equalsIgnoreCase(name)) {
/*  368 */         return cosmetic;
/*      */       }
/*      */     } 
/*  371 */     return null;
/*      */   }
/*      */   
/*      */   public Cosmetic getCosmeticById(String id) {
/*  375 */     if (id == null || id.isEmpty()) return null; 
/*  376 */     return this.cosmetics.get(id);
/*      */   }
/*      */   
/*      */   public boolean hasCosmeticById(String id) {
/*  380 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  381 */     Cosmetic cosmetic = Cosmetic.getCosmetic(id);
/*  382 */     if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty()) {
/*  383 */       return cosmetic.hasPermission(getOfflinePlayer().getPlayer());
/*      */     }
/*  385 */     return this.cosmetics.containsKey(id);
/*      */   }
/*      */   
/*      */   public String saveCosmetics() {
/*  389 */     List<String> ids = new ArrayList<>();
/*  390 */     if (MagicCosmetics.getInstance().isPermissions()) {
/*  391 */       for (Cosmetic cosmetic : cosmeticsInUse()) {
/*  392 */         if (ids.contains(cosmetic.getId()))
/*  393 */           continue;  if (cosmetic.getColor() != null) {
/*  394 */           ids.add(cosmetic.getId() + "|" + cosmetic.getId());
/*      */           continue;
/*      */         } 
/*  397 */         ids.add(cosmetic.getId());
/*      */       } 
/*  399 */       if (ids.isEmpty()) return ""; 
/*  400 */       return String.join(",", (Iterable)ids);
/*      */     } 
/*  402 */     for (Cosmetic cosmetic : this.cosmetics.values()) {
/*  403 */       if (ids.contains(cosmetic.getId()))
/*  404 */         continue;  if (cosmetic.getColor() != null) {
/*  405 */         ids.add(cosmetic.getId() + "|" + cosmetic.getId());
/*      */         continue;
/*      */       } 
/*  408 */       ids.add(cosmetic.getId());
/*      */     } 
/*  410 */     if (ids.isEmpty()) return ""; 
/*  411 */     return String.join(",", (Iterable)ids);
/*      */   }
/*      */   
/*      */   public void loadCosmetics(String ids) {
/*  415 */     if (ids.isEmpty())
/*  416 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  417 */     List<String> cosmetics = new ArrayList<>(Arrays.asList(ids.split(",")));
/*  418 */     this.cosmetics.clear();
/*  419 */     for (String cosmetic : cosmetics) {
/*  420 */       String[] color = cosmetic.split("\\|");
/*  421 */       if (color.length > 1) {
/*  422 */         Cosmetic cosmetic2 = Cosmetic.getCloneCosmetic(color[0]);
/*  423 */         if (cosmetic2 == null || (
/*  424 */           plugin.isPermissions() && plugin.isLuckPerms() && plugin.getLuckPerms().isExpirePermission(this.offlinePlayer.getUniqueId(), cosmetic2.getPermission())))
/*      */           continue; 
/*  426 */         if (this.cosmetics.containsKey(color[0]))
/*  427 */           continue;  cosmetic2.setColor(Color.fromRGB(Integer.parseInt(color[1])));
/*  428 */         addCosmetic(cosmetic2);
/*      */         continue;
/*      */       } 
/*  431 */       Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(cosmetic);
/*  432 */       if (cosmetic1 == null || (
/*  433 */         plugin.isPermissions() && plugin.isLuckPerms() && plugin.getLuckPerms().isExpirePermission(this.offlinePlayer.getUniqueId(), cosmetic1.getPermission())))
/*      */         continue; 
/*  435 */       if (this.cosmetics.containsKey(cosmetic))
/*  436 */         continue;  addCosmetic(cosmetic1);
/*      */     }  } public void addCosmetic(Cosmetic cosmetic) { Hat hat, newHat; Bag bag, newBag;
/*      */     WStick wStick, newWStick;
/*      */     Balloon balloon, newBalloon;
/*      */     Spray spray, newSpray;
/*  441 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  442 */     if (cosmetic == null)
/*  443 */       return;  if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty() && plugin.isLuckPerms() && 
/*  444 */       !cosmetic.hasPermission(getOfflinePlayer().getPlayer())) {
/*  445 */       plugin.getLuckPerms().addPermission(getUniqueId(), cosmetic.getPermission());
/*      */     }
/*      */     
/*  448 */     Player player = this.offlinePlayer.getPlayer();
/*  449 */     switch (cosmetic.getCosmeticType()) {
/*      */       case HAT:
/*  451 */         hat = (Hat)cosmetic;
/*  452 */         newHat = new Hat(hat.getId(), hat.getName(), hat.getItemStack().clone(), hat.getModelData(), hat.isColored(), hat.getCosmeticType(), hat.getColor(), hat.isOverlaps(), hat.getPermission(), hat.isTexture(), hat.isHideMenu(), hat.isUseEmote(), hat.getOffSetY(), hat.getNamespacedKey());
/*  453 */         newHat.setColorBlocked(cosmetic.isColorBlocked());
/*  454 */         newHat.setPlayer(player);
/*  455 */         this.cosmetics.put(cosmetic.getId(), newHat);
/*      */         return;
/*      */       case BAG:
/*  458 */         bag = (Bag)cosmetic;
/*  459 */         newBag = new Bag(bag.getId(), bag.getName(), bag.getItemStack().clone(), bag.getModelData(), bag.getBagForMe(), bag.isColored(), bag.getSpace(), bag.getCosmeticType(), bag.getColor(), bag.getDistance(), bag.getPermission(), bag.isTexture(), bag.isHideMenu(), bag.getHeight(), bag.isUseEmote(), (bag.getBackPackEngine() != null) ? bag.getBackPackEngine().getClone() : null, bag.getNamespacedKey());
/*  460 */         newBag.setColorBlocked(cosmetic.isColorBlocked());
/*  461 */         newBag.setPlayer(player);
/*  462 */         this.cosmetics.put(cosmetic.getId(), newBag);
/*      */         return;
/*      */       case WALKING_STICK:
/*  465 */         wStick = (WStick)cosmetic;
/*  466 */         newWStick = new WStick(wStick.getId(), wStick.getName(), wStick.getItemStack().clone(), wStick.getModelData(), wStick.isColored(), wStick.getCosmeticType(), wStick.getColor(), wStick.getPermission(), wStick.isTexture(), wStick.isOverlaps(), wStick.isHideMenu(), wStick.isUseEmote(), wStick.getNamespacedKey());
/*  467 */         newWStick.setColorBlocked(cosmetic.isColorBlocked());
/*  468 */         newWStick.setPlayer(player);
/*  469 */         this.cosmetics.put(cosmetic.getId(), newWStick);
/*      */         break;
/*      */       case BALLOON:
/*  472 */         balloon = (Balloon)cosmetic;
/*  473 */         newBalloon = new Balloon(balloon.getId(), balloon.getName(), balloon.getItemStack().clone(), balloon.getModelData(), balloon.isColored(), balloon.getSpace(), balloon.getCosmeticType(), balloon.getColor(), balloon.isRotation(), balloon.getRotationType(), (balloon.getBalloonEngine() != null) ? balloon.getBalloonEngine().getClone() : null, (balloon.getBalloonIA() != null) ? balloon.getBalloonIA().getClone() : null, balloon.getDistance(), balloon.getPermission(), balloon.isTexture(), balloon.isBigHead(), balloon.isHideMenu(), balloon.isInvisibleLeash(), balloon.isUseEmote(), balloon.isInstantFollow(), balloon.getNamespacedKey());
/*  474 */         newBalloon.setColorBlocked(cosmetic.isColorBlocked());
/*  475 */         newBalloon.setPlayer(player);
/*  476 */         this.cosmetics.put(cosmetic.getId(), newBalloon);
/*      */         break;
/*      */       case SPRAY:
/*  479 */         spray = (Spray)cosmetic;
/*  480 */         newSpray = new Spray(spray.getId(), spray.getName(), spray.getItemStack().clone(), spray.getModelData(), spray.isColored(), spray.getCosmeticType(), spray.getColor(), spray.getPermission(), spray.isTexture(), spray.getImage(), spray.isItemImage(), spray.isHideMenu(), spray.isUseEmote(), spray.getNamespacedKey());
/*  481 */         newSpray.setColorBlocked(cosmetic.isColorBlocked());
/*  482 */         newSpray.setPlayer(player);
/*  483 */         this.cosmetics.put(cosmetic.getId(), newSpray);
/*      */         break;
/*      */     }  }
/*      */ 
/*      */   
/*      */   public void removeHat() {
/*  489 */     clearHat();
/*  490 */     this.hat = null;
/*      */   }
/*      */   
/*      */   public void removeBag() {
/*  494 */     clearBag();
/*  495 */     this.bag = null;
/*      */   }
/*      */   
/*      */   public void removeWStick() {
/*  499 */     clearWStick();
/*  500 */     this.wStick = null;
/*      */   }
/*      */   
/*      */   public void removeBalloon() {
/*  504 */     clearBalloon();
/*  505 */     this.balloon = null;
/*      */   }
/*      */   
/*      */   public void removeSpray() {
/*  509 */     clearSpray();
/*  510 */     this.spray = null;
/*      */   }
/*      */   
/*      */   public void removePreviewHat() {
/*  514 */     clearPreviewHat();
/*  515 */     this.previewHat = null;
/*      */   }
/*      */   
/*      */   public void removePreviewBag() {
/*  519 */     clearPreviewBag();
/*  520 */     this.previewBag = null;
/*      */   }
/*      */   
/*      */   public void removePreviewWStick() {
/*  524 */     clearPreviewWStick();
/*  525 */     this.previewWStick = null;
/*      */   }
/*      */   
/*      */   public void removePreviewBalloon() {
/*  529 */     clearPreviewBalloon();
/*  530 */     this.previewBalloon = null;
/*      */   }
/*      */   
/*      */   public void removePreviewSpray() {
/*  534 */     clearPreviewSpray();
/*  535 */     this.previewSpray = null;
/*      */   }
/*      */   
/*      */   public Cosmetic getEquip(CosmeticType cosmeticType) {
/*  539 */     switch (cosmeticType) {
/*      */       case HAT:
/*  541 */         if (this.hat == null) return null; 
/*  542 */         return (Cosmetic)this.hat;
/*      */       case BAG:
/*  544 */         if (this.bag == null) return null; 
/*  545 */         return this.bag;
/*      */       case WALKING_STICK:
/*  547 */         if (this.wStick == null) return null; 
/*  548 */         return (Cosmetic)this.wStick;
/*      */       case BALLOON:
/*  550 */         if (this.balloon == null) return null; 
/*  551 */         return this.balloon;
/*      */       case SPRAY:
/*  553 */         if (this.spray == null) return null; 
/*  554 */         return this.spray;
/*      */     } 
/*  556 */     return null;
/*      */   }
/*      */   
/*      */   public Cosmetic getEquip(String id) {
/*  560 */     if (this.hat != null && 
/*  561 */       this.hat.getId().equalsIgnoreCase(id)) {
/*  562 */       return (Cosmetic)this.hat;
/*      */     }
/*      */     
/*  565 */     if (this.bag != null && 
/*  566 */       this.bag.getId().equalsIgnoreCase(id)) {
/*  567 */       return this.bag;
/*      */     }
/*      */     
/*  570 */     if (this.wStick != null && 
/*  571 */       this.wStick.getId().equalsIgnoreCase(id)) {
/*  572 */       return (Cosmetic)this.wStick;
/*      */     }
/*      */     
/*  575 */     if (this.balloon != null && 
/*  576 */       this.balloon.getId().equalsIgnoreCase(id)) {
/*  577 */       return this.balloon;
/*      */     }
/*      */     
/*  580 */     if (this.spray != null && 
/*  581 */       this.spray.getId().equalsIgnoreCase(id)) {
/*  582 */       return this.spray;
/*      */     }
/*      */     
/*  585 */     return null;
/*      */   }
/*      */   
/*      */   public void removeEquip(String id) {
/*  589 */     if (this.hat != null && 
/*  590 */       this.hat.getId().equalsIgnoreCase(id)) {
/*  591 */       removeHat();
/*      */       
/*      */       return;
/*      */     } 
/*  595 */     if (this.bag != null && 
/*  596 */       this.bag.getId().equalsIgnoreCase(id)) {
/*  597 */       removeBag();
/*      */       
/*      */       return;
/*      */     } 
/*  601 */     if (this.wStick != null && 
/*  602 */       this.wStick.getId().equalsIgnoreCase(id)) {
/*  603 */       removeWStick();
/*      */       
/*      */       return;
/*      */     } 
/*  607 */     if (this.balloon != null && 
/*  608 */       this.balloon.getId().equalsIgnoreCase(id)) {
/*  609 */       removeBalloon();
/*      */     }
/*      */     
/*  612 */     if (this.spray != null && 
/*  613 */       this.spray.getId().equalsIgnoreCase(id)) {
/*  614 */       removeSpray();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeEquip(CosmeticType cosmeticType) {
/*  619 */     switch (cosmeticType) {
/*      */       case HAT:
/*  621 */         removeHat();
/*      */         return;
/*      */       case BAG:
/*  624 */         removeBag();
/*      */         return;
/*      */       case WALKING_STICK:
/*  627 */         removeWStick();
/*      */         return;
/*      */       case BALLOON:
/*  630 */         removeBalloon();
/*      */         return;
/*      */       case SPRAY:
/*  633 */         removeSpray();
/*      */         break;
/*      */     } 
/*      */   }
/*      */   public void removePreviewEquip(String id) {
/*  638 */     if (this.previewHat != null && 
/*  639 */       this.previewHat.getId().equalsIgnoreCase(id)) {
/*  640 */       removePreviewHat();
/*      */     }
/*      */     
/*  643 */     if (this.previewBag != null && 
/*  644 */       this.previewBag.getId().equalsIgnoreCase(id)) {
/*  645 */       removePreviewBag();
/*      */     }
/*      */     
/*  648 */     if (this.previewWStick != null && 
/*  649 */       this.previewWStick.getId().equalsIgnoreCase(id)) {
/*  650 */       removePreviewWStick();
/*      */     }
/*      */     
/*  653 */     if (this.previewBalloon != null && 
/*  654 */       this.previewBalloon.getId().equalsIgnoreCase(id)) {
/*  655 */       removePreviewBalloon();
/*      */     }
/*      */     
/*  658 */     if (this.previewSpray != null && 
/*  659 */       this.previewSpray.getId().equalsIgnoreCase(id)) {
/*  660 */       removePreviewSpray();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearHat() {
/*  666 */     Player player = getOfflinePlayer().getPlayer();
/*  667 */     if (player == null)
/*  668 */       return;  if (this.hat == null)
/*  669 */       return;  if (this.hat.isRemovedLendEntity())
/*  670 */       return;  this.hat.remove();
/*      */   }
/*      */   
/*      */   public void clearBag() {
/*  674 */     Player player = getOfflinePlayer().getPlayer();
/*  675 */     if (player == null)
/*  676 */       return;  if (this.bag == null)
/*  677 */       return;  if (this.bag.isRemovedLendEntity())
/*  678 */       return;  this.bag.remove();
/*      */   }
/*      */   
/*      */   public void clearWStick() {
/*  682 */     Player player = getOfflinePlayer().getPlayer();
/*  683 */     if (player == null)
/*  684 */       return;  if (this.wStick == null)
/*  685 */       return;  if (this.wStick.isRemovedLendEntity())
/*  686 */       return;  this.wStick.remove();
/*      */   }
/*      */   
/*      */   public void clearBalloon() {
/*  690 */     Player player = getOfflinePlayer().getPlayer();
/*  691 */     if (player == null)
/*  692 */       return;  if (this.balloon == null)
/*  693 */       return;  if (this.balloon.isRemovedLendEntity())
/*  694 */       return;  this.balloon.remove();
/*      */   }
/*      */   
/*      */   public void clearSpray() {
/*  698 */     Player player = getOfflinePlayer().getPlayer();
/*  699 */     if (player == null) {
/*      */       return;
/*      */     }
/*  702 */     if (this.spray == null) {
/*      */       return;
/*      */     }
/*  705 */     if (this.spray.isRemovedLendEntity())
/*  706 */       return;  this.spray.remove();
/*      */   }
/*      */   
/*      */   public void clearPreviewHat() {
/*  710 */     Player player = getOfflinePlayer().getPlayer();
/*  711 */     if (player == null) {
/*      */       return;
/*      */     }
/*  714 */     if (this.previewHat == null) {
/*      */       return;
/*      */     }
/*  717 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  718 */     if (npc == null)
/*  719 */       return;  npc.equipNPC(player, ItemSlot.HELMET, XMaterial.AIR.parseItem());
/*      */   }
/*      */   
/*      */   public void clearPreviewBag() {
/*  723 */     Player player = getOfflinePlayer().getPlayer();
/*  724 */     if (player == null) {
/*      */       return;
/*      */     }
/*  727 */     if (this.previewBag == null) {
/*      */       return;
/*      */     }
/*  730 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  731 */     if (npc == null)
/*  732 */       return;  npc.armorStandSetItem(player, XMaterial.AIR.parseItem());
/*      */   }
/*      */   
/*      */   public void clearPreviewWStick() {
/*  736 */     Player player = getOfflinePlayer().getPlayer();
/*  737 */     if (player == null) {
/*      */       return;
/*      */     }
/*  740 */     if (this.previewWStick == null) {
/*      */       return;
/*      */     }
/*  743 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  744 */     if (npc == null)
/*  745 */       return;  npc.equipNPC(player, ItemSlot.OFF_HAND, XMaterial.AIR.parseItem());
/*      */   }
/*      */   
/*      */   public void clearPreviewBalloon() {
/*  749 */     Player player = getOfflinePlayer().getPlayer();
/*  750 */     if (player == null) {
/*      */       return;
/*      */     }
/*  753 */     if (this.previewBalloon == null) {
/*      */       return;
/*      */     }
/*  756 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  757 */     if (npc == null)
/*  758 */       return;  npc.removeBalloon(player);
/*      */   }
/*      */   
/*      */   public void clearPreviewSpray() {
/*  762 */     Player player = getOfflinePlayer().getPlayer();
/*  763 */     if (player == null) {
/*      */       return;
/*      */     }
/*  766 */     if (this.previewSpray == null) {
/*      */       return;
/*      */     }
/*  769 */     this.previewSpray.remove();
/*      */   }
/*      */   
/*      */   public void activeHat() {
/*  773 */     Player player = getOfflinePlayer().getPlayer();
/*  774 */     if (player == null) {
/*      */       return;
/*      */     }
/*  777 */     if (this.hat == null) {
/*      */       return;
/*      */     }
/*  780 */     if (this.isZone)
/*  781 */       return;  if (MagicCosmetics.getInstance().isItemsAdder() && 
/*  782 */       MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.hat.isUseEmote()) {
/*  783 */       this.hat.update();
/*      */       
/*      */       return;
/*      */     } 
/*  787 */     if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
/*  788 */       clearHat();
/*      */       return;
/*      */     } 
/*  791 */     this.hat.update();
/*      */   }
/*      */   
/*      */   public void activeBag() {
/*  795 */     Player player = getOfflinePlayer().getPlayer();
/*  796 */     if (player == null) {
/*      */       return;
/*      */     }
/*  799 */     if (this.bag == null) {
/*      */       return;
/*      */     }
/*  802 */     if (this.isZone)
/*  803 */       return;  if (MagicCosmetics.getInstance().isItemsAdder() && 
/*  804 */       MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.bag.isUseEmote()) {
/*  805 */       this.bag.update();
/*      */       
/*      */       return;
/*      */     } 
/*  809 */     Material material = player.getLocation().getBlock().getType();
/*  810 */     if (player.getPose() == Pose.SLEEPING || player.getPose() == Pose.SWIMMING || player.isGliding() || player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY) || material == Material.NETHER_PORTAL || material == Material.END_PORTAL) {
/*  811 */       clearBag();
/*      */       return;
/*      */     } 
/*  814 */     this.bag.update();
/*      */   }
/*      */   
/*      */   public void activeWStick() {
/*  818 */     Player player = getOfflinePlayer().getPlayer();
/*  819 */     if (player == null) {
/*      */       return;
/*      */     }
/*  822 */     if (this.wStick == null) {
/*      */       return;
/*      */     }
/*  825 */     if (this.isZone)
/*  826 */       return;  if (MagicCosmetics.getInstance().isItemsAdder() && 
/*  827 */       MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.wStick.isUseEmote()) {
/*  828 */       this.wStick.update();
/*      */       
/*      */       return;
/*      */     } 
/*  832 */     if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
/*  833 */       clearWStick();
/*      */       return;
/*      */     } 
/*  836 */     this.wStick.update();
/*      */   }
/*      */   
/*      */   public void activeBalloon() {
/*  840 */     Player player = getOfflinePlayer().getPlayer();
/*  841 */     if (player == null) {
/*      */       return;
/*      */     }
/*  844 */     if (this.balloon == null) {
/*      */       return;
/*      */     }
/*  847 */     if (this.isZone)
/*  848 */       return;  if (MagicCosmetics.getInstance().isItemsAdder() && 
/*  849 */       MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.balloon.isUseEmote()) {
/*  850 */       this.balloon.update();
/*      */       
/*      */       return;
/*      */     } 
/*  854 */     if (this.balloon.getLendEntity() != null) {
/*  855 */       this.balloon.lendToEntity();
/*      */       return;
/*      */     } 
/*  858 */     this.balloon.update();
/*      */   }
/*      */   
/*      */   public void previewDraw() {
/*  862 */     Player player = getOfflinePlayer().getPlayer();
/*  863 */     if (player == null) {
/*      */       return;
/*      */     }
/*  866 */     if (this.previewSpray == null) {
/*      */       return;
/*      */     }
/*  869 */     Zone zone = getZone();
/*  870 */     if (zone == null)
/*  871 */       return;  if (zone.getSprayFace() == null || zone.getSprayLoc() == null)
/*  872 */       return;  ((Spray)this.previewSpray).draw(player, zone.getSprayFace(), zone.getSprayLoc(), zone.getRotation());
/*      */   }
/*      */   
/*      */   public void draw(SprayKeys key) {
/*  876 */     Player player = getOfflinePlayer().getPlayer();
/*  877 */     if (player == null) {
/*      */       return;
/*      */     }
/*  880 */     if (this.spray == null) {
/*      */       return;
/*      */     }
/*  883 */     if (this.isZone)
/*  884 */       return;  ((Spray)this.spray).draw(player, key);
/*      */   }
/*      */   
/*      */   public void activeSpray() {
/*  888 */     Player player = getOfflinePlayer().getPlayer();
/*  889 */     if (player == null) {
/*      */       return;
/*      */     }
/*  892 */     if (this.spray == null) {
/*      */       return;
/*      */     }
/*  895 */     if (this.isZone)
/*  896 */       return;  this.spray.update();
/*      */   }
/*      */   
/*      */   public void activePreviewHat() {
/*  900 */     Player player = getOfflinePlayer().getPlayer();
/*  901 */     if (player == null) {
/*      */       return;
/*      */     }
/*  904 */     if (this.previewHat == null) {
/*      */       return;
/*      */     }
/*  907 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  908 */     if (npc == null)
/*  909 */       return;  npc.equipNPC(player, ItemSlot.HELMET, this.previewHat.getItemColor(player));
/*      */   }
/*      */   
/*      */   public void activePreviewBag() {
/*  913 */     Player player = getOfflinePlayer().getPlayer();
/*  914 */     if (player == null) {
/*      */       return;
/*      */     }
/*  917 */     if (this.previewBag == null) {
/*      */       return;
/*      */     }
/*  920 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  921 */     if (npc == null)
/*  922 */       return;  npc.armorStandSetItem(player, this.previewBag.getItemColor(player));
/*      */   }
/*      */   
/*      */   public void activePreviewWStick() {
/*  926 */     Player player = getOfflinePlayer().getPlayer();
/*  927 */     if (player == null) {
/*      */       return;
/*      */     }
/*  930 */     if (this.previewWStick == null) {
/*      */       return;
/*      */     }
/*  933 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  934 */     if (npc == null)
/*  935 */       return;  npc.equipNPC(player, ItemSlot.OFF_HAND, this.previewWStick.getItemColor(player));
/*      */   }
/*      */   
/*      */   public void activePB() {
/*  939 */     Player player = getOfflinePlayer().getPlayer();
/*  940 */     if (player == null) {
/*      */       return;
/*      */     }
/*  943 */     if (this.previewBalloon == null) {
/*      */       return;
/*      */     }
/*  946 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  947 */     if (npc == null)
/*  948 */       return;  npc.animation(player);
/*      */   }
/*      */   public void activePreviewBalloon() {
/*      */     Location location;
/*  952 */     Player player = getOfflinePlayer().getPlayer();
/*  953 */     if (player == null) {
/*      */       return;
/*      */     }
/*  956 */     if (this.previewBalloon == null) {
/*      */       return;
/*      */     }
/*  959 */     NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
/*  960 */     if (npc == null)
/*  961 */       return;  Zone zone = getZone();
/*  962 */     if (zone == null)
/*      */       return; 
/*  964 */     if (zone.getBalloon() == null) {
/*  965 */       location = npc.getEntity().getLocation();
/*      */     } else {
/*  967 */       location = zone.getBalloon();
/*      */     } 
/*  969 */     npc.balloonNPC(player, location, this.previewBalloon.getItemColor(player), ((Balloon)this.previewBalloon).isBigHead());
/*      */   }
/*      */   
/*      */   public void activePreviewSpray() {
/*  973 */     previewDraw();
/*      */   }
/*      */ 
/*      */   
/*      */   public void activeCosmetics() {
/*  978 */     activeBag();
/*      */     
/*  980 */     activePB();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearCosmeticsInUse(boolean inventory) {
/*  985 */     if (inventory)
/*  986 */       clearCosmeticsInventory(); 
/*  987 */     clearBag();
/*  988 */     clearBalloon();
/*  989 */     clearSpray();
/*      */   }
/*      */   
/*      */   public void clearCosmeticsToSaveData() {
/*  993 */     if (this.hat != null) {
/*  994 */       this.hat.clearClose();
/*      */     }
/*  996 */     if (this.wStick != null)
/*  997 */       this.wStick.clearClose(); 
/*  998 */     if (this.balloon != null)
/*  999 */       this.balloon.clearClose(); 
/* 1000 */     if (this.bag != null)
/* 1001 */       this.bag.clearClose(); 
/* 1002 */     if (this.spray != null)
/* 1003 */       this.spray.clearClose(); 
/*      */   }
/*      */   
/*      */   public void forceClearCosmeticsInventory() {
/* 1007 */     if (this.hat != null)
/* 1008 */       this.hat.forceRemove(); 
/* 1009 */     if (this.wStick != null)
/* 1010 */       this.wStick.forceRemove(); 
/*      */   }
/*      */   
/*      */   public void clearCosmeticsInventory() {
/* 1014 */     clearHat();
/* 1015 */     clearWStick();
/*      */   }
/*      */   
/*      */   public void activeCosmeticsInventory() {
/* 1019 */     activeHat();
/* 1020 */     activeWStick();
/*      */   }
/*      */   
/*      */   public void setZone(Zone zone) {
/* 1024 */     this.zone = zone;
/*      */   }
/*      */   
/*      */   public Zone getZone() {
/* 1028 */     return this.zone;
/*      */   }
/*      */   
/*      */   public boolean isSneak() {
/* 1032 */     return this.sneak;
/*      */   }
/*      */   
/*      */   public void setSneak(boolean sneak) {
/* 1036 */     this.sneak = sneak;
/*      */   }
/*      */   
/*      */   public boolean isZone() {
/* 1040 */     return this.isZone;
/*      */   }
/*      */   
/*      */   public boolean removeHelmet() {
/* 1044 */     if (!getOfflinePlayer().isOnline()) return false; 
/* 1045 */     Player player = getOfflinePlayer().getPlayer();
/* 1046 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1047 */     ItemStack helmet = player.getInventory().getHelmet();
/* 1048 */     if (this.hat == null) {
/* 1049 */       if (helmet != null && 
/* 1050 */         player.getInventory().firstEmpty() == -1) {
/* 1051 */         Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
/* 1052 */         Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1053 */         return false;
/*      */       } 
/*      */       
/* 1056 */       return true;
/*      */     } 
/* 1058 */     if (helmet != null && 
/* 1059 */       player.getInventory().firstEmpty() == -1) {
/* 1060 */       Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
/* 1061 */       Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1062 */       return false;
/*      */     } 
/*      */     
/* 1065 */     ItemStack savedItem = this.hat.leftItemAndGet();
/* 1066 */     if (savedItem != null)
/* 1067 */       player.getInventory().addItem(new ItemStack[] { savedItem }); 
/* 1068 */     return true;
/*      */   }
/*      */   
/*      */   public boolean removeOffHand() {
/* 1072 */     if (!getOfflinePlayer().isOnline()) return false; 
/* 1073 */     Player player = getOfflinePlayer().getPlayer();
/* 1074 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1075 */     ItemStack offHand = player.getInventory().getItemInOffHand();
/* 1076 */     if (this.wStick == null) {
/* 1077 */       if (!offHand.getType().isAir() && 
/* 1078 */         player.getInventory().firstEmpty() == -1) {
/* 1079 */         Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
/* 1080 */         Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1081 */         return false;
/*      */       } 
/*      */       
/* 1084 */       return true;
/*      */     } 
/* 1086 */     if (!offHand.getType().isAir() && 
/* 1087 */       player.getInventory().firstEmpty() == -1) {
/* 1088 */       Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
/* 1089 */       Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1090 */       return false;
/*      */     } 
/*      */     
/* 1093 */     ItemStack savedItem = this.wStick.leftItemAndGet();
/* 1094 */     if (savedItem != null)
/* 1095 */       player.getInventory().addItem(new ItemStack[] { savedItem }); 
/* 1096 */     return true;
/*      */   }
/*      */   
/*      */   public void enterZone() {
/* 1100 */     Zone zone = getZone();
/* 1101 */     if (zone == null) {
/* 1102 */       for (Zone z : Zone.zones.values()) {
/* 1103 */         if (!z.isInZone(getOfflinePlayer().getPlayer().getLocation().getBlock()))
/* 1104 */           continue;  setZone(z);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1109 */     if (!zone.isActive()) {
/* 1110 */       this.isZone = false;
/*      */       return;
/*      */     } 
/* 1113 */     Player player = getOfflinePlayer().getPlayer();
/* 1114 */     if (!getOfflinePlayer().isOnline())
/* 1115 */       return;  if (this.isZone) {
/* 1116 */       if (this.spectator && 
/* 1117 */         !player.getLocation().equals(zone.getEnter())) {
/* 1118 */         player.teleport(zone.getEnter());
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/* 1123 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1124 */     ZoneEnterEvent event = new ZoneEnterEvent(player, zone);
/* 1125 */     plugin.getServer().getPluginManager().callEvent((Event)event);
/* 1126 */     if ((plugin.getMagicCrates() != null && plugin.getMagicCrates().hasInCrate(player)) || event.isCancelled()) {
/* 1127 */       setZone((Zone)null);
/* 1128 */       player.teleport(zone.getExit());
/*      */       return;
/*      */     } 
/* 1131 */     this.gameMode = player.getGameMode();
/* 1132 */     Utils.sendSound(player, Sound.getSound("on_enter_zone"));
/* 1133 */     plugin.getVersion().setSpectator(player);
/* 1134 */     String title = plugin.getMessages().getString("title-zone.enter");
/* 1135 */     if (player.getGameMode() != GameMode.SPECTATOR) {
/* 1136 */       exitZone();
/*      */       return;
/*      */     } 
/* 1139 */     if (plugin.isItemsAdder()) {
/* 1140 */       if (plugin.getItemsAdder().hasEmote(player)) {
/* 1141 */         plugin.getItemsAdder().stopEmote(player);
/*      */       }
/* 1143 */       title = plugin.getItemsAdder().replaceFontImages(title);
/*      */     } 
/* 1145 */     if (plugin.isOraxen()) {
/* 1146 */       title = plugin.getOraxen().replaceFontImages(title);
/*      */     }
/*      */     
/* 1149 */     if (!removeHelmet()) {
/* 1150 */       ZoneExitEvent exitEvent = new ZoneExitEvent(player, zone, Reason.ITEM_IN_HELMET);
/* 1151 */       plugin.getServer().getPluginManager().callEvent((Event)exitEvent);
/* 1152 */       this.isZone = false;
/* 1153 */       this.sneak = false;
/* 1154 */       this.spectator = false;
/* 1155 */       player.teleport(zone.getExit());
/* 1156 */       setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
/* 1157 */       player.setFlySpeed(getSpeedFly());
/* 1158 */       plugin.getVersion().setCamera(player, (Entity)player);
/* 1159 */       setZone((Zone)null);
/* 1160 */       if (plugin.gameMode == null) {
/* 1161 */         player.setGameMode(this.gameMode);
/*      */       } else {
/* 1163 */         player.setGameMode(plugin.gameMode);
/*      */       } 
/*      */       return;
/*      */     } 
/* 1167 */     if (!removeOffHand()) {
/* 1168 */       ZoneExitEvent exitEvent = new ZoneExitEvent(player, zone, Reason.ITEM_IN_OFF_HAND);
/* 1169 */       plugin.getServer().getPluginManager().callEvent((Event)exitEvent);
/* 1170 */       this.isZone = false;
/* 1171 */       this.sneak = false;
/* 1172 */       this.spectator = false;
/* 1173 */       player.teleport(zone.getExit());
/* 1174 */       setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
/* 1175 */       player.setFlySpeed(getSpeedFly());
/* 1176 */       plugin.getVersion().setCamera(player, (Entity)player);
/* 1177 */       setZone((Zone)null);
/* 1178 */       if (plugin.gameMode == null) {
/* 1179 */         player.setGameMode(this.gameMode);
/*      */       } else {
/* 1181 */         player.setGameMode(plugin.gameMode);
/*      */       } 
/*      */       return;
/*      */     } 
/* 1185 */     player.sendTitle(title, "", 15, 7, 15);
/* 1186 */     clearCosmeticsInUse(true);
/* 1187 */     for (BossBar bossBar : plugin.getBossBar()) {
/* 1188 */       if (bossBar.getPlayers().contains(player))
/* 1189 */         continue;  bossBar.addPlayer(player);
/*      */     } 
/* 1191 */     plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, task -> { if (this.hat != null) this.hat.setHideCosmetic(true);  if (this.wStick != null) this.wStick.setHideCosmetic(true);  saveItems(); if (player.getGameMode() == GameMode.SPECTATOR) { player.teleport(zone.getEnter()); setSpeedFly(player.getFlySpeed()); player.setFlySpeed(0.0F); this.spectator = true; }  plugin.getVersion().createNPC(player, zone.getNpc()); plugin.getVersion().getNPC(player).spawnPunch(player, zone.getEnter()); setPreviewCosmetic((Cosmetic)this.hat); setPreviewCosmetic(this.bag); setPreviewCosmetic((Cosmetic)this.wStick); setPreviewCosmetic(this.balloon); setPreviewCosmetic(this.spray); if (plugin.getCosmeticsManager().npcTaskStopped()) plugin.getCosmeticsManager().reRunTasks();  }12L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1214 */     this.isZone = true;
/*      */   }
/*      */   
/*      */   public void exitZoneSync() {
/* 1218 */     Zone zone = getZone();
/* 1219 */     if (zone == null)
/* 1220 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1221 */     Player player = getOfflinePlayer().getPlayer();
/* 1222 */     Utils.showPlayer(player);
/* 1223 */     this.sneak = false;
/* 1224 */     this.spectator = false;
/* 1225 */     this.isZone = false;
/* 1226 */     for (BossBar bossBar : plugin.getBossBar()) {
/* 1227 */       bossBar.removePlayer(player);
/*      */     }
/* 1229 */     loadItems();
/* 1230 */     if (plugin.gameMode == null) {
/* 1231 */       player.setGameMode(this.gameMode);
/*      */     } else {
/* 1233 */       player.setGameMode(plugin.gameMode);
/*      */     } 
/* 1235 */     setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
/* 1236 */     player.setFlySpeed(getSpeedFly());
/* 1237 */     player.teleport(zone.getExit());
/* 1238 */     plugin.getVersion().removeNPC(player);
/* 1239 */     setZone((Zone)null);
/*      */   }
/*      */   
/*      */   public void exitZone() {
/* 1243 */     Zone zone = getZone();
/* 1244 */     if (zone == null)
/* 1245 */       return;  if (!this.isZone)
/* 1246 */       return;  if (this.sneak)
/* 1247 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1248 */     Player player = getOfflinePlayer().getPlayer();
/* 1249 */     ZoneExitEvent event = new ZoneExitEvent(player, zone, Reason.NORMAL);
/* 1250 */     plugin.getServer().getPluginManager().callEvent((Event)event);
/* 1251 */     if (event.isCancelled())
/* 1252 */       return;  String title = plugin.getMessages().getString("title-zone.exit");
/* 1253 */     if (plugin.isItemsAdder()) {
/* 1254 */       title = plugin.getItemsAdder().replaceFontImages(title);
/*      */     }
/* 1256 */     if (plugin.isOraxen()) {
/* 1257 */       title = plugin.getOraxen().replaceFontImages(title);
/*      */     }
/* 1259 */     player.sendTitle(title, "", 15, 7, 15);
/* 1260 */     Utils.showPlayer(player);
/* 1261 */     this.sneak = true;
/* 1262 */     for (BossBar bossBar : plugin.getBossBar()) {
/* 1263 */       bossBar.removePlayer(player);
/*      */     }
/* 1265 */     plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, () -> { loadItems(); if (plugin.gameMode == null) { player.setGameMode(this.gameMode); } else { player.setGameMode(plugin.gameMode); }  Utils.sendSound(player, Sound.getSound("on_exit_zone")); plugin.getVersion().removeNPC(player); int count = 0; if ((this.previewHat != null && this.previewHat.isColorBlocked()) || (this.previewBag != null && this.previewBag.isColorBlocked()) || (this.previewWStick != null && this.previewWStick.isColorBlocked()) || (this.previewBalloon != null && this.previewBalloon.isColorBlocked()) || (this.previewSpray != null && this.previewSpray.isColorBlocked())) { this.isZone = false; this.sneak = false; this.spectator = false; setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly()); player.setFlySpeed(getSpeedFly()); plugin.getVersion().setCamera(player, (Entity)player); player.teleport(zone.getExit()); setZone((Zone)null); Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix); return; }  if (plugin.isPermissions()) { if (this.previewHat != null) { if (!this.previewHat.hasPermission(player)) count++;  setPreviewHat(null); }  if (this.previewBag != null) { if (!this.previewBag.hasPermission(player)) count++;  setPreviewBag(null); }  if (this.previewWStick != null) { if (!this.previewWStick.hasPermission(player)) count++;  setPreviewWStick(null); }  if (this.previewBalloon != null) { if (!this.previewBalloon.hasPermission(player)) count++;  setPreviewBalloon(null); }  if (this.previewSpray != null) { if (!this.previewSpray.hasPermission(player)) count++;  setPreviewSpray(null); }  } else { if (this.previewHat != null) { if (getCosmeticById(this.previewHat.getId()) == null) count++;  setPreviewHat(null); }  if (this.previewBag != null) { if (getCosmeticById(this.previewBag.getId()) == null) count++;  setPreviewBag(null); }  if (this.previewWStick != null) { if (getCosmeticById(this.previewWStick.getId()) == null) count++;  setPreviewWStick(null); }  if (this.previewBalloon != null) { if (getCosmeticById(this.previewBalloon.getId()) == null) count++;  setPreviewBalloon(null); }  if (this.previewSpray != null) { if (getCosmeticById(this.previewSpray.getId()) == null) count++;  setPreviewSpray(null); }  }  if (count != 0) { if (count == 4) { Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix); this.isZone = false; this.sneak = false; this.spectator = false; setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly()); player.setFlySpeed(getSpeedFly()); plugin.getVersion().setCamera(player, (Entity)player); player.teleport(zone.getExit()); setZone((Zone)null); return; }  Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix); }  this.isZone = false; this.sneak = false; this.spectator = false; setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly()); player.setFlySpeed(getSpeedFly()); plugin.getVersion().setCamera(player, (Entity)player); player.teleport(zone.getExit()); setZone((Zone)null); if (this.hat != null) this.hat.setHideCosmetic(false);  if (this.wStick != null) this.wStick.setHideCosmetic(false);  }17L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getTokenInPlayer() {
/* 1381 */     Player player = getOfflinePlayer().getPlayer();
/* 1382 */     ItemStack mainHand = player.getInventory().getItemInMainHand();
/* 1383 */     ItemStack offHand = player.getInventory().getItemInOffHand();
/* 1384 */     if (!mainHand.getType().isAir()) {
/* 1385 */       Token token = Token.getTokenByItem(mainHand);
/* 1386 */       if (token == null) {
/* 1387 */         token = Token.getOldTokenByItem(mainHand);
/*      */       }
/* 1389 */       if (token != null)
/* 1390 */         return mainHand; 
/*      */     } 
/* 1392 */     if (!offHand.getType().isAir()) {
/* 1393 */       Token token = Token.getTokenByItem(offHand);
/* 1394 */       if (token == null && this.wStick != null && !this.wStick.isCosmetic(offHand)) {
/* 1395 */         token = Token.getOldTokenByItem(offHand);
/*      */       }
/* 1397 */       if (token != null)
/* 1398 */         return offHand; 
/*      */     }  int i;
/* 1400 */     for (i = 0; i < 8; ) {
/* 1401 */       ItemStack itemStack = player.getInventory().getItem(i);
/* 1402 */       Token token = Token.getTokenByItem(itemStack);
/* 1403 */       if (token == null) { i++; continue; }
/* 1404 */        return itemStack;
/*      */     } 
/* 1406 */     for (i = 0; i < 8; ) {
/* 1407 */       ItemStack itemStack = player.getInventory().getItem(i);
/* 1408 */       Token token = Token.getOldTokenByItem(itemStack);
/* 1409 */       if (token == null) { i++; continue; }
/* 1410 */        return itemStack;
/*      */     } 
/* 1412 */     return null;
/*      */   }
/*      */   
/*      */   public boolean removeTokenInPlayer() {
/* 1416 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1417 */     Player player = getOfflinePlayer().getPlayer();
/* 1418 */     PlayerData playerData = getPlayer((OfflinePlayer)player);
/* 1419 */     ItemStack mainHand = player.getInventory().getItemInMainHand();
/* 1420 */     ItemStack offHand = player.getInventory().getItemInOffHand();
/* 1421 */     if (mainHand.getType() != XMaterial.AIR.parseMaterial()) {
/* 1422 */       Token token = Token.getTokenByItem(mainHand);
/* 1423 */       if (token != null) {
/* 1424 */         Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
/* 1425 */         if (cosmetic == null) {
/* 1426 */           return false;
/*      */         }
/* 1428 */         if (mainHand.getAmount() < token.getItemStack().getAmount()) {
/* 1429 */           Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1430 */           return false;
/*      */         } 
/* 1432 */         if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
/* 1433 */           if (cosmetic.hasPermission(player)) {
/* 1434 */             Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1435 */             return false;
/*      */           }
/*      */         
/* 1438 */         } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
/* 1439 */           Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1440 */           return false;
/*      */         } 
/*      */         
/* 1443 */         if (mainHand.getAmount() > token.getItemStack().getAmount()) {
/* 1444 */           ItemStack newItem = token.getItemStack().clone();
/* 1445 */           newItem.setAmount(mainHand.getAmount() - token.getItemStack().getAmount());
/* 1446 */           player.getInventory().setItemInMainHand(newItem);
/* 1447 */           return true;
/*      */         } 
/* 1449 */         player.getInventory().setItemInMainHand(XMaterial.AIR.parseItem());
/* 1450 */         return true;
/*      */       } 
/*      */     } 
/* 1453 */     if (offHand.getType() != XMaterial.AIR.parseMaterial()) {
/* 1454 */       Token token = Token.getTokenByItem(offHand);
/* 1455 */       if (token != null) {
/* 1456 */         Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
/* 1457 */         if (cosmetic == null) {
/* 1458 */           return false;
/*      */         }
/* 1460 */         if (offHand.getAmount() < token.getItemStack().getAmount()) {
/* 1461 */           Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1462 */           return false;
/*      */         } 
/* 1464 */         if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
/* 1465 */           if (cosmetic.hasPermission(player)) {
/* 1466 */             Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1467 */             return false;
/*      */           }
/*      */         
/* 1470 */         } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
/* 1471 */           Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1472 */           return false;
/*      */         } 
/*      */         
/* 1475 */         if (offHand.getAmount() > token.getItemStack().getAmount() && token.getItemStack().getAmount() > 1) {
/* 1476 */           ItemStack newItem = token.getItemStack().clone();
/* 1477 */           newItem.setAmount(offHand.getAmount() - token.getItemStack().getAmount());
/* 1478 */           player.getInventory().setItemInOffHand(newItem);
/* 1479 */           return true;
/*      */         } 
/* 1481 */         player.getInventory().setItemInOffHand(XMaterial.AIR.parseItem());
/* 1482 */         return true;
/*      */       } 
/*      */     } 
/* 1485 */     for (int i = 0; i < 8; i++) {
/* 1486 */       ItemStack itemStack = player.getInventory().getItem(i);
/* 1487 */       if (itemStack != null) {
/* 1488 */         Token token = Token.getTokenByItem(itemStack);
/* 1489 */         if (token != null)
/* 1490 */         { Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
/* 1491 */           if (cosmetic == null) {
/* 1492 */             return false;
/*      */           }
/* 1494 */           if (itemStack.getAmount() < token.getItemStack().getAmount()) {
/* 1495 */             Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1496 */             return false;
/*      */           } 
/* 1498 */           if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
/* 1499 */             if (cosmetic.hasPermission(player)) {
/* 1500 */               Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1501 */               return false;
/*      */             }
/*      */           
/* 1504 */           } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
/* 1505 */             Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/* 1506 */             return false;
/*      */           } 
/*      */           
/* 1509 */           player.getInventory().removeItem(new ItemStack[] { token.getItemStack().clone() });
/* 1510 */           return true; } 
/*      */       } 
/* 1512 */     }  return false;
/*      */   }
/*      */   
/*      */   public void saveItems() {
/* 1516 */     if (!MagicCosmetics.getInstance().isZoneHideItems())
/* 1517 */       return;  Player player = getOfflinePlayer().getPlayer();
/* 1518 */     for (int i = 0; i < player.getInventory().getSize(); i++) {
/* 1519 */       ItemStack itemStack = player.getInventory().getItem(i);
/* 1520 */       if (itemStack == null) {
/* 1521 */         this.inventory.put(Integer.valueOf(i), null);
/*      */       
/*      */       }
/* 1524 */       else if (itemStack.getType() == XMaterial.AIR.parseMaterial()) {
/* 1525 */         this.inventory.put(Integer.valueOf(i), null);
/*      */       } else {
/*      */         
/* 1528 */         this.inventory.put(Integer.valueOf(i), itemStack.clone());
/*      */       } 
/* 1530 */     }  player.getInventory().clear();
/*      */   }
/*      */   
/*      */   public int getFreeSlotInventory() {
/* 1534 */     Player player = getOfflinePlayer().getPlayer();
/* 1535 */     for (int i = 0; i < (player.getInventory().getStorageContents()).length; i++) {
/* 1536 */       if (this.inventory.get(Integer.valueOf(i)) == null) {
/* 1537 */         return i;
/*      */       }
/*      */     } 
/* 1540 */     return -1;
/*      */   }
/*      */   
/*      */   public void loadItems() {
/* 1544 */     if (!MagicCosmetics.getInstance().isZoneHideItems())
/* 1545 */       return;  Player player = getOfflinePlayer().getPlayer();
/* 1546 */     for (Map.Entry<Integer, ItemStack> inv : this.inventory.entrySet()) {
/* 1547 */       player.getInventory().setItem(((Integer)inv.getKey()).intValue(), inv.getValue());
/*      */     }
/* 1549 */     this.inventory.clear();
/*      */   }
/*      */   
/*      */   public Map<Integer, ItemStack> getInventory() {
/* 1553 */     return this.inventory;
/*      */   }
/*      */   
/*      */   public void setZone(boolean zone) {
/* 1557 */     this.isZone = zone;
/*      */   }
/*      */   
/*      */   public int getEquippedCount() {
/* 1561 */     int count = 0;
/* 1562 */     if (this.hat != null) {
/* 1563 */       count++;
/*      */     }
/* 1565 */     if (this.bag != null) {
/* 1566 */       count++;
/*      */     }
/* 1568 */     if (this.wStick != null) {
/* 1569 */       count++;
/*      */     }
/* 1571 */     if (this.balloon != null) {
/* 1572 */       count++;
/*      */     }
/* 1574 */     if (this.spray != null) {
/* 1575 */       count++;
/*      */     }
/* 1577 */     return count;
/*      */   }
/*      */   
/*      */   public Set<Cosmetic> cosmeticsInUse() {
/* 1581 */     Set<Cosmetic> cosmetics = new HashSet<>();
/* 1582 */     if (this.hat != null) {
/* 1583 */       cosmetics.add(this.hat);
/*      */     }
/* 1585 */     if (this.bag != null) {
/* 1586 */       cosmetics.add(this.bag);
/*      */     }
/* 1588 */     if (this.wStick != null) {
/* 1589 */       cosmetics.add(this.wStick);
/*      */     }
/* 1591 */     if (this.balloon != null) {
/* 1592 */       cosmetics.add(this.balloon);
/*      */     }
/* 1594 */     if (this.spray != null) {
/* 1595 */       cosmetics.add(this.spray);
/*      */     }
/* 1597 */     return cosmetics;
/*      */   }
/*      */   
/*      */   public float getSpeedFly() {
/* 1601 */     return this.speedFly;
/*      */   }
/*      */   
/*      */   public void setSpeedFly(float speedFly) {
/* 1605 */     this.speedFly = speedFly;
/*      */   }
/*      */   
/*      */   public boolean isSpectator() {
/* 1609 */     return this.spectator;
/*      */   }
/*      */   
/*      */   public void toggleHiddeCosmetics() {
/* 1613 */     this.hideCosmetics = !this.hideCosmetics;
/* 1614 */     if (this.hideCosmetics) {
/* 1615 */       hideAllCosmetics();
/*      */       return;
/*      */     } 
/* 1618 */     showAllCosmetics();
/*      */   }
/*      */   
/*      */   public void hideAllCosmetics() {
/* 1622 */     if (this.hat != null && !this.hat.isHideCosmetic()) {
/* 1623 */       this.hat.setHideCosmetic(true);
/*      */     }
/* 1625 */     if (this.bag != null && !this.bag.isHideCosmetic()) {
/* 1626 */       this.bag.setHideCosmetic(true);
/*      */     }
/* 1628 */     if (this.wStick != null && !this.wStick.isHideCosmetic()) {
/* 1629 */       this.wStick.setHideCosmetic(true);
/*      */     }
/* 1631 */     if (this.balloon != null && !this.balloon.isHideCosmetic()) {
/* 1632 */       this.balloon.setHideCosmetic(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void showAllCosmetics() {
/* 1637 */     if (this.hat != null && this.hat.isHideCosmetic()) {
/* 1638 */       this.hat.setHideCosmetic(false);
/*      */     }
/* 1640 */     if (this.bag != null && this.bag.isHideCosmetic()) {
/* 1641 */       this.bag.setHideCosmetic(false);
/*      */     }
/* 1643 */     if (this.wStick != null && this.wStick.isHideCosmetic()) {
/* 1644 */       this.wStick.setHideCosmetic(false);
/*      */     }
/* 1646 */     if (this.balloon != null && this.balloon.isHideCosmetic()) {
/* 1647 */       this.balloon.setHideCosmetic(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isHasInBlackList() {
/* 1652 */     return this.hasInBlackList;
/*      */   }
/*      */   
/*      */   public void setHasInBlackList(boolean hasInBlackList) {
/* 1656 */     this.hasInBlackList = hasInBlackList;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadCosmetics(String loadCosmetics, String loadUseCosmetics) {
/* 1661 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1662 */     loadCosmetics(loadCosmetics);
/* 1663 */     String[] cosmetics = loadUseCosmetics.split(";");
/* 1664 */     String hat = cosmetics[0];
/* 1665 */     String bag = cosmetics[1];
/* 1666 */     String wStick = cosmetics[2];
/* 1667 */     String balloon = cosmetics[3];
/* 1668 */     String spray = cosmetics[4];
/*      */     
/* 1670 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> {
/*      */           setCosmetic(CosmeticType.HAT, getCosmeticById(hat));
/*      */           setCosmetic(CosmeticType.WALKING_STICK, getCosmeticById(wStick));
/*      */           setCosmetic(CosmeticType.BAG, getCosmeticById(bag));
/*      */         });
/* 1675 */     setCosmetic(CosmeticType.BALLOON, getCosmeticById(balloon));
/* 1676 */     setCosmetic(CosmeticType.SPRAY, getCosmeticById(spray));
/*      */   }
/*      */   
/*      */   public String getCosmeticsInUse() {
/* 1680 */     return ((this.hat != null) ? this.hat.getId() : "0") + ";" + ((this.hat != null) ? this.hat.getId() : "0") + ";" + (
/* 1681 */       (this.bag != null) ? this.bag.getId() : "0") + ";" + (
/* 1682 */       (this.wStick != null) ? this.wStick.getId() : "0") + ";" + (
/* 1683 */       (this.balloon != null) ? this.balloon.getId() : "0") + ";";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendLoadPlayerData() {
/* 1691 */     ByteArrayDataOutput output = ByteStreams.newDataOutput();
/* 1692 */     output.writeUTF("load_cosmetics");
/* 1693 */     output.writeUTF(getOfflinePlayer().getName());
/* 1694 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 1695 */     plugin.getServer().sendPluginMessage((Plugin)plugin, "mc:player", output.toByteArray());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSavePlayerData() {
/* 1701 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1706 */     if (!plugin.isProxy())
/* 1707 */       return;  ByteArrayDataOutput output = ByteStreams.newDataOutput();
/* 1708 */     output.writeUTF("save_cosmetics");
/* 1709 */     output.writeUTF(getOfflinePlayer().getName());
/* 1710 */     output.writeUTF(saveCosmetics());
/* 1711 */     output.writeUTF(getCosmeticsInUse());
/* 1712 */     plugin.getServer().sendPluginMessage((Plugin)plugin, "mc:player", output.toByteArray());
/*      */   }
/*      */ 
/*      */   
/*      */   public IRangeManager getRangeManager() {
/* 1717 */     return this.rangeManager;
/*      */   }
/*      */   
/*      */   public void verifyWorldBlackList(MagicCosmetics plugin) {
/* 1721 */     if (getEquippedCount() < 1)
/* 1722 */       return;  Player player = getOfflinePlayer().getPlayer();
/* 1723 */     if (player == null)
/* 1724 */       return;  World world = player.getWorld();
/* 1725 */     if (plugin.getWorldsBlacklist().contains(world.getName())) {
/* 1726 */       if (isHasInBlackList())
/* 1727 */         return;  setHasInBlackList(true);
/* 1728 */       hideAllCosmetics();
/* 1729 */       PlayerChangeBlacklistEvent playerChangeBlacklistEvent = new PlayerChangeBlacklistEvent(player, isHasInBlackList());
/* 1730 */       plugin.getServer().getPluginManager().callEvent((Event)playerChangeBlacklistEvent);
/*      */       return;
/*      */     } 
/* 1733 */     if (!isHasInBlackList())
/* 1734 */       return;  setHasInBlackList(false);
/* 1735 */     showAllCosmetics();
/* 1736 */     PlayerChangeBlacklistEvent callEvent = new PlayerChangeBlacklistEvent(player, isHasInBlackList());
/* 1737 */     plugin.getServer().getPluginManager().callEvent((Event)callEvent);
/*      */   }
/*      */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\PlayerData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */