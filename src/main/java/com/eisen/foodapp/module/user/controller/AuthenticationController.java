package com.eisen.foodapp.module.user.controller;

import com.eisen.foodapp.common.service.TokenService;
import com.eisen.foodapp.module.user.dto.AuthenticationDTO;
import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.LoginResponseDTO;
import com.eisen.foodapp.module.user.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody CreateUserDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
