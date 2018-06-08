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

package com.epam.health.tool.facade.hdp.cluster;

import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.TestHealthCheckerToolApplication;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
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
public class HdpClusterSnapshotFacadeImplTest {

    @Autowired
    @Qualifier("HDP-cluster")
    IClusterSnapshotFacade clusterSnapshotFacade;

    @Autowired
    private GetFsStatisticsAction getFsStatisticsAction;

    @Autowired
    @Qualifier("HDP-cluster")
    private IRunningClusterParamReceiver runningClusterParamReceiver;

    @Autowired
    private ClusterDao clusterDao;

    @Test
    public void testGetYarnLogDirectory() throws CommonUtilException, InvalidResponseException {
        assertEquals("/yarn/container-logs", runningClusterParamReceiver.getLogDirectory("HDP25Unsecure"));
    }

    @Test
    public void testGetNameNodeJson() throws CommonUtilException, InvalidResponseException, ImplementationNotResolvedException {
        System.out.println(runningClusterParamReceiver.getHdfsNamenodeJson("HDP26Unsecure"));
    }

    @Test
    public void testCollectHealthSummaryAccumulator() throws CommonUtilException, InvalidResponseException, ImplementationNotResolvedException {
        HealthCheckResultsAccumulator hdp26Unsecure = clusterSnapshotFacade.getLatestClusterSnapshot(ClusterAccumulatorToken.Builder.get().withClusterName("HDP26Unsecure").buildClusterAccumulatorToken());
        System.out.println(hdp26Unsecure);
    }

}
