package com.francobm.magicosmetics.nms.v1_16_R3.cache;

import com.francobm.magicosmetics.nms.spray.CustomSpray;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityItemFrame;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class CustomSprayHandler extends CustomSpray {
  private final EntityItemFrame itemFrame;
  
  private final Location location;
  
  private final ItemStack itemStack;
  
  private final EnumDirection enumDirection;
  
  private final MapView mapView;
  
  private final int rotation;
  
  public CustomSprayHandler(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
    this.players = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = player.getUniqueId();
    customSprays.put(this.uuid, this);
    WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
    this.enumDirection = getEnumDirection(blockFace);
    this.itemFrame = new EntityItemFrame(EntityTypes.ITEM_FRAME, (World)world);
    this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
    this.location = location;
    this.itemStack = CraftItemStack.asNMSCopy(itemStack);
    this.mapView = mapView;
    this.rotation = rotation;
    this.itemFrame.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
  }
  
  public void spawn(Player player) {
    if (this.players.contains(player.getUniqueId())) {
      if (!player.getWorld().equals(this.location.getWorld()))
        remove(player); 
      return;
    } 
    if (!player.getWorld().equals(this.location.getWorld()))
      return; 
    this.itemFrame.setInvisible(true);
    this.itemFrame.setInvulnerable(true);
    this.itemFrame.setItem(this.itemStack, true, false);
    this.itemFrame.setPositionRotation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    this.itemFrame.setDirection(this.enumDirection);
    this.itemFrame.setRotation(this.rotation);
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
    connection.sendPacket((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.c()));
    connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.getId(), this.itemFrame.getDataWatcher(), true));
    if (this.mapView != null)
      player.sendMap(this.mapView); 
    this.players.add(player.getUniqueId());
  }
  
  public void spawn(boolean exception) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      spawn(player);
    } 
  }
  
  public void remove() {
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      if (!this.players.contains(player.getUniqueId()))
        continue; 
      remove(player);
    } 
    customSprays.remove(this.uuid);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
    connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.getId() }));
    this.players.remove(player.getUniqueId());
  }
  
  private EnumDirection getEnumDirection(BlockFace facing) {
    switch (facing) {
      case NORTH:
      case NORTH_EAST:
      case NORTH_WEST:
      case NORTH_NORTH_EAST:
      case NORTH_NORTH_WEST:
        return EnumDirection.NORTH;
      case SOUTH:
      case SOUTH_EAST:
      case SOUTH_WEST:
      case SOUTH_SOUTH_EAST:
      case SOUTH_SOUTH_WEST:
        return EnumDirection.SOUTH;
      case WEST:
      case WEST_NORTH_WEST:
      case WEST_SOUTH_WEST:
        return EnumDirection.WEST;
      case EAST:
      case EAST_NORTH_EAST:
      case EAST_SOUTH_EAST:
        return EnumDirection.EAST;
      case DOWN:
        return EnumDirection.DOWN;
    } 
    return EnumDirection.UP;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */