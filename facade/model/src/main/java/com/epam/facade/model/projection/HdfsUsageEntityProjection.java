package com.epam.facade.model.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface HdfsUsageEntityProjection {
    @Value( "#{target.used}" )
    long getUsedGb();
    @Value( "#{target.total}" )
    long getTotalGb();
}
