/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class UnknownEntityInteractEvent
/*    */   extends Event
/*    */   implements Cancellable
/*    */ {
/*    */   private final Player player;
/*    */   private final NPC unknownEntity;
/*    */   private final Action action;
/* 16 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private boolean isCancelled;
/*    */   
/*    */   public UnknownEntityInteractEvent(Player player, NPC unknownEntity, Action action) {
/* 20 */     this.player = player;
/* 21 */     this.unknownEntity = unknownEntity;
/* 22 */     this.action = action;
/* 23 */     this.isCancelled = false;
/*    */   }
/*    */   
/*    */   public UnknownEntityInteractEvent(boolean isAsync, Player player, NPC unknownEntity, Action action) {
/* 27 */     super(isAsync);
/* 28 */     this.player = player;
/* 29 */     this.unknownEntity = unknownEntity;
/* 30 */     this.action = action;
/* 31 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 36 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 40 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 44 */     return this.player;
/*    */   }
/*    */   
/*    */   public NPC getUnknownEntity() {
/* 48 */     return this.unknownEntity;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 52 */     return this.action;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 57 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 62 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\UnknownEntityInteractEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */