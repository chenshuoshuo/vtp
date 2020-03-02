package com.you07.vtp.dao;

import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsPersonDockingDao {

    /**
     * 查询对接配置列表
     */
    @Select({
            "select * from ss_person_docking order by docking_id"
    })
    List<SsPersonDocking> queryAll();

    /**
     * 根据ID查询对接配置参数
     **/
    @Select({"select * from ss_person_docking_parameter where docking_id = #{dockingId}"})
    List<SsPersonDockingParameter> queryAllParameterByDockingId(@Param("dockingId") Integer dockingId);

}
