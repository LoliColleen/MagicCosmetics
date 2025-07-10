/*     */ package com.francobm.magicosmetics.nms.v1_19_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
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
/* 256 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 257 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.t(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aw, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutSpawnEntity((EntityLiving)this.armorStand)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); connection.a((Packet)new PacketPlayOutSpawnEntity(this.leashed)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true)); if (!this.invisibleLeash) connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 260 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
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
/* 290 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 291 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
/* 322 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 324 */         if (this.rot > 0.2D) {
/* 325 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 328 */         this.rot -= 0.01D;
/* 329 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 331 */         if (this.rot < -0.2D) {
/* 332 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 335 */       Location newLocation = standToLoc.clone();
/* 336 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 337 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 339 */     for (UUID uuid : this.viewers) {
/* 340 */       Player player = Bukkit.getPlayer(uuid);
/* 341 */       if (player == null) {
/* 342 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 345 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 346 */       if (!this.invisibleLeash) {
/* 347 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 349 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 350 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 351 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 354 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 355 */       if (!this.heightLoop) {
/* 356 */         this.height += 0.01D;
/* 357 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         
/* 359 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 362 */     } else if (this.heightLoop) {
/* 363 */       this.height -= 0.01D;
/* 364 */       this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */       
/* 366 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 371 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 372 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 374 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { Location newLocation = location.add(0.0D, this.space, 0.0D); this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); }
/* 379 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone(); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(0)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 380 */     if (owner == null)
/* 381 */       return;  if (this.armorStand == null)
/* 382 */       return;  if (this.leashed == null)
/* 383 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 384 */       spawn(false);
/*     */       return;
/*     */     } 
/* 387 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 388 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 389 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 390 */     Location distance2 = stand.clone();
/* 391 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 393 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 394 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 395 */       if (!standDir.equals(new Vector())) {
/* 396 */         standDir.normalize();
/*     */       }
/* 398 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 399 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 400 */       Location newLocation = standTo.clone();
/* 401 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 402 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 404 */       if (!standDir.equals(new Vector())) {
/* 405 */         standDir.normalize();
/*     */       }
/* 407 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 408 */       if (!this.floatLoop) {
/* 409 */         this.y += 0.01D;
/* 410 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 412 */         if (this.y > 0.1D) {
/* 413 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 416 */         this.y -= 0.01D;
/* 417 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 419 */         if (this.y < -0.11D) {
/* 420 */           this.floatLoop = false;
/* 421 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 425 */       if (!this.rotateLoop) {
/* 426 */         this.rot += 0.01D;
/* 427 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 429 */         if (this.rot > 0.2D) {
/* 430 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 433 */         this.rot -= 0.01D;
/* 434 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 436 */         if (this.rot < -0.2D) {
/* 437 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 440 */       Location newLocation = standToLoc.clone();
/* 441 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 442 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 444 */     for (UUID uuid : this.viewers) {
/* 445 */       Player player = Bukkit.getPlayer(uuid);
/* 446 */       if (player == null) {
/* 447 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 450 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 451 */       if (!this.invisibleLeash) {
/* 452 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 454 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 455 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 456 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 459 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 460 */       if (!this.heightLoop) {
/* 461 */         this.height += 0.01D;
/* 462 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         
/* 464 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 467 */     } else if (this.heightLoop) {
/* 468 */       this.height -= 0.01D;
/* 469 */       this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */       
/* 471 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 476 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 477 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 479 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 485 */     if (isBigHead()) {
/* 486 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 489 */     if (!rotation)
/* 490 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 492 */         this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */       case UP:
/* 495 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         break;
/*     */       case ALL:
/* 498 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */     } 
/* 501 */     for (UUID uuid : this.viewers) {
/* 502 */       Player player = Bukkit.getPlayer(uuid);
/* 503 */       if (player == null) {
/* 504 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 507 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 512 */     if (!rotation)
/* 513 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 515 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */       case UP:
/* 518 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         break;
/*     */       case ALL:
/* 521 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */     } 
/* 524 */     for (UUID uuid : this.viewers) {
/* 525 */       Player player = Bukkit.getPlayer(uuid);
/* 526 */       if (player == null) {
/* 527 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 530 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 535 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R1\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */