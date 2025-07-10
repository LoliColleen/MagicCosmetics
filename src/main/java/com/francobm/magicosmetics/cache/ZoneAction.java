package com.francobm.magicosmetics.cache;

import com.francobm.magicosmetics.MagicCosmetics;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZoneAction {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  private final String id;
  
  private List<String> commands;
  
  public ZoneAction(String id, List<String> commands) {
    this.id = id;
    this.commands = commands;
  }
  
  public String getId() {
    return this.id;
  }
  
  public List<String> getCommands() {
    return this.commands;
  }
  
  public void setCommands(List<String> commands) {
    this.commands = commands;
  }
  
  public void executeCommands(Player player, String zoneId) {
    for (String command : this.commands) {
      if (this.plugin.isPlaceholderAPI()) {
        command = this.plugin.getPlaceholderAPI().setPlaceholders(player, command.replace("%player%", player.getName()).replace("%zone%", zoneId));
      } else {
        command = command.replace("%player%", player.getName()).replace("%zone%", zoneId);
      } 
      if (command.startsWith("[console] ")) {
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command.replace("[console] ", ""));
        continue;
      } 
      if (command.startsWith("[player] ")) {
        player.performCommand(command.replace("[player] ", ""));
        continue;
      } 
      player.performCommand(command);
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\ZoneAction.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */