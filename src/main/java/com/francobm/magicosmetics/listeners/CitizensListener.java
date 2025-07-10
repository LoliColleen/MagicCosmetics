package com.francobm.magicosmetics.listeners;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.EntityCache;
import com.francobm.magicosmetics.cache.NPC;
import com.francobm.magicosmetics.cache.NPCType;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCRemoveByCommandSenderEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.NPCTeleportEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensListener implements Listener {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  @EventHandler
  public void onLoad(NPCSpawnEvent event) {
    NPC cNPC = event.getNPC();
    NPC npc = this.plugin.getNPCsLoader().getNPC(cNPC.getId(), NPCType.CITIZENS);
    if (npc == null)
      return; 
    this.plugin.getCitizens().loadNPC(cNPC, npc);
  }
  
  @EventHandler
  public void onRemove(NPCRemoveByCommandSenderEvent event) {
    this.plugin.getNPCsLoader().removeNPC(event.getNPC().getId(), NPCType.CITIZENS);
  }
  
  @EventHandler
  public void onDespawn(NPCDespawnEvent event) {
    NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
    if (npc == null)
      return; 
    EntityCache entityCache = npc.getEntityCache();
    if (entityCache == null)
      return; 
    entityCache.clearCosmeticsInUse();
  }
  
  @EventHandler
  public void onDeath(NPCDeathEvent event) {
    NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
    if (npc == null)
      return; 
    EntityCache entityCache = npc.getEntityCache();
    if (entityCache == null)
      return; 
    entityCache.clearCosmeticsInUse();
  }
  
  @EventHandler
  public void onTeleport(NPCTeleportEvent event) {
    NPC npc = this.plugin.getNPCsLoader().getNPC(event.getNPC().getId(), NPCType.CITIZENS);
    if (npc == null)
      return; 
    EntityCache entityCache = npc.getEntityCache();
    if (entityCache == null)
      return; 
    entityCache.clearCosmeticsInUse();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\CitizensListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */