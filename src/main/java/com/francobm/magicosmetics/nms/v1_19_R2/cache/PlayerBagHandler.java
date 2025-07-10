/*     */ package com.francobm.magicosmetics.nms.v1_19_R2.cache;
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
/*     */ import org.bukkit.entity.Entity;
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
/*  48 */     this.backpackId = this.armorStand.ah();
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
/*     */       return; 
/*  75 */     Location location = owner.getLocation();
/*  76 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  78 */     sendPackets(player, getBackPackSpawn((this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe));
/*  79 */     if (this.height > 0.0F) {
/*  80 */       int i; for (i = 0; i < this.height; i++) {
/*  81 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/*  82 */         entityAreaEffectCloud.a(0.0F);
/*  83 */         entityAreaEffectCloud.j(true);
/*  84 */         entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  85 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/*  86 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.ah()));
/*     */       } 
/*  88 */       for (i = 0; i < this.height; i++) {
/*  89 */         if (i == 0) {
/*  90 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/*  93 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/*  95 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.armorStand.ah());
/*     */     } else {
/*  97 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.armorStand.ah());
/*     */     } 
/*  99 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/* 104 */     for (Player player : getPlayersInRange()) {
/* 105 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 106 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 112 */     for (Player player : getPlayersInRange()) {
/* 113 */       remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 119 */     if (player.getUniqueId().equals(this.uuid)) {
/* 120 */       sendPackets(player, getBackPackDismount(true));
/* 121 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 124 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 129 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.armorStand.ah());
/* 130 */     for (Player player : getPlayersInRange()) {
/* 131 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 132 */         continue;  sendPackets(player, backPack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 138 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 143 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 148 */     Player owner = getPlayer();
/* 149 */     if (owner == null)
/* 150 */       return;  if (all) {
/* 151 */       for (Player player : getPlayersInRange()) {
/* 152 */         sendPackets(player, getBackPackRotationPackets(yaw));
/*     */       }
/*     */       return;
/*     */     } 
/* 156 */     sendPackets(owner, getBackPackRotationPackets(yaw));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 160 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 161 */     T result = null;
/*     */     try {
/* 163 */       result = callback.apply(data);
/* 164 */     } catch (Exception e) {
/* 165 */       e.printStackTrace();
/*     */     } finally {
/* 167 */       data.release();
/*     */     } 
/* 169 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 178 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 183 */     return (Entity)this.armorStand.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn(ItemStack backpackItem) {
/* 187 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.armorStand);
/* 188 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.armorStand.ah(), this.armorStand.al().c());
/* 189 */     PacketPlayOutMount mountEntity = new PacketPlayOutMount((Entity)this.entityPlayer);
/* 190 */     PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(this.armorStand.ah(), Collections.singletonList(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(backpackItem))));
/* 191 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata, (Packet)equip, (Packet)mountEntity });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 195 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 196 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.ah(), entityAreaEffectCloud.al().c());
/* 197 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 201 */     List<Packet<?>> packets = new ArrayList<>();
/* 202 */     if (!removeClouds) {
/* 203 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ah() });
/* 204 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 206 */     for (Integer id : this.ids) {
/* 207 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 209 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ah() }));
/* 210 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 214 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 219 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 223 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 224 */     list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 225 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ah(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 229 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.armorStand.ah(), pairs));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 233 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F));
/* 234 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ah(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 235 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 239 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).b);
/* 240 */     if (pipeline == null)
/* 241 */       return;  for (Packet<?> packet : packets)
/* 242 */       pipeline.write(packet); 
/* 243 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 247 */     return playerConnection.b.m.pipeline();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\cache\PlayerBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */