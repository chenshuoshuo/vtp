package com.you07.map.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.map.dao.*;
import com.you07.map.model.MapRoadAttribute;
import com.you07.map.model.MapRoutePolyline;
import com.you07.map.model.MapRoutePolylineVerticesPgr;
import com.you07.map.model.NavigationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 导航数据
 */
@Service
public class MapRouteDataService {
    //数据
    @Autowired
    MapRoutePolylineDao mapRoutePolylineDao;
    @Autowired
    MapRoutePolylineVerticesPgrDao mapRoutePolylineVerticesPgrDao;

    //道路属性
    @Autowired
    MapRoadAttributeDao mapRoadAttributeDao;
    @Autowired
    MapRoadPavementTypeDao mapRoadPavementTypeDao;
    @Autowired
    MapRoadPlanenessDao mapRoadPlanenessDao;
    @Autowired
    MapRoadTypeDao mapRoadTypeDao;

    /**
     * 获取导航数据
     * @param mapId 地图id
     * @return 导航数据(json)
     */
    @Cacheable(value = "routeCache",key = "#mapId")
    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    public NavigationInfo getRouteData(String mapId){
        NavigationInfo navigationInfo=new NavigationInfo();

        List<MapRoutePolyline> lines=mapRoutePolylineDao.selectPolyLineByMapId(
                Long.parseLong(mapId));
        List<MapRoutePolylineVerticesPgr> points=mapRoutePolylineVerticesPgrDao
                .selectAllByMapId(Long.parseLong(mapId));

        setLineAttribute(lines);

        navigationInfo.setRoutePolyline2ds(lines);
        navigationInfo.setRoutePolyline2dVerticesPgrs(points);

        return navigationInfo;
    }

    /**
     * 设置线属性
     * @param lines
     */
    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    private void setLineAttribute(List<MapRoutePolyline> lines) {
        for(MapRoutePolyline line:lines){
            Long attrId=line.getRoadattrId();

            if (attrId!=null){
                MapRoadAttribute attribute=mapRoadAttributeDao.selectByPrimaryKey(
                        attrId.intValue());

                Integer paveId=attribute.getPaveId();
                if (paveId!=null){
                    attribute.setPavementType(mapRoadPavementTypeDao.selectByPrimaryKey(paveId).getPaveName());
                }
                Integer planId=attribute.getPlanId();
                if (paveId!=null){
                    attribute.setPlan(mapRoadPlanenessDao.selectByPrimaryKey(planId).getPlanName());
                }
                Integer typeId=attribute.getRoadTypeid();
                if (typeId!=null){
                    attribute.setEdgeType(mapRoadTypeDao.selectByPrimaryKey(typeId).getRoadName());
                }

                line.setRoadAttribute(attribute);
            }
        }
    }
}
