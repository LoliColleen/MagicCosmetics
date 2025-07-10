package com.francobm.magicosmetics.cache.cosmetics.balloons;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.RotationType;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Balloon extends Cosmetic {
  private ArmorStand armorStand;
  
  private PufferFish leashed;
  
  private PlayerBalloon playerBalloon;
  
  private EntityBalloon entityBalloon;
  
  private double space;
  
  private boolean rotation;
  
  private RotationType rotationType;
  
  private BalloonEngine balloonEngine;
  
  private BalloonIA balloonIA;
  
  protected double distance;
  
  private double SQUARED_WALKING;
  
  private double SQUARED_DISTANCE;
  
  private boolean bigHead;
  
  private boolean invisibleLeash;
  
  private boolean instantFollow;
  
  private final double CATCH_UP_INCREMENTS = 0.27D;
  
  private double CATCH_UP_INCREMENTS_DISTANCE;
  
  public Balloon(String id, String name, ItemStack itemStack, int modelData, boolean colored, double space, CosmeticType cosmeticType, Color color, boolean rotation, RotationType rotationType, BalloonEngine balloonEngine, BalloonIA balloonIA, double distance, String permission, boolean texture, boolean bigHead, boolean hideMenu, boolean invisibleLeash, boolean useEmote, boolean instantFollow, NamespacedKey namespacedKey) {
    super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
    this.CATCH_UP_INCREMENTS = 0.27D;
    this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
    this.space = space;
    this.rotation = rotation;
    this.rotationType = rotationType;
    this.distance = distance;
    this.balloonEngine = balloonEngine;
    this.balloonIA = balloonIA;
    this.SQUARED_WALKING = 5.5D * space;
    this.SQUARED_DISTANCE = 10.0D * space;
    this.bigHead = bigHead;
    this.invisibleLeash = invisibleLeash;
    this.instantFollow = instantFollow;
  }
  
  protected void updateCosmetic(Cosmetic cosmetic) {
    super.updateCosmetic(cosmetic);
    Balloon balloon = (Balloon)cosmetic;
    this.space = balloon.space;
    this.rotation = balloon.rotation;
    this.rotationType = balloon.rotationType;
    this.distance = balloon.distance;
    this.balloonEngine = balloon.balloonEngine;
    this.balloonIA = balloon.balloonIA;
    this.SQUARED_WALKING = 5.5D * this.space;
    this.SQUARED_DISTANCE = 10.0D * this.space;
    this.bigHead = balloon.bigHead;
    this.invisibleLeash = balloon.invisibleLeash;
    this.instantFollow = balloon.instantFollow;
  }
  
  public double getSpace() {
    return this.space;
  }
  
  public boolean isRotation() {
    return this.rotation;
  }
  
  public boolean isInstantFollow() {
    return this.instantFollow;
  }
  
  public RotationType getRotationType() {
    return this.rotationType;
  }
  
  public BalloonEngine getBalloonEngine() {
    return this.balloonEngine;
  }
  
  public void active(Entity entity) {
    if (entity == null) {
      remove();
      return;
    } 
    if (this.balloonIA != null) {
      if (this.invisibleLeash) {
        if (this.balloonIA.getCustomEntity() == null) {
          if (entity.isDead())
            return; 
          remove();
          this.balloonIA.spawn(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1)));
          this.balloonIA.detectPlayers(this.leashed, entity);
          if (isColored())
            this.balloonIA.paintBalloon(getColor()); 
        } 
        this.balloonIA.detectPlayers(this.leashed, entity);
        update(entity);
        return;
      } 
      if (this.balloonIA.getCustomEntity() == null) {
        if (entity.isDead())
          return; 
        remove();
        this.balloonIA.spawn(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation());
        this.balloonIA.detectPlayers(this.leashed, entity);
        if (isColored())
          this.balloonIA.paintBalloon(getColor()); 
      } 
      this.balloonIA.detectPlayers(this.leashed, entity);
      update(entity);
      return;
    } 
    if (this.balloonEngine != null) {
      if (this.invisibleLeash) {
        if (this.balloonEngine.getBalloonUniqueId() == null) {
          if (entity.isDead())
            return; 
          remove();
          this.armorStand = this.balloonEngine.spawnModel(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1)));
          if (isColored())
            this.balloonEngine.tintModel(getColor()); 
        } 
        update(entity);
        return;
      } 
      if (this.balloonEngine.getBalloonUniqueId() == null) {
        if (entity.isDead())
          return; 
        remove();
        this.armorStand = this.balloonEngine.spawnModel(entity.getLocation().clone().add(0.0D, this.space, 0.0D).add(entity.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.balloonEngine.spawnLeash(entity);
        if (isColored())
          this.balloonEngine.tintModel(getColor()); 
      } 
      update(entity);
      return;
    } 
    if (this.entityBalloon == null) {
      if (entity.isDead())
        return; 
      remove();
      this.entityBalloon = MagicCosmetics.getInstance().getVersion().createEntityBalloon(entity, this.space, this.distance, this.bigHead, this.invisibleLeash);
      this.entityBalloon.spawn(false);
    } 
    if (entity instanceof Player) {
      this.entityBalloon.setItem(getItemColor((Player)entity));
    } else {
      this.entityBalloon.setItem(getItemColor());
    } 
    this.entityBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation);
    this.entityBalloon.update();
    this.entityBalloon.spawn(true);
  }
  
  public void lendToEntity() {
    if (this.balloonIA != null) {
      if (this.invisibleLeash) {
        if (this.balloonIA.getCustomEntity() == null) {
          if (this.lendEntity.isDead())
            return; 
          remove();
          this.balloonIA.spawn(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1)));
          if (isColored())
            this.balloonIA.paintBalloon(getColor()); 
        } 
        update((Entity)this.player);
        return;
      } 
      if (this.balloonIA.getCustomEntity() == null) {
        if (this.lendEntity.isDead())
          return; 
        remove();
        this.balloonIA.spawn(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation());
        this.balloonIA.detectPlayers(this.leashed, (Entity)this.player);
        if (isColored())
          this.balloonIA.paintBalloon(getColor()); 
      } 
      this.balloonIA.detectPlayers(this.leashed, (Entity)this.player);
      update((Entity)this.player);
      return;
    } 
    if (this.balloonEngine != null) {
      if (this.invisibleLeash) {
        if (this.balloonEngine.getBalloonUniqueId() == null) {
          if (this.lendEntity.isDead())
            return; 
          remove();
          this.armorStand = this.balloonEngine.spawnModel(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1)));
          if (isColored())
            this.balloonEngine.tintModel(getColor()); 
        } 
        update((Entity)this.player);
        return;
      } 
      if (this.balloonEngine.getBalloonUniqueId() == null) {
        if (this.lendEntity.isDead())
          return; 
        remove();
        this.armorStand = this.balloonEngine.spawnModel(this.lendEntity.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.lendEntity.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.balloonEngine.spawnLeash((Entity)this.lendEntity);
        if (isColored())
          this.balloonEngine.tintModel(getColor()); 
      } 
      update((Entity)this.player);
      return;
    } 
    if (this.playerBalloon == null) {
      if (this.lendEntity.isDead())
        return; 
      remove();
      this.playerBalloon = MagicCosmetics.getInstance().getVersion().createPlayerBalloon(this.player, this.space, this.distance, this.bigHead, this.invisibleLeash);
      this.playerBalloon.spawn(false);
    } 
    this.playerBalloon.setItem(getItemColor(this.player));
    this.playerBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation);
    this.playerBalloon.update(this.instantFollow);
    this.playerBalloon.spawn(true);
  }
  
  public void hide(Player player) {
    if (this.balloonIA != null)
      return; 
    if (this.balloonEngine != null) {
      this.balloonEngine.hideModel(player);
      return;
    } 
    if (this.playerBalloon != null)
      this.playerBalloon.addHideViewer(player); 
  }
  
  public void show(Player player) {
    if (this.balloonIA != null)
      return; 
    if (this.balloonEngine != null) {
      this.balloonEngine.showModel(player);
      return;
    } 
    if (this.playerBalloon != null)
      this.playerBalloon.removeHideViewer(player); 
  }
  
  public void update() {
    if (isHideCosmetic()) {
      remove();
      return;
    } 
    if ((!this.removedLendEntity && this.player.isInvisible()) || (!this.removedLendEntity && this.player.isGliding()) || (!this.removedLendEntity && this.player.hasPotionEffect(PotionEffectType.INVISIBILITY))) {
      remove();
      return;
    } 
    if (this.player == null)
      return; 
    if (this.balloonIA != null) {
      if (this.invisibleLeash) {
        if (this.balloonIA.getCustomEntity() == null) {
          if (this.player.isDead())
            return; 
          if (this.player.getGameMode() == GameMode.SPECTATOR)
            return; 
          remove();
          this.balloonIA.spawn(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1)));
          if (isColored())
            this.balloonIA.paintBalloon(getColor()); 
        } 
        update((Entity)this.player);
        return;
      } 
      if (this.balloonIA.getCustomEntity() == null) {
        if (this.player.isDead() || !this.player.isValid())
          return; 
        if (this.player.getGameMode() == GameMode.SPECTATOR)
          return; 
        remove();
        this.balloonIA.spawn(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.leashed = this.balloonIA.spawnLeash(this.balloonIA.getLeashBone().getLocation());
        this.balloonIA.detectPlayers(this.leashed, (Entity)this.player);
        if (isColored())
          this.balloonIA.paintBalloon(getColor()); 
      } 
      this.balloonIA.detectPlayers(this.leashed, (Entity)this.player);
      update((Entity)this.player);
      return;
    } 
    if (this.balloonEngine != null) {
      if (this.invisibleLeash) {
        if (this.balloonEngine.getBalloonUniqueId() == null) {
          if (this.player.isDead())
            return; 
          remove();
          this.armorStand = this.balloonEngine.spawnModel(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1)));
          if (isColored())
            this.balloonEngine.tintModel(getColor()); 
        } 
        update((Entity)this.player);
        return;
      } 
      if (this.balloonEngine.getBalloonUniqueId() == null) {
        if (this.player.isDead())
          return; 
        remove();
        this.armorStand = this.balloonEngine.spawnModel(this.player.getLocation().clone().add(0.0D, this.space, 0.0D).add(this.player.getLocation().clone().getDirection().normalize().multiply(-1)));
        this.balloonEngine.spawnLeash((Entity)this.player);
        if (isColored())
          this.balloonEngine.tintModel(getColor()); 
      } 
      update((Entity)this.player);
      return;
    } 
    if (this.playerBalloon == null) {
      if (this.player.isDead())
        return; 
      if (this.player.getGameMode() == GameMode.SPECTATOR)
        return; 
      remove();
      this.playerBalloon = MagicCosmetics.getInstance().getVersion().createPlayerBalloon(this.player, this.space, this.distance, this.bigHead, this.invisibleLeash);
      this.playerBalloon.spawn(false);
    } 
    if (this.removedLendEntity && !this.player.isInvisible())
      this.removedLendEntity = false; 
    this.playerBalloon.setItem(getItemColor(this.player));
    this.playerBalloon.rotate(this.rotation, this.rotationType, (float)(MagicCosmetics.getInstance()).balloonRotation);
    this.playerBalloon.update(this.instantFollow);
    this.playerBalloon.spawn(true);
  }
  
  public void remove() {
    if (this.balloonEngine != null)
      this.balloonEngine.remove((LivingEntity)this.armorStand); 
    if (this.balloonIA != null)
      this.balloonIA.remove(this.leashed); 
    if (this.armorStand != null)
      this.armorStand = null; 
    if (this.leashed != null)
      this.leashed = null; 
    if (this.playerBalloon != null) {
      this.playerBalloon.remove();
      this.playerBalloon = null;
    } 
    if (this.entityBalloon != null) {
      this.entityBalloon.remove();
      this.entityBalloon = null;
    } 
  }
  
  public void clearClose() {
    if (this.balloonEngine != null)
      this.balloonEngine.remove((LivingEntity)this.armorStand); 
    if (this.balloonIA != null)
      this.balloonIA.remove(this.leashed); 
    if (this.armorStand != null)
      this.armorStand = null; 
    if (this.leashed != null)
      this.leashed = null; 
    if (this.playerBalloon != null) {
      this.playerBalloon.remove();
      this.playerBalloon = null;
    } 
    if (this.entityBalloon != null) {
      this.entityBalloon.remove();
      this.entityBalloon = null;
    } 
  }
  
  public void instantUpdate(Entity entity) {
    if (this.balloonIA != null) {
      Vector standDir;
      Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
      Location stand = this.balloonIA.getModelLocation();
      if (entity instanceof Player) {
        standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
      } else {
        standDir = entity.getLocation().clone().subtract(stand).toVector();
      } 
      Location distance2 = stand.clone();
      Location distance1 = entity.getLocation().clone();
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
        Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
        Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
        this.balloonIA.getCustomEntity().teleport(normal.clone());
        this.balloonIA.updateTeleport(this.leashed);
      } else {
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Location standToLoc = stand.clone().setDirection(standDir.setY(0));
        Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
        this.balloonIA.getCustomEntity().teleport(normal.clone());
        this.balloonIA.updateTeleport(this.leashed);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        this.balloonIA.setState(1);
      } else {
        this.balloonIA.setState(0);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
        this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
      } else {
        this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
      } 
      return;
    } 
    if (this.balloonEngine != null && this.armorStand != null) {
      Vector standDir;
      Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
      Location stand = this.armorStand.getLocation();
      if (entity instanceof Player) {
        standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
      } else {
        standDir = entity.getLocation().clone().subtract(stand).toVector();
      } 
      if (!standDir.equals(new Vector()))
        standDir.normalize(); 
      Location standToLoc = playerLoc.setDirection(standDir.setY(0));
      standToLoc = standToLoc.setDirection(standToLoc.getDirection().multiply(0.01D));
      this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, standToLoc);
      this.balloonEngine.setStatePlayOn(0);
    } 
  }
  
  public void update(Entity entity) {
    if (this.instantFollow) {
      instantUpdate(entity);
      return;
    } 
    if (this.balloonIA != null) {
      Vector standDir;
      Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
      Location stand = this.balloonIA.getModelLocation();
      if (entity instanceof Player) {
        standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
      } else {
        standDir = entity.getLocation().clone().subtract(stand).toVector();
      } 
      Location distance2 = stand.clone();
      Location distance1 = entity.getLocation().clone();
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
        Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
        Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
        this.balloonIA.getCustomEntity().teleport(normal);
        this.balloonIA.updateTeleport(this.leashed);
      } else {
        Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
        double y = distVec.getY();
        if (entity instanceof Player && ((Player)entity).isSneaking())
          y -= 0.13D; 
        Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, y, 0.0D);
        Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
        this.balloonIA.getCustomEntity().teleport(normal);
        this.balloonIA.updateTeleport(this.leashed);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        this.balloonIA.setState(1);
      } else {
        this.balloonIA.setState(0);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
        this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
      } else {
        this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
      } 
      return;
    } 
    if (this.balloonEngine != null && this.armorStand != null) {
      Vector standDir;
      Location playerLoc = entity.getLocation().clone().add(0.0D, this.space, 0.0D);
      Location stand = this.armorStand.getLocation();
      if (entity instanceof Player) {
        standDir = ((Player)entity).getEyeLocation().clone().subtract(stand).toVector();
      } else {
        standDir = entity.getLocation().clone().subtract(stand).toVector();
      } 
      Location distance2 = stand.clone();
      Location distance1 = entity.getLocation().clone();
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
        Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
        Location normal = standTo.clone().setDirection(standTo.getDirection().multiply(0.01D));
        this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, normal);
      } else {
        Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
        if (!standDir.equals(new Vector()))
          standDir.normalize(); 
        Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
        double y = distVec.getY();
        if (entity instanceof Player && ((Player)entity).isSneaking())
          y -= 0.13D; 
        Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, y, 0.0D);
        Location normal = standToLoc.clone().setDirection(standToLoc.getDirection().multiply(0.01D));
        this.balloonEngine.updateTeleport((LivingEntity)this.armorStand, normal);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
        this.balloonEngine.setState(1);
      } else {
        this.balloonEngine.setState(0);
      } 
      if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
        this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
      } else {
        this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
      } 
    } 
  }
  
  public double getDistance() {
    return this.distance;
  }
  
  public BalloonIA getBalloonIA() {
    return this.balloonIA;
  }
  
  public boolean isBigHead() {
    return this.bigHead;
  }
  
  public boolean isInvisibleLeash() {
    return this.invisibleLeash;
  }
  
  public void setLeashedHolder(Entity entity) {
    if (this.leashed == null || !this.leashed.isValid() || this.leashed.isDead())
      return; 
    this.leashed.setLeashHolder(entity);
  }
  
  public void setLendEntity(LivingEntity lendEntity) {
    super.setLendEntity(lendEntity);
    if (this.playerBalloon == null)
      return; 
    this.playerBalloon.setLendEntity(lendEntity);
  }
  
  public void spawn(Player player) {
    if (this.playerBalloon == null)
      return; 
    this.playerBalloon.spawn(player);
  }
  
  public void despawn(Player player) {
    if (this.playerBalloon == null)
      return; 
    this.playerBalloon.remove(player);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\Balloon.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */