package com.you07.vtp.vo;

/**
 * @author egan
 * @date 2019/9/12 14:44
 * @desc
 */
public class CoordinateVO {

    private Double lat;
    private Double lon;

    public CoordinateVO(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public CoordinateVO(double[] latlon) {
        lat = latlon[0];
        lat = latlon[1];
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double[] toArray(){
        return new Double[]{lat, lon};
    }
}
