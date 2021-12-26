package com.aca.service;

import com.aca.db.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    Optional<User> findById(Long id);

    Collection<User> findAll();

    Long create(UserCreationParameter userCreationParameter);

    Collection<User> findAllActiveUsers();

}
