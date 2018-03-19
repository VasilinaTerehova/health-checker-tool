package com.epam.health_tool.cluster.facade;

import com.epam.health_tool.authenticate.impl.ClusterCredentials;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public interface ClusterFacade {
    ClusterCredentials readClusterCredentials(String clusterName) throws ClusterNotFoundException;
}
