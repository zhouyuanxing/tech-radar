package com.techradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 个人技术雷达 - 启动类
 * 
 * @author YuanXing
 */
@SpringBootApplication
@EnableScheduling
public class TechRadarApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechRadarApplication.class, args);
    }
}
