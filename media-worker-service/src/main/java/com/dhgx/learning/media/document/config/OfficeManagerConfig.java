package com.dhgx.learning.media.document.config;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * LibreOffice/JODConverter 本地转换配置。
 */
@Configuration
public class OfficeManagerConfig {

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
        if (properties.getWorkingDir() != null && !properties.getWorkingDir().trim().isEmpty()) {
            builder.workingDir(new File(properties.getWorkingDir()));
        }
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
