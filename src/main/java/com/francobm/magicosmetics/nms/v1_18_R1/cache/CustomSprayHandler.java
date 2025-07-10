/*     */ package com.francobm.magicosmetics.nms.v1_18_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
/*  47 */     this.itemFrame.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   private final EnumDirection enumDirection; private final MapView mapView; private final int rotation;
/*     */   
/*     */   public void spawn(Player player) {
/*  52 */     if (this.players.contains(player.getUniqueId())) {
/*  53 */       if (!player.getWorld().equals(this.location.getWorld())) {
/*  54 */         remove(player);
/*     */       }
/*     */       return;
/*     */     } 
/*  58 */     if (!player.getWorld().equals(this.location.getWorld()))
/*  59 */       return;  this.itemFrame.j(true);
/*  60 */     this.itemFrame.m(true);
/*  61 */     this.itemFrame.setItem(this.itemStack, true, false);
/*     */     
/*  63 */     this.itemFrame.a(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
/*  64 */     this.itemFrame.a(this.enumDirection);
/*  65 */     this.itemFrame.a(this.rotation);
/*  66 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  67 */     connection.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.itemFrame, this.enumDirection.b()));
/*  68 */     connection.a((Packet)new PacketPlayOutEntityMetadata(this.itemFrame.ae(), this.itemFrame.ai(), true));
/*  69 */     if (this.mapView != null) {
/*  70 */       player.sendMap(this.mapView);
/*     */     }
/*  72 */     this.players.add(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(boolean exception) {
/*  77 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  78 */       if (exception && player.getUniqueId().equals(this.uuid))
/*  79 */         continue;  spawn(player);
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
/*  93 */     customSprays.remove(this.uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Player player) {
/*  98 */     PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
/*  99 */     connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.itemFrame.ae() }));
/* 100 */     this.players.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   private EnumDirection getEnumDirection(BlockFace facing) {
/* 104 */     switch (facing) {
/*     */       case NORTH:
/* 106 */         return EnumDirection.c;
/*     */       case SOUTH:
/* 108 */         return EnumDirection.d;
/*     */       case WEST:
/* 110 */         return EnumDirection.e;
/*     */       case EAST:
/* 112 */         return EnumDirection.f;
/*     */       case DOWN:
/* 114 */         return EnumDirection.a;
/*     */     } 
/* 116 */     return EnumDirection.b;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\CustomSprayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */