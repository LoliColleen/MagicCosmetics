/*    */ package com.francobm.magicosmetics.nms.v1_16_R3.cache;
/*    */ 
/*    */ import com.francobm.magicosmetics.nms.IRangeManager;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*    */ import net.minecraft.server.v1_16_R3.PlayerChunkMap;
/*    */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class RangeManager
/*    */   implements IRangeManager {
/*    */   private final PlayerChunkMap.EntityTracker tracked;
/*    */   
/*    */   public RangeManager(PlayerChunkMap.EntityTracker tracked) {
/* 16 */     this.tracked = tracked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addPlayer(Player player) {
/* 21 */     this.tracked.trackedPlayers.add((((CraftPlayer)player).getHandle()).playerConnection.player);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removePlayer(Player player) {
/* 26 */     this.tracked.trackedPlayers.remove((((CraftPlayer)player).getHandle()).playerConnection.player);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Player> getPlayerInRange() {
/* 31 */     Set<Player> list = new HashSet<>();
/* 32 */     if (this.tracked == null) return list; 
/* 33 */     this.tracked.trackedPlayers.forEach(serverPlayerConnection -> list.add(serverPlayerConnection.getBukkitEntity()));
/* 34 */     return list;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\RangeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */