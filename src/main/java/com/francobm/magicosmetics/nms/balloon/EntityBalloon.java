package com.francobm.magicosmetics.nms.balloon;

import com.francobm.magicosmetics.cache.RotationType;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class EntityBalloon {
  public static Map<UUID, EntityBalloon> entitiesBalloon = new ConcurrentHashMap<>();
  
  protected UUID uuid;
  
  protected LivingEntity entity;
  
  protected List<UUID> players;
  
  protected boolean floatLoop = true;
  
  protected double y = 0.0D;
  
  protected double height = 0.0D;
  
  protected boolean heightLoop = true;
  
  protected float rotate = -0.4F;
  
  protected double rot = 0.0D;
  
  protected boolean rotateLoop = true;
  
  protected double space;
  
  protected boolean bigHead;
  
  protected boolean invisibleLeash;
  
  public static void updateEntityBalloon(Player player) {
    for (EntityBalloon entityBalloon : entitiesBalloon.values()) {
      entityBalloon.remove(player);
      entityBalloon.spawn(player);
    } 
  }
  
  public static void removeEntityBalloon(Player player) {
    for (EntityBalloon entityBalloon : entitiesBalloon.values()) {
      if (!entityBalloon.players.contains(player.getUniqueId()))
        continue; 
      entityBalloon.remove(player);
    } 
  }
  
  public abstract void spawn(Player paramPlayer);
  
  public abstract void spawn(boolean paramBoolean);
  
  public abstract void remove();
  
  public abstract void remove(Player paramPlayer);
  
  public abstract void setItem(ItemStack paramItemStack);
  
  public abstract void lookEntity();
  
  public abstract void update();
  
  public abstract void rotate(boolean paramBoolean, RotationType paramRotationType, float paramFloat);
  
  public LivingEntity getEntity() {
    return this.entity;
  }
  
  public UUID getUuid() {
    return this.uuid;
  }
  
  public List<UUID> getPlayers() {
    return this.players;
  }
  
  public boolean isBigHead() {
    return this.bigHead;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\balloon\EntityBalloon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */