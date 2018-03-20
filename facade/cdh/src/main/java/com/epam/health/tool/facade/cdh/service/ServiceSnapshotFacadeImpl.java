package com.epam.health.tool.facade.cdh.service;

import com.epam.health.tool.facade.common.service.CommonServiceSnapshotFacadeImpl;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import org.springframework.stereotype.Component;

@Component("CDH")
public class ServiceSnapshotFacadeImpl extends CommonServiceSnapshotFacadeImpl {
    public ClusterServiceShapshotEntity askForCurrentServiceSnapshot(String clusterName, String serviceName) {
        return null;
    }
}
