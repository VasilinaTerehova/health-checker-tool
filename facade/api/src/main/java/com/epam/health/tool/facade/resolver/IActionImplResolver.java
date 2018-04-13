package com.epam.health.tool.facade.resolver;

import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;

import java.util.List;

public interface IActionImplResolver {
    List<IServiceHealthCheckAction> resolveActionImplementations(String clusterType );
    //For parallel rest requests
    List<IServiceHealthCheckAction> resolveRestActionImplementations(String clusterType);
    List<IServiceHealthCheckAction> resolveSshActionImplementations(String clusterType);
    List<IServiceHealthCheckAction> resolveYarnActionImplementations(String clusterType);
    List<IServiceHealthCheckAction> resolveHdfsActionImplementations(String clusterType);
}
