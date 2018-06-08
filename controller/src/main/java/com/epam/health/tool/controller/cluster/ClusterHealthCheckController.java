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

package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClusterHealthCheckController extends BaseFacadeResolvingController {
    @Autowired
    private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/services")
    public ResponseEntity<List<ServiceStatusHolder>> getRestClusterStatus(@PathVariable("name") String clusterName,
                                                                              @RequestParam(value = "token", defaultValue = "none") String token,
                                                                              @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.OTHER_SERVICES, token, useSave).getServiceStatusList());
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/all")
    public ResponseEntity<HealthCheckResultsAccumulator> getAllClusterStatus(@PathVariable("name") String clusterName,
                                                                             @RequestParam(value = "token", defaultValue = "none") String token,
                                                                             @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.ALL, token, useSave));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/fs")
    public ResponseEntity<List<? extends NodeSnapshotEntityProjection>> getFsClusterStatus(@PathVariable("name") String clusterName,
                                                                                           @RequestParam(value = "token", defaultValue = "none") String token,
                                                                                           @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.FS, token, useSave)
                    .getFsHealthCheckResult().getNodeSnapshotEntityProjections());
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/yarn")
    public ResponseEntity<ServiceStatusHolder> getYarnClusterStatus(@PathVariable("name") String clusterName,
                                                                    @RequestParam(value = "token", defaultValue = "none") String token,
                                                                    @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.YARN_SERVICE, token, useSave).getServiceHealthCheckResult( ServiceTypeEnum.YARN ));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/hdfs/job")
    public ResponseEntity<ServiceStatusHolder> getHdfsClusterStatus(@PathVariable("name") String clusterName,
                                                                    @RequestParam(value = "token", defaultValue = "none") String token,
                                                                    @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.HDFS_SERVICE, token, useSave).getServiceHealthCheckResult( ServiceTypeEnum.HDFS ));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/hdfs/memory")
    public ResponseEntity<HdfsUsageEntityProjection> getHdfsMemoryClusterStatus(@PathVariable("name") String clusterName,
                                                                                @RequestParam(value = "token", defaultValue = "none") String token,
                                                                                @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.HDFS_MEMORY, token, useSave)
                    .getFsHealthCheckResult().getHdfsUsageEntityProjection());
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @GetMapping("/api/cluster/{name}/status/memory")
    public ResponseEntity<MemoryUsageEntityProjection> getMemoryClusterStatus(@PathVariable("name") String clusterName,
                                                                              @RequestParam(value = "token", defaultValue = "none") String token,
                                                                              @RequestParam(value = "useSave", defaultValue = "false") boolean useSave) {
        try {
            return ResponseEntity.ok(askForClusterState(clusterName, HealthCheckActionType.MEMORY, token, useSave)
                    .getFsHealthCheckResult().getMemoryUsageEntityProjection());
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
            throw new RetrievingObjectException(e);
        }
    }

    //Should be changed to /cluster/{name}/status/history
    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @RequestMapping("/getClusterStatusHistory")
    public ResponseEntity<List<ClusterHealthSummary>> getClusterStatusHistory(@RequestParam("clusterName") String clusterName) {
        try {
            return ResponseEntity.ok(resolveClusterSnapshotFacade(clusterName, clusterSnapshotFacadeIFacadeImplResolver)
                    .getClusterSnapshotHistory(clusterName, 30));
        } catch (InvalidResponseException | ImplementationNotResolvedException e) {
            throw new RetrievingObjectException(e);
        }
    }

    private HealthCheckResultsAccumulator askForClusterState(String clusterName, HealthCheckActionType healthCheckAction)
            throws ImplementationNotResolvedException, InvalidResponseException {
        return resolveClusterSnapshotFacade(clusterName, clusterSnapshotFacadeIFacadeImplResolver)
                .getLatestClusterSnapshot(buildAccumulatorToken(clusterName, healthCheckAction));
    }

    private HealthCheckResultsAccumulator askForClusterState(String clusterName, HealthCheckActionType healthCheckAction,
                                                             String token, boolean useSave)
            throws ImplementationNotResolvedException, InvalidResponseException {
        return resolveClusterSnapshotFacade(clusterName, clusterSnapshotFacadeIFacadeImplResolver)
                .getLatestClusterSnapshot(buildAccumulatorToken(clusterName, token, healthCheckAction, useSave));
    }
}
