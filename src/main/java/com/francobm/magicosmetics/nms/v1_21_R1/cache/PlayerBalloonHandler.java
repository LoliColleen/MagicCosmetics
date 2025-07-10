/*     */ package com.francobm.magicosmetics.nms.v1_21_R1.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.core.Vector3f;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.animal.EntityPufferFish;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.util.CraftLocation;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerBalloonHandler
/*     */   extends PlayerBalloon
/*     */ {
/*     */   private final EntityArmorStand armorStand;
/*     */   private final EntityLiving leashed;
/*     */   private final double distance;
/*     */   private final double SQUARED_WALKING;
/*     */   private final double SQUARED_DISTANCE;
/*     */   private final double CATCH_UP_INCREMENTS = 0.27D;
/*     */   private double CATCH_UP_INCREMENTS_DISTANCE;
/*     */   
/*     */   public PlayerBalloonHandler(Player p, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 257 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 258 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.k(true); this.armorStand.n(true); this.armorStand.v(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.D().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aF, (World)world); this.leashed.collides = false; this.leashed.k(true); this.leashed.n(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand, 0, CraftLocation.toBlockPosition(this.armorStand.getBukkitEntity().getLocation()))); entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c())); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed, 0, CraftLocation.toBlockPosition(this.leashed.getBukkitEntity().getLocation()))); entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.leashed.an(), this.leashed.ar().c())); if (!this.invisibleLeash) entityPlayer.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 261 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.an() })); connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.an() })); connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.an() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 262 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 265 */     if (instantFollow) {
/* 266 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 269 */     Player owner = getPlayer();
/* 270 */     if (owner == null)
/* 271 */       return;  if (this.armorStand == null)
/* 272 */       return;  if (this.leashed == null)
/* 273 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 274 */       spawn(false);
/*     */       return;
/*     */     } 
/* 277 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 278 */     Location stand = this.leashed.getBukkitEntity().getLocation().clone();
/* 279 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 280 */     Location distance2 = stand.clone();
/* 281 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 283 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 284 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 285 */       if (!standDir.equals(new Vector())) {
/* 286 */         standDir.normalize();
/*     */       }
/* 288 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 289 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 290 */       Location newLocation = standTo.clone();
/* 291 */       teleport(newLocation);
/*     */     } else {
/* 293 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 294 */       if (!standDir.equals(new Vector())) {
/* 295 */         standDir.normalize();
/*     */       }
/* 297 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 298 */       double distY = distVec.getY();
/* 299 */       if (owner.isSneaking()) {
/* 300 */         distY -= 0.13D;
/*     */       }
/* 302 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 303 */       if (!this.floatLoop) {
/* 304 */         this.y += 0.01D;
/* 305 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 307 */         if (this.y > 0.1D) {
/* 308 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 311 */         this.y -= 0.01D;
/* 312 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 314 */         if (this.y < -0.11D) {
/* 315 */           this.floatLoop = false;
/* 316 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 320 */       if (!this.rotateLoop) {
/* 321 */         this.rot += 0.01D;
/* 322 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 324 */         if (this.rot > 0.2D) {
/* 325 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 328 */         this.rot -= 0.01D;
/* 329 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 331 */         if (this.rot < -0.2D) {
/* 332 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 335 */       Location newLocation = standToLoc.clone();
/* 336 */       teleport(newLocation);
/*     */     } 
/* 338 */     for (UUID uuid : this.viewers) {
/* 339 */       Player player = Bukkit.getPlayer(uuid);
/* 340 */       if (player == null) {
/* 341 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 344 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 345 */       if (!this.invisibleLeash) {
/* 346 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 348 */       p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/* 349 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 350 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 353 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 354 */       if (!this.heightLoop) {
/* 355 */         this.height += 0.01D;
/* 356 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         
/* 358 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 361 */     } else if (this.heightLoop) {
/* 362 */       this.height -= 0.01D;
/* 363 */       this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */       
/* 365 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 370 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 371 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 373 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.an(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.an(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.an(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.an(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); }
/* 378 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(2)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c())); p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 379 */     if (owner == null)
/* 380 */       return;  if (this.armorStand == null)
/* 381 */       return;  if (this.leashed == null)
/* 382 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 383 */       spawn(false);
/*     */       return;
/*     */     } 
/* 386 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 387 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 388 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 389 */     Location distance2 = stand.clone();
/* 390 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 392 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 393 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 394 */       if (!standDir.equals(new Vector())) {
/* 395 */         standDir.normalize();
/*     */       }
/* 397 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 398 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 399 */       Location newLocation = standTo.clone();
/* 400 */       teleport(newLocation);
/*     */     } else {
/* 402 */       if (!standDir.equals(new Vector())) {
/* 403 */         standDir.normalize();
/*     */       }
/* 405 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 406 */       if (!this.floatLoop) {
/* 407 */         this.y += 0.01D;
/* 408 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 410 */         if (this.y > 0.1D) {
/* 411 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 414 */         this.y -= 0.01D;
/* 415 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 417 */         if (this.y < -0.11D) {
/* 418 */           this.floatLoop = false;
/* 419 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 423 */       if (!this.rotateLoop) {
/* 424 */         this.rot += 0.01D;
/* 425 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
/*     */         
/* 427 */         if (this.rot > 0.2D) {
/* 428 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 431 */         this.rot -= 0.01D;
/* 432 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
/*     */         
/* 434 */         if (this.rot < -0.2D) {
/* 435 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 438 */       Location newLocation = standToLoc.clone();
/* 439 */       teleport(newLocation);
/*     */     } 
/* 441 */     for (UUID uuid : this.viewers) {
/* 442 */       Player player = Bukkit.getPlayer(uuid);
/* 443 */       if (player == null) {
/* 444 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 447 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 448 */       if (!this.invisibleLeash) {
/* 449 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 451 */       p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/* 452 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 453 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 456 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 457 */       if (!this.heightLoop) {
/* 458 */         this.height += 0.01D;
/* 459 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */         
/* 461 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 464 */     } else if (this.heightLoop) {
/* 465 */       this.height -= 0.01D;
/* 466 */       this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */       
/* 468 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 473 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 474 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 476 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 482 */     if (isBigHead()) {
/* 483 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 486 */     if (!rotation)
/* 487 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 489 */         this.armorStand.a(new Vector3f(this.armorStand.A().b(), this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */       case UP:
/* 492 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         break;
/*     */       case ALL:
/* 495 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */     } 
/* 498 */     for (UUID uuid : this.viewers) {
/* 499 */       Player player = Bukkit.getPlayer(uuid);
/* 500 */       if (player == null) {
/* 501 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 504 */       (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 509 */     if (!rotation)
/* 510 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 512 */         this.armorStand.d(new Vector3f(this.armorStand.D().b(), this.armorStand.D().c() + rotate, this.armorStand.D().d()));
/*     */         break;
/*     */       case UP:
/* 515 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */         break;
/*     */       case ALL:
/* 518 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c() + rotate, this.armorStand.D().d()));
/*     */         break;
/*     */     } 
/* 521 */     for (UUID uuid : this.viewers) {
/* 522 */       Player player = Bukkit.getPlayer(uuid);
/* 523 */       if (player == null) {
/* 524 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 527 */       (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 532 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_21_R1\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */