/*    */ package com.francobm.magicosmetics.cache.cosmetics.balloons;
/*    */ 
/*    */ import com.francobm.magicosmetics.utils.Offset;
/*    */ import com.francobm.magicosmetics.utils.OffsetModel;
/*    */ import com.francobm.magicosmetics.utils.PositionModelType;
/*    */ import com.ticxo.modelengine.api.entity.Dummy;
/*    */ import com.ticxo.modelengine.api.nms.entity.EntityHandler;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.inventory.EquipmentSlot;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ 
/*    */ public class FakeBalloon4
/*    */   extends Dummy<LivingEntity>
/*    */ {
/*    */   private final Vector offset;
/*    */   private final double yaw;
/*    */   private final double pitch;
/*    */   private final PositionModelType positionModelType;
/*    */   
/*    */   public FakeBalloon4(LivingEntity entity, OffsetModel offsetModel, PositionModelType positionModelType) {
/* 24 */     super(entity);
/* 25 */     this.offset = offsetModel.getBukkitVector();
/* 26 */     this.yaw = Math.toRadians(offsetModel.getYaw());
/* 27 */     this.pitch = Math.toRadians(offsetModel.getPitch());
/* 28 */     this.positionModelType = positionModelType;
/*    */   }
/*    */   
/*    */   public void wrapBodyRotationControl() {
/* 32 */     this.bodyRotationController.setMaxHeadAngle(45.0F);
/* 33 */     this.bodyRotationController.setMaxBodyAngle(45.0F);
/* 34 */     this.bodyRotationController.setStableAngle(5.0F);
/*    */   }
/*    */   
/*    */   public Location getOriginalLocation() {
/* 38 */     return ((LivingEntity)getOriginal()).getLocation();
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityHandler.InteractionResult interact(HumanEntity player, EquipmentSlot slot) {
/* 43 */     return EntityHandler.InteractionResult.SUCCESS;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/*    */     Vector offset;
/* 48 */     Location location = ((LivingEntity)getOriginal()).getLocation();
/* 49 */     if (this.positionModelType == PositionModelType.HEAD) {
/* 50 */       double pYaw = Math.toRadians(location.getYaw());
/* 51 */       double pPitch = Math.toRadians(location.getPitch());
/* 52 */       offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), this.pitch + pPitch), this.yaw + pYaw);
/*    */     } else {
/* 54 */       double pYaw = Math.toRadians((this.bodyRotationController == null) ? getYBodyRot() : this.bodyRotationController.getYBodyRot());
/* 55 */       offset = Offset.rotateYaw(this.offset.clone(), this.yaw + pYaw);
/*    */     } 
/* 57 */     return location.add(offset);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\balloons\FakeBalloon4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */