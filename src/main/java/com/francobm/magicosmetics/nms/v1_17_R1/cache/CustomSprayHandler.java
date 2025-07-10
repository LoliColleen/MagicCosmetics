/*     */ package com.francobm.magicosmetics.nms.v1_17_R1.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.core.EnumDirection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
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
/*     */ import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
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
/*  36 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  37 */     this.uuid = player.getUniqueId();
/*  38 */     customSprays.put(this.uuid, this);
/*  39 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  40 */     this.enumDirection = getEnumDirection(blockFace);
/*  41 */     this.itemFrame = new EntityItemFrame(EntityTypes.R, (World)world);
/*  42 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  43 */     this.location = location;
/*  44 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  45 */     this.mapView = mapView;
/*  46 */     this.rotation = rotation;
/*  47 */     this.itemFrame.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*  48 */     String a = "25424";
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  53 */     if (this.players.contains(player.getUniqueId())) {
/*  54 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  55 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  59 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  60 */       return;  this.itemFrame.setInvisible(true);
/*  61 */     this.itemFrame.setInvulnerable(true);
/*  62 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  64 */     this.itemFrame.setPositionRotation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  65 */     this.itemFrame.setDirection(this.enumDirection);
/*  66 */     this.itemFrame.setRotation(this.rotation);
/*  67 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  68 */     connection.sendPacket((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.b()));
/*  69 */     connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.getId(), this.itemFrame.getDataWatcher(), true));
/*  70 */     if (this.mapView != null) {
/*  71 */       player.sendMap(this.mapView);
/*     */     }
/*  73 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  78 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  79 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  80 */         continue;  spawn(player);
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
/*  94 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/*  99 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 100 */     connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.getId() }));
/* 101 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/* 105 */     switch (facing) {
/*     */       case NORTH:
/* 107 */         return EnumDirection.c;
/*     */       case SOUTH:
/* 109 */         return EnumDirection.d;
/*     */       case WEST:
/* 111 */         return EnumDirection.e;
/*     */       case EAST:
/* 113 */         return EnumDirection.f;
/*     */       case DOWN:
/* 115 */         return EnumDirection.a;
/*     */     } 
/* 117 */     return EnumDirection.b;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_17_R1\cache\CustomSprayHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */