package com.francobm.magicosmetics.cache.items;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.Color;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.SecondaryColor;
import com.francobm.magicosmetics.files.FileCreator;
import com.francobm.magicosmetics.utils.XMaterial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Items {
  public static Map<String, Items> items = new HashMap<>();
  
  private final String id;
  
  private final List<String> loreAvailable;
  
  private final List<String> loreUnselect;
  
  private final List<String> loreUnavailable;
  
  private final ItemStack itemStack;
  
  private Color dyeColor;
  
  public Items(String id, ItemStack itemStack, List<String> loreAvailable, List<String> loreUnselect, List<String> loreUnavailable) {
    this.id = id;
    this.itemStack = itemStack;
    this.loreAvailable = loreAvailable;
    this.loreUnselect = loreUnselect;
    this.loreUnavailable = loreUnavailable;
  }
  
  public Items(ItemStack itemStack) {
    this.id = (new Random()).toString();
    this.itemStack = itemStack;
    this.loreUnselect = new ArrayList<>();
    this.loreAvailable = new ArrayList<>();
    this.loreUnavailable = new ArrayList<>();
  }
  
  public Items(String id, ItemStack itemStack) {
    this.id = id;
    this.itemStack = itemStack;
    this.loreUnselect = new ArrayList<>();
    this.loreAvailable = new ArrayList<>();
    this.loreUnavailable = new ArrayList<>();
  }
  
  public static Items getItem(String id) {
    return items.get(id);
  }
  
  public static void loadItems() {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    items.clear();
    FileCreator menu = MagicCosmetics.getInstance().getMenus();
    int items_count = 0;
    for (String key : menu.getConfigurationSection("items").getKeys(false)) {
      String display = "";
      String material = "";
      ItemStack itemStack = null;
      List<String> loreAvailable = null;
      List<String> loreUnselect = null;
      List<String> loreUnavailable = null;
      int amount = 0;
      boolean glow = false;
      int modelData = 0;
      if (menu.contains("items." + key + ".item.display"))
        display = menu.getString("items." + key + ".item.display"); 
      if (menu.contains("items." + key + ".item.material")) {
        material = menu.getString("items." + key + ".item.material");
        try {
          itemStack = XMaterial.valueOf(material.toUpperCase()).parseItem();
        } catch (IllegalArgumentException exception) {
          plugin.getLogger().warning("Item '" + key + "' material: " + material + " Not Found.");
        } 
      } 
      List<String> lore = null;
      if (menu.contains("items." + key + ".item.lore")) {
        lore = new ArrayList<>();
        for (String l : menu.getStringList("items." + key + ".item.lore"))
          lore.add(l
              .replace("%hats_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.HAT)))
              .replace("%bags_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.BAG)))
              .replace("%wsticks_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.WALKING_STICK)))
              .replace("%balloons_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.BALLOON)))
              .replace("%sprays_count%", String.valueOf(Cosmetic.getCosmeticCount(CosmeticType.SPRAY)))); 
      } 
      if (menu.contains("items." + key + ".item.lore-available"))
        loreAvailable = menu.getStringList("items." + key + ".item.lore-available"); 
      if (menu.contains("items." + key + ".item.lore-unavailable"))
        loreUnavailable = menu.getStringList("items." + key + ".item.lore-unavailable"); 
      if (menu.contains("items." + key + ".item.lore-selected"))
        loreAvailable = menu.getStringList("items." + key + ".item.lore-selected"); 
      if (menu.contains("items." + key + ".item.lore-notselected"))
        loreUnselect = menu.getStringList("items." + key + ".item.lore-notselected"); 
      if (menu.contains("items." + key + ".item.amount"))
        amount = menu.getInt("items." + key + ".item.amount"); 
      if (menu.contains("items." + key + ".item.glow"))
        glow = menu.getBoolean("items." + key + ".item.glow"); 
      if (menu.contains("items." + key + ".item.modeldata"))
        modelData = menu.getInt("items." + key + ".item.modeldata"); 
      String texture = "";
      if (menu.contains("items." + key + ".item.texture"))
        texture = menu.getString("items." + key + ".item.texture"); 
      if (menu.contains("items." + key + ".item.item-adder")) {
        if (!plugin.isItemsAdder()) {
          plugin.getLogger().warning("Item Adder plugin Not Found skipping Menu Item '" + key + "'");
          continue;
        } 
        String id = menu.getString("items." + key + ".item.item-adder");
        if (plugin.getItemsAdder().getCustomStack(id) == null) {
          plugin.getLogger().warning("IA Item: '" + id + "' Not Found skipping...");
          continue;
        } 
        itemStack = plugin.getItemsAdder().getCustomItemStack(id).clone();
        modelData = -1;
      } 
      if (menu.contains("items." + key + ".item.oraxen")) {
        if (!plugin.isOraxen()) {
          plugin.getLogger().warning("Oraxen plugin Not Found skipping Menu Item '" + key + "'");
          continue;
        } 
        String id = menu.getString("items." + key + ".item.oraxen");
        ItemStack oraxen = plugin.getOraxen().getItemStackById(id);
        if (oraxen == null) {
          plugin.getLogger().warning("Oraxen item:  '" + id + "' Not Found skipping...");
          continue;
        } 
        itemStack = oraxen.clone();
        modelData = -1;
      } 
      itemStack.setAmount(amount);
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setDisplayName(display);
      itemMeta.setLore(lore);
      if (glow) {
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
      } 
      if (modelData != -1)
        itemMeta.setCustomModelData(Integer.valueOf(modelData)); 
      itemStack.setItemMeta(itemMeta);
      if (itemStack.getType() == Material.PLAYER_HEAD)
        itemStack = plugin.getVersion().getCustomHead(itemStack, texture); 
      items.put(key, new Items(key, itemStack, loreAvailable, loreUnselect, loreUnavailable));
      items_count++;
    } 
    plugin.getLogger().info("Registered items: " + items_count);
  }
  
  public ItemStack addLore() {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = this.itemStack.clone();
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null)
      return null; 
    if (itemMeta.getLore() != null) {
      List<String> lore = itemMeta.getLore();
      if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
        lore.addAll(getLoreAvailable()); 
      itemMeta.setLore(lore);
    } else {
      itemMeta.setLore(getLoreAvailable());
    } 
    return itemStack;
  }
  
  public Items addVariable(String variable, String string) {
    if (this.itemStack == null)
      return this; 
    ItemMeta itemMeta = this.itemStack.getItemMeta();
    if (itemMeta == null)
      return this; 
    if (itemMeta.hasDisplayName())
      itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, string)); 
    List<String> lore = new ArrayList<>();
    if (itemMeta.getLore() != null)
      for (String l : itemMeta.getLore())
        lore.add(l.replace(variable, string));  
    itemMeta.setLore(lore);
    this.itemStack.setItemMeta(itemMeta);
    return this;
  }
  
  public Items addVariable(String variable, CosmeticType cosmeticType) {
    if (this.itemStack == null)
      return this; 
    ItemMeta itemMeta = this.itemStack.getItemMeta();
    List<String> lore = new ArrayList<>();
    FileCreator messages = MagicCosmetics.getInstance().getMessages();
    if (itemMeta == null)
      return this; 
    switch (cosmeticType) {
      case HAT:
        if (itemMeta.hasDisplayName())
          itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.hat"))); 
        if (itemMeta.getLore() != null)
          for (String l : itemMeta.getLore())
            lore.add(l.replace(variable, messages.getString("types.hat")));  
        break;
      case BAG:
        if (itemMeta.hasDisplayName())
          itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.bag"))); 
        if (itemMeta.getLore() != null)
          for (String l : itemMeta.getLore())
            lore.add(l.replace(variable, messages.getString("types.bag")));  
        break;
      case WALKING_STICK:
        if (itemMeta.hasDisplayName())
          itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.wstick"))); 
        if (itemMeta.getLore() != null)
          for (String l : itemMeta.getLore())
            lore.add(l.replace(variable, messages.getString("types.wstick")));  
        break;
      case BALLOON:
        if (itemMeta.hasDisplayName())
          itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.balloon"))); 
        if (itemMeta.getLore() != null)
          for (String l : itemMeta.getLore())
            lore.add(l.replace(variable, messages.getString("types.balloon")));  
        break;
      case SPRAY:
        if (itemMeta.hasDisplayName())
          itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, messages.getString("types.spray"))); 
        if (itemMeta.getLore() != null)
          for (String l : itemMeta.getLore())
            lore.add(l.replace(variable, messages.getString("types.spray")));  
        break;
    } 
    itemMeta.setLore(lore);
    this.itemStack.setItemMeta(itemMeta);
    return this;
  }
  
  public Items addVariable(String variable, int number) {
    if (this.itemStack == null)
      return this; 
    ItemMeta itemMeta = this.itemStack.getItemMeta();
    if (itemMeta == null)
      return this; 
    if (itemMeta.hasDisplayName())
      itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, String.valueOf(number))); 
    List<String> lore = new ArrayList<>();
    if (itemMeta.getLore() != null)
      for (String l : itemMeta.getLore())
        lore.add(l.replace(variable, String.valueOf(number)));  
    itemMeta.setLore(lore);
    this.itemStack.setItemMeta(itemMeta);
    return this;
  }
  
  public Items addPlaceHolder(Player player) {
    if (this.itemStack == null)
      return this; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (!plugin.isPlaceholderAPI())
      return this; 
    ItemMeta itemMeta = this.itemStack.getItemMeta();
    if (itemMeta == null)
      return this; 
    itemMeta.setDisplayName(plugin.getPlaceholderAPI().setPlaceholders(player, itemMeta.getDisplayName()));
    if (itemMeta.getLore() != null)
      itemMeta.setLore(plugin.getPlaceholderAPI().setPlaceholders(player, itemMeta.getLore())); 
    this.itemStack.setItemMeta(itemMeta);
    return this;
  }
  
  public ItemStack addVariableItem(String variable, int number) {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = this.itemStack.clone();
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null)
      return itemStack; 
    itemMeta.setDisplayName(itemMeta.getDisplayName().replace(variable, String.valueOf(number)));
    List<String> lore = new ArrayList<>();
    if (itemMeta.getLore() != null)
      for (String l : itemMeta.getLore())
        lore.add(l.replace(variable, String.valueOf(number)));  
    itemMeta.setLore(lore);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
  
  public boolean isHead() {
    if (this.itemStack == null)
      return false; 
    return (this.itemStack.getType() == XMaterial.PLAYER_HEAD.parseMaterial());
  }
  
  public ItemStack copyItem(Color color, Color compare) {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = color.getPrimaryItem().clone();
    itemStack.setAmount(this.itemStack.getAmount());
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null)
      return itemStack; 
    if (itemMeta.hasLore()) {
      List<String> lore = new ArrayList<>();
      if (color.getId().equalsIgnoreCase(compare.getId())) {
        if (getLoreAvailable() != null || !getLoreAvailable().isEmpty()) {
          lore.addAll(getLoreAvailable());
          if (itemMeta.hasLore())
            lore.addAll(itemMeta.getLore()); 
        } 
      } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
        lore.addAll(getLoreUnselect());
        if (itemMeta.hasLore())
          lore.addAll(itemMeta.getLore()); 
      } 
      itemMeta.setLore(lore);
    } 
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
  
  public ItemStack copyItem(PlayerData playerData, Cosmetic cosmetic, ItemStack head) {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = head.clone();
    if (!isHead())
      return itemStack; 
    itemStack.setAmount(this.itemStack.getAmount());
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null)
      return itemStack; 
    if (this.itemStack.getItemMeta() == null)
      return itemStack; 
    if (MagicCosmetics.getInstance().isPermissions()) {
      if (!cosmetic.hasPermission(playerData.getOfflinePlayer().getPlayer())) {
        if (itemMeta.getLore() != null) {
          List<String> lore = itemMeta.getLore();
          lore.addAll(this.loreUnavailable);
          itemMeta.setLore(lore);
        } else {
          itemMeta.setLore(this.loreUnavailable);
        } 
      } else if (itemMeta.getLore() != null) {
        List<String> lore = itemMeta.getLore();
        lore.addAll(this.loreAvailable);
        itemMeta.setLore(lore);
      } else {
        itemMeta.setLore(this.loreAvailable);
      } 
    } else if (playerData.getCosmeticById(cosmetic.getId()) == null) {
      if (itemMeta.getLore() != null) {
        List<String> lore = itemMeta.getLore();
        lore.addAll(this.loreUnavailable);
        itemMeta.setLore(lore);
      } else {
        itemMeta.setLore(this.loreUnavailable);
      } 
    } else if (itemMeta.getLore() != null) {
      List<String> lore = itemMeta.getLore();
      lore.addAll(this.loreAvailable);
      itemMeta.setLore(lore);
    } else {
      itemMeta.setLore(this.loreAvailable);
    } 
    itemMeta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
    if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
      itemMeta.addEnchant(Enchantment.DURABILITY, 1, true); 
    itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE });
    itemMeta.setUnbreakable(this.itemStack.getItemMeta().isUnbreakable());
    if (!cosmetic.isTexture() && 
      itemStack.getType() == Material.PLAYER_HEAD) {
      SkullMeta skullMeta = (SkullMeta)itemMeta;
      skullMeta.setOwningPlayer(playerData.getOfflinePlayer());
      itemStack.setItemMeta((ItemMeta)skullMeta);
      return itemStack;
    } 
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
  
  public ItemStack colorItem(Color color, Color compare) {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = this.itemStack.clone();
    itemStack.setAmount(this.itemStack.getAmount());
    if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getPrimaryColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getId().equalsIgnoreCase(compare.getId())) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          meta.setLore(lore);
        } else if (color.getId().equalsIgnoreCase(compare.getId())) {
          meta.setLore(getLoreAvailable());
        } else {
          meta.setLore(getLoreUnselect());
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof PotionMeta) {
      PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getPrimaryColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getId().equalsIgnoreCase(compare.getId())) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          meta.setLore(lore);
        } else if (color.getId().equalsIgnoreCase(compare.getId())) {
          meta.setLore(getLoreAvailable());
        } else {
          meta.setLore(getLoreUnselect());
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof MapMeta) {
      MapMeta meta = (MapMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getPrimaryColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getId().equalsIgnoreCase(compare.getId())) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          meta.setLore(lore);
        } else if (color.getId().equalsIgnoreCase(compare.getId())) {
          meta.setLore(getLoreAvailable());
        } else {
          meta.setLore(getLoreUnselect());
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof FireworkEffectMeta) {
      FireworkEffectMeta meta = (FireworkEffectMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setEffect(FireworkEffect.builder().withColor(color.getPrimaryColor()).build());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getId().equalsIgnoreCase(compare.getId())) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          meta.setLore(lore);
        } else if (color.getId().equalsIgnoreCase(compare.getId())) {
          meta.setLore(getLoreAvailable());
        } else {
          meta.setLore(getLoreUnselect());
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    return itemStack;
  }
  
  public ItemStack colorItem(Player player, SecondaryColor color, SecondaryColor compare) {
    if (this.itemStack == null)
      return null; 
    ItemStack itemStack = this.itemStack.clone();
    itemStack.setAmount(this.itemStack.getAmount());
    if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } else {
          List<String> lore = new ArrayList<>();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            lore.addAll(getLoreAvailable());
          } else {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof PotionMeta) {
      PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } else {
          List<String> lore = new ArrayList<>();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            lore.addAll(getLoreAvailable());
          } else {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof MapMeta) {
      MapMeta meta = (MapMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setColor(color.getColor());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } else {
          List<String> lore = new ArrayList<>();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            lore.addAll(getLoreAvailable());
          } else {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (itemStack.getItemMeta() instanceof FireworkEffectMeta) {
      FireworkEffectMeta meta = (FireworkEffectMeta)itemStack.getItemMeta();
      if (meta == null)
        return itemStack; 
      meta.setEffect(FireworkEffect.builder().withColor(color.getColor()).build());
      if (this.itemStack.getItemMeta() != null) {
        if (this.itemStack.getItemMeta().hasDisplayName())
          meta.setDisplayName(this.itemStack.getItemMeta().getDisplayName()); 
        if (this.itemStack.getItemMeta().getLore() != null) {
          List<String> lore = this.itemStack.getItemMeta().getLore();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            if (getLoreAvailable() != null || !getLoreAvailable().isEmpty())
              lore.addAll(getLoreAvailable()); 
          } else if (getLoreUnselect() != null || !getLoreUnselect().isEmpty()) {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } else {
          List<String> lore = new ArrayList<>();
          if (color.getColor().asRGB() == compare.getColor().asRGB()) {
            lore.addAll(getLoreAvailable());
          } else {
            lore.addAll(getLoreUnselect());
          } 
          if (!color.hasPermission(player))
            lore.addAll(getLoreUnavailable()); 
          meta.setLore(lore);
        } 
        meta.addItemFlags((ItemFlag[])this.itemStack.getItemMeta().getItemFlags().toArray((Object[])new ItemFlag[0]));
        if (this.itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))
          meta.addEnchant(Enchantment.DURABILITY, 1, true); 
      } 
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      itemStack.setItemMeta((ItemMeta)meta);
    } 
    return itemStack;
  }
  
  public Items coloredItem(Color color) {
    setDyeColor(color);
    if (this.itemStack == null)
      return this; 
    if (this.itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      LeatherArmorMeta meta = (LeatherArmorMeta)this.itemStack.getItemMeta();
      if (meta == null)
        return this; 
      meta.setColor(color);
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES });
      this.itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (this.itemStack.getItemMeta() instanceof PotionMeta) {
      PotionMeta meta = (PotionMeta)this.itemStack.getItemMeta();
      if (meta == null)
        return this; 
      meta.setColor(color);
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      this.itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (this.itemStack.getItemMeta() instanceof MapMeta) {
      MapMeta meta = (MapMeta)this.itemStack.getItemMeta();
      if (meta == null)
        return this; 
      meta.setColor(color);
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      this.itemStack.setItemMeta((ItemMeta)meta);
    } 
    if (this.itemStack.getItemMeta() instanceof FireworkEffectMeta) {
      FireworkEffectMeta meta = (FireworkEffectMeta)this.itemStack.getItemMeta();
      if (meta == null)
        return this; 
      meta.setEffect(FireworkEffect.builder().withColor(color).build());
      meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS });
      this.itemStack.setItemMeta((ItemMeta)meta);
    } 
    return this;
  }
  
  public boolean isColored(ItemStack itemStack) {
    if (itemStack == null)
      return false; 
    if (this.itemStack == null)
      return false; 
    ItemMeta thisMeta = this.itemStack.getItemMeta();
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (thisMeta == null || itemMeta == null)
      return false; 
    if (thisMeta.getLore() == null || itemMeta.getLore() == null)
      return false; 
    return containsLore(itemMeta.getLore(), thisMeta.getLore());
  }
  
  public boolean containsLore(List<String> lore, List<String> containsLore) {
    if (containsLore == null || containsLore.isEmpty())
      return false; 
    for (String l : containsLore) {
      if (!lore.contains(l))
        continue; 
      return true;
    } 
    return false;
  }
  
  public Color getColor() {
    if (this.itemStack.getItemMeta() == null)
      return null; 
    ItemMeta meta = this.itemStack.getItemMeta();
    if (meta == null)
      return null; 
    if (this.itemStack.getItemMeta() instanceof LeatherArmorMeta)
      return ((LeatherArmorMeta)meta).getColor(); 
    if (this.itemStack.getItemMeta() instanceof PotionMeta)
      return ((PotionMeta)meta).getColor(); 
    if (this.itemStack.getItemMeta() instanceof MapMeta)
      return ((MapMeta)meta).getColor(); 
    if (this.itemStack.getItemMeta() instanceof FireworkEffectMeta)
      return ((FireworkEffectMeta)meta).getEffect().getColors().get(0); 
    return null;
  }
  
  public List<String> getLoreAvailable() {
    return this.loreAvailable;
  }
  
  public List<String> getLoreUnavailable() {
    return this.loreUnavailable;
  }
  
  public List<String> getLoreUnselect() {
    return this.loreUnselect;
  }
  
  public String getId() {
    return this.id;
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
  
  public Color getDyeColor() {
    return this.dyeColor;
  }
  
  public void setDyeColor(Color dyeColor) {
    this.dyeColor = dyeColor;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\items\Items.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */