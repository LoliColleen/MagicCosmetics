package org.slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.event.LoggingEvent;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.NOPServiceProvider;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteServiceProvider;
import org.slf4j.helpers.Util;
import org.slf4j.spi.SLF4JServiceProvider;

public final class LoggerFactory {
  static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
  
  static final String NO_PROVIDERS_URL = "http://www.slf4j.org/codes.html#noProviders";
  
  static final String IGNORED_BINDINGS_URL = "http://www.slf4j.org/codes.html#ignoredBindings";
  
  static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
  
  static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
  
  static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
  
  static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
  
  static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
  
  static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
  
  static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
  
  static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
  
  static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
  
  static final int UNINITIALIZED = 0;
  
  static final int ONGOING_INITIALIZATION = 1;
  
  static final int FAILED_INITIALIZATION = 2;
  
  static final int SUCCESSFUL_INITIALIZATION = 3;
  
  static final int NOP_FALLBACK_INITIALIZATION = 4;
  
  static volatile int INITIALIZATION_STATE = 0;
  
  static final SubstituteServiceProvider SUBST_PROVIDER = new SubstituteServiceProvider();
  
  static final NOPServiceProvider NOP_FALLBACK_FACTORY = new NOPServiceProvider();
  
  static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
  
  static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
  
  static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
  
  static volatile SLF4JServiceProvider PROVIDER;
  
  private static List<SLF4JServiceProvider> findServiceProviders() {
    ServiceLoader<SLF4JServiceProvider> serviceLoader = ServiceLoader.load(SLF4JServiceProvider.class);
    List<SLF4JServiceProvider> providerList = new ArrayList<>();
    for (SLF4JServiceProvider provider : serviceLoader)
      providerList.add(provider); 
    return providerList;
  }
  
  private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.8", "1.7" };
  
  static void reset() {
    INITIALIZATION_STATE = 0;
  }
  
  private static final void performInitialization() {
    bind();
    if (INITIALIZATION_STATE == 3)
      versionSanityCheck(); 
  }
  
  private static final void bind() {
    try {
      List<SLF4JServiceProvider> providersList = findServiceProviders();
      reportMultipleBindingAmbiguity(providersList);
      if (providersList != null && !providersList.isEmpty()) {
        PROVIDER = providersList.get(0);
        PROVIDER.initialize();
        INITIALIZATION_STATE = 3;
        reportActualBinding(providersList);
        fixSubstituteLoggers();
        replayEvents();
        SUBST_PROVIDER.getSubstituteLoggerFactory().clear();
      } else {
        INITIALIZATION_STATE = 4;
        Util.report("No SLF4J providers were found.");
        Util.report("Defaulting to no-operation (NOP) logger implementation");
        Util.report("See http://www.slf4j.org/codes.html#noProviders for further details.");
        Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
        reportIgnoredStaticLoggerBinders(staticLoggerBinderPathSet);
      } 
    } catch (Exception e) {
      failedBinding(e);
      throw new IllegalStateException("Unexpected initialization failure", e);
    } 
  }
  
  private static void reportIgnoredStaticLoggerBinders(Set<URL> staticLoggerBinderPathSet) {
    if (staticLoggerBinderPathSet.isEmpty())
      return; 
    Util.report("Class path contains SLF4J bindings targeting slf4j-api versions prior to 1.8.");
    for (URL path : staticLoggerBinderPathSet)
      Util.report("Ignoring binding found at [" + path + "]"); 
    Util.report("See http://www.slf4j.org/codes.html#ignoredBindings for an explanation.");
  }
  
  private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
  
  static Set<URL> findPossibleStaticLoggerBinderPathSet() {
    Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<>();
    try {
      Enumeration<URL> paths;
      ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
      if (loggerFactoryClassLoader == null) {
        paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
      } else {
        paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
      } 
      while (paths.hasMoreElements()) {
        URL path = paths.nextElement();
        staticLoggerBinderPathSet.add(path);
      } 
    } catch (IOException ioe) {
      Util.report("Error getting resources from path", ioe);
    } 
    return staticLoggerBinderPathSet;
  }
  
  private static void fixSubstituteLoggers() {
    synchronized (SUBST_PROVIDER) {
      SUBST_PROVIDER.getSubstituteLoggerFactory().postInitialization();
      for (SubstituteLogger substLogger : SUBST_PROVIDER.getSubstituteLoggerFactory().getLoggers()) {
        Logger logger = getLogger(substLogger.getName());
        substLogger.setDelegate(logger);
      } 
    } 
  }
  
