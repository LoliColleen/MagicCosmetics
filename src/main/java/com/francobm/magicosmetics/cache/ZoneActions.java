package com.francobm.magicosmetics.cache;

import com.francobm.magicosmetics.listeners.ZoneListener;

public class ZoneActions {
  private boolean enabled;
  
  private final ZoneAction onEnter;
  
  private final ZoneAction onExit;
  
  private final ZoneListener zoneListener;
  
  public ZoneActions(ZoneAction onEnter, ZoneAction onExit) {
    this.onEnter = onEnter;
    this.onExit = onExit;
    this.enabled = false;
    this.zoneListener = new ZoneListener();
  }
  
  public ZoneAction getOnEnter() {
    return this.onEnter;
  }
  
  public ZoneAction getOnExit() {
    return this.onExit;
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public ZoneListener getZoneListener() {
    return this.zoneListener;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\ZoneActions.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */