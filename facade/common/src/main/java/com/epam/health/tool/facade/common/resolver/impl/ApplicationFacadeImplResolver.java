package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApplicationFacadeImplResolver extends CommonFacadeImplResolver<IApplicationFacade> {
    @Autowired
    private Map<String, IApplicationFacade> applicationFacadeMap;

    @Override
    protected Map<String, IApplicationFacade> getFacadeImplBeansMap() {
        return applicationFacadeMap;
    }
}
