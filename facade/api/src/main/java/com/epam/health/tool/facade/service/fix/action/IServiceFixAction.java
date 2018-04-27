package com.epam.health.tool.facade.service.fix.action;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.service.fix.ServiceFixResult;

import java.util.List;

public interface IServiceFixAction {
    ServiceFixResult performFix( String clusterName, String rootUsername, String rootPassword ) throws InvalidResponseException;
    List<String> getStepsForFix(String clusterName) throws InvalidResponseException;
    String getFixCommand( String clusterName ) throws InvalidResponseException;
}
