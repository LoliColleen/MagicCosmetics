package com.francobm.magicosmetics.nms.v1_20_R2.cache;

import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EntityBagHandler extends EntityBag {
  private final EntityArmorStand armorStand;
  
  private final double distance;
  
  public EntityBagHandler(Entity entity, double distance) {
    this.players = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = entity.getUniqueId();
    this.distance = distance;
    this.entity = entity;
    entityBags.put(this.uuid, this);
    WorldServer world = ((CraftWorld)entity.getWorld()).getHandle();
    this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
    this.armorStand.b(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), 0.0F);
    this.armorStand.j(true);
    this.armorStand.m(true);
    this.armorStand.u(true);
  }
  
  public void spawnBag(Player player) {
    if (this.players.contains(player.getUniqueId())) {
      if (!getEntity().getWorld().equals(player.getWorld())) {
        remove(player);
        return;
      } 
      if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
        remove(player); 
      return;
    } 
    if (!getEntity().getWorld().equals(player.getWorld()))
      return; 
    if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
      return; 
    this.armorStand.m(true);
    this.armorStand.j(true);
    this.armorStand.u(true);
    Location location = getEntity().getLocation();
    this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand));
    this.armorStand.al().refresh(entityPlayer);
    addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
    this.players.add(player.getUniqueId());
  }
  
  public void spawnBag() {
    for (Player player : Bukkit.getOnlinePlayers())
      spawnBag(player); 
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
    entityBags.remove(this.uuid);
  }
  
  public void addPassenger() {
    for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) {
      UUID uuid = iterator.next();
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      Entity e = ((CraftEntity)this.entity).getHandle();
      PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
            packetDataSerializer.c(e.ah());
            packetDataSerializer.a(new int[] { this.armorStand.ah() });
            return new PacketPlayOutMount(packetDataSerializer);
          });
      entityPlayer.c.b((Packet)packetPlayOutMount);
    } 
  }
  
  public void addPassenger(Entity entity, Entity passenger) {
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      Entity e = ((CraftEntity)entity).getHandle();
      Entity pass = ((CraftEntity)passenger).getHandle();
      PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
            packetDataSerializer.d(e.ah());
            packetDataSerializer.a(new int[] { pass.ah() });
            return new PacketPlayOutMount(packetDataSerializer);
          });
      entityPlayer.c.b((Packet)packetPlayOutMount);
    } 
  }
  
  public void addPassenger(Player player, Entity entity, Entity passenger) {
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    Entity e = ((CraftEntity)entity).getHandle();
    Entity pass = ((CraftEntity)passenger).getHandle();
    PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
          packetDataSerializer.d(e.ah());
          packetDataSerializer.a(new int[] { pass.ah() });
          return new PacketPlayOutMount(packetDataSerializer);
        });
    entityPlayer.c.b((Packet)packetPlayOutMount);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
    connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ah() }));
    this.players.remove(player.getUniqueId());
  }
  
  public void setItemOnHelmet(ItemStack itemStack) {
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
      connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ah(), list));
    } 
  }
  
  public void lookEntity() {
    float yaw = getEntity().getLocation().getYaw();
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
      connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ah(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
    } 
  }
  
  private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
    PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
    T result = null;
    try {
      result = callback.apply(data);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      data.release();
    } 
    return result;
  }
  
  @FunctionalInterface
  private static interface UnsafeFunction<K, T> {
    T apply(K param1K) throws Exception;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R2\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */