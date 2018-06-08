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

package com.epam.health.tool.facade.cdh.service.action.yarn.searcher;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.yarn.searcher.impl.DefaultPathJarSearcher;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhDefaultPathJarSearcher extends DefaultPathJarSearcher {
    private final static Logger log = LoggerFactory.getLogger( CdhDefaultPathJarSearcher.class );
    private final static String PATH_TO_EXAMPLES_JAR = "/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce";

    @Autowired
    public CdhDefaultPathJarSearcher(SshAuthenticationClient sshAuthenticationClient) {
        super(sshAuthenticationClient);
    }

    @Override
    protected String getDefaultPath() {
        return PATH_TO_EXAMPLES_JAR;
    }

    @Override
    protected Logger log() {
        return log;
    }
}
