package com.epam.health.tool.facade.cluster.receiver;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.exception.InvalidResponseException;

import java.util.Set;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public interface IRunningClusterParamReceiver {
    String getLogDirectory(String clusterName) throws InvalidResponseException;
    String getPropertySiteXml( String clusterName, String siteName, String propertyName ) throws InvalidResponseException;
    Set<String> getLiveNodes(String clusterName ) throws InvalidResponseException;
    HdfsNamenodeJson getHdfsNamenodeJson( String clusterName ) throws InvalidResponseException;
    String getActiveResourceManagerAddress( String clusterName ) throws InvalidResponseException;
    String getNameNodeUrl( String clusterName ) throws InvalidResponseException;
}
