package com.francobm.magicosmetics.events;

import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CosmeticUnEquipEvent extends PlayerEvent implements Cancellable {
  private final Cosmetic cosmetic;
  
  private final CosmeticType cosmeticType;
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private boolean isCancelled;
  
  public CosmeticUnEquipEvent(Player player, Cosmetic cosmetic, CosmeticType cosmeticType) {
    super(player);
    this.cosmetic = cosmetic;
    this.cosmeticType = cosmeticType;
    this.isCancelled = false;
  }
  
  public CosmeticUnEquipEvent(Player player, Cosmetic cosmetic) {
    super(player);
    this.cosmetic = cosmetic;
    this.cosmeticType = cosmetic.getCosmeticType();
    this.isCancelled = false;
  }
  
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public Cosmetic getCosmetic() {
    return this.cosmetic;
  }
  
  public CosmeticType getCosmeticType() {
    return this.cosmeticType;
  }
  
  public boolean isCancelled() {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean cancel) {
    this.isCancelled = cancel;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\CosmeticUnEquipEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */