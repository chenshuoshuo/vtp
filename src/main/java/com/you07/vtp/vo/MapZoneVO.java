package com.you07.vtp.vo;

/**
 * @author egan
 * @date 2019/9/5 16:11
 * @desc
 */
public class MapZoneVO {

    private String id;

    private String name;

    private PolygonBBox polygonBBox;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolygonBBox getPolygonBBox() {
        return polygonBBox;
    }

    public void setPolygonBBox(PolygonBBox polygonBBox) {
        this.polygonBBox = polygonBBox;
    }
}
