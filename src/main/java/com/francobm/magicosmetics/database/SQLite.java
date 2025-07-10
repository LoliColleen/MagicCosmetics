/*     */ package com.francobm.magicosmetics.database;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import java.io.File;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ 
/*     */ public class SQLite extends SQL {
/*     */   private final File fileSQL;
/*     */   
/*     */   public SQLite() {
/*  24 */     this.hikariCP = new HikariCP();
/*  25 */     this.fileSQL = new File(this.plugin.getDataFolder(), "cosmetics.db");
/*  26 */     this.hikariCP.setProperties(this);
/*  27 */     createTable();
/*     */   }
/*     */   private final HikariCP hikariCP;
/*     */   
/*     */   public void createTable() {
/*  32 */     Connection connection = null;
/*  33 */     PreparedStatement preparedStatement = null;
/*     */     try {
/*  35 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/*  36 */       preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_cosmetics (id INTEGER PRIMARY KEY AUTOINCREMENT, UUID VARCHAR(255), Player VARCHAR(255), Hat VARCHAR(255), Bag VARCHAR(255), WStick VARCHAR(255), Balloon VARCHAR(255), Spray VARCHAR(255), Available VARCHAR(10000))");
/*  37 */       preparedStatement.executeUpdate();
/*  38 */       this.plugin.getLogger().info("SQLite table created successfully");
/*  39 */     } catch (SQLException throwable) {
/*  40 */       this.plugin.getLogger().severe("Could not create table: " + throwable.getMessage());
/*     */     } finally {
/*  42 */       closeConnections(preparedStatement, connection, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPlayer(Player player) {
/*  48 */     loadPlayerInfo(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<PlayerData> loadPlayerAsync(Player player) {
/*  53 */     return loadPlayerInfoAsync(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void savePlayer(PlayerData playerData, boolean close) {
/*  58 */     savePlayerInfo(playerData, close);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> savePlayerAsync(PlayerData playerData) {
/*  63 */     return savePlayerInfoAsync(playerData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void savePlayers() {
/*  68 */     Connection connection = null;
/*  69 */     PreparedStatement preparedStatement = null;
/*     */     try {
/*  71 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/*  72 */       for (PlayerData player : PlayerData.players.values()) {
/*  73 */         player.clearCosmeticsToSaveData();
/*  74 */         if (!checkInfo(player.getUniqueId())) {
/*  75 */           String str = "INSERT INTO player_cosmetics (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
/*  76 */           preparedStatement = connection.prepareStatement(str);
/*  77 */           preparedStatement.setString(1, player.getUniqueId().toString());
/*  78 */           preparedStatement.setString(2, player.getOfflinePlayer().getName());
/*  79 */           preparedStatement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
/*  80 */           preparedStatement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
/*  81 */           preparedStatement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
/*  82 */           preparedStatement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/*  83 */           preparedStatement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
/*  84 */           preparedStatement.setString(8, player.saveCosmetics());
/*  85 */           preparedStatement.executeUpdate();
/*     */           continue;
/*     */         } 
/*  88 */         String query = "UPDATE player_cosmetics SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
/*  89 */         preparedStatement = connection.prepareStatement(query);
/*  90 */         preparedStatement.setString(1, player.getOfflinePlayer().getName());
/*  91 */         preparedStatement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
/*  92 */         preparedStatement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
/*  93 */         preparedStatement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
/*  94 */         preparedStatement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/*  95 */         preparedStatement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
/*  96 */         preparedStatement.setString(7, player.saveCosmetics());
/*  97 */         preparedStatement.setString(8, player.getUniqueId().toString());
/*  98 */         preparedStatement.executeUpdate();
/*     */       }
/*     */     
/* 101 */     } catch (SQLException throwable) {
/* 102 */       this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
/*     */     } finally {
/* 104 */       closeConnections(preparedStatement, connection, null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void savePlayerInfo(PlayerData player, boolean close) {
/* 109 */     Connection connection = null;
/* 110 */     PreparedStatement preparedStatement = null;
/* 111 */     player.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
/*     */     try {
/* 113 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 114 */       if (!checkInfo(player.getUniqueId())) {
/* 115 */         String query = "INSERT INTO player_cosmetics (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
/* 116 */         preparedStatement = connection.prepareStatement(query);
/* 117 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 118 */         preparedStatement.setString(2, player.getOfflinePlayer().getName());
/* 119 */         preparedStatement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
/* 120 */         preparedStatement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
/* 121 */         preparedStatement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 122 */         preparedStatement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 123 */         preparedStatement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 124 */         preparedStatement.setString(8, player.saveCosmetics());
/* 125 */         preparedStatement.executeUpdate();
/*     */       } else {
/*     */         
/* 128 */         String query = "UPDATE player_cosmetics SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
/* 129 */         preparedStatement = connection.prepareStatement(query);
/* 130 */         preparedStatement.setString(1, player.getOfflinePlayer().getName());
/* 131 */         preparedStatement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
/* 132 */         preparedStatement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
/* 133 */         preparedStatement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 134 */         preparedStatement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 135 */         preparedStatement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 136 */         preparedStatement.setString(7, player.saveCosmetics());
/* 137 */         preparedStatement.setString(8, player.getUniqueId().toString());
/* 138 */         preparedStatement.executeUpdate();
/*     */       } 
/* 140 */     } catch (SQLException throwable) {
/* 141 */       this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
/*     */     } finally {
/* 143 */       closeConnections(preparedStatement, connection, null);
/* 144 */       if (close)
/* 145 */         player.clearCosmeticsToSaveData(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Void> savePlayerInfoAsync(PlayerData player) {
/* 150 */     return checkInfoAsync(player.getUniqueId()).thenCompose(check -> CompletableFuture.runAsync(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPlayerInfo(Player player) {
/* 191 */     String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
/* 192 */     CustomSpray.updateSpray(player);
/* 193 */     PlayerBalloon.updatePlayerBalloon(player);
/* 194 */     EntityBag.updateEntityBag(player);
/* 195 */     EntityBalloon.updateEntityBalloon(player);
/* 196 */     Connection connection = null;
/* 197 */     PreparedStatement preparedStatement = null;
/* 198 */     ResultSet resultSet = null;
/*     */     try {
/* 200 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 201 */       preparedStatement = connection.prepareStatement(queryBuilder);
/* 202 */       preparedStatement.setString(1, player.getUniqueId().toString());
/* 203 */       resultSet = preparedStatement.executeQuery();
/* 204 */       if (resultSet == null) {
/*     */         return;
/*     */       }
/* 207 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 208 */       if (resultSet.next()) {
/* 209 */         String cosmetics = resultSet.getString("Available");
/* 210 */         String hat = resultSet.getString("Hat");
/* 211 */         String bag = resultSet.getString("Bag");
/* 212 */         String wStick = resultSet.getString("WStick");
/* 213 */         String balloon = resultSet.getString("Balloon");
/* 214 */         String spray = resultSet.getString("Spray");
/* 215 */         playerData.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
/* 216 */         playerData.loadCosmetics(cosmetics);
/* 217 */         playerData.setCosmetic(CosmeticType.HAT, playerData.getCosmeticById(hat));
/* 218 */         playerData.setCosmetic(CosmeticType.BAG, playerData.getCosmeticById(bag));
/* 219 */         playerData.setCosmetic(CosmeticType.WALKING_STICK, playerData.getCosmeticById(wStick));
/* 220 */         playerData.setCosmetic(CosmeticType.BALLOON, playerData.getCosmeticById(balloon));
/* 221 */         playerData.setCosmetic(CosmeticType.SPRAY, playerData.getCosmeticById(spray));
/* 222 */         this.plugin.getServer().getPluginManager().callEvent((Event)new PlayerDataLoadEvent(playerData, playerData.cosmeticsInUse()));
/*     */       } 
/* 224 */     } catch (SQLException throwable) {
/* 225 */       this.plugin.getLogger().severe("Failed to load player information: " + throwable.getMessage());
/*     */     } finally {
/* 227 */       closeConnections(preparedStatement, connection, resultSet);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<PlayerData> loadPlayerInfoAsync(Player player) {
/* 232 */     return CompletableFuture.supplyAsync(() -> {
/*     */           String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
/*     */           
/*     */           Connection connection = null;
/*     */           
/*     */           PreparedStatement preparedStatement = null;
/*     */           
/*     */           ResultSet resultSet = null;
/*     */           
/*     */           try {
/*     */             connection = this.hikariCP.getHikariDataSource().getConnection();
/*     */             
/*     */             preparedStatement = connection.prepareStatement(queryBuilder);
/*     */             
/*     */             preparedStatement.setString(1, player.getUniqueId().toString());
/*     */             
/*     */             resultSet = preparedStatement.executeQuery();
/*     */             PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*     */             if (resultSet == null) {
/*     */               return playerData;
/*     */             }
/*     */             if (resultSet.next()) {
/*     */               String cosmetics = resultSet.getString("Available");
/*     */               String hat = resultSet.getString("Hat");
/*     */               String bag = resultSet.getString("Bag");
/*     */               String wStick = resultSet.getString("WStick");
/*     */               String balloon = resultSet.getString("Balloon");
/*     */               String spray = resultSet.getString("Spray");
/*     */               playerData.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
/*     */               playerData.loadCosmetics(cosmetics);
/*     */               playerData.setCosmetic(CosmeticType.BALLOON, playerData.getCosmeticById(balloon));
/*     */               playerData.setCosmetic(CosmeticType.SPRAY, playerData.getCosmeticById(spray));
/*     */               EntityBag.updateEntityBag(player);
/*     */               EntityBalloon.updateEntityBalloon(player);
/*     */               CustomSpray.updateSpray(player);
/*     */               PlayerBalloon.updatePlayerBalloon(player);
/*     */               this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, ());
/*     */               return playerData;
/*     */             } 
/* 271 */           } catch (SQLException throwable) {
/*     */             this.plugin.getLogger().severe("Failed to load async player information: " + throwable.getMessage());
/*     */           } finally {
/*     */             closeConnections(preparedStatement, connection, resultSet);
/*     */           } 
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkInfo(UUID uuid) {
/* 282 */     Connection connection = null;
/* 283 */     PreparedStatement preparedStatement = null;
/* 284 */     ResultSet resultSet = null;
/* 285 */     String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
/*     */     try {
/* 287 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 288 */       preparedStatement = connection.prepareStatement(queryBuilder);
/* 289 */       preparedStatement.setString(1, uuid.toString());
/* 290 */       resultSet = preparedStatement.executeQuery();
/* 291 */       if (resultSet != null && resultSet.next()) {
/* 292 */         return true;
/*     */       }
/* 294 */     } catch (SQLException sQLException) {
/*     */     
/*     */     } finally {
/* 297 */       closeConnections(preparedStatement, connection, resultSet);
/*     */     } 
/* 299 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Boolean> checkInfoAsync(UUID uuid) {
/* 303 */     return CompletableFuture.supplyAsync(() -> {
/*     */           Connection connection = null;
/*     */           PreparedStatement preparedStatement = null;
/*     */           ResultSet resultSet = null;
/*     */           String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
/*     */           try {
/*     */             connection = this.hikariCP.getHikariDataSource().getConnection();
/*     */             preparedStatement = connection.prepareStatement(queryBuilder);
/*     */             preparedStatement.setString(1, uuid.toString());
/*     */             resultSet = preparedStatement.executeQuery();
/*     */             if (resultSet != null && resultSet.next()) {
/*     */               return Boolean.valueOf(true);
/*     */             }
/* 316 */           } catch (SQLException sQLException) {
/*     */           
/*     */           } finally {
/*     */             closeConnections(preparedStatement, connection, resultSet);
/*     */           } 
/*     */           return Boolean.valueOf(false);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public DatabaseType getDatabaseType() {
/* 327 */     return DatabaseType.SQLITE;
/*     */   }
/*     */   
/*     */   public File getFileSQL() {
/* 331 */     return this.fileSQL;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\SQLite.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */