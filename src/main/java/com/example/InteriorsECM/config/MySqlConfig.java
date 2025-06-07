package com.example.InteriorsECM.config;

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
        basePackages = "com.example.InteriorsECM.repository.mysql",
        entityManagerFactoryRef = "mySqlEntityManagerFactory",
        transactionManagerRef = "mySqlTransactionManager"
)
public class MySqlConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mySqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mySqlEntityManagerFactory(@Qualifier("mySqlDataSource") DataSource mySqlDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mySqlDataSource);
        em.setPackagesToScan("com.example.InteriorsECM.model.mysql");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("mysql");

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(jpaProperties);

        return em;
    }

    @Bean
    public JpaTransactionManager mySqlTransactionManager(@Qualifier("mySqlEntityManagerFactory") LocalContainerEntityManagerFactoryBean mySqlEntityManagerFactory) {
        return new JpaTransactionManager(mySqlEntityManagerFactory.getObject());
    }
}