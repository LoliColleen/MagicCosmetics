package com.francobm.magicosmetics.provider.husksync;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import java.util.Optional;
import net.william278.husksync.api.HuskSyncAPI;
import net.william278.husksync.data.Data;
import net.william278.husksync.data.DataSnapshot;
import net.william278.husksync.data.Identifier;
import net.william278.husksync.event.BukkitDataSaveEvent;
import net.william278.husksync.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HuskSync implements Listener {
  private static final Identifier COSMETICS_ID = Identifier.from("magicosmetics", "cosmetics");
  
  private final HuskSyncAPI huskSyncAPI;
  
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public HuskSync() {
    this.huskSyncAPI = HuskSyncAPI.getInstance();
    register();
  }
  
  private void register() {
    this.huskSyncAPI.registerDataSerializer(COSMETICS_ID, new CosmeticSerializer(this.huskSyncAPI));
  }
  
  public void loadDataToPlayer(Player player) {
    this.huskSyncAPI.getUser(player.getUniqueId()).thenAccept(optionalUser -> {
          if (!optionalUser.isPresent())
            return; 
          this.huskSyncAPI.getCurrentData(optionalUser.get()).thenAccept(());
        });
  }
  
  public void saveDataToPlayer(PlayerData playerData) {
    this.huskSyncAPI.getUser(playerData.getUniqueId()).thenAccept(optionalUser -> {
          if (!optionalUser.isPresent())
            return; 
          User user = optionalUser.get();
          this.huskSyncAPI.getCurrentData(user).thenAccept(());
        });
  }
  
  @EventHandler
  public void onDataSave(BukkitDataSaveEvent event) {
    event.editData(unpacked -> {
          PlayerData playerData = PlayerData.getPlayer(Bukkit.getOfflinePlayer(event.getUser().getUuid()));
          if (unpacked.getData(COSMETICS_ID).isPresent()) {
            CosmeticData cosmeticData = unpacked.getData(COSMETICS_ID).get();
            cosmeticData.setCosmetics(playerData.saveCosmetics());
            cosmeticData.setCosmeticsInUse(playerData.getCosmeticsInUse());
            cosmeticData.setCosmeticsLoaded((!event.getSaveCause().name().equals(DataSnapshot.SaveCause.DISCONNECT.name()) && !event.getSaveCause().name().equals(DataSnapshot.SaveCause.SERVER_SHUTDOWN.name())));
            return;
          } 
          unpacked.setData(COSMETICS_ID, (Data)new CosmeticData(playerData.saveCosmetics(), playerData.getCosmeticsInUse()));
        });
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\husksync\HuskSync.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */