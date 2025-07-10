package com.francobm.magicosmetics.nms.v1_20_R3.cache;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
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
    this.itemFrame = new EntityItemFrame(EntityTypes.ag, (World)world);
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
    this.itemFrame.b(this.rotation);
    sendPackets(player, spawnItemFrame());
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
    sendPackets(player, Collections.singletonList(destroyItemFrame()));
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
  
  private List<Packet<?>> spawnItemFrame() {
    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.d());
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemFrame.aj(), this.itemFrame.an().c());
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
  }
  
  private Packet<?> destroyItemFrame() {
    return (Packet<?>)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.aj() });
  }
  
  private void sendPackets(Player player, List<Packet<?>> packets) {
    ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).c);
    if (pipeline == null)
      return; 
    for (Packet<?> packet : packets)
      pipeline.write(packet); 
    pipeline.flush();
  }
  
  private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
      String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
      String methodName = "getConnection";
      try {
        Class<?> clazz = Class.forName(className);
        Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
        Method method = clazz.getMethod(methodName, typeParameters);
        Object[] parameters = { playerConnection.e };
        NetworkManager result = (NetworkManager)method.invoke(null, parameters);
        return result.n.pipeline();
      } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
        return null;
      } 
    } 
    try {
      Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
      privateNetworkManager.setAccessible(true);
      NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
      return networkManager.n.pipeline();
    } catch (NoSuchFieldException|IllegalAccessException e) {
      return null;
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R3\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */