package com.francobm.magicosmetics.listeners;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.SprayKeys;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.cache.cosmetics.CosmeticInventory;
import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
import com.francobm.magicosmetics.events.PlayerDataLoadEvent;
import com.francobm.magicosmetics.utils.Utils;
import com.francobm.magicosmetics.utils.XMaterial;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    this.plugin.getVersion().getPacketReader().injectPlayer(player);
    if (this.plugin.isHuskSync())
      return; 
    this.plugin.getSql().loadPlayerAsync(player).thenAccept(playerData -> {
          if (this.plugin.isProxy()) {
            Objects.requireNonNull(playerData);
            this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, playerData::sendLoadPlayerData);
          } 
        });
  }
  
  @EventHandler
  public void onLoadData(PlayerDataLoadEvent event) {
    PlayerData playerData = event.getPlayerData();
    playerData.verifyWorldBlackList(this.plugin);
  }
  
  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (!playerData.isZone())
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    this.plugin.getVersion().getPacketReader().removePlayer(player);
    if (playerData.isZone())
      playerData.exitZoneSync(); 
    this.plugin.getSql().savePlayerAsync(playerData);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(PlayerTeleportEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.isZone()) {
      if (!playerData.isSpectator())
        return; 
      event.setCancelled(true);
    } 
    playerData.clearCosmeticsInUse(false);
  }
  
  @EventHandler
  public void onUnleash(PlayerUnleashEntityEvent event) {
    if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
      return; 
    if (!event.getEntity().hasMetadata("cosmetics"))
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler
  public void OnLeash(PlayerLeashEntityEvent event) {
    if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
      return; 
    if (!event.getEntity().hasMetadata("cosmetics"))
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    playerData.activeCosmeticsInventory();
  }
  
  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getSpray() != null) {
      if (this.plugin.getSprayKey() == null)
        return; 
      if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_Q))
        return; 
      if (!player.isSneaking())
        return; 
      event.setCancelled(true);
      playerData.draw(this.plugin.getSprayKey());
    } 
    if (!Utils.isNewerThan1206())
      return; 
    String nbt = this.plugin.getVersion().isNBTCosmetic(event.getItemDrop().getItemStack());
    if (nbt == null || nbt.isEmpty())
      return; 
    event.getItemDrop().remove();
  }
  
  @EventHandler
  public void onSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    if (!event.isSneaking())
      return; 
    this.plugin.getZonesManager().exitZone(player);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDead(PlayerDeathEvent event) {
    Player player = event.getEntity();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData == null)
      return; 
    playerData.clearCosmeticsInUse(false);
    if (event.getKeepInventory())
      return; 
    Iterator<ItemStack> stackList = event.getDrops().iterator();
    while (stackList.hasNext()) {
      ItemStack itemStack = stackList.next();
      if (itemStack == null)
        break; 
      if (playerData.getHat() != null && playerData.getHat().isCosmetic(itemStack)) {
        stackList.remove();
        continue;
      } 
      if (playerData.getWStick() != null && playerData.getWStick().isCosmetic(itemStack))
        stackList.remove(); 
    } 
    if (playerData.getHat() != null && playerData.getHat().getCurrentItemSaved() != null && 
      !event.getKeepInventory() && playerData.getHat().isOverlaps())
      event.getDrops().add(playerData.getHat().leftItemAndGet()); 
    if (playerData.getWStick() != null && playerData.getWStick().getCurrentItemSaved() != null && 
      !event.getKeepInventory() && playerData.getWStick().isOverlaps())
      event.getDrops().add(playerData.getWStick().leftItemAndGet()); 
  }
  
  @EventHandler
  public void onItemFrame(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (event.getHand() != EquipmentSlot.OFF_HAND)
      return; 
    if (playerData.getWStick() == null)
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onBlock(BlockPlaceEvent event) {
    if (event.getHand() != EquipmentSlot.OFF_HAND)
      return; 
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getWStick() == null)
      return; 
    if (!playerData.getWStick().isCosmetic(event.getItemInHand()))
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteractDupe(PlayerInteractEvent event) {
    if (!Utils.isNewerThan1206())
      return; 
    if (event.getHand() != EquipmentSlot.OFF_HAND)
      return; 
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
      return; 
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getWStick() == null)
      return; 
    ItemStack itemStack = event.getItem();
    if (itemStack == null)
      return; 
    String nbt = this.plugin.getVersion().isNBTCosmetic(itemStack);
    if (nbt == null || nbt.isEmpty())
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    ItemStack itemStack = event.getItem();
    if (itemStack != null) {
      if (itemStack.getType() == XMaterial.BLAZE_ROD.parseMaterial()) {
        String nbt = this.plugin.getVersion().isNBTCosmetic(itemStack);
        if (!nbt.startsWith("wand"))
          return; 
        Zone zone = Zone.getZone(nbt.substring(4));
        if (zone == null)
          return; 
        event.setCancelled(true);
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          Location location = event.getClickedBlock().getLocation();
          zone.setCorn1(location);
          player.sendMessage(this.plugin.prefix + this.plugin.prefix);
          return;
        } 
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
          Location location = event.getClickedBlock().getLocation();
          zone.setCorn2(location);
          player.sendMessage(this.plugin.prefix + this.plugin.prefix);
          return;
        } 
        return;
      } 
      if (itemStack.getType().toString().toUpperCase().endsWith("HELMET") && (
        event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && 
        playerData.getHat() != null) {
        if (playerData.getHat().isHideCosmetic())
          return; 
        event.setCancelled(true);
        ItemStack returnItem = playerData.getHat().changeItem(itemStack);
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
          player.getInventory().setItemInOffHand(returnItem);
        } else {
          player.getInventory().setItemInMainHand(returnItem);
        } 
      } 
    } 
    if (this.plugin.getSprayKey() == null)
      return; 
    if (playerData.getSpray() == null)
      return; 
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_RC))
        return; 
      if (!player.isSneaking())
        return; 
      playerData.draw(this.plugin.getSprayKey());
      event.setCancelled(true);
    } 
    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
      if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_LC))
        return; 
      if (!player.isSneaking())
        return; 
      playerData.draw(this.plugin.getSprayKey());
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChange(PlayerSwapHandItemsEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    ItemStack mainHand = event.getMainHandItem();
    if (playerData.getWStick() != null)
      event.setCancelled(true); 
    if (playerData.getSpray() == null)
      return; 
    if (this.plugin.getSprayKey() == null)
      return; 
    if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_F))
      return; 
    if (!player.isSneaking())
      return; 
    playerData.draw(this.plugin.getSprayKey());
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onAttack(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player))
      return; 
    if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
      return; 
    if (!event.getEntity().hasMetadata("cosmetics"))
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler
  public void playerHeld(PlayerItemHeldEvent event) {
    Player player = event.getPlayer();
    ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
    ItemStack oldItem = player.getInventory().getItem(event.getPreviousSlot());
    if (oldItem != null) {
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
      if (playerData.getHat() != null && 
        playerData.getHat().isCosmetic(oldItem))
        player.getInventory().removeItem(new ItemStack[] { oldItem }); 
      if (playerData.getWStick() != null && 
        playerData.getWStick().isCosmetic(oldItem))
        player.getInventory().removeItem(new ItemStack[] { oldItem }); 
    } 
    if (newItem != null) {
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
      if (playerData.getHat() != null && 
        playerData.getHat().isCosmetic(newItem))
        player.getInventory().removeItem(new ItemStack[] { newItem }); 
      if (playerData.getWStick() != null && 
        playerData.getWStick().isCosmetic(newItem))
        player.getInventory().removeItem(new ItemStack[] { newItem }); 
    } 
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void playerDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    Item item = event.getItemDrop();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getHat() != null && 
      playerData.getHat().isCosmetic(item.getItemStack())) {
      event.setCancelled(false);
      item.remove();
    } 
    if (playerData.getWStick() != null && 
      playerData.getWStick().isCosmetic(item.getItemStack())) {
      event.setCancelled(false);
      item.remove();
    } 
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    playerData.verifyWorldBlackList(this.plugin);
  }
  
  @EventHandler
  public void onInteractInventory(CosmeticInventoryUpdateEvent event) {
    Player player = event.getPlayer();
    Cosmetic cosmetic = event.getCosmetic();
    if (cosmetic.isHideCosmetic())
      return; 
    ItemStack itemStack = event.getItemToChange();
    CosmeticInventory cosmeticInventory = (CosmeticInventory)cosmetic;
    if (itemStack == null || itemStack.getType().isAir()) {
      if (!cosmeticInventory.isOverlaps())
        cosmeticInventory.setCurrentItemSaved(null); 
      cosmetic.update();
      return;
    } 
    if (this.plugin.getMagicCrates() != null && this.plugin.getMagicCrates().hasInCrate(player))
      return; 
    if (this.plugin.getMagicGestures() != null && this.plugin.getMagicGestures().hasInWardrobe(player))
      return; 
    boolean hasItemSaved = (cosmeticInventory.getCurrentItemSaved() != null);
    if (hasItemSaved && 
      itemStack.isSimilar(cosmeticInventory.getCurrentItemSaved()))
      return; 
    if (!cosmeticInventory.isOverlaps()) {
      if (cosmetic.isCosmetic(itemStack))
        return; 
      if (player.getInventory().getItemInMainHand().getType().isAir() || cosmetic.isCosmetic(player.getInventory().getItemInMainHand()))
        player.getInventory().setItemInMainHand(null); 
      cosmeticInventory.setCurrentItemSaved(itemStack);
      return;
    } 
    ItemStack oldItem = cosmeticInventory.changeItem(itemStack);
    if (oldItem == null) {
      if (cosmetic.isCosmetic(player.getInventory().getItemInMainHand()))
        player.getInventory().setItemInMainHand(null); 
      return;
    } 
    if (hasItemSaved && oldItem.isSimilar(cosmeticInventory.getCurrentItemSaved()))
      return; 
    if (itemStack.isSimilar(oldItem))
      return; 
    if (player.getOpenInventory().getType() == InventoryType.PLAYER) {
      player.setItemOnCursor(oldItem);
      return;
    } 
    if (player.getInventory().getItemInMainHand().getType().isAir() || cosmetic.isCosmetic(player.getInventory().getItemInMainHand())) {
      player.getInventory().setItemInMainHand(oldItem);
      return;
    } 
    player.getInventory().addItem(new ItemStack[] { oldItem });
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventory(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData == null)
      return; 
    if (event.getClickedInventory() == null)
      return; 
    if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
      if (playerData.getWStick() != null && event.getClick() == ClickType.SWAP_OFFHAND)
        event.setCancelled(true); 
      return;
    } 
    if (playerData.getWStick() != null) {
      if (playerData.getWStick().isHideCosmetic())
        return; 
      if (event.getClick() == ClickType.SWAP_OFFHAND) {
        event.setCancelled(true);
        return;
      } 
      if (event.getCursor() != null && 
        playerData.getWStick().isCosmetic(event.getCursor()))
        player.setItemOnCursor(null); 
      if (event.getSlotType() == InventoryType.SlotType.QUICKBAR && event.getSlot() == 40) {
        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.RIGHT || (playerData.getWStick().isCosmetic(event.getCurrentItem()) && event.getCursor() == null && playerData.getWStick().getCurrentItemSaved() == null) || (playerData.getWStick().isCosmetic(event.getCurrentItem()) && event.getCursor() != null && event.getCursor().getType().isAir() && playerData.getWStick().getCurrentItemSaved() == null)) {
          event.setCancelled(true);
          return;
        } 
        if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
          if (playerData.getWStick().getCurrentItemSaved() != null) {
            playerData.getWStick().dropItem((event.getClick() == ClickType.CONTROL_DROP));
            event.setCancelled(playerData.getWStick().isOverlaps());
          } 
          return;
        } 
        event.setCancelled(true);
        ItemStack returnItem = playerData.getWStick().changeItem((event.getCursor() != null && event.getCursor().getType().isAir()) ? null : event.getCursor());
        player.setItemOnCursor(returnItem);
        return;
      } 
    } 
    if (playerData.getHat() != null) {
      if (playerData.getHat().isHideCosmetic())
        return; 
      if (event.getCursor() != null && 
        playerData.getHat().isCosmetic(event.getCursor()))
        player.setItemOnCursor(null); 
      if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39) {
        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.RIGHT || (playerData.getHat().isCosmetic(event.getCurrentItem()) && event.getCursor() == null && playerData.getHat().getCurrentItemSaved() == null) || (playerData.getHat().isCosmetic(event.getCurrentItem()) && event.getCursor() != null && event.getCursor().getType().isAir() && playerData.getHat().getCurrentItemSaved() == null)) {
          event.setCancelled(true);
          return;
        } 
        if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
          if (playerData.getHat().getCurrentItemSaved() != null) {
            playerData.getHat().dropItem((event.getClick() == ClickType.CONTROL_DROP));
            event.setCancelled(playerData.getHat().isOverlaps());
          } 
          return;
        } 
        event.setCancelled(true);
        if (event.getCursor() == null || event.getCursor().getType().isAir() || event.getCursor().getType().name().endsWith("HELMET") || event.getCursor().getType().name().endsWith("HEAD") || player.hasPermission("magicosmetics.hat.use")) {
          ItemStack returnItem = playerData.getHat().changeItem((event.getCursor() != null && event.getCursor().getType().isAir()) ? null : event.getCursor());
          player.setItemOnCursor(returnItem);
        } 
      } 
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\PlayerListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */