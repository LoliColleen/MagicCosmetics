/*     */ package com.francobm.magicosmetics.nms.v1_17_R1.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
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
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
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
/*     */ public class EntityBalloonHandler
/*     */   extends EntityBalloon
/*     */ {
/*     */   private final EntityArmorStand armorStand;
/*     */   private final EntityLiving leashed;
/*     */   private final double distance;
/*     */   private final double SQUARED_WALKING;
/*     */   private final double SQUARED_DISTANCE;
/*     */   private final double CATCH_UP_INCREMENTS = 0.27D;
/*     */   private double CATCH_UP_INCREMENTS_DISTANCE;
/*     */   
/*     */   public EntityBalloonHandler(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 182 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 183 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world); this.armorStand.setInvulnerable(true); this.armorStand.setInvisible(true); this.armorStand.setMarker(true); this.armorStand.setPositionRotation(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.bigHead = bigHead; if (isBigHead()) this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world); this.leashed.setInvulnerable(true); this.leashed.setInvisible(true); this.leashed.setSilent(true); this.leashed.collides = false; this.leashed.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.leashed.getId(), this.leashed.getDataWatcher(), true)); if (!this.invisibleLeash) connection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 186 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  if (!this.players.contains(player.getUniqueId())) continue;  remove(player); }  entitiesBalloon.remove(this.uuid); } public void update() { if (isBigHead()) {
/* 187 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 190 */     LivingEntity owner = getEntity();
/* 191 */     if (this.armorStand == null)
/* 192 */       return;  if (this.leashed == null)
/* 193 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 194 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 195 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 196 */     Location distance2 = stand.clone();
/* 197 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 199 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 200 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 201 */       if (!standDir.equals(new Vector())) {
/* 202 */         standDir.normalize();
/*     */       }
/* 204 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 205 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 206 */       Location newLocation = standTo.clone();
/* 207 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 208 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 210 */       if (!standDir.equals(new Vector())) {
/* 211 */         standDir.normalize();
/*     */       }
/* 213 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 214 */       if (!this.floatLoop) {
/* 215 */         this.y += 0.01D;
/* 216 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 218 */         if (this.y > 0.1D) {
/* 219 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 222 */         this.y -= 0.01D;
/* 223 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 225 */         if (this.y < -0.11D) {
/* 226 */           this.floatLoop = false;
/* 227 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 231 */       if (!this.rotateLoop) {
/* 232 */         this.rot += 0.01D;
/* 233 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() - 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate));
/*     */         
/* 235 */         if (this.rot > 0.2D) {
/* 236 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 239 */         this.rot -= 0.01D;
/* 240 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.5F, this.armorStand.v().getY(), this.armorStand.v().getZ() + this.rotate));
/*     */         
/* 242 */         if (this.rot < -0.2D) {
/* 243 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 246 */       Location newLocation = standToLoc.clone();
/* 247 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 248 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 250 */     for (UUID uuid : this.players) {
/* 251 */       Player player = Bukkit.getPlayer(uuid);
/* 252 */       if (player == null) {
/* 253 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 256 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 257 */       if (!this.invisibleLeash) {
/* 258 */         p.b.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 260 */       p.b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 261 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 262 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 265 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 266 */       if (!this.heightLoop) {
/* 267 */         this.height += 0.01D;
/* 268 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() - 0.8F, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */         
/* 270 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 273 */     } else if (this.heightLoop) {
/* 274 */       this.height -= 0.01D;
/* 275 */       this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + 0.8F, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */       
/* 277 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 282 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 283 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 285 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() })); connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.getId() })); this.players.remove(player.getUniqueId()); }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/* 290 */      } public void updateBigHead() { LivingEntity owner = getEntity();
/* 291 */     if (this.armorStand == null)
/* 292 */       return;  if (this.leashed == null)
/* 293 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 294 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 295 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 296 */     Location distance2 = stand.clone();
/* 297 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 299 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 300 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 301 */       if (!standDir.equals(new Vector())) {
/* 302 */         standDir.normalize();
/*     */       }
/* 304 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 305 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 306 */       Location newLocation = standTo.clone();
/* 307 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 308 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 310 */       if (!standDir.equals(new Vector())) {
/* 311 */         standDir.normalize();
/*     */       }
/* 313 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 314 */       if (!this.floatLoop) {
/* 315 */         this.y += 0.01D;
/* 316 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 318 */         if (this.y > 0.1D) {
/* 319 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 322 */         this.y -= 0.01D;
/* 323 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 325 */         if (this.y < -0.11D) {
/* 326 */           this.floatLoop = false;
/* 327 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 331 */       if (!this.rotateLoop) {
/* 332 */         this.rot += 0.01D;
/* 333 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() - 0.5F, this.armorStand.cj.getY(), this.armorStand.cj.getZ() + this.rotate));
/*     */         
/* 335 */         if (this.rot > 0.2D) {
/* 336 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 339 */         this.rot -= 0.01D;
/* 340 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + 0.5F, this.armorStand.cj.getY(), this.armorStand.cj.getZ() + this.rotate));
/*     */         
/* 342 */         if (this.rot < -0.2D) {
/* 343 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 346 */       Location newLocation = standToLoc.clone();
/* 347 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 348 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 350 */     for (UUID uuid : this.players) {
/* 351 */       Player player = Bukkit.getPlayer(uuid);
/* 352 */       if (player == null) {
/* 353 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 356 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 357 */       if (!this.invisibleLeash) {
/* 358 */         p.b.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 360 */       p.b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 361 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 362 */       p.b.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 365 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 366 */       if (!this.heightLoop) {
/* 367 */         this.height += 0.01D;
/* 368 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() - 0.8F, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */         
/* 370 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 373 */     } else if (this.heightLoop) {
/* 374 */       this.height -= 0.01D;
/* 375 */       this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + 0.8F, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */       
/* 377 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 382 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 383 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 385 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 391 */     if (isBigHead()) {
/* 392 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 395 */     if (!rotation)
/* 396 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 398 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX(), this.armorStand.v().getY() + rotate, this.armorStand.v().getZ()));
/*     */         break;
/*     */       case UP:
/* 401 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + rotate, this.armorStand.v().getY(), this.armorStand.v().getZ()));
/*     */         break;
/*     */       case ALL:
/* 404 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.v().getX() + rotate, this.armorStand.v().getY() + rotate, this.armorStand.v().getZ()));
/*     */         break;
/*     */     } 
/* 407 */     for (UUID uuid : this.players) {
/* 408 */       Player player = Bukkit.getPlayer(uuid);
/* 409 */       if (player == null) {
/* 410 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 413 */       (((CraftPlayer)player).getHandle()).b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 418 */     if (!rotation)
/* 419 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 421 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX(), this.armorStand.cj.getY() + rotate, this.armorStand.cj.getZ()));
/*     */         break;
/*     */       case UP:
/* 424 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + rotate, this.armorStand.cj.getY(), this.armorStand.cj.getZ()));
/*     */         break;
/*     */       case ALL:
/* 427 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.cj.getX() + rotate, this.armorStand.cj.getY() + rotate, this.armorStand.cj.getZ()));
/*     */         break;
/*     */     } 
/* 430 */     for (UUID uuid : this.players) {
/* 431 */       Player player = Bukkit.getPlayer(uuid);
/* 432 */       if (player == null) {
/* 433 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 436 */       (((CraftPlayer)player).getHandle()).b.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_17_R1\cache\EntityBalloonHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */