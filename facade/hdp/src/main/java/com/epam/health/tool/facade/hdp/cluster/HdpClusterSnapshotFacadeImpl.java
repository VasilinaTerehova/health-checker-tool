package com.epam.health.tool.facade.hdp.cluster;

import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.stereotype.Component;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {
    //Can be common instance only
}
