package org.slf4j.helpers;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class SubstituteServiceProvider implements SLF4JServiceProvider {
  private SubstituteLoggerFactory loggerFactory = new SubstituteLoggerFactory();
  
  private IMarkerFactory markerFactory = new BasicMarkerFactory();
  
  private MDCAdapter mdcAdapter = new BasicMDCAdapter();
  
  public ILoggerFactory getLoggerFactory() {
    return this.loggerFactory;
  }
  
  public SubstituteLoggerFactory getSubstituteLoggerFactory() {
    return this.loggerFactory;
  }
  
  public IMarkerFactory getMarkerFactory() {
    return this.markerFactory;
  }
  
  public MDCAdapter getMDCAdapter() {
    return this.mdcAdapter;
  }
  
  public String getRequesteApiVersion() {
    throw new UnsupportedOperationException();
  }
  
  public void initialize() {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\SubstituteServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */