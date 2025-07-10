/*     */ package com.francobm.magicosmetics.files;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.SecondaryColor;
/*     */ import com.francobm.magicosmetics.utils.OffsetModel;
/*     */ import com.francobm.magicosmetics.utils.PositionModelType;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class FileCreator extends YamlConfiguration {
/*     */   private final String fileName;
/*     */   
/*     */   public FileCreator(Plugin plugin, String filename, String fileExtension, File folder) {
/*  24 */     this.plugin = plugin;
/*  25 */     this.fileName = filename + filename;
/*  26 */     this.file = new File(folder, this.fileName);
/*  27 */     createFile();
/*     */   }
/*     */   private final Plugin plugin; private final File file;
/*     */   public FileCreator(Plugin plugin, String fileName) {
/*  31 */     this(plugin, fileName, ".yml");
/*     */   }
/*     */   
/*     */   public FileCreator(Plugin plugin, String fileName, String fileExtension) {
/*  35 */     this(plugin, fileName, fileExtension, plugin.getDataFolder());
/*     */   }
/*     */   
/*     */   private void createFile() {
/*     */     try {
/*  40 */       if (!this.fileName.endsWith(".yml") && 
/*  41 */         !this.file.exists()) {
/*  42 */         if (this.plugin.getResource(this.fileName) != null) {
/*  43 */           this.plugin.saveResource(this.fileName, false);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*  48 */       if (!this.file.exists()) {
/*  49 */         if (this.plugin.getResource(this.fileName) != null) {
/*  50 */           this.plugin.saveResource(this.fileName, false);
/*     */         } else {
/*  52 */           save(this.file);
/*     */         } 
/*  54 */         load(this.file);
/*     */         return;
/*     */       } 
/*  57 */       load(this.file);
/*     */       
/*  59 */       save(this.file);
/*  60 */     } catch (InvalidConfigurationException|IOException e) {
/*  61 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public void saveDefault() {
/*  65 */     this.plugin.saveResource(this.fileName, false);
/*     */   }
/*     */   public void save() {
/*     */     try {
/*  69 */       save(this.file);
/*  70 */     } catch (IOException e) {
/*  71 */       this.plugin.getLogger().log(Level.SEVERE, "Save of the file '" + this.fileName + "' failed.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reload() {
/*     */     try {
/*  77 */       load(this.file);
/*  78 */     } catch (IOException|InvalidConfigurationException e) {
/*  79 */       this.plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + this.fileName + "' failed.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStringWF(String path) {
/*  85 */     return getString(path, "");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(String path) {
/*  91 */     String s = super.getString(path);
/*  92 */     if (s == null)
/*     */     {
/*  94 */       return null;
/*     */     }
/*  96 */     return Utils.ChatColor(s);
/*     */   }
/*     */   
/*     */   public OffsetModel getOffseTModel(String path) {
/* 100 */     String string = getString(path);
/* 101 */     if (string == null || string.isEmpty()) return new OffsetModel(0.0D, 0.5D, -0.15D, 0.0F, 0.0F); 
/* 102 */     String[] split = string.split(",");
/* 103 */     if (split.length >= 5) {
/* 104 */       double x = Double.parseDouble(split[0]);
/* 105 */       double y = Double.parseDouble(split[1]);
/* 106 */       double z = Double.parseDouble(split[2]);
/* 107 */       float yaw = Float.parseFloat(split[3]);
/* 108 */       float pitch = Float.parseFloat(split[4]);
/* 109 */       return new OffsetModel(x, y, z, yaw, pitch);
/*     */     } 
/* 111 */     return new OffsetModel(0.0D, 0.5D, -0.15D, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */   public PositionModelType getPositionModelType(String path) {
/* 115 */     String string = getString(path);
/* 116 */     if (string == null || string.isEmpty()) return PositionModelType.BODY; 
/*     */     try {
/* 118 */       return PositionModelType.valueOf(string.toUpperCase());
/* 119 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 120 */       return PositionModelType.BODY;
/*     */     } 
/*     */   }
/*     */   public List<Integer> getIntegerList(String path) {
/* 124 */     List<Integer> integers = new ArrayList<>();
/* 125 */     String list = getString(path);
/* 126 */     if (list.isEmpty()) return integers; 
/* 127 */     String[] split = list.split(",");
/* 128 */     for (String s : split) {
/*     */       try {
/* 130 */         integers.add(Integer.valueOf(Integer.parseInt(s)));
/* 131 */       } catch (NumberFormatException numberFormatException) {}
/*     */     } 
/*     */     
/* 134 */     return integers;
/*     */   }
/*     */   
/*     */   public List<String> getStringListWithComma(String path) {
/* 138 */     List<String> strings = new ArrayList<>();
/* 139 */     String list = getString(path);
/* 140 */     if (list == null || list.isEmpty()) return strings; 
/* 141 */     String[] split = list.split(",");
/* 142 */     strings.addAll(Arrays.asList(split));
/* 143 */     return strings;
/*     */   }
/*     */   
/*     */   public Set<Integer> getIntegerSet(String path) {
/* 147 */     Set<Integer> integers = new HashSet<>();
/* 148 */     String list = getString(path);
/* 149 */     if (list.isEmpty()) return integers; 
/* 150 */     String[] split = list.split(",");
/* 151 */     for (String s : split) {
/*     */       try {
/* 153 */         integers.add(Integer.valueOf(Integer.parseInt(s)));
/* 154 */       } catch (NumberFormatException numberFormatException) {}
/*     */     } 
/*     */     
/* 157 */     return integers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getStringList(String path) {
/* 162 */     List<String> list = super.getStringList(path);
/*     */     
/* 164 */     list.replaceAll(Utils::ChatColor);
/*     */     
/* 166 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getStringListWF(String path) {
/* 171 */     return super.getStringList(path);
/*     */   }
/*     */   
/*     */   public List<SecondaryColor> getSecondaryColor(String path) {
/* 175 */     List<String> colorText = super.getStringList(path);
/* 176 */     List<SecondaryColor> secondaryColors = new ArrayList<>();
/* 177 */     for (String color : colorText) {
/* 178 */       Color secondary; String[] colorTwo = color.split(";");
/*     */       
/*     */       try {
/* 181 */         secondary = Utils.hex2Rgb(colorTwo[0]);
/* 182 */       } catch (IllegalArgumentException exception) {
/*     */         continue;
/*     */       } 
/* 185 */       if (colorTwo.length > 1) {
/* 186 */         secondaryColors.add(new SecondaryColor(secondary, colorTwo[1])); continue;
/*     */       } 
/* 188 */       secondaryColors.add(new SecondaryColor(secondary));
/*     */     } 
/* 190 */     return secondaryColors;
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 194 */     return this.file.exists();
/*     */   }
/*     */   
/*     */   public String getFileName() {
/* 198 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 203 */     String name = this.fileName.replace(".yml", "");
/* 204 */     return name.substring(name.lastIndexOf("/") + 1);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\files\FileCreator.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */