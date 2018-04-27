package com.epam.health.tool.facade.resolver.action;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.List;

public interface IActionImplResolver {
    List<IServiceHealthCheckAction> resolveActionImplementations( String clusterType, HealthCheckActionType healthCheckActionType );
    List<IServiceHealthCheckAction> resolveActionImplementations(ClusterTypeEnum clusterType, HealthCheckActionType healthCheckActionType );
}
