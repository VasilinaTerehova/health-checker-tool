package com.epam.health.tool.facade.common.resolver.impl;

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

    @Override
    public List<IServiceHealthCheckAction> resolveActionImplementations(String clusterType) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) || CommonActionNames.getNames().stream().anyMatch( name -> entry.getKey().contains( name ) ))
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }
}
