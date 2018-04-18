package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
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

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/services" )
    public ResponseEntity<HealthCheckResultsAccumulator> getRestClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( askForClusterState( clusterName, HealthCheckActionType.OTHER_SERVICES ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = {"http://localhost:4200", "*"} )
    @GetMapping( "/api/cluster/{name}/status/all" )
    public ResponseEntity<HealthCheckResultsAccumulator> getAllClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( askForClusterState( clusterName, HealthCheckActionType.ALL ) );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/yarn" )
    public ResponseEntity<YarnHealthCheckResult> getYarnClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( askForClusterState( clusterName, HealthCheckActionType.YARN_SERVICE ).getYarnHealthCheckResult() );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/fs" )
    public ResponseEntity<ClusterSnapshotEntityProjection> getFsClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( askForClusterState( clusterName, HealthCheckActionType.FS ).getClusterHealthSummary().getCluster() );
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping( "/api/cluster/{name}/status/hdfs" )
    public ResponseEntity<HdfsHealthCheckResult> getHdfsClusterStatus(@PathVariable( "name" ) String clusterName ) {
        try {
            return ResponseEntity.ok( askForClusterState( clusterName, HealthCheckActionType.HDFS_SERVICE ).getHdfsHealthCheckResult() );
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

    private ClusterAccumulatorToken buildAccumulatorToken( String clusterName, String token, String type, boolean useSave ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withToken( token ).withType( type ).useSave( useSave ).buildClusterAccumulatorToken();
    }

    //Short form, develop only
    private ClusterAccumulatorToken buildAccumulatorToken( String clusterName, HealthCheckActionType type ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withType( type ).buildClusterAccumulatorToken();
    }

    private HealthCheckResultsAccumulator askForClusterState( String clusterName, HealthCheckActionType healthCheckAction )
            throws ImplementationNotResolvedException, InvalidResponseException {
        return resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                .askForClusterSnapshot( buildAccumulatorToken( clusterName, healthCheckAction ) );
    }
}
