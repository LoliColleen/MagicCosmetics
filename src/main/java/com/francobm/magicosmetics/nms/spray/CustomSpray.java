/*    */ package com.francobm.magicosmetics.nms.spray;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.bukkit.entity.ItemFrame;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public abstract class CustomSpray {
/* 11 */   public static Map<UUID, CustomSpray> customSprays = new ConcurrentHashMap<>();
/*    */   protected UUID uuid;
/*    */   protected List<UUID> players;
/*    */   protected ItemFrame entity;
/*    */   protected boolean preview;
/*    */   
/*    */   public static void updateSpray(Player player) {
/* 18 */     for (CustomSpray spray : customSprays.values()) {
/* 19 */       spray.remove(player);
/* 20 */       spray.spawn(player);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void removeSpray(Player player) {
/* 25 */     for (CustomSpray spray : customSprays.values()) {
/* 26 */       if (!spray.players.contains(player.getUniqueId()))
/* 27 */         continue;  spray.remove(player);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void spawn(Player paramPlayer);
/*    */   
/*    */   public abstract void spawn(boolean paramBoolean);
/*    */   
/*    */   public abstract void remove();
/*    */   
/*    */   public abstract void remove(Player paramPlayer);
/*    */   
/*    */   public ItemFrame getEntity() {
/* 40 */     return this.entity;
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 44 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public List<UUID> getPlayers() {
/* 48 */     return this.players;
/*    */   }
/*    */   
/*    */   public void setPreview(boolean preview) {
/* 52 */     this.preview = preview;
/*    */   }
/*    */   
/*    */   public boolean isPreview() {
/* 56 */     return this.preview;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\spray\CustomSpray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */