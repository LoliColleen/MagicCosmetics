/*    */ package com.francobm.magicosmetics.listeners;
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.EntityCache;
/*    */ import com.francobm.magicosmetics.cache.NPC;
/*    */ import com.francobm.magicosmetics.cache.NPCType;
/*    */ import net.citizensnpcs.api.event.NPCDeathEvent;
/*    */ import net.citizensnpcs.api.event.NPCDespawnEvent;
/*    */ import net.citizensnpcs.api.event.NPCRemoveByCommandSenderEvent;
/*    */ import net.citizensnpcs.api.event.NPCSpawnEvent;
/*    */ import net.citizensnpcs.api.event.NPCTeleportEvent;
/*    */ import net.citizensnpcs.api.npc.NPC;
/*    */ import org.bukkit.event.EventHandler;
/*    */ 
/*    */ public class CitizensListener implements Listener {
/* 15 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   @EventHandler
/*    */   public void onLoad(NPCSpawnEvent event) {
/* 18 */     NPC cNPC = event.getNPC();
/* 19 */     NPC npc = this.plugin.getNPCsLoader().getNPC(cNPC.getId(), NPCType.CITIZENS);
/* 20 */     if (npc == null)
/* 21 */       return;  this.plugin.getCitizens().loadNPC(cNPC, npc);
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onRemove(NPCRemoveByCommandSenderEvent event) {
/* 26 */     this.plugin.getNPCsLoader().removeNPC(event.getNPC().getId(), NPCType.CITIZENS);
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onDespawn(NPCDespawnEvent event) {
/* 31 */     NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
/* 32 */     if (npc == null)
/* 33 */       return;  EntityCache entityCache = npc.getEntityCache();
/* 34 */     if (entityCache == null)
/* 35 */       return;  entityCache.clearCosmeticsInUse();
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onDeath(NPCDeathEvent event) {
/* 40 */     NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
/* 41 */     if (npc == null)
/* 42 */       return;  EntityCache entityCache = npc.getEntityCache();
/* 43 */     if (entityCache == null)
/* 44 */       return;  entityCache.clearCosmeticsInUse();
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onTeleport(NPCTeleportEvent event) {
/* 49 */     NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
/* 50 */     if (npc == null)
/* 51 */       return;  EntityCache entityCache = npc.getEntityCache();
/* 52 */     if (entityCache == null)
/* 53 */       return;  entityCache.clearCosmeticsInUse();
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\CitizensListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */