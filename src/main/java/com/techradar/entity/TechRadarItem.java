package com.techradar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 技术动态条目实体
 * 存储从各平台采集的技术动态信息
 * 
 * @author YuanXing
 */
@Entity
@Table(name = "tech_radar_items", indexes = {
    @Index(name = "idx_source_url", columnList = "source, url"),
    @Index(name = "idx_published_at", columnList = "publishedAt"),
    @Index(name = "idx_fetched_at", columnList = "fetchedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechRadarItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(nullable = false, length = 500)
    private String title;

    /**
     * 来源平台：github/hackernews/zhihu/weibo
     */
    @Column(nullable = false, length = 50)
    private String source;

    /**
     * 原始链接
     */
    @Column(nullable = false, length = 1000)
    private String url;

    /**
     * 描述/简介
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 点赞数/Stars数
     */
    @Builder.Default
    private Integer stars = 0;

    /**
     * 发布时间
     */
    @Column
    private LocalDateTime publishedAt;

    /**
     * 采集时间
     */
    @Column(nullable = false)
    private LocalDateTime fetchedAt;

    /**
     * 关联的关键词（逗号分隔）
     */
    @Column(length = 500)
    private String keywords;

    @PrePersist
    protected void onCreate() {
        fetchedAt = LocalDateTime.now();
    }
}
