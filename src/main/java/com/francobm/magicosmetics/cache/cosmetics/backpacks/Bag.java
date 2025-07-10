package com.francobm.magicosmetics.cache.cosmetics.backpacks;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.francobm.magicosmetics.utils.XMaterial;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;

public class Bag extends Cosmetic {
  private PlayerBag bag1;
  
  private EntityBag bag2;
  
  private ItemStack bagForMe;
  
  private BackPackEngine backPackEngine;
  
  private double space;
  
  private boolean hide = false;
  
  private boolean spectator = false;
  
  private double distance;
  
  private float height;
  
  public Bag(String id, String name, ItemStack itemStack, int modelData, ItemStack bagForMe, boolean colored, double space, CosmeticType cosmeticType, Color color, double distance, String permission, boolean texture, boolean hideMenu, float height, boolean useEmote, BackPackEngine backPackEngine, NamespacedKey namespacedKey) {
    super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
    this.bagForMe = bagForMe;
    this.space = space;
    this.distance = distance;
    this.height = height;
    this.backPackEngine = backPackEngine;
  }
  
  protected void updateCosmetic(Cosmetic cosmetic) {
    super.updateCosmetic(cosmetic);
    Bag bag = (Bag)cosmetic;
    this.bagForMe = bag.bagForMe;
    this.space = bag.space;
    this.distance = bag.distance;
    this.height = bag.height;
    this.backPackEngine = bag.backPackEngine;
  }
  
  public double getSpace() {
    return this.space;
  }
  
  public void active(Entity entity) {
    if (entity == null)
      return; 
    if (this.backPackEngine != null) {
      if (this.backPackEngine.getBackPackUniqueId() == null) {
        if (entity.isDead())
          return; 
        remove();
        this.backPackEngine.spawnModel(entity);
        if (isColored())
          this.backPackEngine.tintModel(entity, getColor()); 
      } 
      return;
    } 
    if (this.bag2 == null) {
      if (entity.isDead()) {
        remove();
        return;
      } 
      remove();
      this.bag2 = MagicCosmetics.getInstance().getVersion().createEntityBag(entity, this.distance);
      this.bag2.spawnBag();
    } 
    this.bag2.addPassenger();
    this.bag2.setItemOnHelmet(getItemColor());
    this.bag2.lookEntity();
  }
  
  public void lendToEntity() {
    if (this.bag1 == null) {
      if (this.lendEntity.isDead())
        return; 
      remove();
      this.bag1 = MagicCosmetics.getInstance().getVersion().createPlayerBag(this.player, getDistance(), this.height, getItemColor(this.player), (getBagForMe() != null) ? getItemColorForMe(this.player) : null);
      this.bag1.setLendEntityId(this.lendEntity.getEntityId());
      if (this.hide)
        hideSelf(false); 
    } 
    this.bag1.addPassenger(true);
    this.bag1.lookEntity(this.lendEntity.getLocation().getYaw(), this.lendEntity.getLocation().getPitch(), true);
    this.bag1.spawn(true);
    if (this.hide)
      return; 
    this.bag1.spawnSelf(this.player);
    this.bag1.lookEntity(this.lendEntity.getLocation().getYaw(), this.lendEntity.getLocation().getPitch(), false);
  }
  
  public void hide(Player player) {
    if (this.backPackEngine != null) {
      this.backPackEngine.hideModel(player);
      return;
    } 
    if (this.bag1 != null)
      this.bag1.addHideViewer(player); 
  }
  
  public void show(Player player) {
    if (this.backPackEngine != null) {
      this.backPackEngine.showModel(player);
      return;
    } 
    if (this.bag1 != null)
      this.bag1.removeHideViewer(player); 
  }
  
  public void update() {
    if (this.lendEntity != null) {
      lendToEntity();
      return;
    } 
    if (isHideCosmetic()) {
      remove();
      return;
    } 
    if (this.backPackEngine != null) {
      if (this.backPackEngine.getBackPackUniqueId() == null) {
        if (this.player.isDead())
          return; 
        if (this.player.getGameMode() == GameMode.SPECTATOR)
          return; 
        remove();
        this.backPackEngine.spawnModel((Entity)this.player);
        if (isColored())
          this.backPackEngine.tintModel((Entity)this.player, getColor()); 
      } 
      return;
    } 
    if (this.bag1 == null) {
      if (this.player.isDead())
        return; 
      if (this.player.getGameMode() == GameMode.SPECTATOR)
        return; 
      remove();
      this.bag1 = MagicCosmetics.getInstance().getVersion().createPlayerBag(this.player, getDistance(), this.height, getItemColor(this.player), (getBagForMe() != null) ? getItemColorForMe(this.player) : null);
      if (this.hide)
        hideSelf(false); 
      this.bag1.spawn(false);
    } 
    this.bag1.lookEntity(this.player.getLocation().getYaw(), this.player.getLocation().getPitch(), true);
  }
  
