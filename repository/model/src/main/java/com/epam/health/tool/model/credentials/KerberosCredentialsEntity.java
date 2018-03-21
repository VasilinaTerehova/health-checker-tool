package com.epam.health.tool.model.credentials;

import com.epam.health.tool.common.AbstractManagedEntity;
import com.epam.health.tool.model.ClusterEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Entity
@Table(name = KerberosCredentialsEntity.TABLE_NAME)
public class KerberosCredentialsEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "kerberos_credentials";
    public static final String COLUMN_USERNAME = "username_";
    public static final String COLUMN_PASSWORD = "password_";
    public static final String COLUMN_KRB5_FILE_PATH = "krb5_file_path_";
    public static final String COLUMN_FK_CLUSTER = ClusterEntity.TABLE_NAME;

    @Column(name = COLUMN_USERNAME)
    String username;

    @Column(name = COLUMN_PASSWORD)
    String password;

    @Column(name = COLUMN_KRB5_FILE_PATH)
    String krb5FilePath;

    @NotNull
    @OneToOne(mappedBy="kerberos")
    private ClusterEntity cluster;

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

    public String getKrb5FilePath() {
        return krb5FilePath;
    }

    public void setKrb5FilePath(String krb5FilePath) {
        this.krb5FilePath = krb5FilePath;
    }

    public ClusterEntity getCluster() {
        return cluster;
    }

    public void setCluster(ClusterEntity cluster) {
        this.cluster = cluster;
    }
}
