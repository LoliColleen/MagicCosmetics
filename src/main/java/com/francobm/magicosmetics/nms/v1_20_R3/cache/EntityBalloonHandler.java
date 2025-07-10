/*     */ package com.francobm.magicosmetics.nms.v1_20_R3.cache;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
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
/* 180 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 181 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.players = new CopyOnWriteArrayList(new ArrayList()); this.uuid = entity.getUniqueId(); this.distance = distance; this.invisibleLeash = invisibleLeash; entitiesBalloon.put(this.uuid, this); this.entity = (LivingEntity)entity; WorldServer world = ((CraftWorld)entity.getWorld()).getHandle(); Location location = entity.getLocation().clone().add(0.0D, space, 0.0D); location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1)); this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.j(true); this.armorStand.m(true); this.armorStand.u(true); this.bigHead = bigHead; if (isBigHead()) this.armorStand.d(new Vector3f(this.armorStand.C().b(), 0.0F, 0.0F));  this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aC, (World)world); this.leashed.collides = false; this.leashed.j(true); this.leashed.m(true); this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.space = space; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space;
/*     */   }
/*     */   public void spawn(Player player) { if (this.players.contains(player.getUniqueId())) { if (!getEntity().getWorld().equals(player.getWorld())) { remove(player); return; }  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) remove(player);  return; }  if (!getEntity().getWorld().equals(player.getWorld())) return;  if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance) return;  EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle(); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand)); this.armorStand.an().refresh(entityPlayer); entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed)); this.leashed.an().refresh(entityPlayer); if (!this.invisibleLeash) entityPlayer.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));  this.players.add(player.getUniqueId()); }
/* 184 */   public void spawn(boolean exception) { for (Player player : Bukkit.getOnlinePlayers()) { if (exception && player.getUniqueId().equals(this.uuid)) continue;  spawn(player); }  } public void remove() { for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  remove(player); }  entitiesBalloon.remove(this.uuid); } public void remove(Player player) { PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.aj(), this.leashed.aj() })); this.players.remove(player.getUniqueId()); } public void update() { if (isBigHead()) {
/* 185 */       updateBigHead();
/*     */       return;
/*     */     } 
/* 188 */     LivingEntity owner = getEntity();
/* 189 */     if (this.armorStand == null)
/* 190 */       return;  if (this.leashed == null)
/* 191 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 192 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 193 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 194 */     Location distance2 = stand.clone();
/* 195 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 197 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 198 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 199 */       if (!standDir.equals(new Vector())) {
/* 200 */         standDir.normalize();
/*     */       }
/* 202 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 203 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 204 */       Location newLocation = standTo.clone();
/* 205 */       teleport(newLocation);
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
/* 230 */         this.armorStand.a(new Vector3f(this.armorStand.B().b() - 0.5F, this.armorStand.B().c(), this.armorStand.B().d() + this.rotate));
/*     */         
/* 232 */         if (this.rot > 0.2D) {
/* 233 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 236 */         this.rot -= 0.01D;
/* 237 */         this.armorStand.a(new Vector3f(this.armorStand.B().b() + 0.5F, this.armorStand.B().c(), this.armorStand.B().d() + this.rotate));
/*     */         
/* 239 */         if (this.rot < -0.2D) {
/* 240 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 243 */       Location newLocation = standToLoc.clone();
/* 244 */       teleport(newLocation);
/*     */     } 
/* 246 */     for (UUID uuid : this.players) {
/* 247 */       Player player = Bukkit.getPlayer(uuid);
/* 248 */       if (player == null) {
/* 249 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 252 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 253 */       if (!this.invisibleLeash) {
/* 254 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 256 */       this.armorStand.an().refresh(p);
/* 257 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 258 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 261 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 262 */       if (!this.heightLoop) {
/* 263 */         this.height += 0.01D;
/* 264 */         this.armorStand.a(new Vector3f(this.armorStand.B().b() - 0.8F, this.armorStand.B().c(), this.armorStand.B().d()));
/*     */         
/* 266 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 269 */     } else if (this.heightLoop) {
/* 270 */       this.height -= 0.01D;
/* 271 */       this.armorStand.a(new Vector3f(this.armorStand.B().b() + 0.8F, this.armorStand.B().c(), this.armorStand.B().d()));
/*     */       
/* 273 */       if (this.height < -0.1D) this.heightLoop = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 278 */     if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE)
/* 279 */     { this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D; }
/*     */     else
/* 281 */     { this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; }  }
/*     */   public void setItem(ItemStack itemStack) { if (isBigHead()) { setItemBigHead(itemStack); return; }  for (UUID uuid : this.players) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack))); Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.aj(), list)); }  }
/*     */   public void setItemBigHead(ItemStack itemStack) { ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>(); list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack))); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.aj(), list)); }  }
/*     */   public void lookEntity() { float yaw = getEntity().getLocation().getYaw(); for (UUID uuid : this.players) { Player player = Bukkit.getPlayer(uuid); if (player == null) { this.players.remove(uuid); continue; }  PlayerConnection connection = (((CraftPlayer)player).getHandle()).c; connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.aj(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F))); connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.aj(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true)); }
/*     */      }
/* 286 */   protected void teleport(Location location) { this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch()); } public void updateBigHead() { LivingEntity owner = getEntity();
/* 287 */     if (this.armorStand == null)
/* 288 */       return;  if (this.leashed == null)
/* 289 */       return;  Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 290 */     Location stand = this.leashed.getBukkitEntity().getLocation();
/* 291 */     Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
/* 292 */     Location distance2 = stand.clone();
/* 293 */     Location distance1 = owner.getLocation().clone();
/*     */     
/* 295 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 296 */       Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 297 */       if (!standDir.equals(new Vector())) {
/* 298 */         standDir.normalize();
/*     */       }
/* 300 */       Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 301 */       Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 302 */       Location newLocation = standTo.clone();
/* 303 */       this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 304 */       this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } else {
/* 306 */       if (!standDir.equals(new Vector())) {
/* 307 */         standDir.normalize();
/*     */       }
/* 309 */       Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 310 */       if (!this.floatLoop) {
/* 311 */         this.y += 0.01D;
/* 312 */         standToLoc.add(0.0D, 0.01D, 0.0D);
/*     */         
/* 314 */         if (this.y > 0.1D) {
/* 315 */           this.floatLoop = true;
/*     */         }
/*     */       } else {
/* 318 */         this.y -= 0.01D;
/* 319 */         standToLoc.subtract(0.0D, 0.01D, 0.0D);
/*     */         
/* 321 */         if (this.y < -0.11D) {
/* 322 */           this.floatLoop = false;
/* 323 */           this.rotate *= -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 327 */       if (!this.rotateLoop) {
/* 328 */         this.rot += 0.01D;
/*     */         
/* 330 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() - 0.5F, this.armorStand.C().c(), this.armorStand.C().d() + this.rotate));
/*     */         
/* 332 */         if (this.rot > 0.2D) {
/* 333 */           this.rotateLoop = true;
/*     */         }
/*     */       } else {
/* 336 */         this.rot -= 0.01D;
/* 337 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + 0.5F, this.armorStand.C().c(), this.armorStand.C().d() + this.rotate));
/*     */         
/* 339 */         if (this.rot < -0.2D) {
/* 340 */           this.rotateLoop = false;
/*     */         }
/*     */       } 
/* 343 */       Location newLocation = standToLoc.clone();
/* 344 */       this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/* 345 */       this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
/*     */     } 
/* 347 */     for (UUID uuid : this.players) {
/* 348 */       Player player = Bukkit.getPlayer(uuid);
/* 349 */       if (player == null) {
/* 350 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 353 */       EntityPlayer p = ((CraftPlayer)player).getHandle();
/* 354 */       if (!this.invisibleLeash) {
/* 355 */         p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle()));
/*     */       }
/* 357 */       this.armorStand.an().refresh(p);
/* 358 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
/* 359 */       p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
/*     */     } 
/*     */     
/* 362 */     if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 363 */       if (!this.heightLoop) {
/* 364 */         this.height += 0.01D;
/* 365 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() - 0.8F, this.armorStand.C().c(), this.armorStand.C().d()));
/*     */         
/* 367 */         if (this.height > 0.1D) this.heightLoop = true;
/*     */       
/*     */       } 
/* 370 */     } else if (this.heightLoop) {
/* 371 */       this.height -= 0.01D;
/* 372 */       this.armorStand.d(new Vector3f(this.armorStand.C().b() + 0.8F, this.armorStand.C().c(), this.armorStand.C().d()));
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
/* 395 */         this.armorStand.a(new Vector3f(this.armorStand.B().b(), this.armorStand.B().c() + rotate, this.armorStand.B().d()));
/*     */         break;
/*     */       case UP:
/* 398 */         this.armorStand.a(new Vector3f(this.armorStand.B().b() + rotate, this.armorStand.B().c(), this.armorStand.B().d()));
/*     */         break;
/*     */       case ALL:
/* 401 */         this.armorStand.a(new Vector3f(this.armorStand.B().b() + rotate, this.armorStand.B().c() + rotate, this.armorStand.B().d()));
/*     */         break;
/*     */     } 
/* 404 */     for (UUID uuid : this.players) {
/* 405 */       Player player = Bukkit.getPlayer(uuid);
/* 406 */       if (player == null) {
/* 407 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 410 */       this.armorStand.an().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
/* 415 */     if (!rotation)
/* 416 */       return;  switch (rotationType) {
/*     */       case RIGHT:
/* 418 */         this.armorStand.d(new Vector3f(this.armorStand.C().b(), this.armorStand.C().c() + rotate, this.armorStand.C().d()));
/*     */         break;
/*     */       case UP:
/* 421 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + rotate, this.armorStand.C().c(), this.armorStand.C().d()));
/*     */         break;
/*     */       case ALL:
/* 424 */         this.armorStand.d(new Vector3f(this.armorStand.C().b() + rotate, this.armorStand.C().c() + rotate, this.armorStand.C().d()));
/*     */         break;
/*     */     } 
/* 427 */     for (UUID uuid : this.players) {
/* 428 */       Player player = Bukkit.getPlayer(uuid);
/* 429 */       if (player == null) {
/* 430 */         this.players.remove(uuid);
/*     */         continue;
/*     */       } 
/* 433 */       this.armorStand.an().refresh(((CraftPlayer)player).getHandle());
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 438 */     return this.distance;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R3\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */