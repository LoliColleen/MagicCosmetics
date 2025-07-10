/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class UtilityElf
/*     */ {
/*     */   public static String getNullIfEmpty(String text) {
/*  42 */     return (text == null) ? null : (text.trim().isEmpty() ? null : text.trim());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quietlySleep(long millis) {
/*     */     try {
/*  53 */       Thread.sleep(millis);
/*     */     }
/*  55 */     catch (InterruptedException e) {
/*     */       
/*  57 */       Thread.currentThread().interrupt();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean safeIsAssignableFrom(Object obj, String className) {
/*     */     try {
/*  69 */       Class<?> clazz = Class.forName(className);
/*  70 */       return clazz.isAssignableFrom(obj.getClass());
/*  71 */     } catch (ClassNotFoundException ignored) {
/*  72 */       return false;
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public static <T> T createInstance(String className, Class<T> clazz, Object... args) {
/*  88 */     if (className == null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  93 */       Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
/*  94 */       if (args.length == 0) {
/*  95 */         return clazz.cast(loaded.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       }
/*     */       
/*  98 */       Class<?>[] argClasses = new Class[args.length];
/*  99 */       for (int i = 0; i < args.length; i++) {
/* 100 */         argClasses[i] = args[i].getClass();
/*     */       }
/* 102 */       Constructor<?> constructor = loaded.getConstructor(argClasses);
/* 103 */       return clazz.cast(constructor.newInstance(args));
/*     */     }
/* 105 */     catch (Exception e) {
/* 106 */       throw new RuntimeException(e);
/*     */     } 
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
/*     */   
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(int queueSize, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
/* 121 */     if (threadFactory == null) {
/* 122 */       threadFactory = new DefaultThreadFactory(threadName, true);
/*     */     }
/*     */     
/* 125 */     LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
/* 126 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
/* 127 */     executor.allowCoreThreadTimeOut(true);
/* 128 */     return executor;
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
/*     */   
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(BlockingQueue<Runnable> queue, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
/* 142 */     if (threadFactory == null) {
/* 143 */       threadFactory = new DefaultThreadFactory(threadName, true);
/*     */     }
/*     */     
/* 146 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
/* 147 */     executor.allowCoreThreadTimeOut(true);
/* 148 */     return executor;
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
/*     */ 
/*     */   
/*     */   public static int getTransactionIsolation(String transactionIsolationName) {
/* 163 */     if (transactionIsolationName != null) {
/*     */       
/*     */       try {
/* 166 */         String upperCaseIsolationLevelName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
/* 167 */         return IsolationLevel.valueOf(upperCaseIsolationLevelName).getLevelId();
/* 168 */       } catch (IllegalArgumentException e) {
/*     */         
/*     */         try {
/* 171 */           int level = Integer.parseInt(transactionIsolationName);
/* 172 */           for (IsolationLevel iso : IsolationLevel.values()) {
/* 173 */             if (iso.getLevelId() == level) {
/* 174 */               return iso.getLevelId();
/*     */             }
/*     */           } 
/*     */           
/* 178 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
/*     */         }
/* 180 */         catch (NumberFormatException nfe) {
/* 181 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName, nfe);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 186 */     return -1;
/*     */   }
/*     */   
/*     */   public static final class DefaultThreadFactory
/*     */     implements ThreadFactory {
/*     */     private final String threadName;
/*     */     private final boolean daemon;
/*     */     
/*     */     public DefaultThreadFactory(String threadName, boolean daemon) {
/* 195 */       this.threadName = threadName;
/* 196 */       this.daemon = daemon;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Thread newThread(Runnable r) {
/* 202 */       Thread thread = new Thread(r, this.threadName);
/* 203 */       thread.setDaemon(this.daemon);
/* 204 */       return thread;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikar\\util\UtilityElf.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */