package com.epam.facade.model.projection;

import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/27/2018.
 */
public interface ServiceStatusProjection {
    @Value("#{target.clusterServiceEntity.serviceType}")
    ServiceTypeEnum getType();

    @Value("#{target.healthStatus}")
    ServiceStatusEnum getHealthSummary();

    @Value("#{target.clusterServiceEntity.serviceType}")
    ServiceTypeEnum getDisplayName();

    @Value("#{target.jobResults}")
    List<JobResultProjection> getJobResults();
}
