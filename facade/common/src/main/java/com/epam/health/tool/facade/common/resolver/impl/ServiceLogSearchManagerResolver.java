package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceLogSearchManagerResolver extends CommonFacadeImplResolver<IServiceLogSearchFacade> {
    @Autowired
    private Map<String, IServiceLogSearchFacade> serviceLogSearchManagerMap;

    @Override
    protected Map<String, IServiceLogSearchFacade> getFacadeImplBeansMap() {
        return serviceLogSearchManagerMap;
    }
}
