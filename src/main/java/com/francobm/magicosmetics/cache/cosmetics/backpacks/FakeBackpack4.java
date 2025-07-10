/*    */ package com.francobm.magicosmetics.cache.cosmetics.backpacks;
/*    */ 
/*    */ import com.francobm.magicosmetics.utils.Offset;
/*    */ import com.francobm.magicosmetics.utils.OffsetModel;
/*    */ import com.francobm.magicosmetics.utils.PositionModelType;
/*    */ import com.ticxo.modelengine.api.entity.BukkitEntity;
/*    */ import com.ticxo.modelengine.api.nms.entity.wrapper.BodyRotationController;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FakeBackpack4
/*    */   extends BukkitEntity
/*    */ {
/*    */   private final Vector offset;
/*    */   private final double yaw;
/*    */   private final double pitch;
/*    */   private final PositionModelType positionModelType;
/*    */   private BodyRotationController controller;
/*    */   
/*    */   public FakeBackpack4(Entity entity, OffsetModel offsetModel, PositionModelType positionModelType) {
/* 24 */     super(entity);
/* 25 */     this.offset = offsetModel.getBukkitVector();
/* 26 */     this.yaw = Math.toRadians(offsetModel.getYaw());
/* 27 */     this.pitch = Math.toRadians(offsetModel.getPitch());
/* 28 */     this.positionModelType = positionModelType;
/*    */   }
/*    */   
/*    */   public void wrapBodyRotationControl() {
/* 32 */     this.controller = getBodyRotationController();
/* 33 */     this.controller.setMaxHeadAngle(45.0F);
/* 34 */     this.controller.setMaxBodyAngle(45.0F);
/* 35 */     this.controller.setStableAngle(5.0F);
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/*    */     Vector offset;
/* 40 */     Location location = getOriginal().getLocation();
/* 41 */     if (this.positionModelType == PositionModelType.HEAD) {
/* 42 */       double pYaw = Math.toRadians(location.getYaw());
/* 43 */       double pPitch = Math.toRadians(location.getPitch());
/* 44 */       offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), this.pitch + pPitch), this.yaw + pYaw);
/*    */     } else {
/* 46 */       double pYaw = Math.toRadians((this.controller == null) ? getYBodyRot() : this.controller.getYBodyRot());
/* 47 */       offset = Offset.rotateYaw(this.offset.clone(), this.yaw + pYaw);
/*    */     } 
/* 49 */     return location.add(offset);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\FakeBackpack4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */