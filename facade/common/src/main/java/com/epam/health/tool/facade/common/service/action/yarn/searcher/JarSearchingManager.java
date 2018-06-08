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

package com.epam.health.tool.facade.common.service.action.yarn.searcher;

import com.epam.health.tool.facade.common.resolver.ClusterSpecificResolver;
import com.epam.health.tool.facade.service.action.IJarSearcher;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JarSearchingManager extends ClusterSpecificResolver<IJarSearcher> {
    private static final Logger log = LoggerFactory.getLogger( JarSearchingManager.class );

    @Autowired
    private List<IJarSearcher> jarSearchers;

    @PostConstruct
    public void init() {
        this.jarSearchers.sort(Comparator.comparingInt(IJarSearcher::speedRating));
    }

    public String findJobJarOnCluster( String jarMask, String clusterName, ClusterTypeEnum clusterTypeEnum, String possiblePath ) {
        for ( IJarSearcher jarSearcher : getAvailableSearchers( clusterTypeEnum ) ) {
            String result = jarSearcher.searchJarPath( jarMask, clusterName, possiblePath );
            if ( CheckingParamsUtil.isParamsNotNullOrEmpty( result ) ) {
                return result;
            }
            else {
                //Possible path was incorrect, so skip it for next searchers
                possiblePath = StringUtils.EMPTY;
            }
        }

        return StringUtils.EMPTY;
    }

    private List<IJarSearcher> getAvailableSearchers( ClusterTypeEnum clusterType ) {
        return jarSearchers.stream().filter( iJarSearcher -> isAvailableForClusterType( iJarSearcher, clusterType ) || isNoneClusterType( iJarSearcher ) )
                .collect(Collectors.toList() );
    }
}
