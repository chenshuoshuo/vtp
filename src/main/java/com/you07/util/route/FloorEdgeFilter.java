package com.you07.util.route;

import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.util.EdgeIteratorState;

import java.util.Map;

/**
 * 根据楼层id过滤边
 * Created by free on 2017/4/26 0026.
 */
public class FloorEdgeFilter implements EdgeFilter {
    private int floor;

    private String mapId;

    private Map<String,String> nameToFloorMap;

    public FloorEdgeFilter(int floor, String mapId) {
        this.floor = floor;
        this.mapId = mapId;

        nameToFloorMap=GraphHopperHelper.getMapToNameAndFloor().get(mapId);
    }

    @Override
    public boolean accept(EdgeIteratorState edgeState) {
        try {
            String floorStr=edgeState.getName().substring(5,6);

            int parseInt= Integer.parseInt(floorStr);

            return parseInt==floor;
        }catch (Exception e){
            String floorStr=nameToFloorMap.get(edgeState.getName()).substring(5,6);

            return Integer.parseInt(floorStr)==floor;
        }
    }
}
