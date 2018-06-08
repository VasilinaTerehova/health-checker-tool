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

package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.accumulator.results.impl.JobResultImpl;
import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.util.common.CheckingParamsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YarnJobBuilder {
    private String name;
    private boolean success;
    private List<String> errors;

    private YarnJobBuilder() {
        this.errors = new ArrayList<>();
    }

    public static YarnJobBuilder get() {
        return new YarnJobBuilder();
    }

    public YarnJobBuilder withName(String name) {
        this.name = name;

        return this;
    }

    public YarnJobBuilder withSuccess(boolean success) {
        this.success = success;

        return this;
    }

    public YarnJobBuilder withErrors(String... errors) {
        this.errors.addAll(Arrays.asList(errors));

        return this;
    }

    public JobResultProjection build() throws InvalidBuildParamsException {
        assertParams();

        return new JobResultImpl(name, success, errors);
    }

    private void assertParams() throws InvalidBuildParamsException {
        if (CheckingParamsUtil.isParamsNullOrEmpty(name)) {
            throw new InvalidBuildParamsException("Name must be not null or empty!");
        }
    }
}
