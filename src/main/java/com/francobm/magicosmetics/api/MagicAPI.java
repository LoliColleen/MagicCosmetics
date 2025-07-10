/*     */ package com.francobm.magicosmetics.api;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.EntityCache;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class MagicAPI {
/*     */   public static boolean hasCosmetic(Player player, String cosmeticId) {
/*  19 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  20 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  21 */     if (plugin.isPermissions()) {
/*  22 */       return Cosmetic.getCosmetic(cosmeticId).hasPermission(player);
/*     */     }
/*  24 */     Cosmetic cosmetic = playerData.getCosmeticById(cosmeticId);
/*  25 */     return (cosmetic != null);
/*     */   }
/*     */   
/*     */   public static boolean spray(Player player) {
/*  29 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  30 */     if (playerData.getSpray() == null) return false; 
/*  31 */     playerData.draw(SprayKeys.API);
/*  32 */     return true;
/*     */   }
/*     */   
/*     */   public static Set<Cosmetic> getCosmetics() {
/*  36 */     return new HashSet<>(Cosmetic.cosmetics.values());
/*     */   }
/*     */   
/*     */   public static Set<Cosmetic> getCosmeticsByType(CosmeticType cosmeticType) {
/*  40 */     return Cosmetic.getCosmeticsByType(cosmeticType);
/*     */   }
/*     */   
/*     */   public static Set<Cosmetic> getCosmeticsHideByType(CosmeticType cosmeticType) {
/*  44 */     return Cosmetic.getSetCosmeticsHideByType(cosmeticType);
/*     */   }
/*     */   
/*     */   public static boolean tintItem(ItemStack item, String colorHex) {
/*  48 */     return MagicCosmetics.getInstance().getCosmeticsManager().tintItem(item, colorHex);
/*     */   }
/*     */   
/*     */   public static boolean hasEquipCosmetic(Entity entity, String cosmeticId) {
/*  52 */     EntityCache entityCache = EntityCache.getEntity(entity.getUniqueId());
/*  53 */     if (entityCache == null) return false; 
/*  54 */     return entityCache.hasEquipped(cosmeticId);
/*     */   }
/*     */   
/*     */   public static boolean hasEquipCosmetic(Player player, CosmeticType cosmeticType) {
/*  58 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  59 */     Cosmetic cosmetic = playerData.getEquip(cosmeticType);
/*  60 */     return (cosmetic != null);
/*     */   }
/*     */   
/*     */   public static void EquipCosmetic(Player player, String cosmeticId, String color, boolean force) {
/*  64 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  65 */     plugin.getCosmeticsManager().equipCosmetic(player, cosmeticId, color, force);
/*     */   }
/*     */   
/*     */   public static void EquipCosmetic(Entity entity, String cosmeticId, String colorHex) {
/*  69 */     EntityCache entityCache = EntityCache.getEntityOrCreate(entity);
/*  70 */     Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
/*  71 */     if (cosmetic == null)
/*  72 */       return;  if (colorHex != null) {
/*  73 */       Color color = Utils.hex2Rgb(colorHex);
/*  74 */       cosmetic.setColor(color);
/*     */     } 
/*  76 */     entityCache.setCosmetic(cosmetic);
/*     */   }
/*     */   
/*     */   public static void EquipCosmetic(Entity entity, String cosmeticId, Color color) {
/*  80 */     EntityCache entityCache = EntityCache.getEntityOrCreate(entity);
/*  81 */     Cosmetic cosmetic = Cosmetic.getCloneCosmetic(cosmeticId);
/*  82 */     if (cosmetic == null)
/*  83 */       return;  if (color != null) {
/*  84 */       cosmetic.setColor(color);
/*     */     }
/*  86 */     entityCache.setCosmetic(cosmetic);
/*     */   }
/*     */   
/*     */   public static void UnEquipCosmetic(Player player, CosmeticType cosmeticType) {
/*  90 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  91 */     plugin.getCosmeticsManager().unSetCosmetic(player, cosmeticType);
/*     */   }
/*     */   
/*     */   public static void UnEquipCosmetic(Entity entity, CosmeticType cosmeticType) {
/*  95 */     EntityCache entityCache = EntityCache.getEntity(entity.getUniqueId());
/*  96 */     if (entityCache == null)
/*  97 */       return;  entityCache.unSetCosmetic(cosmeticType);
/*     */   }
/*     */   
/*     */   public static void RemoveEntityCosmetics(UUID entityUniqueId) {
/* 101 */     if (!EntityCache.entities.containsKey(entityUniqueId))
/* 102 */       return;  EntityCache entityCache = EntityCache.getEntity(entityUniqueId);
/* 103 */     if (entityCache == null)
/* 104 */       return;  entityCache.clearCosmeticsInUse();
/* 105 */     EntityCache.removeEntity(entityUniqueId);
/*     */   }
/*     */   
/*     */   public static ItemStack getCosmeticItem(String id) {
/* 109 */     Cosmetic cosmetic = Cosmetic.getCosmetic(id);
/* 110 */     if (cosmetic == null) return null; 
/* 111 */     return cosmetic.getItemColor();
/*     */   }
/*     */   
/*     */   public static String getCosmeticId(String name, String type) {
/* 115 */     Player player = Bukkit.getPlayerExact(name);
/* 116 */     if (player == null) return null; 
/* 117 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*     */     try {
/* 119 */       CosmeticType cosmeticType = CosmeticType.valueOf(type.toUpperCase());
/* 120 */       Cosmetic cosmetic = playerData.getEquip(cosmeticType);
/* 121 */       if (cosmetic == null) return null; 
/* 122 */       return cosmetic.getId();
/* 123 */     } catch (IllegalArgumentException ignored) {
/* 124 */       MagicCosmetics.getInstance().getLogger().warning("Invalid cosmetic type: " + type);
/*     */       
/* 126 */       return null;
/*     */     } 
/*     */   }
/*     */   public static ItemStack getEquipped(String name, String type) {
/* 130 */     Player player = Bukkit.getPlayerExact(name);
/* 131 */     if (player == null) return null; 
/* 132 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*     */     try {
/* 134 */       CosmeticType cosmeticType = CosmeticType.valueOf(type.toUpperCase());
/* 135 */       Cosmetic cosmetic = playerData.getEquip(cosmeticType);
/* 136 */       if (cosmetic == null) return null; 
/* 137 */       return cosmetic.getItemColor().clone();
/* 138 */     } catch (IllegalArgumentException ignored) {
/* 139 */       MagicCosmetics.getInstance().getLogger().warning("Invalid cosmetic type: " + type);
/*     */       
/* 141 */       return null;
/*     */     } 
/*     */   }
/*     */   public static ItemStack getEquipped(OfflinePlayer offlinePlayer, CosmeticType cosmeticType) {
/* 145 */     if (!offlinePlayer.hasPlayedBefore()) return null; 
/* 146 */     PlayerData playerData = PlayerData.getPlayer(offlinePlayer);
/* 147 */     if (playerData == null) {
/* 148 */       return null;
/*     */     }
/* 150 */     Cosmetic cosmetic = playerData.getEquip(cosmeticType);
/* 151 */     if (cosmetic == null) {
/* 152 */       return null;
/*     */     }
/* 154 */     return cosmetic.getItemColor().clone();
/*     */   }
/*     */   
/*     */   public static int getPlayerCosmeticsAvailable(Player player, CosmeticType cosmeticType) {
/* 158 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 159 */     return playerData.getCosmeticCount(cosmeticType);
/*     */   }
/*     */   
/*     */   public static int getPlayerAllCosmeticsAvailable(Player player) {
/* 163 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 164 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 165 */     if (plugin.isPermissions()) {
/* 166 */       return playerData.getCosmeticsPerm().size();
/*     */     }
/* 168 */     return playerData.getCosmetics().size();
/*     */   }
/*     */   
/*     */   public static int getServerCosmeticsAvailable(CosmeticType cosmeticType) {
/* 172 */     return Cosmetic.getCosmeticCount(cosmeticType);
/*     */   }
/*     */   
/*     */   public static int getServerAllCosmeticsAvailable() {
/* 176 */     return Cosmetic.cosmetics.size();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\api\MagicAPI.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */