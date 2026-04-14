package com.techradar.repository;

import com.techradar.entity.TechKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关键词数据访问层
 * 
 * @author YuanXing
 */
@Repository
public interface TechKeywordRepository extends JpaRepository<TechKeyword, Long> {

    /**
     * 查询所有启用的关键词
     */
    List<TechKeyword> findByEnabledTrue();

    /**
     * 根据分类查询启用的关键词
     */
    List<TechKeyword> findByCategoryAndEnabledTrue(String category);

    /**
     * 检查关键词是否存在
     */
    boolean existsByKeyword(String keyword);
}
