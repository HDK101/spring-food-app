package com.eisen.foodapp.module.user.controller;

import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.SetRolesDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.RoleRepository;
import com.eisen.foodapp.module.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<Page<User>> index(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();

        return ResponseEntity.ok(user);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<User> store(@Valid @RequestBody CreateUserDTO data) {
        User user = User.from(data);

        var clientRole = roleRepository.findByRole("CLIENT");
        user.addRole(clientRole);
        var savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody CreateUserDTO data, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.put(data);

        var savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable(name = "id") Long id) {
        if (!userRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}/roles")
    public ResponseEntity<User> associateRoles(@PathVariable Long id, @RequestBody SetRolesDTO data) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var roles = roleRepository.findAllById(data.roleIds());

        user.clearRoles();
        roles.forEach(user::addRole);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
