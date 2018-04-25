package com.epam.health.tool.facade.hdp.service.action.other;

import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.other.CommonOtherServicesHealthCheckAction;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.stereotype.Component;

@Component
@HealthCheckAction( HealthCheckActionType.OTHER_SERVICES )
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpRestHealthCheckAction extends CommonOtherServicesHealthCheckAction {

}
