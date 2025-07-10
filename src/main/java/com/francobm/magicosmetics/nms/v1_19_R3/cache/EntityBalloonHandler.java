/*     */ package com.francobm.magicosmetics.nms.v1_19_R3.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
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
/* 181 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 182 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.u(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.A().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aB, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand)); this.armorStand.aj().refresh(entityPlayer); entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed)); this.leashed.aj().refresh(entityPlayer); if (!this.invisibleLeash) entityPlayer.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 185 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.af() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.af() })); this.players.remove(player.getUniqueId()); } public void update() { if (isBigHead()) {
/* 186 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 189 */     LivingEntity owner = getEntity();
/* 190 */     if (this.armorStand == null)
/* 191 */       return;  if (this.leashed == null)
/* 192 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 193 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 194 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 195 */     Location distance2 = stand.clone();
/* 196 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 198 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 199 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 200 */       if (!standDir.equals(new Vector())) {
/* 201 */         standDir.normalize();
/*     */       }
/* 203 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 204 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 205 */       Location newLocation = standTo.clone();
/* 206 */       teleport(newLocation);
/*     */     } else {
/* 208 */       if (!standDir.equals(new Vector())) {
/* 209 */         standDir.normalize();
/*     */       }
/* 211 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 212 */       if (!this.floatLoop) {
/* 213 */         this.y += 0.01D;
/* 214 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 216 */         if (this.y > 0.1D) {
/* 217 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 220 */         this.y -= 0.01D;
/* 221 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 223 */         if (this.y < -0.11D) {
/* 224 */           this.floatLoop = false;
/* 225 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 229 */       if (!this.rotateLoop) {
/* 230 */         this.rot += 0.01D;
/* 231 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() - 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate));
/*     */         
/* 233 */         if (this.rot > 0.2D) {
/* 234 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 237 */         this.rot -= 0.01D;
/* 238 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.5F, this.armorStand.x().c(), this.armorStand.x().d() + this.rotate));
/*     */         
/* 240 */         if (this.rot < -0.2D) {
/* 241 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 244 */       Location newLocation = standToLoc.clone();
/* 245 */       teleport(newLocation);
/*     */     } 
/* 247 */     for (UUID uuid : this.players) {
/* 248 */       Player player = Bukkit.getPlayer(uuid);
/* 249 */       if (player == null) {
/* 250 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 253 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 254 */       if (!this.invisibleLeash) {
/* 255 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 257 */       this.armorStand.aj().refresh(p);
/* 258 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 259 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 262 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 263 */       if (!this.heightLoop) {
/* 264 */         this.height += 0.01D;
/* 265 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() - 0.8F, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */         
/* 267 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 270 */     } else if (this.heightLoop) {
/* 271 */       this.height -= 0.01D;
/* 272 */       this.armorStand.a(new Vector3f(this.armorStand.x().b() + 0.8F, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */       
/* 274 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 279 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 280 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 282 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.af(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.af(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.af(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/*     */      }
/* 287 */   protected void teleport(Location location) { this.leashed.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.a(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); } public void updateBigHead() { LivingEntity owner = getEntity();
/* 288 */     if (this.armorStand == null)
/* 289 */       return;  if (this.leashed == null)
/* 290 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 291 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 292 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 293 */     Location distance2 = stand.clone();
/* 294 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 296 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 297 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 298 */       if (!standDir.equals(new Vector())) {
/* 299 */         standDir.normalize();
/*     */       }
/* 301 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 302 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 303 */       Location newLocation = standTo.clone();
/* 304 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 305 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 307 */       if (!standDir.equals(new Vector())) {
/* 308 */         standDir.normalize();
/*     */       }
/* 310 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 311 */       if (!this.floatLoop) {
/* 312 */         this.y += 0.01D;
/* 313 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 315 */         if (this.y > 0.1D) {
/* 316 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 319 */         this.y -= 0.01D;
/* 320 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 322 */         if (this.y < -0.11D) {
/* 323 */           this.floatLoop = false;
/* 324 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 328 */       if (!this.rotateLoop) {
/* 329 */         this.rot += 0.01D;
/*     */         
/* 331 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 333 */         if (this.rot > 0.2D) {
/* 334 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 337 */         this.rot -= 0.01D;
/* 338 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 340 */         if (this.rot < -0.2D) {
/* 341 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 344 */       Location newLocation = standToLoc.clone();
/* 345 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 346 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 348 */     for (UUID uuid : this.players) {
/* 349 */       Player player = Bukkit.getPlayer(uuid);
/* 350 */       if (player == null) {
/* 351 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 354 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 355 */       if (!this.invisibleLeash) {
/* 356 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 358 */       this.armorStand.aj().refresh(p);
/* 359 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 360 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 363 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 364 */       if (!this.heightLoop) {
/* 365 */         this.height += 0.01D;
/* 366 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() - 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         
/* 368 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 371 */     } else if (this.heightLoop) {
/* 372 */       this.height -= 0.01D;
/* 373 */       this.armorStand.d(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */       
/* 375 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 380 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 381 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 383 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 389 */     if (isBigHead()) {
/* 390 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 393 */     if (!rotation)
/* 394 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 396 */         this.armorStand.a(new Vector3f(this.armorStand.x().b(), this.armorStand.x().c() + rotate, this.armorStand.x().d()));
/*     */         break;
/*     */       case UP:
/* 399 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + rotate, this.armorStand.x().c(), this.armorStand.x().d()));
/*     */         break;
/*     */       case ALL:
/* 402 */         this.armorStand.a(new Vector3f(this.armorStand.x().b() + rotate, this.armorStand.x().c() + rotate, this.armorStand.x().d()));
/*     */         break;
/*     */     } 
/* 405 */     for (UUID uuid : this.players) {
/* 406 */       Player player = Bukkit.getPlayer(uuid);
/* 407 */       if (player == null) {
/* 408 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 411 */       this.armorStand.aj().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 416 */     if (!rotation)
/* 417 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 419 */         this.armorStand.d(new Vector3f(this.armorStand.A().b(), this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */       case UP:
/* 422 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         break;
/*     */       case ALL:
/* 425 */         this.armorStand.d(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */     } 
/* 428 */     for (UUID uuid : this.players) {
/* 429 */       Player player = Bukkit.getPlayer(uuid);
/* 430 */       if (player == null) {
/* 431 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 434 */       this.armorStand.aj().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 439 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */