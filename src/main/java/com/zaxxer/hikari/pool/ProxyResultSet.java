package com.zaxxer.hikari.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ProxyResultSet implements ResultSet {
  protected final ProxyConnection connection;
  
  protected final ProxyStatement statement;
  
  final ResultSet delegate;
  
  protected ProxyResultSet(ProxyConnection connection, ProxyStatement statement, ResultSet resultSet) {
    this.connection = connection;
    this.statement = statement;
    this.delegate = resultSet;
  }
  
  final SQLException checkException(SQLException e) {
    return this.connection.checkException(e);
  }
  
  public String toString() {
    return getClass().getSimpleName() + "@" + getClass().getSimpleName() + " wrapping " + System.identityHashCode(this);
  }
  
  public final Statement getStatement() throws SQLException {
    return this.statement;
  }
  
  public void updateRow() throws SQLException {
    this.connection.markCommitStateDirty();
    this.delegate.updateRow();
  }
  
  public void insertRow() throws SQLException {
    this.connection.markCommitStateDirty();
    this.delegate.insertRow();
  }
  
  public void deleteRow() throws SQLException {
    this.connection.markCommitStateDirty();
    this.delegate.deleteRow();
  }
  
  public final <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this.delegate))
      return (T)this.delegate; 
    if (this.delegate != null)
      return this.delegate.unwrap(iface); 
    throw new SQLException("Wrapped ResultSet is not an instance of " + iface);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\ProxyResultSet.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */