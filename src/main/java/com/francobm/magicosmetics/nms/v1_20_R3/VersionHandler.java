package com.francobm.magicosmetics.nms.v1_20_R3;

import com.francobm.magicosmetics.nms.IRangeManager;
import com.francobm.magicosmetics.nms.NPC.ItemSlot;
import com.francobm.magicosmetics.nms.NPC.NPC;
import com.francobm.magicosmetics.nms.bag.EntityBag;
import com.francobm.magicosmetics.nms.bag.PlayerBag;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.francobm.magicosmetics.nms.spray.CustomSpray;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.CustomSprayHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.EntityBagHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.EntityBalloonHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.PlayerBagHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.PlayerBalloonHandler;
import com.francobm.magicosmetics.nms.v1_20_R3.cache.RangeManager;
import com.francobm.magicosmetics.nms.version.Version;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.EntityPufferFish;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
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
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class VersionHandler extends Version {
  public void setSpectator(Player player) {
    player.setGameMode(GameMode.SPECTATOR);
    EntityPlayer p = ((CraftPlayer)player).getHandle();
    ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.c, p);
    try {
      Field packetField = packet.getClass().getDeclaredField("b");
      packetField.setAccessible(true);
      ArrayList<ClientboundPlayerInfoUpdatePacket.b> list = Lists.newArrayList();
      list.add(new ClientboundPlayerInfoUpdatePacket.b(player.getUniqueId(), p.getBukkitEntity().getProfile(), false, 0, EnumGamemode.b, p.N(), null));
      packetField.set(packet, list);
      p.c.b((Packet)packet);
      PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
      p.c.b((Packet)packetPlayOutGameStateChange);
    } catch (NoSuchFieldException|IllegalAccessException e) {
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
  
  public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
    return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackItemForMe);
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
  
  public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
    ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
    switch (itemSlot) {
      case MAIN_HAND:
        list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case OFF_HAND:
        list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case BOOTS:
        list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case LEGGINGS:
        list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case CHESTPLATE:
        list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
        break;
      case HELMET:
        list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
        break;
    } 
    for (Player p : Bukkit.getOnlinePlayers()) {
      PlayerConnection connection = (((CraftPlayer)p).getHandle()).c;
      connection.b((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
    } 
  }
  
  public void updateTitle(Player player, String title) {
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
      return; 
    PacketPlayOutOpenWindow packet = null;
    switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
      case 1:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.a, CraftChatMessage.fromStringOrNull(title));
        break;
      case 2:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.b, CraftChatMessage.fromStringOrNull(title));
        break;
      case 3:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.c, CraftChatMessage.fromStringOrNull(title));
        break;
      case 4:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.d, CraftChatMessage.fromStringOrNull(title));
        break;
      case 5:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.e, CraftChatMessage.fromStringOrNull(title));
        break;
      case 6:
        packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.f, CraftChatMessage.fromStringOrNull(title));
        break;
    } 
    if (packet == null)
      return; 
    entityPlayer.c.b((Packet)packet);
    entityPlayer.bS.b();
  }
  
  public void setCamera(Player player, Entity entity) {
    Entity e = ((CraftEntity)entity).getHandle();
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    entityPlayer.c.b((Packet)new PacketPlayOutCamera(e));
  }
  
  public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
    if (itemStack == null)
      return null; 
    ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
    itemCosmetic.w().a("magic_cosmetic", key);
    return CraftItemStack.asBukkitCopy(itemCosmetic);
  }
  
  public String isNBTCosmetic(ItemStack itemStack) {
    if (itemStack == null)
      return null; 
    ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
    return itemCosmetic.w().l("magic_cosmetic");
  }
  
  public PufferFish spawnFakePuffer(Location location) {
    EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.aC, (World)((CraftWorld)location.getWorld()).getHandle());
    entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    return (PufferFish)entityPufferFish.getBukkitEntity();
  }
  
  public ArmorStand spawnArmorStand(Location location) {
    EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
    entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    return (ArmorStand)entityPufferFish.getBukkitEntity();
  }
  
  public void showEntity(LivingEntity entity, Player... viewers) {
    EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
    entityClient.j(true);
    DataWatcher dataWatcher = entityClient.an();
    PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
    PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher.c());
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.c.b((Packet)packet);
      view.c.b((Packet)metadata);
    } 
  }
  
  public void despawnFakeEntity(Entity entity, Player... viewers) {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.c.b((Packet)packet);
    } 
  }
  
  public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
    EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
    PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.c.b((Packet)packet);
    } 
  }
  
  public void updatePositionFakeEntity(Entity leashed, Location location) {
    Entity entity = ((CraftEntity)leashed).getHandle();
    entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
  }
  
  public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
    Entity entity = ((CraftEntity)leashed).getHandle();
    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
    for (Player viewer : viewers) {
      EntityPlayer view = ((CraftPlayer)viewer).getHandle();
      view.c.b((Packet)packet);
    } 
  }
  
  public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
    ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
    if (!copy.u())
      return cosmetic; 
    ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
    for (String key : copy.v().e()) {
      if (key.equals("display") || key.equals("CustomModelData"))
        continue; 
      if (key.equals("PublicBukkitValues")) {
        NBTTagCompound compound = copy.v().p(key);
        NBTTagCompound realCompound = cosmeticItem.v().p(key);
        Set<String> keys = compound.e();
        for (String compoundKey : keys)
          realCompound.a(compoundKey, compound.c(compoundKey)); 
        cosmeticItem.w().a(key, (NBTBase)realCompound);
        continue;
      } 
      cosmeticItem.w().a(key, copy.v().c(key));
    } 
    return CraftItemStack.asBukkitCopy(cosmeticItem);
  }
  
  public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
    ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
    if (!copy.u())
      return itemStack; 
    ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
    if (!realItem.u())
      return itemStack; 
    for (String key : copy.v().e()) {
      if (key.equals("display") || key.equals("CustomModelData"))
        continue; 
      if (key.equals("PublicBukkitValues")) {
        NBTTagCompound compound = copy.v().p(key);
        NBTTagCompound realCompound = realItem.v().p(key);
        Set<String> keys = compound.e();
        for (String compoundKey : keys) {
          if (!realCompound.e(compoundKey))
            continue; 
          realCompound.a(compoundKey, compound.c(compoundKey));
        } 
        realItem.v().a(key, (NBTBase)realCompound);
        continue;
      } 
      if (!realItem.v().e(key))
        continue; 
      realItem.v().a(key, copy.v().c(key));
    } 
    return CraftItemStack.asBukkitCopy(realItem);
  }
  
  public ItemStack getCustomHead(ItemStack itemStack, String texture) {
    URL urlObject;
    if (itemStack == null)
      return null; 
    if (texture.isEmpty())
      return itemStack; 
    PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
    PlayerTextures textures = profile.getTextures();
    try {
      urlObject = new URL(texture);
    } catch (MalformedURLException exception) {
      try {
        urlObject = getUrlFromBase64(texture);
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      } 
    } 
    textures.setSkin(urlObject);
    profile.setTextures(textures);
    SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
    if (skullMeta == null)
      return itemStack; 
    skullMeta.setOwnerProfile(profile);
    itemStack.setItemMeta((ItemMeta)skullMeta);
    return itemStack;
  }
  
  public IRangeManager createRangeManager(Entity entity) {
    PlayerChunkMap.EntityTracker trackedEntity;
    WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
    try {
      trackedEntity = (PlayerChunkMap.EntityTracker)(level.l()).a.K.get(entity.getEntityId());
    } catch (NoSuchFieldError var8) {
      Entity nmsEntity = ((CraftEntity)entity).getHandle();
      try {
        Field trackerField = nmsEntity.getClass().getField("tracker");
        trackedEntity = (PlayerChunkMap.EntityTracker)trackerField.get(nmsEntity);
      } catch (IllegalAccessException|NoSuchFieldException var7) {
        throw new RuntimeException(var7);
      } 
    } 
    return (IRangeManager)new RangeManager(trackedEntity);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R3\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */