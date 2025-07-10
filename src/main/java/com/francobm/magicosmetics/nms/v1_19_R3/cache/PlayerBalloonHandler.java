/*     */ package com.francobm.magicosmetics.nms.v1_19_R3.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
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
/* 255 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 256 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.u(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.A().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aB, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand)); this.armorStand.aj().refresh(entityPlayer); entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed)); this.leashed.aj().refresh(entityPlayer); if (!this.invisibleLeash) entityPlayer.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 259 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.af() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.af() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 260 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 263 */     if (instantFollow) {
/* 264 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 267 */     Player owner = getPlayer();
/* 268 */     if (owner == null)
/* 269 */       return;  if (this.armorStand == null)
/* 270 */       return;  if (this.leashed == null)
/* 271 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 272 */       spawn(false);
/*     */       return;
/*     */     } 
/* 275 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 276 */     Location stand = this.leashed.getBukkitEntity().getLocation().clone();
/* 277 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 278 */     Location distance2 = stand.clone();
/* 279 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 281 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 282 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 283 */       if (!standDir.equals(new Vector())) {
/* 284 */         standDir.normalize();
/*     */       }
/* 286 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 287 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 288 */       Location newLocation = standTo.clone();
/* 289 */       teleport(newLocation);
/*     */     }
/*     */     else {
/*     */       
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
/* 322 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() - 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate));
/*     */         
/* 324 */         if (this.rot > 0.2D) {
/* 325 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 328 */         this.rot -= 0.01D;
/* 329 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate));
/*     */         
/* 331 */         if (this.rot < -0.2D) {
/* 332 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 335 */       Location newLocation = standToLoc.clone();
/* 336 */       teleport(newLocation);
/*     */     } 
/*     */ 
/*     */     
/* 340 */     for (UUID uuid : this.viewers) {
/* 341 */       Player player = Bukkit.getPlayer(uuid);
/* 342 */       if (player == null) {
/* 343 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 346 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 347 */       if (!this.invisibleLeash) {
/* 348 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 350 */       this.armorStand.aj().refresh(p);
/* 351 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 352 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 355 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 356 */       if (!this.heightLoop) {
/* 357 */         this.height += 0.01D;
/* 358 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() - 0.8F, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */         
/* 360 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 363 */     } else if (this.heightLoop) {
/* 364 */       this.height -= 0.01D;
/* 365 */       this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.8F, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */       
/* 367 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 372 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 373 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 375 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.af(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.af(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { this.leashed.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.a(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); }
/* 380 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(2)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.x().b() - 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.8F, this.armorStand.x().c(), this.armorStand.x().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.armorStand.aj().refresh(p); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 381 */     if (owner == null)
/* 382 */       return;  if (this.armorStand == null)
/* 383 */       return;  if (this.leashed == null)
/* 384 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 385 */       spawn(false);
/*     */       return;
/*     */     } 
/* 388 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 389 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 390 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 391 */     Location distance2 = stand.clone();
/* 392 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 394 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 395 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 396 */       if (!standDir.equals(new Vector())) {
/* 397 */         standDir.normalize();
/*     */       }
/* 399 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 400 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 401 */       Location newLocation = standTo.clone();
/* 402 */       teleport(newLocation);
/*     */     }
/*     */     else {
/*     */       
/* 406 */       if (!standDir.equals(new Vector())) {
/* 407 */         standDir.normalize();
/*     */       }
/* 409 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 410 */       if (!this.floatLoop) {
/* 411 */         this.y += 0.01D;
/* 412 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 414 */         if (this.y > 0.1D) {
/* 415 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 418 */         this.y -= 0.01D;
/* 419 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 421 */         if (this.y < -0.11D) {
/* 422 */           this.floatLoop = false;
/* 423 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 427 */       if (!this.rotateLoop) {
/* 428 */         this.rot += 0.01D;
/* 429 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 431 */         if (this.rot > 0.2D) {
/* 432 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 435 */         this.rot -= 0.01D;
/* 436 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 438 */         if (this.rot < -0.2D) {
/* 439 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 442 */       Location newLocation = standToLoc.clone();
/* 443 */       teleport(newLocation);
/*     */     } 
/*     */ 
/*     */     
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
/* 457 */       this.armorStand.aj().refresh(p);
/* 458 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 459 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 462 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 463 */       if (!this.heightLoop) {
/* 464 */         this.height += 0.01D;
/* 465 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() - 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         
/* 467 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 470 */     } else if (this.heightLoop) {
/* 471 */       this.height -= 0.01D;
/* 472 */       this.armorStand.d(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
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
/* 495 */         this.armorStand.a(new Vector3f(this.armorStand.x().b(), this.armorStand.x().c() + rotate, this.armorStand.x().d()));
/*     */         break;
/*     */       case UP:
/* 498 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + rotate, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */         break;
/*     */       case ALL:
/* 501 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + rotate, this.armorStand.x().c() + rotate, this.armorStand.x().d()));
/*     */         break;
/*     */     } 
/* 504 */     for (UUID uuid : this.viewers) {
/* 505 */       Player player = Bukkit.getPlayer(uuid);
/* 506 */       if (player == null) {
/* 507 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 510 */       this.armorStand.aj().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 515 */     if (!rotation)
/* 516 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 518 */         this.armorStand.d(new Vector3f(this.armorStand.A().b(), this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */       case UP:
/* 521 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         break;
/*     */       case ALL:
/* 524 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */     } 
/* 527 */     for (UUID uuid : this.viewers) {
/* 528 */       Player player = Bukkit.getPlayer(uuid);
/* 529 */       if (player == null) {
/* 530 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 533 */       this.armorStand.aj().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 538 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */