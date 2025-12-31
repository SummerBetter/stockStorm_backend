package com.hxun.stockstorm_backend.entity;

/**
 * 用户角色枚举
 */
public enum UserRole {
    /**
     * 普通用户
     */
    NORMAL("普通用户"),

    /**
     * VIP用户
     */
    VIP("VIP用户"),

    /**
     * 管理员
     */
    ADMIN("管理员");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
