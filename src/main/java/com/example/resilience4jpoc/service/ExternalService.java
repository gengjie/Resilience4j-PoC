package com.example.resilience4jpoc.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 模拟外部服务调用
 * 包含：
 * 1. 正常响应方法
 * 2. 模拟失败方法
 * 3. 熔断后的fallback方法
 */
@Service
public class ExternalService {
    
    // 成功概率计数器
    private int successCounter = 0;

    /**
     * 重置计数器用于测试
     */
    public void resetSuccessCounter() {
        this.successCounter = 0;
    }

    /**
     * 模拟不可靠的外部服务调用
     * @param shouldFail 是否强制失败
     * @return 带延迟的响应
     */
    @CircuitBreaker(name = "backendService", fallbackMethod = "fallback")
    public Mono<String> unreliableService(boolean shouldFail) {
        return Mono.fromCallable(() -> {
            if(shouldFail || ++successCounter % 5 == 0) { // 每5次失败1次
                throw new RuntimeException("External service failure");
            }
            Thread.sleep(200); // 模拟延迟
            return "Service response success";
        });
    }

    /**
     * 熔断器打开时的回退方法
     */
    private Mono<String> fallback(boolean shouldFail, Exception ex) {
        return Mono.just("Fallback response: " + ex.getMessage());
    }
}