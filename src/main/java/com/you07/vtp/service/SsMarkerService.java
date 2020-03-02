package com.you07.vtp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.you07.vtp.dao.SsGroupDao;
import com.you07.vtp.dao.SsMakerDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SsMarkerService {
    @Autowired
    private SsMakerDao makerDao;

    /**
     * 分页获取大楼标注信息
     */
    public PageInfo<SsMarker> queryAll(Integer campusCode,String markerName, Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<SsMarker> list = makerDao.queryAll(campusCode,markerName);
        return new PageInfo<SsMarker>(list);
    }

    /**
     * 获取大楼标注列表
     */
    public List<SsMarker> listQuery(Integer campusCode) {
        return makerDao.listQuery(campusCode);
    }

    /**
     * 添加标注
     */
    public Integer add(SsMarker marker){
        return makerDao.insertSelective(marker);
    }

    /**
     * 编辑分组
     */
    public Integer update(SsMarker marker){
        return makerDao.updateByPrimaryKeySelective(marker);
    }


    /**
     * 删除分组
     */
    public Integer delete(Integer markerId){
        return makerDao.deleteByPrimaryKey(markerId);
    }

    /**
     * 批量删除分组
     */
    public Integer bulkDelete(String markerIds){
        String[] idArray = markerIds.split(",");
        for (String id : idArray) {
            makerDao.deleteByPrimaryKey(Integer.parseInt(id));
        }
        return idArray.length;
    }

}
