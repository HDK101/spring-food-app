package com.eisen.foodapp.module.user.model;


import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({ "password", "rawPassword", "authorities", "roles" })
@Entity(name = "users")
public class User implements UserDetails {
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
    @SequenceGenerator(name = "users_generator", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;

    @Column
    private String login;

    @Column(name = "password_hash")
    private String password;

    @Transient
    private String rawPassword;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles = new HashSet<>();

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public static User from(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setName(createUserDTO.name());
        user.setLogin(createUserDTO.login());
        user.setRawPassword(createUserDTO.password());
        return user;
    }

    public void put(CreateUserDTO createUserDTO) {
        this.setName(createUserDTO.name());
        this.setLogin(createUserDTO.login());
        this.setRawPassword(createUserDTO.password());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(Role::getAsAuthority).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    public void hashPassword() {
        var bcryptEncoder = new BCryptPasswordEncoder();
        this.password = bcryptEncoder.encode(this.rawPassword);
    }
}
