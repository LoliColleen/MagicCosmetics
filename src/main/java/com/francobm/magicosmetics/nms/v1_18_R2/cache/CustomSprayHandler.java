/*     */ package com.francobm.magicosmetics.nms.v1_18_R2.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.map.MapView;
/*     */ 
/*     */ public class CustomSprayHandler
/*     */   extends CustomSpray
/*     */ {
/*     */   private final EntityItemFrame itemFrame;
/*     */   private final Location location;
/*     */   private final ItemStack itemStack;
/*     */   private final EnumDirection enumDirection;
/*     */   private final MapView mapView;
/*     */   private final int rotation;
/*     */   
/*     */   public CustomSprayHandler(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/*  41 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  42 */     this.uuid = player.getUniqueId();
/*  43 */     customSprays.put(this.uuid, this);
/*  44 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  45 */     this.enumDirection = getEnumDirection(blockFace);
/*  46 */     this.itemFrame = new EntityItemFrame(EntityTypes.R, (World)world);
/*  47 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  48 */     this.location = location;
/*  49 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  50 */     this.mapView = mapView;
/*  51 */     this.rotation = rotation;
/*  52 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(Player player) {
/*  57 */     if (this.players.contains(player.getUniqueId())) {
/*  58 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  59 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  63 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  64 */       return;  this.itemFrame.j(true);
/*  65 */     this.itemFrame.m(true);
/*  66 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  68 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  69 */     this.itemFrame.a(this.enumDirection);
/*  70 */     this.itemFrame.a(this.rotation);
/*  71 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  72 */     connection.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.b()));
/*  73 */     connection.a((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.ae(), this.itemFrame.ai(), true));
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
/* 103 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/* 104 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.ae() }));
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
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R2\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */