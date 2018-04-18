package com.epam.health;

import com.epam.facade.model.ApplicationInfo;
import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.common.service.CommonServiceSnapshotFacadeImpl;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = "com.epam.health")
@EnableJpaRepositories
@EnableScheduling
public class TestHealthCheckerToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestHealthCheckerToolApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/health_tool?useUnicode=true&characterSetResults=utf8&characterEncoding=utf8&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory().getObject().createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.epam.health");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        em.setJpaProperties(jpaProperties);
        return em;
    }

    @Bean
    public IApplicationFacade iApplicationFacade() {
        return new IApplicationFacade() {

            @Override
            public List<ApplicationInfo> getApplicationList(String clusterName) throws InvalidResponseException {
                return null;
            }

            @Override
            public void killApplication(String clusterName, String appId) {

            }
        };
    }

    @Bean
    public IClusterSnapshotFacade iClusterSnapshotFacade(IClusterFacade iClusterFacade) {
        CommonClusterSnapshotFacadeImpl commonClusterSnapshotFacade = new CommonClusterSnapshotFacadeImpl() {

//            @Override
//            public String getActiveResourceManagerAddress(String clusterName) throws CommonUtilException {
//                ClusterEntityProjection cluster = iClusterFacade.getCluster(clusterName);
//                if (cluster.getClusterType().equals(ClusterTypeEnum.CDH)) {
//                    return cluster.getHost().replace("n1", "n3") + ":8088";
//                } else if (cluster.getClusterType().equals(ClusterTypeEnum.HDP)) {
//
//                }
//                throw new RuntimeException("Do not know this type of the cluster");
//            }
//
//            @Override
//            public String getLogDirectory(String clusterName) throws CommonUtilException {
//                ClusterEntityProjection cluster = iClusterFacade.getCluster(clusterName);
//                if (cluster.getClusterType().equals(ClusterTypeEnum.CDH)) {
//                    return "/yarn/nm";
//                } else if (cluster.getClusterType().equals(ClusterTypeEnum.HDP)) {
//
//                }
//                throw new RuntimeException("Do not know this type of the cluster");
//            }
//
//            @Override
//            public String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws CommonUtilException {
//                ClusterEntityProjection cluster = iClusterFacade.getCluster(clusterName);
//                if (propertyName.equals(DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS)) {
//                    return cluster.getHost() + ":50700";
//                }
//                throw new RuntimeException("Do not know this type of the cluster");
//            }
        };
        commonClusterSnapshotFacade.setClusterFacade(iClusterFacade);
        return commonClusterSnapshotFacade;
    }

    @Bean
    public IServiceSnapshotFacade iServiceSnapshotFacade() {
        return new CommonServiceSnapshotFacadeImpl() {


            @Override
            public ClusterServiceShapshotEntity askForCurrentServiceSnapshot(String clusterName, String serviceName) {
                return null;
            }

        };
    }

}
