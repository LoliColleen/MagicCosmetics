/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import java.util.List;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ZoneAction {
/* 10 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   private final String id;
/*    */   private List<String> commands;
/*    */   
/*    */   public ZoneAction(String id, List<String> commands) {
/* 15 */     this.id = id;
/* 16 */     this.commands = commands;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 20 */     return this.id;
/*    */   }
/*    */   
/*    */   public List<String> getCommands() {
/* 24 */     return this.commands;
/*    */   }
/*    */   
/*    */   public void setCommands(List<String> commands) {
/* 28 */     this.commands = commands;
/*    */   }
/*    */   
/*    */   public void executeCommands(Player player, String zoneId) {
/* 32 */     for (String command : this.commands) {
/* 33 */       if (this.plugin.isPlaceholderAPI()) {
/* 34 */         command = this.plugin.getPlaceholderAPI().setPlaceholders(player, command.replace("%player%", player.getName()).replace("%zone%", zoneId));
/*    */       } else {
/* 36 */         command = command.replace("%player%", player.getName()).replace("%zone%", zoneId);
/* 37 */       }  if (command.startsWith("[console] ")) {
/* 38 */         Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command.replace("[console] ", ""));
/*    */         continue;
/*    */       } 
/* 41 */       if (command.startsWith("[player] ")) {
/* 42 */         player.performCommand(command.replace("[player] ", ""));
/*    */         continue;
/*    */       } 
/* 45 */       player.performCommand(command);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\ZoneAction.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */