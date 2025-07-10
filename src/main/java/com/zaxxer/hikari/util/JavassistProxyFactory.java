/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import com.zaxxer.hikari.pool.ProxyCallableStatement;
/*     */ import com.zaxxer.hikari.pool.ProxyConnection;
/*     */ import com.zaxxer.hikari.pool.ProxyDatabaseMetaData;
/*     */ import com.zaxxer.hikari.pool.ProxyPreparedStatement;
/*     */ import com.zaxxer.hikari.pool.ProxyResultSet;
/*     */ import com.zaxxer.hikari.pool.ProxyStatement;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.LoaderClassPath;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JavassistProxyFactory
/*     */ {
/*     */   private static ClassPool classPool;
/*  42 */   private static String genDirectory = "";
/*     */   
/*     */   public static void main(String... args) throws Exception {
/*  45 */     classPool = new ClassPool();
/*  46 */     classPool.importPackage("java.sql");
/*  47 */     classPool.appendClassPath((ClassPath)new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
/*     */     
/*  49 */     if (args.length > 0) {
/*  50 */       genDirectory = args[0];
/*     */     }
/*     */ 
/*     */     
/*  54 */     String methodBody = "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }";
/*  55 */     generateProxyClass(Connection.class, ProxyConnection.class.getName(), methodBody);
/*  56 */     generateProxyClass(Statement.class, ProxyStatement.class.getName(), methodBody);
/*  57 */     generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), methodBody);
/*  58 */     generateProxyClass(DatabaseMetaData.class, ProxyDatabaseMetaData.class.getName(), methodBody);
/*     */ 
/*     */     
/*  61 */     methodBody = "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }";
/*  62 */     generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), methodBody);
/*  63 */     generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), methodBody);
/*     */     
/*  65 */     modifyProxyFactory();
/*     */   }
/*     */   
/*     */   private static void modifyProxyFactory() throws NotFoundException, CannotCompileException, IOException {
/*  69 */     System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
/*     */     
/*  71 */     String packageName = ProxyConnection.class.getPackage().getName();
/*  72 */     CtClass proxyCt = classPool.getCtClass("com.zaxxer.hikari.pool.ProxyFactory");
/*  73 */     for (CtMethod method : proxyCt.getMethods()) {
/*  74 */       switch (method.getName()) {
/*     */         case "getProxyConnection":
/*  76 */           method.setBody("{return new " + packageName + ".HikariProxyConnection($$);}");
/*     */           break;
/*     */         case "getProxyStatement":
/*  79 */           method.setBody("{return new " + packageName + ".HikariProxyStatement($$);}");
/*     */           break;
/*     */         case "getProxyPreparedStatement":
/*  82 */           method.setBody("{return new " + packageName + ".HikariProxyPreparedStatement($$);}");
/*     */           break;
/*     */         case "getProxyCallableStatement":
/*  85 */           method.setBody("{return new " + packageName + ".HikariProxyCallableStatement($$);}");
/*     */           break;
/*     */         case "getProxyResultSet":
/*  88 */           method.setBody("{return new " + packageName + ".HikariProxyResultSet($$);}");
/*     */           break;
/*     */         case "getProxyDatabaseMetaData":
/*  91 */           method.setBody("{return new " + packageName + ".HikariProxyDatabaseMetaData($$);}");
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/*  99 */     proxyCt.writeFile(genDirectory + "target/classes");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> void generateProxyClass(Class<T> primaryInterface, String superClassName, String methodBody) throws Exception {
/* 107 */     String newClassName = superClassName.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
/*     */     
/* 109 */     CtClass superCt = classPool.getCtClass(superClassName);
/* 110 */     CtClass targetCt = classPool.makeClass(newClassName, superCt);
/* 111 */     targetCt.setModifiers(Modifier.setPublic(16));
/*     */     
/* 113 */     System.out.println("Generating " + newClassName);
/*     */ 
/*     */     
/* 116 */     HashSet<String> superSigs = new HashSet<>();
/* 117 */     for (CtMethod method : superCt.getMethods()) {
/* 118 */       if ((method.getModifiers() & 0x10) == 16) {
/* 119 */         superSigs.add(method.getName() + method.getName());
/*     */       }
/*     */     } 
/*     */     
/* 123 */     HashSet<String> methods = new HashSet<>();
/* 124 */     for (Class<?> intf : getAllInterfaces(primaryInterface)) {
/* 125 */       CtClass intfCt = classPool.getCtClass(intf.getName());
/* 126 */       targetCt.addInterface(intfCt);
/* 127 */       for (CtMethod intfMethod : intfCt.getDeclaredMethods()) {
/* 128 */         String signature = intfMethod.getName() + intfMethod.getName();
/*     */ 
/*     */         
/* 131 */         if (!superSigs.contains(signature))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 136 */           if (!methods.contains(signature)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 141 */             methods.add(signature);
/*     */ 
/*     */             
/* 144 */             CtMethod method = CtNewMethod.copy(intfMethod, targetCt, null);
/*     */             
/* 146 */             String modifiedBody = methodBody;
/*     */ 
/*     */             
/* 149 */             CtMethod superMethod = superCt.getMethod(intfMethod.getName(), intfMethod.getSignature());
/* 150 */             if ((superMethod.getModifiers() & 0x400) != 1024 && !isDefaultMethod(intf, intfMethod)) {
/* 151 */               modifiedBody = modifiedBody.replace("((cast) ", "");
/* 152 */               modifiedBody = modifiedBody.replace("delegate", "super");
/* 153 */               modifiedBody = modifiedBody.replace("super)", "super");
/*     */             } 
/*     */             
/* 156 */             modifiedBody = modifiedBody.replace("cast", primaryInterface.getName());
/*     */ 
/*     */             
/* 159 */             if (isThrowsSqlException(intfMethod)) {
/* 160 */               modifiedBody = modifiedBody.replace("method", method.getName());
/*     */             } else {
/*     */               
/* 163 */               modifiedBody = "{ return ((cast) delegate).method($$); }".replace("method", method.getName()).replace("cast", primaryInterface.getName());
/*     */             } 
/*     */             
/* 166 */             if (method.getReturnType() == CtClass.voidType) {
/* 167 */               modifiedBody = modifiedBody.replace("return", "");
/*     */             }
/*     */             
/* 170 */             method.setBody(modifiedBody);
/* 171 */             targetCt.addMethod(method);
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 175 */     targetCt.getClassFile().setMajorVersion(52);
/* 176 */     targetCt.writeFile(genDirectory + "target/classes");
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isThrowsSqlException(CtMethod method) {
/*     */     try {
/* 182 */       for (CtClass clazz : method.getExceptionTypes()) {
/* 183 */         if (clazz.getSimpleName().equals("SQLException")) {
/* 184 */           return true;
/*     */         }
/*     */       }
/*     */     
/* 188 */     } catch (NotFoundException notFoundException) {}
/*     */ 
/*     */ 
/*     */     
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDefaultMethod(Class<?> intf, CtMethod intfMethod) throws Exception {
/* 197 */     ArrayList<Class<?>> paramTypes = new ArrayList<>();
/*     */     
/* 199 */     for (CtClass pt : intfMethod.getParameterTypes()) {
/* 200 */       paramTypes.add(toJavaClass(pt));
/*     */     }
/*     */     
/* 203 */     return intf.getDeclaredMethod(intfMethod.getName(), (Class[])paramTypes.<Class<?>[]>toArray((Class<?>[][])new Class[0])).toString().contains("default ");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
/* 208 */     LinkedHashSet<Class<?>> interfaces = new LinkedHashSet<>();
/* 209 */     for (Class<?> intf : clazz.getInterfaces()) {
/* 210 */       if ((intf.getInterfaces()).length > 0) {
/* 211 */         interfaces.addAll(getAllInterfaces(intf));
/*     */       }
/* 213 */       interfaces.add(intf);
/*     */     } 
/* 215 */     if (clazz.getSuperclass() != null) {
/* 216 */       interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
/*     */     }
/*     */     
/* 219 */     if (clazz.isInterface()) {
/* 220 */       interfaces.add(clazz);
/*     */     }
/*     */     
/* 223 */     return interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?> toJavaClass(CtClass cls) throws Exception {
/* 228 */     if (cls.getName().endsWith("[]")) {
/* 229 */       return Array.newInstance(toJavaClass(cls.getName().replace("[]", "")), 0).getClass();
/*     */     }
/*     */     
/* 232 */     return toJavaClass(cls.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> toJavaClass(String cn) throws Exception {
/* 238 */     switch (cn) {
/*     */       case "int":
/* 240 */         return int.class;
/*     */       case "long":
/* 242 */         return long.class;
/*     */       case "short":
/* 244 */         return short.class;
/*     */       case "byte":
/* 246 */         return byte.class;
/*     */       case "float":
/* 248 */         return float.class;
/*     */       case "double":
/* 250 */         return double.class;
/*     */       case "boolean":
/* 252 */         return boolean.class;
/*     */       case "char":
/* 254 */         return char.class;
/*     */       case "void":
/* 256 */         return void.class;
/*     */     } 
/* 258 */     return Class.forName(cn);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikar\\util\JavassistProxyFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */