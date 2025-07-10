/*     */ package com.francobm.magicosmetics.velocity;
/*     */ 
/*     */ import com.francobm.magicosmetics.velocity.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.velocity.listeners.PlayerListener;
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import com.google.common.io.ByteArrayDataOutput;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import com.google.inject.Inject;
/*     */ import com.velocitypowered.api.event.Subscribe;
/*     */ import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
/*     */ import com.velocitypowered.api.plugin.Plugin;
/*     */ import com.velocitypowered.api.proxy.Player;
/*     */ import com.velocitypowered.api.proxy.ProxyServer;
/*     */ import com.velocitypowered.api.proxy.ServerConnection;
/*     */ import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
/*     */ import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(id = "magiccosmetics", name = "MagicCosmetics", version = "1.0.0", authors = {"FrancoBM"})
/*     */ public class MagicCosmetics
/*     */ {
/*     */   private final ProxyServer server;
/*     */   private final Logger logger;
/*     */   private Map<Player, PlayerData> cosmetics;
/*  31 */   public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("mc:player");
/*     */   
/*     */   @Inject
/*     */   public MagicCosmetics(ProxyServer server, Logger logger) {
/*  35 */     this.server = server;
/*  36 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void onProxyInitialization(ProxyInitializeEvent event) {
/*  41 */     this.logger.info("Hello Velocity!");
/*  42 */     registerChannels();
/*  43 */     this.cosmetics = new HashMap<>();
/*  44 */     registerListeners();
/*     */   }
/*     */   
/*     */   public void registerChannels() {
/*  48 */     this.server.getChannelRegistrar().register(new ChannelIdentifier[] { (ChannelIdentifier)IDENTIFIER });
/*     */   }
/*     */   
/*     */   public void registerListeners() {
/*  52 */     this.server.getEventManager().register(this, new PlayerListener(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuitPlayerData(Player player) {
/*  57 */     if (player == null)
/*  58 */       return;  ByteArrayDataOutput out = ByteStreams.newDataOutput();
/*  59 */     out.writeUTF("quit");
/*  60 */     out.writeUTF(player.getUsername());
/*  61 */     player.getCurrentServer().map(serverConnection -> Boolean.valueOf(serverConnection.sendPluginMessage((ChannelIdentifier)IDENTIFIER, out.toByteArray())));
/*     */   }
/*     */   
/*     */   public void sendLoadPlayerData(Player player) {
/*     */     String cosmetics, cosmeticsInUse, status;
/*  66 */     PlayerData playerData = this.cosmetics.get(player);
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (playerData.isFirstJoin()) {
/*  71 */       cosmetics = "";
/*  72 */       cosmeticsInUse = "";
/*  73 */       status = "0";
/*     */     } else {
/*  75 */       cosmetics = playerData.getCosmetics();
/*  76 */       cosmeticsInUse = playerData.getCosmeticsInUse();
/*  77 */       status = "1";
/*     */     } 
/*  79 */     ByteArrayDataOutput out = ByteStreams.newDataOutput();
/*  80 */     out.writeUTF("load_cosmetics");
/*  81 */     out.writeUTF(player.getUsername());
/*  82 */     out.writeUTF(cosmetics);
/*  83 */     out.writeUTF(cosmeticsInUse);
/*  84 */     out.writeUTF(status);
/*  85 */     if (player.getCurrentServer().isEmpty())
/*  86 */       return;  ServerConnection serverConnection = player.getCurrentServer().get();
/*  87 */     serverConnection.sendPluginMessage((ChannelIdentifier)IDENTIFIER, out.toByteArray());
/*     */   }
/*     */   
/*     */   public void executePluginMessage(String tag, byte[] data) {
/*  91 */     if (!tag.equals("mc:player"))
/*     */       return; 
/*  93 */     ByteArrayDataInput in = ByteStreams.newDataInput(data);
/*  94 */     String subChannel = in.readUTF();
/*     */     
/*  96 */     if (subChannel.equals("save_cosmetics")) {
/*  97 */       String playerName = in.readUTF();
/*  98 */       String cosmetics = in.readUTF();
/*  99 */       String cosmeticsInUse = in.readUTF();
/* 100 */       Optional<Player> optionalPlayer = getServer().getPlayer(playerName);
/* 101 */       if (optionalPlayer.isEmpty())
/* 102 */         return;  Player player = optionalPlayer.get();
/* 103 */       PlayerData playerData = PlayerData.getPlayer(player);
/* 104 */       playerData.setCosmetics(cosmetics);
/* 105 */       playerData.setCosmeticsInUse(cosmeticsInUse);
/* 106 */       playerData.setFirstJoin(false);
/*     */     }
/* 108 */     else if (subChannel.equals("load_cosmetics")) {
/* 109 */       String playerName = in.readUTF();
/* 110 */       Optional<Player> optionalPlayer = getServer().getPlayer(playerName);
/* 111 */       if (optionalPlayer.isEmpty())
/* 112 */         return;  Player player = optionalPlayer.get();
/* 113 */       sendLoadPlayerData(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger() {
/* 121 */     return this.logger;
/*     */   }
/*     */   
/*     */   public ProxyServer getServer() {
/* 125 */     return this.server;
/*     */   }
/*     */   
/*     */   public Map<Player, PlayerData> getCosmetics() {
/* 129 */     return this.cosmetics;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\velocity\MagicCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */