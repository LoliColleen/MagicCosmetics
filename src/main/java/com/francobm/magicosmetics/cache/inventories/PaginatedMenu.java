package com.francobm.magicosmetics.cache.inventories;

import com.francobm.magicosmetics.cache.PlayerData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PaginatedMenu extends Menu {
  protected int page = 0;
  
  protected List<Integer> maxItemsPerPage;
  
  protected int startSlot;
  
  protected int endSlot;
  
  protected int pagesSlot;
  
  protected Set<Integer> backSlot;
  
  protected Set<Integer> nextSlot;
  
  protected List<Integer> slotsUnavailable;
  
  protected int index = 0;
  
  protected boolean showAllCosmeticsInMenu = true;
  
  public PaginatedMenu(String id, ContentMenu contentMenu) {
    super(id, contentMenu);
    this.startSlot = 0;
    this.endSlot = 0;
    this.pagesSlot = 0;
    this.backSlot = new HashSet<>();
    this.nextSlot = new HashSet<>();
    this.maxItemsPerPage = new ArrayList<>();
    this.slotsUnavailable = new ArrayList<>();
  }
  
  public PaginatedMenu(PlayerData playerData, Menu menu) {
    super(playerData, menu);
    PaginatedMenu paginatedMenu = (PaginatedMenu)menu;
    this.startSlot = paginatedMenu.getStartSlot();
    this.endSlot = paginatedMenu.getEndSlot();
    this.pagesSlot = paginatedMenu.getPagesSlot();
    this.backSlot = paginatedMenu.getBackSlot();
    this.nextSlot = paginatedMenu.getNextSlot();
    this.maxItemsPerPage = paginatedMenu.getMaxItemsPerPageList();
    this.slotsUnavailable = paginatedMenu.getSlotsUnavailable();
  }
  
  public PaginatedMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
    super(id, contentMenu);
    this.startSlot = startSlot;
    this.endSlot = endSlot;
    this.pagesSlot = pagesSlot;
    this.backSlot = backSlot;
    this.nextSlot = nextSlot;
    this.slotsUnavailable = slotsUnavailable;
    this.maxItemsPerPage = new ArrayList<>();
    for (int i = startSlot; i <= endSlot; i++) {
      if (!slotsUnavailable.contains(Integer.valueOf(i)))
        this.maxItemsPerPage.add(Integer.valueOf(i)); 
    } 
  }
  
  public List<Integer> getSlotsUnavailable() {
    return this.slotsUnavailable;
  }
  
  public void setSlotsUnavailable(List<Integer> slotsUnavailable) {
    this.slotsUnavailable = slotsUnavailable;
  }
  
  public int getPage() {
    return this.page;
  }
  
  public void setPage(int page) {
    this.page = page;
  }
  
  public int getMaxItemsPerPage() {
    return this.maxItemsPerPage.size();
  }
  
  public List<Integer> getMaxItemsPerPageList() {
    return this.maxItemsPerPage;
  }
  
  public int getStartSlot() {
    return this.startSlot;
  }
  
  public int getEndSlot() {
    return this.endSlot;
  }
  
  public Set<Integer> getBackSlot() {
    return this.backSlot;
  }
  
  public Set<Integer> getNextSlot() {
    return this.nextSlot;
  }
  
  public int getPagesSlot() {
    return this.pagesSlot;
  }
  
  public void setShowAllCosmeticsInMenu(boolean showAllCosmeticsInMenu) {
    this.showAllCosmeticsInMenu = showAllCosmeticsInMenu;
  }
  
  public String toString() {
    return "PaginatedMenu{id='" + this.id + "', playerCache=" + String.valueOf(this.playerData) + ", contentMenu=" + String.valueOf(this.contentMenu) + ", page=" + this.page + ", maxItemsPerPage=" + String.valueOf(this.maxItemsPerPage) + ", startSlot=" + this.startSlot + ", endSlot=" + this.endSlot + ", pagesSlot=" + this.pagesSlot + ", backSlot=" + String.valueOf(this.backSlot) + ", nextSlot=" + String.valueOf(this.nextSlot) + ", slotsUnavailable=" + String.valueOf(this.slotsUnavailable) + ", index=" + this.index + "}";
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\PaginatedMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */