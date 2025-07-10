/*    */ package com.francobm.magicosmetics.cache.renderer;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.map.MapCanvas;
/*    */ import org.bukkit.map.MapPalette;
/*    */ import org.bukkit.map.MapRenderer;
/*    */ import org.bukkit.map.MapView;
/*    */ 
/*    */ public class ImageRenderer extends MapRenderer {
/*    */   private BufferedImage image;
/*    */   
/*    */   public ImageRenderer() {
/* 19 */     this.image = null;
/* 20 */     this.loaded = false;
/*    */   }
/*    */   private boolean loaded;
/*    */   public ImageRenderer(String url) {
/* 24 */     this.image = null;
/* 25 */     this.loaded = false;
/*    */   }
/*    */   
/*    */   public boolean load(BufferedImage image) {
/* 29 */     if (image == null) return false; 
/* 30 */     this.image = image;
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public boolean load(String url) {
/*    */     BufferedImage image;
/*    */     try {
/* 37 */       if (url.startsWith("http")) {
/* 38 */         image = ImageIO.read(new URL(url));
/*    */       } else {
/* 40 */         File file = new File(MagicCosmetics.getInstance().getDataFolder(), "sprays/" + url);
/* 41 */         MagicCosmetics.getInstance().getLogger().info("Loading spray from file: " + file.getAbsolutePath());
/* 42 */         if (!file.exists()) return false; 
/* 43 */         image = ImageIO.read(file);
/*    */       } 
/* 45 */       image = MapPalette.resizeImage(image);
/* 46 */     } catch (IOException e) {
/* 47 */       e.printStackTrace();
/* 48 */       return false;
/*    */     } 
/* 50 */     this.image = image;
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
/* 56 */     if (this.loaded)
/* 57 */       return;  mapCanvas.drawImage(0, 0, this.image);
/* 58 */     mapView.setTrackingPosition(false);
/* 59 */     this.loaded = true;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\renderer\ImageRenderer.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */