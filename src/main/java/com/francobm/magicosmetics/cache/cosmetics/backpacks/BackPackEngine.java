/*    */ package com.francobm.magicosmetics.cache.cosmetics.backpacks;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.utils.OffsetModel;
/*    */ import com.francobm.magicosmetics.utils.PositionModelType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Color;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class BackPackEngine
/*    */ {
/*    */   private UUID backPackUniqueId;
/*    */   private final String modelId;
/*    */   private final List<String> colorParts;
/*    */   private final String idle_animation;
/*    */   private final double distance;
/*    */   private final OffsetModel offsetModel;
/*    */   private final PositionModelType positionModelType;
/*    */   
/*    */   public BackPackEngine(String modelId, List<String> colorParts, String idle_animation, double distance, OffsetModel offsetModel, PositionModelType positionModelType) {
/* 25 */     this.modelId = modelId;
/* 26 */     this.colorParts = colorParts;
/* 27 */     this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
/* 28 */     this.distance = distance;
/* 29 */     this.offsetModel = offsetModel;
/* 30 */     this.positionModelType = positionModelType;
/*    */   }
/*    */   
/*    */   public BackPackEngine getClone() {
/* 34 */     return new BackPackEngine(this.modelId, new ArrayList<>(this.colorParts), this.idle_animation, this.distance, this.offsetModel, this.positionModelType);
/*    */   }
/*    */   
/*    */   public void remove() {
/* 38 */     if (this.backPackUniqueId == null)
/* 39 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 40 */     plugin.getModelEngine().removeBackPack(this.backPackUniqueId, this.modelId);
/*    */   }
/*    */   
/*    */   public Set<String> getBones() {
/* 44 */     if (this.backPackUniqueId == null) return null; 
/* 45 */     return MagicCosmetics.getInstance().getModelEngine().getAllBonesIds(this.backPackUniqueId, this.modelId);
/*    */   }
/*    */   
/*    */   public void spawnModel(Entity owner) {
/* 49 */     this.backPackUniqueId = owner.getUniqueId();
/* 50 */     MagicCosmetics.getInstance().getModelEngine().spawnModelBackPack(owner, this.modelId, owner.getLocation(), this.offsetModel, this.positionModelType);
/*    */   }
/*    */   
/*    */   public void showModel(Player player) {
/* 54 */     MagicCosmetics.getInstance().getModelEngine().showModel(this.backPackUniqueId, player);
/*    */   }
/*    */   
/*    */   public void hideModel(Player player) {
/* 58 */     MagicCosmetics.getInstance().getModelEngine().hideModel(this.backPackUniqueId, player);
/*    */   }
/*    */   
/*    */   public void tintModel(Entity owner, Color color) {
/* 62 */     if (color == null)
/* 63 */       return;  if (this.backPackUniqueId == null)
/* 64 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 65 */     for (String id : getBones()) {
/* 66 */       if (getColorParts() != null && !getColorParts().isEmpty() && 
/* 67 */         !getColorParts().contains(id))
/*    */         continue; 
/* 69 */       plugin.getModelEngine().tint(this.backPackUniqueId, this.modelId, color, id);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getModelId() {
/* 74 */     return this.modelId;
/*    */   }
/*    */   
/*    */   public List<String> getColorParts() {
/* 78 */     return this.colorParts;
/*    */   }
/*    */   
/*    */   public UUID getBackPackUniqueId() {
/* 82 */     return this.backPackUniqueId;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\BackPackEngine.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */