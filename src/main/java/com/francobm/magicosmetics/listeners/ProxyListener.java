package com.francobm.magicosmetics.listeners;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class ProxyListener implements PluginMessageListener {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
    if (!channel.equals("mc:player"))
      return; 
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subChannel = in.readUTF();
    if (subChannel.equals("load_cosmetics")) {
      String playerName = in.readUTF();
      String loadCosmetics = in.readUTF();
      String loadUseCosmetics = in.readUTF();
      String status = in.readUTF();
      Player p = Bukkit.getPlayer(playerName);
      if (p == null)
        return; 
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)p);
      if (status.equals("0"))
        return; 
      playerData.loadCosmetics(loadCosmetics, loadUseCosmetics);
    } else if (subChannel.equals("ping")) {
      String playerName = in.readUTF();
      Player p = Bukkit.getPlayer(playerName);
      if (p == null)
        return; 
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)p);
      playerData.sendLoadPlayerData();
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\ProxyListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */