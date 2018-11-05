package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.LocationLatest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationLatestDao extends BaseDao<LocationLatest> {
    /**
     * 根据上网账号/MAC
     * 注销绑定的信息
     * @param accountMac
     * @return
     */
    @Update({
            "update location_latest set account_mac = null where account_mac = #{accountMac}"
    })
    int invalidMac(@Param("accountMac") String accountMac);

    /**
     * 根据上网账号/MAC获取信息
     * @param accountMac
     * @return
     */
    @Select({
            "select * from location_latest where account_mac = #{accountMac} limit 1"
    })
    LocationLatest loadByAccountMac(@Param("accountMac") String accountMac);
}
