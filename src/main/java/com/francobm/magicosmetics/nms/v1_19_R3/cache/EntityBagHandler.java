/*     */ package com.francobm.magicosmetics.nms.v1_19_R3.cache;
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
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class EntityBagHandler extends EntityBag {
/*     */   public EntityBagHandler(Entity entity, double distance) {
/*  33 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  34 */     this.uuid = entity.getUniqueId();
/*  35 */     this.distance = distance;
/*  36 */     this.entity = entity;
/*  37 */     entityBags.put(this.uuid, this);
/*  38 */     WorldServer world = ((CraftWorld)entity.getWorld()).getHandle();
/*     */     
/*  40 */     this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
/*  41 */     this.armorStand.b(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), 0.0F);
/*  42 */     this.armorStand.j(true);
/*  43 */     this.armorStand.m(true);
/*  44 */     this.armorStand.b("507757");
/*     */   }
/*     */   
/*     */   private final EntityArmorStand armorStand;
/*     */   private final double distance;
/*     */   
/*     */   public void spawnBag(Player player) {
/*  51 */     if (this.players.contains(player.getUniqueId())) {
/*  52 */       if (!getEntity().getWorld().equals(player.getWorld())) {
/*  53 */         remove(player);
/*     */         return;
/*     */       } 
/*  56 */       if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) {
/*  57 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  61 */     if (!getEntity().getWorld().equals(player.getWorld()))
/*  62 */       return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
/*  63 */       return;  this.armorStand.m(true);
/*  64 */     this.armorStand.j(true);
/*  65 */     this.armorStand.u(true);
/*  66 */     Location location = getEntity().getLocation();
/*  67 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  69 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/*  70 */     entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand));
/*     */     
/*  72 */     this.armorStand.aj().refresh(entityPlayer);
/*  73 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  74 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  79 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  80 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  86 */     for (UUID uuid : this.players) {
/*  87 */       Player player = Bukkit.getPlayer(uuid);
/*  88 */       if (player == null) {
/*  89 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  92 */       remove(player);
/*     */     } 
/*  94 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/*  99 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/* 100 */       Player player = Bukkit.getPlayer(uuid);
/* 101 */       if (player == null) {
/* 102 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 105 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 106 */       Entity e = ((CraftEntity)this.entity).getHandle();
/* 107 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.af());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.af() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 112 */       entityPlayer.b.a((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 118 */     for (UUID uuid : this.players) {
/* 119 */       Player player = Bukkit.getPlayer(uuid);
/* 120 */       if (player == null) {
/* 121 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 124 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 125 */       Entity e = ((CraftEntity)entity).getHandle();
/* 126 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 128 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.af());
/*     */             packetDataSerializer.a(new int[] { pass.af() });
/*     */             return new PacketPlayOutMount(packetDataSerializer);
/*     */           });
/* 133 */       entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 139 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 140 */     Entity e = ((CraftEntity)entity).getHandle();
/* 141 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 143 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.af());
/*     */           packetDataSerializer.a(new int[] { pass.af() });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 148 */     entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 153 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 154 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.af() }));
/* 155 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 160 */     for (UUID uuid : this.players) {
/* 161 */       Player player = Bukkit.getPlayer(uuid);
/* 162 */       if (player == null) {
/* 163 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 166 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 167 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 168 */       list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 169 */       connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.af(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 175 */     float yaw = getEntity().getLocation().getYaw();
/* 176 */     for (UUID uuid : this.players) {
/* 177 */       Player player = Bukkit.getPlayer(uuid);
/* 178 */       if (player == null) {
/* 179 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 182 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 183 */       connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 184 */       connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 189 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 190 */     T result = null;
/*     */     try {
/* 192 */       result = callback.apply(data);
/* 193 */     } catch (Exception e) {
/* 194 */       e.printStackTrace();
/*     */     } finally {
/* 196 */       data.release();
/*     */     } 
/* 198 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */