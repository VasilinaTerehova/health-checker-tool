package com.epam.health.tool.facade.hdp.cluster;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
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
                    .map(url -> httpAuthenticationClient.makeAuthenticatedRequest(clusterName, url, false))
                    .map(this::extractFromJsonString)
                    .filter(Objects::nonNull)
                    .map(this::mapHealthStateToServiceStatusEnum)
                    .map(this::mapDTOStatusToServiceStatus)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
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
