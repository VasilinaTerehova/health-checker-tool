package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.health.tool.facade.common.service.action.fs.GetMemoryStatisticsAction;
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
public class CdhClusterSnapshotFacadeImplTest {

    @Autowired
    @Qualifier("CDH-cluster")
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    private GetFsStatisticsAction getFsStatisticsAction;

    @Autowired
    private GetMemoryStatisticsAction getMemoryStatisticsAction;

    @Autowired
    @Qualifier("CDH-cluster")
    private IRunningClusterParamReceiver iRunningClusterParamReceiver;

    @Test
    public void testGetYarnLogDirectory() throws CommonUtilException, InvalidResponseException {
        assertEquals("/yarn/container-logs", iRunningClusterParamReceiver.getLogDirectory("CDH512Unsecure"));
    }

//    @Test
//    public void testGetRmAddress() throws CommonUtilException, InvalidResponseException {
//        assertEquals("svqxbdcn6cdh512n3.pentahoqa.com:8088", getMemoryStatisticsAction.getActiveResourceManagerAddress("CDH512Unsecure"));
//    }

}
