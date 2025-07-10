/*     */ package com.francobm.magicosmetics.listeners;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.SprayKeys;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Zone;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.CosmeticInventory;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerRespawnEvent;
/*     */ import org.bukkit.event.player.PlayerSwapHandItemsEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.event.player.PlayerToggleSneakEvent;
/*     */ import org.bukkit.event.player.PlayerUnleashEntityEvent;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class PlayerListener implements Listener {
/*  39 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent event) {
/*  43 */     Player player = event.getPlayer();
/*  44 */     this.plugin.getVersion().getPacketReader().injectPlayer(player);
/*  45 */     if (this.plugin.isHuskSync()) {
/*     */       return;
/*     */     }
/*  48 */     this.plugin.getSql().loadPlayerAsync(player).thenAccept(playerData -> {
/*     */           if (this.plugin.isProxy()) {
/*     */             Objects.requireNonNull(playerData);
/*     */             this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, playerData::sendLoadPlayerData);
/*     */           } 
/*     */         });
/*     */   }
/*     */   @EventHandler
/*     */   public void onLoadData(PlayerDataLoadEvent event) {
/*  57 */     PlayerData playerData = event.getPlayerData();
/*  58 */     playerData.verifyWorldBlackList(this.plugin);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onCommand(PlayerCommandPreprocessEvent event) {
/*  63 */     Player player = event.getPlayer();
/*  64 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  65 */     if (!playerData.isZone())
/*  66 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onQuit(PlayerQuitEvent event) {
/*  71 */     Player player = event.getPlayer();
/*  72 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     this.plugin.getVersion().getPacketReader().removePlayer(player);
/*  78 */     if (playerData.isZone()) {
/*  79 */       playerData.exitZoneSync();
/*     */     }
/*  81 */     this.plugin.getSql().savePlayerAsync(playerData);
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onTeleport(PlayerTeleportEvent event) {
/*  86 */     Player player = event.getPlayer();
/*  87 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  88 */     if (playerData.isZone()) {
/*  89 */       if (!playerData.isSpectator())
/*  90 */         return;  event.setCancelled(true);
/*     */     } 
/*     */     
/*  93 */     playerData.clearCosmeticsInUse(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onUnleash(PlayerUnleashEntityEvent event) {
/* 102 */     if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
/* 103 */       return;  if (!event.getEntity().hasMetadata("cosmetics"))
/* 104 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void OnLeash(PlayerLeashEntityEvent event) {
/* 109 */     if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
/* 110 */       return;  if (!event.getEntity().hasMetadata("cosmetics"))
/* 111 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onRespawn(PlayerRespawnEvent event) {
/* 116 */     Player player = event.getPlayer();
/* 117 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 118 */     playerData.activeCosmeticsInventory();
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDrop(PlayerDropItemEvent event) {
/* 123 */     Player player = event.getPlayer();
/* 124 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 125 */     if (playerData.getSpray() != null) {
/* 126 */       if (this.plugin.getSprayKey() == null)
/* 127 */         return;  if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_Q))
/* 128 */         return;  if (!player.isSneaking())
/* 129 */         return;  event.setCancelled(true);
/* 130 */       playerData.draw(this.plugin.getSprayKey());
/*     */     } 
/* 132 */     if (!Utils.isNewerThan1206())
/*     */       return; 
/* 134 */     String nbt = this.plugin.getVersion().isNBTCosmetic(event.getItemDrop().getItemStack());
/* 135 */     if (nbt == null || nbt.isEmpty())
/* 136 */       return;  event.getItemDrop().remove();
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onSneak(PlayerToggleSneakEvent event) {
/* 141 */     Player player = event.getPlayer();
/* 142 */     if (!event.isSneaking())
/* 143 */       return;  this.plugin.getZonesManager().exitZone(player);
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onDead(PlayerDeathEvent event) {
/* 148 */     Player player = event.getEntity();
/* 149 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 150 */     if (playerData == null)
/* 151 */       return;  playerData.clearCosmeticsInUse(false);
/* 152 */     if (event.getKeepInventory())
/* 153 */       return;  Iterator<ItemStack> stackList = event.getDrops().iterator();
/* 154 */     while (stackList.hasNext()) {
/* 155 */       ItemStack itemStack = stackList.next();
/* 156 */       if (itemStack == null)
/* 157 */         break;  if (playerData.getHat() != null && playerData.getHat().isCosmetic(itemStack)) {
/* 158 */         stackList.remove();
/*     */         continue;
/*     */       } 
/* 161 */       if (playerData.getWStick() != null && playerData.getWStick().isCosmetic(itemStack)) {
/* 162 */         stackList.remove();
/*     */       }
/*     */     } 
/* 165 */     if (playerData.getHat() != null && playerData.getHat().getCurrentItemSaved() != null && 
/* 166 */       !event.getKeepInventory() && playerData.getHat().isOverlaps()) {
/* 167 */       event.getDrops().add(playerData.getHat().leftItemAndGet());
/*     */     }
/*     */     
/* 170 */     if (playerData.getWStick() != null && playerData.getWStick().getCurrentItemSaved() != null && 
/* 171 */       !event.getKeepInventory() && playerData.getWStick().isOverlaps()) {
/* 172 */       event.getDrops().add(playerData.getWStick().leftItemAndGet());
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onItemFrame(PlayerInteractEntityEvent event) {
/* 178 */     Player player = event.getPlayer();
/* 179 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 180 */     if (event.getHand() != EquipmentSlot.OFF_HAND)
/* 181 */       return;  if (playerData.getWStick() == null)
/* 182 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onBlock(BlockPlaceEvent event) {
/* 187 */     if (event.getHand() != EquipmentSlot.OFF_HAND)
/* 188 */       return;  Player player = event.getPlayer();
/* 189 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 190 */     if (playerData.getWStick() == null)
/* 191 */       return;  if (!playerData.getWStick().isCosmetic(event.getItemInHand()))
/* 192 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInteractDupe(PlayerInteractEvent event) {
/* 197 */     if (!Utils.isNewerThan1206())
/* 198 */       return;  if (event.getHand() != EquipmentSlot.OFF_HAND)
/* 199 */       return;  if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
/* 200 */       return;  Player player = event.getPlayer();
/* 201 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 202 */     if (playerData.getWStick() == null)
/* 203 */       return;  ItemStack itemStack = event.getItem();
/* 204 */     if (itemStack == null)
/* 205 */       return;  String nbt = this.plugin.getVersion().isNBTCosmetic(itemStack);
/* 206 */     if (nbt == null || nbt.isEmpty())
/* 207 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInteract(PlayerInteractEvent event) {
/* 212 */     Player player = event.getPlayer();
/* 213 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 214 */     ItemStack itemStack = event.getItem();
/* 215 */     if (itemStack != null) {
/* 216 */       if (itemStack.getType() == XMaterial.BLAZE_ROD.parseMaterial()) {
/* 217 */         String nbt = this.plugin.getVersion().isNBTCosmetic(itemStack);
/*     */         
/* 219 */         if (!nbt.startsWith("wand"))
/* 220 */           return;  Zone zone = Zone.getZone(nbt.substring(4));
/* 221 */         if (zone == null)
/* 222 */           return;  event.setCancelled(true);
/* 223 */         if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
/* 224 */           Location location = event.getClickedBlock().getLocation();
/* 225 */           zone.setCorn1(location);
/* 226 */           player.sendMessage(this.plugin.prefix + this.plugin.prefix);
/*     */           return;
/*     */         } 
/* 229 */         if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
/* 230 */           Location location = event.getClickedBlock().getLocation();
/* 231 */           zone.setCorn2(location);
/* 232 */           player.sendMessage(this.plugin.prefix + this.plugin.prefix);
/*     */           return;
/*     */         } 
/*     */         return;
/*     */       } 
/* 237 */       if (itemStack.getType().toString().toUpperCase().endsWith("HELMET") && (
/* 238 */         event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && 
/* 239 */         playerData.getHat() != null) {
/* 240 */         if (playerData.getHat().isHideCosmetic())
/* 241 */           return;  event.setCancelled(true);
/* 242 */         ItemStack returnItem = playerData.getHat().changeItem(itemStack);
/* 243 */         if (event.getHand() == EquipmentSlot.OFF_HAND) {
/* 244 */           player.getInventory().setItemInOffHand(returnItem);
/*     */         } else {
/* 246 */           player.getInventory().setItemInMainHand(returnItem);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     if (this.plugin.getSprayKey() == null)
/* 257 */       return;  if (playerData.getSpray() == null)
/* 258 */       return;  if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
/* 259 */       if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_RC))
/* 260 */         return;  if (!player.isSneaking())
/* 261 */         return;  playerData.draw(this.plugin.getSprayKey());
/* 262 */       event.setCancelled(true);
/*     */     } 
/* 264 */     if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
/* 265 */       if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_LC))
/* 266 */         return;  if (!player.isSneaking())
/* 267 */         return;  playerData.draw(this.plugin.getSprayKey());
/* 268 */       event.setCancelled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onPlayerChange(PlayerSwapHandItemsEvent event) {
/* 274 */     Player player = event.getPlayer();
/* 275 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 276 */     ItemStack mainHand = event.getMainHandItem();
/* 277 */     if (playerData.getWStick() != null) {
/* 278 */       event.setCancelled(true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     if (playerData.getSpray() == null)
/* 291 */       return;  if (this.plugin.getSprayKey() == null)
/* 292 */       return;  if (!this.plugin.getSprayKey().isKey(SprayKeys.SHIFT_F))
/* 293 */       return;  if (!player.isSneaking())
/* 294 */       return;  playerData.draw(this.plugin.getSprayKey());
/* 295 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onAttack(EntityDamageByEntityEvent event) {
/* 300 */     if (!(event.getDamager() instanceof Player))
/* 301 */       return;  if (!(event.getEntity() instanceof org.bukkit.entity.PufferFish))
/* 302 */       return;  if (!event.getEntity().hasMetadata("cosmetics"))
/* 303 */       return;  event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void playerHeld(PlayerItemHeldEvent event) {
/* 308 */     Player player = event.getPlayer();
/* 309 */     ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
/* 310 */     ItemStack oldItem = player.getInventory().getItem(event.getPreviousSlot());
/* 311 */     if (oldItem != null) {
/* 312 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 313 */       if (playerData.getHat() != null && 
/* 314 */         playerData.getHat().isCosmetic(oldItem)) {
/* 315 */         player.getInventory().removeItem(new ItemStack[] { oldItem });
/*     */       }
/*     */       
/* 318 */       if (playerData.getWStick() != null && 
/* 319 */         playerData.getWStick().isCosmetic(oldItem)) {
/* 320 */         player.getInventory().removeItem(new ItemStack[] { oldItem });
/*     */       }
/*     */     } 
/*     */     
/* 324 */     if (newItem != null) {
/* 325 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 326 */       if (playerData.getHat() != null && 
/* 327 */         playerData.getHat().isCosmetic(newItem)) {
/* 328 */         player.getInventory().removeItem(new ItemStack[] { newItem });
/*     */       }
/*     */       
/* 331 */       if (playerData.getWStick() != null && 
/* 332 */         playerData.getWStick().isCosmetic(newItem)) {
/* 333 */         player.getInventory().removeItem(new ItemStack[] { newItem });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void playerDrop(PlayerDropItemEvent event) {
/* 344 */     Player player = event.getPlayer();
/* 345 */     Item item = event.getItemDrop();
/* 346 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 347 */     if (playerData.getHat() != null && 
/* 348 */       playerData.getHat().isCosmetic(item.getItemStack())) {
/* 349 */       event.setCancelled(false);
/* 350 */       item.remove();
/*     */     } 
/*     */     
/* 353 */     if (playerData.getWStick() != null && 
/* 354 */       playerData.getWStick().isCosmetic(item.getItemStack())) {
/* 355 */       event.setCancelled(false);
/* 356 */       item.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onChangeWorld(PlayerChangedWorldEvent event) {
/* 364 */     Player player = event.getPlayer();
/* 365 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 366 */     playerData.verifyWorldBlackList(this.plugin);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onInteractInventory(CosmeticInventoryUpdateEvent event) {
/* 371 */     Player player = event.getPlayer();
/* 372 */     Cosmetic cosmetic = event.getCosmetic();
/* 373 */     if (cosmetic.isHideCosmetic())
/* 374 */       return;  ItemStack itemStack = event.getItemToChange();
/* 375 */     CosmeticInventory cosmeticInventory = (CosmeticInventory)cosmetic;
/* 376 */     if (itemStack == null || itemStack.getType().isAir()) {
/* 377 */       if (!cosmeticInventory.isOverlaps()) {
/* 378 */         cosmeticInventory.setCurrentItemSaved(null);
/*     */       }
/* 380 */       cosmetic.update();
/*     */       return;
/*     */     } 
/* 383 */     if (this.plugin.getMagicCrates() != null && this.plugin.getMagicCrates().hasInCrate(player))
/* 384 */       return;  if (this.plugin.getMagicGestures() != null && this.plugin.getMagicGestures().hasInWardrobe(player))
/* 385 */       return;  boolean hasItemSaved = (cosmeticInventory.getCurrentItemSaved() != null);
/* 386 */     if (hasItemSaved && 
/* 387 */       itemStack.isSimilar(cosmeticInventory.getCurrentItemSaved()))
/*     */       return; 
/* 389 */     if (!cosmeticInventory.isOverlaps()) {
/* 390 */       if (cosmetic.isCosmetic(itemStack))
/* 391 */         return;  if (player.getInventory().getItemInMainHand().getType().isAir() || cosmetic.isCosmetic(player.getInventory().getItemInMainHand())) {
/* 392 */         player.getInventory().setItemInMainHand(null);
/*     */       }
/* 394 */       cosmeticInventory.setCurrentItemSaved(itemStack);
/*     */       return;
/*     */     } 
/* 397 */     ItemStack oldItem = cosmeticInventory.changeItem(itemStack);
/* 398 */     if (oldItem == null) {
/* 399 */       if (cosmetic.isCosmetic(player.getInventory().getItemInMainHand())) {
/* 400 */         player.getInventory().setItemInMainHand(null);
/*     */       }
/*     */       return;
/*     */     } 
/* 404 */     if (hasItemSaved && oldItem.isSimilar(cosmeticInventory.getCurrentItemSaved()))
/* 405 */       return;  if (itemStack.isSimilar(oldItem))
/* 406 */       return;  if (player.getOpenInventory().getType() == InventoryType.PLAYER) {
/* 407 */       player.setItemOnCursor(oldItem);
/*     */       return;
/*     */     } 
/* 410 */     if (player.getInventory().getItemInMainHand().getType().isAir() || cosmetic.isCosmetic(player.getInventory().getItemInMainHand())) {
/* 411 */       player.getInventory().setItemInMainHand(oldItem);
/*     */       return;
/*     */     } 
/* 414 */     player.getInventory().addItem(new ItemStack[] { oldItem });
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInventory(InventoryClickEvent event) {
/* 419 */     Player player = (Player)event.getWhoClicked();
/* 420 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 421 */     if (playerData == null)
/* 422 */       return;  if (event.getClickedInventory() == null)
/* 423 */       return;  if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
/* 424 */       if (playerData.getWStick() != null && event.getClick() == ClickType.SWAP_OFFHAND) event.setCancelled(true); 
/*     */       return;
/*     */     } 
/* 427 */     if (playerData.getWStick() != null) {
/* 428 */       if (playerData.getWStick().isHideCosmetic())
/* 429 */         return;  if (event.getClick() == ClickType.SWAP_OFFHAND) {
/* 430 */         event.setCancelled(true);
/*     */         return;
/*     */       } 
/* 433 */       if (event.getCursor() != null && 
/* 434 */         playerData.getWStick().isCosmetic(event.getCursor())) {
/* 435 */         player.setItemOnCursor(null);
/*     */       }
/* 437 */       if (event.getSlotType() == InventoryType.SlotType.QUICKBAR && event.getSlot() == 40) {
/* 438 */         if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.RIGHT || (playerData.getWStick().isCosmetic(event.getCurrentItem()) && event.getCursor() == null && playerData.getWStick().getCurrentItemSaved() == null) || (playerData.getWStick().isCosmetic(event.getCurrentItem()) && event.getCursor() != null && event.getCursor().getType().isAir() && playerData.getWStick().getCurrentItemSaved() == null)) {
/* 439 */           event.setCancelled(true);
/*     */           
/*     */           return;
/*     */         } 
/* 443 */         if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
/* 444 */           if (playerData.getWStick().getCurrentItemSaved() != null) {
/* 445 */             playerData.getWStick().dropItem((event.getClick() == ClickType.CONTROL_DROP));
/* 446 */             event.setCancelled(playerData.getWStick().isOverlaps());
/*     */           } 
/*     */           return;
/*     */         } 
/* 450 */         event.setCancelled(true);
/* 451 */         ItemStack returnItem = playerData.getWStick().changeItem((event.getCursor() != null && event.getCursor().getType().isAir()) ? null : event.getCursor());
/* 452 */         player.setItemOnCursor(returnItem);
/*     */         return;
/*     */       } 
/*     */     } 
/* 456 */     if (playerData.getHat() != null) {
/* 457 */       if (playerData.getHat().isHideCosmetic())
/* 458 */         return;  if (event.getCursor() != null && 
/* 459 */         playerData.getHat().isCosmetic(event.getCursor())) {
/* 460 */         player.setItemOnCursor(null);
/*     */       }
/* 462 */       if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39) {
/* 463 */         if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.RIGHT || (playerData.getHat().isCosmetic(event.getCurrentItem()) && event.getCursor() == null && playerData.getHat().getCurrentItemSaved() == null) || (playerData.getHat().isCosmetic(event.getCurrentItem()) && event.getCursor() != null && event.getCursor().getType().isAir() && playerData.getHat().getCurrentItemSaved() == null)) {
/* 464 */           event.setCancelled(true);
/*     */           
/*     */           return;
/*     */         } 
/* 468 */         if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
/* 469 */           if (playerData.getHat().getCurrentItemSaved() != null) {
/* 470 */             playerData.getHat().dropItem((event.getClick() == ClickType.CONTROL_DROP));
/* 471 */             event.setCancelled(playerData.getHat().isOverlaps());
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 477 */         event.setCancelled(true);
/* 478 */         if (event.getCursor() == null || event.getCursor().getType().isAir() || event.getCursor().getType().name().endsWith("HELMET") || event.getCursor().getType().name().endsWith("HEAD") || player.hasPermission("magicosmetics.hat.use")) {
/* 479 */           ItemStack returnItem = playerData.getHat().changeItem((event.getCursor() != null && event.getCursor().getType().isAir()) ? null : event.getCursor());
/* 480 */           player.setItemOnCursor(returnItem);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\listeners\PlayerListener.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */