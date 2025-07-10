package com.francobm.magicosmetics.events;

import com.francobm.magicosmetics.nms.NPC.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UnknownEntityInteractEvent extends Event implements Cancellable {
  private final Player player;
  
  private final NPC unknownEntity;
  
  private final Action action;
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private boolean isCancelled;
  
  public UnknownEntityInteractEvent(Player player, NPC unknownEntity, Action action) {
    this.player = player;
    this.unknownEntity = unknownEntity;
    this.action = action;
    this.isCancelled = false;
  }
  
  public UnknownEntityInteractEvent(boolean isAsync, Player player, NPC unknownEntity, Action action) {
    super(isAsync);
    this.player = player;
    this.unknownEntity = unknownEntity;
    this.action = action;
    this.isCancelled = false;
  }
  
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public Player getPlayer() {
    return this.player;
  }
  
  public NPC getUnknownEntity() {
    return this.unknownEntity;
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public boolean isCancelled() {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean cancel) {
    this.isCancelled = cancel;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\UnknownEntityInteractEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */