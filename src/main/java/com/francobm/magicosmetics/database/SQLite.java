package com.francobm.magicosmetics.database;

import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.events.PlayerDataLoadEvent;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class SQLite extends SQL {
  private final File fileSQL;
  
  private final HikariCP hikariCP;
  
  public SQLite() {
    this.hikariCP = new HikariCP();
    this.fileSQL = new File(this.plugin.getDataFolder(), "cosmetics.db");
    this.hikariCP.setProperties(this);
    createTable();
  }
  
  public void createTable() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_cosmetics (id INTEGER PRIMARY KEY AUTOINCREMENT, UUID VARCHAR(255), Player VARCHAR(255), Hat VARCHAR(255), Bag VARCHAR(255), WStick VARCHAR(255), Balloon VARCHAR(255), Spray VARCHAR(255), Available VARCHAR(10000))");
      preparedStatement.executeUpdate();
      this.plugin.getLogger().info("SQLite table created successfully");
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Could not create table: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, null);
    } 
  }
  
  public void loadPlayer(Player player) {
    loadPlayerInfo(player);
  }
  
  public CompletableFuture<PlayerData> loadPlayerAsync(Player player) {
    return loadPlayerInfoAsync(player);
  }
  
  public void savePlayer(PlayerData playerData, boolean close) {
    savePlayerInfo(playerData, close);
  }
  
  public CompletableFuture<Void> savePlayerAsync(PlayerData playerData) {
    return savePlayerInfoAsync(playerData);
  }
  
  public void savePlayers() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      for (PlayerData player : PlayerData.players.values()) {
        player.clearCosmeticsToSaveData();
        if (!checkInfo(player.getUniqueId())) {
          String str = "INSERT INTO player_cosmetics (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
          preparedStatement = connection.prepareStatement(str);
          preparedStatement.setString(1, player.getUniqueId().toString());
          preparedStatement.setString(2, player.getOfflinePlayer().getName());
          preparedStatement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
          preparedStatement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
          preparedStatement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
          preparedStatement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
          preparedStatement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
          preparedStatement.setString(8, player.saveCosmetics());
          preparedStatement.executeUpdate();
          continue;
        } 
        String query = "UPDATE player_cosmetics SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, player.getOfflinePlayer().getName());
        preparedStatement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
        preparedStatement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
        preparedStatement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
        preparedStatement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
        preparedStatement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
        preparedStatement.setString(7, player.saveCosmetics());
        preparedStatement.setString(8, player.getUniqueId().toString());
        preparedStatement.executeUpdate();
      } 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, null);
    } 
  }
  
  private void savePlayerInfo(PlayerData player, boolean close) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    player.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      if (!checkInfo(player.getUniqueId())) {
        String query = "INSERT INTO player_cosmetics (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, player.getUniqueId().toString());
        preparedStatement.setString(2, player.getOfflinePlayer().getName());
        preparedStatement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
        preparedStatement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
        preparedStatement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
        preparedStatement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
        preparedStatement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
        preparedStatement.setString(8, player.saveCosmetics());
        preparedStatement.executeUpdate();
      } else {
        String query = "UPDATE player_cosmetics SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, player.getOfflinePlayer().getName());
        preparedStatement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
        preparedStatement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
        preparedStatement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
        preparedStatement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
        preparedStatement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
        preparedStatement.setString(7, player.saveCosmetics());
        preparedStatement.setString(8, player.getUniqueId().toString());
        preparedStatement.executeUpdate();
      } 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, null);
      if (close)
        player.clearCosmeticsToSaveData(); 
    } 
  }
  
  private CompletableFuture<Void> savePlayerInfoAsync(PlayerData player) {
    return checkInfoAsync(player.getUniqueId()).thenCompose(check -> CompletableFuture.runAsync(()));
  }
  
  private void loadPlayerInfo(Player player) {
    String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
    CustomSpray.updateSpray(player);
    PlayerBalloon.updatePlayerBalloon(player);
    EntityBag.updateEntityBag(player);
    EntityBalloon.updateEntityBalloon(player);
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      preparedStatement = connection.prepareStatement(queryBuilder);
      preparedStatement.setString(1, player.getUniqueId().toString());
      resultSet = preparedStatement.executeQuery();
      if (resultSet == null)
        return; 
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
      if (resultSet.next()) {
        String cosmetics = resultSet.getString("Available");
        String hat = resultSet.getString("Hat");
        String bag = resultSet.getString("Bag");
        String wStick = resultSet.getString("WStick");
        String balloon = resultSet.getString("Balloon");
        String spray = resultSet.getString("Spray");
        playerData.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        playerData.loadCosmetics(cosmetics);
        playerData.setCosmetic(CosmeticType.HAT, playerData.getCosmeticById(hat));
        playerData.setCosmetic(CosmeticType.BAG, playerData.getCosmeticById(bag));
        playerData.setCosmetic(CosmeticType.WALKING_STICK, playerData.getCosmeticById(wStick));
        playerData.setCosmetic(CosmeticType.BALLOON, playerData.getCosmeticById(balloon));
        playerData.setCosmetic(CosmeticType.SPRAY, playerData.getCosmeticById(spray));
        this.plugin.getServer().getPluginManager().callEvent((Event)new PlayerDataLoadEvent(playerData, playerData.cosmeticsInUse()));
      } 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to load player information: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, resultSet);
    } 
  }
  
  private CompletableFuture<PlayerData> loadPlayerInfoAsync(Player player) {
    return CompletableFuture.supplyAsync(() -> {
          String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          ResultSet resultSet = null;
          try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setString(1, player.getUniqueId().toString());
            resultSet = preparedStatement.executeQuery();
            PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
            if (resultSet == null)
              return playerData; 
            if (resultSet.next()) {
              String cosmetics = resultSet.getString("Available");
              String hat = resultSet.getString("Hat");
              String bag = resultSet.getString("Bag");
              String wStick = resultSet.getString("WStick");
              String balloon = resultSet.getString("Balloon");
              String spray = resultSet.getString("Spray");
              playerData.setOfflinePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
              playerData.loadCosmetics(cosmetics);
              playerData.setCosmetic(CosmeticType.BALLOON, playerData.getCosmeticById(balloon));
              playerData.setCosmetic(CosmeticType.SPRAY, playerData.getCosmeticById(spray));
              EntityBag.updateEntityBag(player);
              EntityBalloon.updateEntityBalloon(player);
              CustomSpray.updateSpray(player);
              PlayerBalloon.updatePlayerBalloon(player);
              this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, ());
              return playerData;
            } 
          } catch (SQLException throwable) {
            this.plugin.getLogger().severe("Failed to load async player information: " + throwable.getMessage());
          } finally {
            closeConnections(preparedStatement, connection, resultSet);
          } 
          return null;
        });
  }
  
  private boolean checkInfo(UUID uuid) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      preparedStatement = connection.prepareStatement(queryBuilder);
      preparedStatement.setString(1, uuid.toString());
      resultSet = preparedStatement.executeQuery();
      if (resultSet != null && resultSet.next())
        return true; 
    } catch (SQLException sQLException) {
    
    } finally {
      closeConnections(preparedStatement, connection, resultSet);
    } 
    return false;
  }
  
  private CompletableFuture<Boolean> checkInfoAsync(UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          ResultSet resultSet = null;
          String queryBuilder = "SELECT * FROM player_cosmetics WHERE UUID = ?";
          try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next())
              return Boolean.valueOf(true); 
          } catch (SQLException sQLException) {
          
          } finally {
            closeConnections(preparedStatement, connection, resultSet);
          } 
          return Boolean.valueOf(false);
        });
  }
  
  public DatabaseType getDatabaseType() {
    return DatabaseType.SQLITE;
  }
  
  public File getFileSQL() {
    return this.fileSQL;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\SQLite.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */