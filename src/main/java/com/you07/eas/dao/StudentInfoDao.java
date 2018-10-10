package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.StudentInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInfoDao extends BaseDao<StudentInfo> {

    @Select({
            "select * from eas_studentinfo"
    })
    @Results({
            @Result(column = "classcode",
                    property = "classInfo",
                    one = @One(
                            select = "com.you07.eas.dao.ClassInfoDao.selectByPrimaryKey"
                    )
            )
    })
    List<StudentInfo> listAll();

    @Select({
            "select * from eas_studentinfo where studentno = #{studentNo}"
    })
    @Results({
            @Result(column = "classcode",
                    property = "classInfo",
                    one = @One(
                            select = "com.you07.eas.dao.ClassInfoDao.selectByPrimaryKey"
                    )
            )
    })
    StudentInfo loadByStudentNo(@Param("studentNo") String studentNo);

    @Select({
            "select * from eas_studentinfo where classcode in (${classCodes})"
    })
    List<StudentInfo> loadWithClassCodes(@Param("classCodes") String classCodes);

    @Select({
            "select * from eas_studentinfo where classcode = #{classCodes} order by studentno"
    })
    List<StudentInfo> queryWithClassCode(@Param("classCode") String classCode);

    @Select({
            "select * from eas_studentinfo where studentno like '%${keyword}%' or name like '%${keyword}%'"
    })
    @Results({
            @Result(column = "classcode",
                    property = "classInfo",
                    one = @One(
                            select = "com.you07.eas.dao.ClassInfoDao.selectByPrimaryKey"
                    )
            )
    })
    List<StudentInfo> searchWithCodeName(@Param("keyword") String keyword);

    /**
     * 根据关键词、有权限的班级代码、学年
     * 搜索学生
     * @param keyWord
     * @param privilegeOrgCodes
     * @param schoolYear
     * @return
     */
    @Select({
            "select es.* from eas_studentinfo es, eas_classinfo ec, eas_major em",
            "where (es.studentno like '%${keyWord}%' or es.name like '%${keyWord}%')",
            "and es.classcode in (${privilegeOrgCodes})",
            "and es.classcode = ec.classcode and ec.major_code = em.major_code",
            "and (TO_NUMBER(ec.grade, '9999') + TO_NUMBER(em.major_last, '9999')) > #{schoolYear}",
            "order by classcode"
    })
    @Results({
            @Result(column = "classcode",
                    property = "classInfo",
                    one = @One(
                            select = "com.you07.eas.dao.ClassInfoDao.selectByPrimaryKey"
                    )
            )
    })
    List<StudentInfo> selectWithPrivilegeOrgCodes(@Param("keyWord") String keyWord,
                                                @Param("privilegeOrgCodes") String privilegeOrgCodes,
                                                  @Param("schoolYear") Integer schoolYear);
}
