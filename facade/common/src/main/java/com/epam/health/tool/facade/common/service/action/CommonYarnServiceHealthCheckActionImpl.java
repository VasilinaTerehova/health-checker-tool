package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component( CommonActionNames.YARN_EXAMPLES )
public class CommonYarnServiceHealthCheckActionImpl implements IServiceHealthCheckAction {
    private final static String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";
    private final static String ERROR_REGEXP = "Exception";
    private final static String IS_SUCCESS_REGEXP = "Job .* completed";
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;

    @Override
    public void performHealthCheck(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        YarnHealthCheckResult yarnHealthCheckResult = new YarnHealthCheckResult();
        yarnHealthCheckResult.setYarnJobs(Collections.singletonList( runExamplesJob( clusterEntity, "wordcount", "/wordcount/input11", "/wordcount/output" ) ));

        healthCheckResultsAccumulator.setYarnHealthCheckResult( yarnHealthCheckResult );
    }

    private YarnHealthCheckResult.YarnJob runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams) {
        String pathToExamplesJar = HadoopClasspathJarSearcher.get().withSshCredentials( clusterEntity.getSsh() )
                .withHost( clusterEntity.getHost() ).findJobJarOnCluster( EXAMPLES_HADOOP_JAR_MASK );

        return representResultStringAsYarnJobObject( jobName, sshAuthenticationClient
                .executeCommand( clusterEntity, "yarn jar " + pathToExamplesJar + " " + jobName + " " + createJobParamsString( jobParams ) ));
    }

    private String createJobParamsString( String... params ) {
        return Arrays.stream( params ).collect(Collectors.joining( " " ));
    }

    private YarnHealthCheckResult.YarnJob representResultStringAsYarnJobObject( String jobName, String result ) {
        YarnJobBuilder yarnJobBuilder = YarnJobBuilder.get().withName( jobName );
        Arrays.stream( result.split( "\n" ) ).forEach( line -> {
            this.setToYarnJob( yarnJobBuilder, line );
        } );

        return yarnJobBuilder.build();
    }

    private void setToYarnJob( YarnJobBuilder yarnJobBuilder, String line) {
        if ( line.contains( ERROR_REGEXP ) ) {
            yarnJobBuilder.withErrors( line );
        }

        if ( line.matches( IS_SUCCESS_REGEXP ) ) {
            yarnJobBuilder.withSuccess( line.contains( "successfully" ) );
        }
    }
}
