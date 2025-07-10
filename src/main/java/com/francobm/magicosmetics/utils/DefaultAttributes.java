/*     */ package com.francobm.magicosmetics.utils;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeModifier;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultAttributes
/*     */ {
/*     */   public static Multimap<Attribute, AttributeModifier> defaultsOf(ItemStack item) {
/*  18 */     ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
/*  19 */     if (item == null)
/*  20 */       return (Multimap<Attribute, AttributeModifier>)arrayListMultimap; 
/*  21 */     Material mat = item.getType();
/*  22 */     double armor = getDefaultArmor(mat);
/*  23 */     double tough = getDefaultArmorToughness(mat);
/*  24 */     double knockBack = getKnockBackResistance(mat);
/*  25 */     double damage = getDefaultAttackDamage(mat);
/*  26 */     double speed = getDefaultAttackSpeed(mat);
/*  27 */     if (armor > 0.0D) {
/*  28 */       arrayListMultimap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR.getKey().getKey(), armor, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat)));
/*     */     }
/*  30 */     if (knockBack > 0.0D) {
/*  31 */       arrayListMultimap.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_KNOCKBACK_RESISTANCE.getKey().getKey(), knockBack, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat)));
/*     */     }
/*  33 */     if (tough > 0.0D) {
/*  34 */       arrayListMultimap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR_TOUGHNESS.getKey().getKey(), tough, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat)));
/*     */     }
/*  36 */     if (damage > 0.0D) {
/*  37 */       arrayListMultimap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ATTACK_DAMAGE.getKey().getKey(), damage, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat)));
/*     */     }
/*  39 */     if (speed > 0.0D) {
/*  40 */       arrayListMultimap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(), speed, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat)));
/*     */     }
/*  42 */     return (Multimap<Attribute, AttributeModifier>)arrayListMultimap;
/*     */   }
/*     */   
/*     */   public static EquipmentSlot guessEquipmentSlotOf(Material material) {
/*  46 */     String itemName = material.name();
/*  47 */     if (itemName.contains("_HELMET"))
/*  48 */       return EquipmentSlot.HEAD; 
/*  49 */     if (itemName.contains("_CHESTPLATE"))
/*  50 */       return EquipmentSlot.CHEST; 
/*  51 */     if (itemName.contains("_LEGGINGS"))
/*  52 */       return EquipmentSlot.LEGS; 
/*  53 */     if (itemName.contains("_BOOTS")) {
/*  54 */       return EquipmentSlot.FEET;
/*     */     }
/*  56 */     return EquipmentSlot.HAND;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getDefaultArmor(Material mat) {
/*  61 */     switch (mat) { case LEATHER_HELMET:
/*  62 */         return 1.0D;
/*  63 */       case LEATHER_CHESTPLATE: return 3.0D;
/*  64 */       case LEATHER_LEGGINGS: return 2.0D;
/*  65 */       case LEATHER_BOOTS: return 1.0D;
/*  66 */       case GOLDEN_HELMET: return 2.0D;
/*  67 */       case GOLDEN_CHESTPLATE: return 5.0D;
/*  68 */       case GOLDEN_LEGGINGS: return 3.0D;
/*  69 */       case GOLDEN_BOOTS: return 1.0D;
/*  70 */       case CHAINMAIL_HELMET: return 2.0D;
/*  71 */       case CHAINMAIL_CHESTPLATE: return 5.0D;
/*  72 */       case CHAINMAIL_LEGGINGS: return 4.0D;
/*  73 */       case CHAINMAIL_BOOTS: return 1.0D;
/*  74 */       case IRON_HELMET: return 2.0D;
/*  75 */       case IRON_CHESTPLATE: return 6.0D;
/*  76 */       case IRON_LEGGINGS: return 5.0D;
/*  77 */       case IRON_BOOTS: return 2.0D;
/*  78 */       case DIAMOND_HELMET: return 3.0D;
/*  79 */       case DIAMOND_CHESTPLATE: return 8.0D;
/*  80 */       case DIAMOND_LEGGINGS: return 6.0D;
/*  81 */       case DIAMOND_BOOTS: return 3.0D;
/*  82 */       case NETHERITE_HELMET: return 3.0D;
/*  83 */       case NETHERITE_CHESTPLATE: return 8.0D;
/*  84 */       case NETHERITE_LEGGINGS: return 6.0D;
/*  85 */       case NETHERITE_BOOTS: return 3.0D;
/*  86 */       case TURTLE_HELMET: return 2.0D; }
/*  87 */      return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getDefaultArmorToughness(Material mat) {
/*  92 */     switch (mat) {
/*     */       case DIAMOND_HELMET:
/*     */       case DIAMOND_CHESTPLATE:
/*     */       case DIAMOND_LEGGINGS:
/*     */       case DIAMOND_BOOTS:
/*  97 */         return 2.0D;
/*     */       case NETHERITE_HELMET:
/*     */       case NETHERITE_CHESTPLATE:
/*     */       case NETHERITE_LEGGINGS:
/*     */       case NETHERITE_BOOTS:
/* 102 */         return 3.0D;
/* 103 */     }  return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getKnockBackResistance(Material mat) {
/* 108 */     switch (mat) {
/*     */       case NETHERITE_HELMET:
/*     */       case NETHERITE_CHESTPLATE:
/*     */       case NETHERITE_LEGGINGS:
/*     */       case NETHERITE_BOOTS:
/* 113 */         return 1.0D;
/* 114 */     }  return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getDefaultAttackDamage(Material mat) {
/* 119 */     switch (mat) { case WOODEN_SWORD:
/* 120 */         return 4.0D;
/* 121 */       case GOLDEN_SWORD: return 4.0D;
/* 122 */       case STONE_SWORD: return 5.0D;
/* 123 */       case IRON_SWORD: return 6.0D;
/* 124 */       case DIAMOND_SWORD: return 7.0D;
/* 125 */       case NETHERITE_SWORD: return 8.0D;
/*     */       case WOODEN_AXE:
/* 127 */         return 7.0D;
/* 128 */       case GOLDEN_AXE: return 7.0D;
/* 129 */       case STONE_AXE: return 9.0D;
/* 130 */       case IRON_AXE: return 9.0D;
/* 131 */       case DIAMOND_AXE: return 9.0D;
/*     */       case WOODEN_PICKAXE:
/* 133 */         return 2.0D;
/* 134 */       case GOLDEN_PICKAXE: return 2.0D;
/* 135 */       case STONE_PICKAXE: return 3.0D;
/* 136 */       case IRON_PICKAXE: return 4.0D;
/* 137 */       case DIAMOND_PICKAXE: return 5.0D;
/*     */       case WOODEN_SHOVEL:
/* 139 */         return 2.5D;
/* 140 */       case GOLDEN_SHOVEL: return 2.5D;
/* 141 */       case STONE_SHOVEL: return 3.5D;
/* 142 */       case IRON_SHOVEL: return 4.5D;
/* 143 */       case DIAMOND_SHOVEL: return 5.5D;
/*     */       case WOODEN_HOE:
/* 145 */         return 1.0D;
/* 146 */       case GOLDEN_HOE: return 1.0D;
/* 147 */       case STONE_HOE: return 1.0D;
/* 148 */       case IRON_HOE: return 1.0D;
/* 149 */       case DIAMOND_HOE: return 1.0D; }
/* 150 */      return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getDefaultAttackSpeed(Material mat) {
/* 155 */     switch (mat) {
/*     */       case WOODEN_SWORD:
/*     */       case GOLDEN_SWORD:
/*     */       case STONE_SWORD:
/*     */       case IRON_SWORD:
/*     */       case DIAMOND_SWORD:
/* 161 */         return 1.6D;
/*     */       case WOODEN_AXE:
/* 163 */         return 0.8D;
/* 164 */       case GOLDEN_AXE: return 1.0D;
/* 165 */       case STONE_AXE: return 0.8D;
/* 166 */       case IRON_AXE: return 0.9D;
/* 167 */       case DIAMOND_AXE: return 1.0D;
/*     */       case WOODEN_PICKAXE:
/* 169 */         return 1.2D;
/* 170 */       case STONE_PICKAXE: return 1.2D;
/* 171 */       case GOLDEN_PICKAXE: return 1.2D;
/* 172 */       case IRON_PICKAXE: return 1.2D;
/* 173 */       case DIAMOND_PICKAXE: return 1.2D;
/*     */       case WOODEN_SHOVEL:
/* 175 */         return 1.0D;
/* 176 */       case GOLDEN_SHOVEL: return 1.0D;
/* 177 */       case STONE_SHOVEL: return 1.0D;
/* 178 */       case IRON_SHOVEL: return 1.0D;
/* 179 */       case DIAMOND_SHOVEL: return 1.0D;
/*     */       case WOODEN_HOE:
/* 181 */         return 1.0D;
/* 182 */       case GOLDEN_HOE: return 1.0D;
/* 183 */       case STONE_HOE: return 2.0D;
/* 184 */       case IRON_HOE: return 3.0D;
/* 185 */       case DIAMOND_HOE: return 4.0D;
/* 186 */     }  return 0.0D;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\DefaultAttributes.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */