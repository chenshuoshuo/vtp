package com.you07.config.datasource.intercepter;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author lijunhong
 * @since 18/6/8 上午10:18
 */
@Aspect
@Component
public class DataSourceIntercepter implements Ordered {


    @Around("@annotation(dataSourceConnection)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, DataSourceConnection dataSourceConnection) throws Throwable {
        try {

            if(dataSourceConnection.value() != null) {
                DataBaseContextHolder.setDatabaseType(dataSourceConnection.value());
            }
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DataBaseContextHolder.clearDataBaseType();
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
