/*    */ package com.francobm.magicosmetics.velocity.cache;
/*    */ 
/*    */ import com.velocitypowered.api.proxy.Player;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class PlayerData
/*    */ {
/* 11 */   public static Map<UUID, PlayerData> players = new HashMap<>();
/*    */   
/*    */   private final UUID uniqueId;
/*    */   private final String name;
/*    */   private String cosmetics;
/*    */   private String cosmeticsInUse;
/*    */   private boolean firstJoin;
/*    */   
/*    */   public PlayerData(Player player, String cosmetics, String cosmeticsInUse) {
/* 20 */     this.uniqueId = player.getUniqueId();
/* 21 */     this.name = player.getUsername();
/* 22 */     this.cosmetics = cosmetics;
/* 23 */     this.cosmeticsInUse = cosmeticsInUse;
/* 24 */     this.firstJoin = true;
/*    */   }
/*    */   
/*    */   public PlayerData(Player player) {
/* 28 */     this(player, "", "");
/*    */   }
/*    */   
/*    */   public static void removePlayer(UUID uniqueId) {
/* 32 */     players.remove(uniqueId);
/*    */   }
/*    */   
/*    */   public static PlayerData getPlayer(Player player) {
/* 36 */     if (!players.containsKey(player.getUniqueId())) {
/* 37 */       PlayerData playerData = new PlayerData(player);
/* 38 */       players.put(player.getUniqueId(), playerData);
/* 39 */       return playerData;
/*    */     } 
/* 41 */     return players.get(player.getUniqueId());
/*    */   }
/*    */   
/*    */   public String getCosmeticsInUse() {
/* 45 */     return this.cosmeticsInUse;
/*    */   }
/*    */   
/*    */   public String getCosmetics() {
/* 49 */     return this.cosmetics;
/*    */   }
/*    */   
/*    */   public void setCosmetics(String cosmetics) {
/* 53 */     this.cosmetics = cosmetics;
/*    */   }
/*    */   
/*    */   public void setCosmeticsInUse(String cosmeticsInUse) {
/* 57 */     this.cosmeticsInUse = cosmeticsInUse;
/*    */   }
/*    */   
/*    */   public void setFirstJoin(boolean firstJoin) {
/* 61 */     this.firstJoin = firstJoin;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 65 */     return this.name;
/*    */   }
/*    */   
/*    */   public UUID getUniqueId() {
/* 69 */     return this.uniqueId;
/*    */   }
/*    */   
/*    */   public boolean isFirstJoin() {
/* 73 */     return this.firstJoin;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\velocity\cache\PlayerData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */