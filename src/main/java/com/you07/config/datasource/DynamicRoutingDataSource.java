package com.you07.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author lijunhong
 * @since 18/5/28 上午1:16
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
//        System.out.println("此时获取到的数据源为："+DataBaseContextHolder.getDatabaseType());
        return DataBaseContextHolder.getDatabaseType();
    }
}
