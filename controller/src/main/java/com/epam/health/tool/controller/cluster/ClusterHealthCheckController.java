package com.epam.health.tool.controller.cluster;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterHealthCheckController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;
    @Autowired
    private IClusterFacade clusterFacade;

    @RequestMapping( "/getClusterStatus" )
    public ClusterShapshotEntity getClusterStatus(@RequestParam( "clusterName" ) String clusterName ) {
        return clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterTypeEnum().name() ) //Should be changed
                .askForCurrentClusterSnapshot( clusterName );
    }
}
