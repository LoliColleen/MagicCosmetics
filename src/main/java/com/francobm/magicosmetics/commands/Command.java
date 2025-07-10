/*     */ package com.francobm.magicosmetics.commands;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.Token;
/*     */ import com.francobm.magicosmetics.cache.Zone;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class Command
/*     */   implements CommandExecutor, TabCompleter {
/*  24 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
/*  28 */     FileCreator messages = this.plugin.getMessages();
/*  29 */     if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
/*  30 */       if (args.length >= 1) {
/*     */         Player target; PlayerData playerData;
/*  32 */         switch (args[0].toLowerCase()) {
/*     */           case "addall":
/*  34 */             if (args.length < 2) {
/*  35 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  36 */               return true;
/*     */             } 
/*  38 */             target = Bukkit.getPlayer(args[1]);
/*  39 */             if (target == null) {
/*  40 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  41 */               return true;
/*     */             } 
/*  43 */             this.plugin.getCosmeticsManager().addAllCosmetics(sender, target);
/*  44 */             return true;
/*     */           
/*     */           case "add":
/*  47 */             if (args.length < 3) {
/*     */               
/*  49 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  50 */               return true;
/*     */             } 
/*  52 */             target = Bukkit.getPlayer(args[1]);
/*  53 */             if (target == null) {
/*  54 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  55 */               return true;
/*     */             } 
/*  57 */             this.plugin.getCosmeticsManager().addCosmetic(sender, target, args[2]);
/*  58 */             return true;
/*     */           
/*     */           case "remove":
/*  61 */             if (args.length < 3) {
/*  62 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  63 */               return true;
/*     */             } 
/*  65 */             target = Bukkit.getPlayer(args[1]);
/*  66 */             if (target == null) {
/*  67 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  68 */               return true;
/*     */             } 
/*  70 */             this.plugin.getCosmeticsManager().removeCosmetic(sender, target, args[2]);
/*  71 */             return true;
/*     */           case "removeall":
/*  73 */             if (args.length < 2) {
/*  74 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  75 */               return true;
/*     */             } 
/*  77 */             target = Bukkit.getPlayer(args[1]);
/*  78 */             if (target == null) {
/*  79 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  80 */               return true;
/*     */             } 
/*  82 */             this.plugin.getCosmeticsManager().removeAllCosmetics(sender, target);
/*  83 */             return true;
/*     */           case "reload":
/*  85 */             this.plugin.getCosmeticsManager().reload(sender);
/*  86 */             return true;
/*     */           case "toggle":
/*  88 */             if (args.length < 2) {
/*  89 */               return true;
/*     */             }
/*  91 */             target = Bukkit.getPlayer(args[1]);
/*  92 */             if (target == null) {
/*  93 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/*  94 */               return true;
/*     */             } 
/*  96 */             playerData = PlayerData.getPlayer((OfflinePlayer)target);
/*  97 */             playerData.toggleHiddeCosmetics();
/*  98 */             return true;
/*     */           
/*     */           case "equip":
/* 101 */             if (args.length < 3) {
/* 102 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 103 */               return true;
/*     */             } 
/* 105 */             target = Bukkit.getPlayer(args[1]);
/* 106 */             if (target == null) {
/* 107 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 108 */               return true;
/*     */             } 
/* 110 */             if (args.length == 4) {
/* 111 */               if (!args[3].startsWith("#")) {
/* 112 */                 this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], null, false);
/*     */               }
/* 114 */               this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], args[3], false);
/* 115 */               return true;
/*     */             } 
/* 117 */             if (args.length == 5) {
/* 118 */               this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], args[3], Boolean.parseBoolean(args[4]));
/* 119 */               return true;
/*     */             } 
/* 121 */             this.plugin.getCosmeticsManager().equipCosmetic(target, args[2], null, false);
/* 122 */             return true;
/*     */           
/*     */           case "unequip":
/* 125 */             if (args.length < 3) {
/* 126 */               return true;
/*     */             }
/* 128 */             target = Bukkit.getPlayer(args[1]);
/* 129 */             if (target == null) {
/* 130 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 131 */               return true;
/*     */             } 
/* 133 */             if (args[2].equalsIgnoreCase("all")) {
/* 134 */               this.plugin.getCosmeticsManager().unEquipAll(sender, target);
/* 135 */               return true;
/*     */             } 
/* 137 */             this.plugin.getCosmeticsManager().unSetCosmetic(target, args[2]);
/* 138 */             return true;
/*     */           
/*     */           case "open":
/* 141 */             if (args.length < 3) {
/* 142 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 143 */               return true;
/*     */             } 
/* 145 */             target = Bukkit.getPlayer(args[2]);
/* 146 */             if (target == null) {
/* 147 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 148 */               return true;
/*     */             } 
/* 150 */             this.plugin.getCosmeticsManager().openMenu(target, args[1]);
/* 151 */             return true;
/*     */           
/*     */           case "token":
/* 154 */             if (args.length < 3) {
/* 155 */               Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 156 */               return true;
/*     */             } 
/* 158 */             if (args[1].equalsIgnoreCase("give")) {
/* 159 */               target = Bukkit.getPlayer(args[2]);
/* 160 */               if (target == null) {
/* 161 */                 Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 162 */                 return true;
/*     */               } 
/* 164 */               this.plugin.getCosmeticsManager().giveToken(sender, target, args[3]);
/* 165 */               return true;
/*     */             } 
/* 167 */             return true;
/*     */         } 
/* 169 */         Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 170 */         return true;
/*     */       } 
/*     */       
/* 173 */       return true;
/*     */     } 
/* 175 */     if (sender instanceof Player) {
/* 176 */       Player player = (Player)sender;
/* 177 */       if (this.plugin.getWorldsBlacklist().contains(player.getWorld().getName())) {
/* 178 */         Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 179 */         return true;
/*     */       } 
/*     */       
/* 182 */       if (args.length >= 1) {
/* 183 */         Player target, p; PlayerData playerData; switch (args[0].toLowerCase()) {
/*     */           case "test":
/* 185 */             target = Bukkit.getPlayer(args[1]);
/* 186 */             if (target == null) {
/* 187 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 188 */               return true;
/*     */             } 
/* 190 */             player.addPassenger((Entity)target);
/* 191 */             return true;
/*     */           case "unlock":
/* 193 */             if (args.length < 2) {
/* 194 */               return true;
/*     */             }
/* 196 */             p = Bukkit.getPlayer(args[1]);
/* 197 */             if (p == null) return true; 
/* 198 */             playerData = PlayerData.getPlayer((OfflinePlayer)p);
/* 199 */             playerData.setZone(false);
/* 200 */             return true;
/*     */           case "addall":
/* 202 */             if (args.length < 2) {
/* 203 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 204 */               return true;
/*     */             } 
/* 206 */             target = Bukkit.getPlayer(args[1]);
/* 207 */             if (target == null) {
/* 208 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 209 */               return true;
/*     */             } 
/* 211 */             this.plugin.getCosmeticsManager().addAllCosmetics((CommandSender)player, target);
/* 212 */             return true;
/*     */           
/*     */           case "add":
/* 215 */             if (args.length < 3) {
/*     */               
/* 217 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 218 */               return true;
/*     */             } 
/* 220 */             target = Bukkit.getPlayer(args[1]);
/* 221 */             if (target == null) {
/* 222 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 223 */               return true;
/*     */             } 
/* 225 */             this.plugin.getCosmeticsManager().addCosmetic((CommandSender)player, target, args[2]);
/* 226 */             return true;
/*     */           
/*     */           case "remove":
/* 229 */             if (args.length < 3) {
/* 230 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 231 */               return true;
/*     */             } 
/* 233 */             target = Bukkit.getPlayer(args[1]);
/* 234 */             if (target == null) {
/* 235 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 236 */               return true;
/*     */             } 
/* 238 */             this.plugin.getCosmeticsManager().removeCosmetic((CommandSender)player, target, args[2]);
/* 239 */             return true;
/*     */           case "removeall":
/* 241 */             if (args.length < 2) {
/* 242 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 243 */               return true;
/*     */             } 
/* 245 */             target = Bukkit.getPlayer(args[1]);
/* 246 */             if (target == null) {
/* 247 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 248 */               return true;
/*     */             } 
/* 250 */             this.plugin.getCosmeticsManager().removeAllCosmetics((CommandSender)player, target);
/* 251 */             return true;
/*     */           case "reload":
/* 253 */             this.plugin.getCosmeticsManager().reload(sender);
/* 254 */             return true;
/*     */           
/*     */           case "use":
/* 257 */             if (args.length < 2) {
/* 258 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 259 */               return true;
/*     */             } 
/* 261 */             if (args.length == 3) {
/* 262 */               this.plugin.getCosmeticsManager().equipCosmetic(player, args[1], args[2], false);
/* 263 */               return true;
/*     */             } 
/* 265 */             this.plugin.getCosmeticsManager().equipCosmetic(player, args[1], null, false);
/* 266 */             return true;
/*     */           case "preview":
/* 268 */             if (args.length < 2) {
/* 269 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 270 */               return true;
/*     */             } 
/* 272 */             this.plugin.getCosmeticsManager().previewCosmetic(player, args[1]);
/* 273 */             return true;
/*     */           case "unuse":
/* 275 */             if (args.length < 2) {
/* 276 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 277 */               return true;
/*     */             } 
/* 279 */             this.plugin.getCosmeticsManager().unUseCosmetic(player, args[1]);
/* 280 */             return true;
/*     */           case "unset":
/* 282 */             if (args.length < 2) {
/* 283 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 284 */               return true;
/*     */             } 
/* 286 */             this.plugin.getCosmeticsManager().unSetCosmetic(player, args[1]);
/* 287 */             return true;
/*     */           case "unequip":
/* 289 */             if (args.length < 2) {
/* 290 */               return true;
/*     */             }
/* 292 */             if (args[1].equalsIgnoreCase("all")) {
/* 293 */               this.plugin.getCosmeticsManager().unEquipAll(player);
/* 294 */               return true;
/*     */             } 
/* 296 */             this.plugin.getCosmeticsManager().unSetCosmetic(player, args[1]);
/* 297 */             return true;
/*     */           
/*     */           case "open":
/* 300 */             if (args.length < 2) {
/* 301 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 302 */               return true;
/*     */             } 
/* 304 */             this.plugin.getCosmeticsManager().openMenu(player, args[1]);
/* 305 */             return true;
/*     */           case "spec":
/* 307 */             this.plugin.getVersion().setSpectator(player);
/* 308 */             return true;
/*     */           case "spawn":
/* 310 */             if (this.plugin.getVersion().getNPC(player) == null) {
/* 311 */               this.plugin.getVersion().createNPC(player);
/* 312 */               return true;
/*     */             } 
/* 314 */             this.plugin.getVersion().removeNPC(player);
/* 315 */             return true;
/*     */           case "hide":
/* 317 */             this.plugin.getCosmeticsManager().hideSelfCosmetic(player, CosmeticType.BAG);
/* 318 */             return true;
/*     */           case "toggle":
/* 320 */             playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 321 */             playerData.toggleHiddeCosmetics();
/* 322 */             return true;
/*     */           
/*     */           case "zones":
/* 325 */             if (args.length < 2) {
/* 326 */               for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 327 */                 Utils.sendMessage((CommandSender)player, msg);
/*     */               }
/* 329 */               return true;
/*     */             } 
/* 331 */             if (args[1].equalsIgnoreCase("add")) {
/* 332 */               if (args.length < 3) {
/* 333 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 334 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 336 */                 return true;
/*     */               } 
/* 338 */               this.plugin.getZonesManager().addZone(player, args[2]);
/* 339 */               return true;
/*     */             } 
/* 341 */             if (args[1].equalsIgnoreCase("remove")) {
/* 342 */               if (args.length < 3) {
/* 343 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 344 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 346 */                 return true;
/*     */               } 
/* 348 */               this.plugin.getZonesManager().removeZone(player, args[2]);
/* 349 */               return true;
/*     */             } 
/* 351 */             if (args[1].equalsIgnoreCase("setnpc")) {
/* 352 */               if (args.length < 3) {
/* 353 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 354 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 356 */                 return true;
/*     */               } 
/* 358 */               this.plugin.getZonesManager().setZoneNPC(player, args[2]);
/* 359 */               return true;
/*     */             } 
/* 361 */             if (args[1].equalsIgnoreCase("setballoon")) {
/* 362 */               if (args.length < 3) {
/* 363 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 364 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 366 */                 return true;
/*     */               } 
/* 368 */               this.plugin.getZonesManager().setBalloonNPC(player, args[2]);
/* 369 */               return true;
/*     */             } 
/* 371 */             if (args[1].equalsIgnoreCase("setspray")) {
/* 372 */               if (args.length < 3) {
/* 373 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 374 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 376 */                 return true;
/*     */               } 
/* 378 */               this.plugin.getZonesManager().setSpray(player, args[2]);
/* 379 */               return true;
/*     */             } 
/* 381 */             if (args[1].equalsIgnoreCase("setenter")) {
/* 382 */               if (args.length < 3) {
/* 383 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 384 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 386 */                 return true;
/*     */               } 
/* 388 */               this.plugin.getZonesManager().setZoneEnter(player, args[2]);
/* 389 */               return true;
/*     */             } 
/* 391 */             if (args[1].equalsIgnoreCase("setexit")) {
/* 392 */               if (args.length < 3) {
/* 393 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 394 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 396 */                 return true;
/*     */               } 
/* 398 */               this.plugin.getZonesManager().setZoneExit(player, args[2]);
/* 399 */               return true;
/*     */             } 
/* 401 */             if (args[1].equalsIgnoreCase("givecorns")) {
/* 402 */               if (args.length < 3) {
/* 403 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 404 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 406 */                 return true;
/*     */               } 
/* 408 */               this.plugin.getZonesManager().giveCorn(player, args[2]);
/* 409 */               return true;
/*     */             } 
/* 411 */             if (args[1].equalsIgnoreCase("enable")) {
/* 412 */               if (args.length < 3) {
/* 413 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 414 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 416 */                 return true;
/*     */               } 
/* 418 */               this.plugin.getZonesManager().enableZone(player, args[2]);
/* 419 */               return true;
/*     */             } 
/* 421 */             if (args[1].equalsIgnoreCase("disable")) {
/* 422 */               if (args.length < 3) {
/* 423 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 424 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 426 */                 return true;
/*     */               } 
/* 428 */               this.plugin.getZonesManager().disableZone(player, args[2]);
/* 429 */               return true;
/*     */             } 
/* 431 */             if (args[1].equalsIgnoreCase("save")) {
/* 432 */               if (args.length < 3) {
/* 433 */                 for (String msg : this.plugin.getMessages().getStringList("commands.zones-usage")) {
/* 434 */                   Utils.sendMessage((CommandSender)player, msg);
/*     */                 }
/* 436 */                 return true;
/*     */               } 
/* 438 */               this.plugin.getZonesManager().saveZone(player, args[2]);
/* 439 */               return true;
/*     */             } 
/* 441 */             return true;
/*     */           
/*     */           case "token":
/* 444 */             if (args.length < 4) {
/* 445 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 446 */               return true;
/*     */             } 
/* 448 */             if (args[1].equalsIgnoreCase("give")) {
/* 449 */               target = Bukkit.getPlayer(args[2]);
/* 450 */               if (target == null) {
/* 451 */                 Utils.sendMessage(sender, this.plugin.prefix + this.plugin.prefix);
/* 452 */                 return true;
/*     */               } 
/* 454 */               this.plugin.getCosmeticsManager().giveToken((CommandSender)player, target, args[3]);
/* 455 */               return true;
/*     */             } 
/* 457 */             return true;
/*     */           case "check":
/* 459 */             this.plugin.getCosmeticsManager().sendCheck(player);
/* 460 */             return true;
/*     */           case "npc":
/* 462 */             if (!this.plugin.isCitizens()) {
/* 463 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + "&cCitizens is not installed!");
/* 464 */               return true;
/*     */             } 
/*     */             
/* 467 */             if (args.length == 2 && args[1].equalsIgnoreCase("save")) {
/* 468 */               this.plugin.getNPCsLoader().save();
/* 469 */               return true;
/*     */             } 
/* 471 */             if (args.length < 3) {
/* 472 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 473 */               return true;
/*     */             } 
/*     */             try {
/* 476 */               this.plugin.getCitizens().equipCosmetic((CommandSender)player, args[1], args[2], args[3]);
/* 477 */             } catch (ArrayIndexOutOfBoundsException exception) {
/* 478 */               this.plugin.getCitizens().equipCosmetic((CommandSender)player, args[1], args[2], null);
/*     */             } 
/* 480 */             return true;
/*     */           
/*     */           case "tint":
/* 483 */             if (args.length < 2) {
/* 484 */               Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 485 */               return true;
/*     */             } 
/* 487 */             this.plugin.getCosmeticsManager().tintItem(player, args[1]);
/* 488 */             return true;
/*     */         } 
/* 490 */         Utils.sendMessage((CommandSender)player, this.plugin.prefix + this.plugin.prefix);
/* 491 */         return true;
/*     */       } 
/*     */       
/* 494 */       if (player.hasPermission("magicosmetics.cosmetics.use")) {
/* 495 */         this.plugin.getCosmeticsManager().openMenu(player, this.plugin.getMainMenu());
/* 496 */         if (this.plugin.getOnExecuteCosmetics().isEmpty()) return true; 
/* 497 */         player.performCommand(this.plugin.getOnExecuteCosmetics());
/*     */       } 
/* 499 */       return true;
/*     */     } 
/* 501 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
/* 506 */     List<String> arguments = new ArrayList<>();
/* 507 */     if (sender.hasPermission("magicosmetics.cosmetics")) {
/* 508 */       arguments.add("add");
/* 509 */       arguments.add("remove");
/* 510 */       arguments.add("addAll");
/* 511 */       if (this.plugin.isCitizens()) {
/* 512 */         arguments.add("npc");
/*     */       }
/*     */     } 
/* 515 */     if (sender.hasPermission("magicosmetics.menus")) {
/* 516 */       arguments.add("open");
/*     */     }
/* 518 */     if (sender.hasPermission("magicosmetics.zones")) {
/* 519 */       arguments.add("zones");
/*     */     }
/* 521 */     if (sender.hasPermission("magicosmetics.tokens")) {
/* 522 */       arguments.add("token");
/*     */     }
/* 524 */     if (sender.hasPermission("magicosmetics.reload")) {
/* 525 */       arguments.add("reload");
/*     */     }
/* 527 */     if (sender.hasPermission("magicosmetics.hide")) {
/* 528 */       arguments.add("hide");
/*     */     }
/* 530 */     if (sender.hasPermission("magicosmetics.toggle")) {
/* 531 */       arguments.add("toggle");
/*     */     }
/* 533 */     if (sender.hasPermission("magicosmetics.equip")) {
/* 534 */       arguments.add("use");
/* 535 */       arguments.add("unequip");
/*     */     } 
/* 537 */     if (sender.hasPermission("magicosmetics.tint")) {
/* 538 */       arguments.add("tint");
/*     */     }
/* 540 */     if (arguments.size() == 0) return arguments; 
/* 541 */     List<String> result = new ArrayList<>();
/* 542 */     switch (args.length) {
/*     */       case 1:
/* 544 */         for (String a : arguments) {
/* 545 */           if (a.toLowerCase().startsWith(args[0].toLowerCase()))
/* 546 */             result.add(a); 
/*     */         } 
/* 548 */         return result;
/*     */       case 2:
/* 550 */         switch (args[0].toLowerCase()) {
/*     */           case "hide":
/*     */           case "toggle":
/*     */           case "add":
/*     */           case "addall":
/*     */           case "remove":
/* 556 */             return null;
/*     */           case "npc":
/* 558 */             if (!this.plugin.isCitizens()) return null; 
/* 559 */             result.add("save");
/* 560 */             result.addAll(this.plugin.getCitizens().getNPCs());
/* 561 */             return result;
/*     */           case "unequip":
/*     */           case "use":
/* 564 */             if (!sender.hasPermission("magicosmetics.equip")) return null; 
/* 565 */             result.add("all");
/* 566 */             result.addAll(Cosmetic.cosmetics.keySet());
/* 567 */             return result;
/*     */           case "open":
/* 569 */             if (!sender.hasPermission("magicosmetics.menus")) return null; 
/* 570 */             result.addAll(Menu.inventories.keySet());
/* 571 */             return result;
/*     */           case "zones":
/* 573 */             if (!sender.hasPermission("magicosmetics.zones")) return null; 
/* 574 */             result.add("add");
/* 575 */             result.add("remove");
/* 576 */             result.add("setNPC");
/* 577 */             result.add("setBalloon");
/* 578 */             result.add("setSpray");
/* 579 */             result.add("setEnter");
/* 580 */             result.add("setExit");
/* 581 */             result.add("giveCorns");
/* 582 */             result.add("enable");
/* 583 */             result.add("disable");
/* 584 */             result.add("save");
/* 585 */             return result;
/*     */           case "token":
/* 587 */             if (!sender.hasPermission("magicosmetics.tokens")) return null; 
/* 588 */             result.add("give");
/* 589 */             return result;
/*     */           case "tint":
/* 591 */             if (!sender.hasPermission("magicosmetics.tint")) return null; 
/* 592 */             result.add("#FFFFFF");
/* 593 */             return result;
/*     */         } 
/*     */       case 3:
/* 596 */         switch (args[0].toLowerCase()) {
/*     */           case "add":
/*     */           case "remove":
/*     */           case "npc":
/* 600 */             if (!sender.hasPermission("magicosmetics.cosmetics")) return null; 
/* 601 */             result.addAll(Cosmetic.cosmetics.keySet());
/* 602 */             return result;
/*     */           case "use":
/*     */           case "equip":
/* 605 */             if (!sender.hasPermission("magicosmetics.equip")) return null; 
/* 606 */             result.add("#FFFFFF");
/* 607 */             result.add("null");
/* 608 */             return result;
/*     */           case "zones":
/* 610 */             if (!sender.hasPermission("magicosmetics.zones")) return null; 
/* 611 */             if (args[1].equalsIgnoreCase("add")) return new ArrayList<>(); 
/* 612 */             result.addAll(Zone.zones.keySet());
/* 613 */             return result;
/*     */           case "token":
/* 615 */             return null;
/*     */         } 
/*     */       case 4:
/* 618 */         if (args[0].equalsIgnoreCase("token") && args[1].equalsIgnoreCase("give")) {
/* 619 */           if (!sender.hasPermission("magicosmetics.tokens")) return null; 
/* 620 */           result.addAll(Token.tokens.keySet());
/* 621 */           return result;
/*     */         } 
/* 623 */         if (args[0].equalsIgnoreCase("npc")) {
/* 624 */           if (!this.plugin.isCitizens()) return null; 
/* 625 */           if (!sender.hasPermission("magicosmetics.cosmetics")) return null; 
/* 626 */           result.add("#FFFFFF");
/* 627 */           return result;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 632 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\commands\Command.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */