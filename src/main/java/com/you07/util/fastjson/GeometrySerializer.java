package com.you07.util.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.vividsolutions.jts.geom.Geometry;
import com.you07.vividsolutions.jts.io.geojson.GeoJsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 空间对象Fastjson序列化
 */
public class GeometrySerializer implements ObjectSerializer {
    GeoJsonWriter writer;

    public GeometrySerializer() {
        writer = new GeoJsonWriter();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }
        if (object instanceof Geometry) {
            serializer.write(JSON.parse(writer.write((Geometry) object)));
        }
    }
}
