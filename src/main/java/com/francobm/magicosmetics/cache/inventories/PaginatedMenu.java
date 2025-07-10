/*     */ package com.francobm.magicosmetics.cache.inventories;
/*     */ 
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class PaginatedMenu
/*     */   extends Menu
/*     */ {
/*  12 */   protected int page = 0;
/*     */   
/*     */   protected List<Integer> maxItemsPerPage;
/*     */   
/*     */   protected int startSlot;
/*     */   
/*     */   protected int endSlot;
/*     */   
/*     */   protected int pagesSlot;
/*     */   
/*     */   protected Set<Integer> backSlot;
/*     */   
/*     */   protected Set<Integer> nextSlot;
/*     */   protected List<Integer> slotsUnavailable;
/*  26 */   protected int index = 0;
/*     */   
/*     */   protected boolean showAllCosmeticsInMenu = true;
/*     */   
/*     */   public PaginatedMenu(String id, ContentMenu contentMenu) {
/*  31 */     super(id, contentMenu);
/*  32 */     this.startSlot = 0;
/*  33 */     this.endSlot = 0;
/*  34 */     this.pagesSlot = 0;
/*  35 */     this.backSlot = new HashSet<>();
/*  36 */     this.nextSlot = new HashSet<>();
/*  37 */     this.maxItemsPerPage = new ArrayList<>();
/*  38 */     this.slotsUnavailable = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public PaginatedMenu(PlayerData playerData, Menu menu) {
/*  42 */     super(playerData, menu);
/*  43 */     PaginatedMenu paginatedMenu = (PaginatedMenu)menu;
/*  44 */     this.startSlot = paginatedMenu.getStartSlot();
/*  45 */     this.endSlot = paginatedMenu.getEndSlot();
/*  46 */     this.pagesSlot = paginatedMenu.getPagesSlot();
/*  47 */     this.backSlot = paginatedMenu.getBackSlot();
/*  48 */     this.nextSlot = paginatedMenu.getNextSlot();
/*  49 */     this.maxItemsPerPage = paginatedMenu.getMaxItemsPerPageList();
/*  50 */     this.slotsUnavailable = paginatedMenu.getSlotsUnavailable();
/*     */   }
/*     */   
/*     */   public PaginatedMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable) {
/*  54 */     super(id, contentMenu);
/*  55 */     this.startSlot = startSlot;
/*  56 */     this.endSlot = endSlot;
/*  57 */     this.pagesSlot = pagesSlot;
/*  58 */     this.backSlot = backSlot;
/*  59 */     this.nextSlot = nextSlot;
/*  60 */     this.slotsUnavailable = slotsUnavailable;
/*  61 */     this.maxItemsPerPage = new ArrayList<>();
/*  62 */     for (int i = startSlot; i <= endSlot; i++) {
/*  63 */       if (!slotsUnavailable.contains(Integer.valueOf(i)))
/*  64 */         this.maxItemsPerPage.add(Integer.valueOf(i)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Integer> getSlotsUnavailable() {
/*  69 */     return this.slotsUnavailable;
/*     */   }
/*     */   
/*     */   public void setSlotsUnavailable(List<Integer> slotsUnavailable) {
/*  73 */     this.slotsUnavailable = slotsUnavailable;
/*     */   }
/*     */   
/*     */   public int getPage() {
/*  77 */     return this.page;
/*     */   }
/*     */   
/*     */   public void setPage(int page) {
/*  81 */     this.page = page;
/*     */   }
/*     */   
/*     */   public int getMaxItemsPerPage() {
/*  85 */     return this.maxItemsPerPage.size();
/*     */   }
/*     */   public List<Integer> getMaxItemsPerPageList() {
/*  88 */     return this.maxItemsPerPage;
/*     */   }
/*     */   
/*     */   public int getStartSlot() {
/*  92 */     return this.startSlot;
/*     */   }
/*     */   
/*     */   public int getEndSlot() {
/*  96 */     return this.endSlot;
/*     */   }
/*     */   
/*     */   public Set<Integer> getBackSlot() {
/* 100 */     return this.backSlot;
/*     */   }
/*     */   
/*     */   public Set<Integer> getNextSlot() {
/* 104 */     return this.nextSlot;
/*     */   }
/*     */   
/*     */   public int getPagesSlot() {
/* 108 */     return this.pagesSlot;
/*     */   }
/*     */   
/*     */   public void setShowAllCosmeticsInMenu(boolean showAllCosmeticsInMenu) {
/* 112 */     this.showAllCosmeticsInMenu = showAllCosmeticsInMenu;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return "PaginatedMenu{id='" + this.id + "', playerCache=" + String.valueOf(this.playerData) + ", contentMenu=" + String.valueOf(this.contentMenu) + ", page=" + this.page + ", maxItemsPerPage=" + String.valueOf(this.maxItemsPerPage) + ", startSlot=" + this.startSlot + ", endSlot=" + this.endSlot + ", pagesSlot=" + this.pagesSlot + ", backSlot=" + String.valueOf(this.backSlot) + ", nextSlot=" + String.valueOf(this.nextSlot) + ", slotsUnavailable=" + String.valueOf(this.slotsUnavailable) + ", index=" + this.index + "}";
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\PaginatedMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */