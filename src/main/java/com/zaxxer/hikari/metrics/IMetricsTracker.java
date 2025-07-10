package com.zaxxer.hikari.metrics;

public interface IMetricsTracker extends AutoCloseable {
  default void recordConnectionCreatedMillis(long connectionCreatedMillis) {}
  
  default void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {}
  
  default void recordConnectionUsageMillis(long elapsedBorrowedMillis) {}
  
  default void recordConnectionTimeout() {}
  
  default void close() {}
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\metrics\IMetricsTracker.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */