/*     */ package com.francobm.magicosmetics.bungeecord;
/*     */ 
/*     */ import com.francobm.magicosmetics.bungeecord.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.bungeecord.listeners.PlayerListener;
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import com.google.common.io.ByteArrayDataOutput;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import net.md_5.bungee.api.connection.ProxiedPlayer;
/*     */ import net.md_5.bungee.api.plugin.Listener;
/*     */ import net.md_5.bungee.api.plugin.Plugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MagicCosmetics
/*     */   extends Plugin
/*     */ {
/*     */   public void onEnable() {
/*  21 */     getLogger().info("Hello Bungeecord!");
/*  22 */     registerChannels();
/*  23 */     registerListeners();
/*     */   }
/*     */   
/*     */   public void registerChannels() {
/*  27 */     getProxy().registerChannel("mc:player");
/*     */   }
/*     */   
/*     */   public void registerListeners() {
/*  31 */     getProxy().getPluginManager().registerListener(this, (Listener)new PlayerListener(this));
/*     */   }
/*     */   
/*     */   public void unregisterChannels() {
/*  35 */     getProxy().unregisterChannel("mc:player");
/*     */   }
/*     */   
/*     */   public void unregisterListeners() {
/*  39 */     getProxy().getPluginManager().unregisterListeners(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPingPlayer(ProxiedPlayer player) {
/*  44 */     ByteArrayDataOutput out = ByteStreams.newDataOutput();
/*  45 */     out.writeUTF("ping");
/*  46 */     out.writeUTF(player.getName());
/*  47 */     player.getServer().getInfo().sendData("mc:player", out.toByteArray());
/*     */   }
/*     */   
/*     */   public void sendLoadPlayerData(ProxiedPlayer player) {
/*     */     String cosmetics, cosmeticsInUse, status;
/*  52 */     PlayerData playerData = PlayerData.getPlayer(player);
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (playerData.isFirstJoin()) {
/*  57 */       cosmetics = "";
/*  58 */       cosmeticsInUse = "";
/*  59 */       status = "0";
/*     */     } else {
/*  61 */       cosmetics = playerData.getCosmetics();
/*  62 */       cosmeticsInUse = playerData.getCosmeticsInUse();
/*  63 */       status = "1";
/*     */     } 
/*  65 */     ByteArrayDataOutput out = ByteStreams.newDataOutput();
/*  66 */     out.writeUTF("load_cosmetics");
/*  67 */     out.writeUTF(player.getName());
/*  68 */     out.writeUTF(cosmetics);
/*  69 */     out.writeUTF(cosmeticsInUse);
/*  70 */     out.writeUTF(status);
/*  71 */     player.getServer().getInfo().sendData("mc:player", out.toByteArray());
/*     */   }
/*     */   
/*     */   public void executePluginMessage(String tag, byte[] data) {
/*  75 */     if (!tag.equals("mc:player"))
/*     */       return; 
/*  77 */     ByteArrayDataInput in = ByteStreams.newDataInput(data);
/*  78 */     String subChannel = in.readUTF();
/*     */     
/*  80 */     if (subChannel.equals("save_cosmetics")) {
/*  81 */       String playerName = in.readUTF();
/*  82 */       String cosmetics = in.readUTF();
/*  83 */       String cosmeticsInUse = in.readUTF();
/*  84 */       PlayerData playerData = PlayerData.getPlayer(getProxy().getPlayer(playerName));
/*  85 */       playerData.setCosmetics(cosmetics);
/*  86 */       playerData.setCosmeticsInUse(cosmeticsInUse);
/*  87 */       playerData.setFirstJoin(false);
/*     */     }
/*  89 */     else if (subChannel.equals("load_cosmetics")) {
/*  90 */       String playerName = in.readUTF();
/*  91 */       ProxiedPlayer player = getProxy().getPlayer(playerName);
/*  92 */       sendLoadPlayerData(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 102 */     unregisterChannels();
/* 103 */     unregisterListeners();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\bungeecord\MagicCosmetics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */