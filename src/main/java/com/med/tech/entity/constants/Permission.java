package com.med.tech.entity.constants;

public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write"),
    PRODUCTS_READ("products:read"),
    PRODUCTS_WRITE("products:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
