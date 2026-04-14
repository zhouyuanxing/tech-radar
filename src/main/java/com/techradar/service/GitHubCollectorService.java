package com.techradar.service;

import com.techradar.dto.GitHubTrendingDTO;
import com.techradar.entity.TechRadarItem;
import com.techradar.repository.TechRadarItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GitHub Trending 采集服务
 * 负责从 GitHub Trending API 获取热门项目
 * 
 * @author YuanXing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubCollectorService {

    private final RestTemplate restTemplate;
    private final TechRadarItemRepository itemRepository;
    private final KeywordService keywordService;

    @Value("${github.api.base-url:https://api.github.com}")
    private String baseUrl;

    @Value("${github.api.trending-url:https://api.github.com/search/repositories}")
    private String trendingUrl;

    /**
     * 采集 GitHub Trending 数据
     * 
     * @param language 编程语言筛选（可选）
     * @param perPage 每页数量
     * @return 采集的条目数量
     */
    public int fetchTrending(String language, int perPage) {
        log.info("开始采集 GitHub Trending，language={}, perPage={}", language, perPage);
        
        try {
            // TODO: Day 5-7 实现完整的 GitHub API 调用逻辑
            // 1. 构建查询参数
            // 2. 调用 GitHub Search API
            // 3. 解析响应数据
            // 4. 去重检查
            // 5. 关键词匹配
            // 6. 数据入库
            
            // 骨架代码：返回模拟数据
            List<GitHubTrendingDTO> trendingItems = fetchFromGitHub(language, perPage);
            
            int savedCount = 0;
            for (GitHubTrendingDTO item : trendingItems) {
                if (saveIfNotExists(item)) {
                    savedCount++;
                }
            }
            
            log.info("GitHub Trending 采集完成，新增 {} 条记录", savedCount);
            return savedCount;
            
        } catch (Exception e) {
            log.error("采集 GitHub Trending 失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 从 GitHub API 获取数据
     * 
     * TODO: Day 5-7 完善 API 调用实现
     */
    private List<GitHubTrendingDTO> fetchFromGitHub(String language, int perPage) {
        // 骨架：构建请求URL
        StringBuilder urlBuilder = new StringBuilder(trendingUrl);
        urlBuilder.append("?q=created:>");
        urlBuilder.append(getDateNDaysAgo(30)); // 最近30天创建的项目
        urlBuilder.append("&sort=stars&order=desc");
        
        if (language != null && !language.isEmpty()) {
            urlBuilder.append("&language=").append(language);
        }
        urlBuilder.append("&per_page=").append(perPage);
        
        log.debug("GitHub API URL: {}", urlBuilder);
        
        // TODO: 实际调用时使用
        // GitHubSearchResponse response = restTemplate.getForObject(urlBuilder.toString(), GitHubSearchResponse.class);
        // return response.getItems();
        
        // 骨架：返回空列表
        return new ArrayList<>();
    }

    /**
     * 保存不重复的条目
     */
    private boolean saveIfNotExists(GitHubTrendingDTO dto) {
        // 检查是否已存在
        if (itemRepository.existsBySourceAndUrl("github", dto.getHtmlUrl())) {
            return false;
        }
        
        // 检查关键词匹配
        List<String> matchedKeywords = keywordService.getMatchedKeywords(
                dto.getName(), 
                dto.getDescription()
        );
        
        // 如果没有匹配的关键词且用户设置了只保存匹配项，则跳过
        // if (matchedKeywords.isEmpty() && onlyMatched) return false;
        
        TechRadarItem item = TechRadarItem.builder()
                .title(dto.getName())
                .source("github")
                .url(dto.getHtmlUrl())
                .description(dto.getDescription())
                .stars(dto.getStargazersCount())
                .publishedAt(dto.getCreatedAt())
                .fetchedAt(LocalDateTime.now())
                .keywords(String.join(",", matchedKeywords))
                .build();
        
        itemRepository.save(item);
        return true;
    }

    /**
     * 获取N天前的日期字符串
     */
    private String getDateNDaysAgo(int days) {
        return LocalDateTime.now().minusDays(days).toLocalDate().toString();
    }

    /**
     * 触发采集（供定时任务或API调用）
     */
    public void triggerFetch() {
        fetchTrending(null, 100);
    }
}
