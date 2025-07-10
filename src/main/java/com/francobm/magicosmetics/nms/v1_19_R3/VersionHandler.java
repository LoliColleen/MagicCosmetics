/*     */ package com.francobm.magicosmetics.nms.v1_19_R3;
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
/*     */ import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
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
/*  69 */       list.add(new ClientboundPlayerInfoUpdatePacket.b(player.getUniqueId(), p.getBukkitEntity().getProfile(), false, 0, EnumGamemode.b, p.J(), null));
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
/* 173 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.a, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 2:
/* 176 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.b, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 3:
/* 179 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.c, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 4:
/* 182 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.d, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 5:
/* 185 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.e, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 6:
/* 188 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bP.j, Containers.f, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */     } 
/* 191 */     if (packet == null)
/* 192 */       return;  entityPlayer.b.a((Packet)packet);
/* 193 */     entityPlayer.bP.b();
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
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 219 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.aB, (World)((CraftWorld)location.getWorld()).getHandle());
/* 220 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 221 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 226 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
/* 227 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 228 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 232 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 233 */     entityClient.j(true);
/* 234 */     DataWatcher dataWatcher = entityClient.aj();
/* 235 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 236 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher.c());
/* 237 */     for (Player viewer : viewers) {
/* 238 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 239 */       entityClient.aj().refresh(view);
/* 240 */       view.b.a((Packet)packet);
/* 241 */       view.b.a((Packet)metadata);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 246 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 247 */     for (Player viewer : viewers) {
/* 248 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 249 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 254 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 255 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 256 */     for (Player viewer : viewers) {
/* 257 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 258 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 263 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 264 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 268 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 269 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 270 */     for (Player viewer : viewers) {
/* 271 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 272 */       view.b.a((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 278 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 279 */     if (!copy.t()) return cosmetic; 
/* 280 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 281 */     for (String key : copy.u().e()) {
/* 282 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 283 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 284 */         NBTTagCompound compound = copy.u().p(key);
/* 285 */         NBTTagCompound realCompound = cosmeticItem.u().p(key);
/* 286 */         Set<String> keys = compound.e();
/* 287 */         for (String compoundKey : keys) {
/* 288 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         }
/* 290 */         cosmeticItem.v().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 293 */       cosmeticItem.v().a(key, copy.u().c(key));
/*     */     } 
/* 295 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 299 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 300 */     if (!copy.t()) return itemStack; 
/* 301 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 302 */     if (!realItem.t()) return itemStack; 
/* 303 */     for (String key : copy.u().e()) {
/* 304 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 305 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 306 */         NBTTagCompound compound = copy.u().p(key);
/* 307 */         NBTTagCompound realCompound = realItem.u().p(key);
/* 308 */         Set<String> keys = compound.e();
/* 309 */         for (String compoundKey : keys) {
/* 310 */           if (!realCompound.e(compoundKey))
/* 311 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 313 */         realItem.u().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 316 */       if (!realItem.u().e(key))
/* 317 */         continue;  realItem.u().a(key, copy.u().c(key));
/*     */     } 
/* 319 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 323 */     if (itemStack == null) return null; 
/* 324 */     if (texture.isEmpty()) {
/* 325 */       return itemStack;
/*     */     }
/* 327 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 328 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 331 */       urlObject = new URL(texture);
/* 332 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 334 */         urlObject = getUrlFromBase64(texture);
/* 335 */       } catch (MalformedURLException e) {
/* 336 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 339 */     textures.setSkin(urlObject);
/* 340 */     profile.setTextures(textures);
/* 341 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 342 */     if (skullMeta == null) return itemStack; 
/* 343 */     skullMeta.setOwnerProfile(profile);
/* 344 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 345 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 350 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 351 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.k()).a.L.get(entity.getEntityId());
/* 352 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R3\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */