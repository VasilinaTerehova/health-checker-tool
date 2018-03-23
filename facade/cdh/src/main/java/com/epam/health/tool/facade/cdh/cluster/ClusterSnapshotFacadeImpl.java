package com.epam.health.tool.facade.cdh.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.authentificate.BaseHttpAuthenticatedAction;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CDH-cluster")
public class ClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {
    @Autowired
    private IClusterFacade clusterFacade;

    public ClusterHealthSummary askForCurrentClusterSnapshot(String clusterName ) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services";
        try{
            String answer = BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .makeAuthenticatedRequest( url );

            return new ClusterHealthSummary( clusterEntity, extractFromJsonString( answer ) );
        }
        catch (CommonUtilException ex) {
            throw new InvalidResponseException( ex );
        }
    }

    private List<ServiceStatus> extractFromJsonString( String jsonString ) throws InvalidResponseException {
        try {
            return CommonJsonHandler.get().getListTypedValueFromField( jsonString, "items", ServiceStatus.class );
        } catch (CommonUtilException e) {
            throw new InvalidResponseException( "Can't extract application list from answer - " + jsonString, e );
        }
    }
}
