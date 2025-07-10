/*    */ package com.francobm.magicosmetics.cache.cosmetics.backpacks;
/*    */ 
/*    */ import com.francobm.magicosmetics.utils.OffsetModel;
/*    */ import com.francobm.magicosmetics.utils.PositionModelType;
/*    */ import com.ticxo.modelengine.api.entity.BukkitEntity;
/*    */ import com.ticxo.modelengine.api.nms.entity.wrapper.BodyRotationController;
/*    */ import com.ticxo.modelengine.api.utils.math.Offset;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FakeBackpack
/*    */   extends BukkitEntity
/*    */ {
/*    */   private final Vector offset;
/*    */   private final double yaw;
/*    */   private final double pitch;
/*    */   private final PositionModelType positionModelType;
/*    */   private BodyRotationController controller;
/*    */   
/*    */   public FakeBackpack(Entity entity, OffsetModel offsetModel, PositionModelType positionModelType) {
/* 27 */     super(entity);
/* 28 */     this.offset = offsetModel.getBukkitVector();
/* 29 */     this.yaw = Math.toRadians(offsetModel.getYaw());
/* 30 */     this.pitch = Math.toRadians(offsetModel.getPitch());
/* 31 */     this.positionModelType = positionModelType;
/*    */   }
/*    */   
/*    */   public BodyRotationController wrapBodyRotationControl() {
/* 35 */     this.controller = super.wrapBodyRotationControl();
/* 36 */     this.controller.setMaxHeadAngle(45.0F);
/* 37 */     this.controller.setMaxBodyAngle(45.0F);
/* 38 */     this.controller.setStableAngle(5.0F);
/* 39 */     return this.controller;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/*    */     Vector offset;
/* 44 */     Location location = getOriginal().getLocation();
/* 45 */     if (this.positionModelType == PositionModelType.HEAD) {
/* 46 */       double pYaw = Math.toRadians(location.getYaw());
/* 47 */       double pPitch = Math.toRadians(location.getPitch());
/* 48 */       offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), this.pitch + pPitch), this.yaw + pYaw);
/*    */     } else {
/* 50 */       double pYaw = Math.toRadians((this.controller == null) ? getYBodyRot() : this.controller.getYBodyRot());
/* 51 */       offset = Offset.rotateYaw(this.offset.clone(), this.yaw + pYaw);
/*    */     } 
/* 53 */     return location.add(offset);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\FakeBackpack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */