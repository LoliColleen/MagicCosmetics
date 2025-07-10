/*     */ package com.francobm.magicosmetics.nms.v1_19_R1.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
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
/* 176 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 177 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.t(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aw, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutSpawnEntity((EntityLiving)this.armorStand)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true)); connection.a((Packet)new PacketPlayOutSpawnEntity(this.leashed)); connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true)); if (!this.invisibleLeash) connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 180 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void update() { if (isBigHead()) {
/* 181 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 184 */     LivingEntity owner = getEntity();
/* 185 */     if (this.armorStand == null)
/* 186 */       return;  if (this.leashed == null)
/* 187 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 188 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 189 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 190 */     Location distance2 = stand.clone();
/* 191 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 193 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 194 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 195 */       if (!standDir.equals(new Vector())) {
/* 196 */         standDir.normalize();
/*     */       }
/* 198 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 199 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 200 */       Location newLocation = standTo.clone();
/* 201 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 202 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 204 */       if (!standDir.equals(new Vector())) {
/* 205 */         standDir.normalize();
/*     */       }
/* 207 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 208 */       if (!this.floatLoop) {
/* 209 */         this.y += 0.01D;
/* 210 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 212 */         if (this.y > 0.1D) {
/* 213 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 216 */         this.y -= 0.01D;
/* 217 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 219 */         if (this.y < -0.11D) {
/* 220 */           this.floatLoop = false;
/* 221 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 225 */       if (!this.rotateLoop) {
/* 226 */         this.rot += 0.01D;
/* 227 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 229 */         if (this.rot > 0.2D) {
/* 230 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 233 */         this.rot -= 0.01D;
/* 234 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
/*     */         
/* 236 */         if (this.rot < -0.2D) {
/* 237 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 240 */       Location newLocation = standToLoc.clone();
/* 241 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 242 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 244 */     for (UUID uuid : this.players) {
/* 245 */       Player player = Bukkit.getPlayer(uuid);
/* 246 */       if (player == null) {
/* 247 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 250 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 251 */       if (!this.invisibleLeash) {
/* 252 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 254 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 255 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 256 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 259 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 260 */       if (!this.heightLoop) {
/* 261 */         this.height += 0.01D;
/* 262 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         
/* 264 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 267 */     } else if (this.heightLoop) {
/* 268 */       this.height -= 0.01D;
/* 269 */       this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */       
/* 271 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 276 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 277 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 279 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() })); connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() })); this.players.remove(player.getUniqueId()); }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).b; connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/* 284 */      } public void updateBigHead() { LivingEntity owner = getEntity();
/* 285 */     if (this.armorStand == null)
/* 286 */       return;  if (this.leashed == null)
/* 287 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 288 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 289 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 290 */     Location distance2 = stand.clone();
/* 291 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 293 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 294 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 295 */       if (!standDir.equals(new Vector())) {
/* 296 */         standDir.normalize();
/*     */       }
/* 298 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 299 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 300 */       Location newLocation = standTo.clone();
/* 301 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 302 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 304 */       if (!standDir.equals(new Vector())) {
/* 305 */         standDir.normalize();
/*     */       }
/* 307 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 308 */       if (!this.floatLoop) {
/* 309 */         this.y += 0.01D;
/* 310 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 312 */         if (this.y > 0.1D) {
/* 313 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 316 */         this.y -= 0.01D;
/* 317 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 319 */         if (this.y < -0.11D) {
/* 320 */           this.floatLoop = false;
/* 321 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 325 */       if (!this.rotateLoop) {
/* 326 */         this.rot += 0.01D;
/* 327 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 329 */         if (this.rot > 0.2D) {
/* 330 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 333 */         this.rot -= 0.01D;
/* 334 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
/*     */         
/* 336 */         if (this.rot < -0.2D) {
/* 337 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 340 */       Location newLocation = standToLoc.clone();
/* 341 */       this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 342 */       this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 344 */     for (UUID uuid : this.players) {
/* 345 */       Player player = Bukkit.getPlayer(uuid);
/* 346 */       if (player == null) {
/* 347 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 350 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 351 */       if (!this.invisibleLeash) {
/* 352 */         p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 354 */       p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/* 355 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 356 */       p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 359 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 360 */       if (!this.heightLoop) {
/* 361 */         this.height += 0.01D;
/* 362 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         
/* 364 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 367 */     } else if (this.heightLoop) {
/* 368 */       this.height -= 0.01D;
/* 369 */       this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */       
/* 371 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 376 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 377 */       this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */     } else {
/* 379 */       this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(boolean rotation, RotationType rotationType, float rotate) {
/* 385 */     if (isBigHead()) {
/* 386 */       rotateBigHead(rotation, rotationType, rotate);
/*     */       return;
/*     */     } 
/* 389 */     if (!rotation)
/* 390 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 392 */         this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */       case UP:
/* 395 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
/*     */         break;
/*     */       case ALL:
/* 398 */         this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
/*     */         break;
/*     */     } 
/* 401 */     for (UUID uuid : this.players) {
/* 402 */       Player player = Bukkit.getPlayer(uuid);
/* 403 */       if (player == null) {
/* 404 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 407 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 412 */     if (!rotation)
/* 413 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 415 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */       case UP:
/* 418 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
/*     */         break;
/*     */       case ALL:
/* 421 */         this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
/*     */         break;
/*     */     } 
/* 424 */     for (UUID uuid : this.players) {
/* 425 */       Player player = Bukkit.getPlayer(uuid);
/* 426 */       if (player == null) {
/* 427 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 430 */       (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 435 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R1\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */