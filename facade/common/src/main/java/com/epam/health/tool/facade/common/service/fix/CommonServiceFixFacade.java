/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

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
