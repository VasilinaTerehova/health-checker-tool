package com.epam.facade.model.projection;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.BaseActionResult;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface MemoryUsageEntityProjection extends BaseActionResult {
    @Value( "#{target.used}" )
    int getUsed();
    @Value( "#{target.total}" )
    int getTotal();
    default HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.MEMORY;
    }
}
