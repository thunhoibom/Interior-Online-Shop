package com.example.InteriorsECM.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.InteriorsECM.repository.timescaledb",
        entityManagerFactoryRef = "timescaleEntityManagerFactory",
        transactionManagerRef = "timescaleTransactionManager"
)
@EntityScan(basePackages = "com.example.InteriorsECM.model.timescaledb")
public class TimescaleDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.timescaledb.datasource")
    public DataSource timescaleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean timescaleEntityManagerFactory(@Qualifier("timescaleDataSource") DataSource timescaleDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(timescaleDataSource);
        em.setPackagesToScan("com.example.InteriorsECM.model.timescaledb");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("timescale");

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        em.setJpaPropertyMap(jpaProperties);

        return em;
    }

    @Bean
    public JpaTransactionManager timescaleTransactionManager(@Qualifier("timescaleEntityManagerFactory") LocalContainerEntityManagerFactoryBean timescaleEntityManagerFactory) {
        return new JpaTransactionManager(timescaleEntityManagerFactory.getObject());
    }
}