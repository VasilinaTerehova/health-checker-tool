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

package com.epam.health.tool.facade.common.application;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.application.IApplicationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;


/**
 * Created by Aliaksandr_Zhuk on 5/14/2018.
 */
public abstract class CommonApplicationFacade implements IApplicationFacade {

  @Autowired
  private Provider<ApplicationDestroyer> idProvider;

  @Override
  public List<Boolean> killSelectedApplications( String clusterName, String appIds ) throws InvalidResponseException {

    List<Boolean> appKillResult = new ArrayList<>();
    List<String> appIdList = splitAppIds( appIds );

    ForkJoinPool pool = initiateExecutorPool( appIdList.size() );

    List<Callable<Boolean>> callables = fillOutAppKillerPerformerList( appIdList, clusterName );

    pool.invokeAll( callables ).stream().map( future -> {
      boolean result = false;
      try {
        result = future.get();
      } catch ( Exception ex ) {
        throw new InvalidResponseException( "application can`t be killed " + ex );
      } finally {
        return result;
      }
    } ).forEach( appKillResult::add );

    return appKillResult;
  }

  private ForkJoinPool initiateExecutorPool( int size ) {
    return new ForkJoinPool( size );
  }

  private List<Callable<Boolean>> fillOutAppKillerPerformerList( List<String> appIdList,
                                                                 String clusterName ) {
    List<Callable<Boolean>> callables = new ArrayList<>( appIdList.size() );

    for ( int i = 0; i < appIdList.size(); i++ ) {
      ApplicationDestroyer applicatioId = idProvider.get();
      applicatioId.setAppId( appIdList.get( i ) );
      applicatioId.setClusterName( clusterName );
      callables.add( applicatioId );
    }
    return callables;
  }

  private List<String> splitAppIds( String ids ) {
    return Arrays.asList( ids.split( "," ) );
  }
}
