package com.you07.util;

/**
 * @author egan
 * @date 2019/11/27 14:46
 * @desc
 */
public class Coordinate {

    public Double x;

    public Double y;

    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {

    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
