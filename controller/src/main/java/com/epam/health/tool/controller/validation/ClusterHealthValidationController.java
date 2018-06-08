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

package com.epam.health.tool.controller.validation;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.recap.IClusterHealthRecapFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterHealthValidationController extends BaseFacadeResolvingController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;
    @Autowired
    private IClusterHealthRecapFacade clusterHealthValidationFacade;

    @GetMapping("/check/cluster/{name}")
    public ResponseEntity<ClusterHealthValidationResult> performClusterHealthValidation(@PathVariable( "name" ) String clusterName) {
        try {
            return ResponseEntity.ok( clusterHealthValidationFacade.validateClusterHealth( resolveClusterSnapshotFacade( clusterName, clusterSnapshotFacadeIFacadeImplResolver )
                    .makeClusterSnapshot( buildAccumulatorToken( clusterName, HealthCheckActionType.ALL ) )));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException( e );
        }
    }
}
