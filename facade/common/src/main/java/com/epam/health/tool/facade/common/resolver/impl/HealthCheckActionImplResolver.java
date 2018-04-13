package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsServiceHealthCheck;
import com.epam.health.tool.facade.common.service.action.yarn.CommonYarnServiceHealthCheckActionImpl;
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

    @Override
    public List<IServiceHealthCheckAction> resolveRestActionImplementations(String clusterType) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) )
                .filter( entry -> entry.getValue() instanceof CommonRestHealthCheckAction)
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }

    @Override
    public List<IServiceHealthCheckAction> resolveSshActionImplementations(String clusterType) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) || CommonActionNames.getNames().stream().anyMatch( name -> entry.getKey().contains( name ) ) )
                .filter( entry -> entry.getValue() instanceof CommonSshHealthCheckAction)
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }

    @Override
    public List<IServiceHealthCheckAction> resolveYarnActionImplementations(String clusterType) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) || CommonActionNames.getNames().stream().anyMatch( name -> entry.getKey().contains( name ) ) )
                .filter( entry -> entry.getValue() instanceof CommonYarnServiceHealthCheckActionImpl)
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }

    @Override
    public List<IServiceHealthCheckAction> resolveHdfsActionImplementations(String clusterType) {
        return serviceHealthCheckActionMap.entrySet().stream()
                .filter( entry -> entry.getKey().contains( clusterType ) || CommonActionNames.getNames().stream().anyMatch( name -> entry.getKey().contains( name ) ) )
                .filter( entry -> entry.getValue() instanceof CommonHdfsServiceHealthCheck)
                .map(Map.Entry::getValue ).collect(Collectors.toList());
    }
}
