package com.epam.health.tool.facade.cdh.application;

import com.epam.health.tool.facade.application.ApplicationInfo;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.common.exception.ElementNotFoundException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CDH-application")
public class ApplicationFacadeImpl implements IApplicationFacade {
    @Autowired
    private IClusterFacade clusterFacade;

    public List<ApplicationInfo> getApplicationList(String clusterName) {
        ClusterEntity clusterEntity = clusterFacade.getCluster( clusterName );
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services/yarn/yarnApplications";
        String answer = BaseHttpAuthenticatedAction.get()
                .withUsername( clusterEntity.getHttp().getUsername() )
                .withPassword( clusterEntity.getHttp().getPassword() )
                .makeAuthenticatedRequest( url );
        List<ApplicationInfo> appList = CommonJsonHandler.get().<ApplicationInfo>getListTypedValue( answer, "applications" );

        if ( appList != null ) {
            return appList;
        }
        else {
            throw new ElementNotFoundException( "Elements not found. Answer string - " + answer );
        }
    }

    public void killApplication(String clusterName, String appId) {

    }
}
