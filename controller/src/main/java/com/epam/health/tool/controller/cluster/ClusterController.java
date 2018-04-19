package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterSearchParam;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.controller.wrapper.ResponseBodyEntityWrapper;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClusterController {
    @Autowired
    private IClusterFacade clusterFacade;

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping("/clusters")
    public List<ClusterEntityProjection> getClusterList() {
        return clusterFacade.getClusterList();
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping("/cluster/search")
    public String getClusterByParam( @RequestParam( value = "node", required = false) String node,
                                     @RequestParam( value = "shimName", required = false ) String shimName,
                                     @RequestParam( value = "secured", required = false) boolean isSecured ) {
        return clusterFacade.getClusterNameByParam( buildClusterSearch( shimName, node, isSecured ) );
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping("/cluster/{name}")
    public ClusterEntityProjection getCluster( @PathVariable( "name" ) String clusterName ) {
        return clusterFacade.getCluster( clusterName );
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @PostMapping( "/cluster" )
    public ClusterEntityProjection updateCluster(@RequestBody ResponseBodyEntityWrapper responseBodyEntityWrapper) {
        return clusterFacade.updateCluster( responseBodyEntityWrapper.getCluster() );
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @PutMapping( value = "/cluster", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ClusterEntityProjection saveCluster(@RequestBody ResponseBodyEntityWrapper responseBodyEntityWrapper) {
        return clusterFacade.saveCluster( responseBodyEntityWrapper.getCluster() );
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @DeleteMapping( "/cluster" )
    public ResponseEntity<Void> deleteCluster(@RequestParam( "clusterName" ) String clusterName) {
        clusterFacade.deleteCluster( clusterName );

        return ResponseEntity.noContent().build();
    }

    private ClusterSearchParam buildClusterSearch( String shimName, String node, boolean isSecured ) {
        return ClusterSearchParam.ClusterSearchParamBuilder.get().withNode( node ).withClusterType( shimName )
                .withSecure( isSecured ).build();
    }
}
