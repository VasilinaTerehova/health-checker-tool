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

package com.epam.health.tool.quartz;

import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.dao.cluster.ClusterServiceSnapshotDao;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.util.DateUtil;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Vasilina_Terehova on 3/22/2018.
 */
@Component
public class ClusterHealthCheckJob {

  private static final Logger log = LoggerFactory.getLogger( ClusterHealthCheckJob.class );

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "HH:mm:ss" );

  @Autowired
  ClusterServiceSnapshotDao clusterServiceSnapshotDao;

  @Autowired
  private IFacadeImplResolver<IClusterSnapshotFacade> clusterSnapshotFacadeIFacadeImplResolver;

    @Scheduled( fixedDelay = 60 * 60 * 1000 )
    public void checkClustersHealth() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        Date hourAgo = DateUtil.dateHourAgo();

    List<ClusterEntity> clusterEntities = clusterServiceSnapshotDao.findClustersForSnapshot( hourAgo );
    long start = System.currentTimeMillis();
    int size = clusterEntities.size();
    if (size > 0) {
      ForkJoinPool forkJoinPool = new ForkJoinPool(size);
      forkJoinPool.submit(() -> clusterEntities.stream().parallel().forEach(clusterEntity -> {

        try {
          clusterSnapshotFacadeIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name())
                  .makeClusterSnapshot(
                          ClusterAccumulatorToken.buildScheduleAllCheck(clusterEntity.getClusterName()));
        } catch (ImplementationNotResolvedException | InvalidResponseException e) {
          log.error("Can't find facade implementation for this vendor ", e);
        }

      })).join();
    } else {
      log.info("no clusters will be checked now");
    }
    long end = System.currentTimeMillis();
    long total = ( end - start );
    long minutes = total / 60 / 1000;
    long seconds = total / 1000 - minutes * 60;
    log.info( "spent on full check: " + total + " minutes: " + minutes + " seconds: " + seconds );
  }

}
