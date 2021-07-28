package com.simple.sharding.mapper;

import com.simple.sharding.entity.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserDO selectById(@Param("id") Integer id);

}