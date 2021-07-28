package com.simple.mybatis.mapper.users;

import com.simple.mybatis.entity.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserDO selectById(@Param("id") Integer id);

}