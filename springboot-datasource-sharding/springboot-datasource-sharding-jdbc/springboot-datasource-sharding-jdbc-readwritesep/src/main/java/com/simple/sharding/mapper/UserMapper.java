package com.simple.sharding.mapper;


import com.simple.sharding.entity.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    UserDO selectById(@Param("id") Integer id);

    List<UserDO> selectListByName(@Param("name") String name);

    List<UserDO> selectUserList();

    void insert(UserDO order);

}