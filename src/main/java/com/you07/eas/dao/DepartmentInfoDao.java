package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.DepartmentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentInfoDao extends BaseDao<DepartmentInfo> {

    @Select({
            "select * from eas_zzjg_xsbm where xsbmdm = #{deptCode} limit 1"
    })
    DepartmentInfo loadByDeptCode(@Param("deptCode") String deptCode);

    @Select({
            "<script>",
            "select * from eas_zzjg_xsbm where 1 = 1",
            "<if test = 'bmlbdm != null and bmlbdm != \"\"'>and bmlbdm = #{bmlbdm}</if>",
            "order by xsbmdm",
            "</script>"
    })
    List<DepartmentInfo> listQuery(@Param("bmlbdm") String bmlbdm);
}
