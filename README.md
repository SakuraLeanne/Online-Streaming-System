# Online-Streaming-System

在线播放系统采用 **1 个主业务服务 + 1 个独立媒体处理服务 + 1 个公共能力模块** 的架构。

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

## 基础能力组件使用说明：Word / PPT 转 PDF

`media-worker-service` 内置了基于 **LibreOffice + JODConverter** 的文档转 PDF 基础能力组件，默认支持以下输入格式：

- Word：`.doc`、`.docx`
- PPT：`.ppt`、`.pptx`

### 1) 环境准备

1. 机器上安装 LibreOffice（或 OpenOffice）。
2. 如未安装在系统默认路径，可通过 `document.convert.office-home` 指定安装目录。

### 2) 配置项说明（`media-worker-service/src/main/resources/application.yml`）

```yaml
document:
  convert:
    office-home: /usr/lib/libreoffice
    working-dir: ${java.io.tmpdir}/libreoffice-work
    task-execution-timeout: 120000
    task-queue-timeout: 30000
```

- `office-home`：LibreOffice 安装目录（可选，不填会按操作系统尝试默认路径）。
- `working-dir`：转换工作目录（不存在会自动创建）。
- `task-execution-timeout`：单个转换任务超时时间（毫秒）。
- `task-queue-timeout`：转换任务排队超时时间（毫秒）。

### 3) 代码中调用方式

在 Spring 业务代码中注入 `DocumentConvertService` 并调用 `convertToPdf`：

```java
@Service
public class DemoUseCase {

    private final DocumentConvertService documentConvertService;

    public DemoUseCase(DocumentConvertService documentConvertService) {
        this.documentConvertService = documentConvertService;
    }

    public String convert() {
        File inputFile = new File("/data/input/chapter1.pptx");
        File outputDir = new File("/data/output");

        DocumentConvertResult result = documentConvertService.convertToPdf(inputFile, outputDir);
        return result.getOutputFilePath();
    }
}
```

### 4) 行为说明

- 输出 PDF 文件名与原文件同名，仅后缀变更为 `.pdf`。
- 若输出目录不存在会自动创建。
- 若输出目录中已存在同名 PDF，会被覆盖。
- 输入文件不存在、格式不支持或转换失败时，会抛出 `DocumentConvertException`。
