package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.NodeDiskUsage;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
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
public class GetFsStatisticsAction extends CommonRestHealthCheckAction {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> runningClusterParamImplResolver;

    @Override
    protected ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl(null, null,
                        null, null, getAvailableDiskDfs(clusterEntity.getClusterName())));
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthSummary clusterHealthSummary) {
        ClusterHealthSummary tempClusterHealthSummary = healthCheckResultsAccumulator.getClusterHealthSummary();

        if (tempClusterHealthSummary == null) {
            tempClusterHealthSummary = clusterHealthSummary;
        } else {
            tempClusterHealthSummary = new ClusterHealthSummary(
                    new ClusterSnapshotEntityProjectionImpl(recreateClusterEntityProjection(tempClusterHealthSummary.getCluster()),
                            tempClusterHealthSummary.getServiceStatusList(), tempClusterHealthSummary.getCluster().getMemoryUsage(),
                            tempClusterHealthSummary.getCluster().getHdfsUsage(), clusterHealthSummary.getCluster().getNodes()));
        }

        healthCheckResultsAccumulator.setClusterHealthSummary(tempClusterHealthSummary);
    }

    private List<? extends NodeSnapshotEntityProjection> getAvailableDiskDfs(String clusterName) throws InvalidResponseException, ImplementationNotResolvedException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        List<NodeDiskUsage> nodeDiskUsages = new ArrayList<>();

        //Should be for all hosts
        //HostExtracter.getAllNodeNames(clusterEntity.getHost(), 3)
        //how get node count
        try {
            Set<String> liveNodes = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name()).getHdfsNamenodeJson(clusterEntity).getLiveNodes();
            for (String node : liveNodes) {
                String availableDiskDfs = getAvailableDiskDfsViaSsh(clusterEntity, node);
                nodeDiskUsages.add(mapAvailableDiskDfsStringToNodeDiskUsage(node, availableDiskDfs));
            }

            return nodeDiskUsages;
        } catch (CommonUtilException e) {
            throw new InvalidResponseException(e);
        }
        //http://svqxbdcn6hdp26n1.pentahoqa.com:8080/api/v1/clusters/HDP26Unsecure/configurations?type=yarn-site&tag=version1
        // /var/run/cloudera-scm-agent/process/253-yarn-NODEMANAGER/yarn-site.xml
        //df -h . | tail -1 | awk '{print $4}'
    }

    private String getAvailableDiskDfsViaSsh(ClusterEntity clusterEntity, String host) throws InvalidResponseException, ImplementationNotResolvedException {
        String logDirPropery = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name()).getLogDirectory(clusterEntity.getClusterName());

        //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - take nodemanager role
        //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles/NODEMANAGER/process/configFiles/yarn-site.xml - download file

        //for cloudera also
        //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - healthChecks,RESOURCE_MANAGER_LOG_DIRECTORY_FREE_SPACE - BAD
        //here for each node required
        //receive each node
        String command = "df -h " + logDirPropery + " | tail -1";
        System.out.println(command);
        String result = sshAuthenticationClient.executeCommand(clusterEntity, command, host).getOutMessage();
        System.out.println(result);

        return result;
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
