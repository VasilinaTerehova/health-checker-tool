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

import com.epam.health.tool.transfer.MappingAcceptor;
import com.epam.health.tool.transfer.SVMapperBuilder;
import com.epam.health.tool.transfer.SVTransferer;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
public class SVMapperBuilderImpl<F, T> implements SVMapperBuilder<F, T> {
    ModelMapperTransfererImpl<F, T> ftModelMapperTransferer;

    public SVMapperBuilderImpl() {
        ftModelMapperTransferer = new ModelMapperTransfererImpl<>();
    }

    @Override
    public void addMappingRule(MappingAcceptor<F, T> mappingAcceptor) {
        ftModelMapperTransferer.addMappingRule(mappingAcceptor);
    }

    @Override
    public SVTransferer<F, T> build() {
        return ftModelMapperTransferer;
    }
}
