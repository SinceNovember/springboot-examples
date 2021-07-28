package com.simple.jpa.repository;

import com.simple.jpa.entity.UserDO;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository01 extends CrudRepository<UserDO, Integer> {
}
