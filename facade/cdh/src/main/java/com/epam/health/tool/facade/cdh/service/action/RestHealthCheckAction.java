package com.epam.health.tool.facade.cdh.service.action;

import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CDH-rest-action")
public class RestHealthCheckAction extends CommonRestHealthCheckAction {
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    @Override
    protected List<ServiceStatus> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName() + "/services";

        String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), url );

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
