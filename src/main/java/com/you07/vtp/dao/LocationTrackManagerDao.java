package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.LocationTrackManager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationTrackManagerDao extends BaseDao<LocationTrackManager>{

    @Select({
            "<script>",
            "select * from location_trajectory_manager where 1 = 1",
            "<if test = 'userid != null and userid != \"\"'>and userid like \'%${userid}%\'</if>",
            "<if test = 'username != null and username != \"\"'>and username like \'%${username}%\'</if>",
            "order by posttime desc",
            "</script>"
    })
    List<LocationTrackManager> listQuery(@Param("userid") String userid,
                                         @Param("username") String username);

}
