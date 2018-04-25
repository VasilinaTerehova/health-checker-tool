package com.epam.health.tool.facade.common.service.status;

import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
public abstract class CommonServiceStatusReceiver implements IServiceStatusReceiver {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;
    @Autowired
    protected SVTransfererManager svTransfererManager;

}
