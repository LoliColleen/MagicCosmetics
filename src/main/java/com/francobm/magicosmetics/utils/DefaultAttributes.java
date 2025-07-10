package com.francobm.magicosmetics.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class DefaultAttributes {
  public static Multimap<Attribute, AttributeModifier> defaultsOf(ItemStack item) {
    ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
    if (item == null)
      return (Multimap<Attribute, AttributeModifier>)arrayListMultimap; 
    Material mat = item.getType();
    double armor = getDefaultArmor(mat);
    double tough = getDefaultArmorToughness(mat);
    double knockBack = getKnockBackResistance(mat);
    double damage = getDefaultAttackDamage(mat);
    double speed = getDefaultAttackSpeed(mat);
    if (armor > 0.0D)
      arrayListMultimap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR.getKey().getKey(), armor, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat))); 
    if (knockBack > 0.0D)
      arrayListMultimap.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_KNOCKBACK_RESISTANCE.getKey().getKey(), knockBack, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat))); 
    if (tough > 0.0D)
      arrayListMultimap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR_TOUGHNESS.getKey().getKey(), tough, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat))); 
    if (damage > 0.0D)
      arrayListMultimap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ATTACK_DAMAGE.getKey().getKey(), damage, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat))); 
    if (speed > 0.0D)
      arrayListMultimap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(), speed, AttributeModifier.Operation.ADD_NUMBER, guessEquipmentSlotOf(mat))); 
    return (Multimap<Attribute, AttributeModifier>)arrayListMultimap;
  }
  
  public static EquipmentSlot guessEquipmentSlotOf(Material material) {
    String itemName = material.name();
    if (itemName.contains("_HELMET"))
      return EquipmentSlot.HEAD; 
    if (itemName.contains("_CHESTPLATE"))
      return EquipmentSlot.CHEST; 
    if (itemName.contains("_LEGGINGS"))
      return EquipmentSlot.LEGS; 
    if (itemName.contains("_BOOTS"))
      return EquipmentSlot.FEET; 
    return EquipmentSlot.HAND;
  }
  
  public static double getDefaultArmor(Material mat) {
    switch (mat) {
      case LEATHER_HELMET:
        return 1.0D;
      case LEATHER_CHESTPLATE:
        return 3.0D;
      case LEATHER_LEGGINGS:
        return 2.0D;
      case LEATHER_BOOTS:
        return 1.0D;
      case GOLDEN_HELMET:
        return 2.0D;
      case GOLDEN_CHESTPLATE:
        return 5.0D;
      case GOLDEN_LEGGINGS:
        return 3.0D;
      case GOLDEN_BOOTS:
        return 1.0D;
      case CHAINMAIL_HELMET:
        return 2.0D;
      case CHAINMAIL_CHESTPLATE:
        return 5.0D;
      case CHAINMAIL_LEGGINGS:
        return 4.0D;
      case CHAINMAIL_BOOTS:
        return 1.0D;
      case IRON_HELMET:
        return 2.0D;
      case IRON_CHESTPLATE:
        return 6.0D;
      case IRON_LEGGINGS:
        return 5.0D;
      case IRON_BOOTS:
        return 2.0D;
      case DIAMOND_HELMET:
        return 3.0D;
      case DIAMOND_CHESTPLATE:
        return 8.0D;
      case DIAMOND_LEGGINGS:
        return 6.0D;
      case DIAMOND_BOOTS:
        return 3.0D;
      case NETHERITE_HELMET:
        return 3.0D;
      case NETHERITE_CHESTPLATE:
        return 8.0D;
      case NETHERITE_LEGGINGS:
        return 6.0D;
      case NETHERITE_BOOTS:
        return 3.0D;
      case TURTLE_HELMET:
        return 2.0D;
    } 
    return 0.0D;
  }
  
  public static double getDefaultArmorToughness(Material mat) {
    switch (mat) {
      case DIAMOND_HELMET:
      case DIAMOND_CHESTPLATE:
      case DIAMOND_LEGGINGS:
      case DIAMOND_BOOTS:
        return 2.0D;
      case NETHERITE_HELMET:
      case NETHERITE_CHESTPLATE:
      case NETHERITE_LEGGINGS:
      case NETHERITE_BOOTS:
        return 3.0D;
    } 
    return 0.0D;
  }
  
  public static double getKnockBackResistance(Material mat) {
    switch (mat) {
      case NETHERITE_HELMET:
      case NETHERITE_CHESTPLATE:
      case NETHERITE_LEGGINGS:
      case NETHERITE_BOOTS:
        return 1.0D;
    } 
    return 0.0D;
  }
  
  public static double getDefaultAttackDamage(Material mat) {
    switch (mat) {
      case WOODEN_SWORD:
        return 4.0D;
      case GOLDEN_SWORD:
        return 4.0D;
      case STONE_SWORD:
        return 5.0D;
      case IRON_SWORD:
        return 6.0D;
      case DIAMOND_SWORD:
        return 7.0D;
      case NETHERITE_SWORD:
        return 8.0D;
      case WOODEN_AXE:
        return 7.0D;
      case GOLDEN_AXE:
        return 7.0D;
      case STONE_AXE:
        return 9.0D;
      case IRON_AXE:
        return 9.0D;
      case DIAMOND_AXE:
        return 9.0D;
      case WOODEN_PICKAXE:
        return 2.0D;
      case GOLDEN_PICKAXE:
        return 2.0D;
      case STONE_PICKAXE:
        return 3.0D;
      case IRON_PICKAXE:
        return 4.0D;
      case DIAMOND_PICKAXE:
        return 5.0D;
      case WOODEN_SHOVEL:
        return 2.5D;
      case GOLDEN_SHOVEL:
        return 2.5D;
      case STONE_SHOVEL:
        return 3.5D;
      case IRON_SHOVEL:
        return 4.5D;
      case DIAMOND_SHOVEL:
        return 5.5D;
      case WOODEN_HOE:
        return 1.0D;
      case GOLDEN_HOE:
        return 1.0D;
      case STONE_HOE:
        return 1.0D;
      case IRON_HOE:
        return 1.0D;
      case DIAMOND_HOE:
        return 1.0D;
    } 
    return 0.0D;
  }
  
  public static double getDefaultAttackSpeed(Material mat) {
    switch (mat) {
      case WOODEN_SWORD:
      case GOLDEN_SWORD:
      case STONE_SWORD:
      case IRON_SWORD:
      case DIAMOND_SWORD:
        return 1.6D;
      case WOODEN_AXE:
        return 0.8D;
      case GOLDEN_AXE:
        return 1.0D;
      case STONE_AXE:
        return 0.8D;
      case IRON_AXE:
        return 0.9D;
      case DIAMOND_AXE:
        return 1.0D;
      case WOODEN_PICKAXE:
        return 1.2D;
      case STONE_PICKAXE:
        return 1.2D;
      case GOLDEN_PICKAXE:
        return 1.2D;
      case IRON_PICKAXE:
        return 1.2D;
      case DIAMOND_PICKAXE:
        return 1.2D;
      case WOODEN_SHOVEL:
        return 1.0D;
      case GOLDEN_SHOVEL:
        return 1.0D;
      case STONE_SHOVEL:
        return 1.0D;
      case IRON_SHOVEL:
        return 1.0D;
      case DIAMOND_SHOVEL:
        return 1.0D;
      case WOODEN_HOE:
        return 1.0D;
      case GOLDEN_HOE:
        return 1.0D;
      case STONE_HOE:
        return 2.0D;
      case IRON_HOE:
        return 3.0D;
      case DIAMOND_HOE:
        return 4.0D;
    } 
    return 0.0D;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\DefaultAttributes.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */