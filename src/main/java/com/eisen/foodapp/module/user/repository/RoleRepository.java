package com.eisen.foodapp.module.user.repository;

import com.eisen.foodapp.module.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
