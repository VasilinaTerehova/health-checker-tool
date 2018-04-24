package com.epam.health.tool.facade.hdp.service.action.other;

import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.other.CommonOtherServicesHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.hdp.cluster.ServiceStateEnumMapper;
import com.epam.health.tool.facade.hdp.cluster.ServiceStatusDTO;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@HealthCheckAction( HealthCheckActionType.OTHER_SERVICES )
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpRestHealthCheckAction extends CommonOtherServicesHealthCheckAction {
    @Override
    protected List<ServiceStatus> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            return Arrays.stream(ServiceTypeEnum.values())
                    .map(serviceTypeEnum -> "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterEntity.getClusterName() + "/services/" + serviceTypeEnum.toString())
                    .map(url -> makeHttpRequest( clusterEntity.getClusterName(), url, false ))
                    .map(this::extractFromJsonString)
                    .filter(Objects::nonNull)
                    .map(this::mapHealthStateToServiceStatusEnum)
                    .map(this::mapDTOStatusToServiceStatus)
                    .collect(Collectors.toList());
        }
        catch ( RuntimeException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    private String makeHttpRequest( String clusterName, String url, boolean useSpnego ) {
        try {
            return httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url, useSpnego );
        } catch (AuthenticationRequestException e) {
            throw new RuntimeException( e );
        }
    }

    private ServiceStatusDTO extractFromJsonString(String jsonString) {
        try {
            return CommonJsonHandler.get().getTypedValueFromInnerField(jsonString, ServiceStatusDTO.class, "ServiceInfo");
        } catch (CommonUtilException e) {
            throw new RuntimeException("Can't extract application list from answer - " + jsonString, e);
        }
    }

    private ServiceStatusDTO mapHealthStateToServiceStatusEnum( ServiceStatusDTO serviceStatusDTO ) {
        serviceStatusDTO.setHealthStatus( ServiceStateEnumMapper.get().mapStringStateToEnum( serviceStatusDTO.getHealthStatus() ).toString() );
        return serviceStatusDTO;
    }

    private ServiceStatus mapDTOStatusToServiceStatus(ServiceStatusDTO serviceStatusDTO) {
        return svTransfererManager.<ServiceStatusDTO, ServiceStatus>getTransferer(ServiceStatusDTO.class, ServiceStatus.class)
                .transfer(serviceStatusDTO, ServiceStatus.class);
    }
}
