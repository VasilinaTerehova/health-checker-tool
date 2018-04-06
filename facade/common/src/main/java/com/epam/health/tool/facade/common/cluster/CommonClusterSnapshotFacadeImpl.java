package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.facade.model.projection.*;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.*;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.date.util.DateUtil;
import com.epam.health.tool.facade.common.resolver.impl.HealthCheckActionImplResolver;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.HdfsNamenodeJson;
import com.epam.health.tool.facade.common.service.MemoryMetricsJson;
import com.epam.health.tool.facade.common.service.NodeDiskUsage;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.*;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.ssh.SshCommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;

public abstract class CommonClusterSnapshotFacadeImpl implements IClusterSnapshotFacade {

    @Autowired
    protected SVTransfererManager svTransfererManager;
    @Autowired
    protected IClusterFacade clusterFacade;
    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;
    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;
    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;
    @Autowired
    ClusterServiceDao clusterServiceDao;
    @Autowired
    ClusterDao clusterDao;

    @Autowired
    NodeSnapshotDao nodeSnapshotDao;

    @Autowired
    private HealthCheckActionImplResolver healthCheckActionImplResolver;

    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    private Logger logger = Logger.getLogger(CommonClusterSnapshotFacadeImpl.class);

    @Override
    public HealthCheckResultsAccumulator askForCurrentClusterSnapshot(String clusterName) throws InvalidResponseException {
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
        healthCheckResultsAccumulator.setClusterHealthSummary(performHealthChecks(clusterName, this::getRestHealthCheckActions).getClusterHealthSummary());
        return healthCheckResultsAccumulator;
    }

    @Override
    public HealthCheckResultsAccumulator askForCurrentFullHealthCheck(String clusterName) throws InvalidResponseException {
        return performHealthChecks(clusterName, this::getHealthCheckActions);
    }

    @Override
    public YarnHealthCheckResult askForCurrentYarnHealthCheck(String clusterName) throws InvalidResponseException {
        return performHealthChecks(clusterName, this::getYarnHealthCheckActions).getYarnHealthCheckResult();
    }

    @Override
    public ClusterHealthSummary getLastClusterSnapshot(String clusterName) {
        receiveAndSaveClusterSnapshot(clusterDao.findByClusterName(clusterName));
        Pageable top1 = new PageRequest(0, 1);
        List<ClusterSnapshotEntityProjection> top1ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top1);
        if (top1ClusterName.isEmpty()) {
            return null;
        }
        ClusterSnapshotEntityProjection cluster = top1ClusterName.get(0);
        return new ClusterHealthSummary(cluster, clusterServiceSnapshotDao.findServiceProjectionsBy(cluster.getId()));
    }

    @Override
    public List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName) throws InvalidResponseException {
        Pageable top30 = new PageRequest(0, 30);
        List<ClusterSnapshotEntityProjection> top30ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterName, top30);
        ArrayList<ClusterHealthSummary> clusterHealthSummaries = new ArrayList<>();
        top30ClusterName.forEach(clusterSnapshotEntityProjection -> clusterHealthSummaries.add(new ClusterHealthSummary(clusterSnapshotEntityProjection, clusterServiceSnapshotDao.findServiceProjectionsBy(clusterSnapshotEntityProjection.getId()))));
        return clusterHealthSummaries;
    }

    @Override
    public ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity) {
        try {
            List<ClusterSnapshotEntityProjection> top10ClusterName = clusterSnapshotDao.findTop10ClusterName(clusterEntity.getClusterName(), new PageRequest(0, 1));
            //for now getting and saving happen once an hour, to be revisited
            if (!top10ClusterName.isEmpty() && new Date().before(DateUtil.dateHourPlus(top10ClusterName.get(0).getDateOfSnapshot()))) {
                return clusterSnapshotDao.findById(top10ClusterName.get(0).getId()).get();
            }

            HealthCheckResultsAccumulator healthCheckResultsAccumulator = askForCurrentFullHealthCheck(clusterEntity.getClusterName());

            ClusterShapshotEntity clusterShapshotEntity = new ClusterShapshotEntity();
            clusterShapshotEntity.setDateOfSnapshot(new Date());
            clusterShapshotEntity.setClusterEntity(clusterEntity);
            clusterSnapshotDao.save(clusterShapshotEntity);

            //here - save memory, hdfs, fs
            HdfsUsageEntityProjection hdfsUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getHdfsUsage();
            HdfsUsageEntity hdfsUsageEntity = new HdfsUsageEntity(hdfsUsage.getUsedGb(), hdfsUsage.getTotalGb());
            clusterShapshotEntity.setHdfsUsageEntity(hdfsUsageEntity);

            MemoryUsageEntityProjection memoryUsage = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getMemoryUsage();
            MemoryUsageEntity memoryUsageEntity = new MemoryUsageEntity(memoryUsage.getUsed(), memoryUsage.getTotal());
            clusterShapshotEntity.setMemoryUsageEntity(memoryUsageEntity);

            List<? extends NodeSnapshotEntityProjection> nodes = healthCheckResultsAccumulator.getClusterHealthSummary().getCluster().getNodes();
            nodes.forEach(o -> nodeSnapshotDao.save(new NodeSnapshotEntity(new FsUsageEntity(o.getUsedGb(), o.getTotalGb()), o.getNode(), clusterShapshotEntity)));

            saveCommonServicesSnapshots(clusterEntity, healthCheckResultsAccumulator, clusterShapshotEntity);
            //refresh
            //clusterShapshotEntity.getClusterServiceShapshotEntityList();
            return clusterShapshotEntity;
        } catch (InvalidResponseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private void saveCommonServicesSnapshots(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterShapshotEntity clusterShapshotEntity) {
        healthCheckResultsAccumulator.getClusterHealthSummary().getServiceStatusList().forEach(serviceStatus -> {
            ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getType());
            ClusterServiceShapshotEntity clusterServiceShapshotEntity = svTransfererManager.<ServiceStatus, ClusterServiceShapshotEntity>getTransferer(ServiceStatus.class, ClusterServiceShapshotEntity.class).transfer((ServiceStatus) serviceStatus, ClusterServiceShapshotEntity.class);
            clusterServiceShapshotEntity.setClusterShapshotEntity(clusterShapshotEntity);
            if (clusterServiceEntity == null) {
                ClusterServiceEntity clusterServiceEntity1 = clusterServiceShapshotEntity.getClusterServiceEntity();
                clusterServiceEntity1.setClusterEntity(clusterEntity);
                clusterServiceDao.save(clusterServiceEntity1);
            } else {
                clusterServiceShapshotEntity.setClusterServiceEntity(clusterServiceEntity);
            }
            clusterServiceSnapshotDao.save(clusterServiceShapshotEntity);
        });
    }

    private HealthCheckResultsAccumulator performHealthChecks(String clusterName, Function<String, List<IServiceHealthCheckAction>> getServiceHealthCheckActionsFunction) {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        HealthCheckResultsAccumulator healthCheckResultsAccumulator = new HealthCheckResultsAccumulator();
//        ExecutorService executorService = Executors.newFixedThreadPool( 4 );

        Flux.fromStream(getServiceHealthCheckActionsFunction.apply(clusterEntity.getClusterTypeEnum().name()).stream())
                .parallel().doOnNext(serviceHealthCheckAction -> {
            try {
                serviceHealthCheckAction.performHealthCheck(clusterEntity, healthCheckResultsAccumulator);
            } catch (InvalidResponseException e) {
                throw new RuntimeException(e);
            }
        }).subscribe();
//        getServiceHealthCheckActionsFunction.apply( clusterEntity.getClusterTypeEnum().name() ).stream()
//                .map( serviceHealthCheckAction -> CompletableFuture.runAsync(() -> {
//                    try {
//                        serviceHealthCheckAction.performHealthCheck(clusterEntity, healthCheckResultsAccumulator);
//                    } catch (InvalidResponseException e) {
//                        throw new RuntimeException( e );
//                    }
//                }))
//                .forEach(CompletableFuture::join);

        return healthCheckResultsAccumulator;
    }

    private List<IServiceHealthCheckAction> getHealthCheckActions(String clusterType) {
        return healthCheckActionImplResolver.resolveActionImplementations(clusterType);
    }

    private List<IServiceHealthCheckAction> getRestHealthCheckActions(String clusterType) {
        return healthCheckActionImplResolver.resolveRestActionImplementations(clusterType);
    }

    private List<IServiceHealthCheckAction> getYarnHealthCheckActions(String clusterType) {
        return healthCheckActionImplResolver.resolveYarnActionImplementations(clusterType);
    }

    public HdfsUsageEntityProjection getAvailableDiskHdfs(String clusterName) throws InvalidResponseException {
        try {
            String nameNodeUrl = getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.HDFS, DFS_NAMENODE_HTTP_ADDRESS);
            String url = "http://" + nameNodeUrl + "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterName, url);
            System.out.println(answer);
            HdfsNamenodeJson hdfsUsageJson = CommonJsonHandler.get().getTypedValueFromInnerFieldArrElement(answer, HdfsNamenodeJson.class, "beans");
            System.out.println(hdfsUsageJson);
            return hdfsUsageJson;
        } catch (CommonUtilException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }

    public List<? extends NodeSnapshotEntityProjection> getAvailableDiskDfs(String clusterName) {
        //this is cloudera specific receiving of yarn.nodemanager.log-dirs
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);
        try {
            String logDirPropery = getLogDirectory(clusterName);

            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - take nodemanager role
            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles/NODEMANAGER/process/configFiles/yarn-site.xml - download file

            //for cloudera also
            //http://svqxbdcn6cdh513n1.pentahoqa.com:7180/api/v10/clusters/CDH513Unsecure/services/yarn/roles - healthChecks,RESOURCE_MANAGER_LOG_DIRECTORY_FREE_SPACE - BAD
            //here for each node required
            //receive each node
            String command = "df -h " + logDirPropery + " | tail -1";
            System.out.println(command);
            CredentialsProjection ssh = clusterEntity.getSsh();
            String result = SshCommonUtil.buildSshCommandExecutor(ssh.getUsername(), ssh.getPassword(), null/*, ssh.getPemFilePath()*/)
                    .executeCommand(clusterEntity.getHost(), command).getOutMessage();
            System.out.println(result);
            if (!CheckingParamsUtil.isParamsNullOrEmpty(result)) {
                String[] split = result.trim().split("\\s+");
                NodeDiskUsage nodeDiskUsage = new NodeDiskUsage(clusterEntity.getHost(), split[1], split[0], split[3]);
                System.out.println(nodeDiskUsage);
                ArrayList<NodeDiskUsage> nodeDiskUsages = new ArrayList<>();
                nodeDiskUsages.add(nodeDiskUsage);
                return nodeDiskUsages;
            }
            throw new RuntimeException("not file usage can't be found on cluster for the command" + command);
            //StringUtils.EMPTY;
        } catch (CommonUtilException ex) {
            throw new RuntimeException(ex);
        }
        //http://svqxbdcn6hdp26n1.pentahoqa.com:8080/api/v1/clusters/HDP26Unsecure/configurations?type=yarn-site&tag=version1
        // /var/run/cloudera-scm-agent/process/253-yarn-NODEMANAGER/yarn-site.xml
        //df -h . | tail -1 | awk '{print $4}'
    }


    public MemoryUsageEntityProjection getMemoryTotal(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);
        //todo: get active rm
        try {
            String activeResourceManagerAddress = getActiveResourceManagerAddress(clusterName);
            String url = "http://" + activeResourceManagerAddress + "/ws/v1/cluster/metrics";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterName, url);
            System.out.println(answer);
            MemoryMetricsJson memoryMetricsJson = CommonJsonHandler.get().getTypedValueFromInnerField(answer, MemoryMetricsJson.class, "clusterMetrics");
            System.out.println(memoryMetricsJson);
            return memoryMetricsJson;
        } catch (CommonUtilException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }

    public void setClusterFacade(IClusterFacade clusterFacade) {
        this.clusterFacade = clusterFacade;
    }
}
