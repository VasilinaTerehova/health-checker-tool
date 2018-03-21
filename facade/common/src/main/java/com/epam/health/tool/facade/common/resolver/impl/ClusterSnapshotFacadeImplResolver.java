package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.common.resolver.ImplementationNotResolvedException;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClusterSnapshotFacadeImplResolver implements IFacadeImplResolver<IClusterSnapshotFacade> {
    @Autowired
    private Map<String, IClusterSnapshotFacade> clusterSnapshotFacadeMap;

    @Override
    public IClusterSnapshotFacade resolveFacadeImpl(String clusterType) {
        return clusterSnapshotFacadeMap.entrySet().stream().filter( entry -> entry.getKey().contains( clusterType ) )
                .map(Map.Entry::getValue ).findFirst().orElse( new IClusterSnapshotFacade() {
                    @Override
                    public ClusterShapshotEntity getLastClusterSnapshot(String clusterName) {
                        throw new ImplementationNotResolvedException(  );
                    }

                    @Override
                    public ClusterHealthSummary askForCurrentClusterSnapshot(String clusterName) {
                        throw new ImplementationNotResolvedException(  );
                    }
                });
    }
}
