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

package com.epam.facade.model;

import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

//Will be implemented
public abstract class BaseObjectBuilder<T> {
    protected T object;

    public T build() throws InvalidBuildParamsException {
        this.assertParamsSetup();

        return this.object;
    }

    protected abstract void assertParamsSetup() throws InvalidBuildParamsException;

    protected BaseObjectBuilder<T> addStringParam( String param, Consumer<String> addParamAction ) {
        addStringParamWithCheck( param, addParamAction );

        return this;
    }

    private void addStringParamWithCheck( String param, Consumer<String> addParamAction ) {
        if ( CheckingParamsUtil.isParamsNotNullOrEmpty( param ) ) {
            addParamAction.accept( param );
        }
    }
}
