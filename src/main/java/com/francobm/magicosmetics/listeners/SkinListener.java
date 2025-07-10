/*    */ package com.francobm.magicosmetics.listeners;
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import java.util.Objects;
/*    */ import net.skinsrestorer.api.event.SkinApplyEvent;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class SkinListener {
/* 10 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   
/*    */   public SkinListener() {
/* 13 */     SkinsRestorerProvider.get().getEventBus().subscribe(this.plugin, SkinApplyEvent.class, event -> {
/*    */           Player player = (Player)event.getPlayer(Player.class);
/*    */           PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*    */           Objects.requireNonNull(playerData);
/*    */           this.plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)this.plugin, playerData::clearBag, 20L);
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\SkinListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */