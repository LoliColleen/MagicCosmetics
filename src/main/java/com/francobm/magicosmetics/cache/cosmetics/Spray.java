/*     */ package com.francobm.magicosmetics.cache.cosmetics;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.api.SprayKeys;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.events.SprayDrawingEvent;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.awt.image.BufferedImage;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FluidCollisionMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.util.RayTraceResult;
/*     */ 
/*     */ public class Spray extends Cosmetic {
/*     */   private CustomSpray customSpray;
/*     */   private BukkitTask bukkitTask;
/*     */   private BufferedImage image;
/*     */   
/*     */   public Spray(String id, String name, ItemStack itemStack, int modelData, boolean colored, CosmeticType cosmeticType, Color color, String permission, boolean texture, BufferedImage image, boolean itemImage, boolean hideMenu, boolean useEmote, NamespacedKey namespacedKey) {
/*  34 */     super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
/*  35 */     this.itemImage = itemImage;
/*  36 */     if (image == null) {
/*  37 */       this.image = null;
/*     */       return;
/*     */     } 
/*  40 */     this.image = Utils.deepCopy(image);
/*     */   }
/*     */   private boolean itemImage; private boolean paint = false; private long coolDown;
/*     */   
/*     */   protected void updateCosmetic(Cosmetic cosmetic) {
/*  45 */     super.updateCosmetic(cosmetic);
/*  46 */     Spray spray = (Spray)cosmetic;
/*  47 */     this.itemImage = spray.itemImage;
/*  48 */     if (spray.image == null) {
/*  49 */       this.image = null;
/*     */       return;
/*     */     } 
/*  52 */     this.image = Utils.deepCopy(spray.image);
/*     */   }
/*     */   
/*     */   public void draw(Player player, BlockFace blockFace, Location location, int rotation) {
/*  56 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  57 */     remove();
/*  58 */     if (this.itemImage) {
/*  59 */       ItemStack item = getItemColor(player);
/*  60 */       ItemMeta itemMeta = item.getItemMeta();
/*  61 */       itemMeta.setDisplayName("");
/*  62 */       item.setItemMeta(itemMeta);
/*  63 */       Utils.sendSound(player, Sound.getSound("spray"));
/*  64 */       this.customSpray = plugin.getVersion().createCustomSpray(player, location, blockFace, item, null, rotation);
/*  65 */       this.customSpray.spawn(player);
/*  66 */       this.customSpray.setPreview(true);
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     if (this.image != null) {
/*  71 */       ItemStack map = Utils.getMapImage(player, this.image, this);
/*  72 */       MapView mapView = ((MapMeta)map.getItemMeta()).getMapView();
/*  73 */       Utils.sendSound(player, Sound.getSound("spray"));
/*  74 */       this.customSpray = plugin.getVersion().createCustomSpray(player, location, blockFace, map.clone(), mapView, rotation);
/*  75 */       this.customSpray.spawn(player);
/*  76 */       this.customSpray.setPreview(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Player player, SprayKeys key) {
/*  81 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  82 */     if (plugin.getSprayCooldown() > 0) {
/*  83 */       if (this.coolDown > System.currentTimeMillis()) {
/*  84 */         int seconds = (int)((this.coolDown - System.currentTimeMillis()) / 1000L);
/*  85 */         Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/*     */         return;
/*     */       } 
/*  88 */       long milliseconds = plugin.getSprayCooldown() * 1000L;
/*  89 */       this.coolDown = System.currentTimeMillis() + milliseconds;
/*     */     } 
/*  91 */     remove();
/*  92 */     if (this.itemImage) {
/*  93 */       int rotation; ItemStack item = getItemColor(player);
/*  94 */       ItemMeta itemMeta = item.getItemMeta();
/*  95 */       itemMeta.setDisplayName("");
/*  96 */       item.setItemMeta(itemMeta);
/*  97 */       Location location = player.getEyeLocation();
/*  98 */       RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10.0D, FluidCollisionMode.ALWAYS, false, 1.0D, entity -> false);
/*  99 */       if (result == null)
/* 100 */         return;  if (result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME)
/*     */         return; 
/* 102 */       if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
/* 103 */         rotation = Utils.getRotation(player.getLocation().getYaw(), false) * 90;
/*     */       } else {
/* 105 */         rotation = 0;
/*     */       } 
/*     */       
/* 108 */       SprayDrawingEvent event = new SprayDrawingEvent(player, result.getHitBlock(), key);
/* 109 */       Bukkit.getPluginManager().callEvent((Event)event);
/* 110 */       if (event.isCancelled())
/* 111 */         return;  Location frameLoc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
/* 112 */       Utils.sendAllSound(frameLoc, Sound.getSound("spray"));
/* 113 */       this.customSpray = plugin.getVersion().createCustomSpray(player, frameLoc, result.getHitBlockFace(), item, null, rotation);
/* 114 */       update();
/* 115 */       this.bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)plugin, () -> { if (this.customSpray == null) { this.bukkitTask.cancel(); return; }  remove(); }plugin
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 121 */           .getSprayStayTime());
/*     */       return;
/*     */     } 
/* 124 */     if (this.image != null) {
/* 125 */       int rotation; ItemStack map = Utils.getMapImage(player, this.image, this);
/* 126 */       MapView mapView = ((MapMeta)map.getItemMeta()).getMapView();
/* 127 */       Location location = player.getEyeLocation();
/* 128 */       RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10.0D, FluidCollisionMode.ALWAYS, false, 1.0D, entity -> false);
/* 129 */       if (result == null)
/* 130 */         return;  if (result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME)
/*     */         return; 
/* 132 */       if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
/* 133 */         rotation = Utils.getRotation(player.getLocation().getYaw(), false) * 45;
/*     */       } else {
/* 135 */         rotation = 0;
/*     */       } 
/* 137 */       SprayDrawingEvent event = new SprayDrawingEvent(player, result.getHitBlock(), key);
/* 138 */       Bukkit.getPluginManager().callEvent((Event)event);
/* 139 */       if (event.isCancelled())
/* 140 */         return;  Location frameLoc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
/* 141 */       Utils.sendAllSound(frameLoc, Sound.getSound("spray"));
/* 142 */       this.customSpray = plugin.getVersion().createCustomSpray(player, frameLoc, result.getHitBlockFace(), map.clone(), mapView, rotation);
/* 143 */       update();
/*     */     } 
/*     */     
/* 146 */     this.bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)plugin, () -> { if (this.customSpray == null) { this.bukkitTask.cancel(); return; }  remove(); }plugin
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         .getSprayStayTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 157 */     if (this.customSpray == null)
/* 158 */       return;  if (this.customSpray.isPreview())
/* 159 */       return;  this.customSpray.spawn(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lendToEntity() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hide(Player player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void show(Player player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 179 */     if (this.customSpray != null) {
/* 180 */       this.customSpray.setPreview(false);
/* 181 */       this.customSpray.remove();
/* 182 */       this.customSpray = null;
/*     */     } 
/* 184 */     if (this.bukkitTask != null) {
/* 185 */       this.bukkitTask.cancel();
/* 186 */       this.bukkitTask = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearClose() {
/* 192 */     if (this.customSpray != null) {
/* 193 */       this.customSpray.setPreview(false);
/* 194 */       this.customSpray.remove();
/* 195 */       this.customSpray = null;
/*     */     } 
/* 197 */     if (this.bukkitTask != null) {
/* 198 */       this.bukkitTask.cancel();
/* 199 */       this.bukkitTask = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public BufferedImage getImage() {
/* 204 */     return this.image;
/*     */   }
/*     */   
/*     */   public boolean isPaint() {
/* 208 */     return this.paint;
/*     */   }
/*     */   
/*     */   public void setPaint(boolean paint) {
/* 212 */     this.paint = paint;
/*     */   }
/*     */   
/*     */   public boolean isItemImage() {
/* 216 */     return this.itemImage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(Player player) {
/* 221 */     if (this.customSpray == null)
/* 222 */       return;  this.customSpray.spawn(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawn(Player player) {
/* 227 */     if (this.customSpray == null)
/* 228 */       return;  this.customSpray.remove(player);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\Spray.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */