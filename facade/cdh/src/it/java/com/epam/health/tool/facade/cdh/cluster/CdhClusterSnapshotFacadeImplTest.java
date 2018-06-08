/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.health.tool.facade.common.service.action.fs.GetMemoryStatisticsAction;
import com.epam.facade.model.exception.InvalidResponseException;
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
