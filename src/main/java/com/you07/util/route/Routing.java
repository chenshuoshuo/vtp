package com.you07.util.route;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gjxchn on 2017/5/16.
 */
public class Routing implements Serializable {
    private String floor;
    private Double distance;
    private Integer sign;
    private Long time;
    private List<Double[]> pointList;
    private String name;

    @Override
    public String toString() {
        return "Routing{" +
                "floor='" + floor + '\'' +
                ", distance=" + distance +
                ", sign=" + sign +
                ", time=" + time +
                ", pointList=" + pointList +
                ", name='" + name + '\'' +
                '}';
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<Double[]> getPointList() {
        return pointList;
    }

    public void setPointList(List<Double[]> pointList) {
        this.pointList = pointList;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
