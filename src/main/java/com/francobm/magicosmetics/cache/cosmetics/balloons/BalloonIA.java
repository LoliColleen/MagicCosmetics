/*     */ package com.francobm.magicosmetics.cache.cosmetics.balloons;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import dev.lone.itemsadder.api.CustomEntity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.PufferFish;
/*     */ 
/*     */ public class BalloonIA {
/*     */   private CustomEntity customEntity;
/*     */   private final String modelId;
/*     */   private final List<String> colorParts;
/*     */   private final List<UUID> players;
/*     */   
/*     */   public BalloonIA(String modelId, List<String> colorParts, String walk_animation, String idle_animation, double distance) {
/*  26 */     this.modelId = modelId;
/*  27 */     this.colorParts = colorParts;
/*  28 */     this.walk_animation = (walk_animation == null) ? "walk" : walk_animation;
/*  29 */     this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
/*  30 */     this.players = new ArrayList<>();
/*  31 */     this.distance = distance;
/*     */   }
/*     */   private final String walk_animation; private final String idle_animation; private final double distance; private CustomEntity.Bone leashBone;
/*     */   public BalloonIA getClone() {
/*  35 */     return new BalloonIA(this.modelId, new ArrayList<>(this.colorParts), this.walk_animation, this.idle_animation, this.distance);
/*     */   }
/*     */   
/*     */   public void spawn(Location location) {
/*  39 */     if (this.customEntity != null) {
/*  40 */       this.customEntity.getEntity().remove();
/*     */     }
/*  42 */     this.customEntity = CustomEntity.spawn(this.modelId, location, false, true, true);
/*  43 */     for (CustomEntity.Bone bone : this.customEntity.getBones()) {
/*  44 */       if (bone.getName().startsWith("l_")) {
/*  45 */         this.leashBone = bone;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paintBalloon(Color color) {
/*  52 */     if (this.colorParts.isEmpty()) {
/*  53 */       this.customEntity.setColorAllBones(color.asRGB());
/*     */       return;
/*     */     } 
/*  56 */     for (CustomEntity.Bone bone : this.customEntity.getBones()) {
/*  57 */       if (!this.colorParts.contains(bone.getName()))
/*  58 */         continue;  bone.setColor(color.asRGB());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setState(int state) {
/*  63 */     switch (state) {
/*     */       case 0:
/*  65 */         if (!getCustomEntity().hasAnimation(this.idle_animation))
/*  66 */           return;  if (getCustomEntity().isPlayingAnimation(this.idle_animation)) {
/*  67 */           getCustomEntity().stopAnimation();
/*     */           return;
/*     */         } 
/*  70 */         getCustomEntity().playAnimation(this.idle_animation);
/*     */         break;
/*     */       case 1:
/*  73 */         if (!getCustomEntity().hasAnimation(this.walk_animation))
/*  74 */           return;  if (getCustomEntity().isPlayingAnimation(this.walk_animation)) {
/*  75 */           getCustomEntity().stopAnimation();
/*     */           return;
/*     */         } 
/*  78 */         getCustomEntity().playAnimation(this.walk_animation);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(PufferFish pufferFish) {
/*  84 */     if (this.customEntity != null) {
/*  85 */       for (Player player : Bukkit.getOnlinePlayers()) {
/*  86 */         removePlayer(pufferFish, player);
/*     */       }
/*  88 */       this.customEntity.destroy();
/*  89 */       this.customEntity = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void detectPlayers(PufferFish pufferFish, Entity owner) {
/*  94 */     if (this.customEntity == null)
/*  95 */       return;  if (pufferFish == null)
/*  96 */       return;  for (Player player : Bukkit.getOnlinePlayers()) {
/*  97 */       if (this.players.contains(player.getUniqueId())) {
/*  98 */         if (!owner.getWorld().equals(player.getWorld())) {
/*  99 */           removePlayer(pufferFish, player);
/*     */           continue;
/*     */         } 
/* 102 */         if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) {
/* 103 */           removePlayer(pufferFish, player);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 107 */       if (!owner.getWorld().equals(player.getWorld()) || 
/* 108 */         owner.getLocation().distanceSquared(player.getLocation()) > this.distance)
/* 109 */         continue;  addPlayer(pufferFish, owner, player);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addPlayer(PufferFish pufferFish, Entity owner, Player player) {
/* 114 */     if (pufferFish == null)
/* 115 */       return;  if (this.customEntity == null)
/* 116 */       return;  if (this.players.contains(player.getUniqueId()))
/* 117 */       return;  this.players.add(player.getUniqueId());
/* 118 */     MagicCosmetics.getInstance().getVersion().showEntity((LivingEntity)pufferFish, new Player[] { player });
/* 119 */     MagicCosmetics.getInstance().getVersion().attachFakeEntity(owner, (Entity)pufferFish, new Player[] { player });
/*     */   }
/*     */   
/*     */   public void removePlayer(PufferFish pufferFish, Player player) {
/* 123 */     if (pufferFish == null)
/* 124 */       return;  if (this.customEntity == null)
/* 125 */       return;  this.players.remove(player.getUniqueId());
/* 126 */     MagicCosmetics.getInstance().getVersion().despawnFakeEntity((Entity)pufferFish, new Player[] { player });
/*     */   }
/*     */   
/*     */   public String getModelId() {
/* 130 */     return this.modelId;
/*     */   }
/*     */   
/*     */   public CustomEntity getCustomEntity() {
/* 134 */     return this.customEntity;
/*     */   }
/*     */   
/*     */   public PufferFish spawnLeash(Location location) {
/* 138 */     return MagicCosmetics.getInstance().getVersion().spawnFakePuffer(location);
/*     */   }
/*     */   
/*     */   public CustomEntity.Bone getLeashBone() {
/* 142 */     return this.leashBone;
/*     */   }
/*     */   
/*     */   public Location getModelLocation() {
/* 146 */     if (this.customEntity == null) return null; 
/* 147 */     return this.customEntity.getLocation();
/*     */   }
/*     */   
/*     */   public void updateTeleport(PufferFish leashed) {
/* 151 */     if (leashed == null)
/* 152 */       return;  MagicCosmetics.getInstance().getVersion().updatePositionFakeEntity((Entity)leashed, this.leashBone.getLocation().add(0.0D, 1.6D, 0.0D));
/* 153 */     Set<Player> players = (Set<Player>)this.players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet());
/* 154 */     MagicCosmetics.getInstance().getVersion().teleportFakeEntity((Entity)leashed, players);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\BalloonIA.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */