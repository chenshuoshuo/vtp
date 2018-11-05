package com.you07.util.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 将字符串转换为json array
 */
public class String2JSONSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object==null){
            serializer.writeNull();
            return;
        }
        if (object instanceof String) {
            String filterStr = (String) object;
            if (filterStr.isEmpty()){
                serializer.writeNull();
                return;
            }
            serializer.write(JSON.parse(filterStr));
        }
    }
}
