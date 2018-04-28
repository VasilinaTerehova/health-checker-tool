package com.epam.health.tool.facade.hdp.service.action.yarn.searcher;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.yarn.searcher.impl.DefaultPathJarSearcher;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpDefaultPathSearcher extends DefaultPathJarSearcher {
    private final static Logger log = LoggerFactory.getLogger( HdpDefaultPathSearcher.class );
    private final static String PATH_TO_EXAMPLES_JAR = "/usr/hdp/current/hadoop-mapreduce-client";

    @Autowired
    public HdpDefaultPathSearcher(SshAuthenticationClient sshAuthenticationClient) {
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
