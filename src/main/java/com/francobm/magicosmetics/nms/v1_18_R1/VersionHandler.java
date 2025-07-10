/*     */ package com.francobm.magicosmetics.nms.v1_18_R1;
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
/*     */ import java.util.Set;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.chat.ChatMessage;
/*     */ import net.minecraft.network.chat.IChatBaseComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
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
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
/*  61 */     player.setGameMode(GameMode.SPECTATOR);
/*  62 */     EntityPlayer p = ((CraftPlayer)player).getHandle();
/*  63 */     PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, new EntityPlayer[] { p });
/*     */     try {
/*  65 */       Field packetField = packet.getClass().getDeclaredField("b");
/*  66 */       packetField.setAccessible(true);
/*  67 */       ArrayList<PacketPlayOutPlayerInfo.PlayerInfoData> list = Lists.newArrayList();
/*  68 */       list.add(new PacketPlayOutPlayerInfo.PlayerInfoData(p.fp(), 0, EnumGamemode.b, p.J()));
/*  69 */       packetField.set(packet, list);
/*  70 */       p.b.a((Packet)packet);
/*  71 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  72 */       p.b.a((Packet)packetPlayOutGameStateChange);
/*  73 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/*  74 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player) {
/*  80 */     NPC npc = new NPCHandler();
/*  81 */     npc.addNPC(player);
/*  82 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player, Location location) {
/*  87 */     NPC npc = new NPCHandler();
/*  88 */     npc.addNPC(player, location);
/*  89 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC(Player player) {
/*  94 */     return (NPC)NPC.npcs.get(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNPC(Player player) {
/*  99 */     NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
/* 100 */     if (npc == null)
/* 101 */       return;  npc.removeNPC(player);
/* 102 */     NPC.npcs.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC() {
/* 107 */     return new NPCHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackForMe) {
/* 112 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 117 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 122 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 127 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 132 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 137 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 138 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 140 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 143 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 146 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 149 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 152 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 155 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/*     */     
/* 159 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 160 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).b;
/* 161 */       connection.a((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 167 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 168 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 169 */       return;  PacketPlayOutOpenWindow packet = null;
/* 170 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 172 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.a, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 2:
/* 175 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.b, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 3:
/* 178 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.c, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 4:
/* 181 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.d, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 5:
/* 184 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.e, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 6:
/* 187 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bW.j, Containers.f, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */     } 
/* 190 */     if (packet == null)
/* 191 */       return;  entityPlayer.b.a((Packet)packet);
/* 192 */     entityPlayer.bW.b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 197 */     Entity e = ((CraftEntity)entity).getHandle();
/* 198 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 199 */     entityPlayer.b.a((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 204 */     if (itemStack == null) return null; 
/* 205 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 206 */     itemCosmetic.t().a("magic_cosmetic", key);
/* 207 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 212 */     if (itemStack == null) return null; 
/* 213 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 214 */     return itemCosmetic.t().l("magic_cosmetic");
/*     */   }
/*     */ 
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 219 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.at, (World)((CraftWorld)location.getWorld()).getHandle());
/* 220 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 221 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 226 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.c, (World)((CraftWorld)location.getWorld()).getHandle());
/* 227 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 228 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 233 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 234 */     entityClient.j(true);
/* 235 */     DataWatcher dataWatcher = entityClient.ai();
/* 236 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 237 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);
/* 238 */     for (Player viewer : viewers) {
/* 239 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 240 */       view.b.a((Packet)packet);
/* 241 */       view.b.a((Packet)metadata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 247 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 248 */     for (Player viewer : viewers) {
/* 249 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 250 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 256 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 257 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 258 */     for (Player viewer : viewers) {
/* 259 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 260 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 266 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 267 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 272 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 273 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 274 */     for (Player viewer : viewers) {
/* 275 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 276 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 282 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 283 */     if (!copy.r()) return cosmetic; 
/* 284 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 285 */     for (String key : copy.s().d()) {
/* 286 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 287 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 288 */         NBTTagCompound compound = copy.s().p(key);
/* 289 */         NBTTagCompound realCompound = cosmeticItem.s().p(key);
/* 290 */         Set<String> keys = compound.d();
/* 291 */         for (String compoundKey : keys) {
/* 292 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         }
/* 294 */         cosmeticItem.t().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 297 */       cosmeticItem.t().a(key, copy.s().c(key));
/*     */     } 
/* 299 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 303 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 304 */     if (!copy.r()) return itemStack; 
/* 305 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 306 */     if (!realItem.r()) return itemStack; 
/* 307 */     for (String key : copy.s().d()) {
/* 308 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 309 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 310 */         NBTTagCompound compound = copy.s().p(key);
/* 311 */         NBTTagCompound realCompound = realItem.s().p(key);
/* 312 */         Set<String> keys = compound.d();
/* 313 */         for (String compoundKey : keys) {
/* 314 */           if (!realCompound.e(compoundKey))
/* 315 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 317 */         realItem.s().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 320 */       if (!realItem.s().e(key))
/* 321 */         continue;  realItem.s().a(key, copy.s().c(key));
/*     */     } 
/* 323 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 327 */     if (itemStack == null) return null; 
/* 328 */     if (texture.isEmpty()) {
/* 329 */       return itemStack;
/*     */     }
/* 331 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 332 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 335 */       urlObject = new URL(texture);
/* 336 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 338 */         urlObject = getUrlFromBase64(texture);
/* 339 */       } catch (MalformedURLException e) {
/* 340 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 343 */     textures.setSkin(urlObject);
/* 344 */     profile.setTextures(textures);
/* 345 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 346 */     if (skullMeta == null) return itemStack; 
/* 347 */     skullMeta.setOwnerProfile(profile);
/* 348 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 349 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 354 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 355 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.k()).a.I.get(entity.getEntityId());
/* 356 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */