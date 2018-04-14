package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.accumulator.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClusterHealthCheckController extends BaseFacadeResolvingController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;

    //Should be changed to /cluster/{name}/status/services
    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping( "/getClusterStatus" )
    public ResponseEntity<HealthCheckResultsAccumulator> getRestClusterStatus(@RequestParam( "clusterName" ) String clusterName ) {
        try {
            return ResponseEntity.ok( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .askForCurrentClusterSnapshot( clusterName ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    //Should be changed to /cluster/{name}/status/all
    @CrossOrigin( origins = {"http://localhost:4200", "*"} )
    @RequestMapping( "/api/cluster/status/all" )
    public ResponseEntity<HealthCheckResultsAccumulator> getAllClusterStatus(@RequestParam( "clusterName" ) String clusterName ) {
        try {
            return ResponseEntity.ok( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .askForCurrentFullHealthCheck( clusterName ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/yarn" )
    public ResponseEntity<YarnHealthCheckResult> getYarnClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .askForCurrentYarnHealthCheck( clusterName ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/hdfs" )
    public ResponseEntity<HdfsHealthCheckResult> getHdfsClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .askForCurrentHdfsHealthCheck( clusterName ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    //Should be changed to /cluster/{name}/status/history
    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping( "/getClusterStatusHistory" )
    public ResponseEntity<List<ClusterHealthSummary>> getClusterStatusHistory(@RequestParam( "clusterName" ) String clusterName ) {
        try {
            return ResponseEntity.ok( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .getClusterSnapshotHistory( clusterName ) );
        } catch (InvalidResponseException | ImplementationNotResolvedException e) {
            throw new RetrievingObjectException( e );
        }
    }
}
