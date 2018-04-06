package com.epam.health.tool.facade.common.cluster;

import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Vasilina_Terehova on 4/3/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestHealthCheckerToolApplication.class)
public class CommonClusterSnapshotFacadeImplTest {

    @Autowired
    private IClusterSnapshotFacade clusterSnapshotFacade;

    @Test
    public void testGetDiskSpace() throws InvalidResponseException {
        clusterSnapshotFacade.getAvailableDiskHdfs("CDH512Unsecure");
    }

    @Test
    public void testGetMemory() throws InvalidResponseException {
        //svqxbdcn6cdh512n3.pentahoqa.com
        clusterSnapshotFacade.getMemoryTotal("CDH512Unsecure");
    }

    @Test
    public void testGetAvailableDiskDfs() {
        clusterSnapshotFacade.getAvailableDiskDfs("CDH512Unsecure");
    }
}
