package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.util.FastList;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class ProxyFactory {
  static ProxyConnection getProxyConnection(PoolEntry paramPoolEntry, Connection paramConnection, FastList<Statement> paramFastList, ProxyLeakTask paramProxyLeakTask, boolean paramBoolean1, boolean paramBoolean2) {
    return new HikariProxyConnection(paramPoolEntry, paramConnection, paramFastList, paramProxyLeakTask, paramBoolean1, paramBoolean2);
  }
  
  static Statement getProxyStatement(ProxyConnection paramProxyConnection, Statement paramStatement) {
    return new HikariProxyStatement(paramProxyConnection, paramStatement);
  }
  
  static CallableStatement getProxyCallableStatement(ProxyConnection paramProxyConnection, CallableStatement paramCallableStatement) {
    return new HikariProxyCallableStatement(paramProxyConnection, paramCallableStatement);
  }
  
  static PreparedStatement getProxyPreparedStatement(ProxyConnection paramProxyConnection, PreparedStatement paramPreparedStatement) {
    return new HikariProxyPreparedStatement(paramProxyConnection, paramPreparedStatement);
  }
  
  static ResultSet getProxyResultSet(ProxyConnection paramProxyConnection, ProxyStatement paramProxyStatement, ResultSet paramResultSet) {
    return new HikariProxyResultSet(paramProxyConnection, paramProxyStatement, paramResultSet);
  }
  
  static DatabaseMetaData getProxyDatabaseMetaData(ProxyConnection paramProxyConnection, DatabaseMetaData paramDatabaseMetaData) {
    return new HikariProxyDatabaseMetaData(paramProxyConnection, paramDatabaseMetaData);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\ProxyFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */