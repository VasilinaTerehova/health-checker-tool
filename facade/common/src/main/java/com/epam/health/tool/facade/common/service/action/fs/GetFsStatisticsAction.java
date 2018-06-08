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

package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.NodeDiskUsage;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
@Component( CommonActionNames.FS_CHECK )
@HealthCheckAction( HealthCheckActionType.FS )
public class GetFsStatisticsAction extends CommonRestHealthCheckAction<List<? extends NodeSnapshotEntityProjection>> {
    private static final Logger log = LoggerFactory.getLogger( GetFsStatisticsAction.class );

    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> runningClusterParamImplResolver;

    @Override
    protected List<? extends NodeSnapshotEntityProjection> performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException {
        return getAvailableDiskDfs(clusterEntity.getClusterName());
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                         List<? extends NodeSnapshotEntityProjection> healthCheckResult) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setNodeSnapshot( healthCheckResult ).modify();
    }

    private List<? extends NodeSnapshotEntityProjection> getAvailableDiskDfs(String clusterName) throws InvalidResponseException, ImplementationNotResolvedException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        List<NodeDiskUsage> nodeDiskUsages = new ArrayList<>();

        //Should be for all hosts
        //HostExtracter.getAllNodeNames(clusterEntity.getHost(), 3)
        //how get node count
        Set<String> liveNodes = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name())
                .getLiveNodes( clusterName );
        for (String node : liveNodes) {
            String availableDiskDfs = getAvailableDiskDfsViaSsh(clusterEntity, node);
            nodeDiskUsages.add(mapAvailableDiskDfsStringToNodeDiskUsage(node, availableDiskDfs));
        }

        return nodeDiskUsages;
        //http://svqxbdcn6hdp26n1.pentahoqa.com:8080/api/v1/clusters/HDP26Unsecure/configurations?type=yarn-site&tag=version1
        // /var/run/cloudera-scm-agent/process/253-yarn-NODEMANAGER/yarn-site.xml
        //df -h . | tail -1 | awk '{print $4}'
    }

    private String getAvailableDiskDfsViaSsh(ClusterEntity clusterEntity, String host) throws InvalidResponseException, ImplementationNotResolvedException {
        try {
            String localDirPropery = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name())
                    .getYarnLocalDirectory(clusterEntity.getClusterName());

            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - take nodemanager role
            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles/NODEMANAGER/process/configFiles/yarn-site.xml - download file

            //for cloudera also
            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - healthChecks,RESOURCE_MANAGER_LOG_DIRECTORY_FREE_SPACE - BAD
            //here for each node required
            //receive each node
            String command = "df -h " + localDirPropery + " | tail -1";
            String result = sshAuthenticationClient.executeCommand(clusterEntity, command, host).getOutMessage();
            log.info( "Running command - ".concat( command ).concat( "\nWith result:\n" ) + result );

            return result;
        }
        catch ( AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    private NodeDiskUsage mapAvailableDiskDfsStringToNodeDiskUsage(String host, String availableDiskDfs) throws InvalidResponseException {
        assertAvailableDiskDfsString(availableDiskDfs);
        String[] split = availableDiskDfs.trim().split("\\s+");
        NodeDiskUsage nodeDiskUsage = new NodeDiskUsage(host, split[1], split[0], split[3]);
        System.out.println(nodeDiskUsage);

        return nodeDiskUsage;
    }

    private void assertAvailableDiskDfsString(String availableDiskDfs) throws InvalidResponseException {
        if (CheckingParamsUtil.isParamsNullOrEmpty(availableDiskDfs) || availableDiskDfs.trim().split("\\s+").length < 3) {
            throw new InvalidResponseException("Not file usage can't be found on cluster");
        }
    }
}
