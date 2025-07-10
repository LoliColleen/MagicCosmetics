package org.slf4j.helpers;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class NamedLoggerBase implements Logger, Serializable {
  private static final long serialVersionUID = 7535258609338176893L;
  
  protected String name;
  
  public String getName() {
    return this.name;
  }
  
  protected Object readResolve() throws ObjectStreamException {
    return LoggerFactory.getLogger(getName());
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\NamedLoggerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */