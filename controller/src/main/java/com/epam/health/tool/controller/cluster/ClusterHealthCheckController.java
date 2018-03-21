package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterHealthCheckController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;
    @Autowired
    private IClusterFacade clusterFacade;

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping( "/getClusterStatus" )
    public ClusterHealthSummary getClusterStatus(@RequestParam( "clusterName" ) String clusterName ) {
        return clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() ) //Should be changed
                .askForCurrentClusterSnapshot( clusterName );
    }
}
