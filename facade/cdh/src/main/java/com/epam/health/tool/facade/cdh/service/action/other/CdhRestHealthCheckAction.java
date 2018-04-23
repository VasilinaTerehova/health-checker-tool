package com.epam.health.tool.facade.cdh.service.action.other;

import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.other.CommonOtherServicesHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@HealthCheckAction( HealthCheckActionType.OTHER_SERVICES )
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhRestHealthCheckAction extends CommonOtherServicesHealthCheckAction {

    @Override
    protected List<ServiceStatus> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName() + "/services";

        String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), url, false );

        return extractFromJsonString(answer);
    }

    private List<ServiceStatus> extractFromJsonString(String jsonString) throws InvalidResponseException {
        try {
            return CommonJsonHandler.get().getListTypedValueFromInnerField(jsonString, ServiceStatus.class, "items");
        } catch (CommonUtilException e) {
            throw new InvalidResponseException("Can't extract application list from answer - " + jsonString, e);
        }
    }
}
