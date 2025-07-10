package com.francobm.magicosmetics.bungeecord;

import com.francobm.magicosmetics.bungeecord.cache.PlayerData;
import com.francobm.magicosmetics.bungeecord.listeners.PlayerListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class MagicCosmetics extends Plugin {
  public void onEnable() {
    getLogger().info("Hello Bungeecord!");
    registerChannels();
    registerListeners();
  }
  
  public void registerChannels() {
    getProxy().registerChannel("mc:player");
  }
  
  public void registerListeners() {
    getProxy().getPluginManager().registerListener(this, (Listener)new PlayerListener(this));
  }
  
  public void unregisterChannels() {
    getProxy().unregisterChannel("mc:player");
  }
  
  public void unregisterListeners() {
    getProxy().getPluginManager().unregisterListeners(this);
  }
  
  public void sendPingPlayer(ProxiedPlayer player) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("ping");
    out.writeUTF(player.getName());
    player.getServer().getInfo().sendData("mc:player", out.toByteArray());
  }
  
  public void sendLoadPlayerData(ProxiedPlayer player) {
    String cosmetics, cosmeticsInUse, status;
    PlayerData playerData = PlayerData.getPlayer(player);
    if (playerData.isFirstJoin()) {
      cosmetics = "";
      cosmeticsInUse = "";
      status = "0";
    } else {
      cosmetics = playerData.getCosmetics();
      cosmeticsInUse = playerData.getCosmeticsInUse();
      status = "1";
    } 
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("load_cosmetics");
    out.writeUTF(player.getName());
    out.writeUTF(cosmetics);
    out.writeUTF(cosmeticsInUse);
    out.writeUTF(status);
    player.getServer().getInfo().sendData("mc:player", out.toByteArray());
  }
  
  public void executePluginMessage(String tag, byte[] data) {
    if (!tag.equals("mc:player"))
      return; 
    ByteArrayDataInput in = ByteStreams.newDataInput(data);
    String subChannel = in.readUTF();
    if (subChannel.equals("save_cosmetics")) {
      String playerName = in.readUTF();
      String cosmetics = in.readUTF();
      String cosmeticsInUse = in.readUTF();
      PlayerData playerData = PlayerData.getPlayer(getProxy().getPlayer(playerName));
      playerData.setCosmetics(cosmetics);
      playerData.setCosmeticsInUse(cosmeticsInUse);
      playerData.setFirstJoin(false);
    } else if (subChannel.equals("load_cosmetics")) {
      String playerName = in.readUTF();
      ProxiedPlayer player = getProxy().getPlayer(playerName);
      sendLoadPlayerData(player);
    } 
  }
  
  public void onDisable() {
    unregisterChannels();
    unregisterListeners();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\bungeecord\MagicCosmetics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */