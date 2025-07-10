/*     */ package com.francobm.magicosmetics.nms.v1_18_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
/* 179 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 180 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.t(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); connection.a((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed)); if (!this.invisibleLeash) connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true)); this.players.add(player.getUniqueId()); }
/* 183 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void update() { if (isBigHead()) {
/* 184 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 187 */     LivingEntity owner = getEntity();
/* 188 */     if (this.armorStand == null)
/* 189 */       return;  if (this.leashed == null)
/* 190 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 191 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 192 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 193 */     Location distance2 = stand.clone();
/* 194 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 196 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 197 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 198 */       if (!standDir.equals(new Vector())) {
/* 199 */         standDir.normalize();
/*     */       }
/* 201 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 202 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 203 */       Location newLocation = standTo.clone();
/* 204 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 205 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 207 */       if (!standDir.equals(new Vector())) {
/* 208 */         standDir.normalize();
/*     */       }
/* 210 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 211 */       if (!this.floatLoop) {
/* 212 */         this.y += 0.01D;
/* 213 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 215 */         if (this.y > 0.1D) {
/* 216 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 219 */         this.y -= 0.01D;
/* 220 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 222 */         if (this.y < -0.11D) {
/* 223 */           this.floatLoop = false;
/* 224 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 228 */       if (!this.rotateLoop) {
/* 229 */         this.rot += 0.01D;
/* 230 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 232 */         if (this.rot > 0.2D) {
/* 233 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 236 */         this.rot -= 0.01D;
/* 237 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 239 */         if (this.rot < -0.2D) {
/* 240 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 243 */       Location newLocation = standToLoc.clone();
/* 244 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 245 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
/* 257 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 258 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 259 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 262 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 263 */       if (!this.heightLoop) {
/* 264 */         this.height += 0.01D;
/* 265 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         
/* 267 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 270 */     } else if (this.heightLoop) {
/* 271 */       this.height -= 0.01D;
/* 272 */       this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
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
/*     */   public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() })); this.players.remove(player.getUniqueId()); }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/* 287 */      } public void updateBigHead() { LivingEntity owner = getEntity();
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
/* 330 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 332 */         if (this.rot > 0.2D) {
/* 333 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 336 */         this.rot -= 0.01D;
/* 337 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 339 */         if (this.rot < -0.2D) {
/* 340 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 343 */       Location newLocation = standToLoc.clone();
/* 344 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 345 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 347 */     for (UUID uuid : this.players) {
/* 348 */       Player player = Bukkit.getPlayer(uuid);
/* 349 */       if (player == null) {
/* 350 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 353 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 354 */       if (!this.invisibleLeash) {
/* 355 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 357 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 358 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 359 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 362 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 363 */       if (!this.heightLoop) {
/* 364 */         this.height += 0.01D;
/* 365 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         
/* 367 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 370 */     } else if (this.heightLoop) {
/* 371 */       this.height -= 0.01D;
/* 372 */       this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */       
/* 374 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 379 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 380 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 382 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 388 */     if (isBigHead()) {
/* 389 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 392 */     if (!rotation)
/* 393 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 395 */         this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */       case UP:
/* 398 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         break;
/*     */       case ALL:
/* 401 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */     } 
/* 404 */     for (UUID uuid : this.players) {
/* 405 */       Player player = Bukkit.getPlayer(uuid);
/* 406 */       if (player == null) {
/* 407 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 410 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 415 */     if (!rotation)
/* 416 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 418 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */       case UP:
/* 421 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         break;
/*     */       case ALL:
/* 424 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */     } 
/* 427 */     for (UUID uuid : this.players) {
/* 428 */       Player player = Bukkit.getPlayer(uuid);
/* 429 */       if (player == null) {
/* 430 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 433 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 438 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */