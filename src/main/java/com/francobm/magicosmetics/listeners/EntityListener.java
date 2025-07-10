/*    */ package com.francobm.magicosmetics.listeners;
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import java.util.Optional;
/*    */ import org.bukkit.entity.ArmorStand;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Item;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.entity.EntityUnleashEvent;
/*    */ import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ 
/*    */ public class EntityListener implements Listener {
/* 16 */   private MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   
/*    */   @EventHandler
/*    */   public void onInteractArmorStand(PlayerArmorStandManipulateEvent event) {
/* 20 */     ArmorStand armorStand = event.getRightClicked();
/* 21 */     if (!armorStand.hasMetadata("cosmetics"))
/* 22 */       return;  event.setCancelled(true);
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void EntityUnleash(EntityUnleashEvent event) {
/* 27 */     if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
/* 28 */       return;  if (!event.getEntity().hasMetadata("cosmetics"))
/* 29 */       return;  LivingEntity livingEntity = (LivingEntity)event.getEntity();
/* 30 */     if (!(livingEntity.getLeashHolder() instanceof Player))
/* 31 */       return;  Player player = (Player)livingEntity.getLeashHolder();
/* 32 */     livingEntity.setLeashHolder(null);
/* 33 */     this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, task -> {
/*    */           livingEntity.setLeashHolder((Entity)player);
/*    */           Optional<Item> lead = livingEntity.getNearbyEntities(15.0D, 15.0D, 15.0D).stream().filter(()).map(()).filter(()).findFirst();
/*    */           if (!lead.isPresent()) {
/*    */             task.cancel();
/*    */             return;
/*    */           } 
/*    */           ((Item)lead.get()).remove();
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\EntityListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */