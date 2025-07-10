/*     */ package com.francobm.magicosmetics.nms.v1_21_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.util.CraftLocation;
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
/* 183 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.k(true); this.armorStand.n(true); this.armorStand.v(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.D().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aF, (World)world); this.leashed.collides = false; this.leashed.k(true); this.leashed.n(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand, 0, CraftLocation.toBlockPosition(this.armorStand.getBukkitEntity().getLocation()))); entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c())); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed, 0, CraftLocation.toBlockPosition(this.leashed.getBukkitEntity().getLocation()))); entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.leashed.an(), this.leashed.ar().c())); if (!this.invisibleLeash) entityPlayer.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 186 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.an(), this.leashed.an() })); this.players.remove(player.getUniqueId()); } public void update() { if (isBigHead()) {
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
/* 207 */       teleport(newLocation);
/*     */     } else {
/* 209 */       if (!standDir.equals(new Vector())) {
/* 210 */         standDir.normalize();
/*     */       }
/* 212 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 213 */       if (!this.floatLoop) {
/* 214 */         this.y += 0.01D;
/* 215 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 217 */         if (this.y > 0.1D) {
/* 218 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 221 */         this.y -= 0.01D;
/* 222 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 224 */         if (this.y < -0.11D) {
/* 225 */           this.floatLoop = false;
/* 226 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 230 */       if (!this.rotateLoop) {
/* 231 */         this.rot += 0.01D;
/* 232 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 234 */         if (this.rot > 0.2D) {
/* 235 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 238 */         this.rot -= 0.01D;
/* 239 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
/*     */         
/* 241 */         if (this.rot < -0.2D) {
/* 242 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 245 */       Location newLocation = standToLoc.clone();
/* 246 */       teleport(newLocation);
/*     */     } 
/* 248 */     for (UUID uuid : this.players) {
/* 249 */       Player player = Bukkit.getPlayer(uuid);
/* 250 */       if (player == null) {
/* 251 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 254 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 255 */       if (!this.invisibleLeash) {
/* 256 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 258 */       p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/* 259 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 260 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 263 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 264 */       if (!this.heightLoop) {
/* 265 */         this.height += 0.01D;
/* 266 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         
/* 268 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 271 */     } else if (this.heightLoop) {
/* 272 */       this.height -= 0.01D;
/* 273 */       this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */       
/* 275 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 280 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 281 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 283 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.an(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.an(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.an(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.an(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/*     */      }
/* 288 */   protected void teleport(Location location) { this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); } public void updateBigHead() { LivingEntity owner = getEntity();
/* 289 */     if (this.armorStand == null)
/* 290 */       return;  if (this.leashed == null)
/* 291 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 292 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 293 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 294 */     Location distance2 = stand.clone();
/* 295 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 297 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 298 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 299 */       if (!standDir.equals(new Vector())) {
/* 300 */         standDir.normalize();
/*     */       }
/* 302 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 303 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 304 */       Location newLocation = standTo.clone();
/* 305 */       this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 306 */       this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 308 */       if (!standDir.equals(new Vector())) {
/* 309 */         standDir.normalize();
/*     */       }
/* 311 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 312 */       if (!this.floatLoop) {
/* 313 */         this.y += 0.01D;
/* 314 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 316 */         if (this.y > 0.1D) {
/* 317 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 320 */         this.y -= 0.01D;
/* 321 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 323 */         if (this.y < -0.11D) {
/* 324 */           this.floatLoop = false;
/* 325 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 329 */       if (!this.rotateLoop) {
/* 330 */         this.rot += 0.01D;
/*     */         
/* 332 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
/*     */         
/* 334 */         if (this.rot > 0.2D) {
/* 335 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 338 */         this.rot -= 0.01D;
/* 339 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
/*     */         
/* 341 */         if (this.rot < -0.2D) {
/* 342 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 345 */       Location newLocation = standToLoc.clone();
/* 346 */       this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 347 */       this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 349 */     for (UUID uuid : this.players) {
/* 350 */       Player player = Bukkit.getPlayer(uuid);
/* 351 */       if (player == null) {
/* 352 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 355 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 356 */       if (!this.invisibleLeash) {
/* 357 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 359 */       p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/* 360 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 361 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 364 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 365 */       if (!this.heightLoop) {
/* 366 */         this.height += 0.01D;
/* 367 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */         
/* 369 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 372 */     } else if (this.heightLoop) {
/* 373 */       this.height -= 0.01D;
/* 374 */       this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */       
/* 376 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 381 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 382 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 384 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 390 */     if (isBigHead()) {
/* 391 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 394 */     if (!rotation)
/* 395 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 397 */         this.armorStand.a(new Vector3f(this.armorStand.A().b(), this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */       case UP:
/* 400 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c(), this.armorStand.A().d()));
/*     */         break;
/*     */       case ALL:
/* 403 */         this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c() + rotate, this.armorStand.A().d()));
/*     */         break;
/*     */     } 
/* 406 */     for (UUID uuid : this.players) {
/* 407 */       Player player = Bukkit.getPlayer(uuid);
/* 408 */       if (player == null) {
/* 409 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 412 */       (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 417 */     if (!rotation)
/* 418 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 420 */         this.armorStand.d(new Vector3f(this.armorStand.D().b(), this.armorStand.D().c() + rotate, this.armorStand.D().d()));
/*     */         break;
/*     */       case UP:
/* 423 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c(), this.armorStand.D().d()));
/*     */         break;
/*     */       case ALL:
/* 426 */         this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c() + rotate, this.armorStand.D().d()));
/*     */         break;
/*     */     } 
/* 429 */     for (UUID uuid : this.players) {
/* 430 */       Player player = Bukkit.getPlayer(uuid);
/* 431 */       if (player == null) {
/* 432 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 435 */       (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.an(), this.armorStand.ar().c()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 440 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_21_R1\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */