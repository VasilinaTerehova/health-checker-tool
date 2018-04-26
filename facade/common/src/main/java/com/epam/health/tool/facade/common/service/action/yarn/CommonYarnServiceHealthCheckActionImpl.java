package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(CommonActionNames.YARN_EXAMPLES)
@HealthCheckAction(HealthCheckActionType.YARN_SERVICE)
public class CommonYarnServiceHealthCheckActionImpl extends CommonSshHealthCheckAction {
    private final static String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";
    private final static String ERROR_REGEXP = "Exception";
    private final static String IS_SUCCESS_REGEXP = ".*Job .* completed.*";
    @Autowired
    private IFacadeImplResolver<IServiceStatusReceiver> serviceStatusReceiverIFacadeImplResolver;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {

        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        try {
            ServiceStatusHolder serviceStatus = getServiceStatus(clusterEntity, healthCheckResultsAccumulator);
            serviceStatus.setJobResults(Collections.singletonList(runExamplesJob(clusterEntity, "pi", "5", "10")));
            serviceStatus.setHealthSummary(mergeJobResultsWithRestStatus(serviceStatus.getHealthSummary(), getYarnServiceStatus(serviceStatus)));
            healthCheckResultsAccumulator.addServiceStatus(serviceStatus);
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException("Can't find according implementation for vendor " + clusterEntity.getClusterTypeEnum(), e);
        }
    }

    private ServiceStatusHolder getServiceStatus(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException, ImplementationNotResolvedException {
        Optional<ServiceStatusHolder> serviceHealthCheckResultIfExists = healthCheckResultsAccumulator.getServiceHealthCheckResultIfExists(ServiceTypeEnum.YARN);
        return serviceHealthCheckResultIfExists.orElse(serviceStatusReceiverIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum()).getServiceStatus(clusterEntity, ServiceTypeEnum.YARN));
    }

    private JobResultProjection runExamplesJob(ClusterEntity clusterEntity, String jobName, String... jobParams) {
        kinitOnClusterIfNecessary(clusterEntity);
        String pathToExamplesJar = HadoopClasspathJarSearcher.get().withSshCredentials(clusterEntity.getSsh())
                .withHost(clusterEntity.getHost()).findJobJarOnCluster(EXAMPLES_HADOOP_JAR_MASK);

        return representResultStringAsYarnJobObject(jobName, sshAuthenticationClient
                .executeCommand(clusterEntity, "yarn jar " + pathToExamplesJar + " " + jobName + " " + createJobParamsString(jobParams)));
    }

    private String createJobParamsString(String... params) {
        return Arrays.stream(params).collect(Collectors.joining(" "));
    }

    private JobResultProjection representResultStringAsYarnJobObject(String jobName, SshExecResult result) {
        YarnJobBuilder yarnJobBuilder = YarnJobBuilder.get().withName(jobName);
        Arrays.stream(result.getOutMessage().concat(result.getErrMessage()).split("\n")).forEach(line -> {
            this.setToYarnJob(yarnJobBuilder, line.trim());
        });

        return yarnJobBuilder.build();
    }

    private void setToYarnJob(YarnJobBuilder yarnJobBuilder, String line) {
        if (line.contains(ERROR_REGEXP)) {
            yarnJobBuilder.withErrors(line);
        }

        if (line.matches(IS_SUCCESS_REGEXP)) {
            yarnJobBuilder.withSuccess(line.contains("successfully"));
        }
    }

    private boolean isAllYarnCheckSuccess(ServiceStatusHolder yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().allMatch(JobResultProjection::isSuccess);
    }

    private boolean isAnyYarnCheckSuccess(ServiceStatusHolder yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private boolean isNoneYarnCheckSuccess(ServiceStatusHolder yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().noneMatch(JobResultProjection::isSuccess);
    }

    public static ServiceStatusEnum mergeJobResultsWithRestStatus(ServiceStatusEnum restCheck, ServiceStatusEnum jobResults) {
        return (restCheck.equals(ServiceStatusEnum.BAD) || restCheck.equals(ServiceStatusEnum.CONCERNING) && !jobResults.equals(ServiceStatusEnum.BAD)) ?
                ServiceStatusEnum.CONCERNING : jobResults;
    }

    private ServiceStatusEnum getYarnServiceStatus(ServiceStatusHolder yarnHealthCheckResult) {
        return isAllYarnCheckSuccess(yarnHealthCheckResult) ? ServiceStatusEnum.GOOD
                : isNoneYarnCheckSuccess(yarnHealthCheckResult) ? ServiceStatusEnum.BAD
                : ServiceStatusEnum.CONCERNING;
    }
}
