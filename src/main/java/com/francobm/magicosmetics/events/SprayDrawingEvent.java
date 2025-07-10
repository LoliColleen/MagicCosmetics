/*    */ package com.francobm.magicosmetics.events;
/*    */ 
/*    */ import com.francobm.magicosmetics.api.SprayKeys;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.player.PlayerEvent;
/*    */ 
/*    */ 
/*    */ public class SprayDrawingEvent
/*    */   extends PlayerEvent
/*    */   implements Cancellable
/*    */ {
/* 15 */   private static final HandlerList HANDLER_LIST = new HandlerList();
/*    */   private final SprayKeys key;
/*    */   private final Block sprayedBlock;
/*    */   private boolean isCancelled;
/*    */   
/*    */   public SprayDrawingEvent(Player player, Block sprayedBlock, SprayKeys key) {
/* 21 */     super(player);
/* 22 */     this.key = key;
/* 23 */     this.sprayedBlock = sprayedBlock;
/* 24 */     this.isCancelled = false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SprayKeys getKey() {
/* 33 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Block getSprayedBlock() {
/* 42 */     return this.sprayedBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 47 */     return HANDLER_LIST;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 51 */     return HANDLER_LIST;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 56 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 61 */     this.isCancelled = cancel;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\SprayDrawingEvent.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */