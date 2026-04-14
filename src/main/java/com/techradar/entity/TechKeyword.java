package com.techradar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 技术关键词实体
 * 用于管理和存储需要追踪的技术关键词
 * 
 * @author YuanXing
 */
@Entity
@Table(name = "tech_keywords")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关键词名称
     */
    @Column(nullable = false, length = 100)
    private String keyword;

    /**
     * 分类：frontend/backend/infra/ai/devops/other
     */
    @Column(length = 50)
    private String category;

    /**
     * 是否启用追踪
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
