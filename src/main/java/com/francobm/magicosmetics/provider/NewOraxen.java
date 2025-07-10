package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.MagicCosmetics;
import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.font.FontManager;
import io.th0rgal.oraxen.font.Glyph;
import io.th0rgal.oraxen.items.ItemBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.inventory.ItemStack;

public class NewOraxen extends CompatibilityProvider<MagicCosmetics> implements Oraxen {
  Pattern pattern = Pattern.compile(":\\w+:");
  
  public void register() {
    CompatibilitiesManager.addCompatibility("MagicCosmetics", NewOraxen.class);
  }
  
  public ItemStack getItemStackById(String id) {
    if (!OraxenItems.exists(id))
      return null; 
    ItemBuilder itemBuilder = OraxenItems.getItemById(id);
    if (itemBuilder == null)
      return null; 
    return itemBuilder.build();
  }
  
  public ItemStack getItemStackByItem(ItemStack itemStack) {
    String id = OraxenItems.getIdByItem(itemStack);
    if (id == null)
      return null; 
    ItemBuilder itemBuilder = OraxenItems.getItemById(id);
    if (itemBuilder == null)
      return null; 
    return itemBuilder.build();
  }
  
  public String replaceFontImages(String id) {
    OraxenPlugin oraxenPlugin = OraxenPlugin.get();
    if (oraxenPlugin == null)
      return id; 
    FontManager fontManager = oraxenPlugin.getFontManager();
    if (fontManager == null)
      return id; 
    Matcher matcher = this.pattern.matcher(id);
    while (matcher.find()) {
      String placeholder = matcher.group();
      String glyphName = placeholder.replace(":", "");
      Glyph glyph = fontManager.getGlyphFromID(glyphName);
      if (glyph == null)
        continue; 
      id = id.replace(placeholder, glyph.getCharacter());
    } 
    return id;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\NewOraxen.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */