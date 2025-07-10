/*     */ package com.francobm.magicosmetics.cache;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.utils.Cuboid;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Zone
/*     */ {
/*  25 */   public static Map<String, Zone> zones = new HashMap<>();
/*     */   private final String id;
/*     */   private final String name;
/*     */   private Cuboid cuboid;
/*     */   private Location corn1;
/*     */   private Location corn2;
/*     */   private Location npc;
/*     */   private Location enter;
/*     */   private Location exit;
/*     */   private Location balloon;
/*     */   private Location sprayLoc;
/*     */   private BlockFace sprayFace;
/*     */   private boolean active;
/*     */   private int rotation;
/*     */   
/*     */   public Zone(String id, String name) {
/*  41 */     this.id = id;
/*  42 */     this.name = name;
/*  43 */     this.cuboid = null;
/*  44 */     this.corn1 = null;
/*  45 */     this.corn2 = null;
/*  46 */     this.npc = null;
/*  47 */     this.enter = null;
/*  48 */     this.exit = null;
/*  49 */     this.balloon = null;
/*  50 */     this.sprayLoc = null;
/*  51 */     this.sprayFace = null;
/*  52 */     this.active = false;
/*     */   }
/*     */   
/*     */   public Zone(String name) {
/*  56 */     this.id = String.valueOf(zones.size() + 1);
/*  57 */     this.name = name;
/*  58 */     this.cuboid = null;
/*  59 */     this.corn1 = null;
/*  60 */     this.corn2 = null;
/*  61 */     this.npc = null;
/*  62 */     this.enter = null;
/*  63 */     this.exit = null;
/*  64 */     this.balloon = null;
/*  65 */     this.sprayLoc = null;
/*  66 */     this.sprayFace = null;
/*  67 */     this.active = false;
/*     */   }
/*     */   
/*     */   public Zone(String id, String name, Location corn1, Location corn2, Location npc, Location enter, Location exit, Location balloon, Location sprayLoc, BlockFace sprayFace) {
/*  71 */     this.id = id;
/*  72 */     this.name = name;
/*  73 */     this.cuboid = new Cuboid(corn1, corn2);
/*  74 */     this.corn1 = corn1;
/*  75 */     this.corn2 = corn2;
/*  76 */     this.npc = npc;
/*  77 */     this.enter = enter;
/*  78 */     this.exit = exit;
/*  79 */     this.balloon = balloon;
/*  80 */     this.sprayLoc = sprayLoc;
/*  81 */     this.sprayFace = sprayFace;
/*  82 */     this.active = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Zone getZone(String id) {
/*  87 */     return zones.get(id);
/*     */   }
/*     */   
/*     */   public static Zone addZone(String name) {
/*  91 */     return zones.put(name, new Zone(String.valueOf(zones.size() + 1), name));
/*     */   }
/*     */   
/*     */   public static void loadZones() {
/*  95 */     zones.clear();
/*  96 */     FileCreator zone = MagicCosmetics.getInstance().getZones();
/*  97 */     if (!zone.contains("zones"))
/*  98 */       return;  for (String key : zone.getConfigurationSection("zones").getKeys(false)) {
/*  99 */       String name = "";
/* 100 */       Location corn1 = null;
/* 101 */       Location corn2 = null;
/* 102 */       Location npc = null;
/* 103 */       Location enter = null;
/* 104 */       Location exit = null;
/* 105 */       Location balloon = null;
/* 106 */       Location sprayLoc = null;
/* 107 */       BlockFace sprayFace = null;
/* 108 */       boolean active = false;
/* 109 */       if (zone.contains("zones." + key + ".name")) {
/* 110 */         name = zone.getString("zones." + key + ".name");
/*     */       }
/* 112 */       if (zone.contains("zones." + key + ".corn1")) {
/* 113 */         String c1 = zone.getString("zones." + key + ".corn1");
/* 114 */         if (c1.equalsIgnoreCase("Location is Null!!")) {
/* 115 */           MagicCosmetics.getInstance().getLogger().info("Location of Corn1 is Null... skipping zone " + key);
/*     */           continue;
/*     */         } 
/* 118 */         corn1 = Utils.convertStringToLocation(c1);
/*     */       } 
/*     */       
/* 121 */       if (zone.contains("zones." + key + ".corn2")) {
/* 122 */         String c2 = zone.getString("zones." + key + ".corn2");
/* 123 */         if (c2.equalsIgnoreCase("Location is Null!!")) {
/* 124 */           MagicCosmetics.getInstance().getLogger().info("Location of Corn2 is Null... skipping zone " + key);
/*     */           continue;
/*     */         } 
/* 127 */         corn2 = Utils.convertStringToLocation(c2);
/*     */       } 
/*     */       
/* 130 */       if (zone.contains("zones." + key + ".npc")) {
/* 131 */         String npcString = zone.getString("zones." + key + ".npc");
/* 132 */         if (npcString.equalsIgnoreCase("Location is Null!!")) {
/* 133 */           MagicCosmetics.getInstance().getLogger().info("Location of NPC is Null!");
/*     */         } else {
/* 135 */           npc = Utils.convertStringToLocation(npcString);
/*     */         } 
/*     */       } 
/* 138 */       if (zone.contains("zones." + key + ".enter")) {
/* 139 */         String enterString = zone.getString("zones." + key + ".enter");
/* 140 */         if (enterString.equalsIgnoreCase("Location is Null!!")) {
/* 141 */           MagicCosmetics.getInstance().getLogger().info("Location of Enter is Null!");
/*     */         } else {
/* 143 */           enter = Utils.convertStringToLocation(enterString);
/*     */         } 
/*     */       } 
/* 146 */       if (zone.contains("zones." + key + ".exit")) {
/* 147 */         String exitString = zone.getString("zones." + key + ".exit");
/* 148 */         if (exitString.equalsIgnoreCase("Location is Null!!")) {
/* 149 */           MagicCosmetics.getInstance().getLogger().info("Location of Exit is Null!");
/*     */         } else {
/* 151 */           exit = Utils.convertStringToLocation(exitString);
/*     */         } 
/*     */       } 
/* 154 */       if (zone.contains("zones." + key + ".balloon")) {
/* 155 */         String balloonString = zone.getString("zones." + key + ".balloon");
/* 156 */         if (balloonString.equalsIgnoreCase("Location is Null!!")) {
/* 157 */           MagicCosmetics.getInstance().getLogger().info("Location of Balloon is Null!");
/*     */         } else {
/* 159 */           balloon = Utils.convertStringToLocation(balloonString);
/*     */         } 
/*     */       } 
/* 162 */       if (zone.contains("zones." + key + ".spray")) {
/* 163 */         String sprayLocString = zone.getString("zones." + key + ".spray.loc");
/* 164 */         String sprayFaceString = zone.getString("zones." + key + ".spray.face");
/* 165 */         if (sprayLocString.equalsIgnoreCase("Location is Null!!")) {
/* 166 */           MagicCosmetics.getInstance().getLogger().info("Location of Spray is Null!");
/*     */         } else {
/* 168 */           sprayLoc = Utils.convertStringToLocation(sprayLocString);
/*     */           try {
/* 170 */             sprayFace = BlockFace.valueOf(sprayFaceString.toUpperCase());
/* 171 */           } catch (IllegalArgumentException e) {
/* 172 */             MagicCosmetics.getInstance().getLogger().info("Face of Spray is Null!");
/*     */           } 
/*     */         } 
/*     */       } 
/* 176 */       if (zone.contains("zones." + key + ".enabled")) {
/* 177 */         active = zone.getBoolean("zones." + key + ".enabled");
/*     */       }
/* 179 */       Zone z = new Zone(key, name, corn1, corn2, npc, enter, exit, balloon, sprayLoc, sprayFace);
/* 180 */       z.setActive(active);
/* 181 */       zones.put(name, z);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void removeZone(String id) {
/* 186 */     Zone zone = getZone(id);
/* 187 */     if (zone == null)
/* 188 */       return;  zone.setCuboid(null);
/* 189 */     zones.remove(zone.getName());
/* 190 */     FileCreator zoneConf = MagicCosmetics.getInstance().getZones();
/* 191 */     zoneConf.set("zones", null);
/* 192 */     for (Zone z : zones.values()) {
/* 193 */       String zId = z.getId();
/* 194 */       String name = z.getName();
/* 195 */       String corn1 = Utils.convertLocationToString(z.getCorn1(), true);
/* 196 */       String corn2 = Utils.convertLocationToString(z.getCorn2(), true);
/* 197 */       String npc = Utils.convertLocationToString(z.getNpc(), false);
/* 198 */       String enter = Utils.convertLocationToString(z.getEnter(), false);
/* 199 */       String exit = Utils.convertLocationToString(z.getExit(), false);
/* 200 */       String balloon = Utils.convertLocationToString(z.getBalloon(), false);
/* 201 */       String sprayLoc = Utils.convertLocationToString(z.getSprayLoc(), true);
/* 202 */       String sprayFace = z.getSprayFace().name();
/* 203 */       boolean enabled = z.isActive();
/* 204 */       zoneConf.set("zones." + zId + ".name", name);
/* 205 */       zoneConf.set("zones." + zId + ".corn1", corn1);
/* 206 */       zoneConf.set("zones." + zId + ".corn2", corn2);
/* 207 */       zoneConf.set("zones." + zId + ".npc", npc);
/* 208 */       zoneConf.set("zones." + zId + ".enter", enter);
/* 209 */       zoneConf.set("zones." + zId + ".exit", exit);
/* 210 */       zoneConf.set("zones." + zId + ".balloon", balloon);
/* 211 */       zoneConf.set("zones." + zId + ".spray.loc", sprayLoc);
/* 212 */       zoneConf.set("zones." + zId + ".spray.face", sprayFace);
/* 213 */       zoneConf.set("zones." + zId + ".enabled", Boolean.valueOf(enabled));
/*     */     } 
/* 215 */     zoneConf.save();
/*     */   }
/*     */   
/*     */   public static void saveZone(String id) {
/* 219 */     FileCreator zone = MagicCosmetics.getInstance().getZones();
/* 220 */     Zone z = getZone(id);
/* 221 */     if (z == null)
/* 222 */       return;  String name = z.getName();
/* 223 */     String corn1 = Utils.convertLocationToString(z.getCorn1(), true);
/* 224 */     String corn2 = Utils.convertLocationToString(z.getCorn2(), true);
/* 225 */     String npc = Utils.convertLocationToString(z.getNpc(), false);
/* 226 */     String enter = Utils.convertLocationToString(z.getEnter(), false);
/* 227 */     String exit = Utils.convertLocationToString(z.getExit(), false);
/* 228 */     String balloon = Utils.convertLocationToString(z.getBalloon(), false);
/* 229 */     String sprayLoc = Utils.convertLocationToString(z.getSprayLoc(), true);
/* 230 */     String sprayFace = z.getSprayFace().name();
/* 231 */     boolean enabled = z.isActive();
/* 232 */     zone.set("zones." + id + ".name", name);
/* 233 */     zone.set("zones." + id + ".corn1", corn1);
/* 234 */     zone.set("zones." + id + ".corn2", corn2);
/* 235 */     zone.set("zones." + id + ".npc", npc);
/* 236 */     zone.set("zones." + id + ".enter", enter);
/* 237 */     zone.set("zones." + id + ".exit", exit);
/* 238 */     zone.set("zones." + id + ".balloon", balloon);
/* 239 */     zone.set("zones." + id + ".spray.loc", sprayLoc);
/* 240 */     zone.set("zones." + id + ".spray.face", sprayFace);
/* 241 */     zone.set("zones." + id + ".enabled", Boolean.valueOf(enabled));
/* 242 */     zone.save();
/* 243 */     if (z.getCuboid() == null && 
/* 244 */       z.getCorn1() != null && z.getCorn2() != null) {
/* 245 */       z.setCuboid(new Cuboid(z.getCorn1(), z.getCorn2()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveZones() {
/* 251 */     FileCreator zone = MagicCosmetics.getInstance().getZones();
/* 252 */     zone.set("zones", null);
/* 253 */     for (Zone z : zones.values()) {
/* 254 */       String id = z.getId();
/* 255 */       String name = z.getName();
/* 256 */       String corn1 = Utils.convertLocationToString(z.getCorn1(), true);
/* 257 */       String corn2 = Utils.convertLocationToString(z.getCorn2(), true);
/* 258 */       String npc = Utils.convertLocationToString(z.getNpc(), false);
/* 259 */       String enter = Utils.convertLocationToString(z.getEnter(), false);
/* 260 */       String exit = Utils.convertLocationToString(z.getExit(), false);
/* 261 */       String balloon = Utils.convertLocationToString(z.getBalloon(), false);
/* 262 */       boolean enabled = z.isActive();
/* 263 */       zone.set("zones." + id + ".name", name);
/* 264 */       zone.set("zones." + id + ".corn1", corn1);
/* 265 */       zone.set("zones." + id + ".corn2", corn2);
/* 266 */       zone.set("zones." + id + ".npc", npc);
/* 267 */       zone.set("zones." + id + ".enter", enter);
/* 268 */       zone.set("zones." + id + ".exit", exit);
/* 269 */       zone.set("zones." + id + ".balloon", balloon);
/* 270 */       zone.set("zones." + id + ".enabled", Boolean.valueOf(enabled));
/*     */     } 
/* 272 */     zone.save();
/*     */   }
/*     */   
/*     */   public void giveCorns(Player player) {
/* 276 */     player.getInventory().addItem(new ItemStack[] { getCorn() });
/* 277 */     player.updateInventory();
/*     */   }
/*     */   
/*     */   public ItemStack getCorn() {
/* 281 */     ItemStack itemStack = XMaterial.BLAZE_ROD.parseItem();
/* 282 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 283 */     itemMeta.setDisplayName("§bSet the points of the area " + getName());
/* 284 */     List<String> lore = new ArrayList<>();
/* 285 */     lore.add("§eLeft click to set the first position");
/* 286 */     lore.add("§eRight click to set second position");
/* 287 */     itemMeta.setLore(lore);
/* 288 */     itemStack.setItemMeta(itemMeta);
/* 289 */     return MagicCosmetics.getInstance().getVersion().setNBTCosmetic(itemStack, "wand" + getName());
/*     */   }
/*     */   
/*     */   public boolean isInZone(Block block) {
/* 293 */     if (!this.active) {
/* 294 */       return false;
/*     */     }
/* 296 */     return detectBlock(block, this.cuboid);
/*     */   }
/*     */   
/*     */   public Location getBalloon() {
/* 300 */     return this.balloon;
/*     */   }
/*     */   
/*     */   public void setBalloon(Location balloon) {
/* 304 */     this.balloon = balloon;
/*     */   }
/*     */   
/*     */   private boolean detectBlock(Block blockLocation, Cuboid cuboid) {
/* 308 */     return cuboid.contains(blockLocation);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 312 */     return this.name;
/*     */   }
/*     */   
/*     */   public Location getNpc() {
/* 316 */     return this.npc;
/*     */   }
/*     */   
/*     */   public Location getExit() {
/* 320 */     return this.exit;
/*     */   }
/*     */   
/*     */   public Location getEnter() {
/* 324 */     return this.enter;
/*     */   }
/*     */   
/*     */   public void setEnter(Location enter) {
/* 328 */     this.enter = enter;
/*     */   }
/*     */   
/*     */   public void setNpc(Location npc) {
/* 332 */     this.npc = npc;
/*     */   }
/*     */   
/*     */   public void setExit(Location exit) {
/* 336 */     this.exit = exit;
/*     */   }
/*     */   
/*     */   public Location getCorn1() {
/* 340 */     return this.corn1;
/*     */   }
/*     */   
/*     */   public Location getCorn2() {
/* 344 */     return this.corn2;
/*     */   }
/*     */   
/*     */   public void setCorn1(Location corn1) {
/* 348 */     this.corn1 = corn1;
/*     */   }
/*     */   
/*     */   public void setCorn2(Location corn2) {
/* 352 */     this.corn2 = corn2;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 356 */     return this.id;
/*     */   }
/*     */   
/*     */   public Cuboid getCuboid() {
/* 360 */     return this.cuboid;
/*     */   }
/*     */   
/*     */   public void setCuboid(Cuboid cuboid) {
/* 364 */     this.cuboid = cuboid;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 368 */     return this.active;
/*     */   }
/*     */   
/*     */   public void setActive(boolean active) {
/* 372 */     this.active = active;
/* 373 */     if (getCuboid() == null && 
/* 374 */       getCorn1() != null && getCorn2() != null) {
/* 375 */       setCuboid(new Cuboid(getCorn1(), getCorn2()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSprayFace(BlockFace sprayFace) {
/* 381 */     this.sprayFace = sprayFace;
/*     */   }
/*     */   
/*     */   public BlockFace getSprayFace() {
/* 385 */     return this.sprayFace;
/*     */   }
/*     */   
/*     */   public void setSprayLoc(Location sprayLoc) {
/* 389 */     this.sprayLoc = sprayLoc;
/*     */   }
/*     */   
/*     */   public Location getSprayLoc() {
/* 393 */     return this.sprayLoc;
/*     */   }
/*     */   
/*     */   public void setRotation(int rotation) {
/* 397 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public int getRotation() {
/* 401 */     return this.rotation;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\Zone.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */