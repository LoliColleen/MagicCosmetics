package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
import com.francobm.magicosmetics.nms.v1_18_R2.VersionHandler;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomPlayer;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ItemsAdder {
  public boolean existModel(String namespaceId) {
    return CustomEntity.isInRegistry(namespaceId);
  }
  
  public CustomStack getCustomStack(String id) {
    return CustomStack.getInstance(id);
  }
  
  public CustomStack getCustomStack(ItemStack itemStack) {
    return CustomStack.byItemStack(itemStack);
  }
  
  public ItemStack getCustomItemStack(String id) {
    CustomStack customStack = CustomStack.getInstance(id);
    if (customStack == null)
      return null; 
    return customStack.getItemStack();
  }
  
  public String replaceFontImageWithoutColor(String id) {
    return ChatColor.stripColor(FontImageWrapper.replaceFontImages(id));
  }
  
  public String replaceFontImages(String id) {
    return FontImageWrapper.replaceFontImages(id);
  }
  
  public void stopEmote(Player player) {
    CustomPlayer.stopEmote(player);
  }
  
  public boolean hasEmote(Player player) {
    try {
      return (CustomPlayer.byAlreadySpawned((Entity)player) != null);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public void balloonEmote(Player player) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    CustomPlayer customPlayer = CustomPlayer.byAlreadySpawned((Entity)player);
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (playerData.getBalloon() == null) {
      plugin.getLogger().info("No balloon found for player " + player.getName());
      return;
    } 
    if (customPlayer == null) {
      plugin.getLogger().warning("Player " + player.getName() + " is not spawned");
      return;
    } 
    ArmorStand armorStand = ((VersionHandler)plugin.getVersion()).createArmor(player);
    ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)armorStand);
    CustomEntity.Bone bone = customPlayer.getBone("pbody_2");
    plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)plugin, task -> {
          if (bone == null) {
            task.cancel();
            ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)player);
            plugin.getLogger().warning("Could not find bone left_arm_slot for player " + player.getName());
            return;
          } 
          if (!hasEmote(player)) {
            task.cancel();
            ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)player);
            plugin.getLogger().warning("ArmorStand for player " + player.getName() + " is invalid");
            return;
          } 
          armorStand.teleport(bone.getLocation());
        }0L, 1L);
  }
  
  public void backPackEmote(Player player) {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    CustomPlayer customPlayer = CustomPlayer.byAlreadySpawned((Entity)player);
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    if (customPlayer == null)
      return; 
    if (playerData.getBag() == null) {
      plugin.getLogger().info("No backpack found for player " + player.getName());
      return;
    } 
    Bag bag = (Bag)playerData.getBag();
    customPlayer.addPassenger((LivingEntity)bag.getBag().getEntity());
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\ItemsAdder.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */