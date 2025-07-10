package com.francobm.magicosmetics.nms.v1_18_R1.cache;

import com.francobm.magicosmetics.nms.spray.CustomSpray;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
    this.itemFrame = new EntityItemFrame(EntityTypes.R, (World)world);
    this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
    this.location = location;
    this.itemStack = CraftItemStack.asNMSCopy(itemStack);
    this.mapView = mapView;
    this.rotation = rotation;
    this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
  }
  
  public void spawn(Player player) {
    if (this.players.contains(player.getUniqueId())) {
      if (!player.getWorld().equals(this.location.getWorld()))
        remove(player); 
      return;
    } 
    if (!player.getWorld().equals(this.location.getWorld()))
      return; 
    this.itemFrame.j(true);
    this.itemFrame.m(true);
    this.itemFrame.setItem(this.itemStack, true, false);
    this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    this.itemFrame.a(this.enumDirection);
    this.itemFrame.a(this.rotation);
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
    connection.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.b()));
    connection.a((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.ae(), this.itemFrame.ai(), true));
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
      remove(player);
    } 
    customSprays.remove(this.uuid);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
    connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.ae() }));
    this.players.remove(player.getUniqueId());
  }
  
  private EnumDirection getEnumDirection(BlockFace facing) {
    switch (facing) {
      case NORTH:
        return EnumDirection.c;
      case SOUTH:
        return EnumDirection.d;
      case WEST:
        return EnumDirection.e;
      case EAST:
        return EnumDirection.f;
      case DOWN:
        return EnumDirection.a;
    } 
    return EnumDirection.b;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */