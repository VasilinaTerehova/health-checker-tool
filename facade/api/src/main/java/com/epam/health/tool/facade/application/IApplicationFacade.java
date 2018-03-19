package com.epam.health.tool.facade.application;

import java.util.List;

public interface IApplicationFacade {
    List<Object> getApplicationList( String clusterName );
    void killApplication( String clusterName, String appId );
}
