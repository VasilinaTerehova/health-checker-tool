package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component( CommonActionNames.YARN_EXAMPLES )
public class CommonYarnServiceHealthCheckActionImpl implements IServiceHealthCheckAction {
    private final static String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";
    private final static String ERROR_REGEXP = "Exception";
    private final static String IS_SUCCESS_REGEXP = ".*Job .* completed.*";
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;

    @Override
    public void performHealthCheck(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        YarnHealthCheckResult yarnHealthCheckResult = new YarnHealthCheckResult();
        yarnHealthCheckResult.setYarnJobs(Collections.singletonList( runExamplesJob( clusterEntity, "pi", "16", "1000" ) ));

        healthCheckResultsAccumulator.setYarnHealthCheckResult( yarnHealthCheckResult );
    }

    private YarnHealthCheckResult.YarnJob runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams) {
        kinitOnClusterIfNecessary( clusterEntity );
        String pathToExamplesJar = HadoopClasspathJarSearcher.get().withSshCredentials( clusterEntity.getSsh() )
                .withHost( clusterEntity.getHost() ).findJobJarOnCluster( EXAMPLES_HADOOP_JAR_MASK );

        return representResultStringAsYarnJobObject( jobName, sshAuthenticationClient
                .executeCommand( clusterEntity, "yarn jar " + pathToExamplesJar + " " + jobName + " " + createJobParamsString( jobParams ) ));
    }

    private void kinitOnClusterIfNecessary( ClusterEntity clusterEntity ) {
        if ( clusterEntity.isSecured() ) {
            sshAuthenticationClient.executeCommand( clusterEntity, "echo " + clusterEntity.getKerberos().getPassword()
                    + " | kinit " + clusterEntity.getKerberos().getUsername() );
        }
    }

    private String createJobParamsString( String... params ) {
        return Arrays.stream( params ).collect(Collectors.joining( " " ));
    }

    private YarnHealthCheckResult.YarnJob representResultStringAsYarnJobObject( String jobName, SshExecResult result ) {
        YarnJobBuilder yarnJobBuilder = YarnJobBuilder.get().withName( jobName );
        Arrays.stream( result.getOutMessage().concat( result.getErrMessage() ).split( "\n" ) ).forEach( line -> {
            this.setToYarnJob( yarnJobBuilder, line.trim() );
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
