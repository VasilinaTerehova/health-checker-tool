package com.epam.health.tool.facade.cdh.cluster;

import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.stereotype.Component;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {
    //Can be common instance only
}
