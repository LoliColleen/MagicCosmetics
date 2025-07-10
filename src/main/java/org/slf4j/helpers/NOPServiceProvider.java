package org.slf4j.helpers;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class NOPServiceProvider implements SLF4JServiceProvider {
  public static String REQUESTED_API_VERSION = "1.8.99";
  
  private ILoggerFactory loggerFactory = new NOPLoggerFactory();
  
  private IMarkerFactory markerFactory = new BasicMarkerFactory();
  
  private MDCAdapter mdcAdapter = new NOPMDCAdapter();
  
  public ILoggerFactory getLoggerFactory() {
    return this.loggerFactory;
  }
  
  public IMarkerFactory getMarkerFactory() {
    return this.markerFactory;
  }
  
  public MDCAdapter getMDCAdapter() {
    return this.mdcAdapter;
  }
  
  public String getRequesteApiVersion() {
    return REQUESTED_API_VERSION;
  }
  
  public void initialize() {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\NOPServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */