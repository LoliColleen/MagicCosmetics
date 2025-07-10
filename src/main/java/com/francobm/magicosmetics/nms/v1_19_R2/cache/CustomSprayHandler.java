/*     */ package com.francobm.magicosmetics.nms.v1_19_R2.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.core.EnumDirection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.map.MapView;
/*     */ 
/*     */ public class CustomSprayHandler
/*     */   extends CustomSpray {
/*     */   private final EntityItemFrame itemFrame;
/*     */   private final Location location;
/*     */   private final ItemStack itemStack;
/*     */   
/*     */   public CustomSprayHandler(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/*  37 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  38 */     this.uuid = player.getUniqueId();
/*  39 */     customSprays.put(this.uuid, this);
/*  40 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  41 */     this.enumDirection = getEnumDirection(blockFace);
/*  42 */     this.itemFrame = new EntityItemFrame(EntityTypes.V, (World)world);
/*  43 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  44 */     this.location = location;
/*  45 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  46 */     this.mapView = mapView;
/*  47 */     this.rotation = rotation;
/*  48 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
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
/*  60 */       return;  this.itemFrame.j(true);
/*  61 */     this.itemFrame.m(true);
/*  62 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  64 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  65 */     this.itemFrame.a(this.enumDirection);
/*  66 */     this.itemFrame.a(this.rotation);
/*  67 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/*  68 */     entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.d()));
/*  69 */     this.itemFrame.al().refresh(entityPlayer);
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
/* 100 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.ah() }));
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


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */