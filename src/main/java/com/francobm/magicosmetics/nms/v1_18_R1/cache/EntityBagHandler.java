/*     */ package com.francobm.magicosmetics.nms.v1_18_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
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
/*  44 */     this.armorStand.b(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), 0.0F);
/*  45 */     this.armorStand.j(true);
/*  46 */     this.armorStand.m(true);
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
/*  65 */       return;  this.armorStand.m(true);
/*  66 */     this.armorStand.j(true);
/*  67 */     this.armorStand.t(true);
/*  68 */     Location location = getEntity().getLocation();
/*  69 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  71 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  72 */     connection.a((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand));
/*     */     
/*  74 */     DataWatcher watcher = this.armorStand.ai();
/*  75 */     watcher.b(new DataWatcherObject(0, DataWatcherRegistry.a), Byte.valueOf((byte)32));
/*  76 */     connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), watcher, true));
/*  77 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  78 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  83 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  84 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  90 */     for (UUID uuid : this.players) {
/*  91 */       Player player = Bukkit.getPlayer(uuid);
/*  92 */       if (player == null) {
/*  93 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  96 */       remove(player);
/*     */     } 
/*  98 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/* 103 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/* 104 */       Player player = Bukkit.getPlayer(uuid);
/* 105 */       if (player == null) {
/* 106 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 109 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 110 */       Entity e = ((CraftEntity)this.entity).getHandle();
/*     */       
/* 112 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.ae());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.ae() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 117 */       entityPlayer.b.a((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 123 */     for (UUID uuid : this.players) {
/* 124 */       Player player = Bukkit.getPlayer(uuid);
/* 125 */       if (player == null) {
/* 126 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 129 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 130 */       Entity e = ((CraftEntity)entity).getHandle();
/* 131 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 133 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.ae());
/*     */             packetDataSerializer.a(new int[] { pass.ae() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 138 */       entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 144 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 145 */     Entity e = ((CraftEntity)entity).getHandle();
/* 146 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 148 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.ae());
/*     */           packetDataSerializer.a(new int[] { pass.ae() });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 153 */     entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 158 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 159 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() }));
/* 160 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 165 */     for (UUID uuid : this.players) {
/* 166 */       Player player = Bukkit.getPlayer(uuid);
/* 167 */       if (player == null) {
/* 168 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 171 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 172 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 173 */       list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 174 */       connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 180 */     float yaw = getEntity().getLocation().getYaw();
/* 181 */     for (UUID uuid : this.players) {
/* 182 */       Player player = Bukkit.getPlayer(uuid);
/* 183 */       if (player == null) {
/* 184 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 187 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 188 */       connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 189 */       connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 194 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 195 */     T result = null;
/*     */     try {
/* 197 */       result = callback.apply(data);
/* 198 */     } catch (Exception e) {
/* 199 */       e.printStackTrace();
/*     */     } finally {
/* 201 */       data.release();
/*     */     } 
/* 203 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */