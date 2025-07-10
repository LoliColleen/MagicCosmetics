package com.francobm.magicosmetics.cache.cosmetics;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.api.SprayKeys;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.events.SprayDrawingEvent;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import com.francobm.magicosmetics.utils.Utils;
import java.awt.image.BufferedImage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

public class Spray extends Cosmetic {
  private CustomSpray customSpray;
  
  private BukkitTask bukkitTask;
  
  private BufferedImage image;
  
  private boolean itemImage;
  
  private boolean paint = false;
  
  private long coolDown;
  
  public Spray(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, String permission, boolean texture, BufferedImage image, boolean itemImage, boolean hideMenu, boolean useEmote, NamespacedKey namespacedKey) {
    super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
    this.itemImage = itemImage;
    if (image == null) {
      this.image = null;
      return;
    } 
    this.image = Utils.deepCopy(image);
  }
  
  protected void updateCosmetic(Cosmetic cosmetic) {
    super.updateCosmetic(cosmetic);
    Spray spray = (Spray)cosmetic;
    this.itemImage = spray.itemImage;
    if (spray.image == null) {
      this.image = null;
      return;
    } 
    this.image = Utils.deepCopy(spray.image);
  }
  
  public void draw(Player player, BlockFace blockFace, Location location, int rotation) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    remove();
    if (this.itemImage) {
      ItemStack item = getItemColor(player);
      ItemMeta itemMeta = item.getItemMeta();
      itemMeta.setDisplayName("");
      item.setItemMeta(itemMeta);
      Utils.sendSound(player, Sound.getSound("spray"));
      this.customSpray = plugin.getVersion().createCustomSpray(player, location, blockFace, item, null, rotation);
      this.customSpray.spawn(player);
      this.customSpray.setPreview(true);
      return;
    } 
    if (this.image != null) {
      ItemStack map = Utils.getMapImage(player, this.image, this);
      MapView mapView = ((MapMeta)map.getItemMeta()).getMapView();
      Utils.sendSound(player, Sound.getSound("spray"));
      this.customSpray = plugin.getVersion().createCustomSpray(player, location, blockFace, map.clone(), mapView, rotation);
      this.customSpray.spawn(player);
      this.customSpray.setPreview(true);
    } 
  }
  
  public void draw(Player player, SprayKeys key) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (plugin.getSprayCooldown() > 0) {
      if (this.coolDown > System.currentTimeMillis()) {
        int seconds = (int)((this.coolDown - System.currentTimeMillis()) / 1000L);
        Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
        return;
      } 
      long milliseconds = plugin.getSprayCooldown() * 1000L;
      this.coolDown = System.currentTimeMillis() + milliseconds;
    } 
    remove();
    if (this.itemImage) {
      int rotation;
      ItemStack item = getItemColor(player);
      ItemMeta itemMeta = item.getItemMeta();
      itemMeta.setDisplayName("");
      item.setItemMeta(itemMeta);
      Location location = player.getEyeLocation();
      RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10.0D, FluidCollisionMode.ALWAYS, false, 1.0D, entity -> false);
      if (result == null)
        return; 
      if (result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME)
        return; 
      if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
        rotation = Utils.getRotation(player.getLocation().getYaw(), false) * 90;
      } else {
        rotation = 0;
      } 
      SprayDrawingEvent event = new SprayDrawingEvent(player, result.getHitBlock(), key);
      Bukkit.getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
      Location frameLoc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
      Utils.sendAllSound(frameLoc, Sound.getSound("spray"));
      this.customSpray = plugin.getVersion().createCustomSpray(player, frameLoc, result.getHitBlockFace(), item, null, rotation);
      update();
      this.bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)plugin, () -> {
            if (this.customSpray == null) {
              this.bukkitTask.cancel();
              return;
            } 
            remove();
          }plugin
          
          .getSprayStayTime());
      return;
    } 
    if (this.image != null) {
      int rotation;
      ItemStack map = Utils.getMapImage(player, this.image, this);
      MapView mapView = ((MapMeta)map.getItemMeta()).getMapView();
      Location location = player.getEyeLocation();
      RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10.0D, FluidCollisionMode.ALWAYS, false, 1.0D, entity -> false);
      if (result == null)
        return; 
      if (result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME)
        return; 
      if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
        rotation = Utils.getRotation(player.getLocation().getYaw(), false) * 45;
      } else {
        rotation = 0;
      } 
      SprayDrawingEvent event = new SprayDrawingEvent(player, result.getHitBlock(), key);
      Bukkit.getPluginManager().callEvent((Event)event);
      if (event.isCancelled())
        return; 
      Location frameLoc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
      Utils.sendAllSound(frameLoc, Sound.getSound("spray"));
      this.customSpray = plugin.getVersion().createCustomSpray(player, frameLoc, result.getHitBlockFace(), map.clone(), mapView, rotation);
      update();
    } 
    this.bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)plugin, () -> {
          if (this.customSpray == null) {
            this.bukkitTask.cancel();
            return;
          } 
          remove();
        }plugin
        
        .getSprayStayTime());
  }
  
  public void update() {
    if (this.customSpray == null)
      return; 
    if (this.customSpray.isPreview())
      return; 
    this.customSpray.spawn(false);
  }
  
  public void lendToEntity() {}
  
  public void hide(Player player) {}
  
  public void show(Player player) {}
  
  public void remove() {
    if (this.customSpray != null) {
      this.customSpray.setPreview(false);
      this.customSpray.remove();
      this.customSpray = null;
    } 
    if (this.bukkitTask != null) {
      this.bukkitTask.cancel();
      this.bukkitTask = null;
    } 
  }
  
  public void clearClose() {
    if (this.customSpray != null) {
      this.customSpray.setPreview(false);
      this.customSpray.remove();
      this.customSpray = null;
    } 
    if (this.bukkitTask != null) {
      this.bukkitTask.cancel();
      this.bukkitTask = null;
    } 
  }
  
  public BufferedImage getImage() {
    return this.image;
  }
  
  public boolean isPaint() {
    return this.paint;
  }
  
  public void setPaint(boolean paint) {
    this.paint = paint;
  }
  
  public boolean isItemImage() {
    return this.itemImage;
  }
  
  public void spawn(Player player) {
    if (this.customSpray == null)
      return; 
    this.customSpray.spawn(player);
  }
  
  public void despawn(Player player) {
    if (this.customSpray == null)
      return; 
    this.customSpray.remove(player);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\Spray.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */