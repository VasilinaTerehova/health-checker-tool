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

package com.epam.health.tool.controller.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckActionImplResolver;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsServiceHealthCheck;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController extends BaseFacadeResolvingController {
  @Autowired
  private IFacadeImplResolver<IApplicationFacade> applicationFacadeIFacadeImplResolver;

  @Autowired
  private HealthCheckActionImplResolver healthCheckActionImplResolver;

  @Autowired
  CommonHdfsServiceHealthCheck commonHdfsServiceHealthCheck;

  @CrossOrigin( origins = { "http://localhost:4200", "*" } )
  @GetMapping( "/api/cluster/{name}/applications" )
  public List<ApplicationInfo> getYarnAppList( @PathVariable( "name" ) String clusterName ) {
    try {
      return resolveClusterSnapshotFacade( clusterName, applicationFacadeIFacadeImplResolver )
        .getApplicationList( clusterName );
    } catch ( ImplementationNotResolvedException | InvalidResponseException ex ) {
      throw new RetrievingObjectException( ex );
    }
  }

  @CrossOrigin( origins = { "http://localhost:4200", "*" } )
  @GetMapping( "/api/cluster/{clusterName}/applications/kill" )
  public List<Boolean> killApplications( @PathVariable( "clusterName" ) String clusterName,
                                         @RequestParam( "id" ) String appIds ) throws InvalidResponseException {
    try {
      return resolveClusterSnapshotFacade( clusterName, applicationFacadeIFacadeImplResolver )
        .killSelectedApplications( clusterName, appIds );
    } catch ( ImplementationNotResolvedException | InvalidResponseException ex ) {
      throw new RetrievingObjectException( ex );
    }
  }
}
