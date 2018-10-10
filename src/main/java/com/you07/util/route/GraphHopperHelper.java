package com.you07.util.route;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoderFactory;
import com.graphhopper.routing.util.FootFlagEncoder;
import com.graphhopper.storage.*;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.LocationIndexTree;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;
import com.vividsolutions.jts.geom.Coordinate;
import com.you07.map.model.MapRoadAttribute;
import com.you07.map.model.MapRoutePolyline;
import com.you07.map.model.MapRoutePolylineVerticesPgr;
import com.you07.map.model.NavigationInfo;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地图数据类辅助创建器
 * Created by free on 2017/4/24 0024.
 */
public class GraphHopperHelper {
    /**
     * 导航数据路径
     */
    private static final String GRAPH_PATH = "/graph/";

    /**
     * 索引数据路径
     */
    private static final String INDEX_PATH = "/index/";

    /**
     * 边权重定义
     */
    public static EncodingManager em = new EncodingManager(FlagEncoderFactory.CAR + "," + FlagEncoderFactory.FOOT);
    public static CarFlagEncoder carEncoder = (CarFlagEncoder) em.getEncoder(FlagEncoderFactory.CAR);
    public static FootFlagEncoder footEncoder = (FootFlagEncoder) em.getEncoder(FlagEncoderFactory.FOOT);

    private static ReaderWay readerWay;


    /**
     * 地图id<->名字<->楼层的对应表
     */
    private static Map<String, Map<String, String>> mapToNameAndFloor = new ConcurrentHashMap<String, Map<String, String>>();

    /**
     * 创建导航数据文件以及文件夹
     *
     * @param url
     * @param navigationInfo 服务器获取的导航数据
     * @param type           导航类型
     */
    public static GraphHopperStorage createGraph(String url,
                                                 NavigationInfo navigationInfo,
                                                 String mapId, String type) {
        File file = new File(new StringBuilder()
                .append(url).append(GRAPH_PATH).append(mapId)
                .append("/").append(type).toString());

        GraphBuilder gb = new GraphBuilder(em)
                .setLocation(file.getAbsolutePath())
                .set3D(false)
                .setStore(true);

        try {
            if (!file.exists() && !file.isDirectory()) {
                return createGraphFile(navigationInfo, type, gb, mapId, url);
            } else {
                GraphHopperStorage graphHopperStorage = gb.load();

                readNameToFloorData(url, mapId);

                return graphHopperStorage;
            }
        } catch (Exception e) {
            return createGraphFile(navigationInfo, type, gb, mapId, url);
        }
    }

    /**
     * 读取http数据，创建导航文件
     *
     * @param navigationInfo
     * @param type
     * @param gb
     * @return
     */
    private static GraphHopperStorage createGraphFile(NavigationInfo navigationInfo,
                                                      String type, GraphBuilder gb,
                                                      String mapId, String url) {
        if (navigationInfo == null) {
            return null;
        }

        GraphHopperStorage graph = gb.create();

        NodeAccess nodeAccess = graph.getNodeAccess();

        for (MapRoutePolylineVerticesPgr routeNode : navigationInfo.getRoutePolyline2dVerticesPgrs()) {
            Coordinate coordinate = routeNode.getTheGeom().getCoordinate();

            nodeAccess.setNode(routeNode.getId().intValue(), coordinate.y, coordinate.x);
        }

        Map<String, String> nameToFloorMap = new ConcurrentHashMap<String, String>();

        for (MapRoutePolyline routeEdge : navigationInfo.getRoutePolyline2ds()) {
            String name = routeEdge.getName();

            EdgeIteratorState state = graph.edge(routeEdge.getSource().intValue(), routeEdge.getTarget().intValue());
            state.setDistance(routeEdge.getDirection());

            if (StringUtils.isNotBlank(name)) {
                nameToFloorMap.put(name, String.valueOf(routeEdge.getRouteid()));
                state.setName(name);
            } else {
                state.setName(String.valueOf(routeEdge.getRouteid()));
            }
            if (routeEdge.getRoadAttribute() != null) {
                state.setFlags(getFlag(routeEdge.getRoadAttribute(), type));
            }

            PointList pointList = routeEdge.getPointList();
            if (pointList.size() != 0) {
                state.setWayGeometry(pointList);
            }
        }

        mapToNameAndFloor.put(mapId, nameToFloorMap);

        writeNameToFloorData(url, mapId);

        graph.flush();
        return graph;
    }

    /**
     * 根据边的数据，返回边的权重
     *
     * @param roadAttribute 边的数据
     * @param type          导航类型，人走或者车走
     */
    private static long getFlag(MapRoadAttribute roadAttribute, String type) {
        if (readerWay == null) {
            readerWay = new ReaderWay(0);
        }

        synchronized (readerWay) {
            readerWay.setTag("maxspeed", String.valueOf(roadAttribute.getMaxSpeed()));
            readerWay.setTag("highway", roadAttribute.getEdgeType());
            readerWay.setTag("surface", roadAttribute.getPaveId());
            readerWay.setTag("smoothness", roadAttribute.getPlan());
            readerWay.setTag("incline", String.valueOf(roadAttribute.getSlope()));
            readerWay.setTag("width", String.valueOf(roadAttribute.getWidth()));

            if (roadAttribute.getIsSingle()) {
                readerWay.setTag("oneway", "yes");
            }
            if (roadAttribute.getWalkauth() != null) {
                readerWay.setTag("foot", roadAttribute.getWalkauth());
            }
            if (roadAttribute.getCarauth() != null) {
                readerWay.setTag("vehicle", roadAttribute.getCarauth());
            }

            if (roadAttribute.getIsBridge()) {
                readerWay.setTag("bridge", "yes");
            }

            long tag = 0;

            if (type.equals(FlagEncoderFactory.FOOT)) {
                tag = footEncoder.handleWayTags(readerWay, footEncoder.acceptWay(readerWay), 0);
            } else if (type.equals(FlagEncoderFactory.CAR)) {
                tag = carEncoder.handleWayTags(readerWay, carEncoder.acceptWay(readerWay), 0);
            }

            readerWay.clearTags();
            return tag;
        }
    }

    /**
     * 得到地图数据索引
     *
     * @param url
     * @param graph 导航数据
     * @return
     */
    public static LocationIndex createTree(String url, Graph graph, String mapId) {
        String file = new StringBuilder().append(url).append(INDEX_PATH)
                .append(mapId).toString();

        GHDirectory directory = new GHDirectory(file, DAType.RAM_STORE);

        if (directory.isStoring()) {
            directory.create();
        }

        LocationIndexTree tree = new LocationIndexTree(graph.getBaseGraph(), directory);

        if (tree.loadExisting()) {
            return tree;
        } else {
            tree.flush();

            return tree.prepareIndex();
        }
    }

    /**
     * 读取名字和楼层对应的数据到sd卡
     *
     * @param
     */
    private static void readNameToFloorData(String url, String mapId) {
        String path = new StringBuilder()
                .append(url).append("/")
                .append(mapId) + "/mapToFloor.data";

        File file = new File(path);

        BufferedSource bufferedSource = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            bufferedSource = Okio.buffer(Okio.source(file));
            String jsonStr = bufferedSource.readUtf8();

            mapToNameAndFloor = JSON.parseObject(jsonStr, new TypeReference<Map<String, Map<String, String>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSource != null) {
                try {
                    bufferedSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将名字和楼层对应的数据写入sd卡
     */
    private static void writeNameToFloorData(String url, String mapId) {
        String path = url + "/" + mapId + "/mapToFloor.data";

        File file = new File(path);

        BufferedSink bufferedSink = null;
        try {
            if (!file.exists()) {
                File dir = new File(url + "/" + mapId + "/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file.createNewFile();
            }

            bufferedSink = Okio.buffer(Okio.sink(file));
            bufferedSink.writeString(JSON.toJSONString(mapToNameAndFloor), Charset.forName("utf-8"));
            bufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Map<String, String>> getMapToNameAndFloor() {
        return mapToNameAndFloor;
    }
}
