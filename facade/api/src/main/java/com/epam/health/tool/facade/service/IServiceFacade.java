package com.epam.health.tool.facade.service;

import com.epam.health.tool.model.ClusterServiceEntity;

public interface IServiceFacade {
    ClusterServiceEntity getServiceInfo(String clusterName, String serviceName );
    void saveServiceInfo( Object serviceInfo );
    void deleteServiceInfo( Object serviceInfo );
}
