package org.slf4j;

import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.spi.SLF4JServiceProvider;

public class MarkerFactory {
  static IMarkerFactory MARKER_FACTORY;
  
  static {
    SLF4JServiceProvider provider = LoggerFactory.getProvider();
    if (provider != null) {
      MARKER_FACTORY = provider.getMarkerFactory();
    } else {
      Util.report("Failed to find provider");
      Util.report("Defaulting to BasicMarkerFactory.");
      MARKER_FACTORY = (IMarkerFactory)new BasicMarkerFactory();
    } 
  }
  
  public static Marker getMarker(String name) {
    return MARKER_FACTORY.getMarker(name);
  }
  
  public static Marker getDetachedMarker(String name) {
    return MARKER_FACTORY.getDetachedMarker(name);
  }
  
  public static IMarkerFactory getIMarkerFactory() {
    return MARKER_FACTORY;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\MarkerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */