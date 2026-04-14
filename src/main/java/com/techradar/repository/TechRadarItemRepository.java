package com.techradar.repository;

import com.techradar.entity.TechRadarItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 技术动态条目数据访问层
 * 
 * @author YuanXing
 */
@Repository
public interface TechRadarItemRepository extends JpaRepository<TechRadarItem, Long> {

    /**
     * 根据来源和URL查询（用于去重）
     */
    Optional<TechRadarItem> findBySourceAndUrl(String source, String url);

    /**
     * 检查是否存在
     */
    boolean existsBySourceAndUrl(String source, String url);

    /**
     * 分页查询所有条目
     */
    Page<TechRadarItem> findAllByOrderByFetchedAtDesc(Pageable pageable);

    /**
     * 根据来源查询
     */
    Page<TechRadarItem> findBySourceOrderByFetchedAtDesc(String source, Pageable pageable);

    /**
     * 根据关键词查询
     */
    @Query("SELECT r FROM TechRadarItem r WHERE r.keywords LIKE %:keyword% ORDER BY r.fetchedAt DESC")
    Page<TechRadarItem> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查询最近采集的条目
     */
    List<TechRadarItem> findTop10ByOrderByFetchedAtDesc();

    /**
     * 根据时间范围查询
     */
    @Query("SELECT r FROM TechRadarItem r WHERE r.fetchedAt BETWEEN :start AND :end ORDER BY r.fetchedAt DESC")
    List<TechRadarItem> findByTimeRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
