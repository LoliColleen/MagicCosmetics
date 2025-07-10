/*     */ package com.francobm.magicosmetics.database;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
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
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MySQL extends SQL {
/*     */   private final HikariCP hikariCP;
/*     */   
/*     */   public MySQL() {
/*  25 */     FileCreator config = this.plugin.getConfig();
/*  26 */     String hostname = config.getString("MySQL.host");
/*  27 */     int port = config.getInt("MySQL.port");
/*  28 */     String username = config.getString("MySQL.user");
/*  29 */     String password = config.getString("MySQL.password");
/*  30 */     String database = config.getString("MySQL.database");
/*  31 */     String options = config.getString("MySQL.options");
/*  32 */     this.table = config.getString("MySQL.table");
/*  33 */     this.hikariCP = new HikariCP(hostname, port, username, password, database, options);
/*  34 */     this.hikariCP.setProperties(this);
/*  35 */     createTable();
/*     */   }
/*     */   private final String table;
/*     */   
/*     */   public void createTable() {
/*  40 */     Connection connection = null;
/*  41 */     PreparedStatement preparedStatement = null;
/*     */     try {
/*  43 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/*  44 */       preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.table + "` (id INT AUTO_INCREMENT, UUID VARCHAR(255), Player VARCHAR(255), Hat VARCHAR(255), Bag VARCHAR(255), WStick VARCHAR(255), Balloon VARCHAR(255), Spray VARCHAR(255), Available VARCHAR(10000), PRIMARY KEY (id))");
/*  45 */       preparedStatement.executeUpdate();
/*  46 */       this.plugin.getLogger().info("MySQL table created successfully");
/*  47 */     } catch (SQLException throwable) {
/*  48 */       throwable.printStackTrace();
/*  49 */       this.plugin.getLogger().severe("Could not create table: " + throwable.getMessage());
/*     */     } finally {
/*  51 */       closeConnections(preparedStatement, connection, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPlayer(Player player) {
/*  57 */     loadPlayerInfo(player);
/*     */   }
/*     */   
/*     */   private void loadPlayerInfo(Player player) {
/*  61 */     String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
/*  62 */     EntityBag.updateEntityBag(player);
/*  63 */     EntityBalloon.updateEntityBalloon(player);
/*  64 */     Connection connection = null;
/*  65 */     PreparedStatement statement = null;
/*  66 */     ResultSet resultSet = null;
/*     */     try {
/*  68 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/*  69 */       statement = connection.prepareStatement(queryBuilder);
/*  70 */       statement.setString(1, player.getUniqueId().toString());
/*  71 */       resultSet = statement.executeQuery();
/*  72 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  73 */       if (resultSet == null) {
/*     */         return;
/*     */       }
/*  76 */       if (resultSet.next()) {
/*  77 */         String cosmetics = resultSet.getString("Available");
/*  78 */         String hat = resultSet.getString("Hat");
/*  79 */         String bag = resultSet.getString("Bag");
/*  80 */         String wStick = resultSet.getString("WStick");
/*  81 */         String balloon = resultSet.getString("Balloon");
/*  82 */         String spray = resultSet.getString("Spray");
/*  83 */         playerData.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
/*  84 */         playerData.loadCosmetics(cosmetics);
/*  85 */         playerData.setCosmetic(CosmeticType.HAT, playerData.getCosmeticById(hat));
/*  86 */         playerData.setCosmetic(CosmeticType.BAG, playerData.getCosmeticById(bag));
/*  87 */         playerData.setCosmetic(CosmeticType.WALKING_STICK, playerData.getCosmeticById(wStick));
/*  88 */         playerData.setCosmetic(CosmeticType.BALLOON, playerData.getCosmeticById(balloon));
/*  89 */         playerData.setCosmetic(CosmeticType.SPRAY, playerData.getCosmeticById(spray));
/*  90 */         CustomSpray.updateSpray(player);
/*  91 */         PlayerBalloon.updatePlayerBalloon(player);
/*  92 */         this.plugin.getServer().getPluginManager().callEvent((Event)new PlayerDataLoadEvent(playerData, playerData.cosmeticsInUse()));
/*     */       } 
/*  94 */     } catch (SQLException throwable) {
/*  95 */       this.plugin.getLogger().severe("Failed to load player information: " + throwable.getMessage());
/*     */     } finally {
/*  97 */       closeConnections(statement, connection, resultSet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void savePlayer(PlayerData playerData, boolean close) {
/* 103 */     savePlayerInfo(playerData, close);
/*     */   }
/*     */   
/*     */   private void savePlayerInfo(PlayerData player, boolean close) {
/* 107 */     Connection connection = null;
/* 108 */     PreparedStatement statement = null;
/*     */     try {
/* 110 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 111 */       if (!checkInfo(player.getUniqueId())) {
/* 112 */         String str = "INSERT INTO " + this.table + " (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
/* 113 */         statement = connection.prepareStatement(str);
/* 114 */         statement.setString(1, player.getUniqueId().toString());
/* 115 */         statement.setString(2, player.getOfflinePlayer().getName());
/* 116 */         statement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
/* 117 */         statement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
/* 118 */         statement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 119 */         statement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 120 */         statement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 121 */         statement.setString(8, player.saveCosmetics());
/* 122 */         statement.executeUpdate();
/*     */         return;
/*     */       } 
/* 125 */       String query = "UPDATE " + this.table + " SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
/* 126 */       statement = connection.prepareStatement(query);
/* 127 */       statement.setString(1, player.getOfflinePlayer().getName());
/* 128 */       statement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
/* 129 */       statement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
/* 130 */       statement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 131 */       statement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 132 */       statement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 133 */       statement.setString(7, player.saveCosmetics());
/* 134 */       statement.setString(8, player.getUniqueId().toString());
/* 135 */       statement.executeUpdate();
/* 136 */     } catch (SQLException throwable) {
/* 137 */       this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
/*     */     } finally {
/* 139 */       closeConnections(statement, connection, null);
/* 140 */       if (close) {
/* 141 */         player.clearCosmeticsToSaveData();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void savePlayers() {
/* 147 */     Connection connection = null;
/* 148 */     PreparedStatement statement = null;
/*     */     try {
/* 150 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 151 */       for (PlayerData player : PlayerData.players.values()) {
/* 152 */         player.clearCosmeticsToSaveData();
/* 153 */         if (!checkInfo(player.getUniqueId())) {
/* 154 */           String str = "INSERT INTO " + this.table + " (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
/* 155 */           statement = connection.prepareStatement(str);
/* 156 */           statement.setString(1, player.getUniqueId().toString());
/* 157 */           statement.setString(2, player.getOfflinePlayer().getName());
/* 158 */           statement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
/* 159 */           statement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
/* 160 */           statement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 161 */           statement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 162 */           statement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 163 */           statement.setString(8, player.saveCosmetics());
/* 164 */           statement.executeUpdate();
/*     */           return;
/*     */         } 
/* 167 */         String query = "UPDATE " + this.table + " SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
/* 168 */         statement = connection.prepareStatement(query);
/* 169 */         statement.setString(1, player.getOfflinePlayer().getName());
/* 170 */         statement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
/* 171 */         statement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
/* 172 */         statement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
/* 173 */         statement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
/* 174 */         statement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
/* 175 */         statement.setString(7, player.saveCosmetics());
/* 176 */         statement.setString(8, player.getUniqueId().toString());
/* 177 */         statement.executeUpdate();
/*     */       } 
/* 179 */     } catch (SQLException throwable) {
/* 180 */       this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
/*     */     } finally {
/* 182 */       closeConnections(statement, connection, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<PlayerData> loadPlayerAsync(Player player) {
/* 188 */     return loadPlayerInfoAsync(player);
/*     */   }
/*     */   
/*     */   private CompletableFuture<PlayerData> loadPlayerInfoAsync(Player player) {
/* 192 */     return CompletableFuture.supplyAsync(() -> {
/*     */           String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
/*     */           
/*     */           Connection connection = null;
/*     */           
/*     */           PreparedStatement statement = null;
/*     */           
/*     */           ResultSet resultSet = null;
/*     */           
/*     */           try {
/*     */             connection = this.hikariCP.getHikariDataSource().getConnection();
/*     */             
/*     */             statement = connection.prepareStatement(queryBuilder);
/*     */             
/*     */             statement.setString(1, player.getUniqueId().toString());
/*     */             resultSet = statement.executeQuery();
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
/* 230 */           } catch (SQLException throwable) {
/*     */             this.plugin.getLogger().severe("Failed to load async player information: " + throwable.getMessage());
/*     */           } finally {
/*     */             closeConnections(statement, connection, resultSet);
/*     */           } 
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> savePlayerAsync(PlayerData playerData) {
/* 241 */     return savePlayerInfoAsync(playerData);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Void> savePlayerInfoAsync(PlayerData player) {
/* 245 */     player.clearCosmeticsToSaveData();
/* 246 */     return checkInfoAsync(player.getUniqueId()).thenCompose(check -> CompletableFuture.runAsync(()));
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
/*     */   private CompletableFuture<Boolean> checkInfoAsync(UUID uuid) {
/* 287 */     return CompletableFuture.supplyAsync(() -> {
/*     */           String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
/*     */           Connection connection = null;
/*     */           PreparedStatement preparedStatement = null;
/*     */           ResultSet resultSet = null;
/*     */           try {
/*     */             connection = this.hikariCP.getHikariDataSource().getConnection();
/*     */             preparedStatement = connection.prepareStatement(queryBuilder);
/*     */             preparedStatement.setString(1, uuid.toString());
/*     */             resultSet = preparedStatement.executeQuery();
/*     */             if (resultSet != null && resultSet.next()) {
/*     */               return Boolean.valueOf(true);
/*     */             }
/* 300 */           } catch (SQLException throwable) {
/*     */             this.plugin.getLogger().severe("Player information could not be verified.: " + throwable.getMessage());
/*     */           } finally {
/*     */             closeConnections(preparedStatement, connection, resultSet);
/*     */           } 
/*     */           return Boolean.valueOf(false);
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean checkInfo(UUID uuid) {
/* 310 */     String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
/* 311 */     Connection connection = null;
/* 312 */     PreparedStatement preparedStatement = null;
/* 313 */     ResultSet resultSet = null;
/*     */     try {
/* 315 */       connection = this.hikariCP.getHikariDataSource().getConnection();
/* 316 */       preparedStatement = connection.prepareStatement(queryBuilder);
/* 317 */       preparedStatement.setString(1, uuid.toString());
/* 318 */       resultSet = preparedStatement.executeQuery();
/* 319 */       if (resultSet != null && resultSet.next()) {
/* 320 */         return true;
/*     */       }
/* 322 */     } catch (SQLException throwable) {
/* 323 */       this.plugin.getLogger().severe("Player information could not be verified.: " + throwable.getMessage());
/*     */     } finally {
/* 325 */       closeConnections(preparedStatement, connection, resultSet);
/*     */     } 
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatabaseType getDatabaseType() {
/* 332 */     return DatabaseType.MYSQL;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\MySQL.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */