package com.epam.health.tool.cluster.facade;

import com.epam.health.tool.authenticate.impl.ClusterCredentials;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public interface ClusterFacade {
    ClusterCredentials readClusterCredentials(String clusterName) throws ClusterNotFoundException;
}
