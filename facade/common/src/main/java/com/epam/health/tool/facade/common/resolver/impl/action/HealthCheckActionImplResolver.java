package com.epam.health.tool.facade.common.resolver.impl.action;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.resolver.IActionImplResolver;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HealthCheckActionImplResolver implements IActionImplResolver {
    @Autowired
    private Map<String, IServiceHealthCheckAction> serviceHealthCheckActionMap;

    public List<IServiceHealthCheckAction> resolveActionImplementations( String clusterType, HealthCheckActionType healthCheckActionType ) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) || isCommonAction( entry.getKey() ) )
                .filter( entry -> filterByHealthCheckType(healthCheckActionType, entry))
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }

    private boolean filterByHealthCheckType(HealthCheckActionType healthCheckActionType, Map.Entry<String, IServiceHealthCheckAction> entry) {
        //if all - make all checks
        return getHealthCheckActionType( entry.getValue() ).equals( healthCheckActionType ) || healthCheckActionType.equals(HealthCheckActionType.ALL);
    }

    private HealthCheckActionType getHealthCheckActionType( IServiceHealthCheckAction healthCheckAction ) {
        if ( healthCheckAction.getClass().isAnnotationPresent( HealthCheckAction.class ) ) {
            return healthCheckAction.getClass().getAnnotation( HealthCheckAction.class ).value();
        }

        return HealthCheckActionType.NONE;
    }

    private boolean isCommonAction( String actionKey ) {
        return CommonActionNames.getNames().stream().anyMatch(actionKey::contains);
    }
}
