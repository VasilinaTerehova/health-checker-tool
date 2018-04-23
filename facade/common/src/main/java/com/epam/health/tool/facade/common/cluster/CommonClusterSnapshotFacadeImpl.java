package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.health.tool.dao.cluster.*;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.cluster.IHealthCheckFacade;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.*;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.function.BiConsumer;


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
    private IHealthCheckFacade healthCheckFacade;

    private Logger logger = Logger.getLogger(CommonClusterSnapshotFacadeImpl.class);
    private Map<HealthCheckActionType, BiConsumer<ClusterSnapshotEntity, HealthCheckResultsAccumulator>> healthActionSavers = new HashMap<>();

    {
        healthActionSavers.put(HealthCheckActionType.FS, (clusterShapshotEntity, healthCheckResultsAccumulator) -> {
            List<? extends NodeSnapshotEntityProjection> nodes = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getNodes();
            if (nodes != null) {
                nodes.forEach(o -> nodeSnapshotDao.save(new NodeSnapshotEntity(new FsUsageEntity(o.getUsedGb(), o.getTotalGb()), o.getNode(), clusterShapshotEntity)));
            } else {
                logger.error("full statistics for nodes didn't contain needed info");
            }
        });
        healthActionSavers.put(HealthCheckActionType.HDFS_MEMORY, (clusterShapshotEntity, healthCheckResultsAccumulator) -> {
            HdfsUsageEntityProjection hdfsUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getHdfsUsage();
            HdfsUsageEntity hdfsUsageEntity = new HdfsUsageEntity(hdfsUsage.getUsedGb(), hdfsUsage.getTotalGb());
            clusterShapshotEntity.setHdfsUsageEntity(hdfsUsageEntity);
        });
        healthActionSavers.put(HealthCheckActionType.MEMORY, (clusterShapshotEntity, healthCheckResultsAccumulator) -> {
            MemoryUsageEntityProjection memoryUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getMemoryUsage();
            MemoryUsageEntity memoryUsageEntity = new MemoryUsageEntity(memoryUsage.getUsed(), memoryUsage.getTotal());
            clusterShapshotEntity.setMemoryUsageEntity(memoryUsageEntity);
        });
        healthActionSavers.put(HealthCheckActionType.OTHER_SERVICES, (clusterShapshotEntity, healthCheckResultsAccumulator) -> {
            saveCommonServicesSnapshots(healthCheckResultsAccumulator, clusterShapshotEntity);
        });
        healthActionSavers.put(HealthCheckActionType.YARN_SERVICE, (clusterShapshotEntity, healthCheckResultsAccumulator) -> {
            YarnHealthCheckResult yarnHealthCheckResult = healthCheckResultsAccumulator.getYarnHealthCheckResult();

        });
    }

    @Override
    public List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName, int count) throws InvalidResponseException {
        Pageable top30 = new PageRequest(0, count);
        List<ClusterSnapshotEntityProjection> top30ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top30);
        ArrayList<ClusterHealthSummary> clusterHealthSummaries = new ArrayList<>();
        top30ClusterName.forEach(clusterSnapshotEntityProjection -> clusterHealthSummaries.add(new ClusterHealthSummary(clusterSnapshotEntityProjection, clusterServiceSnapshotDao.findServiceProjectionsBy(clusterSnapshotEntityProjection.getId()))));
        return clusterHealthSummaries;
    }

    @Override
    public HealthCheckResultsAccumulator makeClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) {
        try {
//            List<ClusterSnapshotEntityProjection> top10ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterAccumulatorToken.getClusterName().get(), new PageRequest(0, 1));
//            //for now getting and saving happen once an hour, to be revisited
//            if (!top10ClusterName.isEmpty() && new Date().before(DateUtil.dateHourPlus(top10ClusterName.get(0).getDateOfSnapshot()))) {
//                return clusterSnapshotDao.findById(top10ClusterName.get(0).getId()).get();
//            }
            ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterAccumulatorToken.getClusterName());

            HealthCheckResultsAccumulator healthCheckResultsAccumulatorNotFull = healthCheckFacade.askForClusterSnapshot(clusterAccumulatorToken);

            ClusterSnapshotEntity clusterSnapshotEntity = null;
            if (clusterAccumulatorToken.getToken() != null) {
                clusterSnapshotEntity = clusterSnapshotDao.findByToken(clusterAccumulatorToken.getToken());
            }
            if (clusterSnapshotEntity == null) {
                clusterSnapshotEntity = new ClusterSnapshotEntity();
                clusterSnapshotEntity.setDateOfSnapshot(new Date());
                clusterSnapshotEntity.setClusterEntity(clusterEntity);
                clusterSnapshotEntity.setToken(clusterAccumulatorToken.getToken());
            }

            clusterSnapshotDao.save(clusterSnapshotEntity);
            ClusterSnapshotEntity finalClusterSnapshotEntity = clusterSnapshotEntity;
            List<HealthCheckActionType> passedActionTypes = clusterAccumulatorToken.getPassedActionTypes();
            passedActionTypes.forEach(healthCheckActionType -> {
                BiConsumer<ClusterSnapshotEntity, HealthCheckResultsAccumulator> actionConsumer = healthActionSavers.get(healthCheckActionType);
                if (actionConsumer != null) {
                    actionConsumer.accept(finalClusterSnapshotEntity, healthCheckResultsAccumulatorNotFull);
                } else {
                    logger.error("action type " + healthCheckActionType + " can't be handled, no implementation found");
                }
            });

            //refresh
            clusterSnapshotDao.save(clusterSnapshotEntity);
            HealthCheckResultsAccumulator healthCheckResultsAccumulatorDb = new HealthCheckResultsAccumulator();
            healthCheckResultsAccumulatorDb.setClusterHealthSummary(
                    new ClusterHealthSummary(clusterSnapshotDao.findClusterSnapshotById(clusterSnapshotEntity.getId()), clusterServiceSnapshotDao.findServiceProjectionsBy(clusterSnapshotEntity.getId())));
            //todo: change here - yarn and hdfs should come with the same service entities, result in JobResultEntity
            healthCheckResultsAccumulatorDb.setYarnHealthCheckResult(healthCheckResultsAccumulatorNotFull.getYarnHealthCheckResult());
            healthCheckResultsAccumulatorDb.setHdfsHealthCheckResult(healthCheckResultsAccumulatorNotFull.getHdfsHealthCheckResult());
            if (HealthCheckActionType.containAllActionTypes(passedActionTypes)) {
                clusterSnapshotEntity.setFull(true);
                //here - clean previous for last hour
                //clusterSnapshotDao.clearForLastHour();
            }
            return healthCheckResultsAccumulatorDb;
        } catch (InvalidResponseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private void saveCommonServicesSnapshots(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterSnapshotEntity clusterSnapshotEntity) {
        ClusterEntity clusterEntity = clusterSnapshotEntity.getClusterEntity();
        healthCheckResultsAccumulator.getClusterHealthSummary().getServiceStatusList().forEach(serviceStatus -> {
            ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getType());
            ClusterServiceShapshotEntity clusterServiceShapshotEntity = svTransfererManager.<ServiceStatus, ClusterServiceShapshotEntity>getTransferer(ServiceStatus.class, ClusterServiceShapshotEntity.class).transfer((ServiceStatus) serviceStatus, ClusterServiceShapshotEntity.class);
            clusterServiceShapshotEntity.setClusterSnapshotEntity(clusterSnapshotEntity);
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

    @Override
    public HealthCheckResultsAccumulator getLatestClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) throws InvalidResponseException {
        List<ClusterHealthSummary> clusterSnapshotHistory = getClusterSnapshotHistory(clusterAccumulatorToken.getClusterName(), 1);
        if (clusterSnapshotHistory.size() == 0 || isTokenNotEmpty(clusterAccumulatorToken)) {
            return makeClusterSnapshot(clusterAccumulatorToken);
        } else {
            HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
            healthCheckResultsAccumulator.setClusterHealthSummary(clusterSnapshotHistory.get(0));
            return healthCheckResultsAccumulator;
        }
    }

    private boolean isTokenNotEmpty(ClusterAccumulatorToken clusterAccumulatorToken) {
        return clusterAccumulatorToken.getToken() != null && !clusterAccumulatorToken.getToken().trim().isEmpty() && !clusterAccumulatorToken.getToken().trim().equals("empty");
    }

    public void setClusterFacade(IClusterFacade clusterFacade) {
        this.clusterFacade = clusterFacade;
    }
}
