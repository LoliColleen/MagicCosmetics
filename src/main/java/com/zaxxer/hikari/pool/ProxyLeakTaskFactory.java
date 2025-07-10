package com.zaxxer.hikari.pool;

import java.util.concurrent.ScheduledExecutorService;

class ProxyLeakTaskFactory {
  private ScheduledExecutorService executorService;
  
  private long leakDetectionThreshold;
  
  ProxyLeakTaskFactory(long leakDetectionThreshold, ScheduledExecutorService executorService) {
    this.executorService = executorService;
    this.leakDetectionThreshold = leakDetectionThreshold;
  }
  
  ProxyLeakTask schedule(PoolEntry poolEntry) {
    return (this.leakDetectionThreshold == 0L) ? ProxyLeakTask.NO_LEAK : scheduleNewTask(poolEntry);
  }
  
  void updateLeakDetectionThreshold(long leakDetectionThreshold) {
    this.leakDetectionThreshold = leakDetectionThreshold;
  }
  
  private ProxyLeakTask scheduleNewTask(PoolEntry poolEntry) {
    ProxyLeakTask task = new ProxyLeakTask(poolEntry);
    task.schedule(this.executorService, this.leakDetectionThreshold);
    return task;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\zaxxer\hikari\pool\ProxyLeakTaskFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */