/*     */ package com.francobm.magicosmetics.provider;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.clip.placeholderapi.expansion.PlaceholderExpansion;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class PlaceholderAPI
/*     */   extends PlaceholderExpansion {
/*  17 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  18 */   private final Pattern pattern = Pattern.compile("\\{([^}]*?)}");
/*     */   
/*     */   public PlaceholderAPI() {
/*  21 */     register();
/*     */   }
/*     */   
/*     */   public List<String> setPlaceholders(Player player, List<String> message) {
/*  25 */     return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
/*     */   }
/*     */   
/*     */   public String setPlaceholders(Player player, String message) {
/*  29 */     return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean persist() {
/*  34 */     return true;
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
/*     */   public boolean canRegister() {
/*  46 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthor() {
/*  56 */     return "FrancoBM";
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
/*     */   public String getIdentifier() {
/*  70 */     return "magicosmetics";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  81 */     return this.plugin.getDescription().getVersion();
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
/*     */   public String onRequest(OfflinePlayer player, String identifier) {
/* 100 */     if (player == null || !player.isOnline() || player.getPlayer() == null) {
/* 101 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 105 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player.getPlayer());
/*     */ 
/*     */     
/* 108 */     if (identifier.equals("get_zone")) {
/* 109 */       if (playerData.getZone() == null) {
/* 110 */         return "";
/*     */       }
/* 112 */       return playerData.getZone().getId();
/*     */     } 
/*     */     
/* 115 */     if (identifier.startsWith("get_")) {
/* 116 */       Matcher matcher = this.pattern.matcher(identifier);
/* 117 */       String id = "";
/* 118 */       if (matcher.find()) {
/* 119 */         id = matcher.group(1);
/*     */       }
/* 121 */       if (this.plugin.isPermissions()) {
/* 122 */         return String.valueOf(Cosmetic.getCosmetic(id).hasPermission(player.getPlayer()));
/*     */       }
/* 124 */       return String.valueOf((playerData.getCosmeticById(id) != null));
/*     */     } 
/*     */     
/* 127 */     if (identifier.equals("equipped_count")) {
/* 128 */       return String.valueOf(playerData.getEquippedCount());
/*     */     }
/*     */     
/* 131 */     if (identifier.startsWith("equipped_")) {
/* 132 */       Matcher matcher = this.pattern.matcher(identifier);
/* 133 */       String id = "";
/* 134 */       if (matcher.find()) {
/* 135 */         id = matcher.group(1);
/*     */       }
/*     */       try {
/* 138 */         CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
/* 139 */         Cosmetic cosmetic = playerData.getEquip(cosmeticType);
/*     */         
/* 141 */         if ((identifier.split("_")).length > 2) {
/* 142 */           if (cosmetic == null) return null; 
/* 143 */           String subId = identifier.split("_")[2];
/* 144 */           if (subId == null || subId.isEmpty()) {
/* 145 */             return null;
/*     */           }
/* 147 */           Color color = cosmetic.getColor();
/* 148 */           switch (subId.toLowerCase()) {
/*     */             case "id":
/* 150 */               return cosmetic.getId();
/*     */             case "material":
/* 152 */               return cosmetic.getItemStack().getType().name();
/*     */             case "modeldata":
/* 154 */               return String.valueOf(cosmetic.getItemStack().getItemMeta().getCustomModelData());
/*     */             case "hex":
/* 156 */               if (color == null) return null; 
/* 157 */               return String.format("#%02X%02X%02X", new Object[] { Integer.valueOf(color.getRed()), Integer.valueOf(color.getGreen()), Integer.valueOf(color.getBlue()) });
/*     */             case "r":
/* 159 */               if (color == null) return null; 
/* 160 */               return String.valueOf(color.getRed());
/*     */             case "g":
/* 162 */               if (color == null) return null; 
/* 163 */               return String.valueOf(color.getGreen());
/*     */             case "b":
/* 165 */               if (color == null) return null; 
/* 166 */               return String.valueOf(color.getBlue());
/*     */           } 
/* 168 */           return null;
/*     */         } 
/* 170 */         return String.valueOf((cosmetic != null));
/* 171 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */         
/* 173 */         return String.valueOf((playerData.getEquip(id) != null));
/*     */       } 
/*     */     } 
/* 176 */     if (identifier.startsWith("using_")) {
/* 177 */       Matcher matcher = this.pattern.matcher(identifier);
/* 178 */       String id = "";
/* 179 */       if (matcher.find()) {
/* 180 */         id = matcher.group(1);
/*     */       }
/*     */       try {
/* 183 */         CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
/* 184 */         return String.valueOf((playerData.getEquip(cosmeticType) != null));
/* 185 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */         
/* 187 */         return String.valueOf((playerData.getEquip(id) != null));
/*     */       } 
/*     */     } 
/* 190 */     if (identifier.startsWith("player_available_")) {
/* 191 */       Matcher matcher = this.pattern.matcher(identifier);
/* 192 */       String id = "";
/* 193 */       if (matcher.find()) {
/* 194 */         id = matcher.group(1);
/*     */       }
/* 196 */       if (id.equalsIgnoreCase("all")) {
/* 197 */         if (this.plugin.isPermissions()) {
/* 198 */           return String.valueOf(playerData.getCosmeticsPerm().size());
/*     */         }
/* 200 */         return String.valueOf(playerData.getCosmetics().size());
/*     */       } 
/*     */       try {
/* 203 */         CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
/* 204 */         return String.valueOf(playerData.getCosmeticCount(cosmeticType));
/* 205 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */         
/* 207 */         return null;
/*     */       } 
/*     */     } 
/* 210 */     if (identifier.startsWith("available_")) {
/* 211 */       Matcher matcher = this.pattern.matcher(identifier);
/* 212 */       String id = "";
/* 213 */       if (matcher.find()) {
/* 214 */         id = matcher.group(1);
/*     */       }
/* 216 */       if (id.equalsIgnoreCase("all")) {
/* 217 */         return String.valueOf(Cosmetic.cosmetics.size());
/*     */       }
/*     */       try {
/* 220 */         CosmeticType cosmeticType = CosmeticType.valueOf(id.toUpperCase());
/* 221 */         return String.valueOf(Cosmetic.getCosmeticCount(cosmeticType));
/* 222 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */         
/* 224 */         return null;
/*     */       } 
/*     */     } 
/* 227 */     if (identifier.equals("in_zone")) {
/* 228 */       return String.valueOf(playerData.isZone());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 233 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\PlaceholderAPI.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */