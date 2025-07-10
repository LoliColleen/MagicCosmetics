/*     */ package com.francobm.magicosmetics.loaders;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.EntityCache;
/*     */ import com.francobm.magicosmetics.cache.NPC;
/*     */ import com.francobm.magicosmetics.cache.NPCType;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.entity.Entity;
/*     */ 
/*     */ public class NPCsLoader
/*     */ {
/*  16 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */   private final Map<String, NPC> NPCs;
/*     */   
/*     */   public NPCsLoader() {
/*  20 */     this.NPCs = new HashMap<>();
/*  21 */     load();
/*     */   }
/*     */   
/*     */   public void load() {
/*  25 */     this.NPCs.clear();
/*  26 */     FileCreator fileCreator = this.plugin.getNPCs();
/*  27 */     ConfigurationSection configurationSection = fileCreator.getConfigurationSection("NPCs");
/*  28 */     if (configurationSection == null)
/*  29 */       return;  int i = 0;
/*  30 */     for (String key : configurationSection.getKeys(false)) {
/*  31 */       for (String keyId : fileCreator.getConfigurationSection("NPCs." + key).getKeys(false)) {
/*  32 */         int id; String cosmetics; UUID uuid; switch (key.toLowerCase()) {
/*     */           case "citizens":
/*  34 */             id = Integer.parseInt(keyId);
/*  35 */             cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
/*  36 */             this.NPCs.put("" + id + id, new NPC(id, NPCType.CITIZENS, cosmetics));
/*  37 */             i++;
/*     */           
/*     */           case "znpcsplus":
/*  40 */             id = Integer.parseInt(keyId);
/*  41 */             cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
/*  42 */             this.NPCs.put("" + id + id, new NPC(id, NPCType.Z_NPC_PLUS, cosmetics));
/*  43 */             i++;
/*     */           
/*     */           case "vanilla":
/*  46 */             uuid = UUID.fromString(keyId);
/*  47 */             cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
/*  48 */             this.NPCs.put(keyId + keyId, new NPC(uuid, NPCType.VANILLA, cosmetics));
/*  49 */             i++;
/*     */         } 
/*     */       
/*     */       } 
/*     */     } 
/*  54 */     this.plugin.getLogger().info("" + i + " NPCs has been loaded");
/*     */   }
/*     */   
/*     */   public void addNPC(int id, NPCType npcType, EntityCache entityCache, String cosmeticId) {
/*  58 */     NPC npc = new NPC(id, npcType, cosmeticId);
/*  59 */     npc.setEntityCache(entityCache);
/*  60 */     this.NPCs.put("" + id + id, npc);
/*     */   }
/*     */   
/*     */   public boolean hasNPC(int id, NPCType npcType) {
/*  64 */     return this.NPCs.containsKey("" + id + id);
/*     */   }
/*     */   
/*     */   public NPC getNPC(int id, NPCType npcType) {
/*  68 */     return this.NPCs.get("" + id + id);
/*     */   }
/*     */   
/*     */   public void removeNPC(int id, NPCType npcType) {
/*  72 */     NPC npc = getNPC(id, npcType);
/*  73 */     if (npc == null)
/*  74 */       return;  EntityCache entityCache = npc.getEntityCache();
/*  75 */     if (entityCache != null) {
/*  76 */       entityCache.clearCosmeticsInUse();
/*  77 */       EntityCache.removeEntity(entityCache.getUniqueId());
/*     */     } 
/*  79 */     this.NPCs.remove("" + id + id);
/*     */   }
/*     */   
/*     */   public void save() {
/*  83 */     FileCreator fileCreator = this.plugin.getNPCs();
/*  84 */     fileCreator.set("NPCs", null);
/*  85 */     int i = 0;
/*  86 */     for (NPC npc : this.NPCs.values()) {
/*  87 */       if (npc.getEntityCache() == null)
/*  88 */         continue;  switch (npc.getNpcType()) {
/*     */         case CITIZENS:
/*  90 */           fileCreator.set("NPCs.Citizens." + npc.getId(), npc.getEntityCache().saveCosmetics());
/*     */           break;
/*     */         case Z_NPC_PLUS:
/*  93 */           fileCreator.set("NPCs.ZNPCsPlus." + npc.getId(), npc.getEntityCache().saveCosmetics());
/*     */           break;
/*     */         case VANILLA:
/*  96 */           fileCreator.set("NPCs.Vanilla." + String.valueOf(((Entity)npc.getNpc()).getUniqueId()), npc.getEntityCache().saveCosmetics());
/*     */           break;
/*     */       } 
/*  99 */       i++;
/*     */     } 
/* 101 */     this.plugin.getLogger().info("Data for " + i + " NPCs have been saved.");
/* 102 */     fileCreator.save();
/*     */   }
/*     */   
/*     */   public Map<String, NPC> getNPCs() {
/* 106 */     return this.NPCs;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\loaders\NPCsLoader.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */