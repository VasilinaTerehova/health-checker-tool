package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClusterFacadeImpl implements IClusterFacade {
    @Autowired
    private ClusterDao clusterDao;

    @Override
    public List<ClusterEntityProjection> getClusterList() {
        return clusterDao.findAllProjections();
    }

    public ClusterEntityProjection getCluster(String name) {
        return clusterDao.findByClusterName( name );
    }

    public void saveCluster(String name) {

    }

    public void updateCluster(String name) {

    }

    public void deleteCluster(String name) {

    }
}
