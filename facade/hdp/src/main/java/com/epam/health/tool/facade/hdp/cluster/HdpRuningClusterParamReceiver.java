package com.epam.health.tool.facade.hdp.cluster;

import com.epam.health.tool.facade.common.cluster.CommonRuningClusterParamReceiver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
@Component("HDP-param-cluster")
@Qualifier("HDP-cluster")
public class HdpRuningClusterParamReceiver extends CommonRuningClusterParamReceiver {

    @Override
    public String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        //try {
        String clusterName = clusterEntity.getClusterName();
        String siteType = siteName.replace(".xml", "");
        String urlConfiguration = "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterName + "/configurations?type=" + siteType + "&tag=version1";
        System.out.println(urlConfiguration);
        try {
            return CommonJsonHandler.get().getTypedValueFromInnerField(
                    httpAuthenticationClient.makeAuthenticatedRequest(clusterName, urlConfiguration, false), String.class, "items", "properties", propertyName);
        } catch (CommonUtilException e) {
            throw new InvalidResponseException(e);
        }

    }

}
