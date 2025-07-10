/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FastList<T>
/*     */   implements List<T>, RandomAccess, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4598088075242913858L;
/*     */   private final Class<?> clazz;
/*     */   private T[] elementData;
/*     */   private int size;
/*     */   
/*     */   public FastList(Class<?> clazz) {
/*  54 */     this.elementData = (T[])Array.newInstance(clazz, 32);
/*  55 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastList(Class<?> clazz, int capacity) {
/*  66 */     this.elementData = (T[])Array.newInstance(clazz, capacity);
/*  67 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(T element) {
/*  78 */     if (this.size < this.elementData.length) {
/*  79 */       this.elementData[this.size++] = element;
/*     */     }
/*     */     else {
/*     */       
/*  83 */       int oldCapacity = this.elementData.length;
/*  84 */       int newCapacity = oldCapacity << 1;
/*     */       
/*  86 */       T[] newElementData = (T[])Array.newInstance(this.clazz, newCapacity);
/*  87 */       System.arraycopy(this.elementData, 0, newElementData, 0, oldCapacity);
/*  88 */       newElementData[this.size++] = element;
/*  89 */       this.elementData = newElementData;
/*     */     } 
/*     */     
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(int index) {
/* 104 */     return this.elementData[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T removeLast() {
/* 116 */     T element = this.elementData[--this.size];
/* 117 */     this.elementData[this.size] = null;
/* 118 */     return element;
/*     */   }
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
/*     */   public boolean remove(Object element) {
/* 131 */     for (int index = this.size - 1; index >= 0; index--) {
/* 132 */       if (element == this.elementData[index]) {
/* 133 */         int numMoved = this.size - index - 1;
/* 134 */         if (numMoved > 0) {
/* 135 */           System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
/*     */         }
/* 137 */         this.elementData[--this.size] = null;
/* 138 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 151 */     for (int i = 0; i < this.size; i++) {
/* 152 */       this.elementData[i] = null;
/*     */     }
/*     */     
/* 155 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 166 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 173 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T set(int index, T element) {
/* 180 */     T old = this.elementData[index];
/* 181 */     this.elementData[index] = element;
/* 182 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T remove(int index) {
/* 189 */     if (this.size == 0) {
/* 190 */       return null;
/*     */     }
/*     */     
/* 193 */     T old = this.elementData[index];
/*     */     
/* 195 */     int numMoved = this.size - index - 1;
/* 196 */     if (numMoved > 0) {
/* 197 */       System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
/*     */     }
/*     */     
/* 200 */     this.elementData[--this.size] = null;
/*     */     
/* 202 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 209 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 216 */     return new Iterator<T>()
/*     */       {
/*     */         private int index;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 222 */           return (this.index < FastList.this.size);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public T next() {
/* 228 */           if (this.index < FastList.this.size) {
/* 229 */             return FastList.this.elementData[this.index++];
/*     */           }
/*     */           
/* 232 */           throw new NoSuchElementException("No more elements in FastList");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 241 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> E[] toArray(E[] a) {
/* 248 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 255 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends T> c) {
/* 262 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends T> c) {
/* 269 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 276 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 283 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, T element) {
/* 290 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 297 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 304 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator() {
/* 311 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator(int index) {
/* 318 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<T> subList(int fromIndex, int toIndex) {
/* 325 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 332 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super T> action) {
/* 339 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<T> spliterator() {
/* 346 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super T> filter) {
/* 353 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceAll(UnaryOperator<T> operator) {
/* 360 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sort(Comparator<? super T> c) {
/* 367 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikar\\util\FastList.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */