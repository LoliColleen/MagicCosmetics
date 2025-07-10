package com.francobm.magicosmetics.cache;

public class Row {
  private final String row;
  
  private final String character;
  
  private final String selected;
  
  public Row(String row, String character, String selected) {
    this.row = row;
    this.character = character;
    this.selected = selected;
  }
  
  public String getRow() {
    return this.row;
  }
  
  public String getCharacter() {
    return this.character;
  }
  
  public String getSelected() {
    return this.selected;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\Row.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */