package com.simple.dynamic.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.simple.dynamic.constant.DBConstants;
import com.simple.dynamic.entity.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_ORDERS)
public interface OrderMapper {
    OrderDO selectById(@Param("id") Integer id);
}
