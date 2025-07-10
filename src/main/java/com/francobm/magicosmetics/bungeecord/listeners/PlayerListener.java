package com.francobm.magicosmetics.bungeecord.listeners;

import com.francobm.magicosmetics.bungeecord.MagicCosmetics;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
  private final MagicCosmetics plugin;
  
  public PlayerListener(MagicCosmetics plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPluginMessage(PluginMessageEvent event) {
    this.plugin.executePluginMessage(event.getTag(), event.getData());
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\bungeecord\listeners\PlayerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */