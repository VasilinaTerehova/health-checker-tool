package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApplicationFacadeImplResolver implements IFacadeImplResolver<IApplicationFacade> {
    @Autowired
    private Map<String, IApplicationFacade> applicationFacadeMap;

    @Override
    public IApplicationFacade resolveFacadeImpl(String clusterType) throws ImplementationNotResolvedException {
        return applicationFacadeMap.entrySet().stream().filter( entry -> entry.getKey().contains( clusterType ) )
                .map(Map.Entry::getValue ).findFirst()
                .orElseThrow(() -> new ImplementationNotResolvedException( "Can't find implementation for + " + clusterType ));
    }
}
