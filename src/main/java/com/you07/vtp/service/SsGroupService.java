package com.you07.vtp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.you07.vtp.dao.SsGroupDao;
import com.you07.vtp.dao.SsPersonDockingDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SsGroupService {
    @Autowired
    private SsGroupDao groupDao;

    /**
     * 分页获取分组信息
     */
    public PageInfo<SsGroup> queryAll(String groupName,Integer page,Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<SsGroup> list = groupDao.queryAll(groupName);
        return new PageInfo<SsGroup>(list);
    }

    /**
     * 获取分组列表
     */
    public List<SsGroup> listQuery() {
        return groupDao.queryAll(null);
    }

    /**
     * 添加分组
     */
    public int add(SsGroup group){
        SsGroup exsitWithEnName = groupDao.exsitWithEnName(group.getGroupEnName());
        if(exsitWithEnName == null){
            if(group.getGroupId() == null || group.getGroupId().toString().equals("")){
                group.setGroupId(groupDao.queryNewColumnId());
            }
            return groupDao.add(group);
        }else {
            return -1;
        }

    }

    /**
     * 编辑分组
     */
    public int update(SsGroup group){
        SsGroup exsitWithEnName = groupDao.exsitWithEnName(group.getGroupEnName());
        if(exsitWithEnName != null && !exsitWithEnName.getGroupId().toString().equals(group.getGroupId().toString())){
            return -1;
        }else {
            if(group.getIdString() != null){
                group.setSpecialPersonId(group.getIdString().split(","));
            }
            return groupDao.update(group);
        }
    }

    /**
     * 根据主键获取
     */
    public SsGroup get(Integer groupId){
        return groupDao.get(groupId);
    }

    /**
     * 人员绑定
     */
    public SsGroup blindPerson(Integer groupId,String personIds){
        String[] idArray = personIds.split(",");
        SsGroup group = groupDao.get(groupId);
        if(group != null){
            group.setSpecialPersonId(idArray);
        }
        groupDao.update(group);
        return groupDao.get(groupId);
    }

    /**
     * 删除分组
     */
    public Integer delete(Integer groupId){
        return groupDao.deleteById(groupId);
    }

    /**
     * 批量删除分组
     */
    public Integer bulkDelete(String groupIds){
        String[] idArray = groupIds.split(",");
        for (String id : idArray) {
            groupDao.deleteById(Integer.parseInt(id));
        }
        return idArray.length;
    }

}
