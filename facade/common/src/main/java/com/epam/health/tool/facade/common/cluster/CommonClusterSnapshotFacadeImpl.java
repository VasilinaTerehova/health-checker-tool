package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.date.util.DateUtil;
import com.epam.health.tool.facade.common.resolver.impl.HealthCheckActionImplResolver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CommonClusterSnapshotFacadeImpl implements IClusterSnapshotFacade {

    @Autowired
    protected SVTransfererManager svTransfererManager;
    @Autowired
    protected IClusterFacade clusterFacade;
    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;
    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;
    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;
    @Autowired
    ClusterServiceDao clusterServiceDao;
    @Autowired
    ClusterDao clusterDao;

    @Autowired
    private HealthCheckActionImplResolver healthCheckActionImplResolver;

    private Logger logger = Logger.getLogger(CommonClusterSnapshotFacadeImpl.class);

    @Override
    public ClusterHealthSummary askForCurrentClusterSnapshot(String clusterName) throws InvalidResponseException {
        return performHealthChecks( clusterName, this::getRestHealthCheckActions ).getClusterHealthSummary();
    }

    @Override
    public HealthCheckResultsAccumulator askForCurrentFullHealthCheck(String clusterName) throws InvalidResponseException {
        return performHealthChecks( clusterName, this::getHealthCheckActions );
    }

    @Override
    public YarnHealthCheckResult askForCurrentYarnHealthCheck(String clusterName) throws InvalidResponseException {
        return performHealthChecks( clusterName, this::getYarnHealthCheckActions ).getYarnHealthCheckResult();
    }

    @Override
    public ClusterHealthSummary getLastClusterSnapshot(String clusterName) {
        receiveAndSaveClusterSnapshot(clusterDao.findByClusterName(clusterName));
        Pageable top1 = new PageRequest(0, 1);
        List<ClusterSnapshotEntityProjection> top1ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top1);
        if (top1ClusterName.isEmpty()) {
            return null;
        }
        ClusterSnapshotEntityProjection cluster = top1ClusterName.get(0);
        return new ClusterHealthSummary(cluster, clusterServiceSnapshotDao.findServiceProjectionsBy(cluster.getId()));
    }

    @Override
    public List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName) throws InvalidResponseException {
        Pageable top30 = new PageRequest(0, 30);
        List<ClusterSnapshotEntityProjection> top30ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top30);
        ArrayList<ClusterHealthSummary> clusterHealthSummaries = new ArrayList<>();
        top30ClusterName.forEach(clusterSnapshotEntityProjection -> clusterHealthSummaries.add(new ClusterHealthSummary(clusterSnapshotEntityProjection, clusterServiceSnapshotDao.findServiceProjectionsBy(clusterSnapshotEntityProjection.getId()))));
        return clusterHealthSummaries;
    }

    @Override
    public ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity) {
        try {
            List<ServiceStatus> serviceStatusList = askForCurrentServicesSnapshot(clusterEntity.getClusterName());
            List<ClusterSnapshotEntityProjection> top10ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterEntity.getClusterName(), new PageRequest(0, 1));
            //for now getting and saving happen once an hour, to be revisited
            if (!top10ClusterName.isEmpty() && new Date().before(DateUtil.dateHourPlus(top10ClusterName.get(0).getDateOfSnapshot()))) {
                return clusterSnapshotDao.findById(top10ClusterName.get(0).getId()).get();
            }

            ClusterShapshotEntity clusterShapshotEntity = new ClusterShapshotEntity();
            clusterShapshotEntity.setDateOfSnapshot(new Date());
            clusterShapshotEntity.setClusterEntity(clusterEntity);
            clusterSnapshotDao.save(clusterShapshotEntity);
            serviceStatusList.forEach(serviceStatus -> {
                ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getType());
                ClusterServiceShapshotEntity clusterServiceShapshotEntity = svTransfererManager.<ServiceStatus, ClusterServiceShapshotEntity>getTransferer(ServiceStatus.class, ClusterServiceShapshotEntity.class).transfer(serviceStatus, ClusterServiceShapshotEntity.class);
                clusterServiceShapshotEntity.setClusterShapshotEntity(clusterShapshotEntity);
                if (clusterServiceEntity == null) {
                    ClusterServiceEntity clusterServiceEntity1 = clusterServiceShapshotEntity.getClusterServiceEntity();
                    clusterServiceEntity1.setClusterEntity(clusterEntity);
                    clusterServiceDao.save(clusterServiceEntity1);
                } else {
                    clusterServiceShapshotEntity.setClusterServiceEntity(clusterServiceEntity);
                }
                clusterServiceSnapshotDao.save(clusterServiceShapshotEntity);
            });
            //refresh
            clusterShapshotEntity.getClusterServiceShapshotEntityList();
            return clusterShapshotEntity;
        } catch (InvalidResponseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private HealthCheckResultsAccumulator performHealthChecks(String clusterName, Function<String, List<IServiceHealthCheckAction>> getServiceHealthCheckActionsFunction) {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
//        ExecutorService executorService = Executors.newFixedThreadPool( 4 );

        Flux.fromStream( getServiceHealthCheckActionsFunction.apply( clusterEntity.getClusterTypeEnum().name() ).stream() )
                .parallel().doOnNext( serviceHealthCheckAction -> {
            try {
                serviceHealthCheckAction.performHealthCheck(clusterEntity, healthCheckResultsAccumulator);
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
}
