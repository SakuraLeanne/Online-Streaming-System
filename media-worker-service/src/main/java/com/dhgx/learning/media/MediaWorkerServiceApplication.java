package com.dhgx.learning.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 独立媒体处理服务启动类。
 */
@SpringBootApplication(scanBasePackages = "com.dhgx.learning")
public class MediaWorkerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaWorkerServiceApplication.class, args);
    }
}
