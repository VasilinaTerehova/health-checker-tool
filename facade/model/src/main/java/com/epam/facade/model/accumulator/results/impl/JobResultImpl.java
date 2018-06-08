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

package com.epam.facade.model.accumulator.results.impl;

import com.epam.facade.model.projection.JobResultProjection;
import com.epam.util.common.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
public class JobResultImpl implements JobResultProjection {
    private String name;
    private boolean success;
    private List<String> alerts;

    public JobResultImpl() {
        this( StringUtils.EMPTY, false, Collections.emptyList() );
    }

    public JobResultImpl(String name) {
        this( name, false, Collections.emptyList() );
    }

    public JobResultImpl(String name, boolean success, List<String> alerts) {
        this.name = name;
        this.success = success;
        this.alerts = alerts;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public List<String> getAlerts() {
        return alerts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}
