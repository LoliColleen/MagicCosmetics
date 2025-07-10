/*    */ package com.francobm.magicosmetics.provider.znpcplus;
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.cache.EntityCache;
/*    */ import com.francobm.magicosmetics.cache.NPC;
/*    */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*    */ import lol.pyr.znpcsplus.api.NpcApi;
/*    */ import lol.pyr.znpcsplus.api.NpcApiProvider;
/*    */ import lol.pyr.znpcsplus.api.entity.EntityProperty;
/*    */ import lol.pyr.znpcsplus.api.npc.NpcEntry;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class ZNPCsPlus {
/* 14 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance(); private final NpcApi npcApi;
/*    */   
/*    */   public ZNPCsPlus() {
/* 17 */     this.npcApi = NpcApiProvider.get();
/* 18 */     NpcApiProvider.register((Plugin)this.plugin, this.npcApi);
/* 19 */     a();
/*    */   }
/*    */   
/*    */   private void a() {
/* 23 */     for (EntityProperty<?> entityProperty : (Iterable<EntityProperty<?>>)this.npcApi.getPropertyRegistry().getAll())
/* 24 */       this.plugin.getLogger().info("EntityProperty: " + entityProperty.getName() + " - " + String.valueOf(entityProperty.getDefaultValue())); 
/* 25 */     for (NpcEntry npcEntry : this.npcApi.getNpcRegistry().getAll());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void EquipmentNPC(ItemSlot itemSlot, int id, ItemStack itemStack) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void equipCosmetic(CommandSender sender, String npcID, String id, String colorHex) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadNPC(NpcEntry znpc, NPC npc) {
/* 89 */     NpcApi npcApiProvider = NpcApiProvider.get();
/* 90 */     npcApiProvider.getPropertyRegistry().getAll();
/* 91 */     EntityCache entityCache = EntityCache.getEntityOrCreate((Entity)znpc.getNpc().getProperty(this.npcApi.getPropertyRegistry().getByName("entity", Entity.class)));
/* 92 */     if (this.plugin.getUser() == null)
/* 93 */       return;  entityCache.loadCosmetics(npc.getCosmetics());
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\znpcplus\ZNPCsPlus.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */