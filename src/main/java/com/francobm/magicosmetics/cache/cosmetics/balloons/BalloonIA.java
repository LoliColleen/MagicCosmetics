package com.francobm.magicosmetics.cache.cosmetics.balloons;

import com.francobm.magicosmetics.MagicCosmetics;
import dev.lone.itemsadder.api.CustomEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;

public class BalloonIA {
  private CustomEntity customEntity;
  
  private final String modelId;
  
  private final List<String> colorParts;
  
  private final List<UUID> players;
  
  private final String walk_animation;
  
  private final String idle_animation;
  
  private final double distance;
  
  private CustomEntity.Bone leashBone;
  
  public BalloonIA(String modelId, List<String> colorParts, String walk_animation, String idle_animation, double distance) {
    this.modelId = modelId;
    this.colorParts = colorParts;
    this.walk_animation = (walk_animation == null) ? "walk" : walk_animation;
    this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
    this.players = new ArrayList<>();
    this.distance = distance;
  }
  
  public BalloonIA getClone() {
    return new BalloonIA(this.modelId, new ArrayList<>(this.colorParts), this.walk_animation, this.idle_animation, this.distance);
  }
  
  public void spawn(Location location) {
    if (this.customEntity != null)
      this.customEntity.getEntity().remove(); 
    this.customEntity = CustomEntity.spawn(this.modelId, location, false, true, true);
    for (CustomEntity.Bone bone : this.customEntity.getBones()) {
      if (bone.getName().startsWith("l_")) {
        this.leashBone = bone;
        break;
      } 
    } 
  }
  
  public void paintBalloon(Color color) {
    if (this.colorParts.isEmpty()) {
      this.customEntity.setColorAllBones(color.asRGB());
      return;
    } 
    for (CustomEntity.Bone bone : this.customEntity.getBones()) {
      if (!this.colorParts.contains(bone.getName()))
        continue; 
      bone.setColor(color.asRGB());
    } 
  }
  
  public void setState(int state) {
    switch (state) {
      case 0:
        if (!getCustomEntity().hasAnimation(this.idle_animation))
          return; 
        if (getCustomEntity().isPlayingAnimation(this.idle_animation)) {
          getCustomEntity().stopAnimation();
          return;
        } 
        getCustomEntity().playAnimation(this.idle_animation);
        break;
      case 1:
        if (!getCustomEntity().hasAnimation(this.walk_animation))
          return; 
        if (getCustomEntity().isPlayingAnimation(this.walk_animation)) {
          getCustomEntity().stopAnimation();
          return;
        } 
        getCustomEntity().playAnimation(this.walk_animation);
        break;
    } 
  }
  
  public void remove(PufferFish pufferFish) {
    if (this.customEntity != null) {
      for (Player player : Bukkit.getOnlinePlayers())
        removePlayer(pufferFish, player); 
      this.customEntity.destroy();
      this.customEntity = null;
    } 
  }
  
  public void detectPlayers(PufferFish pufferFish, Entity owner) {
    if (this.customEntity == null)
      return; 
    if (pufferFish == null)
      return; 
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (this.players.contains(player.getUniqueId())) {
        if (!owner.getWorld().equals(player.getWorld())) {
          removePlayer(pufferFish, player);
          continue;
        } 
        if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance) {
          removePlayer(pufferFish, player);
          continue;
        } 
      } 
      if (!owner.getWorld().equals(player.getWorld()) || 
        owner.getLocation().distanceSquared(player.getLocation()) > this.distance)
        continue; 
      addPlayer(pufferFish, owner, player);
    } 
  }
  
  private void addPlayer(PufferFish pufferFish, Entity owner, Player player) {
    if (pufferFish == null)
      return; 
    if (this.customEntity == null)
      return; 
    if (this.players.contains(player.getUniqueId()))
      return; 
    this.players.add(player.getUniqueId());
    MagicCosmetics.getInstance().getVersion().showEntity((LivingEntity)pufferFish, new Player[] { player });
    MagicCosmetics.getInstance().getVersion().attachFakeEntity(owner, (Entity)pufferFish, new Player[] { player });
  }
  
  public void removePlayer(PufferFish pufferFish, Player player) {
    if (pufferFish == null)
      return; 
    if (this.customEntity == null)
      return; 
    this.players.remove(player.getUniqueId());
    MagicCosmetics.getInstance().getVersion().despawnFakeEntity((Entity)pufferFish, new Player[] { player });
  }
  
  public String getModelId() {
    return this.modelId;
  }
  
  public CustomEntity getCustomEntity() {
    return this.customEntity;
  }
  
  public PufferFish spawnLeash(Location location) {
    return MagicCosmetics.getInstance().getVersion().spawnFakePuffer(location);
  }
  
  public CustomEntity.Bone getLeashBone() {
    return this.leashBone;
  }
  
  public Location getModelLocation() {
    if (this.customEntity == null)
      return null; 
    return this.customEntity.getLocation();
  }
  
  public void updateTeleport(PufferFish leashed) {
    if (leashed == null)
      return; 
    MagicCosmetics.getInstance().getVersion().updatePositionFakeEntity((Entity)leashed, this.leashBone.getLocation().add(0.0D, 1.6D, 0.0D));
    Set<Player> players = (Set<Player>)this.players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet());
    MagicCosmetics.getInstance().getVersion().teleportFakeEntity((Entity)leashed, players);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\BalloonIA.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */