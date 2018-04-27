package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IHealthCheckFacade;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckActionImplResolver;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Created by Vasilina_Terehova on 4/19/2018.
 */
@Component
public class HealthCheckerFacadeImpl implements IHealthCheckFacade {
    @Autowired
    protected ClusterDao clusterDao;

    @Autowired
    private HealthCheckActionImplResolver healthCheckActionImplResolver;

    public HealthCheckResultsAccumulator performHealthChecks(String clusterName, ClusterAccumulatorToken clusterAccumulatorToken) {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = HealthCheckResultsAccumulator.HealthCheckResultsModifier.get()
                .setClusterName( clusterName ).setToken(clusterAccumulatorToken.getToken()).modify();

        Flux.fromStream( healthCheckActionImplResolver.resolveActionImplementations( clusterEntity.getClusterTypeEnum().name(), clusterAccumulatorToken.getHealthCheckActionType() ).stream() )
                .parallel().doOnNext( serviceHealthCheckAction -> {
            try {
                serviceHealthCheckAction.performHealthCheck(clusterEntity.getClusterName(), healthCheckResultsAccumulator);
            } catch (InvalidResponseException e) {
                //Should be changed
                throw new RuntimeException( e );
            }
        } ).subscribe();

        return healthCheckResultsAccumulator;
    }

    @Override
    public HealthCheckResultsAccumulator askForClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) throws InvalidResponseException {
        return performHealthChecks( clusterAccumulatorToken.getClusterName(), clusterAccumulatorToken );
    }
}
