/*    */ package com.francobm.magicosmetics.provider;
/*    */ 
/*    */ import com.francobm.magiccrates.cache.PlayerData;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class MagicCrates
/*    */ {
/*    */   public boolean hasInCrate(Player player) {
/*  9 */     PlayerData playerData = com.francobm.magiccrates.MagicCrates.getInstance().getManager().getPlayerData(player);
/* 10 */     if (playerData == null) return false; 
/* 11 */     return playerData.isInCrate();
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\MagicCrates.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */