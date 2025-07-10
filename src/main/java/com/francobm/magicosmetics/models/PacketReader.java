/*    */ package com.francobm.magicosmetics.models;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public abstract class PacketReader
/*    */ {
/*    */   public abstract void injectPlayer(Player paramPlayer);
/*    */   
/*    */   public abstract void removePlayer(Player paramPlayer);
/*    */   
/*    */   protected Object getValue(Object instance, String name) {
/* 14 */     Object result = null;
/*    */     try {
/* 16 */       Field field = instance.getClass().getDeclaredField(name);
/* 17 */       field.setAccessible(true);
/* 18 */       result = field.get(instance);
/* 19 */       field.setAccessible(false);
/* 20 */     } catch (Exception exception) {
/* 21 */       exception.printStackTrace();
/*    */     } 
/* 23 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\models\PacketReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */