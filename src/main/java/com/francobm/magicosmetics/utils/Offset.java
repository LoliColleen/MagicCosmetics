/*    */ package com.francobm.magicosmetics.utils;
/*    */ 
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class Offset
/*    */ {
/*    */   public static Vector rotateYaw(Vector vec, double yaw) {
/*  8 */     double sin = Math.sin(yaw);
/*  9 */     double cos = Math.cos(yaw);
/* 10 */     double x = vec.getX() * cos - vec.getZ() * sin;
/* 11 */     double z = vec.getX() * sin + vec.getZ() * cos;
/* 12 */     return vec.setX(x).setZ(z);
/*    */   }
/*    */   
/*    */   public static Vector rotatePitch(Vector vec, double pitch) {
/* 16 */     double sin = Math.sin(pitch);
/* 17 */     double cos = Math.cos(pitch);
/* 18 */     double y = vec.getY() * cos - vec.getZ() * sin;
/* 19 */     double z = vec.getY() * sin + vec.getZ() * cos;
/* 20 */     return vec.setY(y).setZ(z);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\Offset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */