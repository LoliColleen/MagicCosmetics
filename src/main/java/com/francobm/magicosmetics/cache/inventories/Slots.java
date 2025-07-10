/*     */ package com.francobm.magicosmetics.cache.inventories;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slots
/*     */ {
/*  16 */   private int min = 0;
/*  17 */   private int max = 0;
/*     */   
/*     */   private boolean num1 = false;
/*     */   private boolean num2 = false;
/*     */   private boolean num3 = false;
/*     */   private boolean num4 = false;
/*     */   private boolean num5 = false;
/*     */   private boolean num6 = false;
/*     */   
/*     */   public void resetSlots() {
/*  27 */     this.min = 0;
/*  28 */     this.max = 0;
/*  29 */     this.num1 = false;
/*  30 */     this.num2 = false;
/*  31 */     this.num3 = false;
/*  32 */     this.num4 = false;
/*  33 */     this.num5 = false;
/*  34 */     this.num6 = false;
/*     */   }
/*     */   
/*     */   public String isSlot(int slot) {
/*  38 */     if (slot >= 0 && slot < 9 && 
/*  39 */       !isNum1()) {
/*  40 */       setNum1(true);
/*  41 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-1")) {
/*  42 */         String space = MagicCosmetics.getInstance().getMessages().getString("edge.row-1");
/*  43 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/*  44 */           space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */         }
/*  46 */         if (MagicCosmetics.getInstance().isOraxen()) {
/*  47 */           space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */         }
/*  49 */         return space;
/*     */       } 
/*  51 */       return "";
/*     */     } 
/*     */     
/*  54 */     if (slot >= 9 && slot < 18 && 
/*  55 */       !isNum2()) {
/*  56 */       setNum2(true);
/*  57 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-2")) {
/*  58 */         String space = MagicCosmetics.getInstance().getMessages().getString("edge.row-2");
/*  59 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/*  60 */           space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */         }
/*  62 */         if (MagicCosmetics.getInstance().isOraxen()) {
/*  63 */           space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */         }
/*  65 */         return space;
/*     */       } 
/*  67 */       return "";
/*     */     } 
/*     */     
/*  70 */     if (slot >= 18 && slot < 27 && 
/*  71 */       !isNum3()) {
/*  72 */       setNum3(true);
/*  73 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-3")) {
/*  74 */         String str = MagicCosmetics.getInstance().getMessages().getString("edge.row-3");
/*  75 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/*  76 */           str = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(str);
/*     */         }
/*  78 */         if (MagicCosmetics.getInstance().isOraxen()) {
/*  79 */           str = MagicCosmetics.getInstance().getOraxen().replaceFontImages(str);
/*     */         }
/*  81 */         return str;
/*     */       } 
/*  83 */       String space = MagicCosmetics.getInstance().getMessages().getString("edge.space-1");
/*  84 */       if (MagicCosmetics.getInstance().isItemsAdder()) {
/*  85 */         space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */       }
/*  87 */       if (MagicCosmetics.getInstance().isOraxen()) {
/*  88 */         space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */       }
/*  90 */       return space;
/*     */     } 
/*     */     
/*  93 */     if (slot >= 27 && slot < 36 && 
/*  94 */       !isNum4()) {
/*  95 */       setNum4(true);
/*  96 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-4")) {
/*  97 */         String str = MagicCosmetics.getInstance().getMessages().getString("edge.row-4");
/*  98 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/*  99 */           str = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(str);
/*     */         }
/* 101 */         if (MagicCosmetics.getInstance().isOraxen()) {
/* 102 */           str = MagicCosmetics.getInstance().getOraxen().replaceFontImages(str);
/*     */         }
/* 104 */         return str;
/*     */       } 
/* 106 */       String space = MagicCosmetics.getInstance().getMessages().getString("edge.space-2");
/* 107 */       if (MagicCosmetics.getInstance().isItemsAdder()) {
/* 108 */         space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */       }
/* 110 */       if (MagicCosmetics.getInstance().isOraxen()) {
/* 111 */         space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */       }
/* 113 */       return space;
/*     */     } 
/*     */     
/* 116 */     if (slot >= 36 && slot < 45 && 
/* 117 */       !isNum5()) {
/* 118 */       setNum5(true);
/* 119 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-5")) {
/* 120 */         String str = MagicCosmetics.getInstance().getMessages().getString("edge.row-5");
/* 121 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/* 122 */           str = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(str);
/*     */         }
/* 124 */         if (MagicCosmetics.getInstance().isOraxen()) {
/* 125 */           str = MagicCosmetics.getInstance().getOraxen().replaceFontImages(str);
/*     */         }
/* 127 */         return str;
/*     */       } 
/* 129 */       String space = MagicCosmetics.getInstance().getMessages().getString("edge.space-3");
/* 130 */       if (MagicCosmetics.getInstance().isItemsAdder()) {
/* 131 */         space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */       }
/* 133 */       if (MagicCosmetics.getInstance().isOraxen()) {
/* 134 */         space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */       }
/* 136 */       return space;
/*     */     } 
/*     */     
/* 139 */     if (slot >= 45 && slot < 54 && 
/* 140 */       !isNum6()) {
/* 141 */       setNum6(true);
/* 142 */       if (MagicCosmetics.getInstance().getMessages().contains("edge.row-6")) {
/* 143 */         String space = MagicCosmetics.getInstance().getMessages().getString("edge.row-6");
/* 144 */         if (MagicCosmetics.getInstance().isItemsAdder()) {
/* 145 */           space = MagicCosmetics.getInstance().getItemsAdder().replaceFontImages(space);
/*     */         }
/* 147 */         if (MagicCosmetics.getInstance().isOraxen()) {
/* 148 */           space = MagicCosmetics.getInstance().getOraxen().replaceFontImages(space);
/*     */         }
/* 150 */         return space;
/*     */       } 
/* 152 */       return "";
/*     */     } 
/*     */     
/* 155 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isNum1() {
/* 159 */     return this.num1;
/*     */   }
/*     */   
/*     */   public void setNum1(boolean num1) {
/* 163 */     this.num1 = num1;
/*     */   }
/*     */   
/*     */   public boolean isNum2() {
/* 167 */     return this.num2;
/*     */   }
/*     */   
/*     */   public void setNum2(boolean num2) {
/* 171 */     this.num2 = num2;
/*     */   }
/*     */   
/*     */   public boolean isNum3() {
/* 175 */     return this.num3;
/*     */   }
/*     */   
/*     */   public void setNum3(boolean num3) {
/* 179 */     this.num3 = num3;
/*     */   }
/*     */   
/*     */   public boolean isNum4() {
/* 183 */     return this.num4;
/*     */   }
/*     */   
/*     */   public void setNum4(boolean num4) {
/* 187 */     this.num4 = num4;
/*     */   }
/*     */   
/*     */   public boolean isNum5() {
/* 191 */     return this.num5;
/*     */   }
/*     */   
/*     */   public void setNum5(boolean num5) {
/* 195 */     this.num5 = num5;
/*     */   }
/*     */   
/*     */   public boolean isNum6() {
/* 199 */     return this.num6;
/*     */   }
/*     */   
/*     */   public void setNum6(boolean num6) {
/* 203 */     this.num6 = num6;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\Slots.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */