package com.francobm.magicosmetics.nms.spray;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public abstract class CustomSpray {
  public static Map<UUID, CustomSpray> customSprays = new ConcurrentHashMap<>();
  
  protected UUID uuid;
  
  protected List<UUID> players;
  
  protected ItemFrame entity;
  
  protected boolean preview;
  
  public static void updateSpray(Player player) {
    for (CustomSpray spray : customSprays.values()) {
      spray.remove(player);
      spray.spawn(player);
    } 
  }
  
  public static void removeSpray(Player player) {
    for (CustomSpray spray : customSprays.values()) {
      if (!spray.players.contains(player.getUniqueId()))
        continue; 
      spray.remove(player);
    } 
  }
  
  public abstract void spawn(Player paramPlayer);
  
  public abstract void spawn(boolean paramBoolean);
  
  public abstract void remove();
  
  public abstract void remove(Player paramPlayer);
  
  public ItemFrame getEntity() {
    return this.entity;
  }
  
  public UUID getUuid() {
    return this.uuid;
  }
  
  public List<UUID> getPlayers() {
    return this.players;
  }
  
  public void setPreview(boolean preview) {
    this.preview = preview;
  }
  
  public boolean isPreview() {
    return this.preview;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\spray\CustomSpray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */