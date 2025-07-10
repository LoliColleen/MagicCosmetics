/*    */ package com.francobm.magicosmetics.provider.husksync;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import net.william278.husksync.BukkitHuskSync;
/*    */ import net.william278.husksync.adapter.Adaptable;
/*    */ import net.william278.husksync.data.BukkitData;
/*    */ import net.william278.husksync.user.BukkitUser;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class CosmeticData
/*    */   extends BukkitData implements Adaptable {
/*    */   private String cosmetics;
/*    */   private String cosmeticsInUse;
/*    */   private boolean isCosmeticsLoaded;
/*    */   
/*    */   public CosmeticData(String cosmetics, String cosmeticsInUse) {
/* 18 */     this.cosmetics = cosmetics;
/* 19 */     this.cosmeticsInUse = cosmeticsInUse;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private CosmeticData() {}
/*    */ 
/*    */   
/*    */   public void apply(BukkitUser bukkitUser, BukkitHuskSync bukkitHuskSync) {
/* 28 */     if (this.isCosmeticsLoaded)
/* 29 */       return;  Player player = bukkitUser.getPlayer();
/* 30 */     MagicCosmetics.getInstance().getSql().loadPlayerAsync(player).thenAccept(playerData -> {
/*    */           playerData.forceClearCosmeticsInventory();
/*    */           playerData.loadCosmetics(getCosmetics(), getCosmeticsInUse());
/*    */           this.isCosmeticsLoaded = true;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCosmetics() {
/* 39 */     return this.cosmetics;
/*    */   }
/*    */   
/*    */   public String getCosmeticsInUse() {
/* 43 */     return this.cosmeticsInUse;
/*    */   }
/*    */   
/*    */   public void setCosmeticsInUse(String cosmeticsInUse) {
/* 47 */     this.cosmeticsInUse = cosmeticsInUse;
/*    */   }
/*    */   
/*    */   public void setCosmetics(String cosmetics) {
/* 51 */     this.cosmetics = cosmetics;
/*    */   }
/*    */   
/*    */   public void setCosmeticsLoaded(boolean cosmeticsLoaded) {
/* 55 */     this.isCosmeticsLoaded = cosmeticsLoaded;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\husksync\CosmeticData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */