package com.epam.health.tool.facade.cluster.receiver;

public interface ISingleParamReceiver<T> {
    T receiveParam( String clusterName, IRunningClusterParamReceiver runningClusterParamReceiver );
}
