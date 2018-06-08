/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.common.util;

import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

public class ClusterEntityModifier {
    private ClusterEntity clusterEntity;
    private ClusterIdsProjection clusterIdsProjection;

    private ClusterEntityModifier() {}

    public static ClusterEntityModifier get() {
        return new ClusterEntityModifier();
    }

    public ClusterEntityModifier withEntity( ClusterEntity clusterEntity ) {
        assertValue( clusterEntity, "clusterEntity" );
        this.clusterEntity = clusterEntity;

        return this;
    }

    public ClusterEntityModifier withIds( ClusterIdsProjection clusterIdsProjection ) {
        assertValue( clusterIdsProjection, "clusterIdsProjection" );
        this.clusterIdsProjection = clusterIdsProjection;

        return this;
    }

    public ClusterEntityModifier fillEmptyRequiredFields( ) {
        assertValue( clusterEntity, "clusterEntity" );
        if ( CheckingParamsUtil.isParamsNull( clusterEntity.getTitle() ) ) {
            clusterEntity.setTitle( "" );
        }

        return this;
    }

    public ClusterEntityModifier setIdsIfMissing() {
        assertValue( clusterEntity, "clusterEntity" );
        assertValue( clusterIdsProjection, "clusterIdsProjection" );

        return setIdIfNull( clusterEntity.getId(), clusterIdsProjection.getId(), id -> clusterEntity.setId( id ) )
                .setIdIfNull( clusterEntity.getHttp().getId(), clusterIdsProjection.getHttpId(), id -> clusterEntity.getHttp().setId( id ) )
                .setIdIfNull( clusterEntity.getSsh().getId(), clusterIdsProjection.getSshId(), id -> clusterEntity.getSsh().setId( id ) )
                .setIdIfNull( clusterEntity.getKerberos().getId(), clusterIdsProjection.getKerberosId(), id -> clusterEntity.getKerberos().setId( id ) );
    }

    public ClusterEntityModifier nullAllIds() {
        assertValue( clusterEntity, "clusterEntity" );
        clusterEntity.setId( null );
        clusterEntity.getHttp().setId( null );
        clusterEntity.getSsh().setId( null );
        clusterEntity.getKerberos().setId( null );

        return this;
    }

    public ClusterEntityModifier nullEmptyCredentials() {
        if ( CheckingParamsUtil.isParamsNullOrEmpty( clusterEntity.getHttp().getUsername() ) && clusterEntity.getHttp().getId() == null ) {
            clusterEntity.setHttp( null );
        }

        if ( CheckingParamsUtil.isParamsNullOrEmpty( clusterEntity.getSsh().getUsername() ) && clusterEntity.getSsh().getId() == null ) {
            clusterEntity.setSsh( null );
        }

        if ( CheckingParamsUtil.isParamsNullOrEmpty( clusterEntity.getKerberos().getUsername() ) && clusterEntity.getKerberos().getId() == null ) {
            clusterEntity.setKerberos( null );
        }

        return this;
    }

    public ClusterEntity doModify() {
        return clusterEntity;
    }

    private ClusterEntityModifier setIdIfNull(Long targetId, Long replaceId, Consumer<Long> setIdAction) {
        if ( targetId == null && replaceId != null ) {
            setIdAction.accept( replaceId );
        }

        return this;
    }

    private void assertValue(Object object, String paramName ) {
        if ( object == null ) {
            throw new RuntimeException( "This param must be not null - " + paramName );
        }
    }
}
