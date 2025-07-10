/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.server.v1_16_R3.EntityItemFrame;
/*     */ import net.minecraft.server.v1_16_R3.EnumDirection;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.Packet;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.v1_16_R3.PlayerConnection;
/*     */ import net.minecraft.server.v1_16_R3.World;
/*     */ import net.minecraft.server.v1_16_R3.WorldServer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
/*  29 */     this.players = new CopyOnWriteArrayList(new ArrayList());
/*  30 */     this.uuid = player.getUniqueId();
/*  31 */     customSprays.put(this.uuid, this);
/*  32 */     WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
/*  33 */     this.enumDirection = getEnumDirection(blockFace);
/*  34 */     this.itemFrame = new EntityItemFrame(EntityTypes.ITEM_FRAME, (World)world);
/*  35 */     this.entity = (ItemFrame)this.itemFrame.getBukkitEntity();
/*  36 */     this.location = location;
/*  37 */     this.itemStack = CraftItemStack.asNMSCopy(itemStack);
/*  38 */     this.mapView = mapView;
/*  39 */     this.rotation = rotation;
/*  40 */     this.itemFrame.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  45 */     if (this.players.contains(player.getUniqueId())) {
/*  46 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  47 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  51 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  52 */       return;  this.itemFrame.setInvisible(true);
/*  53 */     this.itemFrame.setInvulnerable(true);
/*  54 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  56 */     this.itemFrame.setPositionRotation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  57 */     this.itemFrame.setDirection(this.enumDirection);
/*  58 */     this.itemFrame.setRotation(this.rotation);
/*  59 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/*  60 */     connection.sendPacket((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.c()));
/*  61 */     connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.getId(), this.itemFrame.getDataWatcher(), true));
/*  62 */     if (this.mapView != null) {
/*  63 */       player.sendMap(this.mapView);
/*     */     }
/*  65 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  70 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  71 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  72 */         continue;  spawn(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  78 */     for (UUID uuid : this.players) {
/*  79 */       Player player = Bukkit.getPlayer(uuid);
/*  80 */       if (player == null) {
/*  81 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/*  84 */       if (!this.players.contains(player.getUniqueId()))
/*  85 */         continue;  remove(player);
/*     */     } 
/*  87 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/*  92 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
/*  93 */     connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.getId() }));
/*  94 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/*  98 */     switch (facing) {
/*     */       case NORTH:
/*     */       case NORTH_EAST:
/*     */       case NORTH_WEST:
/*     */       case NORTH_NORTH_EAST:
/*     */       case NORTH_NORTH_WEST:
/* 104 */         return EnumDirection.NORTH;
/*     */       case SOUTH:
/*     */       case SOUTH_EAST:
/*     */       case SOUTH_WEST:
/*     */       case SOUTH_SOUTH_EAST:
/*     */       case SOUTH_SOUTH_WEST:
/* 110 */         return EnumDirection.SOUTH;
/*     */       case WEST:
/*     */       case WEST_NORTH_WEST:
/*     */       case WEST_SOUTH_WEST:
/* 114 */         return EnumDirection.WEST;
/*     */       case EAST:
/*     */       case EAST_NORTH_EAST:
/*     */       case EAST_SOUTH_EAST:
/* 118 */         return EnumDirection.EAST;
/*     */       case DOWN:
/* 120 */         return EnumDirection.DOWN;
/*     */     } 
/* 122 */     return EnumDirection.UP;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */