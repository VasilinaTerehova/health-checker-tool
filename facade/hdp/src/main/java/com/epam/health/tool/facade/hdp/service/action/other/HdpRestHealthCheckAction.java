package com.epam.health.tool.facade.hdp.service.action.other;

import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.other.CommonOtherServicesHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.hdp.cluster.ServiceStateEnumMapper;
import com.epam.health.tool.facade.hdp.cluster.ServiceStatusDTO;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@HealthCheckAction( HealthCheckActionType.OTHER_SERVICES )
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpRestHealthCheckAction extends CommonOtherServicesHealthCheckAction {

}
