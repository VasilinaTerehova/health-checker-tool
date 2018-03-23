package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.util.ClusterEntityModifier;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTransformers;
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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setDestinationNameTransformer( NameTransformers.JAVABEANS_ACCESSOR );

        return modelMapper.map( clusterEntityProjection, ClusterEntity.class );
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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setDestinationNameTransformer( NameTransformers.JAVABEANS_ACCESSOR );

        return modelMapper.map( clusterEntity, ClusterEntityProjectionImpl.class );
    }
}
