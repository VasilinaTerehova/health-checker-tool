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

package com.epam.health.tool.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ClusterTypeEnum {
    CDH("cdh", "cdh"),
    HDP("hdp", "hdp"),
    MAPR("mapr", "mapr"),
    HDI("hdi", "hdi"),
    EMR("emr", "emr"),
    NONE("none", "none");

    String code;
    String title;

    ClusterTypeEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static ClusterTypeEnum parse(String code) {
        Optional<ClusterTypeEnum> any = Arrays.stream(ClusterTypeEnum.values()).filter(clusterTypeEnum -> clusterTypeEnum.code.equals(code)).findAny();
        return any.orElse(null);
    }
}
