/*     */ package com.francobm.magicosmetics.cache.cosmetics.backpacks;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.nms.bag.EntityBag;
/*     */ import com.francobm.magicosmetics.nms.bag.PlayerBag;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.ArmorStand;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.LeatherArmorMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.inventory.meta.PotionMeta;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.util.EulerAngle;
/*     */ 
/*     */ public class Bag extends Cosmetic {
/*     */   private PlayerBag bag1;
/*     */   private EntityBag bag2;
/*     */   private ItemStack bagForMe;
/*     */   private BackPackEngine backPackEngine;
/*     */   
/*     */   public Bag(String id, String name, ItemStack itemStack, int modelData, ItemStack bagForMe, boolean colored, double space, CosmeticType cosmeticType, Color color, double distance, String permission, boolean texture, boolean hideMenu, float height, boolean useEmote, BackPackEngine backPackEngine, NamespacedKey namespacedKey) {
/*  30 */     super(id, name, itemStack, modelData, colored, cosmeticType, color, permission, texture, hideMenu, useEmote, namespacedKey);
/*  31 */     this.bagForMe = bagForMe;
/*  32 */     this.space = space;
/*  33 */     this.distance = distance;
/*  34 */     this.height = height;
/*  35 */     this.backPackEngine = backPackEngine;
/*     */   }
/*     */   private double space; private boolean hide = false; private boolean spectator = false; private double distance; private float height;
/*     */   
/*     */   protected void updateCosmetic(Cosmetic cosmetic) {
/*  40 */     super.updateCosmetic(cosmetic);
/*  41 */     Bag bag = (Bag)cosmetic;
/*  42 */     this.bagForMe = bag.bagForMe;
/*  43 */     this.space = bag.space;
/*  44 */     this.distance = bag.distance;
/*  45 */     this.height = bag.height;
/*  46 */     this.backPackEngine = bag.backPackEngine;
/*     */   }
/*     */   
/*     */   public double getSpace() {
/*  50 */     return this.space;
/*     */   }
/*     */   
/*     */   public void active(Entity entity) {
/*  54 */     if (entity == null)
/*  55 */       return;  if (this.backPackEngine != null) {
/*  56 */       if (this.backPackEngine.getBackPackUniqueId() == null) {
/*  57 */         if (entity.isDead())
/*  58 */           return;  remove();
/*  59 */         this.backPackEngine.spawnModel(entity);
/*  60 */         if (isColored()) {
/*  61 */           this.backPackEngine.tintModel(entity, getColor());
/*     */         }
/*     */       } 
/*     */       return;
/*     */     } 
/*  66 */     if (this.bag2 == null) {
/*  67 */       if (entity.isDead()) {
/*  68 */         remove();
/*     */         return;
/*     */       } 
/*  71 */       remove();
/*  72 */       this.bag2 = MagicCosmetics.getInstance().getVersion().createEntityBag(entity, this.distance);
/*  73 */       this.bag2.spawnBag();
/*     */     } 
/*     */     
/*  76 */     this.bag2.addPassenger();
/*  77 */     this.bag2.setItemOnHelmet(getItemColor());
/*  78 */     this.bag2.lookEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void lendToEntity() {
/*  83 */     if (this.bag1 == null) {
/*  84 */       if (this.lendEntity.isDead())
/*  85 */         return;  remove();
/*  86 */       this.bag1 = MagicCosmetics.getInstance().getVersion().createPlayerBag(this.player, getDistance(), this.height, getItemColor(this.player), (getBagForMe() != null) ? getItemColorForMe(this.player) : null);
/*  87 */       this.bag1.setLendEntityId(this.lendEntity.getEntityId());
/*  88 */       if (this.hide) {
/*  89 */         hideSelf(false);
/*     */       }
/*     */     } 
/*  92 */     this.bag1.addPassenger(true);
/*  93 */     this.bag1.lookEntity(this.lendEntity.getLocation().getYaw(), this.lendEntity.getLocation().getPitch(), true);
/*  94 */     this.bag1.spawn(true);
/*  95 */     if (this.hide)
/*  96 */       return;  this.bag1.spawnSelf(this.player);
/*  97 */     this.bag1.lookEntity(this.lendEntity.getLocation().getYaw(), this.lendEntity.getLocation().getPitch(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void hide(Player player) {
/* 102 */     if (this.backPackEngine != null) {
/* 103 */       this.backPackEngine.hideModel(player);
/*     */       return;
/*     */     } 
/* 106 */     if (this.bag1 != null) {
/* 107 */       this.bag1.addHideViewer(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void show(Player player) {
/* 113 */     if (this.backPackEngine != null) {
/* 114 */       this.backPackEngine.showModel(player);
/*     */       return;
/*     */     } 
/* 117 */     if (this.bag1 != null) {
/* 118 */       this.bag1.removeHideViewer(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 124 */     if (this.lendEntity != null) {
/* 125 */       lendToEntity();
/*     */       return;
/*     */     } 
/* 128 */     if (isHideCosmetic()) {
/* 129 */       remove();
/*     */       return;
/*     */     } 
/* 132 */     if (this.backPackEngine != null) {
/* 133 */       if (this.backPackEngine.getBackPackUniqueId() == null) {
/* 134 */         if (this.player.isDead())
/* 135 */           return;  if (this.player.getGameMode() == GameMode.SPECTATOR)
/* 136 */           return;  remove();
/* 137 */         this.backPackEngine.spawnModel((Entity)this.player);
/*     */         
/* 139 */         if (isColored()) {
/* 140 */           this.backPackEngine.tintModel((Entity)this.player, getColor());
/*     */         }
/*     */       } 
/*     */       return;
/*     */     } 
/* 145 */     if (this.bag1 == null) {
/* 146 */       if (this.player.isDead())
/* 147 */         return;  if (this.player.getGameMode() == GameMode.SPECTATOR)
/*     */         return; 
/* 149 */       remove();
/* 150 */       this.bag1 = MagicCosmetics.getInstance().getVersion().createPlayerBag(this.player, getDistance(), this.height, getItemColor(this.player), (getBagForMe() != null) ? getItemColorForMe(this.player) : null);
/* 151 */       if (this.hide) {
/* 152 */         hideSelf(false);
/*     */       }
/* 154 */       this.bag1.spawn(false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     this.bag1.lookEntity(this.player.getLocation().getYaw(), this.player.getLocation().getPitch(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 167 */     if (this.backPackEngine != null) {
/* 168 */       this.backPackEngine.remove();
/*     */     }
/* 170 */     if (this.bag1 != null) {
/* 171 */       this.bag1.remove();
/*     */     }
/* 173 */     if (this.bag2 != null) {
/* 174 */       this.bag2.remove();
/*     */     }
/* 176 */     this.bag1 = null;
/* 177 */     this.bag2 = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearClose() {
/* 182 */     if (this.backPackEngine != null) {
/* 183 */       this.backPackEngine.remove();
/*     */     }
/* 185 */     if (this.bag1 != null) {
/* 186 */       this.bag1.remove();
/*     */     }
/* 188 */     if (this.bag2 != null) {
/* 189 */       this.bag2.remove();
/*     */     }
/* 191 */     this.bag1 = null;
/* 192 */     this.bag2 = null;
/*     */   }
/*     */   
/*     */   public void setHeadPos(ArmorStand as, double yaw, double pitch) {
/* 196 */     double yint = Math.cos(yaw / Math.PI);
/* 197 */     double zint = Math.sin(yaw / Math.PI);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     double xint = Math.sin(pitch / Math.PI);
/*     */     
/* 207 */     EulerAngle ea = as.getHeadPose();
/* 208 */     ea.setX(xint);
/* 209 */     ea.setY(yint);
/* 210 */     ea.setZ(zint);
/* 211 */     as.setHeadPose(ea);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getBagForMe() {
/* 216 */     return this.bagForMe;
/*     */   }
/*     */   
/*     */   public ItemStack getItemColorForMe() {
/* 220 */     if (this.bagForMe == null) return null; 
/* 221 */     ItemStack itemStack = this.bagForMe.clone();
/* 222 */     if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
/* 223 */       LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemStack.getItemMeta();
/* 224 */       if (getColor() != null) {
/* 225 */         itemMeta.setColor(getColor());
/*     */       }
/* 227 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 228 */       return itemStack;
/*     */     } 
/* 230 */     if (itemStack.getItemMeta() instanceof PotionMeta) {
/* 231 */       PotionMeta itemMeta = (PotionMeta)itemStack.getItemMeta();
/* 232 */       if (getColor() != null) {
/* 233 */         itemMeta.setColor(getColor());
/*     */       }
/* 235 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 236 */       return itemStack;
/*     */     } 
/* 238 */     if (itemStack.getItemMeta() instanceof MapMeta) {
/* 239 */       MapMeta itemMeta = (MapMeta)itemStack.getItemMeta();
/* 240 */       if (getColor() != null) {
/* 241 */         itemMeta.setColor(getColor());
/*     */       }
/* 243 */       itemStack.setItemMeta((ItemMeta)itemMeta);
/* 244 */       return itemStack;
/*     */     } 
/* 246 */     return itemStack;
/*     */   }
/*     */   
/*     */   public ItemStack getItemColorForMe(Player player) {
/* 250 */     if (isTexture()) return getItemColorForMe(); 
/* 251 */     ItemStack itemStack = getItemColorForMe();
/* 252 */     if (itemStack.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) return itemStack; 
/* 253 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 254 */     skullMeta.setOwningPlayer((OfflinePlayer)player);
/* 255 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 256 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void hideSelf(boolean change) {
/* 260 */     if (this.bag1 == null)
/* 261 */       return;  Player player = this.bag1.getPlayer();
/* 262 */     if (change) {
/* 263 */       hide();
/*     */     }
/* 265 */     this.bag1.spawnSelf(player);
/*     */   }
/*     */   
/*     */   public void hide() {
/* 269 */     this.hide = !this.hide;
/*     */   }
/*     */   
/*     */   public void setSpectator(boolean spectator) {
/* 273 */     this.spectator = spectator;
/*     */   }
/*     */   
/*     */   public boolean isSpectator() {
/* 277 */     return this.spectator;
/*     */   }
/*     */   
/*     */   public PlayerBag getBag() {
/* 281 */     return this.bag1;
/*     */   }
/*     */   
/*     */   public double getDistance() {
/* 285 */     return this.distance;
/*     */   }
/*     */   
/*     */   public boolean isHide() {
/* 289 */     return this.hide;
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 293 */     return this.height;
/*     */   }
/*     */   
/*     */   public BackPackEngine getBackPackEngine() {
/* 297 */     return this.backPackEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawn(Player player) {
/* 302 */     if (this.bag1 != null) {
/* 303 */       this.bag1.spawn(player);
/*     */     }
/* 305 */     if (this.bag2 != null) {
/* 306 */       this.bag2.spawnBag(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void despawn(Player player) {
/* 312 */     if (this.bag1 != null) {
/* 313 */       this.bag1.remove(player);
/*     */     }
/* 315 */     if (this.bag2 != null) {
/* 316 */       this.bag2.remove(player);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getBackpackId() {
/* 321 */     if (this.bag1 == null) return -1; 
/* 322 */     return this.bag1.getBackpackId();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\backpacks\Bag.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */