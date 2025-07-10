package org.slf4j.helpers;

import org.slf4j.Marker;

public abstract class LegacyAbstractLogger extends AbstractLogger {
  private static final long serialVersionUID = -7041884104854048950L;
  
  public boolean isTraceEnabled(Marker marker) {
    return isTraceEnabled();
  }
  
  public boolean isDebugEnabled(Marker marker) {
    return isDebugEnabled();
  }
  
  public boolean isInfoEnabled(Marker marker) {
    return isInfoEnabled();
  }
  
  public boolean isWarnEnabled(Marker marker) {
    return isWarnEnabled();
  }
  
  public boolean isErrorEnabled(Marker marker) {
    return isErrorEnabled();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\LegacyAbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */