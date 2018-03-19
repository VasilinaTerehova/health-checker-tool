package com.epam.health.tool.facade.service;

public interface IServiceFacade {
    Object getServiceInfo( String clusterName, String serviceName );
    void saveServiceInfo( Object serviceInfo );
    void deleteServiceInfo( Object serviceInfo );
}
