package com.you07.util.datatype;

        import com.vividsolutions.jts.geom.*;
        import com.vividsolutions.jts.io.ParseException;
        import com.vividsolutions.jts.io.WKBReader;
        import org.apache.ibatis.type.JdbcType;
        import org.apache.ibatis.type.MappedTypes;
        import org.apache.ibatis.type.TypeHandler;
        import org.geotools.data.postgis.WKBAttributeIO;

        import java.io.IOException;
        import java.sql.CallableStatement;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;

@MappedTypes({Geometry.class,Point.class, LineString.class, Polygon.class, MultiPoint.class, MultiLineString.class, MultiPolygon.class,
        Envelope.class, GeometryCollection.class})
public class PostGISHandler implements TypeHandler<Geometry> {
    private static WKBReader wKBReader = new WKBReader();
    private static WKBAttributeIO wKBAttributeIO = new WKBAttributeIO();

    @Override
    public void setParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            wKBAttributeIO.write(ps, i, parameter);
        } catch (IOException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Geometry getResult(ResultSet rs, String columnName) throws SQLException {
        Geometry resultGeometry = null;
        try {
            if(rs.getObject(columnName) != null){
                resultGeometry = wKBReader.read(WKBReader.hexToBytes(rs.getObject(columnName).toString()));
            }
        } catch (ParseException e) {
            throw new SQLException(e.getMessage());
        }
        return resultGeometry;
    }

    @Override
    public Geometry getResult(ResultSet rs, int columnIndex) throws SQLException {
        Geometry resultGeometry = null;
        try {
            resultGeometry = wKBReader.read(WKBReader.hexToBytes(rs.getObject(columnIndex).toString()));
        } catch (ParseException e) {
            throw new SQLException(e.getMessage());
        }
        return resultGeometry;
    }

    @Override
    public Geometry getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Geometry resultGeometry = null;
        try {
            resultGeometry = wKBReader.read(WKBReader.hexToBytes(cs.getObject(columnIndex).toString()));
        } catch (ParseException e) {
            throw new SQLException(e.getMessage());
        }
        return resultGeometry;
    }
}
