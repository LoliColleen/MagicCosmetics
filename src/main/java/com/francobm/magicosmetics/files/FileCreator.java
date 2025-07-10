package com.francobm.magicosmetics.files;

import com.francobm.magicosmetics.cache.SecondaryColor;
import com.francobm.magicosmetics.utils.OffsetModel;
import com.francobm.magicosmetics.utils.PositionModelType;
import com.francobm.magicosmetics.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileCreator extends YamlConfiguration {
  private final String fileName;
  
  private final Plugin plugin;
  
  private final File file;
  
  public FileCreator(Plugin plugin, String filename, String fileExtension, File folder) {
    this.plugin = plugin;
    this.fileName = filename + filename;
    this.file = new File(folder, this.fileName);
    createFile();
  }
  
  public FileCreator(Plugin plugin, String fileName) {
    this(plugin, fileName, ".yml");
  }
  
  public FileCreator(Plugin plugin, String fileName, String fileExtension) {
    this(plugin, fileName, fileExtension, plugin.getDataFolder());
  }
  
  private void createFile() {
    try {
      if (!this.fileName.endsWith(".yml") && 
        !this.file.exists()) {
        if (this.plugin.getResource(this.fileName) != null)
          this.plugin.saveResource(this.fileName, false); 
        return;
      } 
      if (!this.file.exists()) {
        if (this.plugin.getResource(this.fileName) != null) {
          this.plugin.saveResource(this.fileName, false);
        } else {
          save(this.file);
        } 
        load(this.file);
        return;
      } 
      load(this.file);
      save(this.file);
    } catch (InvalidConfigurationException|IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void saveDefault() {
    this.plugin.saveResource(this.fileName, false);
  }
  
  public void save() {
    try {
      save(this.file);
    } catch (IOException e) {
      this.plugin.getLogger().log(Level.SEVERE, "Save of the file '" + this.fileName + "' failed.", e);
    } 
  }
  
  public void reload() {
    try {
      load(this.file);
    } catch (IOException|InvalidConfigurationException e) {
      this.plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + this.fileName + "' failed.", e);
    } 
  }
  
  public String getStringWF(String path) {
    return getString(path, "");
  }
  
  public String getString(String path) {
    String s = super.getString(path);
    if (s == null)
      return null; 
    return Utils.ChatColor(s);
  }
  
  public OffsetModel getOffseTModel(String path) {
    String string = getString(path);
    if (string == null || string.isEmpty())
      return new OffsetModel(0.0D, 0.5D, -0.15D, 0.0F, 0.0F); 
    String[] split = string.split(",");
    if (split.length >= 5) {
      double x = Double.parseDouble(split[0]);
      double y = Double.parseDouble(split[1]);
      double z = Double.parseDouble(split[2]);
      float yaw = Float.parseFloat(split[3]);
      float pitch = Float.parseFloat(split[4]);
      return new OffsetModel(x, y, z, yaw, pitch);
    } 
    return new OffsetModel(0.0D, 0.5D, -0.15D, 0.0F, 0.0F);
  }
  
  public PositionModelType getPositionModelType(String path) {
    String string = getString(path);
    if (string == null || string.isEmpty())
      return PositionModelType.BODY; 
    try {
      return PositionModelType.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException illegalArgumentException) {
      return PositionModelType.BODY;
    } 
  }
  
  public List<Integer> getIntegerList(String path) {
    List<Integer> integers = new ArrayList<>();
    String list = getString(path);
    if (list.isEmpty())
      return integers; 
    String[] split = list.split(",");
    for (String s : split) {
      try {
        integers.add(Integer.valueOf(Integer.parseInt(s)));
      } catch (NumberFormatException numberFormatException) {}
    } 
    return integers;
  }
  
  public List<String> getStringListWithComma(String path) {
    List<String> strings = new ArrayList<>();
    String list = getString(path);
    if (list == null || list.isEmpty())
      return strings; 
    String[] split = list.split(",");
    strings.addAll(Arrays.asList(split));
    return strings;
  }
  
  public Set<Integer> getIntegerSet(String path) {
    Set<Integer> integers = new HashSet<>();
    String list = getString(path);
    if (list.isEmpty())
      return integers; 
    String[] split = list.split(",");
    for (String s : split) {
      try {
        integers.add(Integer.valueOf(Integer.parseInt(s)));
      } catch (NumberFormatException numberFormatException) {}
    } 
    return integers;
  }
  
  public List<String> getStringList(String path) {
    List<String> list = super.getStringList(path);
    list.replaceAll(Utils::ChatColor);
    return list;
  }
  
  public List<String> getStringListWF(String path) {
    return super.getStringList(path);
  }
  
  public List<SecondaryColor> getSecondaryColor(String path) {
    List<String> colorText = super.getStringList(path);
    List<SecondaryColor> secondaryColors = new ArrayList<>();
    for (String color : colorText) {
      Color secondary;
      String[] colorTwo = color.split(";");
      try {
        secondary = Utils.hex2Rgb(colorTwo[0]);
      } catch (IllegalArgumentException exception) {
        continue;
      } 
      if (colorTwo.length > 1) {
        secondaryColors.add(new SecondaryColor(secondary, colorTwo[1]));
        continue;
      } 
      secondaryColors.add(new SecondaryColor(secondary));
    } 
    return secondaryColors;
  }
  
  public boolean exists() {
    return this.file.exists();
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public String getName() {
    String name = this.fileName.replace(".yml", "");
    return name.substring(name.lastIndexOf("/") + 1);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\files\FileCreator.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */