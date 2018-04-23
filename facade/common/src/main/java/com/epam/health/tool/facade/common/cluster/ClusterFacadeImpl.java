package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.ClusterNodes;
import com.epam.facade.model.ClusterSearchParam;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.dao.cluster.ClusterSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.util.ClusterEntityModifier;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ClusterFacadeImpl implements IClusterFacade {

    @Autowired
    private ClusterDao clusterDao;

    @Autowired
    private ClusterSnapshotDao clusterSnapshotDao;

    @Autowired
    private SVTransfererManager svTransfererManager;

    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver;

    @Override
    public List<ClusterEntityProjection> getClusterList() {
        return clusterDao.findAllProjections();
    }

    public ClusterEntityProjection getCluster(String name) {
        return clusterDao.findProjectionByClusterName( name );
    }

    @Override
    public String getClusterNameByParam( ClusterSearchParam clusterSearchParam ) {
        List<ClusterEntity> clusterEntities = StreamSupport.stream( clusterDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        String result = getClusterNameInClusterListByParams( clusterEntities, clusterSearchParam );

        return CheckingParamsUtil.isParamsNullOrEmpty( result ) ? getClusterNameInClusterListByNode( clusterEntities, clusterSearchParam.getNode() )
                : result;
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

    private String getClusterNameInClusterListByParams( List<ClusterEntity> clusterEntities, ClusterSearchParam clusterSearchParam ) {
        return clusterEntities.stream()
                .filter( clusterEntity -> clusterEntity.getHost() != null && isNodesEquals( clusterEntity.getHost(), clusterSearchParam.getNode() ) )
                .filter( clusterEntity -> clusterEntity.isSecured() && clusterSearchParam.isSecured() )
                .filter( clusterEntity -> clusterEntity.getClusterTypeEnum() != null
                        && clusterEntity.getClusterTypeEnum().equals( clusterSearchParam.getClusterType() ) )
                .map( ClusterEntity::getClusterName ).findFirst().orElse( StringUtils.EMPTY );
    }

    private String getClusterNameInClusterListByNode( List<ClusterEntity> clusterEntities, String node ) {
        return clusterEntities.stream().map( this::getClusterLiveNodes ).filter( clusterNodes -> isNodeBelongToClusterNodes( clusterNodes, node ) )
                .map( ClusterNodes::getClusterName ).findFirst().orElse( StringUtils.EMPTY );
    }

    private ClusterNodes getClusterLiveNodes(ClusterEntity clusterEntity ) {
        try {
            return new ClusterNodes( this.clusterParamReceiverIFacadeImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum().name() )
                    .getHdfsNamenodeJson( clusterEntity ).getLiveNodes(), clusterEntity.getClusterName() );
        } catch (InvalidResponseException | ImplementationNotResolvedException | CommonUtilException e) {
            return new ClusterNodes( Collections.emptySet(), clusterEntity.getClusterName() );
        }
    }

    private boolean isNodeBelongToClusterNodes( ClusterNodes clusterNodes, String node ) {
        return clusterNodes.getLiveNodes().stream().filter( Objects::nonNull ).anyMatch( liveNode -> isNodesEquals( liveNode, node ) );
    }

    private boolean isNodesEquals( String host1, String host2 ) {
        try {
            return host1.equals( host2 ) || InetAddress.getByName( host1 ).getHostAddress().equals( host2 );
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
