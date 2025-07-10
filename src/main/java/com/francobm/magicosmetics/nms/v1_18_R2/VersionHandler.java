/*     */ package com.francobm.magicosmetics.nms.v1_18_R2;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.chat.ChatMessage;
/*     */ import net.minecraft.network.chat.IChatBaseComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.network.syncher.DataWatcher;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.PlayerChunkMap;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAreaEffectCloud;
/*     */ import net.minecraft.world.entity.EntityLiving;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EnumItemSlot;
/*     */ import net.minecraft.world.entity.animal.EntityPufferFish;
/*     */ import net.minecraft.world.entity.decoration.EntityArmorStand;
/*     */ import net.minecraft.world.inventory.Containers;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
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
/*  67 */     PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, new EntityPlayer[] { p });
/*     */     try {
/*  69 */       Field packetField = packet.getClass().getDeclaredField("b");
/*  70 */       packetField.setAccessible(true);
/*  71 */       ArrayList<PacketPlayOutPlayerInfo.PlayerInfoData> list = Lists.newArrayList();
/*  72 */       list.add(new PacketPlayOutPlayerInfo.PlayerInfoData(p.fq(), 0, EnumGamemode.b, p.J()));
/*  73 */       packetField.set(packet, list);
/*  74 */       p.b.a((Packet)packet);
/*  75 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  76 */       p.b.a((Packet)packetPlayOutGameStateChange);
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
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/* 116 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 121 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 126 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 131 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 136 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 142 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 143 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 145 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 148 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 151 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 154 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 157 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 160 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/* 163 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 164 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).b;
/* 165 */       connection.a((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 174 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 175 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 176 */       return;  PacketPlayOutOpenWindow packet = null;
/* 177 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 179 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.a, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 2:
/* 182 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.b, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 3:
/* 185 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.c, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 4:
/* 188 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.d, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 5:
/* 191 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.e, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 6:
/* 194 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.f, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */     } 
/* 197 */     if (packet == null)
/* 198 */       return;  entityPlayer.b.a((Packet)packet);
/* 199 */     entityPlayer.bV.b();
/*     */   }
/*     */   
/*     */   public void testBackPackFake(Player player, int clouds) {
/* 203 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 204 */     Location location = player.getLocation();
/* 205 */     List<Integer> list = new ArrayList<>();
/* 206 */     EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/* 207 */     entityArmorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 208 */     entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)entityArmorStand));
/* 209 */     entityPlayer.b.a((Packet)new PacketPlayOutEntityMetadata(entityArmorStand.ae(), entityArmorStand.ai(), true));
/*     */     int i;
/* 211 */     for (i = 0; i < clouds; i++) {
/* 212 */       EntityAreaEffectCloud entityAreaEffectCloud = new EntityAreaEffectCloud(EntityTypes.b, (World)((CraftWorld)player.getWorld()).getHandle());
/* 213 */       entityAreaEffectCloud.a(0.0F);
/* 214 */       entityAreaEffectCloud.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 215 */       entityPlayer.b.a((Packet)new PacketPlayOutSpawnEntity((Entity)entityAreaEffectCloud));
/* 216 */       entityPlayer.b.a((Packet)new PacketPlayOutEntityMetadata(entityAreaEffectCloud.ae(), entityAreaEffectCloud.ai(), true));
/* 217 */       list.add(Integer.valueOf(entityAreaEffectCloud.ae()));
/*     */     } 
/*     */     
/* 220 */     for (i = 0; i < clouds; i++) {
/* 221 */       if (i == 0) {
/* 222 */         mount(entityPlayer, player.getEntityId(), ((Integer)list.get(i)).intValue());
/*     */       } else {
/*     */         
/* 225 */         mount(entityPlayer, ((Integer)list.get(i - 1)).intValue(), ((Integer)list.get(i)).intValue());
/*     */       } 
/* 227 */     }  mount(entityPlayer, ((Integer)list.get(list.size() - 1)).intValue(), entityArmorStand.ae());
/*     */   }
/*     */   
/*     */   public void mount(EntityPlayer entityPlayer, int entity, int passenger) {
/* 231 */     PacketPlayOutMount packetPlayOutMount = createDataSerializer(packetDataSerializer -> {
/*     */           packetDataSerializer.d(entity);
/*     */           packetDataSerializer.a(new int[] { passenger });
/*     */           return new PacketPlayOutMount(packetDataSerializer);
/*     */         });
/* 236 */     entityPlayer.b.a((Packet)packetPlayOutMount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 241 */     Entity e = ((CraftEntity)entity).getHandle();
/* 242 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 243 */     entityPlayer.b.a((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */   
/*     */   private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
/* 247 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 248 */     T result = null;
/*     */     try {
/* 250 */       result = callback.apply(data);
/* 251 */     } catch (Exception e) {
/* 252 */       e.printStackTrace();
/*     */     } finally {
/* 254 */       data.release();
/*     */     } 
/* 256 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArmorStand createArmor(Player player) {
/* 265 */     EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.c, (World)((CraftWorld)player.getWorld()).getHandle());
/* 266 */     entityArmorStand.b(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
/* 267 */     (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutSpawnEntity((Entity)entityArmorStand));
/* 268 */     (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(entityArmorStand.ae(), entityArmorStand.ai(), true));
/* 269 */     return (ArmorStand)entityArmorStand.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 274 */     if (itemStack == null) return null; 
/* 275 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 276 */     itemCosmetic.u().a("magic_cosmetic", key);
/* 277 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 282 */     if (itemStack == null) return null; 
/* 283 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 284 */     return itemCosmetic.u().l("magic_cosmetic");
/*     */   }
/*     */ 
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 289 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.at, (World)((CraftWorld)location.getWorld()).getHandle());
/* 290 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 291 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 296 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.c, (World)((CraftWorld)location.getWorld()).getHandle());
/* 297 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 298 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 303 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 304 */     entityClient.j(true);
/* 305 */     DataWatcher dataWatcher = entityClient.ai();
/* 306 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 307 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);
/* 308 */     for (Player viewer : viewers) {
/* 309 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 310 */       view.b.a((Packet)packet);
/* 311 */       view.b.a((Packet)metadata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 317 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 318 */     for (Player viewer : viewers) {
/* 319 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 320 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 326 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 327 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 328 */     for (Player viewer : viewers) {
/* 329 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 330 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 336 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 337 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 342 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 343 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 344 */     for (Player viewer : viewers) {
/* 345 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 346 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 352 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 353 */     if (!copy.s()) return cosmetic; 
/* 354 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 355 */     for (String key : copy.t().d()) {
/* 356 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 357 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 358 */         NBTTagCompound compound = copy.t().p(key);
/* 359 */         NBTTagCompound realCompound = cosmeticItem.t().p(key);
/* 360 */         Set<String> keys = compound.d();
/* 361 */         for (String compoundKey : keys) {
/* 362 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         }
/* 364 */         cosmeticItem.u().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 367 */       cosmeticItem.u().a(key, copy.t().c(key));
/*     */     } 
/* 369 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 373 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 374 */     if (!copy.s()) return itemStack; 
/* 375 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 376 */     if (!realItem.s()) return itemStack; 
/* 377 */     for (String key : copy.t().d()) {
/* 378 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 379 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 380 */         NBTTagCompound compound = copy.t().p(key);
/* 381 */         NBTTagCompound realCompound = realItem.t().p(key);
/* 382 */         Set<String> keys = compound.d();
/* 383 */         for (String compoundKey : keys) {
/* 384 */           if (!realCompound.e(compoundKey))
/* 385 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 387 */         realItem.t().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 390 */       if (!realItem.t().e(key))
/* 391 */         continue;  realItem.t().a(key, copy.t().c(key));
/*     */     } 
/* 393 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 397 */     if (itemStack == null) return null; 
/* 398 */     if (texture.isEmpty()) {
/* 399 */       return itemStack;
/*     */     }
/* 401 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 402 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 405 */       urlObject = new URL(texture);
/* 406 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 408 */         urlObject = getUrlFromBase64(texture);
/* 409 */       } catch (MalformedURLException e) {
/* 410 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 413 */     textures.setSkin(urlObject);
/* 414 */     profile.setTextures(textures);
/* 415 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 416 */     if (skullMeta == null) return itemStack; 
/* 417 */     skullMeta.setOwnerProfile(profile);
/* 418 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 419 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 424 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 425 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.k()).a.J.get(entity.getEntityId());
/* 426 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface UnsafeFunction<K, T> {
/*     */     T apply(K param1K) throws Exception;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R2\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */