package com.francobm.magicosmetics.provider.husksync;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import net.william278.husksync.BukkitHuskSync;
import net.william278.husksync.adapter.Adaptable;
import net.william278.husksync.data.BukkitData;
import net.william278.husksync.user.BukkitUser;
import org.bukkit.entity.Player;

public class CosmeticData extends BukkitData implements Adaptable {
  private String cosmetics;
  
  private String cosmeticsInUse;
  
  private boolean isCosmeticsLoaded;
  
  public CosmeticData(String cosmetics, String cosmeticsInUse) {
    this.cosmetics = cosmetics;
    this.cosmeticsInUse = cosmeticsInUse;
  }
  
  private CosmeticData() {}
  
  public void apply(BukkitUser bukkitUser, BukkitHuskSync bukkitHuskSync) {
    if (this.isCosmeticsLoaded)
      return; 
    Player player = bukkitUser.getPlayer();
    MagicCosmetics.getInstance().getSql().loadPlayerAsync(player).thenAccept(playerData -> {
          playerData.forceClearCosmeticsInventory();
          playerData.loadCosmetics(getCosmetics(), getCosmeticsInUse());
          this.isCosmeticsLoaded = true;
        });
  }
  
  public String getCosmetics() {
    return this.cosmetics;
  }
  
  public String getCosmeticsInUse() {
    return this.cosmeticsInUse;
  }
  
  public void setCosmeticsInUse(String cosmeticsInUse) {
    this.cosmeticsInUse = cosmeticsInUse;
  }
  
  public void setCosmetics(String cosmetics) {
    this.cosmetics = cosmetics;
  }
  
  public void setCosmeticsLoaded(boolean cosmeticsLoaded) {
    this.isCosmeticsLoaded = cosmeticsLoaded;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\husksync\CosmeticData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */