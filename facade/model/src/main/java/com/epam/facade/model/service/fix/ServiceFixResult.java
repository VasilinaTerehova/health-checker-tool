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

package com.epam.facade.model.service.fix;

import com.epam.facade.model.INoArgConsumer;
import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.health.tool.model.ServiceTypeEnum;

import java.util.Objects;

public class ServiceFixResult {
    private ServiceTypeEnum serviceName;
    private boolean fixed;
    private String alert;

    private ServiceFixResult() {}

    public ServiceTypeEnum getServiceName() {
        return serviceName;
    }

    public boolean isFixed() {
        return fixed;
    }

    public String getAlert() {
        return alert;
    }

    public static class ServiceFixResultBuilder {
        private ServiceFixResult serviceFixResult;

        private ServiceFixResultBuilder() {
            this.serviceFixResult = new ServiceFixResult();
        }

        public static ServiceFixResultBuilder get() {
            return new ServiceFixResultBuilder();
        }

        public ServiceFixResultBuilder withServiceName( String serviceName ) {
            return setIfNotNullAndReturnBuilder( serviceName, () -> this.withServiceName( ServiceTypeEnum.getTypeByName( serviceName ) ) );
        }

        public ServiceFixResultBuilder withServiceName( ServiceTypeEnum serviceName ) {
            return setIfNotNullAndReturnBuilder( serviceName, () -> this.serviceFixResult.serviceName = serviceName );
        }

        public ServiceFixResultBuilder withFixed( boolean fixed ) {
            return setIfNotNullAndReturnBuilder( fixed, () -> this.serviceFixResult.fixed = fixed );
        }

        public ServiceFixResultBuilder withAlert( String alert ) {
            return setIfNotNullAndReturnBuilder( alert, () -> this.serviceFixResult.alert = alert );
        }

        public ServiceFixResult build() throws InvalidBuildParamsException {
            assertParams();

            return this.serviceFixResult;
        }

        private ServiceFixResultBuilder setIfNotNullAndReturnBuilder( Object param, INoArgConsumer noArgConsumer ) {
            if ( Objects.nonNull( param ) ) {
                noArgConsumer.execute();
            }

            return this;
        }

        private void assertParams() throws InvalidBuildParamsException {
            if ( Objects.isNull( this.serviceFixResult.serviceName ) ) {
                throw new InvalidBuildParamsException( "Service name must be not null!" );
            }
        }
    }
}
