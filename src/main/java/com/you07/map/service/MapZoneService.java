package com.you07.map.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.map.dao.MapZoneDao;
import com.you07.map.model.MapZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapZoneService {
    @Autowired
    private MapZoneDao mapZoneDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    public List<MapZone> query2dMap(){
        return mapZoneDao.queryWithMapType("2d");
    }

    /**
     * @Author wells
     * @Description //TODO 根据地图zoneid获取地图信息
     * @Date 16:59 2019/6/28
     * @Param [zoneid]
     * @return java.util.List<com.you07.map.model.MapZone>
     **/
    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    public MapZone query2ZoneId(String zoneid){
        return mapZoneDao.queryWithId(zoneid);
    }


}
