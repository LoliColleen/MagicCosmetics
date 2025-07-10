/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.files.FileCreator;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Sound
/*    */ {
/* 10 */   public static Map<String, Sound> sounds = new HashMap<>();
/*    */   private final String id;
/*    */   private final String soundCustom;
/*    */   private final org.bukkit.Sound soundBukkit;
/*    */   private final float yaw;
/*    */   private final float pitch;
/*    */   
/*    */   public Sound(String id, String soundCustom, org.bukkit.Sound soundBukkit, float yaw, float pitch) {
/* 18 */     this.id = id;
/* 19 */     this.soundCustom = soundCustom;
/* 20 */     this.soundBukkit = soundBukkit;
/* 21 */     this.yaw = yaw;
/* 22 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public static Sound getSound(String id) {
/* 26 */     return sounds.get(id);
/*    */   }
/*    */   
/*    */   public static void loadSounds() {
/* 30 */     sounds.clear();
/* 31 */     FileCreator soundsConfig = MagicCosmetics.getInstance().getSounds();
/* 32 */     for (String key : soundsConfig.getConfigurationSection("sounds").getKeys(false)) {
/*    */       
/* 34 */       String soundCustom = "";
/* 35 */       org.bukkit.Sound soundBukkit = null;
/* 36 */       float yaw = 1.0F;
/* 37 */       float pitch = 0.5F;
/* 38 */       if (soundsConfig.contains("sounds." + key + ".type")) {
/* 39 */         String sound = soundsConfig.getString("sounds." + key + ".type");
/*    */         try {
/* 41 */           soundBukkit = org.bukkit.Sound.valueOf(sound.toUpperCase());
/* 42 */         } catch (IllegalArgumentException exception) {
/* 43 */           MagicCosmetics.getInstance().getLogger().info("Sound '" + sound + "' not Found in Bukkit... Transform custom");
/* 44 */           soundCustom = sound;
/*    */         } 
/*    */       } 
/* 47 */       if (soundsConfig.contains("sounds." + key + ".yaw")) {
/* 48 */         yaw = (float)soundsConfig.getDouble("sounds." + key + ".yaw");
/*    */       }
/* 50 */       if (soundsConfig.contains("sounds." + key + ".pitch")) {
/* 51 */         pitch = (float)soundsConfig.getDouble("sounds." + key + ".pitch");
/*    */       }
/* 53 */       sounds.put(key, new Sound(key, soundCustom, soundBukkit, yaw, pitch));
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getId() {
/* 58 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSoundCustom() {
/* 62 */     return this.soundCustom;
/*    */   }
/*    */   
/*    */   public boolean isCustom() {
/* 66 */     return !this.soundCustom.isEmpty();
/*    */   }
/*    */   
/*    */   public org.bukkit.Sound getSoundBukkit() {
/* 70 */     return this.soundBukkit;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 74 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 78 */     return this.pitch;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\Sound.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */