package com.epam.health.tool.facade.common.cluster;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClusterFacadeImpl implements IClusterFacade {
    @Override
    public List<ClusterEntity> getClusterList() {
        return new ArrayList<>();
    }

    public ClusterEntity getCluster(String name) {
        return new ClusterEntity();
    }

    public void saveCluster(String name) {

    }

    public void updateCluster(String name) {

    }

    public void deleteCluster(String name) {

    }
}
