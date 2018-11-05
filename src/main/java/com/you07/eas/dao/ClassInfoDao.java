package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.ClassInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassInfoDao extends BaseDao<ClassInfo> {

    @Select({
            "select * from eas_classinfo where major_code = #{majorCode} order by classcode"
    })
    List<ClassInfo> queryWithMajor(@Param("majorCode") String majorCode);

    @Select({
            "select ec.* from eas_classinfo ec, eas_major em",
            "where ec.major_code = em.major_code and em.acadamey_code = #{acadameyCode}",
            "and (TO_NUMBER(ec.grade, '9999') + TO_NUMBER(em.major_last, '9999')) > #{schoolYear}",
            "order by classcode"
    })
    List<ClassInfo> queryInSchoolWithAcadamey(@Param("acadameyCode") String acadameyCode,
                                      @Param("schoolYear") Integer schoolYear);

    /**
     * 根据关键词、有权限的班级代码、学年
     * 搜索班级
     * @param keyWord
     * @param privilegeOrgCodes
     * @param schoolYear
     * @return
     */
    @Select({
            "select ec.* from eas_classinfo ec, eas_major em",
            "where (ec.classcode like '%${keyWord}%' or ec.classname like '%${keyWord}%')",
            "and ec.classcode in (${privilegeOrgCodes})",
            "and ec.major_code = em.major_code",
            "and (TO_NUMBER(ec.grade, '9999') + TO_NUMBER(em.major_last, '9999')) > #{schoolYear}",
            "order by classcode"
    })
    List<ClassInfo> selectWithPrivilegeOrgCodes(@Param("keyWord") String keyWord,
                                              @Param("privilegeOrgCodes") String privilegeOrgCodes,
                                                @Param("schoolYear") Integer schoolYear);
}
