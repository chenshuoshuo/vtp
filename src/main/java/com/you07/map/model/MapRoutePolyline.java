package com.you07.map.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.graphhopper.util.PointList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.you07.util.fastjson.GeometrySerializer;

public class MapRoutePolyline {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.routeid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Long routeid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.road_type
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Integer roadType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.direction
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Double direction;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.geom
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @JSONField(serializeUsing = GeometrySerializer.class)
    private Geometry geom;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.memo
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private String memo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.source
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Long source;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.target
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Long target;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.roadattr_id
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @JSONField(serialize = false)
    private Long roadattrId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.zoneid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private Long zoneid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column map.map_route_polyline.name
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    private String name;

    private MapRoadAttribute roadAttribute;

    public MapRoadAttribute getRoadAttribute() {
        return roadAttribute;
    }

    public void setRoadAttribute(MapRoadAttribute roadAttribute) {
        this.roadAttribute = roadAttribute;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.routeid
     *
     * @return the value of map.map_route_polyline.routeid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Long getRouteid() {
        return routeid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.routeid
     *
     * @param routeid the value for map.map_route_polyline.routeid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setRouteid(Long routeid) {
        this.routeid = routeid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.road_type
     *
     * @return the value of map.map_route_polyline.road_type
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Integer getRoadType() {
        return roadType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.road_type
     *
     * @param roadType the value for map.map_route_polyline.road_type
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setRoadType(Integer roadType) {
        this.roadType = roadType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.direction
     *
     * @return the value of map.map_route_polyline.direction
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Double getDirection() {
        return direction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.direction
     *
     * @param direction the value for map.map_route_polyline.direction
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setDirection(Double direction) {
        this.direction = direction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.geom
     *
     * @return the value of map.map_route_polyline.geom
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Geometry getGeom() {
        return geom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.geom
     *
     * @param geom the value for map.map_route_polyline.geom
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.memo
     *
     * @return the value of map.map_route_polyline.memo
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.memo
     *
     * @param memo the value for map.map_route_polyline.memo
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.source
     *
     * @return the value of map.map_route_polyline.source
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Long getSource() {
        return source;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.source
     *
     * @param source the value for map.map_route_polyline.source
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setSource(Long source) {
        this.source = source;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.target
     *
     * @return the value of map.map_route_polyline.target
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Long getTarget() {
        return target;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.target
     *
     * @param target the value for map.map_route_polyline.target
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setTarget(Long target) {
        this.target = target;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.roadattr_id
     *
     * @return the value of map.map_route_polyline.roadattr_id
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Long getRoadattrId() {
        return roadattrId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.roadattr_id
     *
     * @param roadattrId the value for map.map_route_polyline.roadattr_id
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setRoadattrId(Long roadattrId) {
        this.roadattrId = roadattrId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.zoneid
     *
     * @return the value of map.map_route_polyline.zoneid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public Long getZoneid() {
        return zoneid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.zoneid
     *
     * @param zoneid the value for map.map_route_polyline.zoneid
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setZoneid(Long zoneid) {
        this.zoneid = zoneid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column map.map_route_polyline.name
     *
     * @return the value of map.map_route_polyline.name
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column map.map_route_polyline.name
     *
     * @param name the value for map.map_route_polyline.name
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public PointList getPointList(){
        PointList pointList=new PointList();

        if (geom instanceof LineString){
            LineString lineString=(LineString)geom;

            Coordinate[] coordinates=lineString.getCoordinates();

            for(int i=0;i<coordinates.length;i++){
                Coordinate coordinate=coordinates[i];

                pointList.add(coordinate.y,coordinate.x);
            }
        }else if (geom instanceof MultiLineString){
            MultiLineString multiLineString=(MultiLineString)geom;

            Coordinate[] coordinates=multiLineString.getCoordinates();

            for(int i=0;i<coordinates.length;i++){
                Coordinate coordinate=coordinates[i];

                pointList.add(coordinate.y,coordinate.x);
            }
        }

        return pointList;
    }
}