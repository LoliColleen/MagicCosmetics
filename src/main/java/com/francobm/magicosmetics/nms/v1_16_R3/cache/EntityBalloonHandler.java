/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityArmorStand;
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
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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
/* 170 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 171 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)world); this.armorStand.setInvulnerable(true); this.armorStand.setInvisible(true); this.armorStand.setMarker(true); this.armorStand.setPositionRotation(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.bigHead = bigHead; if (isBigHead()) this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.PUFFERFISH, (World)world); this.leashed.setInvulnerable(true); this.leashed.setInvisible(true); this.leashed.setSilent(true); this.leashed.collides = false; this.leashed.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true)); connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.leashed.getId(), this.leashed.getDataWatcher(), true)); if (!this.invisibleLeash) connection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 174 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void update() { if (isBigHead()) {
/* 175 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 178 */     LivingEntity owner = getEntity();
/* 179 */     if (this.armorStand == null)
/* 180 */       return;  if (this.leashed == null)
/* 181 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 182 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 183 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 184 */     Location distance2 = stand.clone();
/* 185 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 187 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 188 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 189 */       if (!standDir.equals(new Vector())) {
/* 190 */         standDir.normalize();
/*     */       }
/* 192 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 193 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 194 */       Location newLocation = standTo.clone();
/* 195 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 196 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 198 */       if (!standDir.equals(new Vector())) {
/* 199 */         standDir.normalize();
/*     */       }
/* 201 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 202 */       if (!this.floatLoop) {
/* 203 */         this.y += 0.01D;
/* 204 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 206 */         if (this.y > 0.1D) {
/* 207 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 210 */         this.y -= 0.01D;
/* 211 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 213 */         if (this.y < -0.11D) {
/* 214 */           this.floatLoop = false;
/* 215 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 219 */       if (!this.rotateLoop) {
/* 220 */         this.rot += 0.01D;
/* 221 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
/*     */         
/* 223 */         if (this.rot > 0.2D) {
/* 224 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 227 */         this.rot -= 0.01D;
/* 228 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
/*     */         
/* 230 */         if (this.rot < -0.2D) {
/* 231 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 234 */       Location newLocation = standToLoc.clone();
/* 235 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 236 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 238 */     for (UUID uuid : this.players) {
/* 239 */       Player player = Bukkit.getPlayer(uuid);
/* 240 */       if (player == null) {
/* 241 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 244 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 245 */       if (!this.invisibleLeash) {
/* 246 */         p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 248 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 249 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 250 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 253 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 254 */       if (!this.heightLoop) {
/* 255 */         this.height += 0.01D;
/* 256 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */         
/* 258 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 261 */     } else if (this.heightLoop) {
/* 262 */       this.height -= 0.01D;
/* 263 */       this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */       
/* 265 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 270 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 271 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 273 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() })); connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.getId() })); this.players.remove(player.getUniqueId()); }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack))); connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection; connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/* 278 */      } public void updateBigHead() { LivingEntity owner = getEntity();
/* 279 */     if (this.armorStand == null)
/* 280 */       return;  if (this.leashed == null)
/* 281 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 282 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 283 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 284 */     Location distance2 = stand.clone();
/* 285 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 287 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 288 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 289 */       if (!standDir.equals(new Vector())) {
/* 290 */         standDir.normalize();
/*     */       }
/* 292 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 293 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 294 */       Location newLocation = standTo.clone();
/* 295 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 296 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 298 */       if (!standDir.equals(new Vector())) {
/* 299 */         standDir.normalize();
/*     */       }
/* 301 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
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
/* 321 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
/*     */         
/* 323 */         if (this.rot > 0.2D) {
/* 324 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 327 */         this.rot -= 0.01D;
/* 328 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
/*     */         
/* 330 */         if (this.rot < -0.2D) {
/* 331 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 334 */       Location newLocation = standToLoc.clone();
/* 335 */       this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 336 */       this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 338 */     for (UUID uuid : this.players) {
/* 339 */       Player player = Bukkit.getPlayer(uuid);
/* 340 */       if (player == null) {
/* 341 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 344 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 345 */       if (!this.invisibleLeash) {
/* 346 */         p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 348 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/* 349 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 350 */       p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 353 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 354 */       if (!this.heightLoop) {
/* 355 */         this.height += 0.01D;
/* 356 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */         
/* 358 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 361 */     } else if (this.heightLoop) {
/* 362 */       this.height -= 0.01D;
/* 363 */       this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */       
/* 365 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 370 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 371 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 373 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 379 */     if (isBigHead()) {
/* 380 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 383 */     if (!rotation)
/* 384 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 386 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX(), this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
/*     */         break;
/*     */       case UP:
/* 389 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY(), this.armorStand.r().getZ()));
/*     */         break;
/*     */       case ALL:
/* 392 */         this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
/*     */         break;
/*     */     } 
/* 395 */     for (UUID uuid : this.players) {
/* 396 */       Player player = Bukkit.getPlayer(uuid);
/* 397 */       if (player == null) {
/* 398 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 401 */       (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 406 */     if (!rotation)
/* 407 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 409 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */       case UP:
/* 412 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */       case ALL:
/* 415 */         this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
/*     */         break;
/*     */     } 
/* 418 */     for (UUID uuid : this.players) {
/* 419 */       Player player = Bukkit.getPlayer(uuid);
/* 420 */       if (player == null)
/* 421 */         continue;  (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */