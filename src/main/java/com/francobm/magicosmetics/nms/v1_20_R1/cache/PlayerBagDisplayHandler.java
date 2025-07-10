/*     */ package com.francobm.magicosmetics.nms.v1_20_R1.cache;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.math.Transformation;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAreaEffectCloud;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class PlayerBagDisplayHandler extends PlayerBag {
/*     */   public PlayerBagDisplayHandler(Player p, IRangeManager rangeManager, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/*  45 */     this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
/*  46 */     this.uuid = p.getUniqueId();
/*  47 */     this.distance = distance;
/*     */     
/*  49 */     this.height = height;
/*  50 */     this.ids = new ArrayList();
/*  51 */     this.backPackItem = backPackItem;
/*  52 */     this.backPackItemForMe = backPackItemForMe;
/*  53 */     this.rangeManager = rangeManager;
/*  54 */     Player player = getPlayer();
/*  55 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  56 */     this.itemDisplay = new Display.ItemDisplay(EntityTypes.ae, (World)world);
/*  57 */     this.itemDisplay.a(CraftItemStack.asNMSCopy(backPackItem));
/*  58 */     this.itemDisplay.a(ItemDisplayContext.f);
/*  59 */     this.itemDisplay.a(new Transformation(new Vector3f(0.0F, height, 0.0F), new Quaternionf(), new Vector3f(0.6F, 0.6F, 0.6F), new Quaternionf()));
/*     */     
/*  61 */     this.itemDisplay.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Display.ItemDisplay itemDisplay;
/*     */   
/*     */   private final double distance;
/*     */ 
/*     */   
/*     */   public void spawn(Player player) {
/*  71 */     Player owner = getPlayer();
/*  72 */     if (owner == null)
/*  73 */       return;  Location location = owner.getLocation();
/*  74 */     this.itemDisplay.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */ 
/*     */     
/*  77 */     sendPackets(player, getBackPackSpawn());
/*  78 */     addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.itemDisplay.af());
/*  79 */     setItemOnHelmet(player, this.backPackItem);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnSelf(Player player) {
/*  84 */     Player owner = getPlayer();
/*  85 */     if (owner == null)
/*  86 */       return;  Location location = owner.getLocation();
/*     */ 
/*     */ 
/*     */     
/*  90 */     this.itemDisplay.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
/*     */     
/*  92 */     sendPackets(player, getBackPackSpawn());
/*  93 */     if (this.height > 0.0F) {
/*  94 */       int i; for (i = 0; i < this.height; i++) {
/*  95 */         EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/*  96 */         entityAreaEffectCloud.a(0.0F);
/*  97 */         entityAreaEffectCloud.j(true);
/*  98 */         entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  99 */         sendPackets(player, getCloudsSpawn(entityAreaEffectCloud));
/* 100 */         this.ids.add(Integer.valueOf(entityAreaEffectCloud.af()));
/*     */       } 
/* 102 */       for (i = 0; i < this.height; i++) {
/* 103 */         if (i == 0) {
/* 104 */           addPassenger(player, (this.lendEntityId == -1) ? player.getEntityId() : this.lendEntityId, ((Integer)this.ids.get(i)).intValue());
/*     */         } else {
/*     */           
/* 107 */           addPassenger(player, ((Integer)this.ids.get(i - 1)).intValue(), ((Integer)this.ids.get(i)).intValue());
/*     */         } 
/* 109 */       }  addPassenger(player, ((Integer)this.ids.get(this.ids.size() - 1)).intValue(), this.itemDisplay.af());
/*     */     } else {
/* 111 */       addPassenger(player, (this.lendEntityId == -1) ? owner.getEntityId() : this.lendEntityId, this.itemDisplay.af());
/*     */     } 
/* 113 */     setItemOnHelmet(player, (this.backPackItemForMe == null) ? this.backPackItem : this.backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/* 118 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 119 */       if (exception && player.getUniqueId().equals(this.uuid))
/* 120 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {}
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 130 */     if (player.getUniqueId().equals(this.uuid)) {
/* 131 */       sendPackets(player, getBackPackDismount(true));
/* 132 */       this.ids.clear();
/*     */       return;
/*     */     } 
/* 135 */     sendPackets(player, getBackPackDismount(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPassenger(boolean exception) {
/* 140 */     List<Packet<?>> backPack = getBackPackMountPacket((this.lendEntityId == -1) ? getPlayer().getEntityId() : this.lendEntityId, this.itemDisplay.af());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPassenger(Player player, int entity, int passenger) {
/* 146 */     sendPackets(player, getBackPackMountPacket(entity, passenger));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemOnHelmet(Player player, ItemStack itemStack) {
/* 151 */     sendPackets(player, getBackPackHelmetPacket(itemStack));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackSpawn() {
/* 155 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemDisplay);
/* 156 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemDisplay.af(), this.itemDisplay.aj().c());
/* 157 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getCloudsSpawn(EntityAreaEffectCloud entityAreaEffectCloud) {
/* 161 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud);
/* 162 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityAreaEffectCloud.af(), entityAreaEffectCloud.aj().c());
/* 163 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackDismount(boolean removeClouds) {
/* 167 */     List<Packet<?>> packets = new ArrayList<>();
/* 168 */     if (!removeClouds) {
/* 169 */       PacketPlayOutEntityDestroy backPackDestroy = new PacketPlayOutEntityDestroy(new int[] { this.itemDisplay.af() });
/* 170 */       return (List)Collections.singletonList(backPackDestroy);
/*     */     } 
/* 172 */     for (Integer id : this.ids) {
/* 173 */       packets.add(new PacketPlayOutEntityDestroy(new int[] { id.intValue() }));
/*     */     } 
/* 175 */     packets.add(new PacketPlayOutEntityDestroy(new int[] { this.itemDisplay.af() }));
/* 176 */     return packets;
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackMountPacket(int entity, int passenger) {
/* 180 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 185 */     return (List)Collections.singletonList(packetPlayOutMount);
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ItemStack itemStack) {
/* 189 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 190 */     list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/* 191 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.itemDisplay.af(), list));
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackHelmetPacket(ArrayList<Pair<EnumItemSlot, ItemStack>> pairs) {
/* 195 */     return (List)Collections.singletonList(new PacketPlayOutEntityEquipment(this.itemDisplay.af(), pairs));
/*     */   }
/*     */ 
/*     */   
/*     */   public void lookEntity(float yaw, float pitch, boolean all) {
/* 200 */     Player owner = getPlayer();
/* 201 */     if (owner == null)
/*     */       return; 
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 206 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 207 */     T result = null;
/*     */     try {
/* 209 */       result = callback.apply(data);
/* 210 */     } catch (Exception e) {
/* 211 */       e.printStackTrace();
/*     */     } finally {
/* 213 */       data.release();
/*     */     } 
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 224 */     return this.distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 229 */     return (Entity)this.itemDisplay.getBukkitEntity();
/*     */   }
/*     */   
/*     */   private List<Packet<?>> getBackPackRotationPackets(float yaw) {
/* 233 */     PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation((Entity)this.itemDisplay, (byte)(int)(yaw * 256.0F / 360.0F));
/* 234 */     PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.itemDisplay.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true);
/* 235 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)packetPlayOutEntityHeadRotation, (Packet)packetPlayOutEntityLook });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 239 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).c);
/* 240 */     if (pipeline == null)
/* 241 */       return;  for (Packet<?> packet : packets)
/* 242 */       pipeline.write(packet); 
/* 243 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 247 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 248 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 249 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 250 */       String methodName = "getConnection";
/*     */       try {
/* 252 */         Class<?> clazz = Class.forName(className);
/* 253 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 254 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 255 */         Object[] parameters = { playerConnection.b };
/* 256 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 257 */         return result.m.pipeline();
/* 258 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/*     */         
/* 260 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */     try {
/* 264 */       Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
/* 265 */       privateNetworkManager.setAccessible(true);
/* 266 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 267 */       return networkManager.m.pipeline();
/* 268 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 269 */       Bukkit.getLogger().severe("Error: Channel Pipeline not found");
/* 270 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R1\cache\PlayerBagDisplayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */