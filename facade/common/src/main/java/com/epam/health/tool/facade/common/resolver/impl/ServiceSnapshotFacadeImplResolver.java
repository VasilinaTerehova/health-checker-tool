package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceSnapshotFacadeImplResolver extends CommonFacadeImplResolver<IServiceSnapshotFacade> {
    @Autowired
    private Map<String, IServiceSnapshotFacade> serviceSnapshotFacadeMap;

    @Override
    protected Map<String, IServiceSnapshotFacade> getFacadeImplBeansMap() {
        return serviceSnapshotFacadeMap;
    }
}
