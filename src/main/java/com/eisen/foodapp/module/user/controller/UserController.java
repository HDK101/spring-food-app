package com.eisen.foodapp.module.user.controller;

import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.RoleRepository;
import com.eisen.foodapp.module.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public ResponseEntity<Page<User>> index(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

    @PostMapping("/")
    public ResponseEntity<User> store(@Valid @RequestBody CreateUserDTO data) {
        User user = User.from(data);

        var clientRole = roleRepository.findByRole("CLIENT");
        user.addRole(clientRole);
        var savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody CreateUserDTO data, @RequestParam Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.put(data);

        var savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@RequestParam Long id) {
        if (!userRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
