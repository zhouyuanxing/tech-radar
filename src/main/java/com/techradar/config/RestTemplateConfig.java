package com.techradar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate HTTP客户端配置
 * 
 * @author YuanXing
 */
@Configuration
public class RestTemplateConfig {

    @Value("${github.api.timeout:5000}")
    private int timeout;

    /**
     * 配置 RestTemplate 实例
     * - 设置连接超时和读取超时
     * - 启用压缩支持
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}
