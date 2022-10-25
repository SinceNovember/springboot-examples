package com.simple.clickhouse.demo.mapper.clickhouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.clickhouse.demo.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    void insertData(UserInfo userInfo);

    @Select("select * from cs_user_info where id =#{id}")
    UserInfo findById(@Param("id") Integer id);

    @Select("select * from cs_user_info")
    List<UserInfo> findList();


}
