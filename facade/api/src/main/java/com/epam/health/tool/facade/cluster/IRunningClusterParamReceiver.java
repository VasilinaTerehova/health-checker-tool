package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public interface IRunningClusterParamReceiver {
    String getLogDirectory(String clusterName) throws InvalidResponseException;
    String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException;
    HdfsNamenodeJson getHdfsNamenodeJson(ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException, CommonUtilException;
    String getHANameNodeUrl(String clusterName) throws InvalidResponseException;
}
