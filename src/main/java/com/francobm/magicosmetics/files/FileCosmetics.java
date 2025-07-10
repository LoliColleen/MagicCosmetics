/*    */ package com.francobm.magicosmetics.files;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ 
/*    */ public class FileCosmetics
/*    */ {
/* 12 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 13 */   private final Map<String, FileCreator> files = new ConcurrentHashMap<>();
/*    */   
/*    */   public FileCosmetics() {
/* 16 */     loadFiles();
/*    */   }
/*    */   
/*    */   public Map<String, FileCreator> getFiles() {
/* 20 */     return this.files;
/*    */   }
/*    */   
/*    */   public FileCreator getFile(String name) {
/* 24 */     return this.files.get(name);
/*    */   }
/*    */   
/*    */   public void loadFiles() {
/* 28 */     File path = new File(this.plugin.getDataFolder(), "cosmetics");
/* 29 */     if (!path.exists()) {
/* 30 */       MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: cosmetics.yml");
/* 31 */       this.files.put("cosmetics.yml", new FileCreator((Plugin)this.plugin, "cosmetics/cosmetics"));
/*    */       return;
/*    */     } 
/* 34 */     for (File file : path.listFiles()) {
/* 35 */       MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: " + file.getName());
/* 36 */       this.files.put(file.getName(), new FileCreator((Plugin)this.plugin, "cosmetics/" + file.getName()));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void loadFile(String name) {
/* 41 */     name = name.endsWith(".yml") ? name : (name + ".yml");
/* 42 */     MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: " + name);
/* 43 */     this.files.put(name, new FileCreator((Plugin)this.plugin, "cosmetics/" + name));
/*    */   }
/*    */   
/*    */   public void saveFiles() {
/* 47 */     for (FileCreator file : this.files.values()) {
/* 48 */       MagicCosmetics.getInstance().getLogger().info("Saving file cosmetic: " + file.getFileName());
/* 49 */       file.save();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void saveFile(String name) {
/* 54 */     name = name.endsWith(".yml") ? name : (name + ".yml");
/* 55 */     MagicCosmetics.getInstance().getLogger().info("Saving file cosmetic: " + name);
/* 56 */     ((FileCreator)this.files.get(name)).save();
/*    */   }
/*    */   
/*    */   public void reloadFiles() {
/* 60 */     File path = new File(this.plugin.getDataFolder(), "cosmetics");
/* 61 */     if (!path.exists()) {
/* 62 */       MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: cosmetics.yml");
/* 63 */       this.files.put("cosmetics.yml", new FileCreator((Plugin)this.plugin, "cosmetics/cosmetics"));
/*    */       return;
/*    */     } 
/* 66 */     for (FileCreator file : this.files.values()) {
/* 67 */       if (!file.exists()) {
/* 68 */         deleteFile(file.getFileName());
/*    */         continue;
/*    */       } 
/* 71 */       MagicCosmetics.getInstance().getLogger().info("Reloading file cosmetic: " + file.getFileName());
/* 72 */       file.reload();
/*    */     } 
/* 74 */     for (File file : path.listFiles()) {
/* 75 */       if (!this.files.containsKey(file.getName()))
/* 76 */         loadFile(file.getName()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void reloadFile(String name) {
/* 81 */     name = name.endsWith(".yml") ? name : (name + ".yml");
/* 82 */     MagicCosmetics.getInstance().getLogger().info("Reloading file cosmetic: " + name);
/* 83 */     ((FileCreator)this.files.get(name)).reload();
/*    */   }
/*    */   
/*    */   public void deleteFile(String name) {
/* 87 */     if (name.contains("/")) {
/* 88 */       name = name.split("/")[1];
/*    */     }
/* 90 */     name = name.endsWith(".yml") ? name : (name + ".yml");
/* 91 */     MagicCosmetics.getInstance().getLogger().info("Deleting file cosmetic: " + name);
/* 92 */     this.files.remove(name);
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\files\FileCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */