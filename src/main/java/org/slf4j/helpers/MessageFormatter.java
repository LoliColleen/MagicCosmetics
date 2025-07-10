/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg) {
/* 124 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */   public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
/* 151 */     return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
/* 156 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/* 157 */     Object[] args = argArray;
/* 158 */     if (throwableCandidate != null) {
/* 159 */       args = trimmedCopy(argArray);
/*     */     }
/* 161 */     return arrayFormat(messagePattern, args, throwableCandidate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String basicArrayFormat(String messagePattern, Object[] argArray) {
/* 171 */     FormattingTuple ft = arrayFormat(messagePattern, argArray, null);
/* 172 */     return ft.getMessage();
/*     */   }
/*     */   
/*     */   public static String basicArrayFormat(NormalizedParameters np) {
/* 176 */     return basicArrayFormat(np.getMessage(), np.getArguments());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
/* 181 */     if (messagePattern == null) {
/* 182 */       return new FormattingTuple(null, argArray, throwable);
/*     */     }
/*     */     
/* 185 */     if (argArray == null) {
/* 186 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 189 */     int i = 0;
/*     */ 
/*     */     
/* 192 */     StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
/*     */ 
/*     */     
/* 195 */     for (int L = 0; L < argArray.length; L++) {
/*     */       
/* 197 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 199 */       if (j == -1) {
/*     */         
/* 201 */         if (i == 0) {
/* 202 */           return new FormattingTuple(messagePattern, argArray, throwable);
/*     */         }
/*     */         
/* 205 */         sbuf.append(messagePattern, i, messagePattern.length());
/* 206 */         return new FormattingTuple(sbuf.toString(), argArray, throwable);
/*     */       } 
/*     */       
/* 209 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 210 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 211 */           L--;
/* 212 */           sbuf.append(messagePattern, i, j - 1);
/* 213 */           sbuf.append('{');
/* 214 */           i = j + 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 219 */           sbuf.append(messagePattern, i, j - 1);
/* 220 */           deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<>());
/* 221 */           i = j + 2;
/*     */         } 
/*     */       } else {
/*     */         
/* 225 */         sbuf.append(messagePattern, i, j);
/* 226 */         deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<>());
/* 227 */         i = j + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 232 */     sbuf.append(messagePattern, i, messagePattern.length());
/* 233 */     return new FormattingTuple(sbuf.toString(), argArray, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
/* 238 */     if (delimeterStartIndex == 0) {
/* 239 */       return false;
/*     */     }
/* 241 */     char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
/* 242 */     if (potentialEscape == '\\') {
/* 243 */       return true;
/*     */     }
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
/* 250 */     if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
/* 251 */       return true;
/*     */     }
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
/* 259 */     if (o == null) {
/* 260 */       sbuf.append("null");
/*     */       return;
/*     */     } 
/* 263 */     if (!o.getClass().isArray()) {
/* 264 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */     
/*     */     }
/* 268 */     else if (o instanceof boolean[]) {
/* 269 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 270 */     } else if (o instanceof byte[]) {
/* 271 */       byteArrayAppend(sbuf, (byte[])o);
/* 272 */     } else if (o instanceof char[]) {
/* 273 */       charArrayAppend(sbuf, (char[])o);
/* 274 */     } else if (o instanceof short[]) {
/* 275 */       shortArrayAppend(sbuf, (short[])o);
/* 276 */     } else if (o instanceof int[]) {
/* 277 */       intArrayAppend(sbuf, (int[])o);
/* 278 */     } else if (o instanceof long[]) {
/* 279 */       longArrayAppend(sbuf, (long[])o);
/* 280 */     } else if (o instanceof float[]) {
/* 281 */       floatArrayAppend(sbuf, (float[])o);
/* 282 */     } else if (o instanceof double[]) {
/* 283 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 285 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void safeObjectAppend(StringBuilder sbuf, Object o) {
/*     */     try {
/* 292 */       String oAsString = o.toString();
/* 293 */       sbuf.append(oAsString);
/* 294 */     } catch (Throwable t) {
/* 295 */       Util.report("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
/* 296 */       sbuf.append("[FAILED toString()]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
/* 302 */     sbuf.append('[');
/* 303 */     if (!seenMap.containsKey(a)) {
/* 304 */       seenMap.put(a, null);
/* 305 */       int len = a.length;
/* 306 */       for (int i = 0; i < len; i++) {
/* 307 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 308 */         if (i != len - 1) {
/* 309 */           sbuf.append(", ");
/*     */         }
/*     */       } 
/* 312 */       seenMap.remove(a);
/*     */     } else {
/* 314 */       sbuf.append("...");
/*     */     } 
/* 316 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
/* 320 */     sbuf.append('[');
/* 321 */     int len = a.length;
/* 322 */     for (int i = 0; i < len; i++) {
/* 323 */       sbuf.append(a[i]);
/* 324 */       if (i != len - 1)
/* 325 */         sbuf.append(", "); 
/*     */     } 
/* 327 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
/* 331 */     sbuf.append('[');
/* 332 */     int len = a.length;
/* 333 */     for (int i = 0; i < len; i++) {
/* 334 */       sbuf.append(a[i]);
/* 335 */       if (i != len - 1)
/* 336 */         sbuf.append(", "); 
/*     */     } 
/* 338 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuilder sbuf, char[] a) {
/* 342 */     sbuf.append('[');
/* 343 */     int len = a.length;
/* 344 */     for (int i = 0; i < len; i++) {
/* 345 */       sbuf.append(a[i]);
/* 346 */       if (i != len - 1)
/* 347 */         sbuf.append(", "); 
/*     */     } 
/* 349 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
/* 353 */     sbuf.append('[');
/* 354 */     int len = a.length;
/* 355 */     for (int i = 0; i < len; i++) {
/* 356 */       sbuf.append(a[i]);
/* 357 */       if (i != len - 1)
/* 358 */         sbuf.append(", "); 
/*     */     } 
/* 360 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuilder sbuf, int[] a) {
/* 364 */     sbuf.append('[');
/* 365 */     int len = a.length;
/* 366 */     for (int i = 0; i < len; i++) {
/* 367 */       sbuf.append(a[i]);
/* 368 */       if (i != len - 1)
/* 369 */         sbuf.append(", "); 
/*     */     } 
/* 371 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuilder sbuf, long[] a) {
/* 375 */     sbuf.append('[');
/* 376 */     int len = a.length;
/* 377 */     for (int i = 0; i < len; i++) {
/* 378 */       sbuf.append(a[i]);
/* 379 */       if (i != len - 1)
/* 380 */         sbuf.append(", "); 
/*     */     } 
/* 382 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
/* 386 */     sbuf.append('[');
/* 387 */     int len = a.length;
/* 388 */     for (int i = 0; i < len; i++) {
/* 389 */       sbuf.append(a[i]);
/* 390 */       if (i != len - 1)
/* 391 */         sbuf.append(", "); 
/*     */     } 
/* 393 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
/* 397 */     sbuf.append('[');
/* 398 */     int len = a.length;
/* 399 */     for (int i = 0; i < len; i++) {
/* 400 */       sbuf.append(a[i]);
/* 401 */       if (i != len - 1)
/* 402 */         sbuf.append(", "); 
/*     */     } 
/* 404 */     sbuf.append(']');
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
/*     */   public static Throwable getThrowableCandidate(Object[] argArray) {
/* 416 */     return NormalizedParameters.getThrowableCandidate(argArray);
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
/*     */   public static Object[] trimmedCopy(Object[] argArray) {
/* 428 */     return NormalizedParameters.trimmedCopy(argArray);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\MessageFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */