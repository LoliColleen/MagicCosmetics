package com.francobm.magicosmetics.cache.cosmetics.balloons;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.utils.OffsetModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BalloonEngine {
  private UUID balloonUniqueId;
  
  private final String modelId;
  
  private final List<String> colorParts;
  
  private final String walk_animation;
  
  private final String idle_animation;
  
  private final double distance;
  
  private final OffsetModel offsetModel;
  
  private boolean playOn;
  
  public BalloonEngine(String modelId, List<String> colorParts, String walk_animation, String idle_animation, double distance, OffsetModel offsetModel) {
    this.modelId = modelId;
    this.colorParts = colorParts;
    this.walk_animation = (walk_animation == null) ? "walk" : walk_animation;
    this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
    this.distance = distance;
    this.offsetModel = offsetModel;
  }
  
  public BalloonEngine getClone() {
    return new BalloonEngine(this.modelId, new ArrayList<>(this.colorParts), this.walk_animation, this.idle_animation, this.distance, this.offsetModel);
  }
  
  public void setStatePlayOn(int state) {
    if (this.playOn)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    switch (state) {
      case 0:
        if (!plugin.getModelEngine().existAnimation(this.modelId, this.idle_animation))
          return; 
        if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.idle_animation)) {
          plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.idle_animation);
          break;
        } 
        plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
        plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
        plugin.getModelEngine().loopAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
        break;
      case 1:
        if (!MagicCosmetics.getInstance().getModelEngine().existAnimation(this.modelId, this.walk_animation))
          return; 
        if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.walk_animation)) {
          plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.walk_animation);
          break;
        } 
        plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
        plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
        plugin.getModelEngine().loopAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
        break;
    } 
    this.playOn = true;
  }
  
  public void setState(int state) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    switch (state) {
      case 0:
        if (!plugin.getModelEngine().existAnimation(this.modelId, this.idle_animation))
          return; 
        if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.idle_animation)) {
          plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.idle_animation);
          return;
        } 
        plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
        plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.idle_animation);
        return;
      case 1:
        if (!MagicCosmetics.getInstance().getModelEngine().existAnimation(this.modelId, this.walk_animation))
          return; 
        if (plugin.getModelEngine().isPlayingAnimation(this.balloonUniqueId, this.modelId, this.walk_animation)) {
          plugin.getModelEngine().stopAnimationExcept(this.balloonUniqueId, this.modelId, this.walk_animation);
          return;
        } 
        plugin.getModelEngine().stopAnimations(this.balloonUniqueId, this.modelId);
        plugin.getModelEngine().playAnimation(this.balloonUniqueId, this.modelId, this.walk_animation);
        break;
    } 
  }
  
  public void remove(LivingEntity pufferFish) {
    if (this.balloonUniqueId == null)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    plugin.getModelEngine().removeModeledEntity(this.balloonUniqueId, this.modelId);
    this.balloonUniqueId = null;
  }
  
  public Set<String> getBones() {
    if (this.balloonUniqueId == null)
      return null; 
    return MagicCosmetics.getInstance().getModelEngine().getAllBonesIds(this.balloonUniqueId, this.modelId);
  }
  
  public ArmorStand spawnModel(Location location) {
    ArmorStand armorStand = MagicCosmetics.getInstance().getVersion().spawnArmorStand(location);
    this.balloonUniqueId = MagicCosmetics.getInstance().getModelEngine().spawnModel((Entity)armorStand, this.modelId, location, this.offsetModel);
    return armorStand;
  }
  
  public void updateTeleport(LivingEntity leashed, Location location) {
    if (this.balloonUniqueId == null)
      return; 
    MagicCosmetics.getInstance().getVersion().updatePositionFakeEntity((Entity)leashed, location);
    Set<Player> players = MagicCosmetics.getInstance().getModelEngine().getTrackedPlayers(this.balloonUniqueId);
    MagicCosmetics.getInstance().getVersion().teleportFakeEntity((Entity)leashed, players);
    MagicCosmetics.getInstance().getModelEngine().movementModel(this.balloonUniqueId, location);
  }
  
  public void tintModel(Color color) {
    if (color == null)
      return; 
    if (this.balloonUniqueId == null)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    for (String id : getBones()) {
      if (getColorParts() != null && !getColorParts().isEmpty() && 
        !getColorParts().contains(id))
        continue; 
      plugin.getModelEngine().tint(this.balloonUniqueId, this.modelId, color, id);
    } 
  }
  
  public void showModel(Player player) {
    MagicCosmetics.getInstance().getModelEngine().showModel(this.balloonUniqueId, player);
  }
  
  public void hideModel(Player player) {
    MagicCosmetics.getInstance().getModelEngine().hideModel(this.balloonUniqueId, player);
  }
  
  public UUID getBalloonUniqueId() {
    return this.balloonUniqueId;
  }
  
  public String getModelId() {
    return this.modelId;
  }
  
  public List<String> getColorParts() {
    return this.colorParts;
  }
  
  public void spawnLeash(Entity entity) {
    if (this.balloonUniqueId == null)
      return; 
    MagicCosmetics.getInstance().getModelEngine().spawnLeash(entity, this.balloonUniqueId, this.modelId);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\BalloonEngine.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */