/*     */ package com.francobm.magicosmetics.utils;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Spray;
/*     */ import com.francobm.magicosmetics.cache.renderer.ImageRenderer;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Base64;
/*     */ import java.util.Random;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.md_5.bungee.api.ChatColor;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.map.MapPalette;
/*     */ import org.bukkit.map.MapRenderer;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*  40 */   private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
/*     */   
/*  42 */   private static final Random random = new Random();
/*     */   
/*     */   public static boolean isNewerThan1206() {
/*  45 */     return (getVersion().contains("1.20.6") || getVersion().contains("1.21"));
/*     */   }
/*     */   
/*     */   public static String getVersion() {
/*  49 */     return Bukkit.getServer().getBukkitVersion().split("-")[0];
/*     */   }
/*     */   
/*     */   public static boolean existPluginClass(String className) {
/*     */     try {
/*  54 */       Class.forName(className);
/*  55 */     } catch (ClassNotFoundException e) {
/*  56 */       return false;
/*     */     } 
/*  58 */     return true;
/*     */   }
/*     */   public static String getTime(int time) {
/*     */     String secondsMsg, minutesMsg, hoursMsg;
/*  62 */     int hours = time / 3600;
/*  63 */     int i = time - hours * 3600;
/*  64 */     int minutes = i / 60;
/*  65 */     int seconds = i - minutes * 60;
/*     */ 
/*     */ 
/*     */     
/*  69 */     if (seconds < 10) {
/*  70 */       if (seconds == 1) {
/*  71 */         secondsMsg = "0" + seconds + " second";
/*     */       } else {
/*  73 */         secondsMsg = "0" + seconds + " seconds";
/*     */       } 
/*     */     } else {
/*  76 */       secondsMsg = "" + seconds + " seconds";
/*     */     } 
/*  78 */     if (minutes < 10) {
/*  79 */       if (minutes == 1) {
/*  80 */         minutesMsg = "0" + minutes + " minute";
/*     */       } else {
/*  82 */         minutesMsg = "0" + minutes + " minutes";
/*     */       } 
/*     */     } else {
/*  85 */       minutesMsg = "" + minutes + " minutes";
/*     */     } 
/*  87 */     if (hours < 10) {
/*  88 */       if (hours == 1) {
/*  89 */         hoursMsg = "0" + hours + " hour";
/*     */       } else {
/*  91 */         hoursMsg = "0" + hours + " hours";
/*     */       } 
/*     */     } else {
/*  94 */       hoursMsg = "" + hours + " hours";
/*     */     } 
/*     */     
/*  97 */     if (hours != 0)
/*     */     {
/*  99 */       return hoursMsg + " " + hoursMsg + " " + minutesMsg; } 
/* 100 */     if (minutes != 0) {
/* 101 */       return minutesMsg + " " + minutesMsg;
/*     */     }
/* 103 */     return secondsMsg;
/*     */   }
/*     */   
/*     */   public static void sendSound(Player player, Sound sound) {
/* 107 */     if (player == null)
/* 108 */       return;  if (sound == null)
/*     */       return; 
/* 110 */     if (sound.isCustom()) {
/* 111 */       player.playSound(player.getLocation(), sound.getSoundCustom(), sound.getYaw(), sound.getPitch());
/*     */       return;
/*     */     } 
/* 114 */     player.playSound(player.getLocation(), sound.getSoundBukkit(), sound.getYaw(), sound.getPitch());
/*     */   }
/*     */   
/*     */   public static void sendAllSound(Location location, Sound sound) {
/* 118 */     if (location.getWorld() == null)
/* 119 */       return;  if (sound == null)
/*     */       return; 
/* 121 */     if (sound.isCustom()) {
/* 122 */       location.getWorld().playSound(location, sound.getSoundCustom(), sound.getYaw(), sound.getPitch());
/*     */       return;
/*     */     } 
/* 125 */     location.getWorld().playSound(location, sound.getSoundBukkit(), sound.getYaw(), sound.getPitch());
/*     */   }
/*     */   
/*     */   public static ItemStack getMapImage(Player player, BufferedImage image, Spray spray) {
/* 129 */     Color color = spray.getColor();
/* 130 */     if (!spray.isPaint() && 
/* 131 */       color != null) {
/* 132 */       Graphics2D g = image.createGraphics();
/* 133 */       g.setPaint(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
/* 134 */       g.fillRect(0, 0, image.getWidth(), image.getHeight());
/* 135 */       spray.setPaint(true);
/*     */     } 
/*     */     
/* 138 */     MapView mapView = Bukkit.createMap(player.getWorld());
/* 139 */     mapView.getRenderers().clear();
/* 140 */     ImageRenderer imageRenderer = new ImageRenderer();
/* 141 */     if (!imageRenderer.load(image)) return null; 
/* 142 */     mapView.addRenderer((MapRenderer)imageRenderer);
/* 143 */     ItemStack map = XMaterial.FILLED_MAP.parseItem();
/* 144 */     if (map == null) return null; 
/* 145 */     MapMeta meta = (MapMeta)map.getItemMeta();
/* 146 */     if (meta == null) return null; 
/* 147 */     meta.setMapView(mapView);
/* 148 */     map.setItemMeta((ItemMeta)meta);
/* 149 */     return map;
/*     */   }
/*     */   
/*     */   public static BufferedImage deepCopy(BufferedImage bi) {
/* 153 */     ColorModel cm = bi.getColorModel();
/* 154 */     boolean isAlphaPreMultiplied = cm.isAlphaPremultiplied();
/* 155 */     WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
/* 156 */     return new BufferedImage(cm, raster, isAlphaPreMultiplied, null);
/*     */   }
/*     */   
/*     */   public static BufferedImage getImage(String url) {
/*     */     BufferedImage image;
/*     */     try {
/* 162 */       if (url.startsWith("http")) {
/* 163 */         image = ImageIO.read(new URL(url));
/*     */       } else {
/* 165 */         File file = new File(MagicCosmetics.getInstance().getDataFolder(), "sprays/" + url);
/* 166 */         if (!file.exists()) {
/* 167 */           return null;
/*     */         }
/* 169 */         image = ImageIO.read(file);
/*     */       } 
/* 171 */       image = MapPalette.resizeImage(image);
/* 172 */     } catch (IOException e) {
/* 173 */       return null;
/*     */     } 
/* 175 */     return image;
/*     */   }
/*     */   
/*     */   public static boolean isDyeable(ItemStack itemStack) {
/* 179 */     if (itemStack == null) return false; 
/* 180 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 181 */     return (itemMeta instanceof org.bukkit.inventory.meta.LeatherArmorMeta || itemMeta instanceof org.bukkit.inventory.meta.PotionMeta || itemMeta instanceof MapMeta || itemMeta instanceof org.bukkit.inventory.meta.FireworkEffectMeta);
/*     */   }
/*     */   
/*     */   public static void hidePlayer(Player player) {
/* 185 */     for (Player players : Bukkit.getOnlinePlayers()) {
/* 186 */       players.hidePlayer((Plugin)MagicCosmetics.getInstance(), player);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void showPlayer(Player player) {
/* 191 */     for (Player players : Bukkit.getOnlinePlayers()) {
/* 192 */       players.showPlayer((Plugin)MagicCosmetics.getInstance(), player);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Location convertStringToLocation(String string) {
/* 197 */     String[] strings = string.split(",");
/* 198 */     String world = strings[0];
/* 199 */     double x = Double.parseDouble(strings[1]);
/* 200 */     double y = Double.parseDouble(strings[2]);
/* 201 */     double z = Double.parseDouble(strings[3]);
/* 202 */     if (strings.length > 4) {
/* 203 */       float yaw = Float.parseFloat(strings[4]);
/* 204 */       float pitch = Float.parseFloat(strings[5]);
/* 205 */       return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
/*     */     } 
/* 207 */     return new Location(Bukkit.getWorld(world), x, y, z);
/*     */   }
/*     */   
/*     */   public static String convertLocationToString(Location location, boolean isBlock) {
/* 211 */     if (location != null) {
/* 212 */       if (isBlock) {
/* 213 */         return location.getWorld().getName() + "," + location.getWorld().getName() + "," + location.getX() + "," + location.getY();
/*     */       }
/* 215 */       return location.getWorld().getName() + "," + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw();
/*     */     } 
/* 217 */     return "Location is Null!!";
/*     */   }
/*     */   
/*     */   public static String ChatColor(String message) {
/* 221 */     String version = getVersion();
/* 222 */     if (version.contains("1.16") || version.contains("1.17") || version.contains("1.18") || version.contains("1.19") || version.contains("1.20") || version.contains("1.21")) {
/* 223 */       Matcher matcher = pattern.matcher(message);
/* 224 */       while (matcher.find()) {
/* 225 */         String color = message.substring(matcher.start(), matcher.end());
/* 226 */         message = message.replace(color, String.valueOf(ChatColor.of(color)));
/* 227 */         matcher = pattern.matcher(message);
/*     */       } 
/*     */     } 
/* 230 */     return ChatColor.translateAlternateColorCodes('&', message);
/*     */   }
/*     */   
/*     */   public static int getRotation(float yaw, boolean allowDiagonals) {
/* 234 */     if (allowDiagonals) return MathUtils.floor((Location.normalizeYaw(yaw) + 180.0F) * 8.0F / 360.0F + 0.5F) % 8; 
/* 235 */     return MathUtils.floor((Location.normalizeYaw(yaw) + 180.0F) * 4.0F / 360.0F + 0.5F) % 4;
/*     */   }
/*     */   
/*     */   public static String bsc(String string) {
/* 239 */     return new String(Base64.getDecoder().decode(string));
/*     */   }
/*     */   
/*     */   public static Color hex2Rgb(String colorStr) {
/* 243 */     return Color.fromRGB(
/* 244 */         Integer.valueOf(colorStr.substring(1, 3), 16).intValue(), 
/* 245 */         Integer.valueOf(colorStr.substring(3, 5), 16).intValue(), 
/* 246 */         Integer.valueOf(colorStr.substring(5, 7), 16).intValue());
/*     */   }
/*     */   
/*     */   public static YamlConfiguration getPaperConfig(Server server) {
/*     */     try {
/* 251 */       return (YamlConfiguration)Server.Spigot.class.getMethod("getPaperConfig", new Class[0]).invoke(server.spigot(), new Object[0]);
/* 252 */     } catch (Exception exception) {
/* 253 */       exception.printStackTrace();
/* 254 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPaper() {
/*     */     try {
/* 260 */       Class.forName("com.destroystokyo.paper.PaperConfig");
/* 261 */       return true;
/* 262 */     } catch (ClassNotFoundException classNotFoundException) {
/*     */       try {
/* 264 */         Class.forName("io.papermc.paper.configuration.Configuration");
/* 265 */         return true;
/* 266 */       } catch (ClassNotFoundException classNotFoundException1) {
/* 267 */         return false;
/*     */       } 
/*     */     } 
/*     */   } private static boolean isMajorTo181(String version) {
/* 271 */     String[] partes = version.split("\\.");
/* 272 */     int major = Integer.parseInt(partes[1]);
/* 273 */     int minor = Integer.parseInt(partes[2]);
/* 274 */     int patch = Integer.parseInt(partes[3]);
/* 275 */     return (major > 1 || (major == 1 && (minor > 18 || (minor == 18 && patch > 1))));
/*     */   }
/*     */   
/*     */   public static void sendMessage(CommandSender sender, String string) {
/* 279 */     if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
/* 280 */       MagicCosmetics.getInstance().getLogger().info(string);
/*     */       return;
/*     */     } 
/* 283 */     if (sender instanceof Player) {
/* 284 */       Player player = (Player)sender;
/* 285 */       player.sendMessage(string);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Vector getItemDropVelocity(Player player) {
/* 290 */     float pitch = player.getLocation().getPitch();
/* 291 */     float yaw = player.getLocation().getYaw();
/*     */     
/* 293 */     float f1 = (float)Math.sin(Math.toRadians(pitch));
/* 294 */     float f2 = (float)Math.cos(Math.toRadians(pitch));
/* 295 */     float f3 = (float)Math.sin(Math.toRadians(yaw));
/* 296 */     float f4 = (float)Math.cos(Math.toRadians(yaw));
/* 297 */     float f5 = random.nextFloat() * 3.1415927F * 2.0F;
/* 298 */     float f6 = 0.02F * random.nextFloat();
/*     */     
/* 300 */     return new Vector((-f3 * f2 * 0.3F) + 
/* 301 */         Math.cos(f5) * f6, (-f1 * 0.3F + 0.1F + (random
/* 302 */         .nextFloat() - random.nextFloat()) * 0.1F), (f4 * f2 * 0.3F) + 
/* 303 */         Math.sin(f5) * f6);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\Utils.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */