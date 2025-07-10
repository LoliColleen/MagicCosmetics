package com.francobm.magicosmetics.cache;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.api.SprayKeys;
import com.francobm.magicosmetics.cache.cosmetics.Hat;
import com.francobm.magicosmetics.cache.cosmetics.Spray;
import com.francobm.magicosmetics.cache.cosmetics.WStick;
import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
import com.francobm.magicosmetics.events.CosmeticUnEquipEvent;
import com.francobm.magicosmetics.events.PlayerChangeBlacklistEvent;
import com.francobm.magicosmetics.events.Reason;
import com.francobm.magicosmetics.events.ZoneEnterEvent;
import com.francobm.magicosmetics.events.ZoneExitEvent;
import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.nms.NPC.NPC;
import com.francobm.magicosmetics.utils.Utils;
import com.francobm.magicosmetics.utils.XMaterial;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class PlayerData {
  public static Map<UUID, PlayerData> players = new HashMap<>();
  
  private OfflinePlayer offlinePlayer;
  
  private final UUID uniqueId;
  
  private final String name;
  
  private Hat hat;
  
  private Cosmetic bag;
  
  private WStick wStick;
  
  private Cosmetic balloon;
  
  private Cosmetic spray;
  
  private final Map<String, Cosmetic> cosmetics;
  
  private Cosmetic previewHat;
  
  private Cosmetic previewBag;
  
  private Cosmetic previewWStick;
  
  private Cosmetic previewBalloon;
  
  private Cosmetic previewSpray;
  
  private boolean isZone;
  
  private boolean sneak;
  
  private boolean spectator;
  
  private Zone zone;
  
  private final Map<Integer, ItemStack> inventory;
  
  private GameMode gameMode;
  
  private float speedFly;
  
  private boolean hideCosmetics;
  
  private boolean hasInBlackList;
  
  private final IRangeManager rangeManager;
  
  public PlayerData(UUID uniqueId, String name, IRangeManager rangeManager) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.hat = null;
    this.bag = null;
    this.wStick = null;
    this.balloon = null;
    this.cosmetics = new HashMap<>();
    this.previewHat = null;
    this.previewBag = null;
    this.previewWStick = null;
    this.previewBalloon = null;
    this.isZone = false;
    this.sneak = false;
    this.spectator = false;
    this.zone = null;
    this.inventory = new HashMap<>();
    this.offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
    this.rangeManager = rangeManager;
  }
  
  public static PlayerData getPlayer(OfflinePlayer player) {
    if (!players.containsKey(player.getUniqueId())) {
      PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName(), null);
      players.put(player.getUniqueId(), playerData);
      return playerData;
    } 
    return players.get(player.getUniqueId());
  }
  
  public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
    this.offlinePlayer = offlinePlayer;
  }
  
  public static void reload() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      PlayerData playerData = getPlayer((OfflinePlayer)player);
      playerData.updateCosmetics();
    } 
  }
  
  public void updateCosmetics() {
    clearCosmeticsInUse(true);
    if (this.hat != null && !this.hat.updateProperties())
      removeHat(); 
    if (this.wStick != null && !this.wStick.updateProperties())
      removeWStick(); 
    if (this.balloon != null && !this.balloon.updateProperties())
      removeBalloon(); 
    if (this.spray != null && !this.spray.updateProperties())
      removeSpray(); 
  }
  
  public int getCosmeticCount(CosmeticType cosmeticType) {
    int i = 0;
    if (MagicCosmetics.getInstance().isPermissions()) {
      for (Cosmetic cosmetic : Cosmetic.cosmetics.values()) {
        if (!cosmetic.hasPermission(getOfflinePlayer().getPlayer()) || 
          cosmetic.getCosmeticType() != cosmeticType)
          continue; 
        i++;
      } 
      return i;
    } 
    for (Cosmetic cosmetic : this.cosmetics.values()) {
      if (cosmetic.getCosmeticType() != cosmeticType)
        continue; 
      i++;
    } 
    return i;
  }
  
  public Cosmetic getPreviewBalloon() {
    return this.previewBalloon;
  }
  
  public void setPreviewBalloon(Cosmetic previewBalloon) {
    this.previewBalloon = previewBalloon;
  }
  
  public Cosmetic getPreviewSpray() {
    return this.previewSpray;
  }
  
  public void setPreviewSpray(Cosmetic previewSpray) {
    this.previewSpray = previewSpray;
  }
  
  public Cosmetic getPreviewHat() {
    return this.previewHat;
  }
  
  public void setPreviewHat(Cosmetic previewHat) {
    this.previewHat = previewHat;
  }
  
  public Cosmetic getPreviewBag() {
    return this.previewBag;
  }
  
  public void setPreviewBag(Cosmetic previewBag) {
    this.previewBag = previewBag;
  }
  
  public Cosmetic getPreviewWStick() {
    return this.previewWStick;
  }
  
  public void setPreviewWStick(Cosmetic previewWStick) {
    this.previewWStick = previewWStick;
  }
  
  public static void removePlayer(PlayerData player) {
    players.remove(player.getUniqueId());
  }
  
  public Hat getHat() {
    return this.hat;
  }
  
  public void setHat(Hat hat) {
    this.hat = hat;
    if (this.hat == null)
      return; 
    this.hat.setPlayer(this.offlinePlayer.getPlayer());
    activeHat();
  }
  
  public Cosmetic getBag() {
    return this.bag;
  }
  
  public void setBag(Cosmetic bag) {
    this.bag = bag;
    if (this.bag == null)
      return; 
    this.bag.setPlayer(this.offlinePlayer.getPlayer());
  }
  
  public WStick getWStick() {
    return this.wStick;
  }
  
  public void setWStick(WStick wStick) {
    this.wStick = wStick;
    if (this.wStick == null)
      return; 
    this.wStick.setPlayer(this.offlinePlayer.getPlayer());
    activeWStick();
  }
  
  public Cosmetic getBalloon() {
    return this.balloon;
  }
  
  public void setBalloon(Cosmetic balloon) {
    this.balloon = balloon;
    if (this.balloon == null)
      return; 
    this.balloon.setPlayer(this.offlinePlayer.getPlayer());
  }
  
  public Cosmetic getSpray() {
    return this.spray;
  }
  
  public void setSpray(Cosmetic spray) {
    this.spray = spray;
    if (this.spray == null)
      return; 
    this.spray.setPlayer(this.offlinePlayer.getPlayer());
  }
  
  public void removeCosmetic(String cosmeticId) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    Cosmetic cosmetic = getCosmeticById(cosmeticId);
    if (cosmetic == null) {
      cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
      if (cosmetic == null)
        return; 
    } 
    CosmeticUnEquipEvent event = new CosmeticUnEquipEvent(getOfflinePlayer().getPlayer(), cosmetic);
    MagicCosmetics.getInstance().getServer().getPluginManager().callEvent((Event)event);
    if (event.isCancelled())
      return; 
    if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
      removeEquip(cosmeticId);
      removePreviewEquip(cosmeticId);
      plugin.getLuckPerms().removePermission(getUniqueId(), cosmetic.getPermission());
      return;
    } 
    removeEquip(cosmeticId);
    removePreviewEquip(cosmeticId);
    this.cosmetics.remove(cosmeticId);
  }
  
  public void setCosmetic(Cosmetic cosmetic) {
    if (cosmetic == null)
      return; 
    switch (cosmetic.getCosmeticType()) {
      case HAT:
        clearHat();
        setHat((Hat)cosmetic);
        break;
      case BAG:
        clearBag();
        setBag(cosmetic);
        break;
      case WALKING_STICK:
        clearWStick();
        setWStick((WStick)cosmetic);
        break;
      case BALLOON:
        clearBalloon();
        setBalloon(cosmetic);
        break;
      case SPRAY:
        clearSpray();
        setSpray(cosmetic);
        break;
    } 
  }
  
  public void setCosmetic(CosmeticType cosmeticType, Cosmetic cosmetic) {
    switch (cosmeticType) {
      case HAT:
        clearHat();
        setHat((Hat)cosmetic);
        break;
      case BAG:
        clearBag();
        setBag(cosmetic);
        break;
      case WALKING_STICK:
        clearWStick();
        setWStick((WStick)cosmetic);
        break;
      case BALLOON:
        clearBalloon();
        setBalloon(cosmetic);
        break;
      case SPRAY:
        clearSpray();
        setSpray(cosmetic);
        break;
    } 
  }
  
  public void setPreviewCosmetic(Cosmetic cosmetic) {
    if (cosmetic == null)
      return; 
    switch (cosmetic.getCosmeticType()) {
      case HAT:
        clearPreviewHat();
        setPreviewHat(cosmetic);
        activePreviewHat();
        break;
      case BAG:
        clearPreviewBag();
        setPreviewBag(cosmetic);
        activePreviewBag();
        break;
      case WALKING_STICK:
        clearPreviewWStick();
        setPreviewWStick(cosmetic);
        activePreviewWStick();
        break;
      case BALLOON:
        clearPreviewBalloon();
        setPreviewBalloon(cosmetic);
        activePreviewBalloon();
        break;
      case SPRAY:
        clearPreviewSpray();
        setPreviewSpray(cosmetic);
        activePreviewSpray();
        break;
    } 
  }
  
  public UUID getUniqueId() {
    return this.uniqueId;
  }
  
  public OfflinePlayer getOfflinePlayer() {
    return this.offlinePlayer;
  }
  
  public Map<String, Cosmetic> getCosmetics() {
    return this.cosmetics;
  }
  
  public List<Cosmetic> getCosmeticsPerm() {
    List<Cosmetic> cosmetics = new ArrayList<>();
    for (Cosmetic cosmetic : Cosmetic.cosmetics.values()) {
      if (!cosmetic.hasPermission(getOfflinePlayer().getPlayer()))
        continue; 
      cosmetics.add(cosmetic);
    } 
    return cosmetics;
  }
  
  public Cosmetic getCosmeticByName(String name) {
    for (Cosmetic cosmetic : this.cosmetics.values()) {
      if (cosmetic.getName().equalsIgnoreCase(name))
        return cosmetic; 
    } 
    return null;
  }
  
  public Cosmetic getCosmeticById(String id) {
    if (id == null || id.isEmpty())
      return null; 
    return this.cosmetics.get(id);
  }
  
  public boolean hasCosmeticById(String id) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    Cosmetic cosmetic = Cosmetic.getCosmetic(id);
    if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty())
      return cosmetic.hasPermission(getOfflinePlayer().getPlayer()); 
    return this.cosmetics.containsKey(id);
  }
  
  public String saveCosmetics() {
    List<String> ids = new ArrayList<>();
    if (MagicCosmetics.getInstance().isPermissions()) {
      for (Cosmetic cosmetic : cosmeticsInUse()) {
        if (ids.contains(cosmetic.getId()))
          continue; 
        if (cosmetic.getColor() != null) {
          ids.add(cosmetic.getId() + "|" + cosmetic.getId());
          continue;
        } 
        ids.add(cosmetic.getId());
      } 
      if (ids.isEmpty())
        return ""; 
      return String.join(",", (Iterable)ids);
    } 
    for (Cosmetic cosmetic : this.cosmetics.values()) {
      if (ids.contains(cosmetic.getId()))
        continue; 
      if (cosmetic.getColor() != null) {
        ids.add(cosmetic.getId() + "|" + cosmetic.getId());
        continue;
      } 
      ids.add(cosmetic.getId());
    } 
    if (ids.isEmpty())
      return ""; 
    return String.join(",", (Iterable)ids);
  }
  
  public void loadCosmetics(String ids) {
    if (ids.isEmpty())
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    List<String> cosmetics = new ArrayList<>(Arrays.asList(ids.split(",")));
    this.cosmetics.clear();
    for (String cosmetic : cosmetics) {
      String[] color = cosmetic.split("\\|");
      if (color.length > 1) {
        Cosmetic cosmetic2 = Cosmetic.getCloneCosmetic(color[0]);
        if (cosmetic2 == null || (
          plugin.isPermissions() && plugin.isLuckPerms() && plugin.getLuckPerms().isExpirePermission(this.offlinePlayer.getUniqueId(), cosmetic2.getPermission())))
          continue; 
        if (this.cosmetics.containsKey(color[0]))
          continue; 
        cosmetic2.setColor(Color.fromRGB(Integer.parseInt(color[1])));
        addCosmetic(cosmetic2);
        continue;
      } 
      Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(cosmetic);
      if (cosmetic1 == null || (
        plugin.isPermissions() && plugin.isLuckPerms() && plugin.getLuckPerms().isExpirePermission(this.offlinePlayer.getUniqueId(), cosmetic1.getPermission())))
        continue; 
      if (this.cosmetics.containsKey(cosmetic))
        continue; 
      addCosmetic(cosmetic1);
    } 
  }
  
  public void addCosmetic(Cosmetic cosmetic) {
    Hat hat, newHat;
    Bag bag, newBag;
    WStick wStick, newWStick;
    Balloon balloon, newBalloon;
    Spray spray, newSpray;
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (cosmetic == null)
      return; 
    if (plugin.isPermissions() && !cosmetic.getPermission().isEmpty() && plugin.isLuckPerms() && 
      !cosmetic.hasPermission(getOfflinePlayer().getPlayer()))
      plugin.getLuckPerms().addPermission(getUniqueId(), cosmetic.getPermission()); 
    Player player = this.offlinePlayer.getPlayer();
    switch (cosmetic.getCosmeticType()) {
      case HAT:
        hat = (Hat)cosmetic;
        newHat = new Hat(hat.getId(), hat.getName(), hat.getItemStack().clone(), hat.getModelData(), hat.isColored(), hat.getCosmeticType(), hat.getColor(), hat.isOverlaps(), hat.getPermission(), hat.isTexture(), hat.isHideMenu(), hat.isUseEmote(), hat.getOffSetY(), hat.getNamespacedKey());
        newHat.setColorBlocked(cosmetic.isColorBlocked());
        newHat.setPlayer(player);
        this.cosmetics.put(cosmetic.getId(), newHat);
        return;
      case BAG:
        bag = (Bag)cosmetic;
        newBag = new Bag(bag.getId(), bag.getName(), bag.getItemStack().clone(), bag.getModelData(), bag.getBagForMe(), bag.isColored(), bag.getSpace(), bag.getCosmeticType(), bag.getColor(), bag.getDistance(), bag.getPermission(), bag.isTexture(), bag.isHideMenu(), bag.getHeight(), bag.isUseEmote(), (bag.getBackPackEngine() != null) ? bag.getBackPackEngine().getClone() : null, bag.getNamespacedKey());
        newBag.setColorBlocked(cosmetic.isColorBlocked());
        newBag.setPlayer(player);
        this.cosmetics.put(cosmetic.getId(), newBag);
        return;
      case WALKING_STICK:
        wStick = (WStick)cosmetic;
        newWStick = new WStick(wStick.getId(), wStick.getName(), wStick.getItemStack().clone(), wStick.getModelData(), wStick.isColored(), wStick.getCosmeticType(), wStick.getColor(), wStick.getPermission(), wStick.isTexture(), wStick.isOverlaps(), wStick.isHideMenu(), wStick.isUseEmote(), wStick.getNamespacedKey());
        newWStick.setColorBlocked(cosmetic.isColorBlocked());
        newWStick.setPlayer(player);
        this.cosmetics.put(cosmetic.getId(), newWStick);
        break;
      case BALLOON:
        balloon = (Balloon)cosmetic;
        newBalloon = new Balloon(balloon.getId(), balloon.getName(), balloon.getItemStack().clone(), balloon.getModelData(), balloon.isColored(), balloon.getSpace(), balloon.getCosmeticType(), balloon.getColor(), balloon.isRotation(), balloon.getRotationType(), (balloon.getBalloonEngine() != null) ? balloon.getBalloonEngine().getClone() : null, (balloon.getBalloonIA() != null) ? balloon.getBalloonIA().getClone() : null, balloon.getDistance(), balloon.getPermission(), balloon.isTexture(), balloon.isBigHead(), balloon.isHideMenu(), balloon.isInvisibleLeash(), balloon.isUseEmote(), balloon.isInstantFollow(), balloon.getNamespacedKey());
        newBalloon.setColorBlocked(cosmetic.isColorBlocked());
        newBalloon.setPlayer(player);
        this.cosmetics.put(cosmetic.getId(), newBalloon);
        break;
      case SPRAY:
        spray = (Spray)cosmetic;
        newSpray = new Spray(spray.getId(), spray.getName(), spray.getItemStack().clone(), spray.getModelData(), spray.isColored(), spray.getCosmeticType(), spray.getColor(), spray.getPermission(), spray.isTexture(), spray.getImage(), spray.isItemImage(), spray.isHideMenu(), spray.isUseEmote(), spray.getNamespacedKey());
        newSpray.setColorBlocked(cosmetic.isColorBlocked());
        newSpray.setPlayer(player);
        this.cosmetics.put(cosmetic.getId(), newSpray);
        break;
    } 
  }
  
  public void removeHat() {
    clearHat();
    this.hat = null;
  }
  
  public void removeBag() {
    clearBag();
    this.bag = null;
  }
  
  public void removeWStick() {
    clearWStick();
    this.wStick = null;
  }
  
  public void removeBalloon() {
    clearBalloon();
    this.balloon = null;
  }
  
  public void removeSpray() {
    clearSpray();
    this.spray = null;
  }
  
  public void removePreviewHat() {
    clearPreviewHat();
    this.previewHat = null;
  }
  
  public void removePreviewBag() {
    clearPreviewBag();
    this.previewBag = null;
  }
  
  public void removePreviewWStick() {
    clearPreviewWStick();
    this.previewWStick = null;
  }
  
  public void removePreviewBalloon() {
    clearPreviewBalloon();
    this.previewBalloon = null;
  }
  
  public void removePreviewSpray() {
    clearPreviewSpray();
    this.previewSpray = null;
  }
  
  public Cosmetic getEquip(CosmeticType cosmeticType) {
    switch (cosmeticType) {
      case HAT:
        if (this.hat == null)
          return null; 
        return (Cosmetic)this.hat;
      case BAG:
        if (this.bag == null)
          return null; 
        return this.bag;
      case WALKING_STICK:
        if (this.wStick == null)
          return null; 
        return (Cosmetic)this.wStick;
      case BALLOON:
        if (this.balloon == null)
          return null; 
        return this.balloon;
      case SPRAY:
        if (this.spray == null)
          return null; 
        return this.spray;
    } 
    return null;
  }
  
  public Cosmetic getEquip(String id) {
    if (this.hat != null && 
      this.hat.getId().equalsIgnoreCase(id))
      return (Cosmetic)this.hat; 
    if (this.bag != null && 
      this.bag.getId().equalsIgnoreCase(id))
      return this.bag; 
    if (this.wStick != null && 
      this.wStick.getId().equalsIgnoreCase(id))
      return (Cosmetic)this.wStick; 
    if (this.balloon != null && 
      this.balloon.getId().equalsIgnoreCase(id))
      return this.balloon; 
    if (this.spray != null && 
      this.spray.getId().equalsIgnoreCase(id))
      return this.spray; 
    return null;
  }
  
  public void removeEquip(String id) {
    if (this.hat != null && 
      this.hat.getId().equalsIgnoreCase(id)) {
      removeHat();
      return;
    } 
    if (this.bag != null && 
      this.bag.getId().equalsIgnoreCase(id)) {
      removeBag();
      return;
    } 
    if (this.wStick != null && 
      this.wStick.getId().equalsIgnoreCase(id)) {
      removeWStick();
      return;
    } 
    if (this.balloon != null && 
      this.balloon.getId().equalsIgnoreCase(id))
      removeBalloon(); 
    if (this.spray != null && 
      this.spray.getId().equalsIgnoreCase(id))
      removeSpray(); 
  }
  
  public void removeEquip(CosmeticType cosmeticType) {
    switch (cosmeticType) {
      case HAT:
        removeHat();
        return;
      case BAG:
        removeBag();
        return;
      case WALKING_STICK:
        removeWStick();
        return;
      case BALLOON:
        removeBalloon();
        return;
      case SPRAY:
        removeSpray();
        break;
    } 
  }
  
  public void removePreviewEquip(String id) {
    if (this.previewHat != null && 
      this.previewHat.getId().equalsIgnoreCase(id))
      removePreviewHat(); 
    if (this.previewBag != null && 
      this.previewBag.getId().equalsIgnoreCase(id))
      removePreviewBag(); 
    if (this.previewWStick != null && 
      this.previewWStick.getId().equalsIgnoreCase(id))
      removePreviewWStick(); 
    if (this.previewBalloon != null && 
      this.previewBalloon.getId().equalsIgnoreCase(id))
      removePreviewBalloon(); 
    if (this.previewSpray != null && 
      this.previewSpray.getId().equalsIgnoreCase(id))
      removePreviewSpray(); 
  }
  
  public void clearHat() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.hat == null)
      return; 
    if (this.hat.isRemovedLendEntity())
      return; 
    this.hat.remove();
  }
  
  public void clearBag() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.bag == null)
      return; 
    if (this.bag.isRemovedLendEntity())
      return; 
    this.bag.remove();
  }
  
  public void clearWStick() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.wStick == null)
      return; 
    if (this.wStick.isRemovedLendEntity())
      return; 
    this.wStick.remove();
  }
  
  public void clearBalloon() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.balloon == null)
      return; 
    if (this.balloon.isRemovedLendEntity())
      return; 
    this.balloon.remove();
  }
  
  public void clearSpray() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.spray == null)
      return; 
    if (this.spray.isRemovedLendEntity())
      return; 
    this.spray.remove();
  }
  
  public void clearPreviewHat() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewHat == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.equipNPC(player, ItemSlot.HELMET, XMaterial.AIR.parseItem());
  }
  
  public void clearPreviewBag() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewBag == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.armorStandSetItem(player, XMaterial.AIR.parseItem());
  }
  
  public void clearPreviewWStick() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewWStick == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.equipNPC(player, ItemSlot.OFF_HAND, XMaterial.AIR.parseItem());
  }
  
  public void clearPreviewBalloon() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewBalloon == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.removeBalloon(player);
  }
  
  public void clearPreviewSpray() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewSpray == null)
      return; 
    this.previewSpray.remove();
  }
  
  public void activeHat() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.hat == null)
      return; 
    if (this.isZone)
      return; 
    if (MagicCosmetics.getInstance().isItemsAdder() && 
      MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.hat.isUseEmote()) {
      this.hat.update();
      return;
    } 
    if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
      clearHat();
      return;
    } 
    this.hat.update();
  }
  
  public void activeBag() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.bag == null)
      return; 
    if (this.isZone)
      return; 
    if (MagicCosmetics.getInstance().isItemsAdder() && 
      MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.bag.isUseEmote()) {
      this.bag.update();
      return;
    } 
    Material material = player.getLocation().getBlock().getType();
    if (player.getPose() == Pose.SLEEPING || player.getPose() == Pose.SWIMMING || player.isGliding() || player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY) || material == Material.NETHER_PORTAL || material == Material.END_PORTAL) {
      clearBag();
      return;
    } 
    this.bag.update();
  }
  
  public void activeWStick() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.wStick == null)
      return; 
    if (this.isZone)
      return; 
    if (MagicCosmetics.getInstance().isItemsAdder() && 
      MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.wStick.isUseEmote()) {
      this.wStick.update();
      return;
    } 
    if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
      clearWStick();
      return;
    } 
    this.wStick.update();
  }
  
  public void activeBalloon() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.balloon == null)
      return; 
    if (this.isZone)
      return; 
    if (MagicCosmetics.getInstance().isItemsAdder() && 
      MagicCosmetics.getInstance().getItemsAdder().hasEmote(player) && this.balloon.isUseEmote()) {
      this.balloon.update();
      return;
    } 
    if (this.balloon.getLendEntity() != null) {
      this.balloon.lendToEntity();
      return;
    } 
    this.balloon.update();
  }
  
  public void previewDraw() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewSpray == null)
      return; 
    Zone zone = getZone();
    if (zone == null)
      return; 
    if (zone.getSprayFace() == null || zone.getSprayLoc() == null)
      return; 
    ((Spray)this.previewSpray).draw(player, zone.getSprayFace(), zone.getSprayLoc(), zone.getRotation());
  }
  
  public void draw(SprayKeys key) {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.spray == null)
      return; 
    if (this.isZone)
      return; 
    ((Spray)this.spray).draw(player, key);
  }
  
  public void activeSpray() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.spray == null)
      return; 
    if (this.isZone)
      return; 
    this.spray.update();
  }
  
  public void activePreviewHat() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewHat == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.equipNPC(player, ItemSlot.HELMET, this.previewHat.getItemColor(player));
  }
  
  public void activePreviewBag() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewBag == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.armorStandSetItem(player, this.previewBag.getItemColor(player));
  }
  
  public void activePreviewWStick() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewWStick == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.equipNPC(player, ItemSlot.OFF_HAND, this.previewWStick.getItemColor(player));
  }
  
  public void activePB() {
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewBalloon == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    npc.animation(player);
  }
  
  public void activePreviewBalloon() {
    Location location;
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    if (this.previewBalloon == null)
      return; 
    NPC npc = MagicCosmetics.getInstance().getVersion().getNPC(player);
    if (npc == null)
      return; 
    Zone zone = getZone();
    if (zone == null)
      return; 
    if (zone.getBalloon() == null) {
      location = npc.getEntity().getLocation();
    } else {
      location = zone.getBalloon();
    } 
    npc.balloonNPC(player, location, this.previewBalloon.getItemColor(player), ((Balloon)this.previewBalloon).isBigHead());
  }
  
  public void activePreviewSpray() {
    previewDraw();
  }
  
  public void activeCosmetics() {
    activeBag();
    activePB();
  }
  
  public void clearCosmeticsInUse(boolean inventory) {
    if (inventory)
      clearCosmeticsInventory(); 
    clearBag();
    clearBalloon();
    clearSpray();
  }
  
  public void clearCosmeticsToSaveData() {
    if (this.hat != null)
      this.hat.clearClose(); 
    if (this.wStick != null)
      this.wStick.clearClose(); 
    if (this.balloon != null)
      this.balloon.clearClose(); 
    if (this.bag != null)
      this.bag.clearClose(); 
    if (this.spray != null)
      this.spray.clearClose(); 
  }
  
  public void forceClearCosmeticsInventory() {
    if (this.hat != null)
      this.hat.forceRemove(); 
    if (this.wStick != null)
      this.wStick.forceRemove(); 
  }
  
  public void clearCosmeticsInventory() {
    clearHat();
    clearWStick();
  }
  
  public void activeCosmeticsInventory() {
    activeHat();
    activeWStick();
  }
  
  public void setZone(Zone zone) {
    this.zone = zone;
  }
  
  public Zone getZone() {
    return this.zone;
  }
  
  public boolean isSneak() {
    return this.sneak;
  }
  
  public void setSneak(boolean sneak) {
    this.sneak = sneak;
  }
  
  public boolean isZone() {
    return this.isZone;
  }
  
  public boolean removeHelmet() {
    if (!getOfflinePlayer().isOnline())
      return false; 
    Player player = getOfflinePlayer().getPlayer();
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    ItemStack helmet = player.getInventory().getHelmet();
    if (this.hat == null) {
      if (helmet != null && 
        player.getInventory().firstEmpty() == -1) {
        Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
        Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
        return false;
      } 
      return true;
    } 
    if (helmet != null && 
      player.getInventory().firstEmpty() == -1) {
      Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
      Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
      return false;
    } 
    ItemStack savedItem = this.hat.leftItemAndGet();
    if (savedItem != null)
      player.getInventory().addItem(new ItemStack[] { savedItem }); 
    return true;
  }
  
  public boolean removeOffHand() {
    if (!getOfflinePlayer().isOnline())
      return false; 
    Player player = getOfflinePlayer().getPlayer();
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    ItemStack offHand = player.getInventory().getItemInOffHand();
    if (this.wStick == null) {
      if (!offHand.getType().isAir() && 
        player.getInventory().firstEmpty() == -1) {
        Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
        Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
        return false;
      } 
      return true;
    } 
    if (!offHand.getType().isAir() && 
      player.getInventory().firstEmpty() == -1) {
      Utils.sendSound(player, Sound.getSound("on_enter_zone_error"));
      Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
      return false;
    } 
    ItemStack savedItem = this.wStick.leftItemAndGet();
    if (savedItem != null)
      player.getInventory().addItem(new ItemStack[] { savedItem }); 
    return true;
  }
  
  public void enterZone() {
    Zone zone = getZone();
    if (zone == null) {
      for (Zone z : Zone.zones.values()) {
        if (!z.isInZone(getOfflinePlayer().getPlayer().getLocation().getBlock()))
          continue; 
        setZone(z);
      } 
      return;
    } 
    if (!zone.isActive()) {
      this.isZone = false;
      return;
    } 
    Player player = getOfflinePlayer().getPlayer();
    if (!getOfflinePlayer().isOnline())
      return; 
    if (this.isZone) {
      if (this.spectator && 
        !player.getLocation().equals(zone.getEnter()))
        player.teleport(zone.getEnter()); 
      return;
    } 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    ZoneEnterEvent event = new ZoneEnterEvent(player, zone);
    plugin.getServer().getPluginManager().callEvent((Event)event);
    if ((plugin.getMagicCrates() != null && plugin.getMagicCrates().hasInCrate(player)) || event.isCancelled()) {
      setZone((Zone)null);
      player.teleport(zone.getExit());
      return;
    } 
    this.gameMode = player.getGameMode();
    Utils.sendSound(player, Sound.getSound("on_enter_zone"));
    plugin.getVersion().setSpectator(player);
    String title = plugin.getMessages().getString("title-zone.enter");
    if (player.getGameMode() != GameMode.SPECTATOR) {
      exitZone();
      return;
    } 
    if (plugin.isItemsAdder()) {
      if (plugin.getItemsAdder().hasEmote(player))
        plugin.getItemsAdder().stopEmote(player); 
      title = plugin.getItemsAdder().replaceFontImages(title);
    } 
    if (plugin.isOraxen())
      title = plugin.getOraxen().replaceFontImages(title); 
    if (!removeHelmet()) {
      ZoneExitEvent exitEvent = new ZoneExitEvent(player, zone, Reason.ITEM_IN_HELMET);
      plugin.getServer().getPluginManager().callEvent((Event)exitEvent);
      this.isZone = false;
      this.sneak = false;
      this.spectator = false;
      player.teleport(zone.getExit());
      setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
      player.setFlySpeed(getSpeedFly());
      plugin.getVersion().setCamera(player, (Entity)player);
      setZone((Zone)null);
      if (plugin.gameMode == null) {
        player.setGameMode(this.gameMode);
      } else {
        player.setGameMode(plugin.gameMode);
      } 
      return;
    } 
    if (!removeOffHand()) {
      ZoneExitEvent exitEvent = new ZoneExitEvent(player, zone, Reason.ITEM_IN_OFF_HAND);
      plugin.getServer().getPluginManager().callEvent((Event)exitEvent);
      this.isZone = false;
      this.sneak = false;
      this.spectator = false;
      player.teleport(zone.getExit());
      setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
      player.setFlySpeed(getSpeedFly());
      plugin.getVersion().setCamera(player, (Entity)player);
      setZone((Zone)null);
      if (plugin.gameMode == null) {
        player.setGameMode(this.gameMode);
      } else {
        player.setGameMode(plugin.gameMode);
      } 
      return;
    } 
    player.sendTitle(title, "", 15, 7, 15);
    clearCosmeticsInUse(true);
    for (BossBar bossBar : plugin.getBossBar()) {
      if (bossBar.getPlayers().contains(player))
        continue; 
      bossBar.addPlayer(player);
    } 
    plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, task -> {
          if (this.hat != null)
            this.hat.setHideCosmetic(true); 
          if (this.wStick != null)
            this.wStick.setHideCosmetic(true); 
          saveItems();
          if (player.getGameMode() == GameMode.SPECTATOR) {
            player.teleport(zone.getEnter());
            setSpeedFly(player.getFlySpeed());
            player.setFlySpeed(0.0F);
            this.spectator = true;
          } 
          plugin.getVersion().createNPC(player, zone.getNpc());
          plugin.getVersion().getNPC(player).spawnPunch(player, zone.getEnter());
          setPreviewCosmetic((Cosmetic)this.hat);
          setPreviewCosmetic(this.bag);
          setPreviewCosmetic((Cosmetic)this.wStick);
          setPreviewCosmetic(this.balloon);
          setPreviewCosmetic(this.spray);
          if (plugin.getCosmeticsManager().npcTaskStopped())
            plugin.getCosmeticsManager().reRunTasks(); 
        }12L);
    this.isZone = true;
  }
  
  public void exitZoneSync() {
    Zone zone = getZone();
    if (zone == null)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    Player player = getOfflinePlayer().getPlayer();
    Utils.showPlayer(player);
    this.sneak = false;
    this.spectator = false;
    this.isZone = false;
    for (BossBar bossBar : plugin.getBossBar())
      bossBar.removePlayer(player); 
    loadItems();
    if (plugin.gameMode == null) {
      player.setGameMode(this.gameMode);
    } else {
      player.setGameMode(plugin.gameMode);
    } 
    setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
    player.setFlySpeed(getSpeedFly());
    player.teleport(zone.getExit());
    plugin.getVersion().removeNPC(player);
    setZone((Zone)null);
  }
  
  public void exitZone() {
    Zone zone = getZone();
    if (zone == null)
      return; 
    if (!this.isZone)
      return; 
    if (this.sneak)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    Player player = getOfflinePlayer().getPlayer();
    ZoneExitEvent event = new ZoneExitEvent(player, zone, Reason.NORMAL);
    plugin.getServer().getPluginManager().callEvent((Event)event);
    if (event.isCancelled())
      return; 
    String title = plugin.getMessages().getString("title-zone.exit");
    if (plugin.isItemsAdder())
      title = plugin.getItemsAdder().replaceFontImages(title); 
    if (plugin.isOraxen())
      title = plugin.getOraxen().replaceFontImages(title); 
    player.sendTitle(title, "", 15, 7, 15);
    Utils.showPlayer(player);
    this.sneak = true;
    for (BossBar bossBar : plugin.getBossBar())
      bossBar.removePlayer(player); 
    plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, () -> {
          loadItems();
          if (plugin.gameMode == null) {
            player.setGameMode(this.gameMode);
          } else {
            player.setGameMode(plugin.gameMode);
          } 
          Utils.sendSound(player, Sound.getSound("on_exit_zone"));
          plugin.getVersion().removeNPC(player);
          int count = 0;
          if ((this.previewHat != null && this.previewHat.isColorBlocked()) || (this.previewBag != null && this.previewBag.isColorBlocked()) || (this.previewWStick != null && this.previewWStick.isColorBlocked()) || (this.previewBalloon != null && this.previewBalloon.isColorBlocked()) || (this.previewSpray != null && this.previewSpray.isColorBlocked())) {
            this.isZone = false;
            this.sneak = false;
            this.spectator = false;
            setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
            player.setFlySpeed(getSpeedFly());
            plugin.getVersion().setCamera(player, (Entity)player);
            player.teleport(zone.getExit());
            setZone((Zone)null);
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
            return;
          } 
          if (plugin.isPermissions()) {
            if (this.previewHat != null) {
              if (!this.previewHat.hasPermission(player))
                count++; 
              setPreviewHat(null);
            } 
            if (this.previewBag != null) {
              if (!this.previewBag.hasPermission(player))
                count++; 
              setPreviewBag(null);
            } 
            if (this.previewWStick != null) {
              if (!this.previewWStick.hasPermission(player))
                count++; 
              setPreviewWStick(null);
            } 
            if (this.previewBalloon != null) {
              if (!this.previewBalloon.hasPermission(player))
                count++; 
              setPreviewBalloon(null);
            } 
            if (this.previewSpray != null) {
              if (!this.previewSpray.hasPermission(player))
                count++; 
              setPreviewSpray(null);
            } 
          } else {
            if (this.previewHat != null) {
              if (getCosmeticById(this.previewHat.getId()) == null)
                count++; 
              setPreviewHat(null);
            } 
            if (this.previewBag != null) {
              if (getCosmeticById(this.previewBag.getId()) == null)
                count++; 
              setPreviewBag(null);
            } 
            if (this.previewWStick != null) {
              if (getCosmeticById(this.previewWStick.getId()) == null)
                count++; 
              setPreviewWStick(null);
            } 
            if (this.previewBalloon != null) {
              if (getCosmeticById(this.previewBalloon.getId()) == null)
                count++; 
              setPreviewBalloon(null);
            } 
            if (this.previewSpray != null) {
              if (getCosmeticById(this.previewSpray.getId()) == null)
                count++; 
              setPreviewSpray(null);
            } 
          } 
          if (count != 0) {
            if (count == 4) {
              Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
              this.isZone = false;
              this.sneak = false;
              this.spectator = false;
              setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
              player.setFlySpeed(getSpeedFly());
              plugin.getVersion().setCamera(player, (Entity)player);
              player.teleport(zone.getExit());
              setZone((Zone)null);
              return;
            } 
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
          } 
          this.isZone = false;
          this.sneak = false;
          this.spectator = false;
          setSpeedFly((getSpeedFly() == 0.0F) ? 0.1F : getSpeedFly());
          player.setFlySpeed(getSpeedFly());
          plugin.getVersion().setCamera(player, (Entity)player);
          player.teleport(zone.getExit());
          setZone((Zone)null);
          if (this.hat != null)
            this.hat.setHideCosmetic(false); 
          if (this.wStick != null)
            this.wStick.setHideCosmetic(false); 
        }17L);
  }
  
  public ItemStack getTokenInPlayer() {
    Player player = getOfflinePlayer().getPlayer();
    ItemStack mainHand = player.getInventory().getItemInMainHand();
    ItemStack offHand = player.getInventory().getItemInOffHand();
    if (!mainHand.getType().isAir()) {
      Token token = Token.getTokenByItem(mainHand);
      if (token == null)
        token = Token.getOldTokenByItem(mainHand); 
      if (token != null)
        return mainHand; 
    } 
    if (!offHand.getType().isAir()) {
      Token token = Token.getTokenByItem(offHand);
      if (token == null && this.wStick != null && !this.wStick.isCosmetic(offHand))
        token = Token.getOldTokenByItem(offHand); 
      if (token != null)
        return offHand; 
    } 
    int i;
    for (i = 0; i < 8; ) {
      ItemStack itemStack = player.getInventory().getItem(i);
      Token token = Token.getTokenByItem(itemStack);
      if (token == null) {
        i++;
        continue;
      } 
      return itemStack;
    } 
    for (i = 0; i < 8; ) {
      ItemStack itemStack = player.getInventory().getItem(i);
      Token token = Token.getOldTokenByItem(itemStack);
      if (token == null) {
        i++;
        continue;
      } 
      return itemStack;
    } 
    return null;
  }
  
  public boolean removeTokenInPlayer() {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    Player player = getOfflinePlayer().getPlayer();
    PlayerData playerData = getPlayer((OfflinePlayer)player);
    ItemStack mainHand = player.getInventory().getItemInMainHand();
    ItemStack offHand = player.getInventory().getItemInOffHand();
    if (mainHand.getType() != XMaterial.AIR.parseMaterial()) {
      Token token = Token.getTokenByItem(mainHand);
      if (token != null) {
        Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
        if (cosmetic == null)
          return false; 
        if (mainHand.getAmount() < token.getItemStack().getAmount()) {
          Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
          return false;
        } 
        if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
          if (cosmetic.hasPermission(player)) {
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
            return false;
          } 
        } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
          Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
          return false;
        } 
        if (mainHand.getAmount() > token.getItemStack().getAmount()) {
          ItemStack newItem = token.getItemStack().clone();
          newItem.setAmount(mainHand.getAmount() - token.getItemStack().getAmount());
          player.getInventory().setItemInMainHand(newItem);
          return true;
        } 
        player.getInventory().setItemInMainHand(XMaterial.AIR.parseItem());
        return true;
      } 
    } 
    if (offHand.getType() != XMaterial.AIR.parseMaterial()) {
      Token token = Token.getTokenByItem(offHand);
      if (token != null) {
        Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
        if (cosmetic == null)
          return false; 
        if (offHand.getAmount() < token.getItemStack().getAmount()) {
          Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
          return false;
        } 
        if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
          if (cosmetic.hasPermission(player)) {
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
            return false;
          } 
        } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
          Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
          return false;
        } 
        if (offHand.getAmount() > token.getItemStack().getAmount() && token.getItemStack().getAmount() > 1) {
          ItemStack newItem = token.getItemStack().clone();
          newItem.setAmount(offHand.getAmount() - token.getItemStack().getAmount());
          player.getInventory().setItemInOffHand(newItem);
          return true;
        } 
        player.getInventory().setItemInOffHand(XMaterial.AIR.parseItem());
        return true;
      } 
    } 
    for (int i = 0; i < 8; i++) {
      ItemStack itemStack = player.getInventory().getItem(i);
      if (itemStack != null) {
        Token token = Token.getTokenByItem(itemStack);
        if (token != null) {
          Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
          if (cosmetic == null)
            return false; 
          if (itemStack.getAmount() < token.getItemStack().getAmount()) {
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
            return false;
          } 
          if (!cosmetic.getPermission().isEmpty() && plugin.isLuckPerms()) {
            if (cosmetic.hasPermission(player)) {
              Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
              return false;
            } 
          } else if (playerData.getCosmeticById(token.getCosmetic()) != null) {
            Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
            return false;
          } 
          player.getInventory().removeItem(new ItemStack[] { token.getItemStack().clone() });
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public void saveItems() {
    if (!MagicCosmetics.getInstance().isZoneHideItems())
      return; 
    Player player = getOfflinePlayer().getPlayer();
    for (int i = 0; i < player.getInventory().getSize(); i++) {
      ItemStack itemStack = player.getInventory().getItem(i);
      if (itemStack == null) {
        this.inventory.put(Integer.valueOf(i), null);
      } else if (itemStack.getType() == XMaterial.AIR.parseMaterial()) {
        this.inventory.put(Integer.valueOf(i), null);
      } else {
        this.inventory.put(Integer.valueOf(i), itemStack.clone());
      } 
    } 
    player.getInventory().clear();
  }
  
  public int getFreeSlotInventory() {
    Player player = getOfflinePlayer().getPlayer();
    for (int i = 0; i < (player.getInventory().getStorageContents()).length; i++) {
      if (this.inventory.get(Integer.valueOf(i)) == null)
        return i; 
    } 
    return -1;
  }
  
  public void loadItems() {
    if (!MagicCosmetics.getInstance().isZoneHideItems())
      return; 
    Player player = getOfflinePlayer().getPlayer();
    for (Map.Entry<Integer, ItemStack> inv : this.inventory.entrySet())
      player.getInventory().setItem(((Integer)inv.getKey()).intValue(), inv.getValue()); 
    this.inventory.clear();
  }
  
  public Map<Integer, ItemStack> getInventory() {
    return this.inventory;
  }
  
  public void setZone(boolean zone) {
    this.isZone = zone;
  }
  
  public int getEquippedCount() {
    int count = 0;
    if (this.hat != null)
      count++; 
    if (this.bag != null)
      count++; 
    if (this.wStick != null)
      count++; 
    if (this.balloon != null)
      count++; 
    if (this.spray != null)
      count++; 
    return count;
  }
  
  public Set<Cosmetic> cosmeticsInUse() {
    Set<Cosmetic> cosmetics = new HashSet<>();
    if (this.hat != null)
      cosmetics.add(this.hat); 
    if (this.bag != null)
      cosmetics.add(this.bag); 
    if (this.wStick != null)
      cosmetics.add(this.wStick); 
    if (this.balloon != null)
      cosmetics.add(this.balloon); 
    if (this.spray != null)
      cosmetics.add(this.spray); 
    return cosmetics;
  }
  
  public float getSpeedFly() {
    return this.speedFly;
  }
  
  public void setSpeedFly(float speedFly) {
    this.speedFly = speedFly;
  }
  
  public boolean isSpectator() {
    return this.spectator;
  }
  
  public void toggleHiddeCosmetics() {
    this.hideCosmetics = !this.hideCosmetics;
    if (this.hideCosmetics) {
      hideAllCosmetics();
      return;
    } 
    showAllCosmetics();
  }
  
  public void hideAllCosmetics() {
    if (this.hat != null && !this.hat.isHideCosmetic())
      this.hat.setHideCosmetic(true); 
    if (this.bag != null && !this.bag.isHideCosmetic())
      this.bag.setHideCosmetic(true); 
    if (this.wStick != null && !this.wStick.isHideCosmetic())
      this.wStick.setHideCosmetic(true); 
    if (this.balloon != null && !this.balloon.isHideCosmetic())
      this.balloon.setHideCosmetic(true); 
  }
  
  public void showAllCosmetics() {
    if (this.hat != null && this.hat.isHideCosmetic())
      this.hat.setHideCosmetic(false); 
    if (this.bag != null && this.bag.isHideCosmetic())
      this.bag.setHideCosmetic(false); 
    if (this.wStick != null && this.wStick.isHideCosmetic())
      this.wStick.setHideCosmetic(false); 
    if (this.balloon != null && this.balloon.isHideCosmetic())
      this.balloon.setHideCosmetic(false); 
  }
  
  public boolean isHasInBlackList() {
    return this.hasInBlackList;
  }
  
  public void setHasInBlackList(boolean hasInBlackList) {
    this.hasInBlackList = hasInBlackList;
  }
  
  public void loadCosmetics(String loadCosmetics, String loadUseCosmetics) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    loadCosmetics(loadCosmetics);
    String[] cosmetics = loadUseCosmetics.split(";");
    String hat = cosmetics[0];
    String bag = cosmetics[1];
    String wStick = cosmetics[2];
    String balloon = cosmetics[3];
    String spray = cosmetics[4];
    plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> {
          setCosmetic(CosmeticType.HAT, getCosmeticById(hat));
          setCosmetic(CosmeticType.WALKING_STICK, getCosmeticById(wStick));
          setCosmetic(CosmeticType.BAG, getCosmeticById(bag));
        });
    setCosmetic(CosmeticType.BALLOON, getCosmeticById(balloon));
    setCosmetic(CosmeticType.SPRAY, getCosmeticById(spray));
  }
  
  public String getCosmeticsInUse() {
    return ((this.hat != null) ? this.hat.getId() : "0") + ";" + ((this.hat != null) ? this.hat.getId() : "0") + ";" + (
      (this.bag != null) ? this.bag.getId() : "0") + ";" + (
      (this.wStick != null) ? this.wStick.getId() : "0") + ";" + (
      (this.balloon != null) ? this.balloon.getId() : "0") + ";";
  }
  
  public void sendLoadPlayerData() {
    ByteArrayDataOutput output = ByteStreams.newDataOutput();
    output.writeUTF("load_cosmetics");
    output.writeUTF(getOfflinePlayer().getName());
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    plugin.getServer().sendPluginMessage((Plugin)plugin, "mc:player", output.toByteArray());
  }
  
  public void sendSavePlayerData() {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (!plugin.isProxy())
      return; 
    ByteArrayDataOutput output = ByteStreams.newDataOutput();
    output.writeUTF("save_cosmetics");
    output.writeUTF(getOfflinePlayer().getName());
    output.writeUTF(saveCosmetics());
    output.writeUTF(getCosmeticsInUse());
    plugin.getServer().sendPluginMessage((Plugin)plugin, "mc:player", output.toByteArray());
  }
  
  public IRangeManager getRangeManager() {
    return this.rangeManager;
  }
  
  public void verifyWorldBlackList(MagicCosmetics plugin) {
    if (getEquippedCount() < 1)
      return; 
    Player player = getOfflinePlayer().getPlayer();
    if (player == null)
      return; 
    World world = player.getWorld();
    if (plugin.getWorldsBlacklist().contains(world.getName())) {
      if (isHasInBlackList())
        return; 
      setHasInBlackList(true);
      hideAllCosmetics();
      PlayerChangeBlacklistEvent playerChangeBlacklistEvent = new PlayerChangeBlacklistEvent(player, isHasInBlackList());
      plugin.getServer().getPluginManager().callEvent((Event)playerChangeBlacklistEvent);
      return;
    } 
    if (!isHasInBlackList())
      return; 
    setHasInBlackList(false);
    showAllCosmetics();
    PlayerChangeBlacklistEvent callEvent = new PlayerChangeBlacklistEvent(player, isHasInBlackList());
    plugin.getServer().getPluginManager().callEvent((Event)callEvent);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\PlayerData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */