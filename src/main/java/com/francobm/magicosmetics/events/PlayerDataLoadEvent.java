/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.api.Cosmetic;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import java.util.Set;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerDataLoadEvent
/*    */   extends Event
/*    */ {
/* 15 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private final PlayerData playerData;
/*    */   private final Set<Cosmetic> equippedCosmetics;
/*    */   
/*    */   public PlayerDataLoadEvent(PlayerData playerData, Set<Cosmetic> equippedCosmetics) {
/* 20 */     this.playerData = playerData;
/* 21 */     this.equippedCosmetics = equippedCosmetics;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 26 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 30 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public PlayerData getPlayerData() {
/* 34 */     return this.playerData;
/*    */   }
/*    */   
/*    */   public Set<Cosmetic> getEquippedCosmetics() {
/* 38 */     return this.equippedCosmetics;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\PlayerDataLoadEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */