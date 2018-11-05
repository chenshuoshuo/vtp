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

}
