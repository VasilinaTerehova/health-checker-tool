package com.epam.health.tool.facade.cdh.service.log;

import com.epam.health.tool.facade.common.service.log.CommonServiceLogSearchFacade;
import com.epam.health.tool.facade.service.log.IServiceLogsSearcher;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("CDH-log-search-manager")
public class CdhServiceLogSearchFacade extends CommonServiceLogSearchFacade {
    private static final String HBASE_DEFAULT_LOG_PATH = "/var/log/hbase1";
    private static final String ZOOKEEPER_DEFAULT_LOG_PATH = "/var/log/zookeeper";

    @Override
    protected Map<ServiceTypeEnum, IServiceLogsSearcher> getLogSearchersMap() {
        Map<ServiceTypeEnum, IServiceLogsSearcher> serviceLogsSearcherMap = new HashMap<>();

        serviceLogsSearcherMap.put(ServiceTypeEnum.HBASE, createServiceLogSearcher( HBASE_LOG_PROPERTY, HBASE_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.ZOOKEEPER, createServiceLogSearcher( ZOOKEEPER_LOG_PROPERTY, ZOOKEEPER_DEFAULT_LOG_PATH ));

        return serviceLogsSearcherMap;
    }
}
