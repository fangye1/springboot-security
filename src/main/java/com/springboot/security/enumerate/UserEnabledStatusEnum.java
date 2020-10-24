package com.springboot.security.enumerate;

import lombok.Getter;

/**
 * 用户启用状态
 */
@Getter
public enum UserEnabledStatusEnum {
    ENABLE(0, "启用"),
    DISABLE(1, "停用");

    private Integer code;
    private String name;

    UserEnabledStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
