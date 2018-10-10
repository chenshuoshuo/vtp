package com.you07.map.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.map.dao.MapRoutePolylineDao;
import com.you07.map.model.MapRoutePolyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapRoutePolylineService {
    @Autowired
    private MapRoutePolylineDao mapRoutePolylineDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    public List<MapRoutePolyline> selectByCampusId(Integer campusId){
        return mapRoutePolylineDao.selectByCampusId(campusId);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESGIS)
    public List<MapRoutePolyline> selectAll(){
        return mapRoutePolylineDao.queryAll();
    }
}
