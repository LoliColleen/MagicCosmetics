package com.francobm.magicosmetics.loaders;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.EntityCache;
import com.francobm.magicosmetics.cache.NPC;
import com.francobm.magicosmetics.cache.NPCType;
import com.francobm.magicosmetics.files.FileCreator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public class NPCsLoader {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  private final Map<String, NPC> NPCs;
  
  public NPCsLoader() {
    this.NPCs = new HashMap<>();
    load();
  }
  
  public void load() {
    this.NPCs.clear();
    FileCreator fileCreator = this.plugin.getNPCs();
    ConfigurationSection configurationSection = fileCreator.getConfigurationSection("NPCs");
    if (configurationSection == null)
      return; 
    int i = 0;
    for (String key : configurationSection.getKeys(false)) {
      for (String keyId : fileCreator.getConfigurationSection("NPCs." + key).getKeys(false)) {
        int id;
        String cosmetics;
        UUID uuid;
        switch (key.toLowerCase()) {
          case "citizens":
            id = Integer.parseInt(keyId);
            cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
            this.NPCs.put("" + id + id, new NPC(id, NPCType.CITIZENS, cosmetics));
            i++;
          case "znpcsplus":
            id = Integer.parseInt(keyId);
            cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
            this.NPCs.put("" + id + id, new NPC(id, NPCType.Z_NPC_PLUS, cosmetics));
            i++;
          case "vanilla":
            uuid = UUID.fromString(keyId);
            cosmetics = fileCreator.getString("NPCs." + key + "." + keyId);
            this.NPCs.put(keyId + keyId, new NPC(uuid, NPCType.VANILLA, cosmetics));
            i++;
        } 
      } 
    } 
    this.plugin.getLogger().info("" + i + " NPCs has been loaded");
  }
  
  public void addNPC(int id, NPCType npcType, EntityCache entityCache, String cosmeticId) {
    NPC npc = new NPC(id, npcType, cosmeticId);
    npc.setEntityCache(entityCache);
    this.NPCs.put("" + id + id, npc);
  }
  
  public boolean hasNPC(int id, NPCType npcType) {
    return this.NPCs.containsKey("" + id + id);
  }
  
  public NPC getNPC(int id, NPCType npcType) {
    return this.NPCs.get("" + id + id);
  }
  
  public void removeNPC(int id, NPCType npcType) {
    NPC npc = getNPC(id, npcType);
    if (npc == null)
      return; 
    EntityCache entityCache = npc.getEntityCache();
    if (entityCache != null) {
      entityCache.clearCosmeticsInUse();
      EntityCache.removeEntity(entityCache.getUniqueId());
    } 
    this.NPCs.remove("" + id + id);
  }
  
  public void save() {
    FileCreator fileCreator = this.plugin.getNPCs();
    fileCreator.set("NPCs", null);
    int i = 0;
    for (NPC npc : this.NPCs.values()) {
      if (npc.getEntityCache() == null)
        continue; 
      switch (npc.getNpcType()) {
        case CITIZENS:
          fileCreator.set("NPCs.Citizens." + npc.getId(), npc.getEntityCache().saveCosmetics());
          break;
        case Z_NPC_PLUS:
          fileCreator.set("NPCs.ZNPCsPlus." + npc.getId(), npc.getEntityCache().saveCosmetics());
          break;
        case VANILLA:
          fileCreator.set("NPCs.Vanilla." + String.valueOf(((Entity)npc.getNpc()).getUniqueId()), npc.getEntityCache().saveCosmetics());
          break;
      } 
      i++;
    } 
    this.plugin.getLogger().info("Data for " + i + " NPCs have been saved.");
    fileCreator.save();
  }
  
  public Map<String, NPC> getNPCs() {
    return this.NPCs;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\loaders\NPCsLoader.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */