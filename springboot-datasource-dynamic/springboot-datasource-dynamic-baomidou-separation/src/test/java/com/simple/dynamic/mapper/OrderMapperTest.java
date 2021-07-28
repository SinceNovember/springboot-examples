package com.simple.dynamic.mapper;

import com.simple.dynamic.Application;
import com.simple.dynamic.entity.OrderDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 测试负载均衡
     */
    @Test
    public void testSelectById() {
        for (int i = 0; i < 10; i++) {
            OrderDO order = orderMapper.selectById(1);
            System.out.println(order);
        }
    }

    @Test
    public void testInsert() {
        OrderDO order = new OrderDO();
        order.setUserId(10);
        orderMapper.insert(order);
    }

}