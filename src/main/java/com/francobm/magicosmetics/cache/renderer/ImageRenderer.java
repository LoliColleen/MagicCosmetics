package com.francobm.magicosmetics.cache.renderer;

import com.francobm.magicosmetics.MagicCosmetics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageRenderer extends MapRenderer {
  private BufferedImage image;
  
  private boolean loaded;
  
  public ImageRenderer() {
    this.image = null;
    this.loaded = false;
  }
  
  public ImageRenderer(String url) {
    this.image = null;
    this.loaded = false;
  }
  
  public boolean load(BufferedImage image) {
    if (image == null)
      return false; 
    this.image = image;
    return true;
  }
  
  public boolean load(String url) {
    BufferedImage image;
    try {
      if (url.startsWith("http")) {
        image = ImageIO.read(new URL(url));
      } else {
        File file = new File(MagicCosmetics.getInstance().getDataFolder(), "sprays/" + url);
        MagicCosmetics.getInstance().getLogger().info("Loading spray from file: " + file.getAbsolutePath());
        if (!file.exists())
          return false; 
        image = ImageIO.read(file);
      } 
      image = MapPalette.resizeImage(image);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } 
    this.image = image;
    return true;
  }
  
  public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
    if (this.loaded)
      return; 
    mapCanvas.drawImage(0, 0, this.image);
    mapView.setTrackingPosition(false);
    this.loaded = true;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\renderer\ImageRenderer.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */