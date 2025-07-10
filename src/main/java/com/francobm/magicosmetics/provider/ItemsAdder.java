/*     */ package com.francobm.magicosmetics.provider;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.balloons.Balloon;
/*     */ import dev.lone.itemsadder.api.CustomEntity;
/*     */ import dev.lone.itemsadder.api.CustomPlayer;
/*     */ import dev.lone.itemsadder.api.CustomStack;
/*     */ import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.ArmorStand;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ public class ItemsAdder {
/*     */   public boolean existModel(String namespaceId) {
/*  21 */     return CustomEntity.isInRegistry(namespaceId);
/*     */   }
/*     */   
/*     */   public CustomStack getCustomStack(String id) {
/*  25 */     return CustomStack.getInstance(id);
/*     */   }
/*     */   
/*     */   public CustomStack getCustomStack(ItemStack itemStack) {
/*  29 */     return CustomStack.byItemStack(itemStack);
/*     */   }
/*     */   
/*     */   public ItemStack getCustomItemStack(String id) {
/*  33 */     CustomStack customStack = CustomStack.getInstance(id);
/*  34 */     if (customStack == null) return null; 
/*  35 */     return customStack.getItemStack();
/*     */   }
/*     */   
/*     */   public String replaceFontImageWithoutColor(String id) {
/*  39 */     return ChatColor.stripColor(FontImageWrapper.replaceFontImages(id));
/*     */   }
/*     */   
/*     */   public String replaceFontImages(String id) {
/*  43 */     return FontImageWrapper.replaceFontImages(id);
/*     */   }
/*     */   
/*     */   public void stopEmote(Player player) {
/*  47 */     CustomPlayer.stopEmote(player);
/*     */   }
/*     */   
/*     */   public boolean hasEmote(Player player) {
/*     */     try {
/*  52 */       return (CustomPlayer.byAlreadySpawned((Entity)player) != null);
/*  53 */     } catch (Exception exception) {
/*     */       
/*  55 */       return false;
/*     */     } 
/*     */   }
/*     */   public void balloonEmote(Player player) {
/*  59 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  60 */     CustomPlayer customPlayer = CustomPlayer.byAlreadySpawned((Entity)player);
/*  61 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  62 */     if (playerData.getBalloon() == null) {
/*  63 */       plugin.getLogger().info("No balloon found for player " + player.getName());
/*     */       return;
/*     */     } 
/*  66 */     if (customPlayer == null) {
/*  67 */       plugin.getLogger().warning("Player " + player.getName() + " is not spawned");
/*     */       return;
/*     */     } 
/*  70 */     ArmorStand armorStand = ((VersionHandler)plugin.getVersion()).createArmor(player);
/*  71 */     ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)armorStand);
/*  72 */     CustomEntity.Bone bone = customPlayer.getBone("pbody_2");
/*  73 */     plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)plugin, task -> { if (bone == null) { task.cancel(); ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)player); plugin.getLogger().warning("Could not find bone left_arm_slot for player " + player.getName()); return; }  if (!hasEmote(player)) { task.cancel(); ((Balloon)playerData.getBalloon()).setLeashedHolder((Entity)player); plugin.getLogger().warning("ArmorStand for player " + player.getName() + " is invalid"); return; }  armorStand.teleport(bone.getLocation()); }0L, 1L);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void backPackEmote(Player player) {
/*  92 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  93 */     CustomPlayer customPlayer = CustomPlayer.byAlreadySpawned((Entity)player);
/*  94 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  95 */     if (customPlayer == null)
/*  96 */       return;  if (playerData.getBag() == null) {
/*  97 */       plugin.getLogger().info("No backpack found for player " + player.getName());
/*     */       return;
/*     */     } 
/* 100 */     Bag bag = (Bag)playerData.getBag();
/* 101 */     customPlayer.addPassenger((LivingEntity)bag.getBag().getEntity());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\ItemsAdder.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */