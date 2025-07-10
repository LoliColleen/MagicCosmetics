/*    */ package com.francobm.magicosmetics.listeners;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.ZoneAction;
/*    */ import com.francobm.magicosmetics.events.ZoneEnterEvent;
/*    */ import com.francobm.magicosmetics.events.ZoneExitEvent;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ 
/*    */ public class ZoneListener implements Listener {
/* 12 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   @EventHandler
/*    */   public void onEnterZone(ZoneEnterEvent event) {
/* 15 */     Player player = event.getPlayer();
/* 16 */     ZoneAction onEnterAction = this.plugin.getZoneActions().getOnEnter();
/* 17 */     if (onEnterAction == null)
/* 18 */       return;  onEnterAction.executeCommands(player, event.getZone().getId());
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onExitZone(ZoneExitEvent event) {
/* 23 */     Player player = event.getPlayer();
/* 24 */     ZoneAction onExitAction = this.plugin.getZoneActions().getOnExit();
/* 25 */     if (onExitAction == null)
/* 26 */       return;  onExitAction.executeCommands(player, event.getZone().getId());
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\ZoneListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */