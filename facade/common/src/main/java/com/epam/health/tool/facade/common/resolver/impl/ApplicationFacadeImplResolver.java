package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.application.ApplicationInfo;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.common.resolver.ImplementationNotResolvedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationFacadeImplResolver implements IFacadeImplResolver<IApplicationFacade> {
    @Autowired
    private Map<String, IApplicationFacade> applicationFacadeMap;

    @Override
    public IApplicationFacade resolveFacadeImpl(String clusterType) {
        return applicationFacadeMap.entrySet().stream().filter( entry -> entry.getKey().contains( clusterType ) )
                .map(Map.Entry::getValue ).findFirst().orElse( new IApplicationFacade() {
                    @Override
                    public List<ApplicationInfo> getApplicationList(String clusterName) {
                        throw new ImplementationNotResolvedException();
                    }

                    @Override
                    public void killApplication(String clusterName, String appId) {
                        throw new ImplementationNotResolvedException();
                    }
                });
    }
}
