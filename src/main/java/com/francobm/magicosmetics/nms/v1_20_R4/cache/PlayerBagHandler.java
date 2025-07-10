/*     */ package com.francobm.magicosmetics.nms.v1_20_R4.cache;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAreaEffectCloud;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PlayerBagHandler extends PlayerBag {
/*     */   private final EntityArmorStand armorStand;
/*     */   
/*     */   public PlayerBagHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/*  42 */     this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
/*  43 */     this.uuid = p.getUniqueId();
/*  44 */     this.distance = distance;
/*  45 */     this.height = height;
/*  46 */     this.ids = new ArrayList();
/*  47 */     this.backPackItem = backPackItem;
/*  48 */     this.backPackItemForMe = backPackItemForMe;
/*  49 */     this.rangeManager = rangeManager;
/*  50 */     Player player = getPlayer();
/*  51 */     this.entityPlayer = ((CraftPlayer)player).getHandle();
/*  52 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*     */     
/*  54 */     this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
/*  55 */     this.backpackId = this.armorStand.al();
/*  56 */     this.armorStand.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
/*  57 */     this.armorStand.k(true);
/*  58 */     this.armorStand.n(true);
/*  59 */     this.armorStand.u(true);
/*     */   }
/*     */   private final double distance; private final EntityPlayer entityPlayer;
/*     */   
/*     */   public void spawn(Player player) {
/*  64 */     if (this.hideViewers.contains(player.getUniqueId()))
/*  65 */       return;  Player owner = getPlayer();
/*  66 */     if (owner == null)
/*  67 */       return;  if (player.getUniqueId().equals(owner.getUniqueId())) {
/*  68 */       spawnSelf(owner);
/*     */       return;
/*     */     } 
/*  71 */     Location location = owner.getLocation();
/*  72 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  74 */     sendPackets(player, getBackPackSpawn(this.backPackItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnSelf(Player player) {
/*  79 */     Player owner = getPlayer();
/*  80 */     if (owner == null)
/*     */       return; 
/*  82 */     Location location = owner.getLocation();
/*  83 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  85 */     sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
/*  86 */     if (this.height > 0.0F) {
/*  87 */       int i; for (i = 0; i < this.height; i++) {
/*  88 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.b, (World)((CraftWorld)player.getWorld()).getHandle());
/*  89 */         entityAreaEffectCloud.a(0.0F);
/*  90 */         entityAreaEffectCloud.k(true);
/*  91 */         entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  92 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/*  93 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.al()));
/*     */       } 
/*  95 */       for (i = 0; i < this.height; i++) {
/*  96 */         if (i == 0) {
/*  97 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/* 100 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/* 102 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.al());
/*     */     } else {
/* 104 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.al());
/*     */     } 
/* 106 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/* 111 */     for (Player player : getPlayersInRange()) {
/* 112 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 113 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 119 */     for (Player player : getPlayersInRange()) {
/* 120 */       remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 126 */     if (player.getUniqueId().equals(this.uuid)) {
/* 127 */       sendPackets(player, getBackPackDismount(true));
/* 128 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 131 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 136 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.al());
/* 137 */     for (Player player : getPlayersInRange()) {
/* 138 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 139 */         continue;  sendPackets(player, backPack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 145 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 150 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
/* 154 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.armorStand);
/* 155 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c());
/* 156 */     PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
/* 157 */     PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.al(), Collections.singletonList(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(backpackItem))));
/* 158 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 162 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 163 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.al(), entityAreaEffectCloud.ap().c());
/* 164 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 168 */     List<Packet<?>> packets = new ArrayList<>();
/* 169 */     if (!removeClouds) {
/* 170 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.al() });
/* 171 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 173 */     for (Integer id : this.ids) {
/* 174 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 176 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.al() }));
/* 177 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 181 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.c(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return (PacketPlayOutMount)PacketPlayOutMount.a.decode(packetDataSerializer);
/*     */         });
/* 186 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 190 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 191 */     list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 192 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.al(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 196 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.al(), pairs));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 201 */     Player owner = getPlayer();
/* 202 */     if (owner == null)
/* 203 */       return;  if (all) {
/* 204 */       for (Player player : getPlayersInRange()) {
/* 205 */         sendPackets(player, getBackPackRotationPackets(yaw));
/*     */       }
/*     */       return;
/*     */     } 
/* 209 */     sendPackets(owner, getBackPackRotationPackets(yaw));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 213 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 214 */     T result = null;
/*     */     try {
/* 216 */       result = callback.apply(data);
/* 217 */     } catch (Exception e) {
/* 218 */       e.printStackTrace();
/*     */     } finally {
/* 220 */       data.release();
/*     */     } 
/* 222 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 231 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 236 */     return (Entity)this.armorStand.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 240 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
/* 241 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.al(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 242 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 246 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).c);
/* 247 */     if (pipeline == null)
/* 248 */       return;  for (Packet<?> packet : packets)
/* 249 */       pipeline.write(packet); 
/* 250 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 254 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 255 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 256 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 257 */       String methodName = "getConnection";
/*     */       try {
/* 259 */         Class<?> clazz = Class.forName(className);
/* 260 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 261 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 262 */         Object[] parameters = { playerConnection.f };
/* 263 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 264 */         return result.n.pipeline();
/* 265 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/*     */         
/* 267 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */     try {
/* 271 */       Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
/* 272 */       privateNetworkManager.setAccessible(true);
/* 273 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 274 */       return networkManager.n.pipeline();
/* 275 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 276 */       Bukkit.getLogger().severe("Error: Channel Pipeline not found");
/* 277 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R4\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */