/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertyElf
/*     */ {
/*  33 */   private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTargetFromProperties(Object target, Properties properties) {
/*  41 */     if (target == null || properties == null) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     List<Method> methods = Arrays.asList(target.getClass().getMethods());
/*  46 */     properties.forEach((key, value) -> {
/*     */           if (target instanceof HikariConfig && key.toString().startsWith("dataSource.")) {
/*     */             ((HikariConfig)target).addDataSourceProperty(key.toString().substring("dataSource.".length()), value);
/*     */           } else {
/*     */             setProperty(target, key.toString(), value, methods);
/*     */           } 
/*     */         });
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
/*     */   public static Set<String> getPropertyNames(Class<?> targetClass) {
/*  64 */     HashSet<String> set = new HashSet<>();
/*  65 */     Matcher matcher = GETTER_PATTERN.matcher("");
/*  66 */     for (Method method : targetClass.getMethods()) {
/*  67 */       String name = method.getName();
/*  68 */       if ((method.getParameterTypes()).length == 0 && matcher.reset(name).matches()) {
/*  69 */         name = name.replaceFirst("(get|is)", "");
/*     */         try {
/*  71 */           if (targetClass.getMethod("set" + name, new Class[] { method.getReturnType() }) != null) {
/*  72 */             name = "" + Character.toLowerCase(name.charAt(0)) + Character.toLowerCase(name.charAt(0));
/*  73 */             set.add(name);
/*     */           }
/*     */         
/*  76 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  82 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getProperty(String propName, Object target) {
/*     */     try {
/*  89 */       String capitalized = "get" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
/*  90 */       Method method = target.getClass().getMethod(capitalized, new Class[0]);
/*  91 */       return method.invoke(target, new Object[0]);
/*     */     }
/*  93 */     catch (Exception e) {
/*     */       try {
/*  95 */         String capitalized = "is" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
/*  96 */         Method method = target.getClass().getMethod(capitalized, new Class[0]);
/*  97 */         return method.invoke(target, new Object[0]);
/*     */       }
/*  99 */       catch (Exception e2) {
/* 100 */         return null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Properties copyProperties(Properties props) {
/* 107 */     Properties copy = new Properties();
/* 108 */     props.forEach((key, value) -> copy.setProperty(key.toString(), value.toString()));
/* 109 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setProperty(Object target, String propName, Object propValue, List<Method> methods) {
/* 114 */     Logger logger = LoggerFactory.getLogger(PropertyElf.class);
/*     */ 
/*     */     
/* 117 */     String methodName = "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
/* 118 */     Method writeMethod = methods.stream().filter(m -> (m.getName().equals(methodName) && m.getParameterCount() == 1)).findFirst().orElse(null);
/*     */     
/* 120 */     if (writeMethod == null) {
/* 121 */       String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
/* 122 */       writeMethod = methods.stream().filter(m -> (m.getName().equals(methodName2) && m.getParameterCount() == 1)).findFirst().orElse(null);
/*     */     } 
/*     */     
/* 125 */     if (writeMethod == null) {
/* 126 */       logger.error("Property {} does not exist on target {}", propName, target.getClass());
/* 127 */       throw new RuntimeException(String.format("Property %s does not exist on target %s", new Object[] { propName, target.getClass() }));
/*     */     } 
/*     */     
/*     */     try {
/* 131 */       Class<?> paramClass = writeMethod.getParameterTypes()[0];
/* 132 */       if (paramClass == int.class) {
/* 133 */         writeMethod.invoke(target, new Object[] { Integer.valueOf(Integer.parseInt(propValue.toString())) });
/*     */       }
/* 135 */       else if (paramClass == long.class) {
/* 136 */         writeMethod.invoke(target, new Object[] { Long.valueOf(Long.parseLong(propValue.toString())) });
/*     */       }
/* 138 */       else if (paramClass == short.class) {
/* 139 */         writeMethod.invoke(target, new Object[] { Short.valueOf(Short.parseShort(propValue.toString())) });
/*     */       }
/* 141 */       else if (paramClass == boolean.class || paramClass == Boolean.class) {
/* 142 */         writeMethod.invoke(target, new Object[] { Boolean.valueOf(Boolean.parseBoolean(propValue.toString())) });
/*     */       }
/* 144 */       else if (paramClass == String.class) {
/* 145 */         writeMethod.invoke(target, new Object[] { propValue.toString() });
/*     */       } else {
/*     */         
/*     */         try {
/* 149 */           logger.debug("Try to create a new instance of \"{}\"", propValue);
/* 150 */           writeMethod.invoke(target, new Object[] { Class.forName(propValue.toString()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]) });
/*     */         }
/* 152 */         catch (InstantiationException|ClassNotFoundException e) {
/* 153 */           logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", propValue);
/* 154 */           writeMethod.invoke(target, new Object[] { propValue });
/*     */         }
/*     */       
/*     */       } 
/* 158 */     } catch (Exception e) {
/* 159 */       logger.error("Failed to set property {} on target {}", new Object[] { propName, target.getClass(), e });
/* 160 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikar\\util\PropertyElf.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */