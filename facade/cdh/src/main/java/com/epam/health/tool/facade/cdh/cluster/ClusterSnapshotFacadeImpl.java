package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CDH-cluster")
public class ClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {
    @Autowired
    private IClusterFacade clusterFacade;

    public ClusterShapshotEntity askForCurrentClusterSnapshot(String clusterName ) {
        ClusterEntity clusterEntity = clusterFacade.getCluster( clusterName );
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services";
        String answer = BaseHttpAuthenticatedAction.get()
                .withUsername( clusterEntity.getHttp().getUsername() )
                .withPassword( clusterEntity.getHttp().getPassword() )
                .makeAuthenticatedRequest( url );

        return CommonJsonHandler.get().<ClusterShapshotEntity>getTypedValue( answer, "items" );
    }
}
