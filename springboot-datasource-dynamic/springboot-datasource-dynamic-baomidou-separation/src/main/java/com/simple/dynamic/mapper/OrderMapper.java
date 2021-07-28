package com.simple.dynamic.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.simple.dynamic.constant.DBConstants;
import com.simple.dynamic.entity.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

    @DS(DBConstants.DATASOURCE_SLAVE)
    OrderDO selectById(@Param("id") Integer id);

    @DS(DBConstants.DATASOURCE_MASTER)
    int insert(OrderDO entity);

}