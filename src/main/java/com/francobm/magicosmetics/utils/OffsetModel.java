/*    */ package com.francobm.magicosmetics.utils;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class OffsetModel {
/*    */   private final double x;
/*    */   private final double y;
/*    */   private final double z;
/*    */   private final float yaw;
/*    */   private final float pitch;
/*    */   
/*    */   public OffsetModel(Location location, float yaw, float pitch) {
/* 14 */     this(location.getX(), location.getY(), location.getZ(), yaw, pitch);
/*    */   }
/*    */   
/*    */   public OffsetModel(double x, double y, double z, float yaw, float pitch) {
/* 18 */     this.x = x;
/* 19 */     this.y = y;
/* 20 */     this.z = z;
/* 21 */     this.yaw = yaw;
/* 22 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 26 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 30 */     return this.y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 34 */     return this.z;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 38 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 42 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public Vector getBukkitVector() {
/* 46 */     return new Vector(this.x, this.y, this.z);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\OffsetModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */