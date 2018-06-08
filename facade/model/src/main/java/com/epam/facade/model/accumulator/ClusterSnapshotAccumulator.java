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

package com.epam.facade.model.accumulator;

import java.util.Date;

public class ClusterSnapshotAccumulator {
    private Long id;
    private Date dateOfSnapshot;
    private String token;
    private String clusterName;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateOfSnapshot(Date dateOfSnapshot) {
        if ( dateOfSnapshot!= null ) {
            this.dateOfSnapshot = dateOfSnapshot;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setClusterName(String clusterName) {
        if ( clusterName != null ) {
            this.clusterName = clusterName;
        }
    }

    public Long getId() {
        return id;
    }

    public Date getDateOfSnapshot() {
        return dateOfSnapshot;
    }

    public String getToken() {
        return token;
    }

    public String getClusterName() {
        return clusterName;
    }
}
