/*     */ package com.francobm.magicosmetics.nms.v1_20_R4.cache;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
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
/*  42 */     this.armorStand.k(true);
/*  43 */     this.armorStand.n(true);
/*  44 */     this.armorStand.u(true);
/*     */   }
/*     */   private final EntityArmorStand armorStand;
/*     */   private final double distance;
/*     */   
/*     */   public void spawnBag(Player player) {
/*  50 */     if (this.players.contains(player.getUniqueId())) {
/*  51 */       if (!getEntity().getWorld().equals(player.getWorld())) {
/*  52 */         remove(player);
/*     */         return;
/*     */       } 
/*  55 */       if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) {
/*  56 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  60 */     if (!getEntity().getWorld().equals(player.getWorld()))
/*  61 */       return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
/*  62 */       return;  this.armorStand.n(true);
/*  63 */     this.armorStand.k(true);
/*  64 */     this.armorStand.u(true);
/*  65 */     Location location = getEntity().getLocation();
/*  66 */     this.armorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  68 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/*  69 */     entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand));
/*     */     
/*  71 */     entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
/*  72 */     addPassenger(player, getEntity(), (Entity)this.armorStand.getBukkitEntity());
/*  73 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnBag() {
/*  78 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  79 */       spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  85 */     for (UUID uuid : this.players) {
/*  86 */       Player player = Bukkit.getPlayer(uuid);
/*  87 */       if (player == null) {
/*  88 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  91 */       remove(player);
/*     */     } 
/*  93 */     entityBags.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger() {
/*  98 */     for (Iterator<UUID> iterator = this.players.iterator(); iterator.hasNext(); ) { UUID uuid = iterator.next();
/*  99 */       Player player = Bukkit.getPlayer(uuid);
/* 100 */       if (player == null) {
/* 101 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 104 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 105 */       Entity e = ((CraftEntity)this.entity).getHandle();
/* 106 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.c(e.al());
/*     */             packetDataSerializer.a(new int[] { this.armorStand.al() });
/*     */             return (PacketPlayOutMount)PacketPlayOutMount.a.decode(packetDataSerializer);
/*     */           });
/* 111 */       entityPlayer.c.b((Packet)packetPlayOutMount); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Entity entity, Entity passenger) {
/* 117 */     for (UUID uuid : this.players) {
/* 118 */       Player player = Bukkit.getPlayer(uuid);
/* 119 */       if (player == null) {
/* 120 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 123 */       EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 124 */       Entity e = ((CraftEntity)entity).getHandle();
/* 125 */       Entity pass = ((CraftEntity)passenger).getHandle();
/*     */       
/* 127 */       PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */             packetDataSerializer.d(e.al());
/*     */             packetDataSerializer.a(new int[] { pass.al() });
/*     */             return (PacketPlayOutMount)PacketPlayOutMount.a.decode(packetDataSerializer);
/*     */           });
/* 132 */       entityPlayer.c.b((Packet)packetPlayOutMount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, Entity entity, Entity passenger) {
/* 138 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 139 */     Entity e = ((CraftEntity)entity).getHandle();
/* 140 */     Entity pass = ((CraftEntity)passenger).getHandle();
/*     */     
/* 142 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(e.al());
/*     */           packetDataSerializer.a(new int[] { pass.al() });
/*     */           return (PacketPlayOutMount)PacketPlayOutMount.a.decode(packetDataSerializer);
/*     */         });
/* 147 */     entityPlayer.c.b((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 152 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
/* 153 */     connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.al() }));
/* 154 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(ItemStack itemStack) {
/* 159 */     for (UUID uuid : this.players) {
/* 160 */       Player player = Bukkit.getPlayer(uuid);
/* 161 */       if (player == null) {
/* 162 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 165 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
/* 166 */       ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 167 */       list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 168 */       connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.al(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity() {
/* 174 */     float yaw = getEntity().getLocation().getYaw();
/* 175 */     for (UUID uuid : this.players) {
/* 176 */       Player player = Bukkit.getPlayer(uuid);
/* 177 */       if (player == null) {
/* 178 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 181 */       PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
/* 182 */       connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
/* 183 */       connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.al(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 188 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 189 */     T result = null;
/*     */     try {
/* 191 */       result = callback.apply(data);
/* 192 */     } catch (Exception e) {
/* 193 */       e.printStackTrace();
/*     */     } finally {
/* 195 */       data.release();
/*     */     } 
/* 197 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R4\cache\EntityBagHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */