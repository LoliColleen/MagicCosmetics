package com.francobm.magicosmetics.nms.v1_18_R2.cache;

import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerBagHandler extends PlayerBag {
  private final EntityArmorStand armorStand;
  
  private final double distance;
  
  private final EntityPlayer entityPlayer;
  
  public PlayerBagHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
    this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = p.getUniqueId();
    this.backPackItem = backPackItem;
    this.backPackItemForMe = backPackItemForMe;
    this.distance = distance;
    this.height = height;
    this.ids = new ArrayList();
    this.rangeManager = rangeManager;
    Player player = getPlayer();
    this.entityPlayer = ((CraftPlayer)player).getHandle();
    WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
    this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world);
    this.backpackId = this.armorStand.ae();
    this.armorStand.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
    this.armorStand.j(true);
    this.armorStand.m(true);
    this.armorStand.t(true);
  }
  
  public void spawn(Player player) {
    if (this.hideViewers.contains(player.getUniqueId()))
      return; 
    Player owner = getPlayer();
    if (owner == null)
      return; 
    if (player.getUniqueId().equals(owner.getUniqueId())) {
      spawnSelf(owner);
      return;
    } 
    Location location = owner.getLocation();
    this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
    sendPackets(player, getBackPackSpawn(this.backPackItem));
  }
  
  public void spawnSelf(Player player) {
    Player owner = getPlayer();
    if (owner == null)
      return; 
    Location location = owner.getLocation();
    this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
    sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
    if (this.height > 0.0F) {
      int i;
      for (i = 0; i < this.height; i++) {
        EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.b, (World)((CraftWorld)player.getWorld()).getHandle());
        entityAreaEffectCloud.a(0.0F);
        entityAreaEffectCloud.j(true);
        entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
        this.ids.add(Integer.valueOf(entityAreaEffectCloud.ae()));
      } 
      for (i = 0; i < this.height; i++) {
        if (i == 0) {
          addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
        } else {
          addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
        } 
      } 
      addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.ae());
    } else {
      addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.ae());
    } 
    setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
  }
  
  public void spawn(boolean exception) {
    for (Player player : getPlayersInRange()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      spawn(player);
    } 
  }
  
  public void remove() {
    for (Player player : getPlayersInRange())
      remove(player); 
  }
  
  public void remove(Player player) {
    if (player.getUniqueId().equals(this.uuid)) {
      sendPackets(player, getBackPackDismount(true));
      this.ids.clear();
      return;
    } 
    sendPackets(player, getBackPackDismount(false));
  }
  
  public void addPassenger(boolean exception) {
    List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.ae());
    for (Player player : getPlayersInRange()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      sendPackets(player, backPack);
    } 
  }
  
  public void addPassenger(Player player, int entity, int passenger) {
    sendPackets(player, getBackPackMountPacket(entity, passenger));
  }
  
  public void setItemOnHelmet(Player player, ItemStack itemStack) {
    sendPackets(player, getBackPackHelmetPacket(itemStack));
  }
  
  public void lookEntity(float yaw, float pitch, boolean all) {
    Player owner = getPlayer();
    if (owner == null)
      return; 
    if (all) {
      for (Player player : getPlayersInRange())
        sendPackets(player, getBackPackRotationPackets(yaw)); 
      return;
    } 
    sendPackets(owner, getBackPackRotationPackets(yaw));
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
  
  public double getDistance() {
    return this.distance;
  }
  
  public Entity getEntity() {
    return (Entity)this.armorStand.getBukkitEntity();
  }
  
  private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.armorStand);
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true);
    PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
    PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.ae(), Collections.singletonList(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(backpackItem))));
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
  }
  
  private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.ae(), entityAreaEffectCloud.ai(), true);
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
  }
  
  private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
    List<Packet<?>> packets = new ArrayList<>();
    if (!removeClouds) {
      PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() });
      return (List)Collections.singletonList(backPackDestroy);
    } 
    for (Integer id : this.ids) {
      packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
    } 
    packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() }));
    return packets;
  }
  
  private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
    PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
          packetDataSerializer.d(entity);
          packetDataSerializer.a(new int[] { passenger });
          return new PacketPlayOutMount(packetDataSerializer);
        });
    return (List)Collections.singletonList(packetPlayOutMount);
  }
  
  private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
    ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
    list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
    return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ae(), list));
  }
  
  private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
    return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ae(), pairs));
  }
  
  private List<Packet<?>> getBackPackRotationPackets(float yaw) {
    PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
    PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
  }
  
  private void sendPackets(Player player, List<Packet<?>> packets) {
    ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).b);
    if (pipeline == null)
      return; 
    for (Packet<?> packet : packets)
      pipeline.write(packet); 
    pipeline.flush();
  }
  
  private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
    return playerConnection.a.m.pipeline();
  }
  
  @FunctionalInterface
  private static interface UnsafeFunction<K, T> {
    T apply(K param1K) throws Exception;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R2\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */