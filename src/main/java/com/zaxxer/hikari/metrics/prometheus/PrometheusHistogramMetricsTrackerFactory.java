package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusHistogramMetricsTrackerFactory implements MetricsTrackerFactory {
  private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap<>();
  
  private final HikariCPCollector collector = new HikariCPCollector();
  
  private final CollectorRegistry collectorRegistry;
  
  public PrometheusHistogramMetricsTrackerFactory() {
    this(CollectorRegistry.defaultRegistry);
  }
  
  public PrometheusHistogramMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
    this.collectorRegistry = collectorRegistry;
  }
  
  public IMetricsTracker create(String poolName, PoolStats poolStats) {
    registerCollector(this.collector, this.collectorRegistry);
    this.collector.add(poolName, poolStats);
    return new PrometheusHistogramMetricsTracker(poolName, this.collectorRegistry, this.collector);
  }
  
  private void registerCollector(Collector collector, CollectorRegistry collectorRegistry) {
    if (registrationStatuses.putIfAbsent(collectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null)
      collector.register(collectorRegistry); 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\metrics\prometheus\PrometheusHistogramMetricsTrackerFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */