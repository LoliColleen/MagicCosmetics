/*     */ package com.francobm.magicosmetics.nms.v1_18_R1.cache;
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
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.animal.EntityPufferFish;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
/* 257 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 258 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.t(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); connection.a((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); if (!this.invisibleLeash) connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true)); this.viewers.add(player.getUniqueId()); }
/* 261 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
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
/* 278 */     Location stand = this.leashed.getBukkitEntity().getLocation();
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
/* 291 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 292 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 294 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 295 */       if (!standDir.equals(new Vector())) {
/* 296 */         standDir.normalize();
/*     */       }
/* 298 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 299 */       double distY = distVec.getY();
/* 300 */       if (owner.isSneaking()) {
/* 301 */         distY -= 0.13D;
/*     */       }
/* 303 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 304 */       if (!this.floatLoop) {
/* 305 */         this.y += 0.01D;
/* 306 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 308 */         if (this.y > 0.1D) {
/* 309 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 312 */         this.y -= 0.01D;
/* 313 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 315 */         if (this.y < -0.11D) {
/* 316 */           this.floatLoop = false;
/* 317 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 321 */       if (!this.rotateLoop) {
/* 322 */         this.rot += 0.01D;
/* 323 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 325 */         if (this.rot > 0.2D) {
/* 326 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 329 */         this.rot -= 0.01D;
/* 330 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 332 */         if (this.rot < -0.2D) {
/* 333 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 336 */       Location newLocation = standToLoc.clone();
/* 337 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 338 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
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
/* 350 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 351 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 352 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 355 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 356 */       if (!this.heightLoop) {
/* 357 */         this.height += 0.01D;
/* 358 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         
/* 360 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 363 */     } else if (this.heightLoop) {
/* 364 */       this.height -= 0.01D;
/* 365 */       this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
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
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { Location newLocation = location.add(0.0D, this.space, 0.0D); this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); }
/* 380 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone(); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(0)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
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
/* 402 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 403 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 405 */       if (!standDir.equals(new Vector())) {
/* 406 */         standDir.normalize();
/*     */       }
/* 408 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 409 */       if (!this.floatLoop) {
/* 410 */         this.y += 0.01D;
/* 411 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 413 */         if (this.y > 0.1D) {
/* 414 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 417 */         this.y -= 0.01D;
/* 418 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 420 */         if (this.y < -0.11D) {
/* 421 */           this.floatLoop = false;
/* 422 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 426 */       if (!this.rotateLoop) {
/* 427 */         this.rot += 0.01D;
/* 428 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 430 */         if (this.rot > 0.2D) {
/* 431 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 434 */         this.rot -= 0.01D;
/* 435 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 437 */         if (this.rot < -0.2D) {
/* 438 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 441 */       Location newLocation = standToLoc.clone();
/* 442 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 443 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 445 */     for (UUID uuid : this.viewers) {
/* 446 */       Player player = Bukkit.getPlayer(uuid);
/* 447 */       if (player == null) {
/* 448 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 451 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 452 */       if (!this.invisibleLeash) {
/* 453 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 455 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 456 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 457 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 460 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 461 */       if (!this.heightLoop) {
/* 462 */         this.height += 0.01D;
/* 463 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         
/* 465 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 468 */     } else if (this.heightLoop) {
/* 469 */       this.height -= 0.01D;
/* 470 */       this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */       
/* 472 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 477 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 478 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 480 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 486 */     if (isBigHead()) {
/* 487 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 490 */     if (!rotation)
/* 491 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 493 */         this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */       case UP:
/* 496 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         break;
/*     */       case ALL:
/* 499 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */     } 
/* 502 */     for (UUID uuid : this.viewers) {
/* 503 */       Player player = Bukkit.getPlayer(uuid);
/* 504 */       if (player == null) {
/* 505 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 508 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 513 */     if (!rotation)
/* 514 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 516 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */       case UP:
/* 519 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         break;
/*     */       case ALL:
/* 522 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */     } 
/* 525 */     for (UUID uuid : this.viewers) {
/* 526 */       Player player = Bukkit.getPlayer(uuid);
/* 527 */       if (player == null) {
/* 528 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 531 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 536 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */