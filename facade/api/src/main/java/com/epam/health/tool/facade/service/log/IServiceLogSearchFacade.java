package com.epam.health.tool.facade.service.log;

import com.epam.health.tool.model.ServiceTypeEnum;

public interface IServiceLogSearchFacade {
    String searchLogs(String clusterName, ServiceTypeEnum serviceType);
}
