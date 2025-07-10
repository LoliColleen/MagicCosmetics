package com.francobm.magicosmetics.database;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public abstract class SQL {
  protected MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public abstract void createTable();
  
  public abstract void loadPlayer(Player paramPlayer);
  
  public abstract CompletableFuture<PlayerData> loadPlayerAsync(Player paramPlayer);
  
  public abstract void savePlayer(PlayerData paramPlayerData, boolean paramBoolean);
  
  public abstract CompletableFuture<Void> savePlayerAsync(PlayerData paramPlayerData);
  
  public abstract void savePlayers();
  
  public abstract DatabaseType getDatabaseType();
  
  protected void closeConnections(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
    if (connection == null)
      return; 
    try {
      if (connection.isClosed())
        return; 
      if (resultSet != null)
        resultSet.close(); 
      if (preparedStatement != null)
        preparedStatement.close(); 
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\database\SQL.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */