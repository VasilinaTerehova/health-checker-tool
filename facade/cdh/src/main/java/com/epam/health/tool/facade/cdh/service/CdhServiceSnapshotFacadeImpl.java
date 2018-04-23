package com.epam.health.tool.facade.cdh.service;

import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.service.CommonServiceSnapshotFacadeImpl;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.stereotype.Component;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhServiceSnapshotFacadeImpl extends CommonServiceSnapshotFacadeImpl {
    public ClusterServiceShapshotEntity askForCurrentServiceSnapshot(String clusterName, String serviceName) {
        return null;
    }

}
