package com.eisen.foodapp.module.user.repository;

import com.eisen.foodapp.module.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {
    UserDetails findByLogin(String login);
}
