/*     */ package com.francobm.magicosmetics.nms.v1_18_R2.cache;
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
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityInsentient;
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.animal.EntityPufferFish;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
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
/* 259 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 260 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.t(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); ((EntityInsentient)this.leashed).s(true); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); connection.a((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true)); if (!this.invisibleLeash) connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 263 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 264 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 267 */     if (instantFollow) {
/* 268 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 271 */     Player owner = getPlayer();
/* 272 */     if (owner == null)
/* 273 */       return;  if (this.armorStand == null)
/* 274 */       return;  if (this.leashed == null)
/* 275 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 276 */       spawn(false);
/*     */       return;
/*     */     } 
/* 279 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 280 */     Location stand = this.leashed.getBukkitEntity().getLocation().clone();
/* 281 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 282 */     Location distance2 = stand.clone();
/* 283 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 285 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 286 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 287 */       if (!standDir.equals(new Vector())) {
/* 288 */         standDir.normalize();
/*     */       }
/* 290 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 291 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 292 */       Location newLocation = standTo.clone();
/* 293 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 294 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 296 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 297 */       if (!standDir.equals(new Vector())) {
/* 298 */         standDir.normalize();
/*     */       }
/* 300 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 301 */       double distY = distVec.getY();
/* 302 */       if (owner.isSneaking()) {
/* 303 */         distY -= 0.13D;
/*     */       }
/* 305 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 306 */       if (!this.floatLoop) {
/* 307 */         this.y += 0.01D;
/* 308 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 310 */         if (this.y > 0.1D) {
/* 311 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 314 */         this.y -= 0.01D;
/* 315 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 317 */         if (this.y < -0.11D) {
/* 318 */           this.floatLoop = false;
/* 319 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 323 */       if (!this.rotateLoop) {
/* 324 */         this.rot += 0.01D;
/* 325 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 327 */         if (this.rot > 0.2D) {
/* 328 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 331 */         this.rot -= 0.01D;
/* 332 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 334 */         if (this.rot < -0.2D) {
/* 335 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 338 */       Location newLocation = standToLoc.clone();
/* 339 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 340 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 342 */     for (UUID uuid : this.viewers) {
/* 343 */       Player player = Bukkit.getPlayer(uuid);
/* 344 */       if (player == null) {
/* 345 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 348 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 349 */       if (!this.invisibleLeash) {
/* 350 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 352 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 353 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 354 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 357 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 358 */       if (!this.heightLoop) {
/* 359 */         this.height += 0.01D;
/* 360 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         
/* 362 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 365 */     } else if (this.heightLoop) {
/* 366 */       this.height -= 0.01D;
/* 367 */       this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */       
/* 369 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 374 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 375 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 377 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { Location newLocation = location.add(0.0D, this.space, 0.0D); this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); }
/* 382 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone(); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(0)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 383 */     if (owner == null)
/* 384 */       return;  if (this.armorStand == null)
/* 385 */       return;  if (this.leashed == null)
/* 386 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 387 */       spawn(false);
/*     */       return;
/*     */     } 
/* 390 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 391 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 392 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 393 */     Location distance2 = stand.clone();
/* 394 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 396 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 397 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 398 */       if (!standDir.equals(new Vector())) {
/* 399 */         standDir.normalize();
/*     */       }
/* 401 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 402 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 403 */       Location newLocation = standTo.clone();
/* 404 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 405 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 407 */       if (!standDir.equals(new Vector())) {
/* 408 */         standDir.normalize();
/*     */       }
/* 410 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 411 */       if (!this.floatLoop) {
/* 412 */         this.y += 0.01D;
/* 413 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 415 */         if (this.y > 0.1D) {
/* 416 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 419 */         this.y -= 0.01D;
/* 420 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 422 */         if (this.y < -0.11D) {
/* 423 */           this.floatLoop = false;
/* 424 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 428 */       if (!this.rotateLoop) {
/* 429 */         this.rot += 0.01D;
/* 430 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 432 */         if (this.rot > 0.2D) {
/* 433 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 436 */         this.rot -= 0.01D;
/* 437 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 439 */         if (this.rot < -0.2D) {
/* 440 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 443 */       Location newLocation = standToLoc.clone();
/* 444 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 445 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 447 */     for (UUID uuid : this.viewers) {
/* 448 */       Player player = Bukkit.getPlayer(uuid);
/* 449 */       if (player == null) {
/* 450 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 453 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 454 */       if (!this.invisibleLeash) {
/* 455 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 457 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 458 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 459 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 462 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 463 */       if (!this.heightLoop) {
/* 464 */         this.height += 0.01D;
/* 465 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         
/* 467 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 470 */     } else if (this.heightLoop) {
/* 471 */       this.height -= 0.01D;
/* 472 */       this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */       
/* 474 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 479 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 480 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 482 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 488 */     if (isBigHead()) {
/* 489 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 492 */     if (!rotation)
/* 493 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 495 */         this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */       case UP:
/* 498 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         break;
/*     */       case ALL:
/* 501 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */     } 
/* 504 */     for (UUID uuid : this.viewers) {
/* 505 */       Player player = Bukkit.getPlayer(uuid);
/* 506 */       if (player == null) {
/* 507 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 510 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 515 */     if (!rotation)
/* 516 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 518 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */       case UP:
/* 521 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         break;
/*     */       case ALL:
/* 524 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */     } 
/* 527 */     for (UUID uuid : this.viewers) {
/* 528 */       Player player = Bukkit.getPlayer(uuid);
/* 529 */       if (player == null) {
/* 530 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 533 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 538 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R2\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */