package com.epam.health.tool.quartz;

import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.util.DateUtil;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/22/2018.
 */
@Component
public class ClusterHealthCheckJob {

    private static final Logger log = LoggerFactory.getLogger(ClusterHealthCheckJob.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;

    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;

    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void checkClustersHealth() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        Date hourAgo = DateUtil.dateHourPlus(new Date());

        List<ClusterEntity> clusterEntities = clusterServiceSnapshotDao.findClustersForSnapshot(hourAgo);
        clusterEntities.forEach(clusterEntity -> {

            try {
                clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name()).receiveAndSaveClusterSnapshot(clusterEntity);
            } catch (ImplementationNotResolvedException e) {
                log.error("Can't find facade implementation for this vendor ", e);
            }

        });
    }

}
