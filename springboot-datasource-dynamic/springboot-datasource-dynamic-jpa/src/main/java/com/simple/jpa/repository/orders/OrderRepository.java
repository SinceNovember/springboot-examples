package com.simple.jpa.repository.orders;

import com.simple.jpa.entity.OrderDO;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderDO, Integer> {

}
