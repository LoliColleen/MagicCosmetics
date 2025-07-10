/*     */ package com.francobm.magicosmetics.managers;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Zone;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import org.bukkit.FluidCollisionMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.RayTraceResult;
/*     */ 
/*     */ public class ZonesManager {
/*  18 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */   
/*     */   public void saveZone(Player player, String name) {
/*  21 */     if (!player.hasPermission("magicosmetics.zones")) {
/*  22 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  25 */     Zone zone = Zone.getZone(name);
/*  26 */     if (zone == null) {
/*  27 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  30 */     if (zone.getNpc() == null) {
/*  31 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC Location!");
/*     */       return;
/*     */     } 
/*  34 */     if (zone.getBalloon() == null) {
/*  35 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC's Balloon Location!");
/*     */       return;
/*     */     } 
/*  38 */     if (zone.getEnter() == null) {
/*  39 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Enter Location!");
/*     */       return;
/*     */     } 
/*  42 */     if (zone.getExit() == null) {
/*  43 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Exit Location!");
/*     */       return;
/*     */     } 
/*  46 */     if (zone.getCorn1() == null) {
/*  47 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn1 Location!");
/*     */       return;
/*     */     } 
/*  50 */     if (zone.getCorn2() == null) {
/*  51 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn2 Location!");
/*     */       return;
/*     */     } 
/*  54 */     if (zone.getSprayLoc() == null) {
/*  55 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Spray Location!");
/*     */       return;
/*     */     } 
/*  58 */     Zone.saveZone(name);
/*  59 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void addZone(Player player, String name) {
/*  63 */     if (!player.hasPermission("magicosmetics.zones")) {
/*  64 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  67 */     if (Zone.getZone(name) != null) {
/*  68 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  71 */     Zone.addZone(name);
/*  72 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*  73 */     giveCorn(player, name);
/*     */   }
/*     */   
/*     */   public void removeZone(Player player, String name) {
/*  77 */     if (!player.hasPermission("magicosmetics.zones")) {
/*  78 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  81 */     if (Zone.getZone(name) == null) {
/*  82 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  85 */     Zone.removeZone(name);
/*  86 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void giveCorn(Player player, String name) {
/*  90 */     if (!player.hasPermission("magicosmetics.zones")) {
/*  91 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  94 */     Zone zone = Zone.getZone(name);
/*  95 */     if (zone == null) {
/*  96 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/*  99 */     zone.giveCorns(player);
/* 100 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   public void setSpray(Player player, String name) {
/*     */     int rotation;
/* 104 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 105 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 108 */     Zone zone = Zone.getZone(name);
/* 109 */     if (zone == null) {
/* 110 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 113 */     Location location = player.getEyeLocation();
/* 114 */     RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10.0D, FluidCollisionMode.ALWAYS, false, 1.0D, entity -> false);
/* 115 */     if (result == null)
/* 116 */       return;  if (result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME)
/*     */       return; 
/* 118 */     if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
/* 119 */       rotation = Utils.getRotation(player.getLocation().getYaw(), false) * 45;
/*     */     } else {
/* 121 */       rotation = 0;
/*     */     } 
/* 123 */     Location loc = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
/* 124 */     zone.setSprayLoc(loc);
/* 125 */     zone.setSprayFace(result.getHitBlockFace());
/* 126 */     zone.setRotation(rotation);
/* 127 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void setBalloonNPC(Player player, String name) {
/* 131 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 132 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 135 */     Zone zone = Zone.getZone(name);
/* 136 */     if (zone == null) {
/* 137 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 140 */     zone.setBalloon(player.getLocation());
/* 141 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void setZoneNPC(Player player, String name) {
/* 145 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 146 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 149 */     Zone zone = Zone.getZone(name);
/* 150 */     if (zone == null) {
/* 151 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 154 */     zone.setNpc(player.getLocation());
/* 155 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void setZoneEnter(Player player, String name) {
/* 159 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 160 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 163 */     Zone zone = Zone.getZone(name);
/* 164 */     if (zone == null) {
/* 165 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 168 */     zone.setEnter(player.getLocation().clone());
/* 169 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void setZoneExit(Player player, String name) {
/* 173 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 174 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 177 */     Zone zone = Zone.getZone(name);
/* 178 */     if (zone == null) {
/* 179 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 182 */     zone.setExit(player.getLocation());
/* 183 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void disableZone(Player player, String name) {
/* 187 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 188 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 191 */     Zone zone = Zone.getZone(name);
/* 192 */     if (zone == null) {
/* 193 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 196 */     zone.setActive(false);
/* 197 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void enableZone(Player player, String name) {
/* 201 */     if (!player.hasPermission("magicosmetics.zones")) {
/* 202 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 205 */     Zone zone = Zone.getZone(name);
/* 206 */     if (zone == null) {
/* 207 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */       return;
/*     */     } 
/* 210 */     if (zone.getNpc() == null) {
/* 211 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC Location!");
/*     */       return;
/*     */     } 
/* 214 */     if (zone.getBalloon() == null) {
/* 215 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the NPC's Balloon Location!");
/*     */       return;
/*     */     } 
/* 218 */     if (zone.getEnter() == null) {
/* 219 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Enter Location!");
/*     */       return;
/*     */     } 
/* 222 */     if (zone.getExit() == null) {
/* 223 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Exit Location!");
/*     */       return;
/*     */     } 
/* 226 */     if (zone.getCorn1() == null) {
/* 227 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn1 Location!");
/*     */       return;
/*     */     } 
/* 230 */     if (zone.getCorn2() == null) {
/* 231 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Corn2 Location!");
/*     */       return;
/*     */     } 
/* 234 */     if (zone.getSprayLoc() == null) {
/* 235 */       Utils.sendMessage((CommandSender)player, this.plugin.prefix + "§cSet the Spray Location!");
/*     */       return;
/*     */     } 
/* 238 */     if (this.plugin.getUser() == null)
/* 239 */       return;  zone.setActive(true);
/* 240 */     Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/*     */   }
/*     */   
/*     */   public void exitZone(Player player) {
/* 244 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 245 */     playerData.exitZone();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\managers\ZonesManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */