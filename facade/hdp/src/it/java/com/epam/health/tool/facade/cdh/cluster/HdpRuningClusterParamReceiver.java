package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.cluster.CommonRuningClusterParamReceiver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
@Component("HDP-param-cluster")
@Qualifier("CDH-cluster")
public class HdpRuningClusterParamReceiver extends CommonRuningClusterParamReceiver {
    @Autowired
    protected ClusterDao clusterDao;
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    @Override
    public String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        return null;
    }

    @Override
    public String getLogDirectory(String clusterName) throws InvalidResponseException {
        return null;
    }
}
