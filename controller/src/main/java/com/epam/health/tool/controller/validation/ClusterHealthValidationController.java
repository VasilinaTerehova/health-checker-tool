package com.epam.health.tool.controller.validation;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.recap.IClusterHealthRecapFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterHealthValidationController extends BaseFacadeResolvingController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;
    @Autowired
    private IClusterHealthRecapFacade clusterHealthValidationFacade;

    @GetMapping("/check/cluster/{name}")
    public ResponseEntity<ClusterHealthValidationResult> performClusterHealthValidation(@PathVariable( "name" ) String clusterName) {
        try {
            return ResponseEntity.ok( clusterHealthValidationFacade.validateClusterHealth( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .makeClusterSnapshot( buildAccumulatorToken( clusterName, HealthCheckActionType.ALL ) )));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }
}
