package com.simple.clickhouse.demo.mapper.mysql;

import com.simple.clickhouse.demo.pojo.GroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GroupInfoMapper {

    @Select("select * from group_info where id=#{id}")
    public GroupInfo findById(@Param("id") Integer id);
}
