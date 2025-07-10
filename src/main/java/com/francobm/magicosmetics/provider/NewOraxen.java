/*    */ package com.francobm.magicosmetics.provider;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import io.th0rgal.oraxen.OraxenPlugin;
/*    */ import io.th0rgal.oraxen.api.OraxenItems;
/*    */ import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
/*    */ import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
/*    */ import io.th0rgal.oraxen.font.FontManager;
/*    */ import io.th0rgal.oraxen.font.Glyph;
/*    */ import io.th0rgal.oraxen.items.ItemBuilder;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class NewOraxen
/*    */   extends CompatibilityProvider<MagicCosmetics>
/*    */   implements Oraxen {
/* 18 */   Pattern pattern = Pattern.compile(":\\w+:");
/*    */   
/*    */   public void register() {
/* 21 */     CompatibilitiesManager.addCompatibility("MagicCosmetics", NewOraxen.class);
/*    */   }
/*    */   
/*    */   public ItemStack getItemStackById(String id) {
/* 25 */     if (!OraxenItems.exists(id)) return null; 
/* 26 */     ItemBuilder itemBuilder = OraxenItems.getItemById(id);
/* 27 */     if (itemBuilder == null) return null; 
/* 28 */     return itemBuilder.build();
/*    */   }
/*    */   
/*    */   public ItemStack getItemStackByItem(ItemStack itemStack) {
/* 32 */     String id = OraxenItems.getIdByItem(itemStack);
/* 33 */     if (id == null) return null; 
/* 34 */     ItemBuilder itemBuilder = OraxenItems.getItemById(id);
/* 35 */     if (itemBuilder == null) return null; 
/* 36 */     return itemBuilder.build();
/*    */   }
/*    */   
/*    */   public String replaceFontImages(String id) {
/* 40 */     OraxenPlugin oraxenPlugin = OraxenPlugin.get();
/* 41 */     if (oraxenPlugin == null) return id; 
/* 42 */     FontManager fontManager = oraxenPlugin.getFontManager();
/* 43 */     if (fontManager == null) return id; 
/* 44 */     Matcher matcher = this.pattern.matcher(id);
/* 45 */     while (matcher.find()) {
/* 46 */       String placeholder = matcher.group();
/* 47 */       String glyphName = placeholder.replace(":", "");
/* 48 */       Glyph glyph = fontManager.getGlyphFromID(glyphName);
/* 49 */       if (glyph == null)
/* 50 */         continue;  id = id.replace(placeholder, glyph.getCharacter());
/*    */     } 
/*    */     
/* 53 */     return id;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\NewOraxen.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */