package com.simple.jpa.repository;

import com.simple.jpa.entity.UserDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface UserRepository03 extends PagingAndSortingRepository<UserDO, Integer> {

    UserDO findByUsername(String username);

    Page<UserDO> findByCreateTimeAfter(Date createTime, Pageable pageable);

}