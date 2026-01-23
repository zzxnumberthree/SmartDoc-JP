package com.spe.smartdocjp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
@Entity
@SuperBuilder

public class Document extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String originalFilename; // 原始文件名

    @Column(name = "path",nullable = false)
    private String storagePath;

    @Column(columnDefinition = "TEXT")
    private String summary;

    // 面试考点：多对一 关系
    // FetchType.LAZY 是性能优化的关键，用到 User 时才去查数据库
    @ManyToOne(fetch = FetchType.LAZY) // 能 LAZY 的，一定 LAZY，默认会多出一条查询语句，降低性能
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 一个document 属于一个 user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocStatus status = DocStatus.uploaded;

    public enum DocStatus { // AI处理情况，因为需要被外部调用，所以public
        uploaded, processing, completed, failed
    }

    @PrePersist // 防御式编程
    public void prePersist() {
        if (status == null) {
            status = DocStatus.uploaded;
        }
    }


}
