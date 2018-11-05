package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.TeacherInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherInfoDao extends BaseDao<TeacherInfo> {

    @Select({
            "select * from eas_teacherinfo"
    })
    @Results({
            @Result(column = "xsbmdm",
                    property = "departmentInfo",
                    one = @One(
                            select = "com.you07.eas.dao.DepartmentInfoDao.selectByPrimaryKey"
                    )
            )
    })
    List<TeacherInfo> listAll();

    @Select({
            "select * from eas_teacherinfo where teachercode = #{teacherCode}"
    })
    @Results({
            @Result(column = "xsbmdm",
                    property = "departmentInfo",
                    one = @One(
                            select = "com.you07.eas.dao.DepartmentInfoDao.selectByPrimaryKey"
                    )
            )
    })
    TeacherInfo loadByTeacherCode(@Param("teacherCode") String teacherCode);

    @Select({
        "<script>",
        "select * from eas_teacherinfo where 1 = 1",
        "<if test = 'xsbmdm != null and xsbmdm != \"\"'>and xsbmdm = #{xsbmdm}</if>",
        "</script>"
    })
    List<TeacherInfo> listQuery(@Param("xsbmdm") String xsbmdm);

    @Select({
            "select * from eas_teacherinfo where teachercode like '%${keyword}%' or name like '%${keyword}%'"
    })
    @Results({
            @Result(column = "xsbmdm",
                    property = "departmentInfo",
                    one = @One(
                            select = "com.you07.eas.dao.DepartmentInfoDao.selectByPrimaryKey"
                    )
            )
    })
    List<TeacherInfo> searchWithCodeName(@Param("keyword") String keyword);
}
