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

import java.util.Arrays;
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
        return groupDao.selectAll();
    }

    /**
     * 添加分组
     */
    public Integer add(SsGroup group){
        return groupDao.insertSelective(group);
    }

    /**
     * 编辑分组
     */
    public Integer update(SsGroup group){
        return groupDao.updateByPrimaryKeySelective(group);
    }

    /**
     * 人员绑定
     */
    public SsGroup blindPerson(Integer groupId,String personIds){
        String[] idArray = personIds.split(",");
        SsGroup group = groupDao.selectByPrimaryKey(groupId);
        if(group != null){
            group.setSpecialPersonIdList(Arrays.asList(idArray));
        }
        groupDao.updateByPrimaryKeySelective(group);
        return groupDao.selectByPrimaryKey(groupId);
    }

    /**
     * 删除分组
     */
    public Integer delete(Integer groupId){
        return groupDao.deleteByPrimaryKey(groupId);
    }

    /**
     * 批量删除分组
     */
    public Integer bulkDelete(String groupIds){
        String[] idArray = groupIds.split(",");
        for (String id : idArray) {
            groupDao.deleteByPrimaryKey(Integer.parseInt(id));
        }
        return idArray.length;
    }

}
