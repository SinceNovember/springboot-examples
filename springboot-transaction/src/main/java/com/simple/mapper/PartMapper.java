package com.simple.mapper;

import com.simple.entity.Part;
import com.simple.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PartMapper {

    @Select("INSERT INTO t_part (id, part_name) VALUES (#{id}, #{partName})")
    void insertPart(Part part);
}
