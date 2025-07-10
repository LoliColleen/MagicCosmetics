/*    */ package com.francobm.magicosmetics.listeners;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import com.google.common.io.ByteArrayDataInput;
/*    */ import com.google.common.io.ByteStreams;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.messaging.PluginMessageListener;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class ProxyListener implements PluginMessageListener {
/* 14 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */ 
/*    */   
/*    */   public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
/* 18 */     if (!channel.equals("mc:player"))
/* 19 */       return;  ByteArrayDataInput in = ByteStreams.newDataInput(message);
/* 20 */     String subChannel = in.readUTF();
/* 21 */     if (subChannel.equals("load_cosmetics")) {
/* 22 */       String playerName = in.readUTF();
/* 23 */       String loadCosmetics = in.readUTF();
/* 24 */       String loadUseCosmetics = in.readUTF();
/* 25 */       String status = in.readUTF();
/* 26 */       Player p = Bukkit.getPlayer(playerName);
/* 27 */       if (p == null)
/* 28 */         return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)p);
/* 29 */       if (status.equals("0")) {
/*    */         return;
/*    */       }
/*    */ 
/*    */       
/* 34 */       playerData.loadCosmetics(loadCosmetics, loadUseCosmetics);
/* 35 */     } else if (subChannel.equals("ping")) {
/* 36 */       String playerName = in.readUTF();
/* 37 */       Player p = Bukkit.getPlayer(playerName);
/* 38 */       if (p == null)
/* 39 */         return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)p);
/* 40 */       playerData.sendLoadPlayerData();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\ProxyListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */