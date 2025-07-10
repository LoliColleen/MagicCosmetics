package com.francobm.magicosmetics.cache.cosmetics.backpacks;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.utils.OffsetModel;
import com.francobm.magicosmetics.utils.PositionModelType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BackPackEngine {
  private UUID backPackUniqueId;
  
  private final String modelId;
  
  private final List<String> colorParts;
  
  private final String idle_animation;
  
  private final double distance;
  
  private final OffsetModel offsetModel;
  
  private final PositionModelType positionModelType;
  
  public BackPackEngine(String modelId, List<String> colorParts, String idle_animation, double distance, OffsetModel offsetModel, PositionModelType positionModelType) {
    this.modelId = modelId;
    this.colorParts = colorParts;
    this.idle_animation = (idle_animation == null) ? "idle" : idle_animation;
    this.distance = distance;
    this.offsetModel = offsetModel;
    this.positionModelType = positionModelType;
  }
  
  public BackPackEngine getClone() {
    return new BackPackEngine(this.modelId, new ArrayList<>(this.colorParts), this.idle_animation, this.distance, this.offsetModel, this.positionModelType);
  }
  
  public void remove() {
    if (this.backPackUniqueId == null)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    plugin.getModelEngine().removeBackPack(this.backPackUniqueId, this.modelId);
  }
  
  public Set<String> getBones() {
    if (this.backPackUniqueId == null)
      return null; 
    return MagicCosmetics.getInstance().getModelEngine().getAllBonesIds(this.backPackUniqueId, this.modelId);
  }
  
  public void spawnModel(Entity owner) {
    this.backPackUniqueId = owner.getUniqueId();
    MagicCosmetics.getInstance().getModelEngine().spawnModelBackPack(owner, this.modelId, owner.getLocation(), this.offsetModel, this.positionModelType);
  }
  
  public void showModel(Player player) {
    MagicCosmetics.getInstance().getModelEngine().showModel(this.backPackUniqueId, player);
  }
  
  public void hideModel(Player player) {
    MagicCosmetics.getInstance().getModelEngine().hideModel(this.backPackUniqueId, player);
  }
  
  public void tintModel(Entity owner, Color color) {
    if (color == null)
      return; 
    if (this.backPackUniqueId == null)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    for (String id : getBones()) {
      if (getColorParts() != null && !getColorParts().isEmpty() && 
        !getColorParts().contains(id))
        continue; 
      plugin.getModelEngine().tint(this.backPackUniqueId, this.modelId, color, id);
    } 
  }
  
  public String getModelId() {
    return this.modelId;
  }
  
  public List<String> getColorParts() {
    return this.colorParts;
  }
  
  public UUID getBackPackUniqueId() {
    return this.backPackUniqueId;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\BackPackEngine.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */