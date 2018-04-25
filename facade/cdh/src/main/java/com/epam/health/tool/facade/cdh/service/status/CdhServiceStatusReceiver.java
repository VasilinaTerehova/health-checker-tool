package com.epam.health.tool.facade.cdh.service.status;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.service.status.CommonServiceStatusReceiver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhServiceStatusReceiver extends CommonServiceStatusReceiver {
    public static final String API_V10_CLUSTERS = ":7180/api/v10/clusters/";
    public static final String SERVICES = "/services";

    @Override
    public List<ServiceStatusProjection> getServiceStatusList(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + API_V10_CLUSTERS + clusterEntity.getClusterName() + SERVICES;

            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url, false);

            return extractFromJsonString(answer);
        } catch (AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    @Override
    public ServiceStatusProjection getServiceStatus(ClusterEntity clusterEntity, ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + API_V10_CLUSTERS + clusterEntity.getClusterName() + SERVICES + "/" + serviceTypeEnum.name().toLowerCase();

            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url, false);

            return CommonJsonHandler.get().getTypedValue(answer, ServiceStatus.class);
        } catch (CommonUtilException | AuthenticationRequestException e) {
            throw new InvalidResponseException("Can't extract status for cluster " + clusterEntity.getHost() + " for service " + serviceTypeEnum.name(), e);
        }
    }

    private List<ServiceStatusProjection> extractFromJsonString(String jsonString) throws InvalidResponseException {
        try {
            return new ArrayList<>(CommonJsonHandler.get().getListTypedValueFromInnerField(jsonString, ServiceStatus.class, "items"));
        } catch (CommonUtilException e) {
            throw new InvalidResponseException("Can't extract application list from answer - " + jsonString, e);
        }
    }
}
