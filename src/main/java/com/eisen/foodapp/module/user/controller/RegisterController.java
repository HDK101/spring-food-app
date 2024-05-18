package com.eisen.foodapp.module.user.controller;

import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.RoleRepository;
import com.eisen.foodapp.module.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/register")
@RestController
public class RegisterController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity register(@Valid @RequestBody RegisterDTO data) {
        if (this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        var role = roleRepository.findByRole("CLIENT");

        var user = new User();
        user.setName(data.name());
        user.setLogin(data.login());
        user.setPassword(data.password());

        user.addRole(role);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
