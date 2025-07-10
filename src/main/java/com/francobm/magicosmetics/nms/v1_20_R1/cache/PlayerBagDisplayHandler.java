package com.francobm.magicosmetics.nms.v1_20_R1.cache;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.NetworkManager;
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
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PlayerBagDisplayHandler extends PlayerBag {
  private final Display.ItemDisplay itemDisplay;
  
  private final double distance;
  
  public PlayerBagDisplayHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
    this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = p.getUniqueId();
    this.distance = distance;
    this.height = height;
    this.ids = new ArrayList();
    this.backPackItem = backPackItem;
    this.backPackItemForMe = backPackItemForMe;
    this.rangeManager = rangeManager;
    Player player = getPlayer();
    WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
    this.itemDisplay = new Display.ItemDisplay(EntityTypes.ae, (World)world);
    this.itemDisplay.a(CraftItemStack.asNMSCopy(backPackItem));
    this.itemDisplay.a(ItemDisplayContext.f);
    this.itemDisplay.a(new Transformation(new Vector3f(0.0F, height, 0.0F), new Quaternionf(), new Vector3f(0.6F, 0.6F, 0.6F), new Quaternionf()));
    this.itemDisplay.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
  }
  
  public void spawn(Player player) {
    Player owner = getPlayer();
    if (owner == null)
      return; 
    Location location = owner.getLocation();
    this.itemDisplay.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
    sendPackets(player, getBackPackSpawn());
    addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.itemDisplay.af());
    setItemOnHelmet(player, this.backPackItem);
  }
  
  public void spawnSelf(Player player) {
    Player owner = getPlayer();
    if (owner == null)
      return; 
    Location location = owner.getLocation();
    this.itemDisplay.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
    sendPackets(player, getBackPackSpawn());
    if (this.height > 0.0F) {
      int i;
      for (i = 0; i < this.height; i++) {
        EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
        entityAreaEffectCloud.a(0.0F);
        entityAreaEffectCloud.j(true);
        entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
        this.ids.add(Integer.valueOf(entityAreaEffectCloud.af()));
      } 
      for (i = 0; i < this.height; i++) {
        if (i == 0) {
          addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
        } else {
          addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
        } 
      } 
      addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.itemDisplay.af());
    } else {
      addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.itemDisplay.af());
    } 
    setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
  }
  
  public void spawn(boolean exception) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      spawn(player);
    } 
  }
  
  public void remove() {}
  
  public void remove(Player player) {
    if (player.getUniqueId().equals(this.uuid)) {
      sendPackets(player, getBackPackDismount(true));
      this.ids.clear();
      return;
    } 
    sendPackets(player, getBackPackDismount(false));
  }
  
  public void addPassenger(boolean exception) {
    List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.itemDisplay.af());
  }
  
  public void addPassenger(Player player, int entity, int passenger) {
    sendPackets(player, getBackPackMountPacket(entity, passenger));
  }
  
  public void setItemOnHelmet(Player player, ItemStack itemStack) {
    sendPackets(player, getBackPackHelmetPacket(itemStack));
  }
  
  private List<Packet<?>> getBackPackSpawn() {
    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemDisplay);
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemDisplay.af(), this.itemDisplay.aj().c());
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
  }
  
  private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.af(), entityAreaEffectCloud.aj().c());
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
  }
  
  private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
    List<Packet<?>> packets = new ArrayList<>();
    if (!removeClouds) {
      PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.itemDisplay.af() });
      return (List)Collections.singletonList(backPackDestroy);
    } 
    for (Integer id : this.ids) {
      packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
    } 
    packets.add(new PacketPlayOutEntityDestroy(new int[] { this.itemDisplay.af() }));
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
    return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.itemDisplay.af(), list));
  }
  
  private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
    return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.itemDisplay.af(), pairs));
  }
  
  public void lookEntity(float yaw, float pitch, boolean all) {
    Player owner = getPlayer();
    if (owner == null)
      return; 
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
    return (Entity)this.itemDisplay.getBukkitEntity();
  }
  
  private List<Packet<?>> getBackPackRotationPackets(float yaw) {
    PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.itemDisplay, (byte)(int)(yaw * 256.0F / 360.0F));
    PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.itemDisplay.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
    return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
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
        Object[] parameters = { playerConnection.b };
        NetworkManager result = (NetworkManager)method.invoke(null, parameters);
        return result.m.pipeline();
      } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
        throw new RuntimeException(e);
      } 
    } 
    try {
      Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
      privateNetworkManager.setAccessible(true);
      NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
      return networkManager.m.pipeline();
    } catch (NoSuchFieldException|IllegalAccessException e) {
      Bukkit.getLogger().severe("Error: Channel Pipeline not found");
      return null;
    } 
  }
  
  @FunctionalInterface
  private static interface UnsafeFunction<K, T> {
    T apply(K param1K) throws Exception;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R1\cache\PlayerBagDisplayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */