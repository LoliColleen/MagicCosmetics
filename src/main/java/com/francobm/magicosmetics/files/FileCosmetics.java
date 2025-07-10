package com.francobm.magicosmetics.files;

import com.francobm.magicosmetics.MagicCosmetics;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.Plugin;

public class FileCosmetics {
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  private final Map<String, FileCreator> files = new ConcurrentHashMap<>();
  
  public FileCosmetics() {
    loadFiles();
  }
  
  public Map<String, FileCreator> getFiles() {
    return this.files;
  }
  
  public FileCreator getFile(String name) {
    return this.files.get(name);
  }
  
  public void loadFiles() {
    File path = new File(this.plugin.getDataFolder(), "cosmetics");
    if (!path.exists()) {
      MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: cosmetics.yml");
      this.files.put("cosmetics.yml", new FileCreator((Plugin)this.plugin, "cosmetics/cosmetics"));
      return;
    } 
    for (File file : path.listFiles()) {
      MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: " + file.getName());
      this.files.put(file.getName(), new FileCreator((Plugin)this.plugin, "cosmetics/" + file.getName()));
    } 
  }
  
  public void loadFile(String name) {
    name = name.endsWith(".yml") ? name : (name + ".yml");
    MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: " + name);
    this.files.put(name, new FileCreator((Plugin)this.plugin, "cosmetics/" + name));
  }
  
  public void saveFiles() {
    for (FileCreator file : this.files.values()) {
      MagicCosmetics.getInstance().getLogger().info("Saving file cosmetic: " + file.getFileName());
      file.save();
    } 
  }
  
  public void saveFile(String name) {
    name = name.endsWith(".yml") ? name : (name + ".yml");
    MagicCosmetics.getInstance().getLogger().info("Saving file cosmetic: " + name);
    ((FileCreator)this.files.get(name)).save();
  }
  
  public void reloadFiles() {
    File path = new File(this.plugin.getDataFolder(), "cosmetics");
    if (!path.exists()) {
      MagicCosmetics.getInstance().getLogger().info("Loading file cosmetic: cosmetics.yml");
      this.files.put("cosmetics.yml", new FileCreator((Plugin)this.plugin, "cosmetics/cosmetics"));
      return;
    } 
    for (FileCreator file : this.files.values()) {
      if (!file.exists()) {
        deleteFile(file.getFileName());
        continue;
      } 
      MagicCosmetics.getInstance().getLogger().info("Reloading file cosmetic: " + file.getFileName());
      file.reload();
    } 
    for (File file : path.listFiles()) {
      if (!this.files.containsKey(file.getName()))
        loadFile(file.getName()); 
    } 
  }
  
  public void reloadFile(String name) {
    name = name.endsWith(".yml") ? name : (name + ".yml");
    MagicCosmetics.getInstance().getLogger().info("Reloading file cosmetic: " + name);
    ((FileCreator)this.files.get(name)).reload();
  }
  
  public void deleteFile(String name) {
    if (name.contains("/"))
      name = name.split("/")[1]; 
    name = name.endsWith(".yml") ? name : (name + ".yml");
    MagicCosmetics.getInstance().getLogger().info("Deleting file cosmetic: " + name);
    this.files.remove(name);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\files\FileCosmetics.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */