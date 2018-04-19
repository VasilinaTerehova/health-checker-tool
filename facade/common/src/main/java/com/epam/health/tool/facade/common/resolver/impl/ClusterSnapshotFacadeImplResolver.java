package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClusterSnapshotFacadeImplResolver extends CommonFacadeImplResolver<IClusterSnapshotFacade> {
    @Autowired
    private Map<String, IClusterSnapshotFacade> clusterSnapshotFacadeMap;

    @Override
    protected Map<String, IClusterSnapshotFacade> getFacadeImplBeansMap() {
        return clusterSnapshotFacadeMap;
    }
}
