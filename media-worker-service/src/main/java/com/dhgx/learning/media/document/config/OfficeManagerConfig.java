package com.dhgx.learning.media.document.config;

import com.dhgx.learning.media.document.exception.DocumentConvertException;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * LibreOffice/JODConverter 本地转换配置。
 */
@Configuration
public class OfficeManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(OfficeManagerConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "document.convert")
    public DocumentConvertProperties documentConvertProperties() {
        return new DocumentConvertProperties();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public OfficeManager officeManager(DocumentConvertProperties properties) throws OfficeException {
        LocalOfficeManager.Builder builder = LocalOfficeManager.builder();

        if (properties.getOfficeHome() != null && !properties.getOfficeHome().trim().isEmpty()) {
            builder.officeHome(new File(properties.getOfficeHome()));
        }

        File workingDir = resolveWorkingDir(properties.getWorkingDir());
        builder.workingDir(workingDir);

        if (properties.getTaskExecutionTimeout() != null) {
            builder.taskExecutionTimeout(properties.getTaskExecutionTimeout());
        }
        if (properties.getTaskQueueTimeout() != null) {
            builder.taskQueueTimeout(properties.getTaskQueueTimeout());
        }

        return builder.build();
    }

    @Bean
    public DocumentConverter documentConverter(OfficeManager officeManager) {
        return LocalConverter.make(officeManager);
    }

    private File resolveWorkingDir(String configuredWorkingDir) {
        File workingDir;
        if (configuredWorkingDir == null || configuredWorkingDir.trim().isEmpty()) {
            workingDir = new File(System.getProperty("java.io.tmpdir"), "libreoffice");
        } else {
            workingDir = new File(configuredWorkingDir);
        }

        if (!workingDir.exists()) {
            boolean created = workingDir.mkdirs();
            if (!created) {
                throw new DocumentConvertException("Failed to create office working directory: " + workingDir.getAbsolutePath());
            }
            log.info("Created office working directory: {}", workingDir.getAbsolutePath());
        }

        if (!workingDir.isDirectory()) {
            throw new DocumentConvertException("Office working path is not a directory: " + workingDir.getAbsolutePath());
        }
        return workingDir;
    }

    /**
     * 文档转换相关配置。
     */
    public static class DocumentConvertProperties {

        /**
         * LibreOffice 安装目录，例如：/usr/lib/libreoffice。
         */
        private String officeHome;

        /**
         * LibreOffice 工作目录。
         */
        private String workingDir;

        /**
         * 单个任务执行超时时间（毫秒）。
         */
        private Long taskExecutionTimeout = 120000L;

        /**
         * 任务排队超时时间（毫秒）。
         */
        private Long taskQueueTimeout = 30000L;

        public String getOfficeHome() {
            return officeHome;
        }

        public void setOfficeHome(String officeHome) {
            this.officeHome = officeHome;
        }

        public String getWorkingDir() {
            return workingDir;
        }

        public void setWorkingDir(String workingDir) {
            this.workingDir = workingDir;
        }

        public Long getTaskExecutionTimeout() {
            return taskExecutionTimeout;
        }

        public void setTaskExecutionTimeout(Long taskExecutionTimeout) {
            this.taskExecutionTimeout = taskExecutionTimeout;
        }

        public Long getTaskQueueTimeout() {
            return taskQueueTimeout;
        }

        public void setTaskQueueTimeout(Long taskQueueTimeout) {
            this.taskQueueTimeout = taskQueueTimeout;
        }
    }
}
