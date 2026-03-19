# Online-Streaming-System

在线播放系统一期采用 **1 个主业务服务 + 1 个独立媒体处理服务 + 1 个公共能力模块** 的快速上线架构。

## 模块说明

- `online-learning-service`：主业务服务，承载课程内容管理、目录管理、资源管理、播放鉴权、文档预览、学习记录以及与 e 券平台接口对接等在线能力。
- `media-worker-service`：独立媒体处理服务，负责视频元数据解析、转码分片、多清晰度生成、封面抽帧、Word/PPT 转 PDF 等异步任务。
- `learning-common`：公共能力模块，沉淀统一 DTO、状态枚举、错误码、签名验签、对象存储工具、统一响应结构等通用能力。

## 技术栈

- Java 1.8
- Spring Boot 2.7.28
- Maven 多模块工程

## 快速启动

```bash
mvn clean package
```

启动主业务服务：

```bash
mvn -pl online-learning-service spring-boot:run
```

启动媒体处理服务：

```bash
mvn -pl media-worker-service spring-boot:run
```
