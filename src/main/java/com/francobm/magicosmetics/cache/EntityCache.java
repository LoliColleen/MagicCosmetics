package com.francobm.magicosmetics.cache;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.utils.XMaterial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityCache {
  public static Map<UUID, EntityCache> entities = new HashMap<>();
  
  private final UUID uniqueId;
  
  private boolean npc = false;
  
  private Entity entity;
  
  private Cosmetic hat;
  
  private Cosmetic bag;
  
  private Cosmetic wStick;
  
  private Cosmetic balloon;
  
  public EntityCache(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }
  
  public EntityCache(Entity entity) {
    this.uniqueId = entity.getUniqueId();
    this.entity = entity;
  }
  
  public static EntityCache getEntity(UUID uniqueId) {
    if (!entities.containsKey(uniqueId))
      return null; 
    return entities.get(uniqueId);
  }
  
  public static EntityCache getEntityOrCreate(Entity entity) {
    if (!entities.containsKey(entity.getUniqueId()))
      entities.put(entity.getUniqueId(), new EntityCache(entity)); 
    return entities.get(entity.getUniqueId());
  }
  
  public static void removeEntity(UUID uniqueId) {
    entities.remove(uniqueId);
  }
  
  public boolean hasEquipped(String cosmeticId) {
    if (this.hat != null && 
      this.hat.getId().equals(cosmeticId))
      return true; 
    if (this.bag != null && 
      this.bag.getId().equals(cosmeticId))
      return true; 
    if (this.wStick != null && 
      this.wStick.getId().equals(cosmeticId))
      return true; 
    if (this.balloon != null)
      return this.balloon.getId().equals(cosmeticId); 
    return false;
  }
  
  public boolean hasEquipped(Cosmetic cosmetic) {
    switch (cosmetic.getCosmeticType()) {
      case HAT:
        if (this.hat == null)
          return false; 
        return this.hat.getId().equals(cosmetic.getId());
      case BAG:
        if (this.bag == null)
          return false; 
        return this.bag.getId().equals(cosmetic.getId());
      case WALKING_STICK:
        if (this.wStick == null)
          return false; 
        return this.wStick.getId().equals(cosmetic.getId());
      case BALLOON:
        if (this.balloon == null)
          return false; 
        return this.balloon.getId().equals(cosmetic.getId());
    } 
    return false;
  }
  
  public void unSetCosmetic(CosmeticType cosmetic) {
    switch (cosmetic) {
      case HAT:
        clearHat();
        this.hat = null;
        break;
      case BAG:
        clearBag();
        this.bag = null;
        break;
      case WALKING_STICK:
        clearWStick();
        this.wStick = null;
        break;
      case BALLOON:
        clearBalloon();
        this.balloon = null;
        break;
    } 
  }
  
  public void setCosmetic(Cosmetic cosmetic) {
    switch (cosmetic.getCosmeticType()) {
      case HAT:
        clearHat();
        this.hat = cosmetic;
        break;
      case BAG:
        clearBag();
        this.bag = cosmetic;
        break;
      case WALKING_STICK:
        clearWStick();
        this.wStick = cosmetic;
        break;
      case BALLOON:
        clearBalloon();
        this.balloon = cosmetic;
        break;
    } 
  }
  
  public void activeCosmetics() {
    activeHat();
    activeBag();
    activeWStick();
    activeBalloon();
  }
  
  public void activeCosmeticsInInventory() {
    activeHat();
    activeBag();
    activeWStick();
  }
  
  public void clearCosmeticsInUse() {
    clearBalloon();
    clearBag();
    clearHat();
    clearWStick();
  }
  
  public void activeHat() {
    if (this.hat == null)
      return; 
    if (!(this.entity instanceof LivingEntity))
      return; 
    if (!this.npc) {
      LivingEntity livingEntity = (LivingEntity)this.entity;
      if (livingEntity instanceof Player) {
        Player player = (Player)livingEntity;
        MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, this.hat.getItemColor(player));
        return;
      } 
      MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, this.hat.getItemColor());
      return;
    } 
    MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.HELMET, getUniqueId(), this.hat.getItemColor());
  }
  
  public void activeBag() {
    if (this.bag == null)
      return; 
    ((Bag)this.bag).active(getEntityOrCreate());
  }
  
  public void activeWStick() {
    if (this.wStick == null)
      return; 
    if (!(this.entity instanceof LivingEntity))
      return; 
    if (!this.npc) {
      LivingEntity livingEntity = (LivingEntity)this.entity;
      if (livingEntity instanceof Player) {
        Player player = (Player)livingEntity;
        MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, this.wStick.getItemColor(player));
        return;
      } 
      MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, this.hat.getItemColor());
      return;
    } 
    MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.OFF_HAND, getUniqueId(), this.wStick.getItemColor());
  }
  
  public void activeBalloon() {
    if (this.balloon == null)
      return; 
    ((Balloon)this.balloon).active(getEntityOrCreate());
  }
  
  public void clearHat() {
    if (this.hat == null)
      return; 
    if (!(this.entity instanceof LivingEntity))
      return; 
    if (!this.npc) {
      LivingEntity livingEntity = (LivingEntity)this.entity;
      MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.HELMET, XMaterial.AIR.parseItem());
      return;
    } 
    MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.HELMET, getUniqueId(), XMaterial.AIR.parseItem());
  }
  
  public void clearBag() {
    if (this.bag == null)
      return; 
    this.bag.remove();
  }
  
  public void clearWStick() {
    if (this.wStick == null)
      return; 
    if (!(this.entity instanceof LivingEntity))
      return; 
    if (!this.npc) {
      LivingEntity livingEntity = (LivingEntity)this.entity;
      MagicCosmetics.getInstance().getVersion().equip(livingEntity, ItemSlot.OFF_HAND, XMaterial.AIR.parseItem());
      return;
    } 
    MagicCosmetics.getInstance().getCitizens().EquipmentNPC(ItemSlot.OFF_HAND, getUniqueId(), XMaterial.AIR.parseItem());
  }
  
  public void clearBalloon() {
    if (this.balloon == null)
      return; 
    this.balloon.remove();
  }
  
  public UUID getUniqueId() {
    return this.uniqueId;
  }
  
  public Entity getEntityOrCreate() {
    return this.entity;
  }
  
  public void setEntity(Entity entity) {
    this.entity = entity;
  }
  
  public Cosmetic getHat() {
    return this.hat;
  }
  
  public Cosmetic getBag() {
    return this.bag;
  }
  
  public Cosmetic getWStick() {
    return this.wStick;
  }
  
  public Cosmetic getBalloon() {
    return this.balloon;
  }
  
  public void loadCosmetics(String ids) {
    if (ids.isEmpty())
      return; 
    List<String> cosmetics = new ArrayList<>(Arrays.asList(ids.split(",")));
    for (String cosmetic : cosmetics) {
      if (cosmetic.isEmpty())
        continue; 
      String[] color = cosmetic.split("\\|");
      if (color.length > 1) {
        Cosmetic cosmetic2 = Cosmetic.getCloneCosmetic(color[0]);
        if (cosmetic2 == null)
          continue; 
        cosmetic2.setColor(Color.fromRGB(Integer.parseInt(color[1])));
        setCosmetic(cosmetic2);
        continue;
      } 
      Cosmetic cosmetic1 = Cosmetic.getCloneCosmetic(cosmetic);
      if (cosmetic1 == null)
        continue; 
      setCosmetic(cosmetic1);
    } 
  }
  
  public String saveCosmetics() {
    return saveHat() + "," + saveHat() + "," + saveBag() + "," + saveWStick();
  }
  
  public String saveHat() {
    if (this.hat == null)
      return ""; 
    if (this.hat.getColor() == null)
      return this.hat.getId(); 
    return this.hat.getId() + "|" + this.hat.getId();
  }
  
  public String saveBag() {
    if (this.bag == null)
      return ""; 
    if (this.bag.getColor() == null)
      return this.bag.getId(); 
    return this.bag.getId() + "|" + this.bag.getId();
  }
  
  public String saveWStick() {
    if (this.wStick == null)
      return ""; 
    if (this.wStick.getColor() == null)
      return this.wStick.getId(); 
    return this.wStick.getId() + "|" + this.wStick.getId();
  }
  
  public String saveBalloon() {
    if (this.balloon == null)
      return ""; 
    if (this.balloon.getColor() == null)
      return this.balloon.getId(); 
    return this.balloon.getId() + "|" + this.balloon.getId();
  }
  
  public boolean isCosmeticUse() {
    return (this.hat != null || this.bag != null || this.wStick != null || this.balloon != null);
  }
  
  public void setNpc(boolean npc) {
    this.npc = npc;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\EntityCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */