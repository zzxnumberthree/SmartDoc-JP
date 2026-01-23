package com.spe.smartdocjp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder //
@AllArgsConstructor //
@NoArgsConstructor //
@MappedSuperclass // 告诉JPA这是一个父类，不要映射成单独的表
@EntityListeners(AuditingEntityListener.class) // 启用自动填充时间，等多种方法
public abstract class BaseEntity { // 不允许实例化

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id;

    @CreatedDate // 自动填充时间，与数据库结构一致
    //这个字段映射到数据库的 created_at 列，不能为空，而且在 INSERT 后永远不允许再被 UPDATE 修改。
    @Column(name = "created_at", nullable = false, updatable = false) // 此处用name= 是因为原型有 下划线
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // 默认为未删除，与数据库对应

}
