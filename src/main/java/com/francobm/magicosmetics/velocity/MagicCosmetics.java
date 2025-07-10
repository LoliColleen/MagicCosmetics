package com.francobm.magicosmetics.velocity;

import com.francobm.magicosmetics.velocity.cache.PlayerData;
import com.francobm.magicosmetics.velocity.listeners.PlayerListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;

@Plugin(id = "magiccosmetics", name = "MagicCosmetics", version = "1.0.0", authors = {"FrancoBM"})
public class MagicCosmetics {
  private final ProxyServer server;
  
  private final Logger logger;
  
  private Map<Player, PlayerData> cosmetics;
  
  public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("mc:player");
  
  @Inject
  public MagicCosmetics(ProxyServer server, Logger logger) {
    this.server = server;
    this.logger = logger;
  }
  
  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    this.logger.info("Hello Velocity!");
    registerChannels();
    this.cosmetics = new HashMap<>();
    registerListeners();
  }
  
  public void registerChannels() {
    this.server.getChannelRegistrar().register(new ChannelIdentifier[] { (ChannelIdentifier)IDENTIFIER });
  }
  
  public void registerListeners() {
    this.server.getEventManager().register(this, new PlayerListener(this));
  }
  
  public void sendQuitPlayerData(Player player) {
    if (player == null)
      return; 
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("quit");
    out.writeUTF(player.getUsername());
    player.getCurrentServer().map(serverConnection -> Boolean.valueOf(serverConnection.sendPluginMessage((ChannelIdentifier)IDENTIFIER, out.toByteArray())));
  }
  
  public void sendLoadPlayerData(Player player) {
    String cosmetics, cosmeticsInUse, status;
    PlayerData playerData = this.cosmetics.get(player);
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
    out.writeUTF(player.getUsername());
    out.writeUTF(cosmetics);
    out.writeUTF(cosmeticsInUse);
    out.writeUTF(status);
    if (player.getCurrentServer().isEmpty())
      return; 
    ServerConnection serverConnection = player.getCurrentServer().get();
    serverConnection.sendPluginMessage((ChannelIdentifier)IDENTIFIER, out.toByteArray());
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
      Optional<Player> optionalPlayer = getServer().getPlayer(playerName);
      if (optionalPlayer.isEmpty())
        return; 
      Player player = optionalPlayer.get();
      PlayerData playerData = PlayerData.getPlayer(player);
      playerData.setCosmetics(cosmetics);
      playerData.setCosmeticsInUse(cosmeticsInUse);
      playerData.setFirstJoin(false);
    } else if (subChannel.equals("load_cosmetics")) {
      String playerName = in.readUTF();
      Optional<Player> optionalPlayer = getServer().getPlayer(playerName);
      if (optionalPlayer.isEmpty())
        return; 
      Player player = optionalPlayer.get();
      sendLoadPlayerData(player);
    } 
  }
  
  public Logger getLogger() {
    return this.logger;
  }
  
  public ProxyServer getServer() {
    return this.server;
  }
  
  public Map<Player, PlayerData> getCosmetics() {
    return this.cosmetics;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\velocity\MagicCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */