package com.francobm.magicosmetics.nms.NPC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class NPC {
  public static Map<UUID, NPC> npcs = new HashMap<>();
  
  protected Entity entity;
  
  protected Entity punch;
  
  protected Entity armorStand;
  
  protected Location balloonPosition;
  
  protected boolean floatLoop = true;
  
  protected double y = 0.0D;
  
  protected double height = 0.0D;
  
  protected boolean heightLoop = true;
  
  protected float rotate = -0.4F;
  
  protected double rot = 0.0D;
  
  protected boolean rotateLoop = true;
  
  protected boolean bigHead = false;
  
  public abstract void spawnNPC(Player paramPlayer);
  
  public abstract void removeNPC(Player paramPlayer);
  
  public abstract void removeBalloon(Player paramPlayer);
  
  public abstract void addNPC(Player paramPlayer);
  
  public abstract void addNPC(Player paramPlayer, Location paramLocation);
  
  public abstract void lookNPC(Player paramPlayer, float paramFloat);
  
  public abstract void equipNPC(Player paramPlayer, ItemSlot paramItemSlot, ItemStack paramItemStack);
  
  public abstract void animation(Player paramPlayer);
  
  public abstract NPC getNPC(Player paramPlayer);
  
  public abstract void addPassenger(Player paramPlayer);
  
  public abstract void balloonNPC(Player paramPlayer, Location paramLocation, ItemStack paramItemStack, boolean paramBoolean);
  
  public abstract void armorStandSetItem(Player paramPlayer, ItemStack paramItemStack);
  
  public abstract void balloonSetItem(Player paramPlayer, ItemStack paramItemStack);
  
  protected void addNPC(NPC npc, Player player) {
    npcs.put(player.getUniqueId(), npc);
  }
  
  public abstract void spawnPunch(Player paramPlayer, Location paramLocation);
  
  public Entity getEntity() {
    return this.entity;
  }
  
  public Entity getPunchEntity() {
    return this.punch;
  }
  
  public boolean isBigHead() {
    return this.bigHead;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\NPC\NPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */