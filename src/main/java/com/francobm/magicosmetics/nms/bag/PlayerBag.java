package com.francobm.magicosmetics.nms.bag;

import com.francobm.magicosmetics.nms.IRangeManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerBag {
  protected ItemStack backPackItem;
  
  protected ItemStack backPackItemForMe;
  
  protected int lendEntityId = -1;
  
  protected UUID uuid;
  
  protected IRangeManager rangeManager;
  
  protected float height;
  
  protected List<Integer> ids;
  
  protected List<UUID> hideViewers;
  
  protected int backpackId;
  
  public abstract void spawn(Player paramPlayer);
  
  public abstract void spawnSelf(Player paramPlayer);
  
  public abstract void spawn(boolean paramBoolean);
  
  public abstract void remove();
  
  public abstract void remove(Player paramPlayer);
  
  public abstract void addPassenger(Player paramPlayer, int paramInt1, int paramInt2);
  
  public abstract void addPassenger(boolean paramBoolean);
  
  public abstract void setItemOnHelmet(Player paramPlayer, ItemStack paramItemStack);
  
  public abstract void lookEntity(float paramFloat1, float paramFloat2, boolean paramBoolean);
  
  public abstract Entity getEntity();
  
  public void setLendEntityId(int id) {
    this.lendEntityId = id;
  }
  
  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }
  
  public UUID getUuid() {
    return this.uuid;
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
  
  protected Set<Player> getPlayersInRange() {
    Set<Player> set = this.rangeManager.getPlayerInRange();
    set.add(getPlayer());
    return set;
  }
  
  public int getBackpackId() {
    return this.backpackId;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\bag\PlayerBag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */