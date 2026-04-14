package com.techradar.service;

import com.techradar.entity.TechKeyword;
import com.techradar.repository.TechKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关键词服务
 * 提供关键词的CRUD和匹配功能
 * 
 * @author YuanXing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final TechKeywordRepository keywordRepository;

    /**
     * 获取所有启用的关键词
     */
    public List<TechKeyword> getEnabledKeywords() {
        return keywordRepository.findByEnabledTrue();
    }

    /**
     * 根据分类获取启用的关键词
     */
    public List<TechKeyword> getKeywordsByCategory(String category) {
        return keywordRepository.findByCategoryAndEnabledTrue(category);
    }

    /**
     * 获取所有关键词
     */
    public List<TechKeyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    /**
     * 添加关键词
     */
    public TechKeyword addKeyword(String keyword, String category) {
        // 检查是否已存在
        if (keywordRepository.existsByKeyword(keyword)) {
            log.warn("关键词已存在: {}", keyword);
            return null;
        }

        TechKeyword entity = TechKeyword.builder()
                .keyword(keyword)
                .category(category)
                .enabled(true)
                .build();

        return keywordRepository.save(entity);
    }

    /**
     * 更新关键词状态
     */
    public TechKeyword updateKeywordStatus(Long id, Boolean enabled) {
        TechKeyword keyword = keywordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("关键词不存在: " + id));
        
        keyword.setEnabled(enabled);
        return keywordRepository.save(keyword);
    }

    /**
     * 删除关键词
     */
    public void deleteKeyword(Long id) {
        keywordRepository.deleteById(id);
    }

    /**
     * 匹配标题是否包含关键词
     */
    public boolean matchesKeyword(String title, String description) {
        List<TechKeyword> keywords = getEnabledKeywords();
        String content = (title + " " + description).toLowerCase();
        
        return keywords.stream()
                .anyMatch(k -> content.contains(k.getKeyword().toLowerCase()));
    }

    /**
     * 获取匹配的关键词列表
     */
    public List<String> getMatchedKeywords(String title, String description) {
        List<TechKeyword> keywords = getEnabledKeywords();
        String content = (title + " " + description).toLowerCase();
        
        return keywords.stream()
                .filter(k -> content.contains(k.getKeyword().toLowerCase()))
                .map(TechKeyword::getKeyword)
                .toList();
    }
}
