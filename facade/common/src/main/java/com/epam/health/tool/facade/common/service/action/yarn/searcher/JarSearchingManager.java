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
