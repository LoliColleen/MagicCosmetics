/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityAreaEffectCloud;
/*     */ import net.minecraft.server.v1_16_R3.EnumItemSlot;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.Packet;
/*     */ import net.minecraft.server.v1_16_R3.PacketDataSerializer;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PlayerBagHandler extends PlayerBag {
/*     */   private final EntityArmorStand armorStand;
/*     */   
/*     */   public PlayerBagHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/*  26 */     this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
/*  27 */     this.uuid = p.getUniqueId();
/*  28 */     this.distance = distance;
/*  29 */     this.height = height;
/*  30 */     this.ids = new ArrayList();
/*  31 */     this.backPackItem = backPackItem;
/*  32 */     this.backPackItemForMe = backPackItemForMe;
/*  33 */     this.rangeManager = rangeManager;
/*  34 */     Player player = getPlayer();
/*  35 */     this.entityPlayer = ((CraftPlayer)player).getHandle();
/*  36 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*     */     
/*  38 */     this.armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)world);
/*  39 */     this.backpackId = this.armorStand.getId();
/*  40 */     this.armorStand.setPositionRotation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
/*  41 */     this.armorStand.setInvisible(true);
/*  42 */     this.armorStand.setInvulnerable(true);
/*  43 */     this.armorStand.setMarker(true);
/*     */   }
/*     */   private final double distance; private final EntityPlayer entityPlayer;
/*     */   
/*     */   public void spawn(Player player) {
/*  48 */     if (this.hideViewers.contains(player.getUniqueId()))
/*  49 */       return;  Player owner = getPlayer();
/*  50 */     if (owner == null)
/*  51 */       return;  if (player.getUniqueId().equals(owner.getUniqueId())) {
/*  52 */       spawnSelf(owner);
/*     */       return;
/*     */     } 
/*  55 */     Location location = owner.getLocation();
/*  56 */     this.armorStand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  58 */     sendPackets(player, getBackPackSpawn(this.backPackItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnSelf(Player player) {
/*  63 */     Player owner = getPlayer();
/*  64 */     if (owner == null)
/*     */       return; 
/*  66 */     Location location = owner.getLocation();
/*  67 */     this.armorStand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  69 */     sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
/*  70 */     if (this.height > 0.0F) {
/*  71 */       int i; for (i = 0; i < this.height; i++) {
/*  72 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.AREA_EFFECT_CLOUD, (World)((CraftWorld)player.getWorld()).getHandle());
/*  73 */         entityAreaEffectCloud.setRadius(0.0F);
/*  74 */         entityAreaEffectCloud.setInvisible(true);
/*  75 */         entityAreaEffectCloud.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  76 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/*  77 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.getId()));
/*     */       } 
/*  79 */       for (i = 0; i < this.height; i++) {
/*  80 */         if (i == 0) {
/*  81 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/*  84 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/*  86 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.getId());
/*     */     } else {
/*  88 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.getId());
/*     */     } 
/*  90 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  95 */     for (Player player : getPlayersInRange()) {
/*  96 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  97 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 103 */     for (Player player : getPlayersInRange()) {
/* 104 */       remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 110 */     if (player.getUniqueId().equals(this.uuid)) {
/* 111 */       sendPackets(player, getBackPackDismount(true));
/* 112 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 115 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 120 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.getId());
/* 121 */     for (Player player : getPlayersInRange()) {
/* 122 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 123 */         continue;  sendPackets(player, backPack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 129 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 134 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 139 */     Player owner = getPlayer();
/* 140 */     if (owner == null)
/* 141 */       return;  if (all) {
/* 142 */       for (Player player : getPlayersInRange()) {
/* 143 */         sendPackets(player, getBackPackRotationPackets(yaw));
/*     */       }
/*     */       return;
/*     */     } 
/* 147 */     sendPackets(owner, getBackPackRotationPackets(yaw));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 151 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 152 */     T result = null;
/*     */     try {
/* 154 */       result = callback.apply(data);
/* 155 */     } catch (Exception e) {
/* 156 */       e.printStackTrace();
/*     */     } finally {
/* 158 */       data.release();
/*     */     } 
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 169 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 174 */     return (Entity)this.armorStand.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
/* 178 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.armorStand);
/* 179 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true);
/* 180 */     PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
/* 181 */     PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.getId(), Collections.singletonList(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(backpackItem))));
/* 182 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 186 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 187 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.getId(), entityAreaEffectCloud.getDataWatcher(), true);
/* 188 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 192 */     List<Packet<?>> packets = new ArrayList<>();
/* 193 */     if (!removeClouds) {
/* 194 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() });
/* 195 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 197 */     for (Integer id : this.ids) {
/* 198 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 200 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() }));
/* 201 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 205 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           PacketPlayOutMount packet = new PacketPlayOutMount();
/*     */           packet.a(packetDataSerializer);
/*     */           return packet;
/*     */         });
/* 212 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 216 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 217 */     list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
/* 218 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.getId(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 222 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.getId(), pairs));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 226 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
/* 227 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 228 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 232 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).playerConnection);
/* 233 */     if (pipeline == null)
/* 234 */       return;  for (Packet<?> packet : packets)
/* 235 */       pipeline.write(packet); 
/* 236 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 240 */     return playerConnection.networkManager.channel.pipeline();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */