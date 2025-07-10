/*     */ package com.francobm.magicosmetics.nms.v1_19_R3.cache;
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
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.decoration.EntityItemFrame;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
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
/*  43 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  44 */     this.uuid = player.getUniqueId();
/*  45 */     customSprays.put(this.uuid, this);
/*  46 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  47 */     this.enumDirection = getEnumDirection(blockFace);
/*  48 */     this.itemFrame = new EntityItemFrame(EntityTypes.af, (World)world);
/*  49 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  50 */     this.location = location;
/*  51 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  52 */     this.mapView = mapView;
/*  53 */     this.rotation = rotation;
/*  54 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  59 */     if (this.players.contains(player.getUniqueId())) {
/*  60 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  61 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  65 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  66 */       return;  this.itemFrame.j(true);
/*  67 */     this.itemFrame.m(true);
/*  68 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  70 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  71 */     this.itemFrame.a(this.enumDirection);
/*  72 */     this.itemFrame.b(this.rotation);
/*  73 */     sendPackets(player, spawnItemFrame());
/*  74 */     if (this.mapView != null) {
/*  75 */       player.sendMap(this.mapView);
/*     */     }
/*  77 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  82 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  83 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  84 */         continue;  spawn(player);
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
/*  98 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 103 */     sendPackets(player, Collections.singletonList(destroyItemFrame()));
/* 104 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/* 108 */     switch (facing) {
/*     */       case NORTH:
/* 110 */         return EnumDirection.c;
/*     */       case SOUTH:
/* 112 */         return EnumDirection.d;
/*     */       case WEST:
/* 114 */         return EnumDirection.e;
/*     */       case EAST:
/* 116 */         return EnumDirection.f;
/*     */       case DOWN:
/* 118 */         return EnumDirection.a;
/*     */     } 
/* 120 */     return EnumDirection.b;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Packet<?>> spawnItemFrame() {
/* 125 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.d());
/* 126 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemFrame.af(), this.itemFrame.aj().c());
/* 127 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private Packet<?> destroyItemFrame() {
/* 131 */     return (Packet<?>)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.af() });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 135 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).b);
/* 136 */     if (pipeline == null)
/* 137 */       return;  for (Packet<?> packet : packets)
/* 138 */       pipeline.write(packet); 
/* 139 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 143 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 144 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 145 */       String className = "com.denizenscript.denizen.nms.v1_19.impl.network.handlers.DenizenNetworkManagerImpl";
/* 146 */       String methodName = "getConnection";
/*     */       try {
/* 148 */         Class<?> clazz = Class.forName(className);
/* 149 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 150 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 151 */         Object[] parameters = { playerConnection.b };
/* 152 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 153 */         return result.m.pipeline();
/* 154 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/* 155 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 159 */       Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
/* 160 */       privateNetworkManager.setAccessible(true);
/* 161 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 162 */       return networkManager.m.pipeline();
/* 163 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 164 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */