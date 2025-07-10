/*     */ package com.francobm.magicosmetics.nms.v1_17_R1.cache;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
/*     */ import net.minecraft.network.syncher.DataWatcher;
/*     */ import net.minecraft.network.syncher.DataWatcherObject;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class EntityBagHandler extends EntityBag {
/*     */   public EntityBagHandler(Entity entity, double distance) {
/*  36 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  37 */     this.uuid = entity.getUniqueId();
/*  38 */     this.distance = distance;
/*  39 */     this.entity = entity;
/*  40 */     entityBags.put(this.uuid, this);
/*  41 */     WorldServer world = ((CraftWorld)entity.getWorld()).getHandle();
/*     */     
/*  43 */     this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world);
/*  44 */     this.armorStand.setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), 0.0F);
/*  45 */     this.armorStand.setInvisible(true);
/*  46 */     this.armorStand.setInvulnerable(true);
/*     */   }
/*     */   
/*     */   private final EntityArmorStand armorStand;
/*     */   private final double distance;
/*     */   
/*     */   public void spawnBag(Player player) {
/*  53 */     if (this.players.contains(player.getUniqueId())) {
/*  54 */       if (!getEntity().getWorld().equals(player.getWorld())) {
/*  55 */         remove(player);
/*     */         return;
/*     */       } 
/*  58 */       if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) {
/*  59 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  63 */     if (!getEntity().getWorld().equals(player.getWorld()))
/*  64 */       return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
/*  65 */       return;  this.armorStand.setInvulnerable(true);
/*  66 */     this.armorStand.setInvisible(true);
/*  67 */     this.armorStand.setMarker(true);
/*  68 */     Location location = getEntity().getLocation();
/*  69 */     this.armorStand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  71 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  72 */     connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand));
/*     */     
/*  74 */     DataWatcher watcher = this.armorStand.getDataWatcher();
/*  75 */     watcher.set(new DataWatcherObject(0, DataWatcherRegistry.a), Byte.valueOf((byte)32));
/*  76 */     PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.armorStand.getId(), watcher, true);
/*  77 */     connection.sendPacket((Packet)packet);
/*  78 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  79 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  84 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  85 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  91 */     for (UUID uuid : this.players) {
/*  92 */       Player player = Bukkit.getPlayer(uuid);
/*  93 */       if (player == null) {
/*  94 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  97 */       remove(player);
/*     */     } 
/*  99 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/* 104 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/* 105 */       Player player = Bukkit.getPlayer(uuid);
/* 106 */       if (player == null) {
/* 107 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 110 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 111 */       Entity e = ((CraftEntity)this.entity).getHandle();
/*     */       
/* 113 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.getId());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.getId() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 118 */       entityPlayer.b.sendPacket((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 124 */     for (UUID uuid : this.players) {
/* 125 */       Player player = Bukkit.getPlayer(uuid);
/* 126 */       if (player == null) {
/* 127 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 130 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 131 */       Entity e = ((CraftEntity)entity).getHandle();
/* 132 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 134 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.getId());
/*     */             packetDataSerializer.a(new int[] { pass.getId() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 139 */       entityPlayer.b.sendPacket((Packet)packetPlayOutMount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 145 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 146 */     Entity e = ((CraftEntity)entity).getHandle();
/* 147 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 149 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.getId());
/*     */           packetDataSerializer.a(new int[] { pass.getId() });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 154 */     entityPlayer.b.sendPacket((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 159 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 160 */     connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() }));
/* 161 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 166 */     for (UUID uuid : this.players) {
/* 167 */       Player player = Bukkit.getPlayer(uuid);
/* 168 */       if (player == null) {
/* 169 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 172 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 173 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 174 */       list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 175 */       connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 181 */     float yaw = getEntity().getLocation().getYaw();
/* 182 */     for (UUID uuid : this.players) {
/* 183 */       Player player = Bukkit.getPlayer(uuid);
/* 184 */       if (player == null) {
/* 185 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 188 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 189 */       connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 190 */       connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 195 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 196 */     T result = null;
/*     */     try {
/* 198 */       result = callback.apply(data);
/* 199 */     } catch (Exception e) {
/* 200 */       e.printStackTrace();
/*     */     } finally {
/* 202 */       data.release();
/*     */     } 
/* 204 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_17_R1\cache\EntityBagHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */