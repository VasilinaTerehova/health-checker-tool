package com.epam.health.tool.quartz;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vasilina_Terehova on 3/22/2018.
 */
@Component
public class ClusterHealthCheckJob {

    private static final Logger log = LoggerFactory.getLogger(ClusterHealthCheckJob.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    IClusterFacade clusterFacade;

    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;

    @Autowired
    ClusterServiceDao clusterServiceDao;

    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        //List<ClusterEntityProjection> clusterList = clusterFacade.getClusterList();
        Date hourAgo = new Date(System.currentTimeMillis() - 3600 * 1000);

        List<ClusterEntity> clusterEntities = clusterServiceSnapshotDao.findClustersForSnapshot(hourAgo);
        clusterEntities.stream().forEach(clusterEntity -> {
            ClusterHealthSummary clusterHealthSummary = clusterSnapshotFacade.askForCurrentClusterSnapshot(clusterEntity.getClusterName());
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            ClusterShapshotEntity clusterShapshotEntity = modelMapper.map(clusterHealthSummary, ClusterShapshotEntity.class);
            clusterShapshotEntity.setDateOfSnapshot(new Date());
            Long clusterId = clusterEntity.getId();
            //ClusterEntity clusterEntity = clusterDao.findById(clusterId).get();
            clusterShapshotEntity.setClusterEntity(clusterEntity);
            clusterSnapshotDao.save(clusterShapshotEntity);
            clusterHealthSummary.getServiceStatusList().stream().forEach(serviceStatus -> {
                ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterId, serviceStatus.getServiceType());
                ClusterServiceShapshotEntity clusterServiceShapshotEntity = modelMapper.map(serviceStatus, ClusterServiceShapshotEntity.class);
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
        });
    }
}
