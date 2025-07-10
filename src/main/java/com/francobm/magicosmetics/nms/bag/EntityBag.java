/*    */ package com.francobm.magicosmetics.nms.bag;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public abstract class EntityBag {
/* 11 */   public static Map<UUID, EntityBag> entityBags = new ConcurrentHashMap<>();
/*    */   
/*    */   protected UUID uuid;
/*    */   protected Entity entity;
/*    */   protected List<UUID> players;
/*    */   
/*    */   public static void updateEntityBag(Player player) {
/* 18 */     for (EntityBag entityBag : entityBags.values()) {
/* 19 */       entityBag.remove(player);
/* 20 */       entityBag.spawnBag(player);
/*    */     } 
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
/*    */   public static void removeEntityBag(Player player) {
/* 33 */     for (EntityBag entityBag : entityBags.values()) {
/* 34 */       if (!entityBag.players.contains(player.getUniqueId()))
/* 35 */         continue;  entityBag.remove(player);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void spawnBag(Player paramPlayer);
/*    */   
/*    */   public abstract void spawnBag();
/*    */   
/*    */   public abstract void remove();
/*    */   
/*    */   public abstract void remove(Player paramPlayer);
/*    */   
/*    */   public abstract void addPassenger();
/*    */   
/*    */   public abstract void addPassenger(Entity paramEntity1, Entity paramEntity2);
/*    */   
/*    */   public abstract void addPassenger(Player paramPlayer, Entity paramEntity1, Entity paramEntity2);
/*    */   
/*    */   public abstract void setItemOnHelmet(ItemStack paramItemStack);
/*    */   
/*    */   public abstract void lookEntity();
/*    */   
/*    */   public UUID getUuid() {
/* 58 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public List<UUID> getPlayers() {
/* 62 */     return this.players;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 66 */     return this.entity;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\bag\EntityBag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */