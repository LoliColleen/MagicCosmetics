/*     */ package com.francobm.magicosmetics.nms.v1_19_R2.cache;
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
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
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
/*  43 */     this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
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
/*  71 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/*  72 */     entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand));
/*     */     
/*  74 */     this.armorStand.al().refresh(entityPlayer);
/*  75 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  76 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  81 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  82 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  88 */     for (UUID uuid : this.players) {
/*  89 */       Player player = Bukkit.getPlayer(uuid);
/*  90 */       if (player == null) {
/*  91 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  94 */       remove(player);
/*     */     } 
/*  96 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/* 101 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/* 102 */       Player player = Bukkit.getPlayer(uuid);
/* 103 */       if (player == null) {
/* 104 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 107 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 108 */       Entity e = ((CraftEntity)this.entity).getHandle();
/*     */       
/* 110 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.ah());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.ah() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 115 */       entityPlayer.b.a((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 121 */     for (UUID uuid : this.players) {
/* 122 */       Player player = Bukkit.getPlayer(uuid);
/* 123 */       if (player == null) {
/* 124 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 127 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 128 */       Entity e = ((CraftEntity)entity).getHandle();
/* 129 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 131 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.ah());
/*     */             packetDataSerializer.a(new int[] { pass.ah() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 136 */       entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 142 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 143 */     Entity e = ((CraftEntity)entity).getHandle();
/* 144 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 146 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.ah());
/*     */           packetDataSerializer.a(new int[] { pass.ah() });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 151 */     entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 156 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 157 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ah() }));
/* 158 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 163 */     for (UUID uuid : this.players) {
/* 164 */       Player player = Bukkit.getPlayer(uuid);
/* 165 */       if (player == null) {
/* 166 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 169 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 170 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 171 */       list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 172 */       connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ah(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 178 */     float yaw = getEntity().getLocation().getYaw();
/* 179 */     for (UUID uuid : this.players) {
/* 180 */       Player player = Bukkit.getPlayer(uuid);
/* 181 */       if (player == null) {
/* 182 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 185 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 186 */       connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 187 */       connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ah(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 192 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 193 */     T result = null;
/*     */     try {
/* 195 */       result = callback.apply(data);
/* 196 */     } catch (Exception e) {
/* 197 */       e.printStackTrace();
/*     */     } finally {
/* 199 */       data.release();
/*     */     } 
/* 201 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */