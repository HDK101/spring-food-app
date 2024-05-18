package com.eisen.foodapp.module.user.repository;

import com.eisen.foodapp.module.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends CrudRepository<User, Long> {
    UserDetails findByLogin(String login);
}
