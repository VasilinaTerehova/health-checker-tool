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

package com.epam.facade.model.projection;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public interface NodeSnapshotEntityProjection extends BaseActionResult {
    @Value( "#{target.node}" )
    String getNode();
    @Value( "#{target.fsUsageEntity.used}" )
    int getUsedGb();
    @Value( "#{target.fsUsageEntity.total}" )
    int getTotalGb();
    default HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.FS;
    }
}
