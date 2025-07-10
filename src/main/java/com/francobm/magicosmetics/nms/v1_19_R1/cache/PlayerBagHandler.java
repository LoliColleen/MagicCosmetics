/*     */ package com.francobm.magicosmetics.nms.v1_19_R1.cache;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAreaEffectCloud;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PlayerBagHandler extends PlayerBag {
/*     */   private final EntityArmorStand armorStand;
/*     */   
/*     */   public PlayerBagHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/*  35 */     this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
/*  36 */     this.uuid = p.getUniqueId();
/*  37 */     this.distance = distance;
/*  38 */     this.height = height;
/*  39 */     this.ids = new ArrayList();
/*  40 */     this.backPackItem = backPackItem;
/*  41 */     this.backPackItemForMe = backPackItemForMe;
/*  42 */     this.rangeManager = rangeManager;
/*  43 */     Player player = getPlayer();
/*  44 */     this.entityPlayer = ((CraftPlayer)player).getHandle();
/*  45 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*     */     
/*  47 */     this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
/*  48 */     this.backpackId = this.armorStand.ae();
/*  49 */     this.armorStand.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
/*  50 */     this.armorStand.j(true);
/*  51 */     this.armorStand.m(true);
/*  52 */     this.armorStand.t(true);
/*     */   }
/*     */   private final double distance; private final EntityPlayer entityPlayer;
/*     */   
/*     */   public void spawn(Player player) {
/*  57 */     if (this.hideViewers.contains(player.getUniqueId()))
/*  58 */       return;  Player owner = getPlayer();
/*  59 */     if (owner == null)
/*  60 */       return;  if (player.getUniqueId().equals(owner.getUniqueId())) {
/*  61 */       spawnSelf(owner);
/*     */       return;
/*     */     } 
/*  64 */     Location location = owner.getLocation();
/*  65 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  67 */     sendPackets(player, getBackPackSpawn(this.backPackItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnSelf(Player player) {
/*  72 */     Player owner = getPlayer();
/*  73 */     if (owner == null)
/*  74 */       return;  Location location = owner.getLocation();
/*  75 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  77 */     sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
/*  78 */     if (this.height > 0.0F) {
/*  79 */       int i; for (i = 0; i < this.height; i++) {
/*  80 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/*  81 */         entityAreaEffectCloud.a(0.0F);
/*  82 */         entityAreaEffectCloud.j(true);
/*  83 */         entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  84 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/*  85 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.ae()));
/*     */       } 
/*  87 */       for (i = 0; i < this.height; i++) {
/*  88 */         if (i == 0) {
/*  89 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/*  92 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/*  94 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.ae());
/*     */     } else {
/*  96 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.ae());
/*     */     } 
/*  98 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/* 103 */     for (Player player : getPlayersInRange()) {
/* 104 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 105 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 111 */     for (Player player : getPlayersInRange()) {
/* 112 */       remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 118 */     if (player.getUniqueId().equals(this.uuid)) {
/* 119 */       sendPackets(player, getBackPackDismount(true));
/* 120 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 123 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 128 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.ae());
/* 129 */     for (Player player : getPlayersInRange()) {
/* 130 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 131 */         continue;  sendPackets(player, backPack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 137 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 142 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 147 */     Player owner = getPlayer();
/* 148 */     if (owner == null)
/* 149 */       return;  if (all) {
/* 150 */       for (Player player : getPlayersInRange()) {
/* 151 */         sendPackets(player, getBackPackRotationPackets(yaw));
/*     */       }
/*     */       return;
/*     */     } 
/* 155 */     sendPackets(owner, getBackPackRotationPackets(yaw));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 159 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 160 */     T result = null;
/*     */     try {
/* 162 */       result = callback.apply(data);
/* 163 */     } catch (Exception e) {
/* 164 */       e.printStackTrace();
/*     */     } finally {
/* 166 */       data.release();
/*     */     } 
/* 168 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 177 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 182 */     return (Entity)this.armorStand.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
/* 186 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((EntityLiving)this.armorStand);
/* 187 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true);
/* 188 */     PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
/* 189 */     PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.ae(), Collections.singletonList(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(backpackItem))));
/* 190 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 194 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 195 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.ae(), entityAreaEffectCloud.ai(), true);
/* 196 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 200 */     List<Packet<?>> packets = new ArrayList<>();
/* 201 */     if (!removeClouds) {
/* 202 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() });
/* 203 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 205 */     for (Integer id : this.ids) {
/* 206 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 208 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() }));
/* 209 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 213 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 218 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 222 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 223 */     list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 224 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ae(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 228 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ae(), pairs));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 232 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
/* 233 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 234 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 238 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).b);
/* 239 */     if (pipeline == null)
/* 240 */       return;  for (Packet<?> packet : packets)
/* 241 */       pipeline.write(packet); 
/* 242 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 246 */     return playerConnection.b.m.pipeline();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R1\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */