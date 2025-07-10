/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.Level;
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
/*     */ public abstract class AbstractLogger
/*     */   implements Logger, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2529255052481744503L;
/*     */   protected String name;
/*     */   
/*     */   public String getName() {
/*  49 */     return this.name;
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
/*     */   protected Object readResolve() throws ObjectStreamException {
/*  69 */     return LoggerFactory.getLogger(getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/*  74 */     if (isTraceEnabled()) {
/*  75 */       handle_0ArgsCall(Level.TRACE, (Marker)null, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg) {
/*  81 */     if (isTraceEnabled()) {
/*  82 */       handle_1ArgsCall(Level.TRACE, (Marker)null, format, arg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg1, Object arg2) {
/*  88 */     if (isTraceEnabled()) {
/*  89 */       handle2ArgsCall(Level.TRACE, (Marker)null, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object... arguments) {
/*  95 */     if (isTraceEnabled()) {
/*  96 */       handleArgArrayCall(Level.TRACE, (Marker)null, format, arguments);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 102 */     if (isTraceEnabled()) {
/* 103 */       handle_0ArgsCall(Level.TRACE, (Marker)null, msg, t);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String msg) {
/* 109 */     if (isTraceEnabled(marker)) {
/* 110 */       handle_0ArgsCall(Level.TRACE, marker, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg) {
/* 116 */     if (isTraceEnabled(marker)) {
/* 117 */       handle_1ArgsCall(Level.TRACE, marker, format, arg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg1, Object arg2) {
/* 123 */     if (isTraceEnabled(marker)) {
/* 124 */       handle2ArgsCall(Level.TRACE, marker, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String format, Object... argArray) {
/* 130 */     if (isTraceEnabled(marker)) {
/* 131 */       handleArgArrayCall(Level.TRACE, marker, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg, Throwable t) {
/* 136 */     if (isTraceEnabled(marker)) {
/* 137 */       handle_0ArgsCall(Level.TRACE, marker, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String msg) {
/* 142 */     if (isDebugEnabled()) {
/* 143 */       handle_0ArgsCall(Level.DEBUG, (Marker)null, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg) {
/* 148 */     if (isDebugEnabled()) {
/* 149 */       handle_1ArgsCall(Level.DEBUG, (Marker)null, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 154 */     if (isDebugEnabled()) {
/* 155 */       handle2ArgsCall(Level.DEBUG, (Marker)null, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object... arguments) {
/* 160 */     if (isDebugEnabled()) {
/* 161 */       handleArgArrayCall(Level.DEBUG, (Marker)null, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 166 */     if (isDebugEnabled()) {
/* 167 */       handle_0ArgsCall(Level.DEBUG, (Marker)null, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg) {
/* 172 */     if (isDebugEnabled(marker)) {
/* 173 */       handle_0ArgsCall(Level.DEBUG, marker, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg) {
/* 178 */     if (isDebugEnabled(marker)) {
/* 179 */       handle_1ArgsCall(Level.DEBUG, marker, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg1, Object arg2) {
/* 184 */     if (isDebugEnabled(marker)) {
/* 185 */       handle2ArgsCall(Level.DEBUG, marker, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object... arguments) {
/* 190 */     if (isDebugEnabled(marker)) {
/* 191 */       handleArgArrayCall(Level.DEBUG, marker, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg, Throwable t) {
/* 196 */     if (isDebugEnabled(marker)) {
/* 197 */       handle_0ArgsCall(Level.DEBUG, marker, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String msg) {
/* 202 */     if (isInfoEnabled()) {
/* 203 */       handle_0ArgsCall(Level.INFO, (Marker)null, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg) {
/* 208 */     if (isInfoEnabled()) {
/* 209 */       handle_1ArgsCall(Level.INFO, (Marker)null, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 214 */     if (isInfoEnabled()) {
/* 215 */       handle2ArgsCall(Level.INFO, (Marker)null, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object... arguments) {
/* 220 */     if (isInfoEnabled()) {
/* 221 */       handleArgArrayCall(Level.INFO, (Marker)null, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 226 */     if (isInfoEnabled()) {
/* 227 */       handle_0ArgsCall(Level.INFO, (Marker)null, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg) {
/* 232 */     if (isInfoEnabled(marker)) {
/* 233 */       handle_0ArgsCall(Level.INFO, marker, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg) {
/* 238 */     if (isInfoEnabled(marker)) {
/* 239 */       handle_1ArgsCall(Level.INFO, marker, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg1, Object arg2) {
/* 244 */     if (isInfoEnabled(marker)) {
/* 245 */       handle2ArgsCall(Level.INFO, marker, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object... arguments) {
/* 250 */     if (isInfoEnabled(marker)) {
/* 251 */       handleArgArrayCall(Level.INFO, marker, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg, Throwable t) {
/* 256 */     if (isInfoEnabled(marker)) {
/* 257 */       handle_0ArgsCall(Level.INFO, marker, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String msg) {
/* 262 */     if (isWarnEnabled()) {
/* 263 */       handle_0ArgsCall(Level.WARN, (Marker)null, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 268 */     if (isWarnEnabled()) {
/* 269 */       handle_1ArgsCall(Level.WARN, (Marker)null, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 274 */     if (isWarnEnabled()) {
/* 275 */       handle2ArgsCall(Level.WARN, (Marker)null, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object... arguments) {
/* 280 */     if (isWarnEnabled()) {
/* 281 */       handleArgArrayCall(Level.WARN, (Marker)null, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 286 */     if (isWarnEnabled()) {
/* 287 */       handle_0ArgsCall(Level.WARN, (Marker)null, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg) {
/* 292 */     if (isWarnEnabled(marker)) {
/* 293 */       handle_0ArgsCall(Level.WARN, marker, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg) {
/* 298 */     if (isWarnEnabled(marker)) {
/* 299 */       handle_1ArgsCall(Level.WARN, marker, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 304 */     if (isWarnEnabled(marker)) {
/* 305 */       handle2ArgsCall(Level.WARN, marker, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object... arguments) {
/* 310 */     if (isWarnEnabled(marker)) {
/* 311 */       handleArgArrayCall(Level.WARN, marker, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg, Throwable t) {
/* 316 */     if (isWarnEnabled(marker)) {
/* 317 */       handle_0ArgsCall(Level.WARN, marker, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String msg) {
/* 322 */     if (isErrorEnabled()) {
/* 323 */       handle_0ArgsCall(Level.ERROR, (Marker)null, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg) {
/* 328 */     if (isErrorEnabled()) {
/* 329 */       handle_1ArgsCall(Level.ERROR, (Marker)null, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 334 */     if (isErrorEnabled()) {
/* 335 */       handle2ArgsCall(Level.ERROR, (Marker)null, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object... arguments) {
/* 340 */     if (isErrorEnabled()) {
/* 341 */       handleArgArrayCall(Level.ERROR, (Marker)null, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 346 */     if (isErrorEnabled()) {
/* 347 */       handle_0ArgsCall(Level.ERROR, (Marker)null, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg) {
/* 352 */     if (isErrorEnabled(marker)) {
/* 353 */       handle_0ArgsCall(Level.ERROR, marker, msg, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg) {
/* 358 */     if (isErrorEnabled(marker)) {
/* 359 */       handle_1ArgsCall(Level.ERROR, marker, format, arg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg1, Object arg2) {
/* 364 */     if (isErrorEnabled(marker)) {
/* 365 */       handle2ArgsCall(Level.ERROR, marker, format, arg1, arg2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object... arguments) {
/* 370 */     if (isErrorEnabled(marker)) {
/* 371 */       handleArgArrayCall(Level.ERROR, marker, format, arguments);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg, Throwable t) {
/* 376 */     if (isErrorEnabled(marker)) {
/* 377 */       handle_0ArgsCall(Level.ERROR, marker, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handle_0ArgsCall(Level level, Marker marker, String msg, Throwable t) {
/* 382 */     handleNormalizedLoggingCall(level, marker, msg, (Object[])null, t);
/*     */   }
/*     */   
/*     */   private void handle_1ArgsCall(Level level, Marker marker, String msg, Object arg1) {
/* 386 */     handleNormalizedLoggingCall(level, marker, msg, new Object[] { arg1 }, (Throwable)null);
/*     */   }
/*     */   
/*     */   private void handle2ArgsCall(Level level, Marker marker, String msg, Object arg1, Object arg2) {
/* 390 */     if (arg2 instanceof Throwable) {
/* 391 */       handleNormalizedLoggingCall(level, marker, msg, new Object[] { arg1 }, (Throwable)arg2);
/*     */     } else {
/* 393 */       handleNormalizedLoggingCall(level, marker, msg, new Object[] { arg1, arg2 }, (Throwable)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleArgArrayCall(Level level, Marker marker, String msg, Object[] args) {
/* 398 */     Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(args);
/* 399 */     if (throwableCandidate != null) {
/* 400 */       Object[] trimmedCopy = MessageFormatter.trimmedCopy(args);
/* 401 */       handleNormalizedLoggingCall(level, marker, msg, trimmedCopy, throwableCandidate);
/*     */     } else {
/* 403 */       handleNormalizedLoggingCall(level, marker, msg, args, (Throwable)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract String getFullyQualifiedCallerName();
/*     */   
/*     */   protected abstract void handleNormalizedLoggingCall(Level paramLevel, Marker paramMarker, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable);
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\helpers\AbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */