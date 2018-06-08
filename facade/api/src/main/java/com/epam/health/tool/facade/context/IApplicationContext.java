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

package com.epam.health.tool.facade.context;

import com.epam.facade.model.context.ContextKey;

public interface IApplicationContext {
    void putHolder( ContextKey holderKey, ISingleContextHolder holder );
    void putHolderIfAbsent( ContextKey holderKey, ISingleContextHolder holder );
    ISingleContextHolder removeHolder( ContextKey holderKey );
    <T> ISingleContextHolder<T> getHolder( ContextKey holderKey );
    //Get value from specific holder if exists or return default
    <T> T getFromContext( String clusterName, String minorKey, Class<?> holderClass, T defaultValue );
    void addToContext( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value );
    void addToContextIfAbsent( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value );
    //Remove all holders by specific minor key
    void removeAllByMinorKey( String minorKey );
}
