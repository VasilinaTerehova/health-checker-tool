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

package com.epam.health.tool.controller.service;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.service.fix.ServiceFixResult;
import com.epam.health.tool.controller.wrapper.StringWrapper;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.service.fix.IServiceFixFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FixServiceController {
    @Autowired
    private IServiceFixFacade serviceFixFacade;

    //Should be post
    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{clusterName}/service/{serviceName}/fix")
    public ResponseEntity<ServiceFixResult> fixYarnService(@PathVariable( "clusterName" ) String clusterName,
                                                           @PathVariable( "serviceName" ) String serviceName,
                                                           @RequestParam( "rootUsername" ) String rootUsername,
                                                           @RequestParam( "rootPassword" ) String rootPassword ) {
        try {
            return ResponseEntity.ok( serviceFixFacade.fixService( clusterName, serviceName, rootUsername, rootPassword ) );
        } catch (InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{clusterName}/service/{serviceName}/manual/fix")
    public ResponseEntity<List<String>> getFixServiceStepList(@PathVariable( "clusterName" ) String clusterName,
                                                              @PathVariable( "serviceName" ) String serviceName ) {
        try {
            return ResponseEntity.ok( serviceFixFacade.getStepsForFix( clusterName, serviceName ) );
        } catch (InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{clusterName}/service/{serviceName}/generate/fix")
    public ResponseEntity<StringWrapper> generateFixServiceScript(@PathVariable( "clusterName" ) String clusterName,
                                                                  @PathVariable( "serviceName" ) String serviceName ) {
        try {
            return ResponseEntity.ok( new StringWrapper( serviceFixFacade.generateBashScript( clusterName, serviceName ) ) );
        } catch (InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }
}
