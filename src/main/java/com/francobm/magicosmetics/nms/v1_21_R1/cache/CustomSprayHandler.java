/*     */ package com.francobm.magicosmetics.nms.v1_21_R1.cache;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.core.EnumDirection;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.server.network.ServerCommonPacketListenerImpl;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.decoration.EntityItemFrame;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.util.CraftLocation;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.map.MapView;
/*     */ 
/*     */ public class CustomSprayHandler extends CustomSpray {
/*     */   private final EntityItemFrame itemFrame;
/*     */   private final Location location;
/*     */   private final ItemStack itemStack;
/*     */   
/*     */   public CustomSprayHandler(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/*  45 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  46 */     this.uuid = player.getUniqueId();
/*  47 */     customSprays.put(this.uuid, this);
/*  48 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  49 */     this.enumDirection = getEnumDirection(blockFace);
/*  50 */     this.itemFrame = new EntityItemFrame(EntityTypes.ai, (World)world);
/*  51 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  52 */     this.location = location;
/*  53 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  54 */     this.mapView = mapView;
/*  55 */     this.rotation = rotation;
/*  56 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  61 */     if (this.players.contains(player.getUniqueId())) {
/*  62 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  63 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  67 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  68 */       return;  this.itemFrame.k(true);
/*  69 */     this.itemFrame.n(true);
/*  70 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  72 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  73 */     this.itemFrame.a(this.enumDirection);
/*  74 */     this.itemFrame.b(this.rotation);
/*  75 */     sendPackets(player, spawnItemFrame());
/*  76 */     if (this.mapView != null) {
/*  77 */       player.sendMap(this.mapView);
/*     */     }
/*  79 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  84 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  85 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  86 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  92 */     for (UUID uuid : this.players) {
/*  93 */       Player player = Bukkit.getPlayer(uuid);
/*  94 */       if (player == null) {
/*  95 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  98 */       remove(player);
/*     */     } 
/* 100 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 105 */     sendPackets(player, Collections.singletonList(destroyItemFrame()));
/* 106 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/* 110 */     switch (facing) {
/*     */       case NORTH:
/* 112 */         return EnumDirection.c;
/*     */       case SOUTH:
/* 114 */         return EnumDirection.d;
/*     */       case WEST:
/* 116 */         return EnumDirection.e;
/*     */       case EAST:
/* 118 */         return EnumDirection.f;
/*     */       case DOWN:
/* 120 */         return EnumDirection.a;
/*     */     } 
/* 122 */     return EnumDirection.b;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Packet<?>> spawnItemFrame() {
/* 127 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.d(), CraftLocation.toBlockPosition(this.location));
/* 128 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemFrame.an(), this.itemFrame.ar().c());
/* 129 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private Packet<?> destroyItemFrame() {
/* 133 */     return (Packet<?>)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.an() });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 137 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).c);
/* 138 */     if (pipeline == null)
/* 139 */       return;  for (Packet<?> packet : packets)
/* 140 */       pipeline.write(packet); 
/* 141 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 145 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 146 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 147 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 148 */       String methodName = "getConnection";
/*     */       try {
/* 150 */         Class<?> clazz = Class.forName(className);
/* 151 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 152 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 153 */         Object[] parameters = { playerConnection.f };
/* 154 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 155 */         return result.n.pipeline();
/* 156 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/* 157 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 161 */       Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
/* 162 */       privateNetworkManager.setAccessible(true);
/* 163 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 164 */       return networkManager.n.pipeline();
/* 165 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 166 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_21_R1\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */