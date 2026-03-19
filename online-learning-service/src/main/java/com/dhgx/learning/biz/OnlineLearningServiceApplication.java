package com.dhgx.learning.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在线播放系统主业务服务启动类。
 */
@SpringBootApplication(scanBasePackages = "com.dhgx.learning")
public class OnlineLearningServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineLearningServiceApplication.class, args);
    }
}
