/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NormalizedParameters
/*     */ {
/*     */   final String message;
/*     */   final Object[] arguments;
/*     */   final Throwable throwable;
/*     */   
/*     */   public NormalizedParameters(String message, Object[] arguments, Throwable throwable) {
/*  20 */     this.message = message;
/*  21 */     this.arguments = arguments;
/*  22 */     this.throwable = throwable;
/*     */   }
/*     */   
/*     */   public NormalizedParameters(String message, Object[] arguments) {
/*  26 */     this(message, arguments, null);
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  30 */     return this.message;
/*     */   }
/*     */   
/*     */   public Object[] getArguments() {
/*  34 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public Throwable getThrowable() {
/*  38 */     return this.throwable;
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
/*     */   public static Throwable getThrowableCandidate(Object[] argArray) {
/*  51 */     if (argArray == null || argArray.length == 0) {
/*  52 */       return null;
/*     */     }
/*     */     
/*  55 */     Object lastEntry = argArray[argArray.length - 1];
/*  56 */     if (lastEntry instanceof Throwable) {
/*  57 */       return (Throwable)lastEntry;
/*     */     }
/*     */     
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] trimmedCopy(Object[] argArray) {
/*  71 */     if (argArray == null || argArray.length == 0) {
/*  72 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*     */     }
/*     */     
/*  75 */     int trimmedLen = argArray.length - 1;
/*     */     
/*  77 */     Object[] trimmed = new Object[trimmedLen];
/*     */     
/*  79 */     if (trimmedLen > 0) {
/*  80 */       System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
/*     */     }
/*     */     
/*  83 */     return trimmed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NormalizedParameters normalize(String msg, Object[] arguments, Throwable t) {
/*  94 */     if (t != null) {
/*  95 */       return new NormalizedParameters(msg, arguments, t);
/*     */     }
/*     */     
/*  98 */     if (arguments == null || arguments.length == 0) {
/*  99 */       return new NormalizedParameters(msg, arguments, t);
/*     */     }
/*     */     
/* 102 */     Throwable throwableCandidate = getThrowableCandidate(arguments);
/* 103 */     if (throwableCandidate != null) {
/* 104 */       Object[] trimmedArguments = MessageFormatter.trimmedCopy(arguments);
/* 105 */       return new NormalizedParameters(msg, trimmedArguments, throwableCandidate);
/*     */     } 
/* 107 */     return new NormalizedParameters(msg, arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static NormalizedParameters normalize(LoggingEvent event) {
/* 113 */     return normalize(event.getMessage(), event.getArgumentArray(), event.getThrowable());
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\NormalizedParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */