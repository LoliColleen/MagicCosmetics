/*    */ package com.francobm.magicosmetics.velocity.listeners;
/*    */ 
/*    */ import com.francobm.magicosmetics.velocity.MagicCosmetics;
/*    */ import com.velocitypowered.api.event.Subscribe;
/*    */ import com.velocitypowered.api.event.connection.PluginMessageEvent;
/*    */ 
/*    */ 
/*    */ public class PlayerListener
/*    */ {
/*    */   private final MagicCosmetics plugin;
/*    */   
/*    */   public PlayerListener(MagicCosmetics plugin) {
/* 13 */     this.plugin = plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onPluginMessage(PluginMessageEvent event) {
/* 34 */     this.plugin.executePluginMessage(event.getIdentifier().getId(), event.getData());
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\velocity\listeners\PlayerListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */