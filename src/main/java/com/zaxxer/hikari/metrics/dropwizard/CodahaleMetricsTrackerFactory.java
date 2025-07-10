package com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

public final class CodahaleMetricsTrackerFactory implements MetricsTrackerFactory {
  private final MetricRegistry registry;
  
  public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
    this.registry = registry;
  }
  
  public MetricRegistry getRegistry() {
    return this.registry;
  }
  
  public IMetricsTracker create(String poolName, PoolStats poolStats) {
    return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\metrics\dropwizard\CodahaleMetricsTrackerFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */