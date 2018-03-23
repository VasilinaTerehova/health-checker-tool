package com.epam.health.tool.facade.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.facade.exception.InvalidResponseException;

import java.util.List;

public interface IApplicationFacade {
    List<ApplicationInfo> getApplicationList(String clusterName ) throws InvalidResponseException;
    void killApplication( String clusterName, String appId );
}
