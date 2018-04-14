package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.facade.common.resolver.impl.HealthCheckActionImplResolver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ClusterHealthCheckPerformer {
    @Autowired
    private HealthCheckActionImplResolver healthCheckActionImplResolver;
    private Map<HealthCheckType, Function<String, List<IServiceHealthCheckAction>>> healthChecksActionMap;

    public ClusterHealthCheckPerformer() {
        healthChecksActionMap = new HashMap<>();

        healthChecksActionMap.put( HealthCheckType.ALL, this::getHealthCheckActions );
        healthChecksActionMap.put( HealthCheckType.HDFS, this::getHdfsHealthCheckActions );
        healthChecksActionMap.put( HealthCheckType.YARN, this::getYarnHealthCheckActions );
        healthChecksActionMap.put( HealthCheckType.OTHER_SERVICES, this::getRestHealthCheckActions );
    }

    public HealthCheckResultsAccumulator performHealthChecks( ClusterEntity clusterEntity, HealthCheckType healthCheckType ) {
        return performHealthChecks( clusterEntity, healthChecksActionMap.getOrDefault(healthCheckType, s -> Collections.emptyList()) );
    }

    private HealthCheckResultsAccumulator performHealthChecks( ClusterEntity clusterEntity,
                                                               Function<String, List<IServiceHealthCheckAction>> getServiceHealthCheckActionsFunction ) {
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
//        ExecutorService executorService = Executors.newFixedThreadPool( 4 );

        Flux.fromStream( getServiceHealthCheckActionsFunction.apply( clusterEntity.getClusterTypeEnum().name() ).stream() )
                .parallel().doOnNext( serviceHealthCheckAction -> {
            try {
                serviceHealthCheckAction.performHealthCheck(clusterEntity.getClusterName(), healthCheckResultsAccumulator);
            } catch (InvalidResponseException e) {
                throw new RuntimeException( e );
            }
        } ).subscribe();
//        getServiceHealthCheckActionsFunction.apply( clusterEntity.getClusterTypeEnum().name() ).stream()
//                .map( serviceHealthCheckAction -> CompletableFuture.runAsync(() -> {
//                    try {
//                        serviceHealthCheckAction.performHealthCheck(clusterEntity, healthCheckResultsAccumulator);
//                    } catch (InvalidResponseException e) {
//                        throw new RuntimeException( e );
//                    }
//                }))
//                .forEach(CompletableFuture::join);

        return healthCheckResultsAccumulator;
    }

    private List<IServiceHealthCheckAction> getHealthCheckActions( String clusterType ) {
        return healthCheckActionImplResolver.resolveActionImplementations( clusterType );
    }

    private List<IServiceHealthCheckAction> getRestHealthCheckActions( String clusterType ) {
        return healthCheckActionImplResolver.resolveRestActionImplementations( clusterType );
    }

    private List<IServiceHealthCheckAction> getYarnHealthCheckActions( String clusterType ) {
        return healthCheckActionImplResolver.resolveYarnActionImplementations( clusterType );
    }

    private List<IServiceHealthCheckAction> getHdfsHealthCheckActions( String clusterType ) {
        return healthCheckActionImplResolver.resolveHdfsActionImplementations( clusterType );
    }

    public enum HealthCheckType {
        ALL, YARN, HDFS, OTHER_SERVICES
    }
}
