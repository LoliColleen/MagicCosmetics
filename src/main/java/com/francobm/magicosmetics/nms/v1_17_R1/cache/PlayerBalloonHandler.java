/*     */ package com.francobm.magicosmetics.nms.v1_17_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
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
/* 260 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 261 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = player.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(player.getLocation().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world); this.armorStand.setInvulnerable(true); this.armorStand.setInvisible(true); this.armorStand.setMarker(true); this.armorStand.setPositionRotation(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.bigHead = bigHead; if (isBigHead()) this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world); this.leashed.setInvulnerable(true); this.leashed.setInvisible(true); this.leashed.setSilent(true); this.leashed.collides = false; this.leashed.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.leashed.getId(), this.leashed.getDataWatcher(), true)); if (!this.invisibleLeash) connection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 264 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  if (!this.viewers.contains(player.getUniqueId())) continue;  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() })); connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.getId() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 265 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 268 */     if (instantFollow) {
/* 269 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 272 */     Player owner = getPlayer();
/* 273 */     if (owner == null)
/* 274 */       return;  if (this.armorStand == null)
/* 275 */       return;  if (this.leashed == null)
/* 276 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 277 */       spawn(false);
/*     */       return;
/*     */     } 
/* 280 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 281 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 282 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 283 */     Location distance2 = stand.clone();
/* 284 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 286 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 287 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 288 */       if (!standDir.equals(new Vector())) {
/* 289 */         standDir.normalize();
/*     */       }
/* 291 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 292 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 293 */       Location newLocation = standTo.clone();
/* 294 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 295 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 297 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 298 */       if (!standDir.equals(new Vector())) {
/* 299 */         standDir.normalize();
/*     */       }
/* 301 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 302 */       double distY = distVec.getY();
/* 303 */       if (owner.isSneaking()) {
/* 304 */         distY -= 0.13D;
/*     */       }
/* 306 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 307 */       if (!this.floatLoop) {
/* 308 */         this.y += 0.01D;
/* 309 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 311 */         if (this.y > 0.1D) {
/* 312 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 315 */         this.y -= 0.01D;
/* 316 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 318 */         if (this.y < -0.11D) {
/* 319 */           this.floatLoop = false;
/* 320 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 324 */       if (!this.rotateLoop) {
/* 325 */         this.rot += 0.01D;
/* 326 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() - 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate));
/*     */         
/* 328 */         if (this.rot > 0.2D) {
/* 329 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 332 */         this.rot -= 0.01D;
/* 333 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate));
/*     */         
/* 335 */         if (this.rot < -0.2D) {
/* 336 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 339 */       Location newLocation = standToLoc.clone();
/* 340 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 341 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 343 */     for (UUID uuid : this.viewers) {
/* 344 */       Player player = Bukkit.getPlayer(uuid);
/* 345 */       if (player == null) {
/* 346 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 349 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 350 */       if (!this.invisibleLeash) {
/* 351 */         p.b.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 353 */       p.b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 354 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 355 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 358 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 359 */       if (!this.heightLoop) {
/* 360 */         this.height += 0.01D;
/* 361 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() - 0.8F, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */         
/* 363 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 366 */     } else if (this.heightLoop) {
/* 367 */       this.height -= 0.01D;
/* 368 */       this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.8F, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */       
/* 370 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 375 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 376 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 378 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { Location newLocation = location.add(0.0D, this.space, 0.0D); this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); }
/* 383 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone(); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(0)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() - 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.8F, this.armorStand.v().getY(), this.armorStand.v().getZ())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.b.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  p.b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 384 */     if (owner == null)
/* 385 */       return;  if (this.armorStand == null)
/* 386 */       return;  if (this.leashed == null)
/* 387 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 388 */       spawn(false);
/*     */       return;
/*     */     } 
/* 391 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 392 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 393 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 394 */     Location distance2 = stand.clone();
/* 395 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 397 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 398 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 399 */       if (!standDir.equals(new Vector())) {
/* 400 */         standDir.normalize();
/*     */       }
/* 402 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 403 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 404 */       Location newLocation = standTo.clone();
/* 405 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 406 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 408 */       if (!standDir.equals(new Vector())) {
/* 409 */         standDir.normalize();
/*     */       }
/* 411 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 412 */       if (!this.floatLoop) {
/* 413 */         this.y += 0.01D;
/* 414 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 416 */         if (this.y > 0.1D) {
/* 417 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 420 */         this.y -= 0.01D;
/* 421 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 423 */         if (this.y < -0.11D) {
/* 424 */           this.floatLoop = false;
/* 425 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 429 */       if (!this.rotateLoop) {
/* 430 */         this.rot += 0.01D;
/* 431 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() - 0.5F, this.armorStand.cj.getY(), this.armorStand.cj.getZ() + this.rotate));
/*     */         
/* 433 */         if (this.rot > 0.2D) {
/* 434 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 437 */         this.rot -= 0.01D;
/* 438 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + 0.5F, this.armorStand.cj.getY(), this.armorStand.cj.getZ() + this.rotate));
/*     */         
/* 440 */         if (this.rot < -0.2D) {
/* 441 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 444 */       Location newLocation = standToLoc.clone();
/* 445 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 446 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 448 */     for (UUID uuid : this.viewers) {
/* 449 */       Player player = Bukkit.getPlayer(uuid);
/* 450 */       if (player == null) {
/* 451 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 454 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 455 */       if (!this.invisibleLeash) {
/* 456 */         p.b.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 458 */       p.b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 459 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 460 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 463 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 464 */       if (!this.heightLoop) {
/* 465 */         this.height += 0.01D;
/* 466 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() - 0.8F, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */         
/* 468 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 471 */     } else if (this.heightLoop) {
/* 472 */       this.height -= 0.01D;
/* 473 */       this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + 0.8F, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */       
/* 475 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 480 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 481 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 483 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 490 */     if (isBigHead()) {
/* 491 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 494 */     if (!rotation)
/* 495 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 497 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX(), this.armorStand.v().getY() + rotate, this.armorStand.v().getZ()));
/*     */         break;
/*     */       case UP:
/* 500 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + rotate, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */         break;
/*     */       case ALL:
/* 503 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + rotate, this.armorStand.v().getY() + rotate, this.armorStand.v().getZ()));
/*     */         break;
/*     */     } 
/* 506 */     for (UUID uuid : this.viewers) {
/* 507 */       Player player = Bukkit.getPlayer(uuid);
/* 508 */       if (player == null) {
/* 509 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 512 */       (((CraftPlayer)player).getHandle()).b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 517 */     if (!rotation)
/* 518 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 520 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX(), this.armorStand.cj.getY() + rotate, this.armorStand.cj.getZ()));
/*     */         break;
/*     */       case UP:
/* 523 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + rotate, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */         break;
/*     */       case ALL:
/* 526 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + rotate, this.armorStand.cj.getY() + rotate, this.armorStand.cj.getZ()));
/*     */         break;
/*     */     } 
/* 529 */     for (UUID uuid : this.viewers) {
/* 530 */       Player player = Bukkit.getPlayer(uuid);
/* 531 */       if (player == null) {
/* 532 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 535 */       (((CraftPlayer)player).getHandle()).b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_17_R1\cache\PlayerBalloonHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */