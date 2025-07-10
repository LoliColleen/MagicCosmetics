package com.francobm.magicosmetics.cache.cosmetics.store;

import com.francobm.magicosmetics.api.Cosmetic;
import java.util.Map;

public interface ICosmeticStore {
  Map<String, Cosmetic> getCosmetics();
  
  boolean hasPermission();
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\cosmetics\store\ICosmeticStore.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */