package com.you07.util.fastjson;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.IOException;
import java.io.StringWriter;

/**
 * geojson处理工具类
 */
public class GeoJSON {
    public static GeometryJSON gjson = new GeometryJSON(7);
    public static FeatureJSON fjson = new FeatureJSON(gjson);

    static {
        fjson.setEncodeNullValues(false);
    }

    public static Object read(Object input) throws IOException {
        throw new UnsupportedOperationException();
    }

    public static void write(Object obj, Object output) throws IOException {
        if (obj instanceof Geometry) {
            gjson.write((Geometry) obj, output);
        } else if (obj instanceof Feature
                || obj instanceof FeatureCollection
                || obj instanceof CoordinateReferenceSystem) {

            if (obj instanceof SimpleFeature) {
                fjson.writeFeature((SimpleFeature) obj, output);
            } else if (obj instanceof FeatureCollection) {
                fjson.writeFeatureCollection((FeatureCollection) obj, output);
            } else if (obj instanceof CoordinateReferenceSystem) {
                fjson.writeCRS((CoordinateReferenceSystem) obj, output);
            } else {
                throw new IllegalArgumentException(
                        "Unable able to encode object of type " + obj.getClass());
            }
        }
    }

    public static String write(Object object) throws IOException {
        StringWriter stringWriter = new StringWriter();

        GeoJSON.write(object, stringWriter);

        return stringWriter.toString();
    }
}