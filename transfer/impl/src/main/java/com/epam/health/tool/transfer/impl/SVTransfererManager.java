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

package com.epam.health.tool.transfer.impl;

import com.epam.health.tool.transfer.SVTransferer;
import com.epam.health.tool.transfer.Tuple2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
@Component
public class SVTransfererManager {

    ConcurrentHashMap<Tuple2<Class, Class>, SVTransferer> transferersMap = new ConcurrentHashMap<>();

    public void addTransferer(Class fromClass, Class toClass, SVTransferer transferer) {
        transferersMap.put(new Tuple2<>(fromClass, toClass), transferer);
    }

    public <F, T> SVTransferer<F, T> getTransferer(Class fromClass, Class toClass) {
        Tuple2<Class, Class> classTuple = new Tuple2<>(fromClass, toClass);
        SVTransferer svTransferer = transferersMap.get(classTuple);
        if (svTransferer == null) {
            svTransferer = new SVMapperBuilderImpl<F, T>().build();
            addTransferer(fromClass, toClass, svTransferer);
        }
        return svTransferer;
    }
}
