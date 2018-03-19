package com.epam.health.tool.facade.service;

public interface IServiceSnapshotFacade {
    Object getLastServiceSnapshot( String clusterName, String serviceName );
    Object askForCurrentServiceSnapshot( String clusterName, String serviceName );
}
