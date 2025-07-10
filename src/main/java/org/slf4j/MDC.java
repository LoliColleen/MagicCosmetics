package org.slf4j;

import java.io.Closeable;
import java.util.Map;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.helpers.Util;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class MDC {
  static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
  
  static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
  
  static MDCAdapter mdcAdapter;
  
  public static class MDCCloseable implements Closeable {
    private final String key;
    
    private MDCCloseable(String key) {
      this.key = key;
    }
    
    public void close() {
      MDC.remove(this.key);
    }
  }
  
  static {
    SLF4JServiceProvider provider = LoggerFactory.getProvider();
    if (provider != null) {
      mdcAdapter = provider.getMDCAdapter();
    } else {
      Util.report("Failed to find provider.");
      Util.report("Defaulting to no-operation MDCAdapter implementation.");
      mdcAdapter = (MDCAdapter)new NOPMDCAdapter();
    } 
  }
  
  public static void put(String key, String val) throws IllegalArgumentException {
    if (key == null)
      throw new IllegalArgumentException("key parameter cannot be null"); 
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    mdcAdapter.put(key, val);
  }
  
  public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
    put(key, val);
    return new MDCCloseable(key);
  }
  
  public static String get(String key) throws IllegalArgumentException {
    if (key == null)
      throw new IllegalArgumentException("key parameter cannot be null"); 
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    return mdcAdapter.get(key);
  }
  
  public static void remove(String key) throws IllegalArgumentException {
    if (key == null)
      throw new IllegalArgumentException("key parameter cannot be null"); 
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    mdcAdapter.remove(key);
  }
  
  public static void clear() {
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    mdcAdapter.clear();
  }
  
  public static Map<String, String> getCopyOfContextMap() {
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    return mdcAdapter.getCopyOfContextMap();
  }
  
  public static void setContextMap(Map<String, String> contextMap) {
    if (mdcAdapter == null)
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA"); 
    mdcAdapter.setContextMap(contextMap);
  }
  
  public static MDCAdapter getMDCAdapter() {
    return mdcAdapter;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\MDC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */