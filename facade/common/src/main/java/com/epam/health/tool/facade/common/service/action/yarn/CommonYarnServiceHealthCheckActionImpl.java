package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component( CommonActionNames.YARN_EXAMPLES )
public class CommonYarnServiceHealthCheckActionImpl extends CommonSshHealthCheckAction {
    private final static String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";
    private final static String ERROR_REGEXP = "Exception";
    private final static String IS_SUCCESS_REGEXP = ".*Job .* completed.*";

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        YarnHealthCheckResult yarnHealthCheckResult = new YarnHealthCheckResult();

        yarnHealthCheckResult.setJobResults(Collections.singletonList( runExamplesJob( clusterDao.findByClusterName(clusterName), "pi", "5", "10" ) ));
        yarnHealthCheckResult.setStatus( getYarnServiceStatus( yarnHealthCheckResult ) );

        healthCheckResultsAccumulator.setYarnHealthCheckResult( yarnHealthCheckResult );
    }

    private YarnHealthCheckResult.YarnJob runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams) {
        kinitOnClusterIfNecessary( clusterEntity );
        String pathToExamplesJar = HadoopClasspathJarSearcher.get().withSshCredentials( clusterEntity.getSsh() )
                .withHost( clusterEntity.getHost() ).findJobJarOnCluster( EXAMPLES_HADOOP_JAR_MASK );

        return representResultStringAsYarnJobObject( jobName, sshAuthenticationClient
                .executeCommand( clusterEntity, "yarn jar " + pathToExamplesJar + " " + jobName + " " + createJobParamsString( jobParams ) ));
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

    private boolean isAllYarnCheckSuccess(YarnHealthCheckResult yarnHealthCheckResult ) {
        return yarnHealthCheckResult.getJobResults().stream().allMatch(YarnHealthCheckResult.YarnJob::isSuccess);
    }

    private boolean isAnyYarnCheckSuccess(YarnHealthCheckResult yarnHealthCheckResult ) {
        return yarnHealthCheckResult.getJobResults().stream().anyMatch(YarnHealthCheckResult.YarnJob::isSuccess);
    }

    private boolean isNoneYarnCheckSuccess(YarnHealthCheckResult yarnHealthCheckResult ) {
        return yarnHealthCheckResult.getJobResults().stream().noneMatch(YarnHealthCheckResult.YarnJob::isSuccess);
    }

    private ServiceStatusEnum getYarnServiceStatus( YarnHealthCheckResult yarnHealthCheckResult ) {
        return isAllYarnCheckSuccess( yarnHealthCheckResult ) ? ServiceStatusEnum.GOOD
                : isNoneYarnCheckSuccess( yarnHealthCheckResult ) ? ServiceStatusEnum.BAD
                : ServiceStatusEnum.CONCERNING;
    }
}
