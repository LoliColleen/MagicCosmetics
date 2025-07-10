package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  private final Pattern pattern = Pattern.compile("\\{([^}]*?)}");
  
  public PlaceholderAPI() {
    register();
  }
  
  public List<String> setPlaceholders(Player player, List<String> message) {
    return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
  }
  
  public String setPlaceholders(Player player, String message) {
    return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
  }
  
  public boolean persist() {
    return true;
  }
  
  public boolean canRegister() {
    return true;
  }
  
  public String getAuthor() {
    return "FrancoBM";
  }
  
  public String getIdentifier() {
    return "magicosmetics";
  }
  
  public String getVersion() {
    return this.plugin.getDescription().getVersion();
  }
  
  public String onRequest(OfflinePlayer player, String identifier) {
    if (player == null || !player.isOnline() || player.getPlayer() == null)
      return null; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player.getPlayer());
    if (identifier.equals("get_zone")) {
      if (playerData.getZone() == null)
        return ""; 
      return playerData.getZone().getId();
    } 
    if (identifier.startsWith("get_")) {
      Matcher matcher = this.pattern.matcher(identifier);
      String id = "";
      if (matcher.find())
        id = matcher.group(1); 
      if (this.plugin.isPermissions())
        return String.valueOf(Cosmetic.getCosmetic(id).hasPermission(player.getPlayer())); 
      return String.valueOf((playerData.getCosmeticById(id) != null));
    } 
    if (identifier.equals("equipped_count"))
      return String.valueOf(playerData.getEquippedCount()); 
    if (identifier.startsWith("equipped_")) {
      Matcher matcher = this.pattern.matcher(identifier);
      String id = "";
      if (matcher.find())
        id = matcher.group(1); 
      try {
        CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
        Cosmetic cosmetic = playerData.getEquip(cosmeticType);
        if ((identifier.split("_")).length > 2) {
          if (cosmetic == null)
            return null; 
          String subId = identifier.split("_")[2];
          if (subId == null || subId.isEmpty())
            return null; 
          Color color = cosmetic.getColor();
          switch (subId.toLowerCase()) {
            case "id":
              return cosmetic.getId();
            case "material":
              return cosmetic.getItemStack().getType().name();
            case "modeldata":
              return String.valueOf(cosmetic.getItemStack().getItemMeta().getCustomModelData());
            case "hex":
              if (color == null)
                return null; 
              return String.format("#%02X%02X%02X", new Object[] { Integer.valueOf(color.getRed()), Integer.valueOf(color.getGreen()), Integer.valueOf(color.getBlue()) });
            case "r":
              if (color == null)
                return null; 
              return String.valueOf(color.getRed());
            case "g":
              if (color == null)
                return null; 
              return String.valueOf(color.getGreen());
            case "b":
              if (color == null)
                return null; 
              return String.valueOf(color.getBlue());
          } 
          return null;
        } 
        return String.valueOf((cosmetic != null));
      } catch (IllegalArgumentException illegalArgumentException) {
        return String.valueOf((playerData.getEquip(id) != null));
      } 
    } 
    if (identifier.startsWith("using_")) {
      Matcher matcher = this.pattern.matcher(identifier);
      String id = "";
      if (matcher.find())
        id = matcher.group(1); 
      try {
        CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
        return String.valueOf((playerData.getEquip(cosmeticType) != null));
      } catch (IllegalArgumentException illegalArgumentException) {
        return String.valueOf((playerData.getEquip(id) != null));
      } 
    } 
    if (identifier.startsWith("player_available_")) {
      Matcher matcher = this.pattern.matcher(identifier);
      String id = "";
      if (matcher.find())
        id = matcher.group(1); 
      if (id.equalsIgnoreCase("all")) {
        if (this.plugin.isPermissions())
          return String.valueOf(playerData.getCosmeticsPerm().size()); 
        return String.valueOf(playerData.getCosmetics().size());
      } 
      try {
        CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
        return String.valueOf(playerData.getCosmeticCount(cosmeticType));
      } catch (IllegalArgumentException illegalArgumentException) {
        return null;
      } 
    } 
    if (identifier.startsWith("available_")) {
      Matcher matcher = this.pattern.matcher(identifier);
      String id = "";
      if (matcher.find())
        id = matcher.group(1); 
      if (id.equalsIgnoreCase("all"))
        return String.valueOf(Cosmetic.cosmetics.size()); 
      try {
        CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
        return String.valueOf(Cosmetic.getCosmeticCount(cosmeticType));
      } catch (IllegalArgumentException illegalArgumentException) {
        return null;
      } 
    } 
    if (identifier.equals("in_zone"))
      return String.valueOf(playerData.isZone()); 
    return null;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\PlaceholderAPI.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */