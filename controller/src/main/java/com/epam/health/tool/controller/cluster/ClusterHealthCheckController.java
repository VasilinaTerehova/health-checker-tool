package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClusterHealthCheckController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;
    @Autowired
    private IClusterFacade clusterFacade;

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping( "/getClusterStatus" )
    public ResponseEntity<ClusterHealthSummary> getClusterStatus(@RequestParam( "clusterName" ) String clusterName ) {
        try {
            return ResponseEntity.ok( clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() ) //Should be changed
                    .getLastClusterSnapshot( clusterName ) );
        } catch (ImplementationNotResolvedException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping( "/getClusterStatusHistory" )
    public ResponseEntity<List<ClusterHealthSummary>> getClusterStatusHistory(@RequestParam( "clusterName" ) String clusterName ) {
        try {
            return ResponseEntity.ok( clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() ) //Should be changed
                    .getClusterSnapshotHistory( clusterName ) );
        } catch (InvalidResponseException | ImplementationNotResolvedException e) {
            throw new RetrievingObjectException( e );
        }
    }
}
