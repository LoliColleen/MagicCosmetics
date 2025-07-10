package com.francobm.magicosmetics.events;

import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class CosmeticInventoryUpdateEvent extends PlayerEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private final CosmeticType cosmeticType;
  
  private final Cosmetic cosmetic;
  
  private final ItemStack itemToChange;
  
  public CosmeticInventoryUpdateEvent(Player player, CosmeticType cosmeticType, Cosmetic cosmetic, ItemStack itemToChange) {
    super(player);
    this.cosmeticType = cosmeticType;
    this.cosmetic = cosmetic;
    this.itemToChange = itemToChange;
  }
  
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public Cosmetic getCosmetic() {
    return this.cosmetic;
  }
  
  public CosmeticType getCosmeticType() {
    return this.cosmeticType;
  }
  
  public ItemStack getItemToChange() {
    return this.itemToChange;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\events\CosmeticInventoryUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */