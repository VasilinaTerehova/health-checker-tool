package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vasilina_Terehova on 4/19/2018.
 */
public class ClusterContextHolder {
    Map<String, Map<String, HealthCheckResultsAccumulator>> clusterContextMap;
    ReentrantLock reentrantLock = new ReentrantLock();

    public void mergeResult(HealthCheckResultsAccumulator healthCheckResultsAccumulator) {

    }
}
