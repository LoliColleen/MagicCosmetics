package com.francobm.magicosmetics.database;

import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.events.PlayerDataLoadEvent;
import com.francobm.magicosmetics.files.FileCreator;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
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

public class MySQL extends SQL {
  private final HikariCP hikariCP;
  
  private final String table;
  
  public MySQL() {
    FileCreator config = this.plugin.getConfig();
    String hostname = config.getString("MySQL.host");
    int port = config.getInt("MySQL.port");
    String username = config.getString("MySQL.user");
    String password = config.getString("MySQL.password");
    String database = config.getString("MySQL.database");
    String options = config.getString("MySQL.options");
    this.table = config.getString("MySQL.table");
    this.hikariCP = new HikariCP(hostname, port, username, password, database, options);
    this.hikariCP.setProperties(this);
    createTable();
  }
  
  public void createTable() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.table + "` (id INT AUTO_INCREMENT, UUID VARCHAR(255), Player VARCHAR(255), Hat VARCHAR(255), Bag VARCHAR(255), WStick VARCHAR(255), Balloon VARCHAR(255), Spray VARCHAR(255), Available VARCHAR(10000), PRIMARY KEY (id))");
      preparedStatement.executeUpdate();
      this.plugin.getLogger().info("MySQL table created successfully");
    } catch (SQLException throwable) {
      throwable.printStackTrace();
      this.plugin.getLogger().severe("Could not create table: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, null);
    } 
  }
  
  public void loadPlayer(Player player) {
    loadPlayerInfo(player);
  }
  
  private void loadPlayerInfo(Player player) {
    String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
    EntityBag.updateEntityBag(player);
    EntityBalloon.updateEntityBalloon(player);
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      statement = connection.prepareStatement(queryBuilder);
      statement.setString(1, player.getUniqueId().toString());
      resultSet = statement.executeQuery();
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
      if (resultSet == null)
        return; 
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
        CustomSpray.updateSpray(player);
        PlayerBalloon.updatePlayerBalloon(player);
        this.plugin.getServer().getPluginManager().callEvent((Event)new PlayerDataLoadEvent(playerData, playerData.cosmeticsInUse()));
      } 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to load player information: " + throwable.getMessage());
    } finally {
      closeConnections(statement, connection, resultSet);
    } 
  }
  
  public void savePlayer(PlayerData playerData, boolean close) {
    savePlayerInfo(playerData, close);
  }
  
  private void savePlayerInfo(PlayerData player, boolean close) {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      if (!checkInfo(player.getUniqueId())) {
        String str = "INSERT INTO " + this.table + " (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
        statement = connection.prepareStatement(str);
        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getOfflinePlayer().getName());
        statement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
        statement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
        statement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
        statement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
        statement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
        statement.setString(8, player.saveCosmetics());
        statement.executeUpdate();
        return;
      } 
      String query = "UPDATE " + this.table + " SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
      statement = connection.prepareStatement(query);
      statement.setString(1, player.getOfflinePlayer().getName());
      statement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
      statement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
      statement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
      statement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
      statement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
      statement.setString(7, player.saveCosmetics());
      statement.setString(8, player.getUniqueId().toString());
      statement.executeUpdate();
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
    } finally {
      closeConnections(statement, connection, null);
      if (close)
        player.clearCosmeticsToSaveData(); 
    } 
  }
  
  public void savePlayers() {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      for (PlayerData player : PlayerData.players.values()) {
        player.clearCosmeticsToSaveData();
        if (!checkInfo(player.getUniqueId())) {
          String str = "INSERT INTO " + this.table + " (id, UUID, Player, Hat, Bag, WStick, Balloon, Spray, Available) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
          statement = connection.prepareStatement(str);
          statement.setString(1, player.getUniqueId().toString());
          statement.setString(2, player.getOfflinePlayer().getName());
          statement.setString(3, (player.getHat() == null) ? "" : player.getHat().getId());
          statement.setString(4, (player.getBag() == null) ? "" : player.getBag().getId());
          statement.setString(5, (player.getWStick() == null) ? "" : player.getWStick().getId());
          statement.setString(6, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
          statement.setString(7, (player.getSpray() == null) ? "" : player.getSpray().getId());
          statement.setString(8, player.saveCosmetics());
          statement.executeUpdate();
          return;
        } 
        String query = "UPDATE " + this.table + " SET Player = ?, Hat = ?, Bag = ?, WStick = ?, Balloon = ?, Spray = ?, Available = ? WHERE UUID = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, player.getOfflinePlayer().getName());
        statement.setString(2, (player.getHat() == null) ? "" : player.getHat().getId());
        statement.setString(3, (player.getBag() == null) ? "" : player.getBag().getId());
        statement.setString(4, (player.getWStick() == null) ? "" : player.getWStick().getId());
        statement.setString(5, (player.getBalloon() == null) ? "" : player.getBalloon().getId());
        statement.setString(6, (player.getSpray() == null) ? "" : player.getSpray().getId());
        statement.setString(7, player.saveCosmetics());
        statement.setString(8, player.getUniqueId().toString());
        statement.executeUpdate();
      } 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Failed to save player information: " + throwable.getMessage());
    } finally {
      closeConnections(statement, connection, null);
    } 
  }
  
  public CompletableFuture<PlayerData> loadPlayerAsync(Player player) {
    return loadPlayerInfoAsync(player);
  }
  
  private CompletableFuture<PlayerData> loadPlayerInfoAsync(Player player) {
    return CompletableFuture.supplyAsync(() -> {
          String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
          Connection connection = null;
          PreparedStatement statement = null;
          ResultSet resultSet = null;
          try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            statement = connection.prepareStatement(queryBuilder);
            statement.setString(1, player.getUniqueId().toString());
            resultSet = statement.executeQuery();
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
            closeConnections(statement, connection, resultSet);
          } 
          return null;
        });
  }
  
  public CompletableFuture<Void> savePlayerAsync(PlayerData playerData) {
    return savePlayerInfoAsync(playerData);
  }
  
  private CompletableFuture<Void> savePlayerInfoAsync(PlayerData player) {
    player.clearCosmeticsToSaveData();
    return checkInfoAsync(player.getUniqueId()).thenCompose(check -> CompletableFuture.runAsync(()));
  }
  
  private CompletableFuture<Boolean> checkInfoAsync(UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
          String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          ResultSet resultSet = null;
          try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next())
              return Boolean.valueOf(true); 
          } catch (SQLException throwable) {
            this.plugin.getLogger().severe("Player information could not be verified.: " + throwable.getMessage());
          } finally {
            closeConnections(preparedStatement, connection, resultSet);
          } 
          return Boolean.valueOf(false);
        });
  }
  
  private boolean checkInfo(UUID uuid) {
    String queryBuilder = "SELECT * FROM " + this.table + " WHERE UUID = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = this.hikariCP.getHikariDataSource().getConnection();
      preparedStatement = connection.prepareStatement(queryBuilder);
      preparedStatement.setString(1, uuid.toString());
      resultSet = preparedStatement.executeQuery();
      if (resultSet != null && resultSet.next())
        return true; 
    } catch (SQLException throwable) {
      this.plugin.getLogger().severe("Player information could not be verified.: " + throwable.getMessage());
    } finally {
      closeConnections(preparedStatement, connection, resultSet);
    } 
    return false;
  }
  
  public DatabaseType getDatabaseType() {
    return DatabaseType.MYSQL;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\MySQL.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */