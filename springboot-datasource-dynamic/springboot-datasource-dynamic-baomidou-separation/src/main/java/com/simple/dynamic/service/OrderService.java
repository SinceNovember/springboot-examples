package com.simple.dynamic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.simple.dynamic.constant.DBConstants;
import com.simple.dynamic.entity.OrderDO;
import com.simple.dynamic.mapper.OrderMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    @DS(DBConstants.DATASOURCE_MASTER)
    public void add(OrderDO order) {
        // 这里先假模假样的读取一下，
        OrderDO exists = orderMapper.selectById(order.getId());
        System.out.println(exists);

        // 插入订单
        orderMapper.insert(order);
    }

    public OrderDO findById(Integer id) {
        return orderMapper.selectById(id);
    }
}