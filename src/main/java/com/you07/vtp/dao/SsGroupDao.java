package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsGroupDao extends BaseDao<SsGroup> {

    /**
     * 根据条件获取分组列表
     */
    @Select({"<script>",
            "select * from ss_group where 1=1 " +
                    "<if test = ' groupName != null and groupName != \"\"'> and group_name = #{groupName}</if>" +
                    "order by group_id" +
                    "</script>"
    })
    List<SsGroup> queryAll(@Param("groupName")String groupName);


}
