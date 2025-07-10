package com.francobm.magicosmetics.events;

import com.francobm.magicosmetics.api.SprayKeys;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SprayDrawingEvent extends PlayerEvent implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private final SprayKeys key;
  
  private final Block sprayedBlock;
  
  private boolean isCancelled;
  
  public SprayDrawingEvent(Player player, Block sprayedBlock, SprayKeys key) {
    super(player);
    this.key = key;
    this.sprayedBlock = sprayedBlock;
    this.isCancelled = false;
  }
  
  public SprayKeys getKey() {
    return this.key;
  }
  
  public Block getSprayedBlock() {
    return this.sprayedBlock;
  }
  
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public boolean isCancelled() {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean cancel) {
    this.isCancelled = cancel;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\SprayDrawingEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */