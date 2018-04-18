package com.epam.health.tool.controller;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseFacadeResolvingController {
    @Autowired
    private IClusterFacade clusterFacade;

    protected ClusterAccumulatorToken buildAccumulatorToken(String clusterName, String token, HealthCheckActionType type, boolean useSave ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withToken( token ).withType( type ).useSave( useSave ).buildClusterAccumulatorToken();
    }

    //Short form, develop only
    protected ClusterAccumulatorToken buildAccumulatorToken( String clusterName, HealthCheckActionType type ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withType( type ).buildClusterAccumulatorToken();
    }

    protected  <T> T resolveClusterSnapshotFacade( String clusterName, IFacadeImplResolver<T> facadeImplResolver ) throws ImplementationNotResolvedException {
        return facadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() );
    }
}
