/*     */ package com.francobm.magicosmetics.provider.citizens;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.EntityCache;
/*     */ import com.francobm.magicosmetics.cache.NPC;
/*     */ import com.francobm.magicosmetics.cache.NPCType;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.citizensnpcs.api.CitizensAPI;
/*     */ import net.citizensnpcs.api.npc.NPC;
/*     */ import net.citizensnpcs.api.trait.trait.Equipment;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class Citizens {
/*  21 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */   
/*     */   public void loadNPCCosmetics() {
/*  24 */     for (NPC npc : this.plugin.getNPCsLoader().getNPCs().values()) {
/*  25 */       if (npc.getNpcType() != NPCType.CITIZENS)
/*  26 */         continue;  NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(npc.getId());
/*  27 */       if (citizensNPC == null)
/*  28 */         continue;  loadNPC(citizensNPC, npc);
/*     */     } 
/*     */   }
/*     */   
/*     */   public NPC getNPC(UUID uuid) {
/*  33 */     return CitizensAPI.getNPCRegistry().getByUniqueId(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getNPCs() {
/*  38 */     List<String> list = new ArrayList<>();
/*  39 */     for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
/*  40 */       list.add(String.valueOf(npc.getId()));
/*     */     }
/*  42 */     return list;
/*     */   }
/*     */   
/*     */   public void EquipmentNPC(ItemSlot itemSlot, UUID uuid, ItemStack itemStack) {
/*  46 */     NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(uuid);
/*  47 */     if (npc == null)
/*  48 */       return;  Equipment equipment = (Equipment)npc.getOrAddTrait(Equipment.class);
/*  49 */     this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> {
/*     */           switch (itemSlot) {
/*     */             case HELMET:
/*     */               equipment.set(Equipment.EquipmentSlot.HELMET, itemStack.clone());
/*     */               return;
/*     */             case CHESTPLATE:
/*     */               equipment.set(Equipment.EquipmentSlot.CHESTPLATE, itemStack.clone());
/*     */               return;
/*     */             case LEGGINGS:
/*     */               equipment.set(Equipment.EquipmentSlot.LEGGINGS, itemStack.clone());
/*     */               return;
/*     */             case BOOTS:
/*     */               equipment.set(Equipment.EquipmentSlot.BOOTS, itemStack.clone());
/*     */               return;
/*     */             case MAIN_HAND:
/*     */               equipment.set(Equipment.EquipmentSlot.HAND, itemStack.clone());
/*     */               return;
/*     */             case OFF_HAND:
/*     */               equipment.set(Equipment.EquipmentSlot.OFF_HAND, itemStack.clone());
/*     */               break;
/*     */           } 
/*     */         });
/*     */   }
/*     */   public void equipCosmetic(CommandSender sender, String npcID, String id, String colorHex) {
/*     */     try {
/*  74 */       int ID = Integer.parseInt(npcID);
/*  75 */       NPC npc = CitizensAPI.getNPCRegistry().getById(ID);
/*  76 */       if (npc == null) {
/*  77 */         if (sender != null)
/*  78 */           Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
/*     */         return;
/*     */       } 
/*  81 */       NPC npcRegistry = this.plugin.getNPCsLoader().getNPC(ID, NPCType.CITIZENS);
/*  82 */       if (npcRegistry == null) {
/*  83 */         EntityCache entityCache1 = EntityCache.getEntityOrCreate(npc.getEntity());
/*  84 */         if (this.plugin.getUser() == null)
/*  85 */           return;  Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(id);
/*  86 */         Color color1 = null;
/*  87 */         if (colorHex != null) {
/*  88 */           color1 = Utils.hex2Rgb(colorHex);
/*     */         }
/*  90 */         if (cosmetic1 == null)
/*  91 */           return;  if (entityCache1.hasEquipped(cosmetic1)) {
/*  92 */           entityCache1.unSetCosmetic(cosmetic1.getCosmeticType());
/*     */           return;
/*     */         } 
/*  95 */         if (color1 != null) {
/*  96 */           cosmetic1.setColor(color1);
/*     */         }
/*  98 */         entityCache1.setCosmetic(cosmetic1);
/*  99 */         this.plugin.getNPCsLoader().addNPC(ID, NPCType.CITIZENS, entityCache1, id);
/* 100 */         if (this.plugin.equipMessage && sender != null)
/* 101 */           Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
/*     */         return;
/*     */       } 
/* 104 */       EntityCache entityCache = npcRegistry.getEntityCache();
/* 105 */       if (entityCache == null) {
/* 106 */         entityCache = EntityCache.getEntityOrCreate(npc.getEntity());
/* 107 */         npcRegistry.setEntityCache(entityCache);
/*     */       } 
/* 109 */       Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
/* 110 */       Color color = null;
/* 111 */       if (colorHex != null) {
/* 112 */         color = Utils.hex2Rgb(colorHex);
/*     */       }
/* 114 */       if (cosmetic == null)
/* 115 */         return;  if (entityCache.hasEquipped(cosmetic)) {
/* 116 */         entityCache.unSetCosmetic(cosmetic.getCosmeticType());
/*     */         return;
/*     */       } 
/* 119 */       if (color != null) {
/* 120 */         cosmetic.setColor(color);
/*     */       }
/* 122 */       entityCache.setCosmetic(cosmetic);
/* 123 */       if (this.plugin.equipMessage && sender != null)
/* 124 */         Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
/* 125 */     } catch (NumberFormatException exception) {
/* 126 */       if (sender == null)
/* 127 */         return;  Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadNPC(NPC citizensNPc, NPC npc) {
/* 132 */     EntityCache entityCache = EntityCache.getEntityOrCreate(citizensNPc.getEntity());
/* 133 */     if (this.plugin.getUser() == null)
/* 134 */       return;  entityCache.loadCosmetics(npc.getCosmetics());
/* 135 */     npc.setEntityCache(entityCache);
/* 136 */     npc.setLoad(true);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\citizens\Citizens.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */