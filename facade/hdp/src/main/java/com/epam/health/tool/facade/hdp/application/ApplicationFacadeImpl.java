package com.epam.health.tool.facade.hdp.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.ssh.SshCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("HDP-application")
public class ApplicationFacadeImpl implements IApplicationFacade {
    @Autowired
    private IClusterFacade clusterFacade;

    public List<ApplicationInfo> getApplicationList(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );

        try {
            String yarnRestUrl = SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), "" )
                    .executeCommand( clusterEntity.getHost(), "hdfs getconf -confKey yarn.resourcemanager.webapp.address" );
            String url = "http://" + yarnRestUrl.trim() + "/ws/v1/cluster/apps";
            String answer = BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .makeAuthenticatedRequest( url );
            List<ApplicationInfo> appList = CommonJsonHandler.get().getListTypedValueFromField( answer, "apps", ApplicationInfo.class );

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
