/*    */ package com.francobm.magicosmetics.provider;
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import com.sk89q.worldedit.bukkit.BukkitAdapter;
/*    */ import com.sk89q.worldedit.util.Location;
/*    */ import com.sk89q.worldguard.protection.ApplicableRegionSet;
/*    */ import com.sk89q.worldguard.protection.flags.Flag;
/*    */ import com.sk89q.worldguard.protection.flags.StateFlag;
/*    */ import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
/*    */ import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerMoveEvent;
/*    */ 
/*    */ public class WorldGuard implements Listener {
/*    */   private final MagicCosmetics plugin;
/*    */   
/*    */   public WorldGuard(MagicCosmetics plugin) {
/* 22 */     registerFlag();
/* 23 */     this.plugin = plugin;
/*    */   }
/*    */   private StateFlag customFlag;
/*    */   public void registerFlag() {
/* 27 */     FlagRegistry registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
/*    */     
/*    */     try {
/* 30 */       StateFlag flag = new StateFlag("cosmetics", true);
/* 31 */       registry.register((Flag)flag);
/* 32 */       this.customFlag = flag;
/* 33 */     } catch (FlagConflictException e) {
/*    */ 
/*    */       
/* 36 */       Flag<?> existing = registry.get("cosmetics");
/* 37 */       if (existing instanceof StateFlag) {
/* 38 */         this.customFlag = (StateFlag)existing;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerMove(PlayerMoveEvent event) {
/* 48 */     Player player = event.getPlayer();
/* 49 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 50 */     if (playerData.isHasInBlackList())
/* 51 */       return;  Location to = event.getTo();
/* 52 */     if (to == null)
/* 53 */       return;  Location location = BukkitAdapter.adapt(to);
/* 54 */     ApplicableRegionSet applicableRegionSet = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(location);
/* 55 */     StateFlag.State flagState = applicableRegionSet.queryState(null, new StateFlag[] { this.customFlag });
/* 56 */     if (flagState == null || flagState.equals(StateFlag.State.DENY)) {
/* 57 */       playerData.hideAllCosmetics();
/*    */       return;
/*    */     } 
/* 60 */     playerData.showAllCosmetics();
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\WorldGuard.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */