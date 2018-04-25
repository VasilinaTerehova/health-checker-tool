package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Vasilina_Terehova on 4/25/2018.
 */
@Component
public class ServiceStatusReceiverResolver extends CommonFacadeImplResolver<IServiceStatusReceiver> {
    @Autowired
    private Map<String, IServiceStatusReceiver> applicationFacadeMap;

    @Override
    protected Map<String, IServiceStatusReceiver> getFacadeImplBeansMap() {
        return applicationFacadeMap;
    }
}
