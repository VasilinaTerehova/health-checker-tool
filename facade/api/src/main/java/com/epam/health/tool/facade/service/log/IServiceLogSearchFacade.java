package com.epam.health.tool.facade.service.log;

import com.epam.facade.model.accumulator.LogLocation;
import com.epam.health.tool.model.ServiceTypeEnum;

public interface IServiceLogSearchFacade {
    LogLocation searchLogs(String clusterName, ServiceTypeEnum serviceType);
}
