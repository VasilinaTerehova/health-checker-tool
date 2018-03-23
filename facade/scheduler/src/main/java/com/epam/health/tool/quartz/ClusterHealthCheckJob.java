package com.epam.health.tool.quartz;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vasilina_Terehova on 3/22/2018.
 */
@Component
public class ClusterHealthCheckJob {

    private static final Logger log = LoggerFactory.getLogger(ClusterHealthCheckJob.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        clusterSnapshotFacade.checkClustersHealth();
    }
}
