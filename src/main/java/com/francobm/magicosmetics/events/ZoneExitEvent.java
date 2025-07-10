package com.francobm.magicosmetics.events;

import com.francobm.magicosmetics.cache.Zone;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ZoneExitEvent extends PlayerEvent implements Cancellable {
  private final Zone zone;
  
  private final Reason reason;
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private boolean isCancelled;
  
  public ZoneExitEvent(Player player, Zone zone, Reason reason) {
    super(player);
    this.zone = zone;
    this.reason = reason;
    this.isCancelled = false;
  }
  
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public Zone getZone() {
    return this.zone;
  }
  
  public Reason getReason() {
    return this.reason;
  }
  
  public boolean isCancelled() {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean cancel) {
    this.isCancelled = cancel;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\ZoneExitEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */