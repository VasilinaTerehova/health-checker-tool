package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.health.tool.facade.common.service.action.fs.GetHdfsStatisticsAction;
import com.epam.health.tool.facade.common.service.action.fs.GetMemoryStatisticsAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/3/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestHealthCheckerToolApplication.class)
public class CommonClusterSnapshotFacadeImplTest {

    @Autowired
    private IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    private GetHdfsStatisticsAction getAvailableDiskHdfs;

    @Autowired
    private GetMemoryStatisticsAction getMemoryStatisticsAction;

    @Autowired
    private GetFsStatisticsAction getFsStatisticsAction;

    @Test
    public void testGetDiskSpace() throws InvalidResponseException {
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
        getAvailableDiskHdfs.performHealthCheck("CDH512Unsecure", healthCheckResultsAccumulator);
        healthCheckResultsAccumulator.getFsHealthCheckResult().getHdfsUsageEntityProjection();
    }

    @Test
    public void testGetMemory() throws InvalidResponseException {
        //svqxbdcn6cdh512n3.pentahoqa.com
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
        getMemoryStatisticsAction.performHealthCheck("CDH512Unsecure", healthCheckResultsAccumulator);
        healthCheckResultsAccumulator.getFsHealthCheckResult().getMemoryUsageEntityProjection();
    }

    @Test
    public void testGetAvailableDiskDfs() throws InvalidResponseException, ImplementationNotResolvedException {
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
        getFsStatisticsAction.performHealthCheck("CDH512Unsecure", healthCheckResultsAccumulator);
        List<? extends NodeSnapshotEntityProjection> nodes = healthCheckResultsAccumulator.getFsHealthCheckResult().getNodeSnapshotEntityProjections();
    }
}
