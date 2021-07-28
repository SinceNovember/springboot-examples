package com.simple.sharding.mapper;

import com.simple.sharding.entity.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

    OrderDO selectById(@Param("id") Integer id);

    int insert(OrderDO entity);

}