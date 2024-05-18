package com.eisen.foodapp.module.user.repository;

import com.eisen.foodapp.module.user.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
