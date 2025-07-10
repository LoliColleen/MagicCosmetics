package com.francobm.magicosmetics.provider;

import com.francobm.magicgestures.api.data.PlayerData;
import org.bukkit.entity.Player;

public class MagicGestures {
  public boolean hasInWardrobe(Player player) {
    PlayerData playerData = com.francobm.magicgestures.MagicGestures.getInstance().getPlayerDataLoader().getPlayerData(player);
    if (playerData == null)
      return false; 
    return playerData.isInWardrobe();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\MagicGestures.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */