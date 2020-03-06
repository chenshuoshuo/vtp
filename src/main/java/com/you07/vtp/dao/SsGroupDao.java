package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsGroupDao extends BaseDao<SsGroup> {

    /**
     * 根据条件获取分组列表
     */
    @Select({"<script>",
            "select group_id,docking_id,group_name,color,update_time,array_to_string(special_person_id, ',')id_string,order_id,memo,icon,group_en_name from ss_group where 1=1 " +
                    "<if test = ' groupName != null and groupName != \"\"'> and group_name = #{groupName}</if>" +
                    "order by group_id" +
                    "</script>"
    })
    List<SsGroup> queryAll(@Param("groupName")String groupName);

    @Select("select case when max(t.group_id) is null then 1 else (max(t.group_id) + 1) end from ss_group t")
    Integer queryNewColumnId();

    @Insert("INSERT INTO ss_group ( group_id,docking_id,group_name,color,update_time,special_person_id,order_id,memo,icon,group_en_name ) VALUES( #{group.groupId},#{group.dockingId},#{group.groupName}," +
            "#{group.color},#{group.updateTime}," +
            "#{group.specialPersonId},#{group.orderId},#{group.memo},#{group.icon},#{group.groupEnName}) ")
    int add(@Param("group") SsGroup group);


    @Update("UPDATE ss_group set docking_id = #{group.dockingId},group_name = #{group.groupName},color = #{group.color},update_time = #{group.updateTime},icon = #{group.icon}," +
            "group_en_name = #{group.groupEnName},special_person_id = #{group.specialPersonId},order_id = #{group.orderId},memo = #{group.memo} where group_id = #{group.groupId}")
    int update(@Param("group") SsGroup group);

    @Select("select group_id,docking_id,group_name,color,update_time,array_to_string(special_person_id, ',')id_string,order_id,memo,icon,group_en_name from ss_group where group_id =#{groupId}")
    SsGroup get(@Param("groupId")Integer groupId);

    @Delete("delete from ss_group where group_id = #{groupId}")
    int deleteById(@Param("groupId")Integer groupId);

    @Select("select * from ss_group where group_en_name = #{enName}")
    SsGroup exsitWithEnName(@Param("enName") String enName);




}
