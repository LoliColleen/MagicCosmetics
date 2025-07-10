/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityArmorStand;
/*     */ import net.minecraft.server.v1_16_R3.EntityInsentient;
/*     */ import net.minecraft.server.v1_16_R3.EntityLiving;
/*     */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*     */ import net.minecraft.server.v1_16_R3.EntityPufferFish;
/*     */ import net.minecraft.server.v1_16_R3.EntityTypes;
/*     */ import net.minecraft.server.v1_16_R3.EnumItemSlot;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.Packet;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
/*     */ import net.minecraft.server.v1_16_R3.PlayerConnection;
/*     */ import net.minecraft.server.v1_16_R3.Vector3f;
/*     */ import net.minecraft.server.v1_16_R3.World;
/*     */ import net.minecraft.server.v1_16_R3.WorldServer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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
/* 240 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 241 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.viewers = new CopyOnWriteArrayList(new ArrayList()); this.hideViewers = new CopyOnWriteArrayList(new ArrayList()); this.uuid = p.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; playerBalloons.put(this.uuid, this); Player player = getPlayer(); WorldServer world = ((CraftWorld)player.getWorld()).getHandle(); Location location = getPlayer().getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(getPlayer().getLocation().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)world); this.armorStand.setInvulnerable(true); this.armorStand.setInvisible(true); this.armorStand.setMarker(true); this.armorStand.setPositionRotation(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.bigHead = bigHead; if (isBigHead()) this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.PUFFERFISH, (World)world); this.leashed.setInvulnerable(true); this.leashed.setInvisible(true); this.leashed.setSilent(true); this.leashed.collides = false; this.leashed.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); ((EntityInsentient)this.leashed).setNoAI(true); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.hideViewers.contains(player.getUniqueId())) return;  Player owner = getPlayer(); if (owner == null) return;  if (this.viewers.contains(player.getUniqueId())) { if (!owner.getWorld().equals(player.getWorld())) { remove(player); return; }  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!owner.getWorld().equals(player.getWorld())) return;  if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.leashed.getId(), this.leashed.getDataWatcher(), true)); if (!this.invisibleLeash) connection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));  this.viewers.add(player.getUniqueId()); }
/* 244 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (Player player : Bukkit.getOnlinePlayers()) { if (!this.viewers.contains(player.getUniqueId())) continue;  remove(player); }  playerBalloons.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() })); connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.getId() })); this.viewers.remove(player.getUniqueId()); } public void update(boolean instantFollow) { if (isBigHead()) {
/* 245 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 248 */     if (instantFollow) {
/* 249 */       instantUpdate();
/*     */       return;
/*     */     } 
/* 252 */     Player owner = getPlayer();
/* 253 */     if (this.armorStand == null)
/* 254 */       return;  if (this.leashed == null)
/* 255 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 256 */       spawn(false);
/*     */       return;
/*     */     } 
/* 259 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 260 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 261 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 262 */     Location distance2 = stand.clone();
/* 263 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 265 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 266 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 267 */       if (!standDir.equals(new Vector())) {
/* 268 */         standDir.normalize();
/*     */       }
/* 270 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 271 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 272 */       Location newLocation = standTo.clone();
/* 273 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 274 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 276 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 277 */       if (!standDir.equals(new Vector())) {
/* 278 */         standDir.normalize();
/*     */       }
/* 280 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 281 */       double distY = distVec.getY();
/* 282 */       if (owner.isSneaking()) {
/* 283 */         distY -= 0.13D;
/*     */       }
/* 285 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
/* 286 */       if (!this.floatLoop) {
/* 287 */         this.y += 0.01D;
/* 288 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 290 */         if (this.y > 0.1D) {
/* 291 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 294 */         this.y -= 0.01D;
/* 295 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 297 */         if (this.y < -0.11D) {
/* 298 */           this.floatLoop = false;
/* 299 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 303 */       if (!this.rotateLoop) {
/* 304 */         this.rot += 0.01D;
/* 305 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
/*     */         
/* 307 */         if (this.rot > 0.2D) {
/* 308 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 311 */         this.rot -= 0.01D;
/* 312 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
/*     */         
/* 314 */         if (this.rot < -0.2D) {
/* 315 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 318 */       Location newLocation = standToLoc.clone();
/* 319 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 320 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 322 */     for (UUID uuid : this.viewers) {
/* 323 */       Player player = Bukkit.getPlayer(uuid);
/* 324 */       if (player == null)
/* 325 */         continue;  EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 326 */       if (!this.invisibleLeash) {
/* 327 */         p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 329 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 330 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 331 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 334 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 335 */       if (!this.heightLoop) {
/* 336 */         this.height += 0.01D;
/* 337 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */         
/* 339 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 342 */     } else if (this.heightLoop) {
/* 343 */       this.height -= 0.01D;
/* 344 */       this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */       
/* 346 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 351 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 352 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 354 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) continue;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void lookEntity(float yaw, float pitch) { for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) continue;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }  }
/*     */   protected void teleport(Location location) { Location newLocation = location.add(0.0D, this.space, 0.0D); this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch()); }
/* 359 */   protected void instantUpdate() { Player owner = getPlayer(); if (owner == null) return;  if (this.armorStand == null) return;  if (this.leashed == null) return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) { spawn(false); return; }  Location playerLoc = owner.getLocation().clone(); Location stand = this.leashed.getBukkitEntity().getLocation().clone(); Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector(); if (!standDir.equals(new Vector())) standDir.normalize();  Location standToLoc = playerLoc.setDirection(standDir.setY(0)); if (!this.floatLoop) { this.y += 0.01D; standToLoc.add(0.0D, 0.01D, 0.0D); if (this.y > 0.1D) this.floatLoop = true;  } else { this.y -= 0.01D; standToLoc.subtract(0.0D, 0.01D, 0.0D); if (this.y < -0.11D) { this.floatLoop = false; this.rotate *= -1.0F; }  }  teleport(standToLoc); if (!this.rotateLoop) { this.rot += 0.02D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate)); if (this.rot > 0.2D) this.rotateLoop = true;  } else { this.rot -= 0.02D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate)); if (this.rot < -0.2D) this.rotateLoop = false;  }  if (this.heightLoop) { this.height -= 0.01D; this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ())); if (this.height < -0.1D) this.heightLoop = false;  return; }  for (UUID uuid : this.viewers) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.viewers.remove(uuid); continue; }  EntityPlayer p = ((CraftPlayer)player).getHandle(); if (!this.invisibleLeash) p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (Entity)((CraftPlayer)owner).getHandle()));  p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed)); p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand)); }  } public void updateBigHead() { Player owner = getPlayer();
/* 360 */     if (this.armorStand == null)
/* 361 */       return;  if (this.leashed == null)
/* 362 */       return;  if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
/* 363 */       spawn(false);
/*     */       return;
/*     */     } 
/* 366 */     Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 367 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 368 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 369 */     Location distance2 = stand.clone();
/* 370 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 372 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 373 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 374 */       if (!standDir.equals(new Vector())) {
/* 375 */         standDir.normalize();
/*     */       }
/* 377 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 378 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 379 */       Location newLocation = standTo.clone();
/* 380 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 381 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 383 */       if (!standDir.equals(new Vector())) {
/* 384 */         standDir.normalize();
/*     */       }
/* 386 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 387 */       if (!this.floatLoop) {
/* 388 */         this.y += 0.01D;
/* 389 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 391 */         if (this.y > 0.1D) {
/* 392 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 395 */         this.y -= 0.01D;
/* 396 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 398 */         if (this.y < -0.11D) {
/* 399 */           this.floatLoop = false;
/* 400 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 404 */       if (!this.rotateLoop) {
/* 405 */         this.rot += 0.01D;
/* 406 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
/*     */         
/* 408 */         if (this.rot > 0.2D) {
/* 409 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 412 */         this.rot -= 0.01D;
/* 413 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
/*     */         
/* 415 */         if (this.rot < -0.2D) {
/* 416 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 419 */       Location newLocation = standToLoc.clone();
/* 420 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 421 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 423 */     for (UUID uuid : this.viewers) {
/* 424 */       Player player = Bukkit.getPlayer(uuid);
/* 425 */       if (player == null) {
/* 426 */         this.viewers.remove(uuid);
/*     */         continue;
/*     */       } 
/* 429 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 430 */       if (!this.invisibleLeash) {
/* 431 */         p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle()));
/*     */       }
/* 433 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 434 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 435 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 438 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 439 */       if (!this.heightLoop) {
/* 440 */         this.height += 0.01D;
/* 441 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */         
/* 443 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 446 */     } else if (this.heightLoop) {
/* 447 */       this.height -= 0.01D;
/* 448 */       this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */       
/* 450 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 455 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 456 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 458 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 464 */     if (isBigHead()) {
/* 465 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 468 */     if (!rotation)
/* 469 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 471 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX(), this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
/*     */         break;
/*     */       case UP:
/* 474 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */         break;
/*     */       case ALL:
/* 477 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
/*     */         break;
/*     */     } 
/* 480 */     for (UUID uuid : this.viewers) {
/* 481 */       Player player = Bukkit.getPlayer(uuid);
/* 482 */       if (player == null)
/* 483 */         continue;  (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 488 */     if (!rotation)
/* 489 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 491 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */       case UP:
/* 494 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */       case ALL:
/* 497 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */     } 
/* 500 */     for (UUID uuid : this.viewers) {
/* 501 */       Player player = Bukkit.getPlayer(uuid);
/* 502 */       if (player == null)
/* 503 */         continue;  (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */