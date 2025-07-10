/*     */ package com.francobm.magicosmetics.nms.v1_20_R3.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
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
/*  44 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  45 */     this.uuid = player.getUniqueId();
/*  46 */     customSprays.put(this.uuid, this);
/*  47 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  48 */     this.enumDirection = getEnumDirection(blockFace);
/*  49 */     this.itemFrame = new EntityItemFrame(EntityTypes.ag, (World)world);
/*  50 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  51 */     this.location = location;
/*  52 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  53 */     this.mapView = mapView;
/*  54 */     this.rotation = rotation;
/*  55 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  60 */     if (this.players.contains(player.getUniqueId())) {
/*  61 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  62 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  66 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  67 */       return;  this.itemFrame.j(true);
/*  68 */     this.itemFrame.m(true);
/*  69 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  71 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  72 */     this.itemFrame.a(this.enumDirection);
/*  73 */     this.itemFrame.b(this.rotation);
/*  74 */     sendPackets(player, spawnItemFrame());
/*  75 */     if (this.mapView != null) {
/*  76 */       player.sendMap(this.mapView);
/*     */     }
/*  78 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  83 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  84 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  85 */         continue;  spawn(player);
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
/*  99 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/* 104 */     sendPackets(player, Collections.singletonList(destroyItemFrame()));
/* 105 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/* 109 */     switch (facing) {
/*     */       case NORTH:
/* 111 */         return EnumDirection.c;
/*     */       case SOUTH:
/* 113 */         return EnumDirection.d;
/*     */       case WEST:
/* 115 */         return EnumDirection.e;
/*     */       case EAST:
/* 117 */         return EnumDirection.f;
/*     */       case DOWN:
/* 119 */         return EnumDirection.a;
/*     */     } 
/* 121 */     return EnumDirection.b;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Packet<?>> spawnItemFrame() {
/* 126 */     PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.d());
/* 127 */     PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(this.itemFrame.aj(), this.itemFrame.an().c());
/* 128 */     return Arrays.asList((Packet<?>[])new Packet[] { (Packet)spawnEntity, (Packet)entityMetadata });
/*     */   }
/*     */   
/*     */   private Packet<?> destroyItemFrame() {
/* 132 */     return (Packet<?>)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.aj() });
/*     */   }
/*     */   
/*     */   private void sendPackets(Player player, List<Packet<?>> packets) {
/* 136 */     ChannelPipeline pipeline = getPrivateChannelPipeline((((CraftPlayer)player).getHandle()).c);
/* 137 */     if (pipeline == null)
/* 138 */       return;  for (Packet<?> packet : packets)
/* 139 */       pipeline.write(packet); 
/* 140 */     pipeline.flush();
/*     */   }
/*     */   
/*     */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 144 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 145 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 146 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 147 */       String methodName = "getConnection";
/*     */       try {
/* 149 */         Class<?> clazz = Class.forName(className);
/* 150 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 151 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 152 */         Object[] parameters = { playerConnection.e };
/* 153 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 154 */         return result.n.pipeline();
/* 155 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/* 156 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 160 */       Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
/* 161 */       privateNetworkManager.setAccessible(true);
/* 162 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 163 */       return networkManager.n.pipeline();
/* 164 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 165 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R3\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */