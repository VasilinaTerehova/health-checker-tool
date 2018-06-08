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

package com.epam.health.tool.context;

import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.context.ContextKey;
import com.epam.health.tool.context.holder.DefaultContextHolder;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.health.tool.facade.context.ISingleContextHolder;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//Should use sync methods
@Component("singleton")
public class CommonApplicationContext implements IApplicationContext {
    private Map<ContextKey, ISingleContextHolder> singleContextHolderMap;

    public CommonApplicationContext() {
        this.singleContextHolderMap = new ConcurrentHashMap<>();
    }

    @Override
    public void putHolder( ContextKey holderKey, ISingleContextHolder holder ) {
        singleContextHolderMap.put( holderKey, holder );
    }

    @Override
    public void putHolderIfAbsent(ContextKey holderKey, ISingleContextHolder holder) {
        singleContextHolderMap.putIfAbsent( holderKey, holder );
    }

    @Override
    public ISingleContextHolder removeHolder( ContextKey holderKey ) {
        return singleContextHolderMap.remove( holderKey );
    }

    @Override
    public <T> ISingleContextHolder<T> getHolder( ContextKey holderKey ) {
        return singleContextHolderMap.getOrDefault( holderKey, new DefaultContextHolder());
    }

    public <T> T getFromContext( String clusterName, String minorKey, Class<?> holderClass, T defaultValue ) {
        try {
            return this.<T>getHolder( buildContextKey( clusterName, minorKey, holderClass ) ).orElse( defaultValue );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
            return defaultValue;
        }
    }

    public void addToContext( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value ) {
        try {
            this.putHolder( buildContextKey( clusterName, minorKey, holderClass ), value );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
        }
    }

    @Override
    public void addToContextIfAbsent(String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value) {
        try {
            this.putHolderIfAbsent( buildContextKey( clusterName, minorKey, holderClass ), value );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
        }
    }

    public void removeAllByMinorKey(String minorKey ) {
        List<ContextKey> keys = singleContextHolderMap.entrySet().stream().map(Map.Entry::getKey).filter(key -> isKeysEquals( key.getMinorKey(), minorKey ) )
                .collect(Collectors.toList());

        keys.forEach( singleContextHolderMap::remove );
    }

    private boolean isKeysEquals( String firstKey, String secondKey ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( firstKey, secondKey ) && firstKey.equals( secondKey );
    }

    private ContextKey buildContextKey(String clusterName, String minorKey, Class<?> holderClass ) throws InvalidBuildParamsException {
        return ContextKey.ContextKeyBuilder.get().withMajorKey( clusterName )
                .withMinorKey( minorKey ).withHolderClass( holderClass ).build();
    }
}
