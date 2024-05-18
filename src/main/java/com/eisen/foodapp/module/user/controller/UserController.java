package com.eisen.foodapp.module.user.controller;

import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.RoleRepository;
import com.eisen.foodapp.module.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/")
    public ResponseEntity store(@Valid @RequestBody CreateUserDTO data) {
        User user = User.from(data);

        var clientRole = roleRepository.findByRole("CLIENT");
        user.addRole(clientRole);
        var savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/")
    public String hello() {
        return "hello";
    }
}
