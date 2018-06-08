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

package com.epam.health.tool.model.credentials;

import com.epam.health.tool.common.AbstractManagedEntity;
import com.epam.health.tool.model.ClusterEntity;

import javax.persistence.*;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Entity
@Table(name = SshCredentialsEntity.TABLE_NAME)
public class SshCredentialsEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "ssh_credentials";
    public static final String COLUMN_USERNAME = "username_";
    public static final String COLUMN_PASSWORD = "password_";
    public static final String COLUMN_PEM_FILE_PATH = "pem_file_path_";
    public static final String COLUMN_FK_CLUSTER = ClusterEntity.TABLE_NAME;

    @Column(name = COLUMN_USERNAME)
    String username;

    @Column(name = COLUMN_PASSWORD)
    String password;

    @Column(name = COLUMN_PEM_FILE_PATH)
    private String pemFilePath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPemFilePath() {
        return pemFilePath;
    }

    public void setPemFilePath(String pemFilePath) {
        this.pemFilePath = pemFilePath;
    }
}
