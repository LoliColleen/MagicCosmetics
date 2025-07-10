package com.francobm.magicosmetics.commands;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.Token;
import com.francobm.magicosmetics.cache.Zone;
import com.francobm.magicosmetics.cache.inventories.Menu;
import com.francobm.magicosmetics.files.FileCreator;
import com.francobm.magicosmetics.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor, TabCompleter {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
    FileCreator messages = this.plugin.getMessages();
    if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
      if (args.length >= 1) {
        Player target;
        PlayerData playerData;
        switch (args[0].toLowerCase()) {
          case "addall":
            if (args.length < 2) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().addAllCosmetics(sender, target);
            return true;
          case "add":
            if (args.length < 3) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().addCosmetic(sender, target, args[2]);
            return true;
          case "remove":
            if (args.length < 3) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().removeCosmetic(sender, target, args[2]);
            return true;
          case "removeall":
            if (args.length < 2) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().removeAllCosmetics(sender, target);
            return true;
          case "reload":
            this.plugin.getCosmeticsManager().reload(sender);
            return true;
          case "toggle":
            if (args.length < 2)
              return true; 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            playerData = PlayerData.getPlayer((OfflinePlayer)target);
            playerData.toggleHiddeCosmetics();
            return true;
          case "equip":
            if (args.length < 3) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            if (args.length == 4) {
              if (!args[3].startsWith("#"))
                this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], null, false); 
              this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], args[3], false);
              return true;
            } 
            if (args.length == 5) {
              this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], args[3], Boolean.parseBoolean(args[4]));
              return true;
            } 
            this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], null, false);
            return true;
          case "unequip":
            if (args.length < 3)
              return true; 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            if (args[2].equalsIgnoreCase("all")) {
              this.plugin.getCosmeticsManager().unEquipAll(sender, target);
              return true;
            } 
            this.plugin.getCosmeticsManager().unSetCosmetic(target, args[2]);
            return true;
          case "open":
            if (args.length < 3) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[2]);
            if (target == null) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().openMenu(target, args[1]);
            return true;
          case "token":
            if (args.length < 3) {
              Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            if (args[1].equalsIgnoreCase("give")) {
              target = Bukkit.getPlayer(args[2]);
              if (target == null) {
                Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
                return true;
              } 
              this.plugin.getCosmeticsManager().giveToken(sender, target, args[3]);
              return true;
            } 
            return true;
        } 
        Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
        return true;
      } 
      return true;
    } 
    if (sender instanceof Player) {
      Player player = (Player)sender;
      if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
        Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
        return true;
      } 
      if (args.length >= 1) {
        Player target, p;
        PlayerData playerData;
        switch (args[0].toLowerCase()) {
          case "test":
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            player.addPassenger((Entity)target);
            return true;
          case "unlock":
            if (args.length < 2)
              return true; 
            p = Bukkit.getPlayer(args[1]);
            if (p == null)
              return true; 
            playerData = PlayerData.getPlayer((OfflinePlayer)p);
            playerData.setZone(false);
            return true;
          case "addall":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().addAllCosmetics((CommandSender)player, target);
            return true;
          case "add":
            if (args.length < 3) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().addCosmetic((CommandSender)player, target, args[2]);
            return true;
          case "remove":
            if (args.length < 3) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().removeCosmetic((CommandSender)player, target, args[2]);
            return true;
          case "removeall":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().removeAllCosmetics((CommandSender)player, target);
            return true;
          case "reload":
            this.plugin.getCosmeticsManager().reload(sender);
            return true;
          case "use":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            if (args.length == 3) {
              this.plugin.getCosmeticsManager().equipCosmetic(player, args[1], args[2], false);
              return true;
            } 
            this.plugin.getCosmeticsManager().equipCosmetic(player, args[1], null, false);
            return true;
          case "preview":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().previewCosmetic(player, args[1]);
            return true;
          case "unuse":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().unUseCosmetic(player, args[1]);
            return true;
          case "unset":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().unSetCosmetic(player, args[1]);
            return true;
          case "unequip":
            if (args.length < 2)
              return true; 
            if (args[1].equalsIgnoreCase("all")) {
              this.plugin.getCosmeticsManager().unEquipAll(player);
              return true;
            } 
            this.plugin.getCosmeticsManager().unSetCosmetic(player, args[1]);
            return true;
          case "open":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().openMenu(player, args[1]);
            return true;
          case "spec":
            this.plugin.getVersion().setSpectator(player);
            return true;
          case "spawn":
            if (this.plugin.getVersion().getNPC(player) == null) {
              this.plugin.getVersion().createNPC(player);
              return true;
            } 
            this.plugin.getVersion().removeNPC(player);
            return true;
          case "hide":
            this.plugin.getCosmeticsManager().hideSelfCosmetic(player, CosmeticType.BAG);
            return true;
          case "toggle":
            playerData = PlayerData.getPlayer((OfflinePlayer)player);
            playerData.toggleHiddeCosmetics();
            return true;
          case "zones":
            if (args.length < 2) {
              for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                Utils.sendMessage((CommandSender)player, msg); 
              return true;
            } 
            if (args[1].equalsIgnoreCase("add")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().addZone(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("remove")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().removeZone(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("setnpc")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().setZoneNPC(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("setballoon")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().setBalloonNPC(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("setspray")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().setSpray(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("setenter")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().setZoneEnter(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("setexit")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().setZoneExit(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("givecorns")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().giveCorn(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("enable")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().enableZone(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("disable")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().disableZone(player, args[2]);
              return true;
            } 
            if (args[1].equalsIgnoreCase("save")) {
              if (args.length < 3) {
                for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage"))
                  Utils.sendMessage((CommandSender)player, msg); 
                return true;
              } 
              this.plugin.getZonesManager().saveZone(player, args[2]);
              return true;
            } 
            return true;
          case "token":
            if (args.length < 4) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            if (args[1].equalsIgnoreCase("give")) {
              target = Bukkit.getPlayer(args[2]);
              if (target == null) {
                Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
                return true;
              } 
              this.plugin.getCosmeticsManager().giveToken((CommandSender)player, target, args[3]);
              return true;
            } 
            return true;
          case "check":
            this.plugin.getCosmeticsManager().sendCheck(player);
            return true;
          case "npc":
            if (!this.plugin.isCitizens()) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + "&cCitizens is not installed!");
              return true;
            } 
            if (args.length == 2 && args[1].equalsIgnoreCase("save")) {
              this.plugin.getNPCsLoader().save();
              return true;
            } 
            if (args.length < 3) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            try {
              this.plugin.getCitizens().equipCosmetic((CommandSender)player, args[1], args[2], args[3]);
            } catch (ArrayIndexOutOfBoundsException exception) {
              this.plugin.getCitizens().equipCosmetic((CommandSender)player, args[1], args[2], null);
            } 
            return true;
          case "tint":
            if (args.length < 2) {
              Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
              return true;
            } 
            this.plugin.getCosmeticsManager().tintItem(player, args[1]);
            return true;
        } 
        Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
        return true;
      } 
      if (player.hasPermission("magicosmetics.cosmetics.use")) {
        this.plugin.getCosmeticsManager().openMenu(player, this.plugin.getMainMenu());
        if (this.plugin.getOnExecuteCosmetics().isEmpty())
          return true; 
        player.performCommand(this.plugin.getOnExecuteCosmetics());
      } 
      return true;
    } 
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
    List<String> arguments = new ArrayList<>();
    if (sender.hasPermission("magicosmetics.cosmetics")) {
      arguments.add("add");
      arguments.add("remove");
      arguments.add("addAll");
      if (this.plugin.isCitizens())
        arguments.add("npc"); 
    } 
    if (sender.hasPermission("magicosmetics.menus"))
      arguments.add("open"); 
    if (sender.hasPermission("magicosmetics.zones"))
      arguments.add("zones"); 
    if (sender.hasPermission("magicosmetics.tokens"))
      arguments.add("token"); 
    if (sender.hasPermission("magicosmetics.reload"))
      arguments.add("reload"); 
    if (sender.hasPermission("magicosmetics.hide"))
      arguments.add("hide"); 
    if (sender.hasPermission("magicosmetics.toggle"))
      arguments.add("toggle"); 
    if (sender.hasPermission("magicosmetics.equip")) {
      arguments.add("use");
      arguments.add("unequip");
    } 
    if (sender.hasPermission("magicosmetics.tint"))
      arguments.add("tint"); 
    if (arguments.size() == 0)
      return arguments; 
    List<String> result = new ArrayList<>();
    switch (args.length) {
      case 1:
        for (String a : arguments) {
          if (a.toLowerCase().startsWith(args[0].toLowerCase()))
            result.add(a); 
        } 
        return result;
      case 2:
        switch (args[0].toLowerCase()) {
          case "hide":
          case "toggle":
          case "add":
          case "addall":
          case "remove":
            return null;
          case "npc":
            if (!this.plugin.isCitizens())
              return null; 
            result.add("save");
            result.addAll(this.plugin.getCitizens().getNPCs());
            return result;
          case "unequip":
          case "use":
            if (!sender.hasPermission("magicosmetics.equip"))
              return null; 
            result.add("all");
            result.addAll(Cosmetic.cosmetics.keySet());
            return result;
          case "open":
            if (!sender.hasPermission("magicosmetics.menus"))
              return null; 
            result.addAll(Menu.inventories.keySet());
            return result;
          case "zones":
            if (!sender.hasPermission("magicosmetics.zones"))
              return null; 
            result.add("add");
            result.add("remove");
            result.add("setNPC");
            result.add("setBalloon");
            result.add("setSpray");
            result.add("setEnter");
            result.add("setExit");
            result.add("giveCorns");
            result.add("enable");
            result.add("disable");
            result.add("save");
            return result;
          case "token":
            if (!sender.hasPermission("magicosmetics.tokens"))
              return null; 
            result.add("give");
            return result;
          case "tint":
            if (!sender.hasPermission("magicosmetics.tint"))
              return null; 
            result.add("#FFFFFF");
            return result;
        } 
      case 3:
        switch (args[0].toLowerCase()) {
          case "add":
          case "remove":
          case "npc":
            if (!sender.hasPermission("magicosmetics.cosmetics"))
              return null; 
            result.addAll(Cosmetic.cosmetics.keySet());
            return result;
          case "use":
          case "equip":
            if (!sender.hasPermission("magicosmetics.equip"))
              return null; 
            result.add("#FFFFFF");
            result.add("null");
            return result;
          case "zones":
            if (!sender.hasPermission("magicosmetics.zones"))
              return null; 
            if (args[1].equalsIgnoreCase("add"))
              return new ArrayList<>(); 
            result.addAll(Zone.zones.keySet());
            return result;
          case "token":
            return null;
        } 
      case 4:
        if (args[0].equalsIgnoreCase("token") && args[1].equalsIgnoreCase("give")) {
          if (!sender.hasPermission("magicosmetics.tokens"))
            return null; 
          result.addAll(Token.tokens.keySet());
          return result;
        } 
        if (args[0].equalsIgnoreCase("npc")) {
          if (!this.plugin.isCitizens())
            return null; 
          if (!sender.hasPermission("magicosmetics.cosmetics"))
            return null; 
          result.add("#FFFFFF");
          return result;
        } 
        break;
    } 
    return null;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\commands\Command.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */