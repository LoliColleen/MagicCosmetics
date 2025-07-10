package com.francobm.magicosmetics.provider.citizens;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.EntityCache;
import com.francobm.magicosmetics.cache.NPC;
import com.francobm.magicosmetics.cache.NPCType;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Citizens {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public void loadNPCCosmetics() {
    for (NPC npc : this.plugin.getNPCsLoader().getNPCs().values()) {
      if (npc.getNpcType() != NPCType.CITIZENS)
        continue; 
      NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(npc.getId());
      if (citizensNPC == null)
        continue; 
      loadNPC(citizensNPC, npc);
    } 
  }
  
  public NPC getNPC(UUID uuid) {
    return CitizensAPI.getNPCRegistry().getByUniqueId(uuid);
  }
  
  public List<String> getNPCs() {
    List<String> list = new ArrayList<>();
    for (NPC npc : CitizensAPI.getNPCRegistry().sorted())
      list.add(String.valueOf(npc.getId())); 
    return list;
  }
  
  public void EquipmentNPC(ItemSlot itemSlot, UUID uuid, ItemStack itemStack) {
    NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(uuid);
    if (npc == null)
      return; 
    Equipment equipment = (Equipment)npc.getOrAddTrait(Equipment.class);
    this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> {
          switch (itemSlot) {
            case HELMET:
              equipment.set(Equipment.EquipmentSlot.HELMET, itemStack.clone());
              return;
            case CHESTPLATE:
              equipment.set(Equipment.EquipmentSlot.CHESTPLATE, itemStack.clone());
              return;
            case LEGGINGS:
              equipment.set(Equipment.EquipmentSlot.LEGGINGS, itemStack.clone());
              return;
            case BOOTS:
              equipment.set(Equipment.EquipmentSlot.BOOTS, itemStack.clone());
              return;
            case MAIN_HAND:
              equipment.set(Equipment.EquipmentSlot.HAND, itemStack.clone());
              return;
            case OFF_HAND:
              equipment.set(Equipment.EquipmentSlot.OFF_HAND, itemStack.clone());
              break;
          } 
        });
  }
  
  public void equipCosmetic(CommandSender sender, String npcID, String id, String colorHex) {
    try {
      int ID = Integer.parseInt(npcID);
      NPC npc = CitizensAPI.getNPCRegistry().getById(ID);
      if (npc == null) {
        if (sender != null)
          Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
        return;
      } 
      NPC npcRegistry = this.plugin.getNPCsLoader().getNPC(ID, NPCType.CITIZENS);
      if (npcRegistry == null) {
        EntityCache entityCache1 = EntityCache.getEntityOrCreate(npc.getEntity());
        if (this.plugin.getUser() == null)
          return; 
        Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(id);
        Color color1 = null;
        if (colorHex != null)
          color1 = Utils.hex2Rgb(colorHex); 
        if (cosmetic1 == null)
          return; 
        if (entityCache1.hasEquipped(cosmetic1)) {
          entityCache1.unSetCosmetic(cosmetic1.getCosmeticType());
          return;
        } 
        if (color1 != null)
          cosmetic1.setColor(color1); 
        entityCache1.setCosmetic(cosmetic1);
        this.plugin.getNPCsLoader().addNPC(ID, NPCType.CITIZENS, entityCache1, id);
        if (this.plugin.equipMessage && sender != null)
          Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
        return;
      } 
      EntityCache entityCache = npcRegistry.getEntityCache();
      if (entityCache == null) {
        entityCache = EntityCache.getEntityOrCreate(npc.getEntity());
        npcRegistry.setEntityCache(entityCache);
      } 
      Cosmetic cosmetic = Cosmetic.getCloneCosmetic(id);
      Color color = null;
      if (colorHex != null)
        color = Utils.hex2Rgb(colorHex); 
      if (cosmetic == null)
        return; 
      if (entityCache.hasEquipped(cosmetic)) {
        entityCache.unSetCosmetic(cosmetic.getCosmeticType());
        return;
      } 
      if (color != null)
        cosmetic.setColor(color); 
      entityCache.setCosmetic(cosmetic);
      if (this.plugin.equipMessage && sender != null)
        Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix); 
    } catch (NumberFormatException exception) {
      if (sender == null)
        return; 
      Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
    } 
  }
  
  public void loadNPC(NPC citizensNPc, NPC npc) {
    EntityCache entityCache = EntityCache.getEntityOrCreate(citizensNPc.getEntity());
    if (this.plugin.getUser() == null)
      return; 
    entityCache.loadCosmetics(npc.getCosmetics());
    npc.setEntityCache(entityCache);
    npc.setLoad(true);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\citizens\Citizens.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */