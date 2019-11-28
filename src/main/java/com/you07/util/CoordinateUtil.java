package com.you07.util;



import java.awt.geom.Point2D;
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
    public static Point2D.Double convertStrToOne(String str){
        Point2D.Double coordinate = new Point2D.Double(
                Double.parseDouble(str.split(",")[0]),
                Double.parseDouble(str.split(",")[1]));
        return coordinate;
    }

    /**
     * egan
     * 将double,double double,double格式的字符串转换为坐标数组
     * @date 2019/9/6 12:46
     * @param str
     **/
    public static List<Point2D.Double> convertStrToList(String str){
        String []cStr = str.split(" ");
        List<Point2D.Double> coordinates = new LinkedList<>();
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
    public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath peneralPath = new java.awt.geom.GeneralPath();

        Point2D.Double first = polygon.get(0);
        // 通过移动到指定坐标（以双精度指定），将一个点添加到路径中
        peneralPath.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            // 通过绘制一条从当前坐标到新指定坐标（以双精度指定）的直线，将一个点添加到路径中。
            peneralPath.lineTo(d.x, d.y);
        }
        // 将几何多边形封闭
        peneralPath.lineTo(first.x, first.y);
        peneralPath.closePath();
        // 测试指定的 Point2D 是否在 Shape 的边界内。
        return peneralPath.contains(point);
    }
}
