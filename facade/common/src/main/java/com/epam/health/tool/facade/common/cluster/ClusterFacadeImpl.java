package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.util.ClusterEntityModifier;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ClusterFacadeImpl implements IClusterFacade {

    public static final int ONE_HOUR_MILLISECONDS = 3600 * 1000;

    private Logger logger = Logger.getLogger( ClusterFacadeImpl.class );
    @Autowired
    private ClusterDao clusterDao;

    @Autowired
    IClusterSnapshotFacade clusterSnapshotFacade;


    @Autowired
    ClusterSnapshotDao clusterSnapshotDao;

    @Autowired
    ClusterServiceDao clusterServiceDao;

    @Autowired
    ClusterServiceSnapshotDao clusterServiceSnapshotDao;

    @Autowired
    private SVTransfererManager svTransfererManager;

    @Override
    public List<ClusterEntityProjection> getClusterList() {
        return clusterDao.findAllProjections();
    }

    public ClusterEntityProjection getCluster(String name) {
        return clusterDao.findByClusterName( name );
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

    public void checkClustersHealth() {
        Date hourAgo = new Date(System.currentTimeMillis() - ONE_HOUR_MILLISECONDS);

        List<ClusterEntity> clusterEntities = clusterServiceSnapshotDao.findClustersForSnapshot(hourAgo);
        clusterEntities.forEach(clusterEntity -> {
           try {
               ClusterHealthSummary clusterHealthSummary = clusterSnapshotFacade.askForCurrentClusterSnapshot(clusterEntity.getClusterName());

               ClusterShapshotEntity clusterShapshotEntity = svTransfererManager.<ClusterHealthSummary, ClusterShapshotEntity> getTransferer(ClusterHealthSummary.class, ClusterShapshotEntity.class).transfer(clusterHealthSummary, ClusterShapshotEntity.class);
               clusterShapshotEntity.setDateOfSnapshot(new Date());
               Long clusterId = clusterEntity.getId();
               clusterShapshotEntity.setId(null);
               clusterShapshotEntity.setClusterEntity(clusterEntity);
               clusterSnapshotDao.save(clusterShapshotEntity);
               clusterHealthSummary.getServiceStatusList().forEach(serviceStatus -> {
                   ClusterServiceEntity clusterServiceEntity = clusterServiceDao.findByClusterIdAndServiceType(clusterId, serviceStatus.getServiceType());
                   ClusterServiceShapshotEntity clusterServiceShapshotEntity = svTransfererManager.<ServiceStatus, ClusterServiceShapshotEntity> getTransferer(ServiceStatus.class, ClusterServiceShapshotEntity.class).transfer(serviceStatus, ClusterServiceShapshotEntity.class);
                   clusterServiceShapshotEntity.setClusterShapshotEntity(clusterShapshotEntity);
                   if (clusterServiceEntity == null) {
                       ClusterServiceEntity clusterServiceEntity1 = clusterServiceShapshotEntity.getClusterServiceEntity();
                       clusterServiceEntity1.setClusterEntity(clusterEntity);
                       clusterServiceDao.save(clusterServiceEntity1);
                   } else {
                       clusterServiceShapshotEntity.setClusterServiceEntity(clusterServiceEntity);
                   }
                   clusterServiceSnapshotDao.save(clusterServiceShapshotEntity);
               });
           }
           catch ( InvalidResponseException e) {
               logger.error( e.getMessage() );
           }
        });
    }
}
