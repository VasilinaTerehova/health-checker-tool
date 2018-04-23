package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.results.impl.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("hdfs")
public class HdfsServiceHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        if ( !isHdfsCheckSuccess( healthCheckResultsAccumulator.getHdfsHealthCheckResult() ) ) {
            clusterHealthValidationResult.setClusterHealthy( false );
            clusterHealthValidationResult.appendErrorSummary( getHdfsCheckErrorsAsString( healthCheckResultsAccumulator.getHdfsHealthCheckResult() ) );
        }
    }

    private boolean isHdfsCheckSuccess( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return hdfsHealthCheckResult.getJobResults().stream().anyMatch( HdfsHealthCheckResult.HdfsOperationResult::isSuccess );
    }

    private String getHdfsCheckErrorsAsString( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return hdfsHealthCheckResult.getJobResults().stream().filter( hdfsOperationResult -> !hdfsOperationResult.isSuccess() )
                .map( this::getHdfsAlertString ).collect( Collectors.joining( "\n" ) );
    }

    private String getHdfsAlertString(HdfsHealthCheckResult.HdfsOperationResult hdfsOperationResult) {
        return hdfsOperationResult.getOperationName().concat( " - " ).concat( hdfsOperationResult.getAlert() );
    }
}
