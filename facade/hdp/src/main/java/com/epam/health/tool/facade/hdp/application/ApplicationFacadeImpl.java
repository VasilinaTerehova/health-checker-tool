package com.epam.health.tool.facade.hdp.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.ssh.SshCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("HDP-application")
public class ApplicationFacadeImpl implements IApplicationFacade {
    @Autowired
    private IClusterFacade clusterFacade;
    @Autowired
    private SVTransfererManager svTransfererManager;

    public List<ApplicationInfo> getApplicationList(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );

        try {
            String configUrl = "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterEntity.getName() + "/configurations?type=yarn-site&tag=version1";
            String yarnRestUrl = CommonJsonHandler.get().getTypedValueFromInnerField( BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .makeAuthenticatedRequest( configUrl ), String.class, "items", "properties", "yarn.resourcemanager.webapp.address" );

//            String yarnRestUrl = SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), "" )
//                    .executeCommand( clusterEntity.getHost(), "hdfs getconf -confKey yarn.resourcemanager.webapp.address" );
            String url = "http://" + yarnRestUrl.trim() + "/ws/v1/cluster/apps?limit=20";
            String answer = BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .makeAuthenticatedRequest( url );
            List<ApplicationInfoDTO> appList = CommonJsonHandler.get().getListTypedValueFromInnerField( answer, ApplicationInfoDTO.class, "apps", "app" );

            if ( appList != null ) {
                return mapDTOInfoToApplicationInfo( appList );
            }
            else {
                throw new InvalidResponseException( "Elements not found. Answer string - " + answer );
            }
        }
        catch ( CommonUtilException ex ) {
            throw new InvalidResponseException( "Elements not found. Reason - " + ex.getMessage(), ex );
        }
    }

    public void killApplication(String clusterName, String appId) {

    }

    private List<ApplicationInfo> mapDTOInfoToApplicationInfo( List<ApplicationInfoDTO> applicationInfoDTOs ) {
        return applicationInfoDTOs.stream()
                .map( applicationInfoDTO -> svTransfererManager.<ApplicationInfoDTO, ApplicationInfo>getTransferer( ApplicationInfoDTO.class, ApplicationInfo.class )
                        .transfer( applicationInfoDTO, ApplicationInfo.class ) ).collect(Collectors.toList());
    }
}
