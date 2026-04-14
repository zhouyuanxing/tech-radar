package com.techradar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * GitHub Trending 项目 DTO
 * 用于接收和解析 GitHub API 返回的数据
 * 
 * @author YuanXing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubTrendingDTO {

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目全名（owner/repo）
     */
    @JsonProperty("full_name")
    private String fullName;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 编程语言
     */
    private String language;

    /**
     * Star数量
     */
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;

    /**
     * Fork数量
     */
    @JsonProperty("forks_count")
    private Integer forksCount;

    /**
     * 项目主页
     */
    @JsonProperty("homepage")
    private String homepage;

    /**
     * GitHub 仓库地址
     */
    @JsonProperty("html_url")
    private String htmlUrl;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 发布时间（用于Trending筛选）
     */
    @JsonProperty("pushed_at")
    private LocalDateTime pushedAt;

    /**
     * 仓库所有者信息（内嵌对象）
     */
    private Owner owner;

    /**
     * 仓库所有者 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {
        private Long id;
        private String login;
        
        @JsonProperty("avatar_url")
        private String avatarUrl;
        
        @JsonProperty("html_url")
        private String htmlUrl;
    }
}
