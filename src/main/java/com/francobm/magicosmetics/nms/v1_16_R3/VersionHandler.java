/*     */ package com.francobm.magicosmetics.nms.v1_16_R3;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.v1_16_R3.ChatMessage;
/*     */ import net.minecraft.server.v1_16_R3.Containers;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityArmorStand;
/*     */ import net.minecraft.server.v1_16_R3.EntityLiving;
/*     */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*     */ import net.minecraft.server.v1_16_R3.EntityPufferFish;
/*     */ import net.minecraft.server.v1_16_R3.EnumItemSlot;
/*     */ import net.minecraft.server.v1_16_R3.IChatBaseComponent;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.NBTTagCompound;
/*     */ import net.minecraft.server.v1_16_R3.Packet;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.v1_16_R3.PlayerChunkMap;
/*     */ import net.minecraft.server.v1_16_R3.World;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.SoundCategory;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.map.MapView;
/*     */ 
/*     */ public class VersionHandler extends Version {
/*     */   public void setSpectator(Player player) {
/*  48 */     if (player.getGameMode() == GameMode.SPECTATOR)
/*  49 */       return;  player.setGameMode(GameMode.SPECTATOR);
/*  50 */     EntityPlayer p = ((CraftPlayer)player).getHandle();
/*  51 */     PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, new EntityPlayer[] { p });
/*     */     try {
/*  53 */       Field packetField = packet.getClass().getDeclaredField("b");
/*  54 */       packetField.setAccessible(true);
/*  55 */       Constructor<?> infoDataConstructor = PacketUtil();
/*  56 */       List<Object> list = Collections.singletonList(infoDataConstructor.newInstance(new Object[] { packet, p.getProfile(), Integer.valueOf(p.ping), EnumGamemode.CREATIVE, p.getPlayerListName() }));
/*  57 */       packetField.set(packet, list);
/*  58 */       p.playerConnection.sendPacket((Packet)packet);
/*  59 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  60 */       p.playerConnection.sendPacket((Packet)packetPlayOutGameStateChange);
/*  61 */     } catch (NoSuchFieldException|IllegalAccessException|java.lang.reflect.InvocationTargetException|InstantiationException e) {
/*  62 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player) {
/*  68 */     NPC npc = new NPCHandler();
/*  69 */     npc.addNPC(player);
/*  70 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player, Location location) {
/*  75 */     NPC npc = new NPCHandler();
/*  76 */     npc.addNPC(player, location);
/*  77 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC(Player player) {
/*  82 */     return (NPC)NPC.npcs.get(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNPC(Player player) {
/*  87 */     NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
/*  88 */     if (npc == null)
/*  89 */       return;  npc.removeNPC(player);
/*  90 */     NPC.npcs.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC() {
/*  95 */     return new NPCHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackForMe) {
/* 100 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 105 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 110 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 114 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 119 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */   
/*     */   public Constructor<?> PacketUtil() {
/*     */     try {
/* 124 */       String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
/* 125 */       Class<?> clazz = Class.forName("net.minecraft.server." + version + ".PacketPlayOutPlayerInfo$PlayerInfoData");
/* 126 */       return clazz.getDeclaredConstructor(new Class[] { PacketPlayOutPlayerInfo.class, GameProfile.class, int.class, EnumGamemode.class, IChatBaseComponent.class });
/* 127 */     } catch (Exception e) {
/* 128 */       e.printStackTrace();
/*     */       
/* 130 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 136 */     List<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 137 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 139 */         list.add(new Pair(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 142 */         list.add(new Pair(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 145 */         list.add(new Pair(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 148 */         list.add(new Pair(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 151 */         list.add(new Pair(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 154 */         list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/* 157 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 158 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).playerConnection;
/* 159 */       connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/* 160 */       if (!livingEntity.getUniqueId().equals(p.getUniqueId()) || 
/* 161 */         !(livingEntity instanceof Player))
/* 162 */         continue;  Player player = (Player)livingEntity;
/* 163 */       SoundCategory category = SoundCategory.PLAYERS;
/* 164 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_CHAIN, category);
/* 165 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_LEATHER, category);
/* 166 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_IRON, category);
/* 167 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_DIAMOND, category);
/* 168 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_GOLD, category);
/* 169 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_GENERIC, category);
/* 170 */       player.stopSound(Sound.ITEM_ARMOR_EQUIP_NETHERITE, category);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 176 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 177 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 178 */       return;  PacketPlayOutOpenWindow packet = null;
/* 179 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 181 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X1, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 2:
/* 184 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X2, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 3:
/* 187 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X3, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 4:
/* 190 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X4, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 5:
/* 193 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X5, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 6:
/* 196 */         packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, Containers.GENERIC_9X6, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */     } 
/* 199 */     if (packet == null)
/* 200 */       return;  entityPlayer.playerConnection.sendPacket((Packet)packet);
/* 201 */     entityPlayer.updateInventory(entityPlayer.activeContainer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 206 */     Entity e = ((CraftEntity)entity).getHandle();
/* 207 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 208 */     entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 213 */     if (itemStack == null) return null; 
/* 214 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 215 */     itemCosmetic.getOrCreateTag().setString("magic_cosmetic", key);
/* 216 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 221 */     if (itemStack == null) return null; 
/* 222 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 223 */     return itemCosmetic.getOrCreateTag().getString("magic_cosmetic");
/*     */   }
/*     */ 
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 228 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.PUFFERFISH, (World)((CraftWorld)location.getWorld()).getHandle());
/* 229 */     entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 230 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 235 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)((CraftWorld)location.getWorld()).getHandle());
/* 236 */     entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 237 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 242 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 243 */     entityClient.setInvisible(true);
/* 244 */     DataWatcher dataWatcher = entityClient.getDataWatcher();
/* 245 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 246 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);
/* 247 */     for (Player viewer : viewers) {
/* 248 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 249 */       view.playerConnection.sendPacket((Packet)packet);
/* 250 */       view.playerConnection.sendPacket((Packet)metadata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 256 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 257 */     for (Player viewer : viewers) {
/* 258 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 259 */       view.playerConnection.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 265 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 266 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 267 */     for (Player viewer : viewers) {
/* 268 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 269 */       view.playerConnection.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 275 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 276 */     entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 281 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 282 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 283 */     for (Player viewer : viewers) {
/* 284 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 285 */       view.playerConnection.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 291 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 292 */     if (!copy.hasTag()) return cosmetic; 
/* 293 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 294 */     for (String key : copy.getTag().getKeys()) {
/* 295 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 296 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 297 */         NBTTagCompound compound = copy.getTag().getCompound(key);
/* 298 */         NBTTagCompound realCompound = cosmeticItem.getTag().getCompound(key);
/* 299 */         Set<String> keys = compound.getKeys();
/* 300 */         for (String compoundKey : keys) {
/* 301 */           realCompound.set(compoundKey, compound.get(compoundKey));
/*     */         }
/* 303 */         cosmeticItem.getOrCreateTag().set(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 306 */       cosmeticItem.getOrCreateTag().set(key, copy.getTag().get(key));
/*     */     } 
/* 308 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 312 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 313 */     if (!copy.hasTag()) return itemStack; 
/* 314 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 315 */     if (!realItem.hasTag()) return itemStack; 
/* 316 */     for (String key : copy.getTag().getKeys()) {
/* 317 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 318 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 319 */         NBTTagCompound compound = copy.getTag().getCompound(key);
/* 320 */         NBTTagCompound realCompound = realItem.getTag().getCompound(key);
/* 321 */         Set<String> keys = compound.getKeys();
/* 322 */         for (String compoundKey : keys) {
/* 323 */           if (!realCompound.hasKey(compoundKey))
/* 324 */             continue;  realCompound.set(compoundKey, compound.get(compoundKey));
/*     */         } 
/* 326 */         realItem.getTag().set(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 329 */       if (!realItem.getTag().hasKey(key))
/* 330 */         continue;  realItem.getTag().set(key, copy.getTag().get(key));
/*     */     } 
/* 332 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/* 336 */     if (itemStack == null) return null; 
/* 337 */     if (texture.isEmpty()) {
/* 338 */       return itemStack;
/*     */     }
/* 340 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 341 */     if (skullMeta == null) return itemStack; 
/* 342 */     GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
/* 343 */     gameProfile.getProperties().put("textures", new Property("textures", texture));
/*     */     try {
/* 345 */       Field profileField = skullMeta.getClass().getDeclaredField("profile");
/* 346 */       profileField.setAccessible(true);
/* 347 */       profileField.set(skullMeta, gameProfile);
/* 348 */     } catch (Exception e) {
/* 349 */       e.printStackTrace();
/*     */     } 
/* 351 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 352 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 357 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 358 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.getChunkProvider()).playerChunkMap.trackedEntities.get(entity.getEntityId());
/* 359 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */