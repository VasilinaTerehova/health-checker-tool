package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.util.ClusterEntityModifier;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClusterFacadeImpl implements IClusterFacade {

    @Autowired
    private ClusterDao clusterDao;

    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;

    @Autowired
    private SVTransfererManager svTransfererManager;

    @Override
    public List<ClusterEntityProjection> getClusterList() {
        return clusterDao.findAllProjections();
    }

    public ClusterEntityProjection getCluster(String name) {
        return clusterDao.findProjectionByClusterName( name );
    }

    @Override
    public ClusterEntityProjection saveCluster(ClusterEntityProjection clusterEntityProjection) {
        ClusterEntity clusterEntity = ClusterEntityModifier.get().withEntity( mapProjectionToEntity( clusterEntityProjection ) )
                .fillEmptyRequiredFields().nullEmptyCredentials().doModify();
        return mapEntityToProjection( clusterDao.save( clusterEntity ) );
    }

    @Override
    public ClusterEntityProjection updateCluster(ClusterEntityProjection clusterEntityProjection) {
        ClusterEntity clusterEntity = mapProjectionToEntity( clusterEntityProjection );
        clusterEntity = ClusterEntityModifier.get().withEntity( clusterEntity )
                .withIds( findClusterIds( clusterEntity ) ).fillEmptyRequiredFields()
                .nullAllIds().setIdsIfMissing().nullEmptyCredentials().doModify();
        return mapEntityToProjection( clusterDao.save( clusterEntity ) );
    }

    public void deleteCluster(String name) {
        clusterDao.deleteById( clusterDao.findIdsByClusterName( name ).getId() );
    }

    private ClusterEntity mapProjectionToEntity( ClusterEntityProjection clusterEntityProjection ) {
        return svTransfererManager.<ClusterEntityProjection, ClusterEntity>getTransferer( ClusterEntityProjection.class, ClusterEntity.class )
                .transfer( clusterEntityProjection, ClusterEntity.class );
    }

    private ClusterIdsProjection findClusterIds( ClusterEntity clusterEntity ) {
        //Name changed?
        ClusterIdsProjection clusterIdsProjection = clusterDao.findIdsByClusterId( clusterEntity.getId() );
        if ( clusterIdsProjection == null || clusterIdsProjection.getId() == null ) {
            clusterIdsProjection =  clusterDao.findIdsByClusterName( clusterEntity.getClusterName() );
        }

        return clusterIdsProjection;
    }

    private ClusterEntityProjection mapEntityToProjection( ClusterEntity clusterEntity ) {
        return svTransfererManager.<ClusterEntity, ClusterEntityProjectionImpl>getTransferer( ClusterEntity.class, ClusterEntityProjectionImpl.class )
                .transfer( clusterEntity, ClusterEntityProjectionImpl.class );
    }

}
