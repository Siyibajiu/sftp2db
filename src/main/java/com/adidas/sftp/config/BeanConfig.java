package com.adidas.sftp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jeasy.batch.core.job.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

@Component
public class BeanConfig {
    private static DbConfig dbConfig;

    @Autowired
    public void setConfig(DbConfig dbConfig) {
        BeanConfig.dbConfig = dbConfig;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        Properties properties  = new Properties();
        properties.setProperty("serverName",dbConfig.getIp());
        properties.setProperty("portNumber","5432");
        properties.setProperty("user",dbConfig.getUsername());
        properties.setProperty("password",dbConfig.getPassword());
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(3);
        properties.setProperty("sslmode","disable");
        properties.setProperty("databaseName",dbConfig.getDbname());
        properties.setProperty("preparedStatementCacheQueries","0");
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setDataSourceProperties(properties);
        config.setConnectionTestQuery("SELECT 1");
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
    @Bean(destroyMethod = "shutdown")
    public JobExecutor jobExecutor(){
        JobExecutor jobExecutor = new JobExecutor();
        return jobExecutor;
    }
}