  public void remove() {
    if (this.backPackEngine != null)
      this.backPackEngine.remove(); 
    if (this.bag1 != null)
      this.bag1.remove(); 
    if (this.bag2 != null)
      this.bag2.remove(); 
    this.bag1 = null;
    this.bag2 = null;
  }
  
  public void clearClose() {
    if (this.backPackEngine != null)
      this.backPackEngine.remove(); 
    if (this.bag1 != null)
      this.bag1.remove(); 
    if (this.bag2 != null)
      this.bag2.remove(); 
    this.bag1 = null;
    this.bag2 = null;
  }
  
  public void setHeadPos(ArmorStand as, double yaw, double pitch) {
    double yint = Math.cos(yaw / Math.PI);
    double zint = Math.sin(yaw / Math.PI);
    double xint = Math.sin(pitch / Math.PI);
    EulerAngle ea = as.getHeadPose();
    ea.setX(xint);
    ea.setY(yint);
    ea.setZ(zint);
    as.setHeadPose(ea);
  }
  
  public ItemStack getBagForMe() {
    return this.bagForMe;
  }
  
  public ItemStack getItemColorForMe() {
    if (this.bagForMe == null)
      return null; 
    ItemStack itemStack = this.bagForMe.clone();
    if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemStack.getItemMeta();
      if (getColor() != null)
        itemMeta.setColor(getColor()); 
      itemStack.setItemMeta((ItemMeta)itemMeta);
      return itemStack;
    } 
    if (itemStack.getItemMeta() instanceof PotionMeta) {
      PotionMeta itemMeta = (PotionMeta)itemStack.getItemMeta();
      if (getColor() != null)
        itemMeta.setColor(getColor()); 
      itemStack.setItemMeta((ItemMeta)itemMeta);
      return itemStack;
    } 
    if (itemStack.getItemMeta() instanceof MapMeta) {
      MapMeta itemMeta = (MapMeta)itemStack.getItemMeta();
      if (getColor() != null)
        itemMeta.setColor(getColor()); 
      itemStack.setItemMeta((ItemMeta)itemMeta);
      return itemStack;
    } 
    return itemStack;
  }
  
  public ItemStack getItemColorForMe(Player player) {
    if (isTexture())
      return getItemColorForMe(); 
    ItemStack itemStack = getItemColorForMe();
    if (itemStack.getType() != XMaterial.PLAYER_HEAD.parseMaterial())
      return itemStack; 
    SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
    skullMeta.setOwningPlayer((OfflinePlayer)player);
    itemStack.setItemMeta((ItemMeta)skullMeta);
    return itemStack;
  }
  
  public void hideSelf(boolean change) {
    if (this.bag1 == null)
      return; 
    Player player = this.bag1.getPlayer();
    if (change)
      hide(); 
    this.bag1.spawnSelf(player);
  }
  
  public void hide() {
    this.hide = !this.hide;
  }
  
  public void setSpectator(boolean spectator) {
    this.spectator = spectator;
  }
  
  public boolean isSpectator() {
    return this.spectator;
  }
  
  public PlayerBag getBag() {
    return this.bag1;
  }
  
  public double getDistance() {
    return this.distance;
  }
  
  public boolean isHide() {
    return this.hide;
  }
  
  public float getHeight() {
    return this.height;
  }
  
  public BackPackEngine getBackPackEngine() {
    return this.backPackEngine;
  }
  
  public void spawn(Player player) {
    if (this.bag1 != null)
      this.bag1.spawn(player); 
    if (this.bag2 != null)
      this.bag2.spawnBag(player); 
  }
  
  public void despawn(Player player) {
    if (this.bag1 != null)
      this.bag1.remove(player); 
    if (this.bag2 != null)
      this.bag2.remove(player); 
  }
  
  public int getBackpackId() {
    if (this.bag1 == null)
      return -1; 
    return this.bag1.getBackpackId();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\Bag.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */