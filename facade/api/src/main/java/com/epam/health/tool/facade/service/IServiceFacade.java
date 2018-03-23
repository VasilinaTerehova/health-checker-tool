package com.epam.health.tool.facade.service;

import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ServiceTypeEnum;

public interface IServiceFacade {
    ClusterServiceEntity getServiceInfo(String clusterName, String serviceName );
    void deleteServiceInfo( Object serviceInfo );
}
