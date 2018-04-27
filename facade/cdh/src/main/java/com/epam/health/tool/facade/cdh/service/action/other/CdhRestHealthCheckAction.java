package com.epam.health.tool.facade.cdh.service.action.other;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.facade.resolver.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.other.CommonOtherServicesHealthCheckAction;
import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.stereotype.Component;

@Component
@HealthCheckAction(HealthCheckActionType.OTHER_SERVICES)
@ClusterSpecificComponent(ClusterTypeEnum.CDH)
public class CdhRestHealthCheckAction extends CommonOtherServicesHealthCheckAction {

}
