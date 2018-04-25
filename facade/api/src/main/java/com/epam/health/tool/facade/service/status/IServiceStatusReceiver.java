package com.epam.health.tool.facade.service.status;

import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceTypeEnum;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
public interface IServiceStatusReceiver {
    List<ServiceStatusProjection> getServiceStatusList(ClusterEntity clusterEntity) throws InvalidResponseException;

    ServiceStatusProjection getServiceStatus(ClusterEntity clusterEntity, ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException;
}
