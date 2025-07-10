/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ public class PlayerChangeBlacklistEvent extends PlayerEvent {
/*  8 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private final boolean isInWorldBlacklist;
/*    */   
/*    */   public PlayerChangeBlacklistEvent(Player player, boolean isInWorldBlacklist) {
/* 12 */     super(player);
/* 13 */     this.isInWorldBlacklist = isInWorldBlacklist;
/*    */   }
/*    */   
/*    */   public boolean isInWorldBlacklist() {
/* 17 */     return this.isInWorldBlacklist;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 22 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 26 */     return HANDLER_LIST;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\PlayerChangeBlacklistEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */