package com.you07.config.datasource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijunhong
 * @since 18/5/27 下午6:27
 */
@Configuration
@AutoConfigureAfter({DataSourceConfiguration.class})
public class MybatisConfiguration extends MybatisAutoConfiguration {


    @Resource(name = "postgresDefaultDataSource")
    private DataSource postgresDefaultDataSource;
    @Resource(name = "postgresGisDataSource")
    private DataSource postgresGisDataSource;
    @Resource(name = "postgresEasDataSource")
    private DataSource postgresEasDataSource;


    public MybatisConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
                                ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {

        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
    }




    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(routingDataSourceProxy());
    }




    @Bean(name = "routingDataSourceProxy")
    public AbstractRoutingDataSource routingDataSourceProxy() {
        DynamicRoutingDataSource proxy = new DynamicRoutingDataSource();
        Map<Object,Object> targetDataResources = new HashMap<>();
        targetDataResources.put(DataBaseContextHolder.DataBaseType.POSTGRESDEFAULT,postgresDefaultDataSource);
        targetDataResources.put(DataBaseContextHolder.DataBaseType.POSTGRESGIS,postgresGisDataSource);
        targetDataResources.put(DataBaseContextHolder.DataBaseType.POSTGRESEAS,postgresEasDataSource);
        //设置默认数据源
        proxy.setDefaultTargetDataSource(postgresDefaultDataSource);
        proxy.setTargetDataSources(targetDataResources);
        proxy.afterPropertiesSet();
        return proxy;
    }

}
