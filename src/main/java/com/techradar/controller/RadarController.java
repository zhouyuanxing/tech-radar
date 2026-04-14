package com.techradar.controller;

import com.techradar.entity.TechKeyword;
import com.techradar.entity.TechRadarItem;
import com.techradar.repository.TechRadarItemRepository;
import com.techradar.service.GitHubCollectorService;
import com.techradar.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技术雷达 REST API 控制器
 * 提供技术动态和关键词的管理接口
 * 
 * @author YuanXing
 */
@Slf4j
@RestController
@RequestMapping("/api/radar")
@RequiredArgsConstructor
public class RadarController {

    private final GitHubCollectorService gitHubCollectorService;
    private final KeywordService keywordService;
    private final TechRadarItemRepository itemRepository;

    // ==================== 技术动态接口 ====================

    /**
     * 获取技术动态列表
     * 
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @param source 来源筛选（可选）
     * @param keyword 关键词筛选（可选）
     */
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TechRadarItem> items;
        
        if (source != null && !source.isEmpty()) {
            items = itemRepository
                    .findBySourceOrderByFetchedAtDesc(source, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            items = itemRepository
                    .findByKeyword(keyword, pageable);
        } else {
            items = itemRepository
                    .findAllByOrderByFetchedAtDesc(pageable);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("items", items.getContent());
        result.put("total", items.getTotalElements());
        result.put("page", items.getNumber());
        result.put("pages", items.getTotalPages());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取最近的技术动态
     */
    @GetMapping("/items/recent")
    public ResponseEntity<List<TechRadarItem>> getRecentItems() {
        return ResponseEntity.ok(itemRepository
                .findTop10ByOrderByFetchedAtDesc());
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalItems", itemRepository.count());
        stats.put("totalKeywords", keywordService.getAllKeywords().size());
        stats.put("enabledKeywords", keywordService.getEnabledKeywords().size());
        return ResponseEntity.ok(stats);
    }

    // ==================== 关键词接口 ====================

    /**
     * 获取所有关键词
     */
    @GetMapping("/keywords")
    public ResponseEntity<List<TechKeyword>> getKeywords(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean enabled) {
        
        List<TechKeyword> keywords;
        
        if (category != null && !category.isEmpty()) {
            keywords = keywordService.getKeywordsByCategory(category);
        } else if (Boolean.TRUE.equals(enabled)) {
            keywords = keywordService.getEnabledKeywords();
        } else {
            keywords = keywordService.getAllKeywords();
        }
        
        return ResponseEntity.ok(keywords);
    }

    /**
     * 添加关键词
     */
    @PostMapping("/keywords")
    public ResponseEntity<Map<String, Object>> addKeyword(
            @RequestBody Map<String, String> request) {
        
        String keyword = request.get("keyword");
        String category = request.getOrDefault("category", "other");
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "关键词不能为空"));
        }
        
        TechKeyword saved = keywordService.addKeyword(keyword.trim(), category);
        
        if (saved == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "关键词已存在"));
        }
        
        return ResponseEntity.ok(Map.of(
                "message", "关键词添加成功",
                "keyword", saved
        ));
    }

    /**
     * 更新关键词状态
     */
    @PutMapping("/keywords/{id}")
    public ResponseEntity<Map<String, Object>> updateKeyword(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        
        try {
            Boolean enabled = (Boolean) request.get("enabled");
            TechKeyword updated = keywordService.updateKeywordStatus(id, enabled);
            return ResponseEntity.ok(Map.of(
                    "message", "更新成功",
                    "keyword", updated
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除关键词
     */
    @DeleteMapping("/keywords/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long id) {
        keywordService.deleteKeyword(id);
        return ResponseEntity.ok().build();
    }

    // ==================== 采集接口 ====================

    /**
     * 触发数据采集
     */
    @PostMapping("/fetch")
    public ResponseEntity<Map<String, Object>> triggerFetch(
            @RequestParam(required = false) String language) {
        
        log.info("手动触发数据采集, language={}", language);
        
        int count = gitHubCollectorService.fetchTrending(language, 100);
        
        return ResponseEntity.ok(Map.of(
                "message", "采集完成",
                "newItems", count
        ));
    }
}
