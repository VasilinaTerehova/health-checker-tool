package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
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

    @Test
    public void testGetYarnLogDirectory() throws CommonUtilException {
        assertEquals("/yarn/container-logs", clusterSnapshotFacade.getLogDirectory("HDP25Unsecure"));
    }
}
