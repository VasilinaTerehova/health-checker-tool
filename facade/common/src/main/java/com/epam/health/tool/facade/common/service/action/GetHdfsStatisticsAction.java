package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
public class GetHdfsStatisticsAction extends CommonRestHealthCheckAction {
    @Override
    protected List<ServiceStatus> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return null;
    }
}
