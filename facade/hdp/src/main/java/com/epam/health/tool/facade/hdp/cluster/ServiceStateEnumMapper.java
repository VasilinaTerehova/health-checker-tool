package com.epam.health.tool.facade.hdp.cluster;

import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ServiceStateEnumMapper {
    private Map<String, ServiceStatusEnum> serviceStatusEnumMap;

    private ServiceStateEnumMapper() {
        this.serviceStatusEnumMap = new HashMap<>();
        this.serviceStatusEnumMap.put( "STARTED", ServiceStatusEnum.GOOD );
        this.serviceStatusEnumMap.put( "INSTALLED", ServiceStatusEnum.BAD );
    }

    //Can be singleton
    public static ServiceStateEnumMapper get() {
        return new ServiceStateEnumMapper();
    }

    public ServiceStatusEnum mapStringStateToEnum( String state ) {
        return this.serviceStatusEnumMap.getOrDefault( toUpperCase( state ), ServiceStatusEnum.CONCERNING );
    }

    private String toUpperCase( String state ) {
        return !CheckingParamsUtil.isParamsNullOrEmpty( state ) ? state.toUpperCase() : StringUtils.EMPTY;
    }

}
