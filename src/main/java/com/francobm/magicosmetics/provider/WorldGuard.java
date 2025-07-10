package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WorldGuard implements Listener {
  private final MagicCosmetics plugin;
  
  private StateFlag customFlag;
  
  public WorldGuard(MagicCosmetics plugin) {
    registerFlag();
    this.plugin = plugin;
  }
  
  public void registerFlag() {
    FlagRegistry registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
    try {
      StateFlag flag = new StateFlag("cosmetics", true);
      registry.register((Flag)flag);
      this.customFlag = flag;
    } catch (FlagConflictException e) {
      Flag<?> existing = registry.get("cosmetics");
      if (existing instanceof StateFlag)
        this.customFlag = (StateFlag)existing; 
    } 
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.isHasInBlackList())
      return; 
    Location to = event.getTo();
    if (to == null)
      return; 
    Location location = BukkitAdapter.adapt(to);
    ApplicableRegionSet applicableRegionSet = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(location);
    StateFlag.State flagState = applicableRegionSet.queryState(null, new StateFlag[] { this.customFlag });
    if (flagState == null || flagState.equals(StateFlag.State.DENY)) {
      playerData.hideAllCosmetics();
      return;
    } 
    playerData.showAllCosmetics();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\WorldGuard.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */