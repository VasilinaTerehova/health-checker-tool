package com.epam.health.tool.facade.common.service;

import com.epam.health.tool.facade.service.IServiceFacade;
import com.epam.health.tool.model.ClusterServiceEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceFacadeImpl implements IServiceFacade {

    public ClusterServiceEntity getServiceInfo(String clusterName, String serviceName) {
        return null;
    }

    public void saveServiceInfo(Object serviceInfo) {

    }

    public void deleteServiceInfo(Object serviceInfo) {

    }
}
