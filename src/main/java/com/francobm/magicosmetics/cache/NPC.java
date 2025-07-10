package com.francobm.magicosmetics.cache;

import java.util.UUID;

public class NPC {
  private int id;
  
  private Object npc;
  
  private final UUID uuid;
  
  private final NPCType npcType;
  
  private String cosmetics;
  
  private EntityCache entityCache;
  
  private boolean load;
  
  public NPC(int id, NPCType npcType, String cosmetics) {
    this.id = id;
    this.npcType = npcType;
    this.cosmetics = cosmetics;
    this.uuid = null;
  }
  
  public NPC(UUID uuid, NPCType npcType, String cosmetics) {
    this.uuid = uuid;
    this.npcType = npcType;
    this.cosmetics = cosmetics;
  }
  
  public int getId() {
    return this.id;
  }
  
  public NPCType getNpcType() {
    return this.npcType;
  }
  
  public Object getNpc() {
    return this.npc;
  }
  
  public void setNpc(Object npc) {
    this.npc = npc;
  }
  
  public String getCosmetics() {
    return this.cosmetics;
  }
  
  public void setCosmetics(String cosmetics) {
    this.cosmetics = cosmetics;
  }
  
  public UUID getUuid() {
    return this.uuid;
  }
  
  public void setEntityCache(EntityCache entityCache) {
    this.entityCache = entityCache;
  }
  
  public EntityCache getEntityCache() {
    return this.entityCache;
  }
  
  public boolean isVanilla() {
    return (this.uuid != null);
  }
  
  public void setLoad(boolean load) {
    this.load = load;
  }
  
  public boolean isLoad() {
    return this.load;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\NPC.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */