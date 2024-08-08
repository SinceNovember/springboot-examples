package com.simple.mapper;

import com.simple.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("INSERT INTO t_user (id, name) VALUES (#{id}, #{name})")
    void insertUser(User user);
}
