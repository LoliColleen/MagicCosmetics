/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ 
/*     */ public abstract class ProxyDatabaseMetaData
/*     */   implements DatabaseMetaData
/*     */ {
/*     */   protected final ProxyConnection connection;
/*     */   protected final DatabaseMetaData delegate;
/*     */   
/*     */   ProxyDatabaseMetaData(ProxyConnection connection, DatabaseMetaData metaData) {
/*  17 */     this.connection = connection;
/*  18 */     this.delegate = metaData;
/*     */   }
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException e) {
/*  23 */     return this.connection.checkException(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  30 */     String delegateToString = this.delegate.toString();
/*  31 */     return getClass().getSimpleName() + "@" + getClass().getSimpleName() + " wrapping " + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Connection getConnection() {
/*  42 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
/*  47 */     ResultSet resultSet = this.delegate.getProcedures(catalog, schemaPattern, procedureNamePattern);
/*  48 */     Statement statement = resultSet.getStatement();
/*  49 */     if (statement != null) {
/*  50 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/*  52 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
/*  57 */     ResultSet resultSet = this.delegate.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
/*  58 */     Statement statement = resultSet.getStatement();
/*  59 */     if (statement != null) {
/*  60 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/*  62 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
/*  67 */     ResultSet resultSet = this.delegate.getTables(catalog, schemaPattern, tableNamePattern, types);
/*  68 */     Statement statement = resultSet.getStatement();
/*  69 */     if (statement != null) {
/*  70 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/*  72 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas() throws SQLException {
/*  77 */     ResultSet resultSet = this.delegate.getSchemas();
/*  78 */     Statement statement = resultSet.getStatement();
/*  79 */     if (statement != null) {
/*  80 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/*  82 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCatalogs() throws SQLException {
/*  87 */     ResultSet resultSet = this.delegate.getCatalogs();
/*  88 */     Statement statement = resultSet.getStatement();
/*  89 */     if (statement != null) {
/*  90 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/*  92 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTableTypes() throws SQLException {
/*  97 */     ResultSet resultSet = this.delegate.getTableTypes();
/*  98 */     Statement statement = resultSet.getStatement();
/*  99 */     if (statement != null) {
/* 100 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 102 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 107 */     ResultSet resultSet = this.delegate.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
/* 108 */     Statement statement = resultSet.getStatement();
/* 109 */     if (statement != null) {
/* 110 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 112 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
/* 117 */     ResultSet resultSet = this.delegate.getColumnPrivileges(catalog, schema, table, columnNamePattern);
/* 118 */     Statement statement = resultSet.getStatement();
/* 119 */     if (statement != null) {
/* 120 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 122 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
/* 127 */     ResultSet resultSet = this.delegate.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
/* 128 */     Statement statement = resultSet.getStatement();
/* 129 */     if (statement != null) {
/* 130 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 132 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
/* 137 */     ResultSet resultSet = this.delegate.getBestRowIdentifier(catalog, schema, table, scope, nullable);
/* 138 */     Statement statement = resultSet.getStatement();
/* 139 */     if (statement != null) {
/* 140 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 142 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
/* 147 */     ResultSet resultSet = this.delegate.getVersionColumns(catalog, schema, table);
/* 148 */     Statement statement = resultSet.getStatement();
/* 149 */     if (statement != null) {
/* 150 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 152 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
/* 157 */     ResultSet resultSet = this.delegate.getPrimaryKeys(catalog, schema, table);
/* 158 */     Statement statement = resultSet.getStatement();
/* 159 */     if (statement != null) {
/* 160 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 162 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/* 167 */     ResultSet resultSet = this.delegate.getImportedKeys(catalog, schema, table);
/* 168 */     Statement statement = resultSet.getStatement();
/* 169 */     if (statement != null) {
/* 170 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 172 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/* 177 */     ResultSet resultSet = this.delegate.getExportedKeys(catalog, schema, table);
/* 178 */     Statement statement = resultSet.getStatement();
/* 179 */     if (statement != null) {
/* 180 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 182 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
/* 187 */     ResultSet resultSet = this.delegate.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
/* 188 */     Statement statement = resultSet.getStatement();
/* 189 */     if (statement != null) {
/* 190 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 192 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTypeInfo() throws SQLException {
/* 197 */     ResultSet resultSet = this.delegate.getTypeInfo();
/* 198 */     Statement statement = resultSet.getStatement();
/* 199 */     if (statement != null) {
/* 200 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 202 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
/* 207 */     ResultSet resultSet = this.delegate.getIndexInfo(catalog, schema, table, unique, approximate);
/* 208 */     Statement statement = resultSet.getStatement();
/* 209 */     if (statement != null) {
/* 210 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 212 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
/* 217 */     ResultSet resultSet = this.delegate.getUDTs(catalog, schemaPattern, typeNamePattern, types);
/* 218 */     Statement statement = resultSet.getStatement();
/* 219 */     if (statement != null) {
/* 220 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 222 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
/* 227 */     ResultSet resultSet = this.delegate.getSuperTypes(catalog, schemaPattern, typeNamePattern);
/* 228 */     Statement statement = resultSet.getStatement();
/* 229 */     if (statement != null) {
/* 230 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 232 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
/* 237 */     ResultSet resultSet = this.delegate.getSuperTables(catalog, schemaPattern, tableNamePattern);
/* 238 */     Statement statement = resultSet.getStatement();
/* 239 */     if (statement != null) {
/* 240 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 242 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
/* 247 */     ResultSet resultSet = this.delegate.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
/* 248 */     Statement statement = resultSet.getStatement();
/* 249 */     if (statement != null) {
/* 250 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 252 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
/* 257 */     ResultSet resultSet = this.delegate.getSchemas(catalog, schemaPattern);
/* 258 */     Statement statement = resultSet.getStatement();
/* 259 */     if (statement != null) {
/* 260 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 262 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getClientInfoProperties() throws SQLException {
/* 267 */     ResultSet resultSet = this.delegate.getClientInfoProperties();
/* 268 */     Statement statement = resultSet.getStatement();
/* 269 */     if (statement != null) {
/* 270 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 272 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
/* 277 */     ResultSet resultSet = this.delegate.getFunctions(catalog, schemaPattern, functionNamePattern);
/* 278 */     Statement statement = resultSet.getStatement();
/* 279 */     if (statement != null) {
/* 280 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 282 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
/* 287 */     ResultSet resultSet = this.delegate.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
/* 288 */     Statement statement = resultSet.getStatement();
/* 289 */     if (statement != null) {
/* 290 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 292 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 297 */     ResultSet resultSet = this.delegate.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
/* 298 */     Statement statement = resultSet.getStatement();
/* 299 */     if (statement != null) {
/* 300 */       statement = ProxyFactory.getProxyStatement(this.connection, statement);
/*     */     }
/* 302 */     return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> iface) throws SQLException {
/* 310 */     if (iface.isInstance(this.delegate)) {
/* 311 */       return (T)this.delegate;
/*     */     }
/* 313 */     if (this.delegate != null) {
/* 314 */       return this.delegate.unwrap(iface);
/*     */     }
/*     */     
/* 317 */     throw new SQLException("Wrapped DatabaseMetaData is not an instance of " + iface);
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\ProxyDatabaseMetaData.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */