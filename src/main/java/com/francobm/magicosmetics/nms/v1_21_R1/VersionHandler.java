/*     */ package com.francobm.magicosmetics.nms.v1_21_R1;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.bag.PlayerBag;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import com.francobm.magicosmetics.nms.v1_21_R1.cache.PlayerBagHandler;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.network.syncher.DataWatcher;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.PlayerChunkMap;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.server.network.PlayerConnection;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.animal.EntityPufferFish;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.inventory.Containers;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_21_R1.util.CraftChatMessage;
/*     */ import org.bukkit.entity.ArmorStand;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.PufferFish;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.profile.PlayerProfile;
/*     */ import org.bukkit.profile.PlayerTextures;
/*     */ 
/*     */ public class VersionHandler extends Version {
/*     */   public void setSpectator(Player player) {
/*  65 */     player.setGameMode(GameMode.SPECTATOR);
/*  66 */     EntityPlayer p = ((CraftPlayer)player).getHandle();
/*  67 */     ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(Enum.<ClientboundPlayerInfoUpdatePacket.a>valueOf(ClientboundPlayerInfoUpdatePacket.a.class, "UPDATE_GAME_MODE"), p);
/*     */     try {
/*  69 */       Field packetField = packet.getClass().getDeclaredField("c");
/*  70 */       packetField.setAccessible(true);
/*  71 */       ArrayList<ClientboundPlayerInfoUpdatePacket.b> list = Lists.newArrayList();
/*  72 */       list.add(new ClientboundPlayerInfoUpdatePacket.b(player.getUniqueId(), p.getBukkitEntity().getProfile(), false, 0, EnumGamemode.b, p.O(), null));
/*  73 */       packetField.set(packet, list);
/*  74 */       p.c.b((Packet)packet);
/*  75 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, 3.0F);
/*  76 */       p.c.b((Packet)packetPlayOutGameStateChange);
/*  77 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/*  78 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player) {
/*  84 */     NPC npc = new NPCHandler();
/*  85 */     npc.addNPC(player);
/*  86 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player, Location location) {
/*  91 */     NPC npc = new NPCHandler();
/*  92 */     npc.addNPC(player, location);
/*  93 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC(Player player) {
/*  98 */     return (NPC)NPC.npcs.get(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNPC(Player player) {
/* 103 */     NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
/* 104 */     if (npc == null)
/* 105 */       return;  npc.removeNPC(player);
/* 106 */     NPC.npcs.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC() {
/* 111 */     return new NPCHandler();
/*     */   }
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/* 115 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 120 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 125 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 130 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 135 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 140 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 141 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 143 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 146 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 149 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 152 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 155 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 158 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/* 161 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 162 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).c;
/* 163 */       connection.b((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 169 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 170 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 171 */       return;  PacketPlayOutOpenWindow packet = null;
/* 172 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 174 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.a, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 2:
/* 177 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.b, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 3:
/* 180 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.c, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 4:
/* 183 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.d, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 5:
/* 186 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.e, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 6:
/* 189 */         packet = new PacketPlayOutOpenWindow(entityPlayer.cd.j, Containers.f, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */     } 
/* 192 */     if (packet == null)
/* 193 */       return;  entityPlayer.c.b((Packet)packet);
/* 194 */     entityPlayer.cd.b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 199 */     Entity e = ((CraftEntity)entity).getHandle();
/* 200 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 201 */     entityPlayer.c.b((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 206 */     if (itemStack == null) return null; 
/* 207 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 208 */     CustomData.a(DataComponents.b, itemCosmetic, nbtTagCompound -> nbtTagCompound.a("magic_cosmetic", key));
/* 209 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 214 */     if (itemStack == null) return null; 
/* 215 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 216 */     if (!itemCosmetic.b(DataComponents.b)) return ""; 
/* 217 */     CustomData customData = (CustomData)itemCosmetic.a(DataComponents.b);
/* 218 */     return customData.c().l("magic_cosmetic");
/*     */   }
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 222 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.aF, (World)((CraftWorld)location.getWorld()).getHandle());
/* 223 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 224 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 229 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
/* 230 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 231 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 235 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 236 */     entityClient.k(true);
/* 237 */     DataWatcher dataWatcher = entityClient.ar();
/* 238 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient, 0, CraftLocation.toBlockPosition(entity.getLocation()));
/* 239 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher.c());
/* 240 */     for (Player viewer : viewers) {
/* 241 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 242 */       view.c.b((Packet)packet);
/* 243 */       view.c.b((Packet)metadata);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 248 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 249 */     for (Player viewer : viewers) {
/* 250 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 251 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 256 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 257 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 258 */     for (Player viewer : viewers) {
/* 259 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 260 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 265 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 266 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 270 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 271 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 272 */     for (Player viewer : viewers) {
/* 273 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 274 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 280 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 281 */     if (!copy.b(DataComponents.b)) return cosmetic; 
/* 282 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 283 */     CustomData copyCustomData = (CustomData)copy.a(DataComponents.b);
/* 284 */     CustomData cosmeticCustomData = (CustomData)cosmeticItem.a(DataComponents.b);
/* 285 */     NBTTagCompound copyNBT = copyCustomData.c();
/* 286 */     NBTTagCompound cosmeticNBT = cosmeticCustomData.c();
/* 287 */     for (String key : copyNBT.e()) {
/* 288 */       Bukkit.getLogger().info("Key: " + key);
/* 289 */       if (key.equals("display") || key.equals("minecraft:custom_name") || key.equals("CustomModelData") || key.equals("minecraft:custom_model_data"))
/* 290 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 291 */         NBTTagCompound compound = copyNBT.p(key);
/* 292 */         NBTTagCompound realCompound = cosmeticNBT.p(key);
/* 293 */         Set<String> keys = compound.e();
/* 294 */         for (String compoundKey : keys) {
/* 295 */           Bukkit.getLogger().info("Key of key: " + compoundKey);
/* 296 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 298 */         cosmeticNBT.a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 301 */       cosmeticNBT.a(key, copyNBT.c(key));
/*     */     } 
/* 303 */     cosmeticItem.b(DataComponents.b, cosmeticCustomData.a(nbtTagCompound -> nbtTagCompound.a(cosmeticNBT)));
/* 304 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 308 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 309 */     if (!copy.b(DataComponents.b)) return itemStack; 
/* 310 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 311 */     if (!realItem.b(DataComponents.b)) return itemStack; 
/* 312 */     CustomData copyCustomData = (CustomData)copy.a(DataComponents.b);
/* 313 */     CustomData realCustomData = (CustomData)realItem.a(DataComponents.b);
/* 314 */     NBTTagCompound copyNBT = copyCustomData.c();
/* 315 */     NBTTagCompound realNBT = realCustomData.c();
/* 316 */     for (String key : copyNBT.e()) {
/* 317 */       if (key.equals("display") || key.equals("minecraft:custom_name") || key.equals("CustomModelData") || key.equals("minecraft:custom_model_data"))
/* 318 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 319 */         NBTTagCompound compound = copyNBT.p(key);
/* 320 */         NBTTagCompound realCompound = realNBT.p(key);
/* 321 */         Set<String> keys = compound.e();
/* 322 */         for (String compoundKey : keys) {
/* 323 */           if (!realCompound.e(compoundKey))
/* 324 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 326 */         realNBT.a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 329 */       if (!realNBT.e(key))
/* 330 */         continue;  realNBT.a(key, copyNBT.c(key));
/*     */     } 
/* 332 */     realItem.b(DataComponents.b, realCustomData.a(nbtTagCompound -> nbtTagCompound.a(realNBT)));
/* 333 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 337 */     if (itemStack == null) return null; 
/* 338 */     if (texture.isEmpty()) {
/* 339 */       return itemStack;
/*     */     }
/* 341 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 342 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 345 */       urlObject = new URL(texture);
/* 346 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 348 */         urlObject = getUrlFromBase64(texture);
/* 349 */       } catch (MalformedURLException e) {
/* 350 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 353 */     textures.setSkin(urlObject);
/* 354 */     profile.setTextures(textures);
/* 355 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 356 */     if (skullMeta == null) return itemStack; 
/* 357 */     skullMeta.setOwnerProfile(profile);
/* 358 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 359 */     return itemStack;
/*     */   }
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/*     */     PlayerChunkMap.EntityTracker trackedEntity;
/* 364 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/*     */ 
/*     */     
/*     */     try {
/* 368 */       trackedEntity = (PlayerChunkMap.EntityTracker)(level.l()).a.K.get(entity.getEntityId());
/* 369 */     } catch (NoSuchFieldError var8) {
/* 370 */       Entity nmsEntity = ((CraftEntity)entity).getHandle();
/*     */       
/*     */       try {
/* 373 */         Field trackerField = nmsEntity.getClass().getField("tracker");
/* 374 */         trackedEntity = (PlayerChunkMap.EntityTracker)trackerField.get(nmsEntity);
/* 375 */       } catch (IllegalAccessException|NoSuchFieldException var7) {
/* 376 */         throw new RuntimeException(var7);
/*     */       } 
/*     */     } 
/*     */     
/* 380 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_21_R1\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */