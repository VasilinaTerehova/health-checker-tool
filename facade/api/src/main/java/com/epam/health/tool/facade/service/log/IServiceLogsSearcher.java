package com.epam.health.tool.facade.service.log;

import com.epam.facade.model.accumulator.LogLocation;

public interface IServiceLogsSearcher {
    LogLocation searchLogsLocation(String clusterName );
}
