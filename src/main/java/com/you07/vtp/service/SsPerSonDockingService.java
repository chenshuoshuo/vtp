package com.you07.vtp.service;

import com.you07.vtp.dao.LocationHistoryDao;
import com.you07.vtp.dao.SsPersonDockingDao;
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SsPerSonDockingService {
    @Autowired
    private SsPersonDockingDao personDockingDao;

    /**
     * 获取对接配置列表
     */
    public List<SsPersonDocking> queryAll() {
       return personDockingDao.queryAll();
    }

    /**
     * 根据对接ID 获取对接参数列表
     */
    public List<SsPersonDockingParameter> queryAllParameterById(Integer dockingId){
            return personDockingDao.queryAllParameterByDockingId(dockingId);
    }

}
