/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.cdh.service.log;

import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.service.log.CommonServiceLogSearchFacade;
import com.epam.health.tool.facade.service.log.IServiceLogsSearcher;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhServiceLogSearchFacade extends CommonServiceLogSearchFacade {
    private static final String HBASE_DEFAULT_LOG_PATH = "/var/log/hbase";
    private static final String HIVE_DEFAULT_LOG_PATH = "/var/log/hive";
    private static final String OOZIE_DEFAULT_LOG_PATH = "/var/log/oozie";
    private static final String SQOOP_DEFAULT_LOG_PATH = "/var/log/sqoop";
    private static final String HDFS_DEFAULT_LOG_PATH = "/var/log/hdfs";
    private static final String ZOOKEEPER_DEFAULT_LOG_PATH = "/var/log/zookeeper";

    @Override
    protected Map<ServiceTypeEnum, IServiceLogsSearcher> getLogSearchersMap() {
        Map<ServiceTypeEnum, IServiceLogsSearcher> serviceLogsSearcherMap = new HashMap<>();

        serviceLogsSearcherMap.put(ServiceTypeEnum.HBASE, createServiceLogSearcher( HBASE_LOG_PROPERTY, HBASE_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.HIVE, createServiceLogSearcher( HIVE_LOG_PROPERTY, HIVE_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.HDFS, createServiceLogSearcher( HDFS_LOG_PROPERTY, HDFS_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.SQOOP, createServiceLogSearcher( SQOOP_LOG_PROPERTY, SQOOP_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.OOZIE, createServiceLogSearcher( OOZIE_LOG_PROPERTY, OOZIE_DEFAULT_LOG_PATH ));
        serviceLogsSearcherMap.put(ServiceTypeEnum.ZOOKEEPER, createServiceLogSearcher( ZOOKEEPER_LOG_PROPERTY, ZOOKEEPER_DEFAULT_LOG_PATH ));

        return serviceLogsSearcherMap;
    }
}
