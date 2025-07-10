package com.francobm.magicosmetics.nms.v1_16_R3;

import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.nms.NPC.NPC;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.CustomSprayHandler;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.EntityBagHandler;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.EntityBalloonHandler;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.PlayerBagHandler;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.PlayerBalloonHandler;
import com.francobm.magicosmetics.nms.v1_16_R3.cache.RangeManager;
import com.francobm.magicosmetics.nms.version.Version;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityPufferFish;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;

public class VersionHandler extends Version {
  public void setSpectator(Player player) {
    if (player.getGameMode() == GameMode.SPECTATOR)
      return; 
    player.setGameMode(GameMode.SPECTATOR);
    EntityPlayer p = ((CraftPlayer)player).getHandle();
    PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, new EntityPlayer[] { p });
    try {
      Field packetField = packet.getClass().getDeclaredField("b");
      packetField.setAccessible(true);
      Constructor<?> infoDataConstructor = PacketUtil();
      List<Object> list = Collections.singletonList(infoDataConstructor.newInstance(new Object[] { packet, p.getProfile(), Integer.valueOf(p.ping), EnumGamemode.CREATIVE, p.getPlayerListName() }));
      packetField.set(packet, list);
      p.playerConnection.sendPacket((Packet)packet);
      PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
      p.playerConnection.sendPacket((Packet)packetPlayOutGameStateChange);
    } catch (NoSuchFieldException|IllegalAccessException|java.lang.reflect.InvocationTargetException|InstantiationException e) {
      e.printStackTrace();
    } 
  }
  
  public void createNPC(Player player) {
    NPC npc = new NPCHandler();
    npc.addNPC(player);
    npc.spawnNPC(player);
  }
  
  public void createNPC(Player player, Location location) {
    NPC npc = new NPCHandler();
    npc.addNPC(player, location);
    npc.spawnNPC(player);
  }
  
  public NPC getNPC(Player player) {
    return (NPC)NPC.npcs.get(player.getUniqueId());
  }
  
  public void removeNPC(Player player) {
    NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
    if (npc == null)
      return; 
    npc.removeNPC(player);
    NPC.npcs.remove(player.getUniqueId());
  }
  
  public NPC getNPC() {
    return new NPCHandler();
  }
  
  public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackForMe) {
    return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackForMe);
  }
  
  public EntityBag createEntityBag(Entity entity, double distance) {
    return (EntityBag)new EntityBagHandler(entity, distance);
  }
  
  public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
    return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
  }
  
  public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
    return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
  }
  
  public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
    return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
  }
  
  public Constructor<?> PacketUtil() {
    try {
      String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
      Class<?> clazz = Class.forName("net.minecraft.server." + version + ".PacketPlayOutPlayerInfo$PlayerInfoData");
      return clazz.getDeclaredConstructor(new Class[] { PacketPlayOutPlayerInfo.class, GameProfile.class, int.class, EnumGamemode.class, IChatBaseComponent.class });
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
    List<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
    switch (itemSlot) {
      case MAIN_HAND:
        list.add(new Pair(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case OFF_HAND:
        list.add(new Pair(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case BOOTS:
        list.add(new Pair(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case LEGGINGS:
        list.add(new Pair(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case CHESTPLATE:
        list.add(new Pair(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case HELMET:
        list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
        break;
    } 
    for (Player p : Bukkit.getOnlinePlayers()) {
      PlayerConnection connection = (((CraftPlayer)p).getHandle()).playerConnection;
      connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
      if (!livingEntity.getUniqueId().equals(p.getUniqueId()) || 
        !(livingEntity instanceof Player))
        continue; 
      Player player = (Player)livingEntity;
      SoundCategory category = SoundCategory.PLAYERS;
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_CHAIN, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_LEATHER, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_IRON, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_DIAMOND, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_GOLD, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_GENERIC, category);
      player.stopSound(Sound.ITEM_ARMOR_EQUIP_NETHERITE, category);
    } 
  }
  
  public void updateTitle(Player player, String title) {
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
      return; 
    PacketPlayOutOpenWindow packet = null;
    switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
      case 1:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X1, (IChatBaseComponent)new ChatMessage(title));
        break;
      case 2:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X2, (IChatBaseComponent)new ChatMessage(title));
        break;
      case 3:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X3, (IChatBaseComponent)new ChatMessage(title));
        break;
      case 4:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X4, (IChatBaseComponent)new ChatMessage(title));
        break;
      case 5:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X5, (IChatBaseComponent)new ChatMessage(title));
        break;
      case 6:
        packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X6, (IChatBaseComponent)new ChatMessage(title));
        break;
    } 
    if (packet == null)
      return; 
    entityPlayer.playerConnection.sendPacket((Packet)packet);
    entityPlayer.updateInventory(entityPlayer.activeContainer);
  }
  
  public void setCamera(Player player, Entity entity) {
    Entity e = ((CraftEntity)entity).getHandle();
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutCamera(e));
  }
  
  public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
    if (itemStack == null)
      return null; 
    ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
    itemCosmetic.getOrCreateTag().setString("magic_cosmetic", key);
    return CraftItemStack.asBukkitCopy(itemCosmetic);
  }
  
  public String isNBTCosmetic(ItemStack itemStack) {
    if (itemStack == null)
      return null; 
    ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
    return itemCosmetic.getOrCreateTag().getString("magic_cosmetic");
  }
  
  public PufferFish spawnFakePuffer(Location location) {
    EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.PUFFERFISH, (World)((CraftWorld)location.getWorld()).getHandle());
    entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    return (PufferFish)entityPufferFish.getBukkitEntity();
  }
  
  public ArmorStand spawnArmorStand(Location location) {
    EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)((CraftWorld)location.getWorld()).getHandle());
    entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    return (ArmorStand)entityPufferFish.getBukkitEntity();
  }
  
  public void showEntity(LivingEntity entity, Player... viewers) {
    EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
    entityClient.setInvisible(true);
    DataWatcher dataWatcher = entityClient.getDataWatcher();
    PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
    PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.playerConnection.sendPacket((Packet)packet);
      view.playerConnection.sendPacket((Packet)metadata);
    } 
  }
  
  public void despawnFakeEntity(Entity entity, Player... viewers) {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.playerConnection.sendPacket((Packet)packet);
    } 
  }
  
  public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
    EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
    PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.playerConnection.sendPacket((Packet)packet);
    } 
  }
  
  public void updatePositionFakeEntity(Entity leashed, Location location) {
    Entity entity = ((CraftEntity)leashed).getHandle();
    entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
  }
  
  public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
    Entity entity = ((CraftEntity)leashed).getHandle();
    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.playerConnection.sendPacket((Packet)packet);
    } 
  }
  
  public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
    ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
    if (!copy.hasTag())
      return cosmetic; 
    ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
    for (String key : copy.getTag().getKeys()) {
      if (key.equals("display") || key.equals("CustomModelData"))
        continue; 
      if (key.equals("PublicBukkitValues")) {
        NBTTagCompound compound = copy.getTag().getCompound(key);
        NBTTagCompound realCompound = cosmeticItem.getTag().getCompound(key);
        Set<String> keys = compound.getKeys();
        for (String compoundKey : keys)
          realCompound.set(compoundKey, compound.get(compoundKey)); 
        cosmeticItem.getOrCreateTag().set(key, (NBTBase)realCompound);
        continue;
      } 
      cosmeticItem.getOrCreateTag().set(key, copy.getTag().get(key));
    } 
    return CraftItemStack.asBukkitCopy(cosmeticItem);
  }
  
  public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
    ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
    if (!copy.hasTag())
      return itemStack; 
    ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
    if (!realItem.hasTag())
      return itemStack; 
    for (String key : copy.getTag().getKeys()) {
      if (key.equals("display") || key.equals("CustomModelData"))
        continue; 
      if (key.equals("PublicBukkitValues")) {
        NBTTagCompound compound = copy.getTag().getCompound(key);
        NBTTagCompound realCompound = realItem.getTag().getCompound(key);
        Set<String> keys = compound.getKeys();
        for (String compoundKey : keys) {
          if (!realCompound.hasKey(compoundKey))
            continue; 
          realCompound.set(compoundKey, compound.get(compoundKey));
        } 
        realItem.getTag().set(key, (NBTBase)realCompound);
        continue;
      } 
      if (!realItem.getTag().hasKey(key))
        continue; 
      realItem.getTag().set(key, copy.getTag().get(key));
    } 
    return CraftItemStack.asBukkitCopy(realItem);
  }
  
  public ItemStack getCustomHead(ItemStack itemStack, String texture) {
    if (itemStack == null)
      return null; 
    if (texture.isEmpty())
      return itemStack; 
    SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
    if (skullMeta == null)
      return itemStack; 
    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
    gameProfile.getProperties().put("textures", new Property("textures", texture));
    try {
      Field profileField = skullMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(skullMeta, gameProfile);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    itemStack.setItemMeta((ItemMeta)skullMeta);
    return itemStack;
  }
  
  public IRangeManager createRangeManager(Entity entity) {
    WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
    PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.getChunkProvider()).playerChunkMap.trackedEntities.get(entity.getEntityId());
    return (IRangeManager)new RangeManager(trackedEntity);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */