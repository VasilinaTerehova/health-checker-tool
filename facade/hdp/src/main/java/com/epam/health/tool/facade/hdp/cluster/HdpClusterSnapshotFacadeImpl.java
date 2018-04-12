package com.epam.health.tool.facade.hdp.cluster;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.authentication.http.BaseHttpAuthenticatedAction;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_NODEMANAGER_LOG_DIRS;
import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_RESOURCEMANAGER_WEBAPP_ADDRESS;

@Component("HDP-cluster")
public class HdpClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    public List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);

        try {
            //Can be Flux.just( ServiceTypeEnum.values() ).#operations...
            return Arrays.stream(ServiceTypeEnum.values())
                    .map(serviceTypeEnum -> "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterName + "/services/" + serviceTypeEnum.toString())
                    .map(url -> httpAuthenticationClient.makeAuthenticatedRequest(clusterName, url))
                    .map(this::extractFromJsonString)
                    .filter(Objects::nonNull)
                    .map(this::mapHealthStateToServiceStatusEnum)
                    .map(this::mapDTOStatusToServiceStatus)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    @Override
    public String getLogDirectory(String clusterName) throws CommonUtilException {
        return getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS);
    }

    @Override
    public String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws CommonUtilException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);

        String siteNameNoExtension = siteName.substring(0, siteName.indexOf("."));

        //try {
        //todo: here refactor ordinary calls
        String configUrl = "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterEntity.getName() + "/configurations?type="+siteNameNoExtension+"&tag=version1";
        return CommonJsonHandler.get().getTypedValueFromInnerField(
                httpAuthenticationClient.makeAuthenticatedRequest( clusterName,configUrl), String.class, "items", "properties", propertyName);
    }

    //@Override
    public String getActiveResourceManagerAddress(String clusterName) throws CommonUtilException {
        return getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS);
    }

    private ServiceStatusDTO extractFromJsonString(String jsonString) {
        try {
            return CommonJsonHandler.get().getTypedValueFromInnerField(jsonString, ServiceStatusDTO.class, "ServiceInfo");
        } catch (CommonUtilException e) {
            throw new RuntimeException("Can't extract application list from answer - " + jsonString, e);
        }
    }

    private ServiceStatusDTO mapHealthStateToServiceStatusEnum(ServiceStatusDTO serviceStatusDTO) {
        serviceStatusDTO.setHealthStatus(ServiceStateEnumMapper.get().mapStringStateToEnum(serviceStatusDTO.getHealthStatus()).toString());
        return serviceStatusDTO;
    }

    private ServiceStatus mapDTOStatusToServiceStatus(ServiceStatusDTO serviceStatusDTO) {
        return svTransfererManager.<ServiceStatusDTO, ServiceStatus>getTransferer(ServiceStatusDTO.class, ServiceStatus.class)
                .transfer(serviceStatusDTO, ServiceStatus.class);
    }
}
