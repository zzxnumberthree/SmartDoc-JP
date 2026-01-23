package com.spe.smartdocjp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users") // user 是数据库保留字
@Getter
@Setter
@NoArgsConstructor // 无参构造法
@AllArgsConstructor // 全参构造法
@SuperBuilder // 构造时的参数可以打乱顺序
public class User extends BaseEntity { // 继承父类BaseEntity

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // 以加密形式存储

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) // 将枚举存为字符串 (如 "ADMIN") 而默认情况是数字，可读性差
    @Column(nullable = false)
    private Role role = Role.USER; // 默认值为USER

    // 定义简单的枚举
    public enum Role {
        USER, ADMIN
    }

    @PrePersist // 防御式编程
    public void prePersist() {
        if (role == null) {
            role = Role.USER;
        }
    }

}
