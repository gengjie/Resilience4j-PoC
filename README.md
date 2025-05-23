# Resilience4j-PoC
PoC for Resilience4j 


# Resilience4j-PoC
Spring Boot Webflux熔断器验证项目

## 项目结构
```
src/main/java
├── com/example/resilience4jpoc
│   ├── Application.java          # Spring Boot启动类
│   ├── controller
│   │   └── DemoController.java  # 提供测试端点
│   └── service
│       └── ExternalService.java  # 熔断器业务逻辑实现
```

## 熔断器配置参数
```yaml
failure-rate-threshold: 50%     # 触发熔断的失败率阈值
minimum-number-of-calls: 5      # 最小统计样本数
wait-duration-in-open-state: 5s # 熔断持续时间
sliding-window-size: 10         # 统计窗口大小
```

## API端点说明
- GET /api/normal : 模拟正常请求（25%失败率）
- GET /api/fail   : 强制失败请求
- GET /api/reset  : 重置服务计数器

## 测试方法
```bash
# 启动应用
mvn spring-boot:run

# 触发连续请求观察熔断
watch -n 0.5 "curl -s http://localhost:8080/api/normal"
```

## 熔断器状态流程图
``` 
[CLOSED] → (失败率超阈值) → [OPEN] 
    ↑           ↓
    └─ [HALF-OPEN] ← 等待时间结束
```

## 注意事项
1. 熔断状态通过/metrics端点可实时监控
2. 半开状态允许3次探测请求
3. 日志级别设置为DEBUG可查看详细熔断事件
```

使用以下命令启动测试：
```bash
mvn clean package
mvn spring-boot:run
```

        