/*     */ package com.francobm.magicosmetics.nms.v1_20_R2.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
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
/* 256 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 257 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.u(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.C().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aB, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand)); this.armorStand.al().refresh(entityPlayer); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed)); this.leashed.al().refresh(entityPlayer); if (!this.invisibleLeash) entityPlayer.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 260 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ah() })); connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ah() })); connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ah() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 261 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 264 */     if (instantFollow) {
/* 265 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 268 */     Player owner = getPlayer();
/* 269 */     if (owner == null)
/* 270 */       return;  if (this.armorStand == null)
/* 271 */       return;  if (this.leashed == null)
/* 272 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 273 */       spawn(false);
/*     */       return;
/*     */     } 
/* 276 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 277 */     Location stand = this.leashed.getBukkitEntity().getLocation().clone();
/* 278 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 279 */     Location distance2 = stand.clone();
/* 280 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 282 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 283 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 284 */       if (!standDir.equals(new Vector())) {
/* 285 */         standDir.normalize();
/*     */       }
/* 287 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 288 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 289 */       Location newLocation = standTo.clone();
/* 290 */       teleport(newLocation);
/*     */     } else {
/* 292 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 293 */       if (!standDir.equals(new Vector())) {
/* 294 */         standDir.normalize();
/*     */       }
/* 296 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 297 */       double distY = distVec.getY();
/* 298 */       if (owner.isSneaking()) {
/* 299 */         distY -= 0.13D;
/*     */       }
/* 301 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 302 */       if (!this.floatLoop) {
/* 303 */         this.y += 0.01D;
/* 304 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 306 */         if (this.y > 0.1D) {
/* 307 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 310 */         this.y -= 0.01D;
/* 311 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 313 */         if (this.y < -0.11D) {
/* 314 */           this.floatLoop = false;
/* 315 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 319 */       if (!this.rotateLoop) {
/* 320 */         this.rot += 0.01D;
/* 321 */         this.armorStand.a(new Vector3f(this.armorStand.z().b() - 0.5F, this.armorStand.z().c(), this.armorStand.z().d() + this.rotate));
/*     */         
/* 323 */         if (this.rot > 0.2D) {
/* 324 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 327 */         this.rot -= 0.01D;
/* 328 */         this.armorStand.a(new Vector3f(this.armorStand.z().b() + 0.5F, this.armorStand.z().c(), this.armorStand.z().d() + this.rotate));
/*     */         
/* 330 */         if (this.rot < -0.2D) {
/* 331 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 334 */       Location newLocation = standToLoc.clone();
/* 335 */       teleport(newLocation);
/*     */     } 
/* 337 */     for (UUID uuid : this.viewers) {
/* 338 */       Player player = Bukkit.getPlayer(uuid);
/* 339 */       if (player == null) {
/* 340 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 343 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 344 */       if (!this.invisibleLeash) {
/* 345 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 347 */       this.armorStand.al().refresh(p);
/* 348 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 349 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 352 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 353 */       if (!this.heightLoop) {
/* 354 */         this.height += 0.01D;
/* 355 */         this.armorStand.a(new Vector3f(this.armorStand.z().b() - 0.8F, this.armorStand.z().c(), this.armorStand.z().d()));
/*     */         
/* 357 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 360 */     } else if (this.heightLoop) {
/* 361 */       this.height -= 0.01D;
/* 362 */       this.armorStand.a(new Vector3f(this.armorStand.z().b() + 0.8F, this.armorStand.z().c(), this.armorStand.z().d()));
/*     */       
/* 364 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 369 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 370 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 372 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ah(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ah(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ah(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ah(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); }
/* 377 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(2)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.z().b() - 0.5F, this.armorStand.z().c(), this.armorStand.z().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.z().b() + 0.5F, this.armorStand.z().c(), this.armorStand.z().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.z().b() + 0.8F, this.armorStand.z().c(), this.armorStand.z().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.armorStand.al().refresh(p); p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 378 */     if (owner == null)
/* 379 */       return;  if (this.armorStand == null)
/* 380 */       return;  if (this.leashed == null)
/* 381 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 382 */       spawn(false);
/*     */       return;
/*     */     } 
/* 385 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 386 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 387 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 388 */     Location distance2 = stand.clone();
/* 389 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 391 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 392 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 393 */       if (!standDir.equals(new Vector())) {
/* 394 */         standDir.normalize();
/*     */       }
/* 396 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 397 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 398 */       Location newLocation = standTo.clone();
/* 399 */       teleport(newLocation);
/*     */     } else {
/* 401 */       if (!standDir.equals(new Vector())) {
/* 402 */         standDir.normalize();
/*     */       }
/* 404 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 405 */       if (!this.floatLoop) {
/* 406 */         this.y += 0.01D;
/* 407 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 409 */         if (this.y > 0.1D) {
/* 410 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 413 */         this.y -= 0.01D;
/* 414 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 416 */         if (this.y < -0.11D) {
/* 417 */           this.floatLoop = false;
/* 418 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 422 */       if (!this.rotateLoop) {
/* 423 */         this.rot += 0.01D;
/* 424 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() - 0.5F, this.armorStand.C().c(), this.armorStand.C().d() + this.rotate));
/*     */         
/* 426 */         if (this.rot > 0.2D) {
/* 427 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 430 */         this.rot -= 0.01D;
/* 431 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + 0.5F, this.armorStand.C().c(), this.armorStand.C().d() + this.rotate));
/*     */         
/* 433 */         if (this.rot < -0.2D) {
/* 434 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 437 */       Location newLocation = standToLoc.clone();
/* 438 */       teleport(newLocation);
/*     */     } 
/* 440 */     for (UUID uuid : this.viewers) {
/* 441 */       Player player = Bukkit.getPlayer(uuid);
/* 442 */       if (player == null) {
/* 443 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 446 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 447 */       if (!this.invisibleLeash) {
/* 448 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 450 */       this.armorStand.al().refresh(p);
/* 451 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 452 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 455 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 456 */       if (!this.heightLoop) {
/* 457 */         this.height += 0.01D;
/* 458 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() - 0.8F, this.armorStand.C().c(), this.armorStand.C().d()));
/*     */         
/* 460 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 463 */     } else if (this.heightLoop) {
/* 464 */       this.height -= 0.01D;
/* 465 */       this.armorStand.d(new Vector3f(this.armorStand.C().b() + 0.8F, this.armorStand.C().c(), this.armorStand.C().d()));
/*     */       
/* 467 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 472 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 473 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 475 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 481 */     if (isBigHead()) {
/* 482 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 485 */     if (!rotation)
/* 486 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 488 */         this.armorStand.a(new Vector3f(this.armorStand.z().b(), this.armorStand.z().c() + rotate, this.armorStand.z().d()));
/*     */         break;
/*     */       case UP:
/* 491 */         this.armorStand.a(new Vector3f(this.armorStand.z().b() + rotate, this.armorStand.z().c(), this.armorStand.z().d()));
/*     */         break;
/*     */       case ALL:
/* 494 */         this.armorStand.a(new Vector3f(this.armorStand.z().b() + rotate, this.armorStand.z().c() + rotate, this.armorStand.z().d()));
/*     */         break;
/*     */     } 
/* 497 */     for (UUID uuid : this.viewers) {
/* 498 */       Player player = Bukkit.getPlayer(uuid);
/* 499 */       if (player == null) {
/* 500 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 503 */       this.armorStand.al().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 508 */     if (!rotation)
/* 509 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 511 */         this.armorStand.d(new Vector3f(this.armorStand.C().b(), this.armorStand.C().c() + rotate, this.armorStand.C().d()));
/*     */         break;
/*     */       case UP:
/* 514 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + rotate, this.armorStand.C().c(), this.armorStand.C().d()));
/*     */         break;
/*     */       case ALL:
/* 517 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + rotate, this.armorStand.C().c() + rotate, this.armorStand.C().d()));
/*     */         break;
/*     */     } 
/* 520 */     for (UUID uuid : this.viewers) {
/* 521 */       Player player = Bukkit.getPlayer(uuid);
/* 522 */       if (player == null) {
/* 523 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 526 */       this.armorStand.al().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 531 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R2\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */