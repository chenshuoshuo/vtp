package com.you07.config.datasource;

import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author lijunhong
 * @since 18/5/27 下午5:43
 * DataSource相关配置信息
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Value("${druid.type}")
    private Class<? extends DataSource> dataSourceType;

    @Primary
    @Bean(name = "postgresDefaultDataSource")
    @ConfigurationProperties(prefix = "druid.postgresdefault")
    public DataSource postgresDefaultDataSource() {
        DataSource postgresDefaultDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        System.out.println("============postgresdefault==============="+postgresDefaultDataSource);
        return postgresDefaultDataSource;
    }



    @Bean(name = "postgresGisDataSource")
    @ConfigurationProperties(prefix = "druid.postgresgis")
    public DataSource postgresGisDataSource() {
        DataSource postgresGisDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        System.out.println("=============postgresgis" + postgresGisDataSource);
        return postgresGisDataSource;
    }


    @Bean(name = "postgresEasDataSource")
    @ConfigurationProperties(prefix = "druid.postgres-eas")
    public DataSource postgresEasDataSource() {
        DataSource postgresEasDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        System.out.println("=============postgresEas" + postgresEasDataSource);
        return postgresEasDataSource;
    }


//    @Bean(name = "mysqlSlaveOneDataSource")
//    @ConfigurationProperties(prefix = "druid.mysqlslaveone")
//    public DataSource mysqlSlaveDataSource() {
//        DataSource mysqlSlaveDataSource = DataSourceBuilder.create().type(dataSourceType).build();
//        System.out.println("=============msyqlSlave" + mysqlSlaveDataSource);
//        return mysqlSlaveDataSource;
//    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //这里通过druid-springboot-start进行注册监控数据库
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico, /druid/*");
        System.out.println("=================druid filter register :" + filterRegistrationBean);
        return filterRegistrationBean;
    }

}
