package com.francobm.magicosmetics.nms.balloon;

import com.francobm.magicosmetics.cache.RotationType;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerBalloon {
  public static Map<UUID, PlayerBalloon> playerBalloons = new ConcurrentHashMap<>();
  
  protected UUID uuid;
  
  protected List<UUID> viewers;
  
  protected List<UUID> hideViewers;
  
  protected LivingEntity lendEntity;
  
  protected boolean floatLoop = true;
  
  protected double y = 0.0D;
  
  protected double height = 0.0D;
  
  protected boolean heightLoop = true;
  
  protected float rotate = -0.4F;
  
  protected double rot = 0.0D;
  
  protected boolean rotateLoop = true;
  
  protected double space;
  
  protected boolean bigHead = false;
  
  protected boolean invisibleLeash;
  
  public static void updatePlayerBalloon(Player player) {
    for (PlayerBalloon playerBalloon : playerBalloons.values()) {
      playerBalloon.remove(player);
      playerBalloon.spawn(player);
    } 
  }
  
  public static void removePlayerBagByPlayer(Player player) {
    for (PlayerBalloon playerBalloon : playerBalloons.values()) {
      if (player.getUniqueId().equals(playerBalloon.uuid) || 
        !playerBalloon.viewers.contains(player.getUniqueId()))
        continue; 
      playerBalloon.remove(player);
    } 
  }
  
  public abstract void spawn(Player paramPlayer);
  
  public abstract void spawn(boolean paramBoolean);
  
  public abstract void remove();
  
  public abstract void remove(Player paramPlayer);
  
  public abstract void setItem(ItemStack paramItemStack);
  
  public abstract void lookEntity(float paramFloat1, float paramFloat2);
  
  protected abstract void teleport(Location paramLocation);
  
  protected abstract void instantUpdate();
  
  public abstract void update(boolean paramBoolean);
  
  public abstract void rotate(boolean paramBoolean, RotationType paramRotationType, float paramFloat);
  
  public void setLendEntity(LivingEntity lendEntity) {
    this.lendEntity = lendEntity;
  }
  
  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }
  
  public UUID getUuid() {
    return this.uuid;
  }
  
  public List<UUID> getViewers() {
    return this.viewers;
  }
  
  public boolean isBigHead() {
    return this.bigHead;
  }
  
  public List<UUID> getHideViewers() {
    return this.hideViewers;
  }
  
  public void addHideViewer(Player player) {
    if (this.hideViewers.contains(player.getUniqueId()))
      return; 
    this.hideViewers.add(player.getUniqueId());
    remove(player);
  }
  
  public void removeHideViewer(Player player) {
    this.hideViewers.remove(player.getUniqueId());
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\balloon\PlayerBalloon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */