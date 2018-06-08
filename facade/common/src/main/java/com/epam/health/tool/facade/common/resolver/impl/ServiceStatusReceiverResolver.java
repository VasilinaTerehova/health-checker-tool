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

package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Vasilina_Terehova on 4/25/2018.
 */
@Component
public class ServiceStatusReceiverResolver extends CommonFacadeImplResolver<IServiceStatusReceiver> {
    @Autowired
    private Map<String, IServiceStatusReceiver> applicationFacadeMap;

    @Override
    protected Map<String, IServiceStatusReceiver> getFacadeImplBeansMap() {
        return applicationFacadeMap;
    }
}
