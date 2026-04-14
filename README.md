# 个人技术雷达 (Tech Radar)

> 自动追踪 GitHub、Hacker News 等平台的技术动态，智能筛选与你相关的技术趋势。

## 项目简介

个人技术雷达是一个基于 Spring Boot 的自动化技术情报收集系统，能够定期从多个平台采集热门项目、技术趋势，并通过关键词匹配为你过滤出真正有价值的技术动态。

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 17 | 编程语言 |
| Spring Boot 3.2.x | 后端框架 |
| Spring Data JPA | 数据持久化 |
| MySQL | 数据存储 |
| Lombok | 代码简化 |
| RestTemplate | HTTP 客户端 |

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 配置

1. 克隆项目后，修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_radar?useUnicode=true&characterEncoding=utf8
    username: your_username
    password: your_password
```

2. 创建数据库：

```sql
CREATE DATABASE tech_radar CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 运行项目：

```bash
mvn spring-boot:run
```

4. 访问 API：`http://localhost:8080/api/radar`

## 项目结构

```
tech-radar/
├── pom.xml                          # Maven 配置
├── README.md                        # 项目说明文档
├── docs/
│   └── 开发计划.md                   # 详细开发计划
└── src/
    └── main/
        ├── java/com/techradar/
        │   ├── TechRadarApplication.java      # 启动类
        │   ├── config/
        │   │   └── RestTemplateConfig.java    # HTTP 客户端配置
        │   ├── controller/
        │   │   └── RadarController.java       # REST 接口
        │   ├── service/
        │   │   ├── GitHubCollectorService.java  # GitHub 采集服务
        │   │   └── KeywordService.java          # 关键词服务
        │   ├── entity/
        │   │   ├── TechKeyword.java           # 关键词实体
        │   │   └── TechRadarItem.java         # 动态条目实体
        │   ├── repository/
        │   │   ├── TechKeywordRepository.java
        │   │   └── TechRadarItemRepository.java
        │   └── dto/
        │       └── GitHubTrendingDTO.java    # GitHub API DTO
        └── resources/
            └── application.yml               # 应用配置
```

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/radar/items` | GET | 获取技术动态列表 |
| `/api/radar/keywords` | GET | 获取关键词列表 |
| `/api/radar/keywords` | POST | 添加关键词 |
| `/api/radar/fetch` | POST | 触发数据采集 |

## License

MIT License
