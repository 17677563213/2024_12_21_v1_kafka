# SpringBoot Kafka 示例项目

这是一个基于 Spring Boot 和 Apache Kafka 的示例项目，用于演示如何在 Spring Boot 应用程序中集成和使用 Kafka 消息中间件。该项目实现了一个完整的消息发布订阅系统，包含生产者和消费者的实现，并使用 Redis 来确保消息处理的幂等性。

## 技术栈

*   **Spring Boot**: 2.7.0

*   **Java**: 1.8

*   **Apache Kafka**: Spring Kafka 集成

*   **Redis**: 用于实现幂等性控制

*   **Lombok**: 用于简化 Java 代码

*   **Maven**: 项目构建工具

## 项目结构

    src/main/java/com/example/kafka/
    ├── KafkaApplication.java        # 应用程序入口
    ├── config/                      # 配置类目录
    │   └── KafkaConfig.java        # Kafka 配置类
    ├── consumer/                    # Kafka 消费者相关代码
    │   └── MessageConsumer.java    # 消息消费者实现
    ├── controller/                  # REST API 控制器
    │   └── MessageController.java  # 消息发送接口
    ├── model/                      # 数据模型/实体类
    │   └── Message.java           # 消息实体类
    └── producer/                   # Kafka 生产者相关代码
        └── MessageProducer.java   # 消息生产者实现

## 主要功能

### 1. Kafka 消息处理

*   消息生产者：支持发送消息到指定的 Kafka topic

*   消息消费者：自动消费指定 topic 的消息

*   支持异步消息处理

*   集成 Redis 实现消息幂等性控制

### 2. REST API

*   提供 RESTful 接口用于消息发送

*   支持消息状态查询

*   异常统一处理

### 3. 可靠性保证

*   Redis 幂等性控制，避免消息重复消费

*   完整的错误处理机制

*   消息发送确认机制

## 环境要求

*   JDK 1.8 或更高版本

*   Apache Kafka 服务器

*   Redis 服务器

*   Maven 3.x

## 快速开始

### 1. 环境准备

确保已经安装并启动：

*   Kafka 服务器

*   Redis 服务器

*   JDK 1.8+

*   Maven 3.x

### 2. 配置修改

修改 `application.properties` 或 `application.yml` 文件：

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: your-group-id
  redis:
    host: localhost
    port: 6379
```

### 3. 构建运行

```bash
# 克隆项目
git clone [项目地址]

# 进入项目目录
cd springboot-kafka-demo

# 编译打包
mvn clean package

# 运行应用
java -jar target/springboot-kafka-demo-1.0-SNAPSHOT.jar
```

## API 使用示例

### 发送消息

```bash
curl -X POST http://localhost:8080/api/messages \
     -H "Content-Type: application/json" \
     -d '{"content":"Hello, Kafka!"}'
```

## 注意事项

1.  确保 Kafka 和 Redis 服务正常运行

2.  检查配置文件中的连接信息是否正确

3.  首次运行时，确保相关的 Kafka Topic 已创建

## 开发建议

1.  遵循代码规范和最佳实践

2.  添加适当的日志记录

3.  编写单元测试和集成测试

4.  注意异常处理和错误恢复机制

## 贡献指南

1.  Fork 本仓库

2.  创建您的特性分支 (`git checkout -b feature/AmazingFeature`)

3.  提交您的更改 (`git commit -m 'Add some AmazingFeature'`)

4.  推送到分支 (`git push origin feature/AmazingFeature`)

5.  创建一个 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情
