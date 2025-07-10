package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.utils.OffsetModel;
import com.francobm.magicosmetics.utils.PositionModelType;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class ModelEngine {
  public abstract boolean existAnimation(String paramString1, String paramString2);
  
  public abstract void loopAnimation(UUID paramUUID, String paramString1, String paramString2);
  
  public abstract Object getModelEngineAPI();
  
  public abstract UUID spawnModel(Entity paramEntity, String paramString, Location paramLocation, OffsetModel paramOffsetModel);
  
  public abstract void spawnLeash(Entity paramEntity, UUID paramUUID, String paramString);
  
  public abstract Object spawnModelBackPack(Entity paramEntity, String paramString, Location paramLocation, OffsetModel paramOffsetModel, PositionModelType paramPositionModelType);
  
  public abstract void stopAnimations(UUID paramUUID, String paramString);
  
  public abstract void stopAnimationExcept(UUID paramUUID, String paramString1, String paramString2);
  
  public abstract boolean isPlayingAnimation(UUID paramUUID, String paramString1, String paramString2);
  
  public abstract void playAnimation(UUID paramUUID, String paramString1, String paramString2);
  
  public abstract void removeModeledEntity(UUID paramUUID, String paramString);
  
  public abstract void removeBackPack(UUID paramUUID, String paramString);
  
  public abstract Set<Player> getTrackedPlayers(UUID paramUUID);
  
  public abstract void hideModel(UUID paramUUID, Player paramPlayer);
  
  public abstract void showModel(UUID paramUUID, Player paramPlayer);
  
  public abstract Set<String> getAllBonesIds(UUID paramUUID, String paramString);
  
  public abstract void tint(UUID paramUUID, String paramString1, Color paramColor, String paramString2);
  
  public abstract void movementModel(UUID paramUUID, Location paramLocation);
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\ModelEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */