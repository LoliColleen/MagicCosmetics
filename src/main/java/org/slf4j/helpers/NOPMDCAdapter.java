package org.slf4j.helpers;

import java.util.Map;
import org.slf4j.spi.MDCAdapter;

public class NOPMDCAdapter implements MDCAdapter {
  public void clear() {}
  
  public String get(String key) {
    return null;
  }
  
  public void put(String key, String val) {}
  
  public void remove(String key) {}
  
  public Map<String, String> getCopyOfContextMap() {
    return null;
  }
  
  public void setContextMap(Map<String, String> contextMap) {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\NOPMDCAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */