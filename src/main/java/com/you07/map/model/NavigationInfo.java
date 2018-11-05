package com.you07.map.model;

import java.io.Serializable;
import java.util.List;

/**
 * 导航信息
 * Created by free on 2017/4/25 0025.
 */
public class NavigationInfo implements Serializable {
    /**
     * 边与边之间的交点集合数据
     */
    private List<MapRoutePolylineVerticesPgr> routePolyline2dVerticesPgrs;
    /**
     * 边的集合数据
     */
    private List<MapRoutePolyline> routePolyline2ds;

    public List<MapRoutePolylineVerticesPgr> getRoutePolyline2dVerticesPgrs() {
        return routePolyline2dVerticesPgrs;
    }

    public void setRoutePolyline2dVerticesPgrs(List<MapRoutePolylineVerticesPgr> routePolyline2dVerticesPgrs) {
        this.routePolyline2dVerticesPgrs = routePolyline2dVerticesPgrs;
    }

    public List<MapRoutePolyline> getRoutePolyline2ds() {
        return routePolyline2ds;
    }

    public void setRoutePolyline2ds(List<MapRoutePolyline> routePolyline2ds) {
        this.routePolyline2ds = routePolyline2ds;
    }
}
