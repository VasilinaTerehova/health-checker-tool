package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.facade.model.projection.*;
import com.epam.health.tool.dao.cluster.*;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckActionImplResolver;
import com.epam.health.tool.facade.common.util.DateUtil;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.*;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    ClusterServiceDao clusterServiceDao;
    @Autowired
    ClusterDao clusterDao;

    @Autowired
    NodeSnapshotDao nodeSnapshotDao;

    @Autowired
    private HealthCheckActionImplResolver healthCheckActionImplResolver;

    private Logger logger = Logger.getLogger(CommonClusterSnapshotFacadeImpl.class);

    @Override
    public HealthCheckResultsAccumulator askForClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) throws InvalidResponseException {
        return performHealthChecks( clusterAccumulatorToken.getClusterName(), clusterAccumulatorToken.getHealthCheckActionType() );
    }

//    @Override
//    public ClusterHealthSummary getLastClusterSnapshot(String clusterName) {
//        receiveAndSaveClusterSnapshot(clusterDao.findByClusterName(clusterName));
//        Pageable top1 = new PageRequest(0, 1);
//        List<ClusterSnapshotEntityProjection> top1ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top1);
//        if (top1ClusterName.isEmpty()) {
//            return null;
//        }
//        ClusterSnapshotEntityProjection cluster = top1ClusterName.get(0);
//        return new ClusterHealthSummary(cluster, clusterServiceSnapshotDao.findServiceProjectionsBy(cluster.getId()));
//    }

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
            List<ClusterSnapshotEntityProjection> top10ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterEntity.getClusterName(), new PageRequest(0, 1));
            //for now getting and saving happen once an hour, to be revisited
            if (!top10ClusterName.isEmpty() && new Date().before(DateUtil.dateHourPlus(top10ClusterName.get(0).getDateOfSnapshot()))) {
                return clusterSnapshotDao.findById(top10ClusterName.get(0).getId()).get();
            }

            HealthCheckResultsAccumulator healthCheckResultsAccumulator = askForClusterSnapshot(ClusterAccumulatorToken.Builder.get()
                    .withClusterName( clusterEntity.getClusterName() ).withType( HealthCheckActionType.ALL.name() ).buildClusterAccumulatorToken());

            ClusterShapshotEntity clusterShapshotEntity = new ClusterShapshotEntity();
            clusterShapshotEntity.setDateOfSnapshot(new Date());
            clusterShapshotEntity.setClusterEntity(clusterEntity);
            clusterSnapshotDao.save(clusterShapshotEntity);
            //here - save memory, hdfs, fs
            HdfsUsageEntityProjection hdfsUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getHdfsUsage();
            HdfsUsageEntity hdfsUsageEntity = new HdfsUsageEntity(hdfsUsage.getUsedGb(), hdfsUsage.getTotalGb());
            clusterShapshotEntity.setHdfsUsageEntity(hdfsUsageEntity);

            MemoryUsageEntityProjection memoryUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getMemoryUsage();
            MemoryUsageEntity memoryUsageEntity = new MemoryUsageEntity(memoryUsage.getUsed(), memoryUsage.getTotal());
            clusterShapshotEntity.setMemoryUsageEntity(memoryUsageEntity);

            List<? extends NodeSnapshotEntityProjection> nodes = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getNodes();
            if (nodes != null) {
                nodes.forEach(o -> nodeSnapshotDao.save(new NodeSnapshotEntity(new FsUsageEntity(o.getUsedGb(), o.getTotalGb()), o.getNode(), clusterShapshotEntity)));
            } else {
                logger.error("full statistics for nodes didn't contain needed info");
            }

            saveCommonServicesSnapshots(clusterEntity, healthCheckResultsAccumulator, clusterShapshotEntity);
            //refresh
            clusterSnapshotDao.save(clusterShapshotEntity);
            //clusterShapshotEntity.getClusterServiceShapshotEntityList();
            return clusterShapshotEntity;
        } catch (InvalidResponseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private void saveCommonServicesSnapshots(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterShapshotEntity clusterShapshotEntity) {
        healthCheckResultsAccumulator.getClusterHealthSummary().getServiceStatusList().forEach(serviceStatus -> {
            ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getType());
            ClusterServiceShapshotEntity clusterServiceShapshotEntity = svTransfererManager.<ServiceStatus, ClusterServiceShapshotEntity>getTransferer(ServiceStatus.class, ClusterServiceShapshotEntity.class).transfer((ServiceStatus) serviceStatus, ClusterServiceShapshotEntity.class);
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
    }

    private HealthCheckResultsAccumulator performHealthChecks(String clusterName, HealthCheckActionType healthCheckType) {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();

        Flux.fromStream( healthCheckActionImplResolver.resolveActionImplementations( clusterEntity.getClusterTypeEnum().name(), healthCheckType ).stream() )
                .parallel().doOnNext( serviceHealthCheckAction -> {
            try {
                serviceHealthCheckAction.performHealthCheck(clusterEntity.getClusterName(), healthCheckResultsAccumulator);
            } catch (InvalidResponseException e) {
                throw new RuntimeException( e );
            }
        } ).subscribe();

        return healthCheckResultsAccumulator;
    }

    public void setClusterFacade(IClusterFacade clusterFacade) {
        this.clusterFacade = clusterFacade;
    }
}
