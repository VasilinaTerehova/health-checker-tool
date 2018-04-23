package com.epam.facade.model.projection;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface HdfsUsageEntityProjection extends BaseActionResult {
    @Value( "#{target.used}" )
    long getUsedGb();
    @Value( "#{target.total}" )
    long getTotalGb();

    default HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.HDFS_MEMORY;
    }
}
