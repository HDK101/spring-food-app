package com.eisen.foodapp.module.user.model;

public enum Role {
    ADMIN("admin"),
    CLIENT("client"),
    ;

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
