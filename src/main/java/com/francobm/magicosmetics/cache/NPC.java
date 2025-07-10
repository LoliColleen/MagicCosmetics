/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class NPC {
/*    */   private int id;
/*    */   private Object npc;
/*    */   private final UUID uuid;
/*    */   private final NPCType npcType;
/*    */   private String cosmetics;
/*    */   private EntityCache entityCache;
/*    */   private boolean load;
/*    */   
/*    */   public NPC(int id, NPCType npcType, String cosmetics) {
/* 15 */     this.id = id;
/* 16 */     this.npcType = npcType;
/* 17 */     this.cosmetics = cosmetics;
/* 18 */     this.uuid = null;
/*    */   }
/*    */   
/*    */   public NPC(UUID uuid, NPCType npcType, String cosmetics) {
/* 22 */     this.uuid = uuid;
/* 23 */     this.npcType = npcType;
/* 24 */     this.cosmetics = cosmetics;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 28 */     return this.id;
/*    */   }
/*    */   
/*    */   public NPCType getNpcType() {
/* 32 */     return this.npcType;
/*    */   }
/*    */   
/*    */   public Object getNpc() {
/* 36 */     return this.npc;
/*    */   }
/*    */   
/*    */   public void setNpc(Object npc) {
/* 40 */     this.npc = npc;
/*    */   }
/*    */   
/*    */   public String getCosmetics() {
/* 44 */     return this.cosmetics;
/*    */   }
/*    */   
/*    */   public void setCosmetics(String cosmetics) {
/* 48 */     this.cosmetics = cosmetics;
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 52 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public void setEntityCache(EntityCache entityCache) {
/* 56 */     this.entityCache = entityCache;
/*    */   }
/*    */   
/*    */   public EntityCache getEntityCache() {
/* 60 */     return this.entityCache;
/*    */   }
/*    */   
/*    */   public boolean isVanilla() {
/* 64 */     return (this.uuid != null);
/*    */   }
/*    */   
/*    */   public void setLoad(boolean load) {
/* 68 */     this.load = load;
/*    */   }
/*    */   
/*    */   public boolean isLoad() {
/* 72 */     return this.load;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\NPC.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */