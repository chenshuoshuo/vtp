package com.you07.config.datasource.annotation;

import com.you07.config.datasource.DataBaseContextHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lijunhong
 * @since 18/6/8 上午10:16
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceConnection {


    DataBaseContextHolder.DataBaseType value() default DataBaseContextHolder.DataBaseType.POSTGRESDEFAULT;
}
