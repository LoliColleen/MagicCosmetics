package com.francobm.magicosmetics.managers;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.utils.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class ZonesManager {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public void saveZone(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (zone.getNpc() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC Location!");
      return;
    } 
    if (zone.getBalloon() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC's Balloon Location!");
      return;
    } 
    if (zone.getEnter() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Enter Location!");
      return;
    } 
    if (zone.getExit() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Exit Location!");
      return;
    } 
    if (zone.getCorn1() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn1 Location!");
      return;
    } 
    if (zone.getCorn2() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn2 Location!");
      return;
    } 
    if (zone.getSprayLoc() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Spray Location!");
      return;
    } 
    Zone.saveZone(name);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void addZone(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (Zone.getZone(name) != null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone.addZone(name);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
    giveCorn(player, name);
  }
  
  public void removeZone(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (Zone.getZone(name) == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone.removeZone(name);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void giveCorn(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.giveCorns(player);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void setSpray(Player player, String name) {
    int rotation;
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
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
    Location loc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
    zone.setSprayLoc(loc);
    zone.setSprayFace(result.getHitBlockFace());
    zone.setRotation(rotation);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void setBalloonNPC(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.setBalloon(player.getLocation());
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void setZoneNPC(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.setNpc(player.getLocation());
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void setZoneEnter(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.setEnter(player.getLocation().clone());
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void setZoneExit(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.setExit(player.getLocation());
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void disableZone(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    zone.setActive(false);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void enableZone(Player player, String name) {
    if (!player.hasPermission("magicosmetics.zones")) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    Zone zone = Zone.getZone(name);
    if (zone == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
      return;
    } 
    if (zone.getNpc() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC Location!");
      return;
    } 
    if (zone.getBalloon() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC's Balloon Location!");
      return;
    } 
    if (zone.getEnter() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Enter Location!");
      return;
    } 
    if (zone.getExit() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Exit Location!");
      return;
    } 
    if (zone.getCorn1() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn1 Location!");
      return;
    } 
    if (zone.getCorn2() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn2 Location!");
      return;
    } 
    if (zone.getSprayLoc() == null) {
      Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Spray Location!");
      return;
    } 
    if (this.plugin.getUser() == null)
      return; 
    zone.setActive(true);
    Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
  }
  
  public void exitZone(Player player) {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    playerData.exitZone();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\managers\ZonesManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */