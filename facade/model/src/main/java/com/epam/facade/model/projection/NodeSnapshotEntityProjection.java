package com.epam.facade.model.projection;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface NodeSnapshotEntityProjection extends BaseActionResult {
    @Value( "#{target.node}" )
    String getNode();
    @Value( "#{target.fsUsageEntity.used}" )
    int getUsedGb();
    @Value( "#{target.fsUsageEntity.total}" )
    int getTotalGb();
    default HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.FS;
    }
}
