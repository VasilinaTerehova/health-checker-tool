package com.epam.health.tool.facade.cdh.service.action.fs;

import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import org.springframework.stereotype.Component;

//Is necessary?
@Component( "CDH-fs-statistics" )
@HealthCheckAction( HealthCheckActionType.FS )
public class CdhGetFsStatisticsAction extends GetFsStatisticsAction {


}
