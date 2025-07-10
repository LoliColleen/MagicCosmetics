/*     */ package com.francobm.magicosmetics.cache;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class EntityCache {
/*  18 */   public static Map<UUID, EntityCache> entities = new HashMap<>();
/*     */   private final UUID uniqueId;
/*     */   private boolean npc = false;
/*     */   private Entity entity;
/*     */   private Cosmetic hat;
/*     */   private Cosmetic bag;
/*     */   private Cosmetic wStick;
/*     */   private Cosmetic balloon;
/*     */   
/*     */   public EntityCache(UUID uniqueId) {
/*  28 */     this.uniqueId = uniqueId;
/*     */   }
/*     */   
/*     */   public EntityCache(Entity entity) {
/*  32 */     this.uniqueId = entity.getUniqueId();
/*  33 */     this.entity = entity;
/*     */   }
/*     */   
/*     */   public static EntityCache getEntity(UUID uniqueId) {
/*  37 */     if (!entities.containsKey(uniqueId)) {
/*  38 */       return null;
/*     */     }
/*  40 */     return entities.get(uniqueId);
/*     */   }
/*     */   
/*     */   public static EntityCache getEntityOrCreate(Entity entity) {
/*  44 */     if (!entities.containsKey(entity.getUniqueId())) {
/*  45 */       entities.put(entity.getUniqueId(), new EntityCache(entity));
/*     */     }
/*  47 */     return entities.get(entity.getUniqueId());
/*     */   }
/*     */   
/*     */   public static void removeEntity(UUID uniqueId) {
/*  51 */     entities.remove(uniqueId);
/*     */   }
/*     */   
/*     */   public boolean hasEquipped(String cosmeticId) {
/*  55 */     if (this.hat != null && 
/*  56 */       this.hat.getId().equals(cosmeticId)) return true;
/*     */     
/*  58 */     if (this.bag != null && 
/*  59 */       this.bag.getId().equals(cosmeticId)) return true;
/*     */     
/*  61 */     if (this.wStick != null && 
/*  62 */       this.wStick.getId().equals(cosmeticId)) return true;
/*     */     
/*  64 */     if (this.balloon != null) {
/*  65 */       return this.balloon.getId().equals(cosmeticId);
/*     */     }
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasEquipped(Cosmetic cosmetic) {
/*  71 */     switch (cosmetic.getCosmeticType()) {
/*     */       case HAT:
/*  73 */         if (this.hat == null) return false; 
/*  74 */         return this.hat.getId().equals(cosmetic.getId());
/*     */       case BAG:
/*  76 */         if (this.bag == null) return false; 
/*  77 */         return this.bag.getId().equals(cosmetic.getId());
/*     */       case WALKING_STICK:
/*  79 */         if (this.wStick == null) return false; 
/*  80 */         return this.wStick.getId().equals(cosmetic.getId());
/*     */       case BALLOON:
/*  82 */         if (this.balloon == null) return false; 
/*  83 */         return this.balloon.getId().equals(cosmetic.getId());
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public void unSetCosmetic(CosmeticType cosmetic) {
/*  89 */     switch (cosmetic) {
/*     */       case HAT:
/*  91 */         clearHat();
/*  92 */         this.hat = null;
/*     */         break;
/*     */       case BAG:
/*  95 */         clearBag();
/*  96 */         this.bag = null;
/*     */         break;
/*     */       case WALKING_STICK:
/*  99 */         clearWStick();
/* 100 */         this.wStick = null;
/*     */         break;
/*     */       case BALLOON:
/* 103 */         clearBalloon();
/* 104 */         this.balloon = null;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCosmetic(Cosmetic cosmetic) {
/* 110 */     switch (cosmetic.getCosmeticType()) {
/*     */       case HAT:
/* 112 */         clearHat();
/* 113 */         this.hat = cosmetic;
/*     */         break;
/*     */       case BAG:
/* 116 */         clearBag();
/* 117 */         this.bag = cosmetic;
/*     */         break;
/*     */       case WALKING_STICK:
/* 120 */         clearWStick();
/* 121 */         this.wStick = cosmetic;
/*     */         break;
/*     */       case BALLOON:
/* 124 */         clearBalloon();
/* 125 */         this.balloon = cosmetic;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void activeCosmetics() {
/* 131 */     activeHat();
/* 132 */     activeBag();
/* 133 */     activeWStick();
/* 134 */     activeBalloon();
/*     */   }
/*     */   
/*     */   public void activeCosmeticsInInventory() {
/* 138 */     activeHat();
/* 139 */     activeBag();
/* 140 */     activeWStick();
/*     */   }
/*     */   
/*     */   public void clearCosmeticsInUse() {
/* 144 */     clearBalloon();
/* 145 */     clearBag();
/* 146 */     clearHat();
/* 147 */     clearWStick();
/*     */   }
/*     */   
/*     */   public void activeHat() {
/* 151 */     if (this.hat == null)
/* 152 */       return;  if (!(this.entity instanceof LivingEntity))
/* 153 */       return;  if (!this.npc) {
/* 154 */       LivingEntity livingEntity = (LivingEntity)this.entity;
/* 155 */       if (livingEntity instanceof Player) {
/* 156 */         Player player = (Player)livingEntity;
/* 157 */         MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, this.hat.getItemColor(player));
/*     */         return;
/*     */       } 
/* 160 */       MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, this.hat.getItemColor());
/*     */       return;
/*     */     } 
/* 163 */     MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.HELMET, getUniqueId(), this.hat.getItemColor());
/*     */   }
/*     */   
/*     */   public void activeBag() {
/* 167 */     if (this.bag == null)
/* 168 */       return;  ((Bag)this.bag).active(getEntityOrCreate());
/*     */   }
/*     */   
/*     */   public void activeWStick() {
/* 172 */     if (this.wStick == null)
/* 173 */       return;  if (!(this.entity instanceof LivingEntity))
/* 174 */       return;  if (!this.npc) {
/* 175 */       LivingEntity livingEntity = (LivingEntity)this.entity;
/* 176 */       if (livingEntity instanceof Player) {
/* 177 */         Player player = (Player)livingEntity;
/* 178 */         MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, this.wStick.getItemColor(player));
/*     */         return;
/*     */       } 
/* 181 */       MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, this.hat.getItemColor());
/*     */       return;
/*     */     } 
/* 184 */     MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.OFF_HAND, getUniqueId(), this.wStick.getItemColor());
/*     */   }
/*     */   
/*     */   public void activeBalloon() {
/* 188 */     if (this.balloon == null)
/* 189 */       return;  ((Balloon)this.balloon).active(getEntityOrCreate());
/*     */   }
/*     */   
/*     */   public void clearHat() {
/* 193 */     if (this.hat == null) {
/*     */       return;
/*     */     }
/* 196 */     if (!(this.entity instanceof LivingEntity))
/* 197 */       return;  if (!this.npc) {
/* 198 */       LivingEntity livingEntity = (LivingEntity)this.entity;
/* 199 */       MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, XMaterial.AIR.parseItem());
/*     */       return;
/*     */     } 
/* 202 */     MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.HELMET, getUniqueId(), XMaterial.AIR.parseItem());
/*     */   }
/*     */   
/*     */   public void clearBag() {
/* 206 */     if (this.bag == null)
/* 207 */       return;  this.bag.remove();
/*     */   }
/*     */   
/*     */   public void clearWStick() {
/* 211 */     if (this.wStick == null) {
/*     */       return;
/*     */     }
/* 214 */     if (!(this.entity instanceof LivingEntity))
/* 215 */       return;  if (!this.npc) {
/* 216 */       LivingEntity livingEntity = (LivingEntity)this.entity;
/* 217 */       MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, XMaterial.AIR.parseItem());
/*     */       return;
/*     */     } 
/* 220 */     MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.OFF_HAND, getUniqueId(), XMaterial.AIR.parseItem());
/*     */   }
/*     */   
/*     */   public void clearBalloon() {
/* 224 */     if (this.balloon == null) {
/*     */       return;
/*     */     }
/* 227 */     this.balloon.remove();
/*     */   }
/*     */   
/*     */   public UUID getUniqueId() {
/* 231 */     return this.uniqueId;
/*     */   }
/*     */   
/*     */   public Entity getEntityOrCreate() {
/* 235 */     return this.entity;
/*     */   }
/*     */   
/*     */   public void setEntity(Entity entity) {
/* 239 */     this.entity = entity;
/*     */   }
/*     */   
/*     */   public Cosmetic getHat() {
/* 243 */     return this.hat;
/*     */   }
/*     */   
/*     */   public Cosmetic getBag() {
/* 247 */     return this.bag;
/*     */   }
/*     */   
/*     */   public Cosmetic getWStick() {
/* 251 */     return this.wStick;
/*     */   }
/*     */   
/*     */   public Cosmetic getBalloon() {
/* 255 */     return this.balloon;
/*     */   }
/*     */   
/*     */   public void loadCosmetics(String ids) {
/* 259 */     if (ids.isEmpty())
/* 260 */       return;  List<String> cosmetics = new ArrayList<>(Arrays.asList(ids.split(",")));
/* 261 */     for (String cosmetic : cosmetics) {
/* 262 */       if (cosmetic.isEmpty())
/* 263 */         continue;  String[] color = cosmetic.split("\\|");
/* 264 */       if (color.length > 1) {
/* 265 */         Cosmetic cosmetic2 = Cosmetic.getCloneCosmetic(color[0]);
/* 266 */         if (cosmetic2 == null)
/* 267 */           continue;  cosmetic2.setColor(Color.fromRGB(Integer.parseInt(color[1])));
/* 268 */         setCosmetic(cosmetic2);
/*     */         continue;
/*     */       } 
/* 271 */       Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(cosmetic);
/* 272 */       if (cosmetic1 == null)
/* 273 */         continue;  setCosmetic(cosmetic1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String saveCosmetics() {
/* 278 */     return saveHat() + "," + saveHat() + "," + saveBag() + "," + saveWStick();
/*     */   }
/*     */   
/*     */   public String saveHat() {
/* 282 */     if (this.hat == null) return ""; 
/* 283 */     if (this.hat.getColor() == null) return this.hat.getId(); 
/* 284 */     return this.hat.getId() + "|" + this.hat.getId();
/*     */   }
/*     */   
/*     */   public String saveBag() {
/* 288 */     if (this.bag == null) return ""; 
/* 289 */     if (this.bag.getColor() == null) return this.bag.getId(); 
/* 290 */     return this.bag.getId() + "|" + this.bag.getId();
/*     */   }
/*     */   
/*     */   public String saveWStick() {
/* 294 */     if (this.wStick == null) return ""; 
/* 295 */     if (this.wStick.getColor() == null) return this.wStick.getId(); 
/* 296 */     return this.wStick.getId() + "|" + this.wStick.getId();
/*     */   }
/*     */   
/*     */   public String saveBalloon() {
/* 300 */     if (this.balloon == null) return ""; 
/* 301 */     if (this.balloon.getColor() == null) return this.balloon.getId(); 
/* 302 */     return this.balloon.getId() + "|" + this.balloon.getId();
/*     */   }
/*     */   
/*     */   public boolean isCosmeticUse() {
/* 306 */     return (this.hat != null || this.bag != null || this.wStick != null || this.balloon != null);
/*     */   }
/*     */   
/*     */   public void setNpc(boolean npc) {
/* 310 */     this.npc = npc;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\EntityCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */