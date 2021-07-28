package com.simple.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.sharing.entity.OrderDO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface OrderMapper extends BaseMapper<OrderDO> {

}