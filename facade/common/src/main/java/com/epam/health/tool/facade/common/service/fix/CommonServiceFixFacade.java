package com.epam.health.tool.facade.common.service.fix;

import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.service.fix.ServiceFixResult;
import com.epam.health.tool.facade.common.service.fix.action.BashScriptCreator;
import com.epam.health.tool.facade.service.fix.action.IServiceFixAction;
import com.epam.health.tool.facade.service.fix.IServiceFixFacade;
import com.epam.health.tool.facade.service.fix.action.ServiceFixAction;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonServiceFixFacade implements IServiceFixFacade {
    @Autowired
    private List<IServiceFixAction> serviceFixActions;

    @Override
    public ServiceFixResult fixService( String clusterName, String serviceName, String rootUsername, String rootPassword ) throws InvalidResponseException {
        return getFixAction( serviceName ).performFix( clusterName, rootUsername, rootPassword );
    }

    @Override
    public List<String> getStepsForFix(String clusterName, String serviceName) throws InvalidResponseException {
        return getFixAction( serviceName ).getStepsForFix( clusterName );
    }

    @Override
    public String generateBashScript(String clusterName, String serviceName) throws InvalidResponseException {
        return generateAndSaveScript( clusterName, serviceName, getFixAction( serviceName ).getFixCommand( clusterName ) );
    }

    private String generateAndSaveScript( String clusterName, String serviceName, String command ) {
        return BashScriptCreator.get( command ).withCluster( clusterName ).withService( serviceName ).generateAndSaveScript();
    }

    private IServiceFixAction getFixAction(String serviceName ) throws InvalidResponseException {
        try {
            return serviceFixActions.stream().filter( serviceFixAction -> isAvailableForService( serviceFixAction, serviceName ) )
                    .findFirst().orElseThrow( () -> new ImplementationNotResolvedException( "Can't find implementation for fix service for service - " + serviceName ) );
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException( e );
        }
    }

    private boolean isAvailableForService(IServiceFixAction serviceFixAction, String serviceName ) {
        return serviceFixAction.getClass().isAnnotationPresent(ServiceFixAction.class)
                && serviceFixAction.getClass().getAnnotation( ServiceFixAction.class ).value().equals(ServiceTypeEnum.getTypeByName( serviceName ));
    }
}
