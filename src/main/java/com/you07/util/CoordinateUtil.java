package com.you07.util;


import com.vividsolutions.jts.geom.Coordinate;

import java.util.LinkedList;
import java.util.List;

/**
 * @author egan
 * @date 2019/9/6 12:44
 * @desc 图形坐标工具
 */
public class CoordinateUtil {

    /**
     * egan
     * 将double,double格式的字符串转换为坐标
     * @date 2019/9/6 12:46
     * @param str
     **/
    public static Coordinate convertStrToOne(String str){
        Coordinate coordinate = new Coordinate();
        coordinate.x = Double.parseDouble(str.split(",")[0]);
        coordinate.y = Double.parseDouble(str.split(",")[1]);
        return coordinate;
    }

    /**
     * egan
     * 将double,double double,double格式的字符串转换为坐标数组
     * @date 2019/9/6 12:46
     * @param str
     **/
    public static List<Coordinate> convertStrToList(String str){
        String []cStr = str.split(" ");
        List<Coordinate> coordinates = new LinkedList<>();
        for (String s : cStr){
            coordinates.add(convertStrToOne(s));
        }
        return coordinates;
    }

    /**
     * egan
     *
     * 射线法判断坐标是否在多边形内
     * @date 2019/9/6 12:51
     * @param point
     * @param polygon
     **/
    public static boolean isInPolygon(Coordinate point, List<Coordinate> polygon) {
        double px = point.x, py = point.y;
        boolean in = false;
        int count = 0;
        //
        for (int i = 0, l = polygon.size(), j = l - 1; i < l; j = i, i++) {
            //取出边界的相邻两个点
            double sx = polygon.get(i).x,
                    sy = polygon.get(i).y,
                    tx = polygon.get(j).x,
                    ty = polygon.get(j).y;
            // 点与多边形顶点重合
            if ((sx == px && sy == py) || (tx == px && ty == py)) {
                return true;
            }
            // 判断线段两端点是否在射线两侧
            //思路:作p点平行于y轴的射线 作s,t的平行线直线  如果射线穿过线段，则py的值在sy和ty之间
            if ((sy < py && ty >= py) || (sy >= py && ty < py)) {
                // 线段上与射线 Y 坐标相同的点的 X 坐标 ,即求射线与线段的交点
                double x = sx + (py - sy) * (tx - sx) / (ty - sy);
                // 点在多边形的边上
                if (x == px) {
                    return true;
                }
                // 射线穿过多边形的边界
                if (x > px) {
                    in = !in;
                    count++;
                }
            }
        }
        // 射线穿过多边形边界的次数为奇数时点在多边形内
        System.out.println(count);
        return in;
    }
}
