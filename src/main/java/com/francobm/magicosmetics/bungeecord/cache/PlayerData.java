package com.francobm.magicosmetics.bungeecord.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerData {
  public static Map<UUID, PlayerData> players = new HashMap<>();
  
  private final UUID uniqueId;
  
  private final String name;
  
  private String cosmetics;
  
  private String cosmeticsInUse;
  
  private boolean firstJoin;
  
  public PlayerData(ProxiedPlayer player, String cosmetics, String cosmeticsInUse) {
    this.uniqueId = player.getUniqueId();
    this.name = player.getName();
    this.cosmetics = cosmetics;
    this.cosmeticsInUse = cosmeticsInUse;
    this.firstJoin = true;
  }
  
  public PlayerData(ProxiedPlayer player) {
    this(player, "", "");
  }
  
  public static void removePlayer(UUID uniqueId) {
    players.remove(uniqueId);
  }
  
  public static PlayerData getPlayer(ProxiedPlayer player) {
    if (!players.containsKey(player.getUniqueId())) {
      PlayerData playerData = new PlayerData(player);
      players.put(player.getUniqueId(), playerData);
      return playerData;
    } 
    return players.get(player.getUniqueId());
  }
  
  public String getCosmeticsInUse() {
    return this.cosmeticsInUse;
  }
  
  public String getCosmetics() {
    return this.cosmetics;
  }
  
  public void setCosmetics(String cosmetics) {
    this.cosmetics = cosmetics;
  }
  
  public void setCosmeticsInUse(String cosmeticsInUse) {
    this.cosmeticsInUse = cosmeticsInUse;
  }
  
  public void setFirstJoin(boolean firstJoin) {
    this.firstJoin = firstJoin;
  }
  
  public String getName() {
    return this.name;
  }
  
  public UUID getUniqueId() {
    return this.uniqueId;
  }
  
  public boolean isFirstJoin() {
    return this.firstJoin;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\bungeecord\cache\PlayerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */