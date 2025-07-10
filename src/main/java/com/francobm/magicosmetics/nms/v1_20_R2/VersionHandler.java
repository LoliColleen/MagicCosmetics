/*     */ package com.francobm.magicosmetics.nms.v1_20_R2;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
/*     */ import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
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
/*  69 */       list.add(new ClientboundPlayerInfoUpdatePacket.b(player.getUniqueId(), p.getBukkitEntity().getProfile(), false, 0, EnumGamemode.b, p.L(), null));
/*  70 */       packetField.set(packet, list);
/*  71 */       p.c.b((Packet)packet);
/*  72 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  73 */       p.c.b((Packet)packetPlayOutGameStateChange);
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
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 138 */     ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 139 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 141 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 144 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 147 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 150 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 153 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 156 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/* 159 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 160 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).c;
/* 161 */       connection.b((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
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
/* 172 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.a, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 2:
/* 175 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.b, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 3:
/* 178 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.c, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 4:
/* 181 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.d, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 5:
/* 184 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.e, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */       case 6:
/* 187 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bS.j, Containers.f, CraftChatMessage.fromStringOrNull(title));
/*     */         break;
/*     */     } 
/* 190 */     if (packet == null)
/* 191 */       return;  entityPlayer.c.b((Packet)packet);
/* 192 */     entityPlayer.bS.b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 197 */     Entity e = ((CraftEntity)entity).getHandle();
/* 198 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 199 */     entityPlayer.c.b((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 204 */     if (itemStack == null) return null; 
/* 205 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 206 */     itemCosmetic.w().a("magic_cosmetic", key);
/* 207 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 212 */     if (itemStack == null) return null; 
/* 213 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 214 */     return itemCosmetic.w().l("magic_cosmetic");
/*     */   }
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 218 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.aB, (World)((CraftWorld)location.getWorld()).getHandle());
/* 219 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 220 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 225 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
/* 226 */     entityPufferFish.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 227 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 231 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 232 */     entityClient.j(true);
/* 233 */     DataWatcher dataWatcher = entityClient.al();
/* 234 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 235 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher.c());
/* 236 */     for (Player viewer : viewers) {
/* 237 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 238 */       view.c.b((Packet)packet);
/* 239 */       view.c.b((Packet)metadata);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 244 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 245 */     for (Player viewer : viewers) {
/* 246 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 247 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 252 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 253 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 254 */     for (Player viewer : viewers) {
/* 255 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 256 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 261 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 262 */     entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 266 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 267 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 268 */     for (Player viewer : viewers) {
/* 269 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 270 */       view.c.b((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 276 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 277 */     if (!copy.u()) return cosmetic; 
/* 278 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 279 */     for (String key : copy.v().e()) {
/* 280 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 281 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 282 */         NBTTagCompound compound = copy.v().p(key);
/* 283 */         NBTTagCompound realCompound = cosmeticItem.v().p(key);
/* 284 */         Set<String> keys = compound.e();
/* 285 */         for (String compoundKey : keys) {
/* 286 */           realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         }
/* 288 */         cosmeticItem.w().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 291 */       cosmeticItem.w().a(key, copy.v().c(key));
/*     */     } 
/* 293 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 297 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 298 */     if (!copy.u()) return itemStack; 
/* 299 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 300 */     if (!realItem.u()) return itemStack; 
/* 301 */     for (String key : copy.v().e()) {
/* 302 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 303 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 304 */         NBTTagCompound compound = copy.v().p(key);
/* 305 */         NBTTagCompound realCompound = realItem.v().p(key);
/* 306 */         Set<String> keys = compound.e();
/* 307 */         for (String compoundKey : keys) {
/* 308 */           if (!realCompound.e(compoundKey))
/* 309 */             continue;  realCompound.a(compoundKey, compound.c(compoundKey));
/*     */         } 
/* 311 */         realItem.v().a(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 314 */       if (!realItem.v().e(key))
/* 315 */         continue;  realItem.v().a(key, copy.v().c(key));
/*     */     } 
/* 317 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/*     */     URL urlObject;
/* 321 */     if (itemStack == null) return null; 
/* 322 */     if (texture.isEmpty()) {
/* 323 */       return itemStack;
/*     */     }
/* 325 */     PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
/* 326 */     PlayerTextures textures = profile.getTextures();
/*     */     
/*     */     try {
/* 329 */       urlObject = new URL(texture);
/* 330 */     } catch (MalformedURLException exception) {
/*     */       try {
/* 332 */         urlObject = getUrlFromBase64(texture);
/* 333 */       } catch (MalformedURLException e) {
/* 334 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 337 */     textures.setSkin(urlObject);
/* 338 */     profile.setTextures(textures);
/* 339 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 340 */     if (skullMeta == null) return itemStack; 
/* 341 */     skullMeta.setOwnerProfile(profile);
/* 342 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 343 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 348 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 349 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.k()).a.K.get(entity.getEntityId());
/* 350 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R2\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */