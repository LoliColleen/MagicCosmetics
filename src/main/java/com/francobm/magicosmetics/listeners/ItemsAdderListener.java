package com.francobm.magicosmetics.listeners;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Sound;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.cache.items.Items;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ItemsAdderListener implements Listener {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  @EventHandler
  public void onIALoadEvent(ItemsAdderLoadDataEvent event) {
    if (event.getCause() != ItemsAdderLoadDataEvent.Cause.FIRST_LOAD)
      return; 
    this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> {
          this.plugin.ava = this.plugin.getItemsAdder().replaceFontImages(this.plugin.ava);
          this.plugin.unAva = this.plugin.getItemsAdder().replaceFontImages(this.plugin.unAva);
          this.plugin.equip = this.plugin.getItemsAdder().replaceFontImages(this.plugin.equip);
          this.plugin.getBossBar().clear();
          for (String lines : this.plugin.getMessages().getStringList("bossbar")) {
            lines = this.plugin.getItemsAdder().replaceFontImages(lines);
            BossBar boss = this.plugin.getServer().createBossBar(lines, this.plugin.bossBarColor, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
            boss.setVisible(true);
            this.plugin.getBossBar().add(boss);
          } 
          Cosmetic.loadCosmetics();
          Color.loadColors();
          Items.loadItems();
          Zone.loadZones();
          Token.loadTokens();
          Sound.loadSounds();
          Menu.loadMenus();
        });
  }
  
  @EventHandler
  public void onPlaceBlocks(CustomBlockPlaceEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getWStick() == null)
      return; 
    if (!playerData.getWStick().isCosmetic(event.getItemInHand()))
      return; 
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onPlaceBlocks(CustomBlockInteractEvent event) {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\ItemsAdderListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */