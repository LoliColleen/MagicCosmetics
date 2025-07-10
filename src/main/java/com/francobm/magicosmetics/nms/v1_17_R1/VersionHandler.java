/*     */ package com.francobm.magicosmetics.nms.v1_17_R1;
/*     */ import com.francobm.magicosmetics.nms.IRangeManager;
/*     */ import com.francobm.magicosmetics.nms.NPC.ItemSlot;
/*     */ import com.francobm.magicosmetics.nms.NPC.NPC;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.bag.PlayerBag;
/*     */ import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
/*     */ import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
/*     */ import com.francobm.magicosmetics.nms.spray.CustomSpray;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.lang.reflect.Field;
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
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
/*     */ import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.ArmorStand;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.PufferFish;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.map.MapView;
/*     */ 
/*     */ public class VersionHandler extends Version {
/*     */   public void setSpectator(Player player) {
/*  59 */     player.setGameMode(GameMode.SPECTATOR);
/*  60 */     EntityPlayer p = ((CraftPlayer)player).getHandle();
/*  61 */     PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, new EntityPlayer[] { p });
/*     */     try {
/*  63 */       Field packetField = packet.getClass().getDeclaredField("b");
/*  64 */       packetField.setAccessible(true);
/*  65 */       ArrayList<PacketPlayOutPlayerInfo.PlayerInfoData> list = Lists.newArrayList();
/*  66 */       list.add(new PacketPlayOutPlayerInfo.PlayerInfoData(p.getProfile(), 0, EnumGamemode.b, p.getPlayerListName()));
/*  67 */       packetField.set(packet, list);
/*  68 */       p.b.sendPacket((Packet)packet);
/*  69 */       PacketPlayOutGameStateChange packetPlayOutGameStateChange = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3.0F);
/*  70 */       p.b.sendPacket((Packet)packetPlayOutGameStateChange);
/*  71 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/*  72 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player) {
/*  78 */     NPC npc = new NPCHandler();
/*  79 */     npc.addNPC(player);
/*  80 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createNPC(Player player, Location location) {
/*  85 */     NPC npc = new NPCHandler();
/*  86 */     npc.addNPC(player, location);
/*  87 */     npc.spawnNPC(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC(Player player) {
/*  92 */     return (NPC)NPC.npcs.get(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNPC(Player player) {
/*  97 */     NPC npc = (NPC)NPC.npcs.get(player.getUniqueId());
/*  98 */     if (npc == null)
/*  99 */       return;  npc.removeNPC(player);
/* 100 */     NPC.npcs.remove(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public NPC getNPC() {
/* 105 */     return new NPCHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBag createPlayerBag(Player player, double distance, float height, ItemStack backPackItem, ItemStack backPackForMe) {
/* 110 */     return (PlayerBag)new PlayerBagHandler(player, createRangeManager((Entity)player), distance, height, backPackItem, backPackForMe);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerBalloon createPlayerBalloon(Player player, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 115 */     return (PlayerBalloon)new PlayerBalloonHandler(player, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBalloon createEntityBalloon(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
/* 120 */     return (EntityBalloon)new EntityBalloonHandler(entity, space, distance, bigHead, invisibleLeash);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBag createEntityBag(Entity entity, double distance) {
/* 125 */     return (EntityBag)new EntityBagHandler(entity, distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomSpray createCustomSpray(Player player, Location location, BlockFace blockFace, ItemStack itemStack, MapView mapView, int rotation) {
/* 130 */     return (CustomSpray)new CustomSprayHandler(player, location, blockFace, itemStack, mapView, rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void equip(LivingEntity livingEntity, ItemSlot itemSlot, ItemStack itemStack) {
/* 135 */     if (itemStack == null)
/* 136 */       return;  ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
/* 137 */     switch (itemSlot) {
/*     */       case MAIN_HAND:
/* 139 */         list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case OFF_HAND:
/* 142 */         list.add(new Pair(EnumItemSlot.b, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case BOOTS:
/* 145 */         list.add(new Pair(EnumItemSlot.c, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case LEGGINGS:
/* 148 */         list.add(new Pair(EnumItemSlot.d, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case CHESTPLATE:
/* 151 */         list.add(new Pair(EnumItemSlot.e, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */       case HELMET:
/* 154 */         list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
/*     */         break;
/*     */     } 
/*     */     
/* 158 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 159 */       PlayerConnection connection = (((CraftPlayer)p).getHandle()).b;
/* 160 */       connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(livingEntity.getEntityId(), list));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTitle(Player player, String title) {
/* 166 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 167 */     if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
/* 168 */       return;  PacketPlayOutOpenWindow packet = null;
/* 169 */     switch (player.getOpenInventory().getTopInventory().getSize() / 9) {
/*     */       case 1:
/* 171 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.a, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 2:
/* 174 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.b, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 3:
/* 177 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.c, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 4:
/* 180 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.d, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 5:
/* 183 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.e, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */       case 6:
/* 186 */         packet = new PacketPlayOutOpenWindow(entityPlayer.bV.j, Containers.f, (IChatBaseComponent)new ChatMessage(title));
/*     */         break;
/*     */     } 
/* 189 */     if (packet == null)
/* 190 */       return;  entityPlayer.b.sendPacket((Packet)packet);
/* 191 */     entityPlayer.bV.updateInventory();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Player player, Entity entity) {
/* 196 */     Entity e = ((CraftEntity)entity).getHandle();
/* 197 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 198 */     entityPlayer.b.sendPacket((Packet)new PacketPlayOutCamera(e));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setNBTCosmetic(ItemStack itemStack, String key) {
/* 203 */     if (itemStack == null) return null; 
/* 204 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 205 */     itemCosmetic.getOrCreateTag().setString("magic_cosmetic", key);
/* 206 */     return CraftItemStack.asBukkitCopy(itemCosmetic);
/*     */   }
/*     */ 
/*     */   
/*     */   public String isNBTCosmetic(ItemStack itemStack) {
/* 211 */     if (itemStack == null) return null; 
/* 212 */     ItemStack itemCosmetic = CraftItemStack.asNMSCopy(itemStack);
/* 213 */     return itemCosmetic.getOrCreateTag().getString("magic_cosmetic");
/*     */   }
/*     */ 
/*     */   
/*     */   public PufferFish spawnFakePuffer(Location location) {
/* 218 */     EntityPufferFish entityPufferFish = new EntityPufferFish(EntityTypes.at, (World)((CraftWorld)location.getWorld()).getHandle());
/* 219 */     entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 220 */     return (PufferFish)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorStand spawnArmorStand(Location location) {
/* 225 */     EntityArmorStand entityPufferFish = new EntityArmorStand(EntityTypes.c, (World)((CraftWorld)location.getWorld()).getHandle());
/* 226 */     entityPufferFish.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/* 227 */     return (ArmorStand)entityPufferFish.getBukkitEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showEntity(LivingEntity entity, Player... viewers) {
/* 232 */     EntityLiving entityClient = ((CraftLivingEntity)entity).getHandle();
/* 233 */     entityClient.setInvisible(true);
/* 234 */     DataWatcher dataWatcher = entityClient.getDataWatcher();
/* 235 */     PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity((Entity)entityClient);
/* 236 */     PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);
/* 237 */     for (Player viewer : viewers) {
/* 238 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 239 */       view.b.sendPacket((Packet)packet);
/* 240 */       view.b.sendPacket((Packet)metadata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawnFakeEntity(Entity entity, Player... viewers) {
/* 246 */     PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
/* 247 */     for (Player viewer : viewers) {
/* 248 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 249 */       view.b.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachFakeEntity(Entity entity, Entity leashed, Player... viewers) {
/* 255 */     EntityPlayer entityPlayer = ((CraftPlayer)entity).getHandle();
/* 256 */     PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(((CraftEntity)leashed).getHandle(), (Entity)entityPlayer);
/* 257 */     for (Player viewer : viewers) {
/* 258 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 259 */       view.b.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePositionFakeEntity(Entity leashed, Location location) {
/* 265 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 266 */     entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public void teleportFakeEntity(Entity leashed, Set<Player> viewers) {
/* 271 */     Entity entity = ((CraftEntity)leashed).getHandle();
/* 272 */     PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
/* 273 */     for (Player viewer : viewers) {
/* 274 */       EntityPlayer view = ((CraftPlayer)viewer).getHandle();
/* 275 */       view.b.sendPacket((Packet)packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemWithNBTsCopy(ItemStack itemToCopy, ItemStack cosmetic) {
/* 281 */     ItemStack copy = CraftItemStack.asNMSCopy(itemToCopy);
/* 282 */     if (!copy.hasTag()) return cosmetic; 
/* 283 */     ItemStack cosmeticItem = CraftItemStack.asNMSCopy(cosmetic);
/* 284 */     for (String key : copy.getTag().getKeys()) {
/* 285 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 286 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 287 */         NBTTagCompound compound = copy.getTag().getCompound(key);
/* 288 */         NBTTagCompound realCompound = cosmeticItem.getTag().getCompound(key);
/* 289 */         Set<String> keys = compound.getKeys();
/* 290 */         for (String compoundKey : keys) {
/* 291 */           realCompound.set(compoundKey, compound.get(compoundKey));
/*     */         }
/* 293 */         cosmeticItem.getOrCreateTag().set(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 296 */       cosmeticItem.getOrCreateTag().set(key, copy.getTag().get(key));
/*     */     } 
/* 298 */     return CraftItemStack.asBukkitCopy(cosmeticItem);
/*     */   }
/*     */   
/*     */   public ItemStack getItemSavedWithNBTsUpdated(ItemStack itemCombined, ItemStack itemStack) {
/* 302 */     ItemStack copy = CraftItemStack.asNMSCopy(itemCombined);
/* 303 */     if (!copy.hasTag()) return itemStack; 
/* 304 */     ItemStack realItem = CraftItemStack.asNMSCopy(itemStack);
/* 305 */     if (!realItem.hasTag()) return itemStack; 
/* 306 */     for (String key : copy.getTag().getKeys()) {
/* 307 */       if (key.equals("display") || key.equals("CustomModelData"))
/* 308 */         continue;  if (key.equals("PublicBukkitValues")) {
/* 309 */         NBTTagCompound compound = copy.getTag().getCompound(key);
/* 310 */         NBTTagCompound realCompound = realItem.getTag().getCompound(key);
/* 311 */         Set<String> keys = compound.getKeys();
/* 312 */         for (String compoundKey : keys) {
/* 313 */           if (!realCompound.hasKey(compoundKey))
/* 314 */             continue;  realCompound.set(compoundKey, compound.get(compoundKey));
/*     */         } 
/* 316 */         realItem.getTag().set(key, (NBTBase)realCompound);
/*     */         continue;
/*     */       } 
/* 319 */       if (!realItem.getTag().hasKey(key))
/* 320 */         continue;  realItem.getTag().set(key, copy.getTag().get(key));
/*     */     } 
/* 322 */     return CraftItemStack.asBukkitCopy(realItem);
/*     */   }
/*     */   
/*     */   public ItemStack getCustomHead(ItemStack itemStack, String texture) {
/* 326 */     if (itemStack == null) return null; 
/* 327 */     if (texture.isEmpty()) {
/* 328 */       return itemStack;
/*     */     }
/* 330 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 331 */     if (skullMeta == null) return itemStack; 
/* 332 */     GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
/* 333 */     gameProfile.getProperties().put("textures", new Property("textures", texture));
/*     */     try {
/* 335 */       Field profileField = skullMeta.getClass().getDeclaredField("profile");
/* 336 */       profileField.setAccessible(true);
/* 337 */       profileField.set(skullMeta, gameProfile);
/* 338 */     } catch (Exception e) {
/* 339 */       e.printStackTrace();
/*     */     } 
/* 341 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 342 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRangeManager createRangeManager(Entity entity) {
/* 347 */     WorldServer level = ((CraftWorld)entity.getWorld()).getHandle();
/* 348 */     PlayerChunkMap.EntityTracker trackedEntity = (PlayerChunkMap.EntityTracker)(level.getChunkProvider()).a.G.get(entity.getEntityId());
/* 349 */     return (IRangeManager)new RangeManager(trackedEntity);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_17_R1\VersionHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */