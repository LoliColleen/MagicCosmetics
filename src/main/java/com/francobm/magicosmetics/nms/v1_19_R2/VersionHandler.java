/*     */ package com.francobm.magicosmetics.nms.v1_19_R2;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.bag.PlayerBag;
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
/*     */ import net.minecraft.world.level.World;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.util.CraftChatMessage;
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
/*  62 */     player.setGameMode(GameMode.SPECTATOR);
/*  63 */     EntityPlayer p = ((CraftPlayer)player).getHandle();
/*  64 */     ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.c, p);
/*     */     try {
/*  66 */       Field packetField = packet.getClass().getDeclaredField("b");
/*  67 */       packetField.setAccessible(true);
/*  68 */       ArrayList<ClientboundPlayerInfoUpdatePacket.b> list = Lists.newArrayList();
/*  69 */       list.add(new ClientboundPlayerInfoUpdatePacket.b(player.getUniqueId(), p.getBukkitEntity().getProfile(), false, 0, EnumGamemode.b, p.K(), null));
/*  70 */       packetField.set(packet, list);
/*  71 */       p.b.a((Packet)packet);
/*  72 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  73 */       p.b.a((Packet)packetPlayOutGameStateChange);
/*  74 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/*  75 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player) {
/*  81 */     NPC npc = new NPCHandler();
/*  82 */     npc.addNPC(player);
/*  83 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player, Location location) {
/*  88 */     NPC npc = new NPCHandler();
/*  89 */     npc.addNPC(player, location);
/*  90 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC(Player player) {
/*  95 */     return (NPC)NPC.npcs.get(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNPC(Player player) {
/* 100 */     NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
/* 101 */     if (npc == null)
/* 102 */       return;  npc.removeNPC(player);
/* 103 */     NPC.npcs.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC() {
/* 108 */     return new NPCHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackItemForMe) {
/* 113 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackItemForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 118 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 123 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 128 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 133 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 139 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 140 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 142 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 145 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 148 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 151 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 154 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 157 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/* 160 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 161 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).b;
/* 162 */       connection.a((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 168 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 169 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 170 */       return;  PacketPlayOutOpenWindow packet = null;
/* 171 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 173 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.a, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 2:
/* 176 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.b, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 3:
/* 179 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.c, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 4:
/* 182 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.d, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 5:
/* 185 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.e, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 6:
/* 188 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bU.j, Containers.f, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */     } 
/* 191 */     if (packet == null)
/* 192 */       return;  entityPlayer.b.a((Packet)packet);
/* 193 */     entityPlayer.bU.b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 198 */     Entity e = ((CraftEntity)entity).getHandle();
/* 199 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 200 */     entityPlayer.b.a((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 205 */     if (itemStack == null) return null; 
/* 206 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 207 */     itemCosmetic.v().a("magic_cosmetic", key);
/* 208 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 213 */     if (itemStack == null) return null; 
/* 214 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 215 */     return itemCosmetic.v().l("magic_cosmetic");
/*     */   }
/*     */ 
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 220 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.ax, (World)((CraftWorld)location.getWorld()).getHandle());
/* 221 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 222 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 227 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
/* 228 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 229 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 234 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 235 */     entityClient.j(true);
/* 236 */     DataWatcher dataWatcher = entityClient.al();
/* 237 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 238 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher.c());
/* 239 */     for (Player viewer : viewers) {
/* 240 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 241 */       view.b.a((Packet)packet);
/* 242 */       view.b.a((Packet)metadata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 248 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 249 */     for (Player viewer : viewers) {
/* 250 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 251 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 257 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 258 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 259 */     for (Player viewer : viewers) {
/* 260 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 261 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 267 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 268 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 273 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 274 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 275 */     for (Player viewer : viewers) {
/* 276 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 277 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 283 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 284 */     if (!copy.t()) return cosmetic; 
/* 285 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 286 */     for (String key : copy.u().e()) {
/* 287 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 288 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 289 */         NBTTagCompound compound = copy.u().p(key);
/* 290 */         NBTTagCompound realCompound = cosmeticItem.u().p(key);
/* 291 */         Set<String> keys = compound.e();
/* 292 */         for (String compoundKey : keys) {
/* 293 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         }
/* 295 */         cosmeticItem.v().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 298 */       cosmeticItem.v().a(key, copy.u().c(key));
/*     */     } 
/* 300 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 304 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 305 */     if (!copy.t()) return itemStack; 
/* 306 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 307 */     if (!realItem.t()) return itemStack; 
/* 308 */     for (String key : copy.u().e()) {
/* 309 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 310 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 311 */         NBTTagCompound compound = copy.u().p(key);
/* 312 */         NBTTagCompound realCompound = realItem.u().p(key);
/* 313 */         Set<String> keys = compound.e();
/* 314 */         for (String compoundKey : keys) {
/* 315 */           if (!realCompound.e(compoundKey))
/* 316 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 318 */         realItem.u().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 321 */       if (!realItem.u().e(key))
/* 322 */         continue;  realItem.u().a(key, copy.u().c(key));
/*     */     } 
/* 324 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 328 */     if (itemStack == null) return null; 
/* 329 */     if (texture.isEmpty()) {
/* 330 */       return itemStack;
/*     */     }
/* 332 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 333 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 336 */       urlObject = new URL(texture);
/* 337 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 339 */         urlObject = getUrlFromBase64(texture);
/* 340 */       } catch (MalformedURLException e) {
/* 341 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 344 */     textures.setSkin(urlObject);
/* 345 */     profile.setTextures(textures);
/* 346 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 347 */     if (skullMeta == null) return itemStack; 
/* 348 */     skullMeta.setOwnerProfile(profile);
/* 349 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 350 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 355 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 356 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.k()).a.L.get(entity.getEntityId());
/* 357 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */