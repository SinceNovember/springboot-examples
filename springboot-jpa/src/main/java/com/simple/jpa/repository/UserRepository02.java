package com.simple.jpa.repository;

import com.simple.jpa.entity.UserDO;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 分页操作
 * 第一个泛型设置对应的实体是 UserDO ，第二个泛型设置对应的主键类型是 Integer
 */
public interface UserRepository02 extends PagingAndSortingRepository<UserDO, Integer> {

}