package com.epam.facade.model.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
public interface JobResultProjection {
    @Value("#{target.jobName}")
    String getName();

    @Value("#{target.result}")
    boolean isSuccess();

    @Value("#{target.alerts}")
    List<String> getAlerts();

}
