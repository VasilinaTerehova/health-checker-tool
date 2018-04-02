package com.epam.health.tool.facade.common.service.job;

import com.epam.health.tool.facade.service.job.IServiceRunJobFacade;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.ssh.SshCommonUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class CommonServiceRunJobFacadeImpl implements IServiceRunJobFacade {
    private final static String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";

    @Override
    public Object runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams) {
        String pathToExamplesJar = HadoopClasspathJarSearcher.get().withSshCredentials( clusterEntity.getSsh() )
                .withHost( clusterEntity.getHost() ).findJobJarOnCluster( EXAMPLES_HADOOP_JAR_MASK );

        try {
            return SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .executeCommand( clusterEntity.getHost(), "hadoop jar " + pathToExamplesJar + " " + jobName + createJobParamsString() );
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }

    private String createJobParamsString( String... params ) {
        return Arrays.stream( params ).collect(Collectors.joining( " " ));
    }
}
