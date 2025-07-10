/*     */ package com.francobm.magicosmetics.cache.cosmetics.balloons;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.RotationType;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Balloon extends Cosmetic {
/*     */   private ArmorStand armorStand;
/*     */   private PufferFish leashed;
/*     */   private PlayerBalloon playerBalloon;
/*     */   private EntityBalloon entityBalloon;
/*     */   private double space;
/*     */   private boolean rotation;
/*     */   private RotationType rotationType;
/*     */   private BalloonEngine balloonEngine;
/*     */   private BalloonIA balloonIA;
/*     */   protected double distance;
/*     */   private double SQUARED_WALKING;
/*     */   private double SQUARED_DISTANCE;
/*     */   private boolean bigHead;
/*     */   private boolean invisibleLeash;
/*     */   private boolean instantFollow;
/*     */   private final double CATCH_UP_INCREMENTS = 0.27D;
/*     */   private double CATCH_UP_INCREMENTS_DISTANCE;
/*     */   
/*  36 */   public Balloon(String id, String name, ItemStack itemStack, int modelData, boolean colored, double space, CosmeticType cosmeticType, Color color, boolean rotation, RotationType rotationType, BalloonEngine balloonEngine, BalloonIA balloonIA, double distance, String permission, boolean texture, boolean bigHead, boolean hideMenu, boolean invisibleLeash, boolean useEmote, boolean instantFollow, NamespacedKey namespacedKey) { super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     this.CATCH_UP_INCREMENTS = 0.27D;
/* 392 */     this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D; this.space = space; this.rotation = rotation; this.rotationType = rotationType; this.distance = distance; this.balloonEngine = balloonEngine; this.balloonIA = balloonIA; this.SQUARED_WALKING = 5.5D * space; this.SQUARED_DISTANCE = 10.0D * space; this.bigHead = bigHead; this.invisibleLeash = invisibleLeash; this.instantFollow = instantFollow; }
/*     */   protected void updateCosmetic(Cosmetic cosmetic) { super.updateCosmetic(cosmetic); Balloon balloon = (Balloon)cosmetic; this.space = balloon.space; this.rotation = balloon.rotation; this.rotationType = balloon.rotationType; this.distance = balloon.distance; this.balloonEngine = balloon.balloonEngine; this.balloonIA = balloon.balloonIA; this.SQUARED_WALKING = 5.5D * this.space; this.SQUARED_DISTANCE = 10.0D * this.space; this.bigHead = balloon.bigHead; this.invisibleLeash = balloon.invisibleLeash; this.instantFollow = balloon.instantFollow; }
/*     */   public double getSpace() { return this.space; }
/* 395 */   public boolean isRotation() { return this.rotation; } public boolean isInstantFollow() { return this.instantFollow; } public RotationType getRotationType() { return this.rotationType; } public BalloonEngine getBalloonEngine() { return this.balloonEngine; } public void instantUpdate(Entity entity) { if (this.balloonIA != null) {
/* 396 */       Vector standDir; Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 397 */       Location stand = this.balloonIA.getModelLocation();
/*     */       
/* 399 */       if (entity instanceof Player) {
/* 400 */         standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
/*     */       } else {
/* 402 */         standDir = entity.getLocation().clone().subtract(stand).toVector();
/*     */       } 
/* 404 */       Location distance2 = stand.clone();
/* 405 */       Location distance1 = entity.getLocation().clone();
/* 406 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 407 */         Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 408 */         if (!standDir.equals(new Vector())) {
/* 409 */           standDir.normalize();
/*     */         }
/* 411 */         Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 412 */         Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 413 */         Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
/* 414 */         this.balloonIA.getCustomEntity().teleport(normal.clone());
/* 415 */         this.balloonIA.updateTeleport(this.leashed);
/*     */       } else {
/*     */         
/* 418 */         if (!standDir.equals(new Vector())) {
/* 419 */           standDir.normalize();
/*     */         }
/* 421 */         Location standToLoc = stand.clone().setDirection(standDir.setY(0));
/* 422 */         Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
/* 423 */         this.balloonIA.getCustomEntity().teleport(normal.clone());
/* 424 */         this.balloonIA.updateTeleport(this.leashed);
/*     */       } 
/*     */       
/* 427 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 428 */         this.balloonIA.setState(1);
/*     */       } else {
/* 430 */         this.balloonIA.setState(0);
/*     */       } 
/* 432 */       if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 433 */         this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */       } else {
/* 435 */         this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */       } 
/*     */       return;
/*     */     } 
/* 439 */     if (this.balloonEngine != null && this.armorStand != null)
/* 440 */     { Vector standDir; Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 441 */       Location stand = this.armorStand.getLocation();
/*     */       
/* 443 */       if (entity instanceof Player) {
/* 444 */         standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
/*     */       } else {
/* 446 */         standDir = entity.getLocation().clone().subtract(stand).toVector();
/*     */       } 
/*     */       
/* 449 */       if (!standDir.equals(new Vector())) {
/* 450 */         standDir.normalize();
/*     */       }
/* 452 */       Location standToLoc = playerLoc.setDirection(standDir.setY(0));
/* 453 */       standToLoc = standToLoc.setDirection(standToLoc.getDirection().multiply(0.01D));
/* 454 */       this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, standToLoc);
/* 455 */       this.balloonEngine.setStatePlayOn(0); }  }
/*     */   public void active(Entity entity) { if (entity == null) { remove(); return; }  if (this.balloonIA != null) { if (this.invisibleLeash) { if (this.balloonIA.getCustomEntity() == null) { if (entity.isDead()) return;  remove(); this.balloonIA.spawn(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1))); this.balloonIA.detectPlayers(this.leashed, entity); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  this.balloonIA.detectPlayers(this.leashed, entity); update(entity); return; }  if (this.balloonIA.getCustomEntity() == null) { if (entity.isDead()) return;  remove(); this.balloonIA.spawn(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1))); this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation()); this.balloonIA.detectPlayers(this.leashed, entity); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  this.balloonIA.detectPlayers(this.leashed, entity); update(entity); return; }  if (this.balloonEngine != null) { if (this.invisibleLeash) { if (this.balloonEngine.getBalloonUniqueId() == null) { if (entity.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1))); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update(entity); return; }  if (this.balloonEngine.getBalloonUniqueId() == null) { if (entity.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1))); this.balloonEngine.spawnLeash(entity); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update(entity); return; }  if (this.entityBalloon == null) { if (entity.isDead()) return;  remove(); this.entityBalloon = MagicCosmetics.getInstance().getVersion().createEntityBalloon(entity, this.space, this.distance, this.bigHead, this.invisibleLeash); this.entityBalloon.spawn(false); }  if (entity instanceof Player) { this.entityBalloon.setItem(getItemColor((Player)entity)); } else { this.entityBalloon.setItem(getItemColor()); }  this.entityBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation); this.entityBalloon.update(); this.entityBalloon.spawn(true); }
/*     */   public void lendToEntity() { if (this.balloonIA != null) { if (this.invisibleLeash) { if (this.balloonIA.getCustomEntity() == null) { if (this.lendEntity.isDead()) return;  remove(); this.balloonIA.spawn(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1))); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  update((Entity)this.player); return; }  if (this.balloonIA.getCustomEntity() == null) { if (this.lendEntity.isDead()) return;  remove(); this.balloonIA.spawn(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1))); this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation()); this.balloonIA.detectPlayers(this.leashed, (Entity)this.player); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  this.balloonIA.detectPlayers(this.leashed, (Entity)this.player); update((Entity)this.player); return; }  if (this.balloonEngine != null) { if (this.invisibleLeash) { if (this.balloonEngine.getBalloonUniqueId() == null) { if (this.lendEntity.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1))); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update((Entity)this.player); return; }  if (this.balloonEngine.getBalloonUniqueId() == null) { if (this.lendEntity.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1))); this.balloonEngine.spawnLeash((Entity)this.lendEntity); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update((Entity)this.player); return; }  if (this.playerBalloon == null) { if (this.lendEntity.isDead()) return;  remove(); this.playerBalloon = MagicCosmetics.getInstance().getVersion().createPlayerBalloon(this.player, this.space, this.distance, this.bigHead, this.invisibleLeash); this.playerBalloon.spawn(false); }  this.playerBalloon.setItem(getItemColor(this.player)); this.playerBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation); this.playerBalloon.update(this.instantFollow); this.playerBalloon.spawn(true); }
/*     */   public void hide(Player player) { if (this.balloonIA != null) return;  if (this.balloonEngine != null) { this.balloonEngine.hideModel(player); return; }  if (this.playerBalloon != null) this.playerBalloon.addHideViewer(player);  }
/*     */   public void show(Player player) { if (this.balloonIA != null) return;  if (this.balloonEngine != null) { this.balloonEngine.showModel(player); return; }  if (this.playerBalloon != null) this.playerBalloon.removeHideViewer(player);  }
/* 460 */   public void update() { if (isHideCosmetic()) { remove(); return; }  if ((!this.removedLendEntity && this.player.isInvisible()) || (!this.removedLendEntity && this.player.isGliding()) || (!this.removedLendEntity && this.player.hasPotionEffect(PotionEffectType.INVISIBILITY))) { remove(); return; }  if (this.player == null) return;  if (this.balloonIA != null) { if (this.invisibleLeash) { if (this.balloonIA.getCustomEntity() == null) { if (this.player.isDead()) return;  if (this.player.getGameMode() == GameMode.SPECTATOR) return;  remove(); this.balloonIA.spawn(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1))); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  update((Entity)this.player); return; }  if (this.balloonIA.getCustomEntity() == null) { if (this.player.isDead() || !this.player.isValid()) return;  if (this.player.getGameMode() == GameMode.SPECTATOR) return;  remove(); this.balloonIA.spawn(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1))); this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation()); this.balloonIA.detectPlayers(this.leashed, (Entity)this.player); if (isColored()) this.balloonIA.paintBalloon(getColor());  }  this.balloonIA.detectPlayers(this.leashed, (Entity)this.player); update((Entity)this.player); return; }  if (this.balloonEngine != null) { if (this.invisibleLeash) { if (this.balloonEngine.getBalloonUniqueId() == null) { if (this.player.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1))); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update((Entity)this.player); return; }  if (this.balloonEngine.getBalloonUniqueId() == null) { if (this.player.isDead()) return;  remove(); this.armorStand = this.balloonEngine.spawnModel(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1))); this.balloonEngine.spawnLeash((Entity)this.player); if (isColored()) this.balloonEngine.tintModel(getColor());  }  update((Entity)this.player); return; }  if (this.playerBalloon == null) { if (this.player.isDead()) return;  if (this.player.getGameMode() == GameMode.SPECTATOR) return;  remove(); this.playerBalloon = MagicCosmetics.getInstance().getVersion().createPlayerBalloon(this.player, this.space, this.distance, this.bigHead, this.invisibleLeash); this.playerBalloon.spawn(false); }  if (this.removedLendEntity && !this.player.isInvisible()) this.removedLendEntity = false;  this.playerBalloon.setItem(getItemColor(this.player)); this.playerBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation); this.playerBalloon.update(this.instantFollow); this.playerBalloon.spawn(true); } public void remove() { if (this.balloonEngine != null) this.balloonEngine.remove((LivingEntity)this.armorStand);  if (this.balloonIA != null) this.balloonIA.remove(this.leashed);  if (this.armorStand != null) this.armorStand = null;  if (this.leashed != null) this.leashed = null;  if (this.playerBalloon != null) { this.playerBalloon.remove(); this.playerBalloon = null; }  if (this.entityBalloon != null) { this.entityBalloon.remove(); this.entityBalloon = null; }  } public void clearClose() { if (this.balloonEngine != null) this.balloonEngine.remove((LivingEntity)this.armorStand);  if (this.balloonIA != null) this.balloonIA.remove(this.leashed);  if (this.armorStand != null) this.armorStand = null;  if (this.leashed != null) this.leashed = null;  if (this.playerBalloon != null) { this.playerBalloon.remove(); this.playerBalloon = null; }  if (this.entityBalloon != null) { this.entityBalloon.remove(); this.entityBalloon = null; }  } public void update(Entity entity) { if (this.instantFollow) {
/* 461 */       instantUpdate(entity);
/*     */       return;
/*     */     } 
/* 464 */     if (this.balloonIA != null) {
/* 465 */       Vector standDir; Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 466 */       Location stand = this.balloonIA.getModelLocation();
/*     */       
/* 468 */       if (entity instanceof Player) {
/* 469 */         standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
/*     */       } else {
/* 471 */         standDir = entity.getLocation().clone().subtract(stand).toVector();
/*     */       } 
/* 473 */       Location distance2 = stand.clone();
/* 474 */       Location distance1 = entity.getLocation().clone();
/* 475 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 476 */         Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 477 */         if (!standDir.equals(new Vector())) {
/* 478 */           standDir.normalize();
/*     */         }
/* 480 */         Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 481 */         Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 482 */         Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
/* 483 */         this.balloonIA.getCustomEntity().teleport(normal);
/* 484 */         this.balloonIA.updateTeleport(this.leashed);
/*     */       } else {
/*     */         
/* 487 */         Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 488 */         if (!standDir.equals(new Vector())) {
/* 489 */           standDir.normalize();
/*     */         }
/* 491 */         Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 492 */         double y = distVec.getY();
/* 493 */         if (entity instanceof Player && ((Player)entity).isSneaking()) {
/* 494 */           y -= 0.13D;
/*     */         }
/* 496 */         Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, y, 0.0D);
/* 497 */         Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
/* 498 */         this.balloonIA.getCustomEntity().teleport(normal);
/* 499 */         this.balloonIA.updateTeleport(this.leashed);
/*     */       } 
/*     */       
/* 502 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 503 */         this.balloonIA.setState(1);
/*     */       } else {
/* 505 */         this.balloonIA.setState(0);
/*     */       } 
/* 507 */       if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 508 */         this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */       } else {
/* 510 */         this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */       } 
/*     */       return;
/*     */     } 
/* 514 */     if (this.balloonEngine != null && this.armorStand != null) {
/* 515 */       Vector standDir; Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
/* 516 */       Location stand = this.armorStand.getLocation();
/*     */       
/* 518 */       if (entity instanceof Player) {
/* 519 */         standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
/*     */       } else {
/* 521 */         standDir = entity.getLocation().clone().subtract(stand).toVector();
/*     */       } 
/* 523 */       Location distance2 = stand.clone();
/* 524 */       Location distance1 = entity.getLocation().clone();
/* 525 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 526 */         Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 527 */         if (!standDir.equals(new Vector())) {
/* 528 */           standDir.normalize();
/*     */         }
/* 530 */         Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 531 */         Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
/* 532 */         Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
/* 533 */         this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, normal);
/*     */       } else {
/* 535 */         Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
/* 536 */         if (!standDir.equals(new Vector())) {
/* 537 */           standDir.normalize();
/*     */         }
/* 539 */         Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
/* 540 */         double y = distVec.getY();
/* 541 */         if (entity instanceof Player && ((Player)entity).isSneaking()) {
/* 542 */           y -= 0.13D;
/*     */         }
/* 544 */         Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, y, 0.0D);
/* 545 */         Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
/* 546 */         this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, normal);
/*     */       } 
/*     */       
/* 549 */       if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
/* 550 */         this.balloonEngine.setState(1);
/*     */       } else {
/* 552 */         this.balloonEngine.setState(0);
/*     */       } 
/* 554 */       if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
/* 555 */         this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
/*     */       } else {
/* 557 */         this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
/*     */       } 
/*     */     }  }
/*     */ 
/*     */   
/*     */   public double getDistance() {
/* 563 */     return this.distance;
/*     */   }
/*     */   
/*     */   public BalloonIA getBalloonIA() {
/* 567 */     return this.balloonIA;
/*     */   }
/*     */   
/*     */   public boolean isBigHead() {
/* 571 */     return this.bigHead;
/*     */   }
/*     */   
/*     */   public boolean isInvisibleLeash() {
/* 575 */     return this.invisibleLeash;
/*     */   }
/*     */   
/*     */   public void setLeashedHolder(Entity entity) {
/* 579 */     if (this.leashed == null || !this.leashed.isValid() || this.leashed.isDead())
/* 580 */       return;  this.leashed.setLeashHolder(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLendEntity(LivingEntity lendEntity) {
/* 585 */     super.setLendEntity(lendEntity);
/* 586 */     if (this.playerBalloon == null)
/* 587 */       return;  this.playerBalloon.setLendEntity(lendEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(Player player) {
/* 592 */     if (this.playerBalloon == null)
/* 593 */       return;  this.playerBalloon.spawn(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawn(Player player) {
/* 598 */     if (this.playerBalloon == null)
/* 599 */       return;  this.playerBalloon.remove(player);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\Balloon.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */