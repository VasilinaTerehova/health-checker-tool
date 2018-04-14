package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestHealthCheckerToolApplication.class)
public class HdpClusterSnapshotFacadeImplTest {

    @Autowired
    @Qualifier("HDP-cluster")
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    private GetFsStatisticsAction getFsStatisticsAction;

    @Autowired
    @Qualifier("HDP-cluster")
    private IRunningClusterParamReceiver runningClusterParamReceiver;

    @Test
    public void testGetYarnLogDirectory() throws CommonUtilException, InvalidResponseException {
        assertEquals("/yarn/container-logs", runningClusterParamReceiver.getLogDirectory("HDP25Unsecure"));
    }
}
