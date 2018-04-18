package com.epam.health.tool.facade.resolver;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;

import java.util.List;

public interface IActionImplResolver {
    List<IServiceHealthCheckAction> resolveActionImplementations( String clusterType, HealthCheckActionType healthCheckActionType );
}
