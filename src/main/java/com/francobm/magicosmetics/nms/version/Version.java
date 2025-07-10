package com.francobm.magicosmetics.nms.version;

import com.francobm.magicosmetics.models.PacketReader;
import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.nms.NPC.NPC;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public abstract class Version {
  protected static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");
  
  protected PacketReader packetReader;
  
  public PacketReader getPacketReader() {
    return this.packetReader;
  }
  
  public abstract void setSpectator(Player paramPlayer);
  
  public abstract void createNPC(Player paramPlayer);
  
  public abstract void createNPC(Player paramPlayer, Location paramLocation);
  
  public abstract NPC getNPC(Player paramPlayer);
  
  public abstract void removeNPC(Player paramPlayer);
  
  public abstract NPC getNPC();
  
  public abstract void equip(LivingEntity paramLivingEntity, ItemSlot paramItemSlot, ItemStack paramItemStack);
  
  public abstract void setCamera(Player paramPlayer, Entity paramEntity);
  
  public abstract PlayerBag createPlayerBag(Player paramPlayer, double paramDouble, float paramFloat, ItemStack paramItemStack1, ItemStack paramItemStack2);
  
  public abstract EntityBag createEntityBag(Entity paramEntity, double paramDouble);
  
  public abstract PlayerBalloon createPlayerBalloon(Player paramPlayer, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract EntityBalloon createEntityBalloon(Entity paramEntity, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract CustomSpray createCustomSpray(Player paramPlayer, Location paramLocation, BlockFace paramBlockFace, ItemStack paramItemStack, MapView paramMapView, int paramInt);
  
  public abstract void updateTitle(Player paramPlayer, String paramString);
  
  public abstract ItemStack setNBTCosmetic(ItemStack paramItemStack, String paramString);
  
  public abstract String isNBTCosmetic(ItemStack paramItemStack);
  
  public abstract PufferFish spawnFakePuffer(Location paramLocation);
  
  public abstract ArmorStand spawnArmorStand(Location paramLocation);
  
  public abstract void showEntity(LivingEntity paramLivingEntity, Player... paramVarArgs);
  
  public abstract void despawnFakeEntity(Entity paramEntity, Player... paramVarArgs);
  
  public abstract void attachFakeEntity(Entity paramEntity1, Entity paramEntity2, Player... paramVarArgs);
  
  public abstract void updatePositionFakeEntity(Entity paramEntity, Location paramLocation);
  
  public abstract void teleportFakeEntity(Entity paramEntity, Set<Player> paramSet);
  
  public abstract ItemStack getItemWithNBTsCopy(ItemStack paramItemStack1, ItemStack paramItemStack2);
  
  public abstract ItemStack getItemSavedWithNBTsUpdated(ItemStack paramItemStack1, ItemStack paramItemStack2);
  
  public abstract ItemStack getCustomHead(ItemStack paramItemStack, String paramString);
  
  public abstract IRangeManager createRangeManager(Entity paramEntity);
  
  protected URL getUrlFromBase64(String base64) throws MalformedURLException {
    String decoded = new String(Base64.getDecoder().decode(base64));
    return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\version\Version.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */