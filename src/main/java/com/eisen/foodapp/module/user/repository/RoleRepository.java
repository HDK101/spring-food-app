package com.eisen.foodapp.module.user.repository;

import com.eisen.foodapp.module.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
    List<Role> findAllByRoleIn(Iterable<String> role);
}
