/*    */ package com.zaxxer.hikari;
/*    */ 
/*    */ import com.zaxxer.hikari.util.PropertyElf;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.RefAddr;
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import javax.sql.DataSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HikariJNDIFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public synchronized Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
/* 38 */     if (obj instanceof Reference && "javax.sql.DataSource".equals(((Reference)obj).getClassName())) {
/* 39 */       Reference ref = (Reference)obj;
/* 40 */       Set<String> hikariPropSet = PropertyElf.getPropertyNames(HikariConfig.class);
/*    */       
/* 42 */       Properties properties = new Properties();
/* 43 */       Enumeration<RefAddr> enumeration = ref.getAll();
/* 44 */       while (enumeration.hasMoreElements()) {
/* 45 */         RefAddr element = enumeration.nextElement();
/* 46 */         String type = element.getType();
/* 47 */         if (type.startsWith("dataSource.") || hikariPropSet.contains(type)) {
/* 48 */           properties.setProperty(type, element.getContent().toString());
/*    */         }
/*    */       } 
/* 51 */       return createDataSource(properties, nameCtx);
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private DataSource createDataSource(Properties properties, Context context) throws NamingException {
/* 58 */     String jndiName = properties.getProperty("dataSourceJNDI");
/* 59 */     if (jndiName != null) {
/* 60 */       return lookupJndiDataSource(properties, context, jndiName);
/*    */     }
/*    */     
/* 63 */     return new HikariDataSource(new HikariConfig(properties));
/*    */   }
/*    */ 
/*    */   
/*    */   private DataSource lookupJndiDataSource(Properties properties, Context context, String jndiName) throws NamingException {
/* 68 */     if (context == null) {
/* 69 */       throw new RuntimeException("JNDI context does not found for dataSourceJNDI : " + jndiName);
/*    */     }
/*    */     
/* 72 */     DataSource jndiDS = (DataSource)context.lookup(jndiName);
/* 73 */     if (jndiDS == null) {
/* 74 */       InitialContext ic = new InitialContext();
/* 75 */       jndiDS = (DataSource)ic.lookup(jndiName);
/* 76 */       ic.close();
/*    */     } 
/*    */     
/* 79 */     if (jndiDS != null) {
/* 80 */       HikariConfig config = new HikariConfig(properties);
/* 81 */       config.setDataSource(jndiDS);
/* 82 */       return new HikariDataSource(config);
/*    */     } 
/*    */     
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\HikariJNDIFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */