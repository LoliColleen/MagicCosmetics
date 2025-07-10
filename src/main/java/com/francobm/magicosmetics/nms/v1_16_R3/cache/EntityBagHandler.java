/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.v1_16_R3.DataWatcher;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*     */ import net.minecraft.server.v1_16_R3.EnumItemSlot;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.Packet;
/*     */ import net.minecraft.server.v1_16_R3.PacketDataSerializer;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
/*     */ import net.minecraft.server.v1_16_R3.PlayerConnection;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class EntityBagHandler extends EntityBag {
/*     */   public EntityBagHandler(Entity entity, double distance) {
/*  26 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  27 */     this.uuid = entity.getUniqueId();
/*  28 */     this.distance = distance;
/*  29 */     this.entity = entity;
/*  30 */     entityBags.put(this.uuid, this);
/*  31 */     WorldServer world = ((CraftWorld)entity.getWorld()).getHandle();
/*     */     
/*  33 */     this.armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)world);
/*  34 */     this.armorStand.setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), 0.0F);
/*  35 */     this.armorStand.setInvisible(true);
/*  36 */     this.armorStand.setInvulnerable(true);
/*     */   }
/*     */   
/*     */   private final EntityArmorStand armorStand;
/*     */   private final double distance;
/*     */   
/*     */   public void spawnBag(Player player) {
/*  43 */     if (this.players.contains(player.getUniqueId())) {
/*  44 */       if (!getEntity().getWorld().equals(player.getWorld())) {
/*  45 */         remove(player);
/*     */         return;
/*     */       } 
/*  48 */       if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) {
/*  49 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  53 */     if (!getEntity().getWorld().equals(player.getWorld()))
/*  54 */       return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
/*  55 */       return;  this.armorStand.setInvulnerable(true);
/*  56 */     this.armorStand.setInvisible(true);
/*  57 */     this.armorStand.setMarker(true);
/*  58 */     Location location = getEntity().getLocation();
/*  59 */     this.armorStand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  61 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/*  62 */     connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand));
/*     */     
/*  64 */     DataWatcher watcher = this.armorStand.getDataWatcher();
/*  65 */     watcher.set(new DataWatcherObject(0, DataWatcherRegistry.a), Byte.valueOf((byte)32));
/*  66 */     PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.armorStand.getId(), watcher, true);
/*  67 */     connection.sendPacket((Packet)packet);
/*  68 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  69 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  74 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  75 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  81 */     for (UUID uuid : this.players) {
/*  82 */       Player player = Bukkit.getPlayer(uuid);
/*  83 */       if (player == null) {
/*  84 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  87 */       remove(player);
/*     */     } 
/*  89 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/*  94 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/*  95 */       Player player = Bukkit.getPlayer(uuid);
/*  96 */       if (player == null) {
/*  97 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 100 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 101 */       Entity e = ((CraftEntity)this.entity).getHandle();
/*     */       
/* 103 */       PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount();
/* 104 */       createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.getId());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.getId() });
/*     */             packetPlayOutMount.a(packetDataSerializer);
/*     */             return null;
/*     */           });
/* 110 */       entityPlayer.playerConnection.sendPacket((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 116 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/* 117 */       Player player = Bukkit.getPlayer(uuid);
/* 118 */       if (player == null) {
/* 119 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 122 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 123 */       Entity e = ((CraftEntity)entity).getHandle();
/* 124 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 126 */       PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount();
/* 127 */       createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.getId());
/*     */             packetDataSerializer.a(new int[] { pass.getId() });
/*     */             packetPlayOutMount.a(packetDataSerializer);
/*     */             return null;
/*     */           });
/* 133 */       entityPlayer.playerConnection.sendPacket((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 139 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 140 */     Entity e = ((CraftEntity)entity).getHandle();
/* 141 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 143 */     PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount();
/* 144 */     createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.getId());
/*     */           packetDataSerializer.a(new int[] { pass.getId() });
/*     */           packetPlayOutMount.a(packetDataSerializer);
/*     */           return null;
/*     */         });
/* 150 */     entityPlayer.playerConnection.sendPacket((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 155 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/* 156 */     connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() }));
/* 157 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 162 */     for (UUID uuid : this.players) {
/* 163 */       Player player = Bukkit.getPlayer(uuid);
/* 164 */       if (player == null) {
/* 165 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 168 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/* 169 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 170 */       list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
/* 171 */       connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 177 */     float yaw = getEntity().getLocation().getYaw();
/* 178 */     for (UUID uuid : this.players) {
/* 179 */       Player player = Bukkit.getPlayer(uuid);
/* 180 */       if (player == null) {
/* 181 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 184 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/* 185 */       connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 186 */       connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 191 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 192 */     T result = null;
/*     */     try {
/* 194 */       result = callback.apply(data);
/* 195 */     } catch (Exception e) {
/* 196 */       e.printStackTrace();
/*     */     } finally {
/* 198 */       data.release();
/*     */     } 
/* 200 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */