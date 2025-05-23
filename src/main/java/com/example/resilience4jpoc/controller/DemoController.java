package com.example.resilience4jpoc.controller;

import com.example.resilience4jpoc.service.ExternalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 演示端点控制器
 * 包含：
 * 1. 正常调用端点
 * 2. 强制失败端点
 * 3. 状态重置端点
 */
@RestController
@RequestMapping("/api")
public class DemoController {
    
    private final ExternalService externalService;

    public DemoController(ExternalService externalService) {
        this.externalService = externalService;
    }

    @GetMapping("/normal")
    public Mono<String> normalCall() {
        return externalService.unreliableService(false)
                .map(result -> "调用结果: " + result);
    }

    @GetMapping("/fail")
    public Mono<String> forcedFailure() {
        return externalService.unreliableService(true)
                .map(result -> "调用结果: " + result);
    }

    @GetMapping("/reset")
    public String resetCounter() {
        // 重置计数器用于测试
        externalService.resetSuccessCounter();
        return "计数器已重置";
    }
}