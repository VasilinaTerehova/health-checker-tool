package com.epam.health.tool.facade.cdh.application;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhApplicationFacadeImpl implements IApplicationFacade {
    @Autowired
    private IClusterFacade clusterFacade;
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    public List<ApplicationInfo> getApplicationList(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services/yarn/yarnApplications";

        try {
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url, false );
            List<ApplicationInfo> appList = CommonJsonHandler.get().getListTypedValueFromInnerField( answer, ApplicationInfo.class, "applications" );

            if ( appList != null ) {
                return appList;
            }
            else {
                throw new InvalidResponseException( "Elements not found. Answer string - " + answer );
            }
        }
        catch ( CommonUtilException | AuthenticationRequestException ex ) {
            throw new InvalidResponseException( "Elements not found.", ex );
        }
    }

    public void killApplication(String clusterName, String appId) {

    }
}
