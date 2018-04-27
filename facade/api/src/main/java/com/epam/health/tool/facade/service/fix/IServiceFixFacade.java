package com.epam.health.tool.facade.service.fix;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.service.fix.ServiceFixResult;

import java.util.List;

public interface IServiceFixFacade {
    ServiceFixResult fixService( String clusterName, String serviceName, String username, String password ) throws InvalidResponseException;
    List<String> getStepsForFix( String clusterName, String serviceName ) throws InvalidResponseException;
    String generateBashScript( String clusterName, String serviceName ) throws InvalidResponseException;
}
