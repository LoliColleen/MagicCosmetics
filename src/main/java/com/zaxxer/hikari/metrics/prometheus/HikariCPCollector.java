package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class HikariCPCollector extends Collector {
  private static final List<String> LABEL_NAMES = Collections.singletonList("pool");
  
  private final Map<String, PoolStats> poolStatsMap = new ConcurrentHashMap<>();
  
  public List<Collector.MetricFamilySamples> collect() {
    return Arrays.asList(new Collector.MetricFamilySamples[] { (Collector.MetricFamilySamples)
          createGauge("hikaricp_active_connections", "Active connections", PoolStats::getActiveConnections), (Collector.MetricFamilySamples)
          
          createGauge("hikaricp_idle_connections", "Idle connections", PoolStats::getIdleConnections), (Collector.MetricFamilySamples)
          
          createGauge("hikaricp_pending_threads", "Pending threads", PoolStats::getPendingThreads), (Collector.MetricFamilySamples)
          
          createGauge("hikaricp_connections", "The number of current connections", PoolStats::getTotalConnections), (Collector.MetricFamilySamples)
          
          createGauge("hikaricp_max_connections", "Max connections", PoolStats::getMaxConnections), (Collector.MetricFamilySamples)
          
          createGauge("hikaricp_min_connections", "Min connections", PoolStats::getMinConnections) });
  }
  
  void add(String name, PoolStats poolStats) {
    this.poolStatsMap.put(name, poolStats);
  }
  
  void remove(String name) {
    this.poolStatsMap.remove(name);
  }
  
  private GaugeMetricFamily createGauge(String metric, String help, Function<PoolStats, Integer> metricValueFunction) {
    GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, LABEL_NAMES);
    this.poolStatsMap.forEach((k, v) -> metricFamily.addMetric(Collections.singletonList(k), ((Integer)metricValueFunction.apply(v)).intValue()));
    return metricFamily;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\metrics\prometheus\HikariCPCollector.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */