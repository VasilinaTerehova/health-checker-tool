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

package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.results.impl.JobResultImpl;
import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.context.holder.StringContextHolder;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.common.service.action.yarn.searcher.JarSearchingManager;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.resolver.action.HealthCheckAction;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import com.epam.util.ssh.delegating.SshExecResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component( CommonActionNames.YARN_EXAMPLES )
@HealthCheckAction( HealthCheckActionType.YARN_SERVICE )
public class CommonYarnServiceHealthCheckActionImpl implements IServiceHealthCheckAction {
  private static final String EXAMPLES_HADOOP_JAR_MASK = "hadoop-mapreduce-examples";
  private static final String ERROR_REGEXP = "Exception";
  private static final String IS_SUCCESS_REGEXP = ".*Job .* completed.*";
  private static final String EXAMPLES_JAR_PATH_CACHE = "EXAMPLES_JAR_PATH_CACHE";
  @Autowired
  protected ClusterDao clusterDao;
  @Autowired
  private SshAuthenticationClient sshAuthenticationClient;
  @Autowired
  private IFacadeImplResolver<IServiceStatusReceiver> serviceStatusReceiverIFacadeImplResolver;
  @Autowired
  private IApplicationContext applicationContext;
  @Autowired
  private JarSearchingManager jarSearchingManager;
  @Autowired
  private IFacadeImplResolver<IServiceLogSearchFacade> serviceLogSearchManagerImplResolver;
  private static final Logger logger = Logger.getLogger( CommonYarnServiceHealthCheckActionImpl.class );

  @Override
  public void performHealthCheck( String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator )
    throws InvalidResponseException {

    ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );
    try {
      ServiceStatusHolder serviceStatus = getServiceStatus( clusterEntity );
      serviceStatus.setJobResults( Collections.singletonList( runExamplesJob( clusterEntity, "pi", "5", "10" ) ) );
      serviceStatus.setHealthSummary(
        mergeJobResultsWithRestStatus( serviceStatus.getHealthSummary(), getYarnServiceStatus( serviceStatus ) ) );
      addLogDirectory( clusterEntity, healthCheckResultsAccumulator, serviceStatus );
      healthCheckResultsAccumulator.addServiceStatus( serviceStatus );
    } catch ( ImplementationNotResolvedException e ) {
      throw new InvalidResponseException(
        "Can't find according implementation for vendor " + clusterEntity.getClusterTypeEnum(), e );
    }
  }

  private void addLogDirectory( ClusterEntity clusterEntity,
                                HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                ServiceStatusHolder serviceStatus ) {
    String clusterType = clusterEntity.getClusterTypeEnum().name();
    try {
      serviceLogSearchManagerImplResolver.resolveFacadeImpl( clusterType ).
        addLogsPathToService( healthCheckResultsAccumulator, serviceStatus, clusterEntity );
    } catch ( ImplementationNotResolvedException e ) {
      logger.error( "can't find implementation for " + clusterType + " for log service", e );
      throw new RuntimeException( e );
    }
  }

  private ServiceStatusHolder getServiceStatus( ClusterEntity clusterEntity )
    throws InvalidResponseException, ImplementationNotResolvedException {
    return serviceStatusReceiverIFacadeImplResolver
      .resolveFacadeImpl( clusterEntity.getClusterTypeEnum() ).getServiceStatus( clusterEntity, ServiceTypeEnum.YARN );
  }

  private JobResultProjection runExamplesJob( ClusterEntity clusterEntity, String jobName, String... jobParams )
    throws InvalidResponseException {
    String pathToExamplesJar = jarSearchingManager.findJobJarOnCluster( EXAMPLES_HADOOP_JAR_MASK,
      clusterEntity.getClusterName(), clusterEntity.getClusterTypeEnum(),
      getJarPathFromContext( clusterEntity.getClusterName() ) );
    saveJarPathToContextIfNotExists( clusterEntity.getClusterName(), pathToExamplesJar );

    try {
      return CheckingParamsUtil.isParamsNotNullOrEmpty( pathToExamplesJar ) ?
        representResultStringAsYarnJobObject( jobName, sshAuthenticationClient
          .executeCommand( clusterEntity,
            "yarn jar " + pathToExamplesJar + " " + jobName + " " + createJobParamsString( jobParams ), true ) )
        : createFailedJob( jobName, "Can't find job jar on cluster!" );
    } catch ( AuthenticationRequestException e ) {
      throw new InvalidResponseException( e );
    }
  }

  private String getJarPathFromContext( String clusterName ) {
    return applicationContext
      .getFromContext( clusterName, EXAMPLES_JAR_PATH_CACHE, StringContextHolder.class, StringUtils.EMPTY );
  }

  private void saveJarPathToContextIfNotExists( String clusterName, String jarPath ) {
    if ( CheckingParamsUtil.isParamsNotNullOrEmpty( jarPath ) ) {

      applicationContext.addToContextIfAbsent( clusterName, EXAMPLES_JAR_PATH_CACHE, StringContextHolder.class,
        new StringContextHolder( jarPath ) );
    }
  }

  private String createJobParamsString( String... params ) {
    return Arrays.stream( params ).collect( Collectors.joining( " " ) );
  }

  private JobResultProjection representResultStringAsYarnJobObject( String jobName, SshExecResult result ) {
    try {
      YarnJobBuilder yarnJobBuilder = YarnJobBuilder.get().withName( jobName );
      Arrays.stream( result.getOutMessage().concat( result.getErrMessage().trim() ).split( "\n" ) )
        .filter( CheckingParamsUtil::isParamsNotNullOrEmpty )
        .forEach( line -> this.setToYarnJob( yarnJobBuilder, line.trim() ) );

      return yarnJobBuilder.build();
    } catch ( InvalidBuildParamsException ex ) {
      return createFailedJob( jobName, ex.getMessage() );
    }
  }

  private void setToYarnJob( YarnJobBuilder yarnJobBuilder, String line ) {
    if ( line.contains( ERROR_REGEXP ) ) {
      yarnJobBuilder.withErrors( line );
    }

    if ( line.matches( IS_SUCCESS_REGEXP ) ) {
      yarnJobBuilder.withSuccess( line.contains( "successfully" ) );
    }
  }

  private boolean isAllYarnCheckSuccess( ServiceStatusHolder yarnHealthCheckResult ) {
    return yarnHealthCheckResult.getJobResults().stream().allMatch( JobResultProjection::isSuccess );
  }

  private boolean isAnyYarnCheckSuccess( ServiceStatusHolder yarnHealthCheckResult ) {
    return yarnHealthCheckResult.getJobResults().stream().anyMatch( JobResultProjection::isSuccess );
  }

  private boolean isNoneYarnCheckSuccess( ServiceStatusHolder yarnHealthCheckResult ) {
    return yarnHealthCheckResult.getJobResults().stream().noneMatch( JobResultProjection::isSuccess );
  }

  public static ServiceStatusEnum mergeJobResultsWithRestStatus( ServiceStatusEnum restCheck,
                                                                 ServiceStatusEnum jobResults ) {
    return
      ( restCheck.equals( ServiceStatusEnum.BAD ) || restCheck.equals( ServiceStatusEnum.CONCERNING ) && !jobResults
        .equals( ServiceStatusEnum.BAD ) ) ?
        ServiceStatusEnum.CONCERNING : jobResults;
  }

  private ServiceStatusEnum getYarnServiceStatus( ServiceStatusHolder yarnHealthCheckResult ) {
    return isAllYarnCheckSuccess( yarnHealthCheckResult ) ? ServiceStatusEnum.GOOD
      : isNoneYarnCheckSuccess( yarnHealthCheckResult ) ? ServiceStatusEnum.BAD
      : ServiceStatusEnum.CONCERNING;
  }

  private JobResultProjection createFailedJob( String jobName, String alertMessage ) {
    return new JobResultImpl( jobName, false, Collections.singletonList( alertMessage ) );
  }
}
