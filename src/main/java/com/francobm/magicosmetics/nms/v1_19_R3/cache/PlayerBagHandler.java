/*     */ package com.francobm.magicosmetics.nms.v1_19_R3.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PlayerBagHandler extends PlayerBag {
/*     */   private final EntityArmorStand armorStand;
/*     */   
/*     */   public PlayerBagHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/*  41 */     this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
/*  42 */     this.uuid = p.getUniqueId();
/*  43 */     this.distance = distance;
/*  44 */     this.height = height;
/*  45 */     this.ids = new ArrayList();
/*  46 */     this.backPackItem = backPackItem;
/*  47 */     this.backPackItemForMe = backPackItemForMe;
/*  48 */     this.rangeManager = rangeManager;
/*  49 */     Player player = getPlayer();
/*  50 */     this.entityPlayer = ((CraftPlayer)player).getHandle();
/*  51 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*     */     
/*  53 */     this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
/*  54 */     this.backpackId = this.armorStand.af();
/*  55 */     this.armorStand.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
/*  56 */     this.armorStand.j(true);
/*  57 */     this.armorStand.m(true);
/*  58 */     this.armorStand.u(true);
/*     */   }
/*     */   private final double distance; private final EntityPlayer entityPlayer;
/*     */   
/*     */   public void spawn(Player player) {
/*  63 */     if (this.hideViewers.contains(player.getUniqueId()))
/*  64 */       return;  Player owner = getPlayer();
/*  65 */     if (owner == null)
/*  66 */       return;  if (player.getUniqueId().equals(owner.getUniqueId())) {
/*  67 */       spawnSelf(owner);
/*     */       return;
/*     */     } 
/*  70 */     Location location = owner.getLocation();
/*  71 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  73 */     sendPackets(player, getBackPackSpawn(this.backPackItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnSelf(Player player) {
/*  78 */     Player owner = getPlayer();
/*  79 */     if (owner == null)
/*     */       return; 
/*  81 */     Location location = owner.getLocation();
/*  82 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  84 */     sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
/*  85 */     if (this.height > 0.0F) {
/*  86 */       int i; for (i = 0; i < this.height; i++) {
/*  87 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/*  88 */         entityAreaEffectCloud.a(0.0F);
/*  89 */         entityAreaEffectCloud.j(true);
/*  90 */         entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  91 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/*  92 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.af()));
/*     */       } 
/*  94 */       for (i = 0; i < this.height; i++) {
/*  95 */         if (i == 0) {
/*  96 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/*  99 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/* 101 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.af());
/*     */     } else {
/* 103 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.af());
/*     */     } 
/* 105 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/* 110 */     for (Player player : getPlayersInRange()) {
/* 111 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 112 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 118 */     for (Player player : getPlayersInRange()) {
/* 119 */       remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 125 */     if (player.getUniqueId().equals(this.uuid)) {
/* 126 */       sendPackets(player, getBackPackDismount(true));
/* 127 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 130 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 135 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.af());
/* 136 */     for (Player player : getPlayersInRange()) {
/* 137 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 138 */         continue;  sendPackets(player, backPack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 144 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 149 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 154 */     Player owner = getPlayer();
/* 155 */     if (owner == null)
/* 156 */       return;  if (all) {
/* 157 */       for (Player player : getPlayersInRange()) {
/* 158 */         sendPackets(player, getBackPackRotationPackets(yaw));
/*     */       }
/*     */       return;
/*     */     } 
/* 162 */     sendPackets(owner, getBackPackRotationPackets(yaw));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 166 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 167 */     T result = null;
/*     */     try {
/* 169 */       result = callback.apply(data);
/* 170 */     } catch (Exception e) {
/* 171 */       e.printStackTrace();
/*     */     } finally {
/* 173 */       data.release();
/*     */     } 
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 184 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 189 */     return (Entity)this.armorStand.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
/* 193 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.armorStand);
/* 194 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.af(), this.armorStand.aj().c());
/* 195 */     PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
/* 196 */     PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.af(), Collections.singletonList(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(backpackItem))));
/* 197 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 201 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 202 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.af(), entityAreaEffectCloud.aj().c());
/* 203 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 207 */     List<Packet<?>> packets = new ArrayList<>();
/* 208 */     if (!removeClouds) {
/* 209 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.af() });
/* 210 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 212 */     for (Integer id : this.ids) {
/* 213 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 215 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.af() }));
/* 216 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 220 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 225 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 229 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 230 */     list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 231 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.af(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 235 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.af(), pairs));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 239 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
/* 240 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 241 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 245 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).b);
/* 246 */     if (pipeline == null)
/* 247 */       return;  for (Packet<?> packet : packets)
/* 248 */       pipeline.write(packet); 
/* 249 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 253 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 254 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 255 */       String className = "com.denizenscript.denizen.nms.v1_19.impl.network.handlers.DenizenNetworkManagerImpl";
/* 256 */       String methodName = "getConnection";
/*     */       try {
/* 258 */         Class<?> clazz = Class.forName(className);
/* 259 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 260 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 261 */         Object[] parameters = { playerConnection.b };
/* 262 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 263 */         return result.m.pipeline();
/* 264 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/*     */         
/* 266 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */     try {
/* 270 */       Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
/* 271 */       privateNetworkManager.setAccessible(true);
/* 272 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 273 */       return networkManager.m.pipeline();
/* 274 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 275 */       Bukkit.getLogger().severe("Error: Channel Pipeline not found");
/* 276 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */