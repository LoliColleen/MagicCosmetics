/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.SubstituteLoggingEvent;
/*     */ import org.slf4j.helpers.NOPServiceProvider;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.SubstituteServiceProvider;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
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
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
/*     */   static final String NO_PROVIDERS_URL = "http://www.slf4j.org/codes.html#noProviders";
/*     */   static final String IGNORED_BINDINGS_URL = "http://www.slf4j.org/codes.html#ignoredBindings";
/*     */   static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
/*     */   static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
/*     */   static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
/*     */   static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  89 */   static volatile int INITIALIZATION_STATE = 0;
/*  90 */   static final SubstituteServiceProvider SUBST_PROVIDER = new SubstituteServiceProvider();
/*  91 */   static final NOPServiceProvider NOP_FALLBACK_FACTORY = new NOPServiceProvider();
/*     */   
/*     */   static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
/*     */   
/*     */   static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
/*     */   
/*  97 */   static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
/*     */   
/*     */   static volatile SLF4JServiceProvider PROVIDER;
/*     */   
/*     */   private static List<SLF4JServiceProvider> findServiceProviders() {
/* 102 */     ServiceLoader<SLF4JServiceProvider> serviceLoader = ServiceLoader.load(SLF4JServiceProvider.class);
/* 103 */     List<SLF4JServiceProvider> providerList = new ArrayList<>();
/* 104 */     for (SLF4JServiceProvider provider : serviceLoader) {
/* 105 */       providerList.add(provider);
/*     */     }
/* 107 */     return providerList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.8", "1.7" };
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
/*     */   static void reset() {
/* 135 */     INITIALIZATION_STATE = 0;
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 139 */     bind();
/* 140 */     if (INITIALIZATION_STATE == 3) {
/* 141 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 147 */       List<SLF4JServiceProvider> providersList = findServiceProviders();
/* 148 */       reportMultipleBindingAmbiguity(providersList);
/* 149 */       if (providersList != null && !providersList.isEmpty()) {
/* 150 */         PROVIDER = providersList.get(0);
/*     */         
/* 152 */         PROVIDER.initialize();
/* 153 */         INITIALIZATION_STATE = 3;
/* 154 */         reportActualBinding(providersList);
/* 155 */         fixSubstituteLoggers();
/* 156 */         replayEvents();
/*     */         
/* 158 */         SUBST_PROVIDER.getSubstituteLoggerFactory().clear();
/*     */       } else {
/* 160 */         INITIALIZATION_STATE = 4;
/* 161 */         Util.report("No SLF4J providers were found.");
/* 162 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 163 */         Util.report("See http://www.slf4j.org/codes.html#noProviders for further details.");
/*     */         
/* 165 */         Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 166 */         reportIgnoredStaticLoggerBinders(staticLoggerBinderPathSet);
/*     */       } 
/* 168 */     } catch (Exception e) {
/* 169 */       failedBinding(e);
/* 170 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportIgnoredStaticLoggerBinders(Set<URL> staticLoggerBinderPathSet) {
/* 175 */     if (staticLoggerBinderPathSet.isEmpty()) {
/*     */       return;
/*     */     }
/* 178 */     Util.report("Class path contains SLF4J bindings targeting slf4j-api versions prior to 1.8.");
/* 179 */     for (URL path : staticLoggerBinderPathSet) {
/* 180 */       Util.report("Ignoring binding found at [" + path + "]");
/*     */     }
/* 182 */     Util.report("See http://www.slf4j.org/codes.html#ignoredBindings for an explanation.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<URL> findPossibleStaticLoggerBinderPathSet() {
/* 195 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<>(); try {
/*     */       Enumeration<URL> paths;
/* 197 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       
/* 199 */       if (loggerFactoryClassLoader == null) {
/* 200 */         paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } else {
/* 202 */         paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } 
/* 204 */       while (paths.hasMoreElements()) {
/* 205 */         URL path = paths.nextElement();
/* 206 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 208 */     } catch (IOException ioe) {
/* 209 */       Util.report("Error getting resources from path", ioe);
/*     */     } 
/* 211 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static void fixSubstituteLoggers() {
/* 215 */     synchronized (SUBST_PROVIDER) {
/* 216 */       SUBST_PROVIDER.getSubstituteLoggerFactory().postInitialization();
/* 217 */       for (SubstituteLogger substLogger : SUBST_PROVIDER.getSubstituteLoggerFactory().getLoggers()) {
/* 218 */         Logger logger = getLogger(substLogger.getName());
/* 219 */         substLogger.setDelegate(logger);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 226 */     INITIALIZATION_STATE = 2;
/* 227 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static void replayEvents() {
/* 231 */     LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_PROVIDER.getSubstituteLoggerFactory().getEventQueue();
/* 232 */     int queueSize = queue.size();
/* 233 */     int count = 0;
/* 234 */     int maxDrain = 128;
/* 235 */     List<SubstituteLoggingEvent> eventList = new ArrayList<>(128);
/*     */     while (true) {
/* 237 */       int numDrained = queue.drainTo(eventList, 128);
/* 238 */       if (numDrained == 0)
/*     */         break; 
/* 240 */       for (SubstituteLoggingEvent event : eventList) {
/* 241 */         replaySingleEvent(event);
/* 242 */         if (count++ == 0)
/* 243 */           emitReplayOrSubstituionWarning(event, queueSize); 
/*     */       } 
/* 245 */       eventList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
/* 250 */     if (event.getLogger().isDelegateEventAware()) {
/* 251 */       emitReplayWarning(queueSize);
/* 252 */     } else if (!event.getLogger().isDelegateNOP()) {
/*     */ 
/*     */       
/* 255 */       emitSubstitutionWarning();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void replaySingleEvent(SubstituteLoggingEvent event) {
/* 260 */     if (event == null) {
/*     */       return;
/*     */     }
/* 263 */     SubstituteLogger substLogger = event.getLogger();
/* 264 */     String loggerName = substLogger.getName();
/* 265 */     if (substLogger.isDelegateNull()) {
/* 266 */       throw new IllegalStateException("Delegate logger cannot be null at this state.");
/*     */     }
/*     */     
/* 269 */     if (!substLogger.isDelegateNOP())
/*     */     {
/* 271 */       if (substLogger.isDelegateEventAware()) {
/* 272 */         substLogger.log((LoggingEvent)event);
/*     */       } else {
/* 274 */         Util.report(loggerName);
/*     */       }  } 
/*     */   }
/*     */   
/*     */   private static void emitSubstitutionWarning() {
/* 279 */     Util.report("The following set of substitute loggers may have been accessed");
/* 280 */     Util.report("during the initialization phase. Logging calls during this");
/* 281 */     Util.report("phase were not honored. However, subsequent logging calls to these");
/* 282 */     Util.report("loggers will work as normally expected.");
/* 283 */     Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
/*     */   }
/*     */   
/*     */   private static void emitReplayWarning(int eventCount) {
/* 287 */     Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
/* 288 */     Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
/* 289 */     Util.report("See also http://www.slf4j.org/codes.html#replay");
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 294 */       String requested = PROVIDER.getRequesteApiVersion();
/*     */       
/* 296 */       boolean match = false;
/* 297 */       for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
/* 298 */         if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
/* 299 */           match = true;
/*     */         }
/*     */       } 
/* 302 */       if (!match) {
/* 303 */         Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + 
/* 304 */             Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/* 305 */         Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 307 */     } catch (NoSuchFieldError noSuchFieldError) {
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 312 */     catch (Throwable e) {
/*     */       
/* 314 */       Util.report("Unexpected problem occured during version sanity check", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousProviderList(List<SLF4JServiceProvider> providerList) {
/* 319 */     return (providerList.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(List<SLF4JServiceProvider> providerList) {
/* 328 */     if (isAmbiguousProviderList(providerList)) {
/* 329 */       Util.report("Class path contains multiple SLF4J providers.");
/* 330 */       for (SLF4JServiceProvider provider : providerList) {
/* 331 */         Util.report("Found provider [" + provider + "]");
/*     */       }
/* 333 */       Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void reportActualBinding(List<SLF4JServiceProvider> providerList) {
/* 339 */     if (!providerList.isEmpty() && isAmbiguousProviderList(providerList)) {
/* 340 */       Util.report("Actual provider is of type [" + providerList.get(0) + "]");
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
/*     */   public static Logger getLogger(String name) {
/* 353 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 354 */     return iLoggerFactory.getLogger(name);
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
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 379 */     Logger logger = getLogger(clazz.getName());
/* 380 */     if (DETECT_LOGGER_NAME_MISMATCH) {
/* 381 */       Class<?> autoComputedCallingClass = Util.getCallingClass();
/* 382 */       if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
/* 383 */         Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass
/* 384 */                 .getName() }));
/* 385 */         Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
/*     */       } 
/*     */     } 
/* 388 */     return logger;
/*     */   }
/*     */   
/*     */   private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
/* 392 */     return !autoComputedCallingClass.isAssignableFrom(clazz);
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
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 404 */     return getProvider().getLoggerFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SLF4JServiceProvider getProvider() {
/* 414 */     if (INITIALIZATION_STATE == 0) {
/* 415 */       synchronized (LoggerFactory.class) {
/* 416 */         if (INITIALIZATION_STATE == 0) {
/* 417 */           INITIALIZATION_STATE = 1;
/* 418 */           performInitialization();
/*     */         } 
/*     */       } 
/*     */     }
/* 422 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 424 */         return PROVIDER;
/*     */       case 4:
/* 426 */         return (SLF4JServiceProvider)NOP_FALLBACK_FACTORY;
/*     */       case 2:
/* 428 */         throw new IllegalStateException("org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 432 */         return (SLF4JServiceProvider)SUBST_PROVIDER;
/*     */     } 
/* 434 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */