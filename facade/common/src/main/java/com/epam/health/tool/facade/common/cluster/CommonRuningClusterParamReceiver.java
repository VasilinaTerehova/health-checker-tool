package com.epam.health.tool.facade.common.cluster;

import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.exception.InvalidResponseException;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public abstract class CommonRuningClusterParamReceiver implements IRunningClusterParamReceiver {
    public abstract String getLogDirectory(String clusterName) throws InvalidResponseException;


}
