package com.francobm.magicosmetics.nms;

import java.util.Set;
import org.bukkit.entity.Player;

public interface IRangeManager {
  void addPlayer(Player paramPlayer);
  
  void removePlayer(Player paramPlayer);
  
  Set<Player> getPlayerInRange();
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\IRangeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */