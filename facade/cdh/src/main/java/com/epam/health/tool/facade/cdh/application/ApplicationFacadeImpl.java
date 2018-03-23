package com.epam.health.tool.facade.cdh.application;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CDH-application")
public class ApplicationFacadeImpl implements IApplicationFacade {
    @Autowired
    private IClusterFacade clusterFacade;

    public List<ApplicationInfo> getApplicationList(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services/yarn/yarnApplications";

        try {
            String answer = BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .makeAuthenticatedRequest( url );
            List<ApplicationInfo> appList = CommonJsonHandler.get().getListTypedValueFromField( answer, "applications", ApplicationInfo.class );

            if ( appList != null ) {
                return appList;
            }
            else {
                throw new InvalidResponseException( "Elements not found. Answer string - " + answer );
            }
        }
        catch ( CommonUtilException ex ) {
            throw new InvalidResponseException( "Elements not found.", ex );
        }
    }

    public void killApplication(String clusterName, String appId) {

    }
}