  static void failedBinding(Throwable t) {
    INITIALIZATION_STATE = 2;
    Util.report("Failed to instantiate SLF4J LoggerFactory", t);
  }
  
  private static void replayEvents() {
    LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_PROVIDER.getSubstituteLoggerFactory().getEventQueue();
    int queueSize = queue.size();
    int count = 0;
    int maxDrain = 128;
    List<SubstituteLoggingEvent> eventList = new ArrayList<>(128);
    while (true) {
      int numDrained = queue.drainTo(eventList, 128);
      if (numDrained == 0)
        break; 
      for (SubstituteLoggingEvent event : eventList) {
        replaySingleEvent(event);
        if (count++ == 0)
          emitReplayOrSubstituionWarning(event, queueSize); 
      } 
      eventList.clear();
    } 
  }
  
  private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
    if (event.getLogger().isDelegateEventAware()) {
      emitReplayWarning(queueSize);
    } else if (!event.getLogger().isDelegateNOP()) {
      emitSubstitutionWarning();
    } 
  }
  
  private static void replaySingleEvent(SubstituteLoggingEvent event) {
    if (event == null)
      return; 
    SubstituteLogger substLogger = event.getLogger();
    String loggerName = substLogger.getName();
    if (substLogger.isDelegateNull())
      throw new IllegalStateException("Delegate logger cannot be null at this state."); 
    if (!substLogger.isDelegateNOP())
      if (substLogger.isDelegateEventAware()) {
        substLogger.log((LoggingEvent)event);
      } else {
        Util.report(loggerName);
      }  
  }
  
  private static void emitSubstitutionWarning() {
    Util.report("The following set of substitute loggers may have been accessed");
    Util.report("during the initialization phase. Logging calls during this");
    Util.report("phase were not honored. However, subsequent logging calls to these");
    Util.report("loggers will work as normally expected.");
    Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
  }
  
  private static void emitReplayWarning(int eventCount) {
    Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
    Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
    Util.report("See also http://www.slf4j.org/codes.html#replay");
  }
  
  private static final void versionSanityCheck() {
    try {
      String requested = PROVIDER.getRequesteApiVersion();
      boolean match = false;
      for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
        if (requested.startsWith(aAPI_COMPATIBILITY_LIST))
          match = true; 
      } 
      if (!match) {
        Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + 
            Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
        Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
      } 
    } catch (NoSuchFieldError noSuchFieldError) {
    
    } catch (Throwable e) {
      Util.report("Unexpected problem occured during version sanity check", e);
    } 
  }
  
  private static boolean isAmbiguousProviderList(List<SLF4JServiceProvider> providerList) {
    return (providerList.size() > 1);
  }
  
  private static void reportMultipleBindingAmbiguity(List<SLF4JServiceProvider> providerList) {
    if (isAmbiguousProviderList(providerList)) {
      Util.report("Class path contains multiple SLF4J providers.");
      for (SLF4JServiceProvider provider : providerList)
        Util.report("Found provider [" + provider + "]"); 
      Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
    } 
  }
  
  private static void reportActualBinding(List<SLF4JServiceProvider> providerList) {
    if (!providerList.isEmpty() && isAmbiguousProviderList(providerList))
      Util.report("Actual provider is of type [" + providerList.get(0) + "]"); 
  }
  
  public static Logger getLogger(String name) {
    ILoggerFactory iLoggerFactory = getILoggerFactory();
    return iLoggerFactory.getLogger(name);
  }
  
  public static Logger getLogger(Class<?> clazz) {
    Logger logger = getLogger(clazz.getName());
    if (DETECT_LOGGER_NAME_MISMATCH) {
      Class<?> autoComputedCallingClass = Util.getCallingClass();
      if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
        Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass
                .getName() }));
        Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
      } 
    } 
    return logger;
  }
  
  private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
    return !autoComputedCallingClass.isAssignableFrom(clazz);
  }
  
  public static ILoggerFactory getILoggerFactory() {
    return getProvider().getLoggerFactory();
  }
  
  static SLF4JServiceProvider getProvider() {
    if (INITIALIZATION_STATE == 0)
      synchronized (LoggerFactory.class) {
        if (INITIALIZATION_STATE == 0) {
          INITIALIZATION_STATE = 1;
          performInitialization();
        } 
      }  
    switch (INITIALIZATION_STATE) {
      case 3:
        return PROVIDER;
      case 4:
        return (SLF4JServiceProvider)NOP_FALLBACK_FACTORY;
      case 2:
        throw new IllegalStateException("org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
      case 1:
        return (SLF4JServiceProvider)SUBST_PROVIDER;
    } 
    throw new IllegalStateException("Unreachable code");
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */