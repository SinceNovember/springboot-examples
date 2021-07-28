package com.simple.jpa.repository.users;

import com.simple.jpa.entity.UserDO;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDO, Integer> {

}