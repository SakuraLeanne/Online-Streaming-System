package com.dhgx.learning.biz.controller;

import com.dhgx.learning.common.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主业务服务健康检查。
 */
@RestController
@RequestMapping("/api/biz")
public class BizHealthController {

    @GetMapping("/health")
    public CommonResponse<String> health() {
        return CommonResponse.success("online-learning-service is running");
    }
}
