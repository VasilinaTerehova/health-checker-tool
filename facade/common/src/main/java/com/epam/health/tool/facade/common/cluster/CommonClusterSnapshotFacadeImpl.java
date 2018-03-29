package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CommonClusterSnapshotFacadeImpl implements IClusterSnapshotFacade {

    public static final int ONE_HOUR_MILLISECONDS = 3600 * 1000;

    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;

    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;

    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    ClusterServiceDao clusterServiceDao;

    private Logger logger = Logger.getLogger(CommonClusterSnapshotFacadeImpl.class);

    @Autowired
    protected SVTransfererManager svTransfererManager;

    @Autowired
    ClusterDao clusterDao;

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

    public List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName) throws InvalidResponseException {
        Pageable top30 = new PageRequest(0, 30);
        List<ClusterSnapshotEntityProjection> top30ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top30);
        ArrayList<ClusterHealthSummary> clusterHealthSummaries = new ArrayList<>();
        top30ClusterName.forEach(clusterSnapshotEntityProjection -> clusterHealthSummaries.add(new ClusterHealthSummary(clusterSnapshotEntityProjection)));
        return clusterHealthSummaries;
    }

    public void checkClustersHealth() {
        Date hourAgo = dateHourAgo();

        List<ClusterEntity> clusterEntities = clusterServiceSnapshotDao.findClustersForSnapshot(hourAgo);
        clusterEntities.forEach(clusterEntity -> {

            clusterSnapshotFacade.receiveAndSaveClusterSnapshot(clusterEntity);

        });
    }

    private Date dateHourAgo() {
        return new Date(System.currentTimeMillis() - ONE_HOUR_MILLISECONDS);
    }

    private Date dateHourPlus(Date date) {
        return new Date(date.getTime() + ONE_HOUR_MILLISECONDS);
    }

    public ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity) {
        try {
            List<ServiceStatus> serviceStatusList = askForCurrentClusterSnapshot(clusterEntity.getClusterName());
            List<ClusterSnapshotEntityProjection> top10ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterEntity.getClusterName(), new PageRequest(0, 1));
            //for now getting and saving happen once an hour, to be revisited
            if (!top10ClusterName.isEmpty() && new Date().before(dateHourPlus(top10ClusterName.get(0).getDateOfSnapshot()))) {
                return clusterSnapshotDao.findById(top10ClusterName.get(0).getId()).get();
            }

            ClusterShapshotEntity clusterShapshotEntity = new ClusterShapshotEntity();
            clusterShapshotEntity.setDateOfSnapshot(new Date());
            clusterShapshotEntity.setClusterEntity(clusterEntity);
            clusterSnapshotDao.save(clusterShapshotEntity);
            serviceStatusList.forEach(serviceStatus -> {
                ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getServiceType());
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
}
