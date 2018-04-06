package com.epam.facade.model.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface NodeSnapshotEntityProjection {
    @Value( "#{target.node}" )
    String getNode();
    @Value( "#{target.fsUsageEntity.used}" )
    int getUsedGb();
    @Value( "#{target.fsUsageEntity.total}" )
    int getTotalGb();
}
