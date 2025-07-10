/*     */ package com.francobm.magicosmetics.cache.cosmetics.balloons;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.utils.OffsetModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.ArmorStand;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class BalloonEngine {
/*     */   private UUID balloonUniqueId;
/*     */   private final String modelId;
/*     */   private final List<String> colorParts;
/*     */   private final String walk_animation;
/*     */   
/*     */   public BalloonEngine(String modelId, List<String> colorParts, String walk_animation, String idle_animation, double distance, OffsetModel offsetModel) {
/*  22 */     this.modelId = modelId;
/*  23 */     this.colorParts = colorParts;
/*  24 */     this.walk_animation = (walk_animation == null) ? "walk" : walk_animation;
/*  25 */     this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
/*  26 */     this.distance = distance;
/*  27 */     this.offsetModel = offsetModel;
/*     */   }
/*     */   private final String idle_animation; private final double distance; private final OffsetModel offsetModel; private boolean playOn;
/*     */   public BalloonEngine getClone() {
/*  31 */     return new BalloonEngine(this.modelId, new ArrayList<>(this.colorParts), this.walk_animation, this.idle_animation, this.distance, this.offsetModel);
/*     */   }
/*     */   
/*     */   public void setStatePlayOn(int state) {
/*  35 */     if (this.playOn)
/*  36 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  37 */     switch (state) {
/*     */       case 0:
/*  39 */         if (!plugin.getModelEngine().existAnimation(this.modelId, this.idle_animation))
/*  40 */           return;  if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.idle_animation)) {
/*  41 */           plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.idle_animation);
/*     */           break;
/*     */         } 
/*  44 */         plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
/*  45 */         plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
/*  46 */         plugin.getModelEngine().loopAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
/*     */         break;
/*     */       case 1:
/*  49 */         if (!MagicCosmetics.getInstance().getModelEngine().existAnimation(this.modelId, this.walk_animation))
/*  50 */           return;  if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.walk_animation)) {
/*  51 */           plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.walk_animation);
/*     */           break;
/*     */         } 
/*  54 */         plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
/*  55 */         plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
/*  56 */         plugin.getModelEngine().loopAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
/*     */         break;
/*     */     } 
/*  59 */     this.playOn = true;
/*     */   }
/*     */   
/*     */   public void setState(int state) {
/*  63 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  64 */     switch (state) {
/*     */       case 0:
/*  66 */         if (!plugin.getModelEngine().existAnimation(this.modelId, this.idle_animation))
/*     */           return; 
/*  68 */         if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.idle_animation)) {
/*  69 */           plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.idle_animation);
/*     */           return;
/*     */         } 
/*  72 */         plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
/*  73 */         plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
/*     */         return;
/*     */       case 1:
/*  76 */         if (!MagicCosmetics.getInstance().getModelEngine().existAnimation(this.modelId, this.walk_animation))
/*  77 */           return;  if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.walk_animation)) {
/*  78 */           plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.walk_animation);
/*     */           return;
/*     */         } 
/*  81 */         plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
/*  82 */         plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(LivingEntity pufferFish) {
/*  88 */     if (this.balloonUniqueId == null)
/*  89 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  90 */     plugin.getModelEngine().removeModeledEntity(this.balloonUniqueId, this.modelId);
/*  91 */     this.balloonUniqueId = null;
/*     */   }
/*     */   
/*     */   public Set<String> getBones() {
/*  95 */     if (this.balloonUniqueId == null) return null; 
/*  96 */     return MagicCosmetics.getInstance().getModelEngine().getAllBonesIds(this.balloonUniqueId, this.modelId);
/*     */   }
/*     */   
/*     */   public ArmorStand spawnModel(Location location) {
/* 100 */     ArmorStand armorStand = MagicCosmetics.getInstance().getVersion().spawnArmorStand(location);
/* 101 */     this.balloonUniqueId = MagicCosmetics.getInstance().getModelEngine().spawnModel((Entity)armorStand, this.modelId, location, this.offsetModel);
/* 102 */     return armorStand;
/*     */   }
/*     */   
/*     */   public void updateTeleport(LivingEntity leashed, Location location) {
/* 106 */     if (this.balloonUniqueId == null)
/* 107 */       return;  MagicCosmetics.getInstance().getVersion().updatePositionFakeEntity((Entity)leashed, location);
/* 108 */     Set<Player> players = MagicCosmetics.getInstance().getModelEngine().getTrackedPlayers(this.balloonUniqueId);
/* 109 */     MagicCosmetics.getInstance().getVersion().teleportFakeEntity((Entity)leashed, players);
/* 110 */     MagicCosmetics.getInstance().getModelEngine().movementModel(this.balloonUniqueId, location);
/*     */   }
/*     */   
/*     */   public void tintModel(Color color) {
/* 114 */     if (color == null)
/* 115 */       return;  if (this.balloonUniqueId == null)
/* 116 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 117 */     for (String id : getBones()) {
/* 118 */       if (getColorParts() != null && !getColorParts().isEmpty() && 
/* 119 */         !getColorParts().contains(id))
/*     */         continue; 
/* 121 */       plugin.getModelEngine().tint(this.balloonUniqueId, this.modelId, color, id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showModel(Player player) {
/* 126 */     MagicCosmetics.getInstance().getModelEngine().showModel(this.balloonUniqueId, player);
/*     */   }
/*     */   
/*     */   public void hideModel(Player player) {
/* 130 */     MagicCosmetics.getInstance().getModelEngine().hideModel(this.balloonUniqueId, player);
/*     */   }
/*     */   
/*     */   public UUID getBalloonUniqueId() {
/* 134 */     return this.balloonUniqueId;
/*     */   }
/*     */   
/*     */   public String getModelId() {
/* 138 */     return this.modelId;
/*     */   }
/*     */   
/*     */   public List<String> getColorParts() {
/* 142 */     return this.colorParts;
/*     */   }
/*     */   
/*     */   public void spawnLeash(Entity entity) {
/* 146 */     if (this.balloonUniqueId == null)
/* 147 */       return;  MagicCosmetics.getInstance().getModelEngine().spawnLeash(entity, this.balloonUniqueId, this.modelId);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\BalloonEngine.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */