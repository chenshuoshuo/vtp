package com.you07.common;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

public interface BaseDao<T> extends BaseMapper<T>{

    @InsertProvider(type = BaseInsertProvider.class, method = "dynamicSQL")
    @Options(useGeneratedKeys = true)
    @Override
    int insertSelective(T var1);

}
