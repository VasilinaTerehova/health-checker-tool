package com.epam.health.tool.facade.service.job;

import com.epam.health.tool.model.ClusterEntity;

public interface IServiceRunJobFacade {
    Object runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams );
}
