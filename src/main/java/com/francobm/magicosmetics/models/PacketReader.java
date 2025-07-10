package com.francobm.magicosmetics.models;

import java.lang.reflect.Field;
import org.bukkit.entity.Player;

public abstract class PacketReader {
  public abstract void injectPlayer(Player paramPlayer);
  
  public abstract void removePlayer(Player paramPlayer);
  
  protected Object getValue(Object instance, String name) {
    Object result = null;
    try {
      Field field = instance.getClass().getDeclaredField(name);
      field.setAccessible(true);
      result = field.get(instance);
      field.setAccessible(false);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return result;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\models\PacketReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */