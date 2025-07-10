package org.slf4j.spi;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;

public interface SLF4JServiceProvider {
  ILoggerFactory getLoggerFactory();
  
  IMarkerFactory getMarkerFactory();
  
  MDCAdapter getMDCAdapter();
  
  String getRequesteApiVersion();
  
  void initialize();
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\spi\SLF4JServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */