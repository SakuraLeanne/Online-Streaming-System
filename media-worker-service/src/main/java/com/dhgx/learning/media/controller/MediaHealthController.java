package com.dhgx.learning.media.controller;

import com.dhgx.learning.common.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 媒体处理服务健康检查。
 */
@RestController
@RequestMapping("/api/media")
public class MediaHealthController {

    @GetMapping("/health")
    public CommonResponse<String> health() {
        return CommonResponse.success("media-worker-service is running");
    }
}
