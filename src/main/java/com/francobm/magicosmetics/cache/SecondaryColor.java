/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ import org.bukkit.Color;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class SecondaryColor {
/*    */   private final Color color;
/*    */   private final String permission;
/*    */   
/*    */   public SecondaryColor(Color color) {
/* 11 */     this(color, "");
/*    */   }
/*    */   public SecondaryColor(Color color, String permission) {
/* 14 */     this.color = color;
/* 15 */     this.permission = permission;
/*    */   }
/*    */   
/*    */   public Color getColor() {
/* 19 */     return this.color;
/*    */   }
/*    */   
/*    */   public String getPermission() {
/* 23 */     return this.permission;
/*    */   }
/*    */   
/*    */   public boolean hasPermission(Player player) {
/* 27 */     if (this.permission.isEmpty()) return true; 
/* 28 */     return player.hasPermission(this.permission);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\SecondaryColor.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */