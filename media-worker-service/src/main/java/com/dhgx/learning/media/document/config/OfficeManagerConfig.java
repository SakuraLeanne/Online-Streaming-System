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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        File officeHome = resolveOfficeHome(properties.getOfficeHome());
        if (officeHome != null) {
            builder.officeHome(officeHome);
        }

        if (properties.getWorkingDir() != null && !properties.getWorkingDir().trim().isEmpty()) {
            builder.workingDir(ensureDirectory(properties.getWorkingDir()));
        }
        if (properties.getTaskExecutionTimeout() != null) {
            builder.taskExecutionTimeout(properties.getTaskExecutionTimeout());
        }
        if (properties.getTaskQueueTimeout() != null) {
            builder.taskQueueTimeout(properties.getTaskQueueTimeout());
        }

        return builder.build();
    }

    private File ensureDirectory(String path) {
        Path directory = Paths.get(path);
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create document convert working directory: " + path, e);
        }
        return directory.toFile();
    }

    private File resolveOfficeHome(String configuredOfficeHome) {
        if (configuredOfficeHome != null && !configuredOfficeHome.trim().isEmpty()) {
            return normalizeOfficeHome(new File(configuredOfficeHome.trim()));
        }

        for (String candidate : defaultOfficeHomes()) {
            File resolved = normalizeOfficeHome(new File(candidate));
            if (resolved != null) {
                return resolved;
            }
        }
        return null;
    }

    private File normalizeOfficeHome(File configured) {
        if (!configured.exists()) {
            return null;
        }

        if (configured.isFile()) {
            if ("soffice".equals(configured.getName()) || "soffice.bin".equals(configured.getName())) {
                File parent = configured.getParentFile();
                if (parent == null) {
                    return null;
                }
                if ("MacOS".equals(parent.getName()) || "program".equals(parent.getName())) {
                    return parent.getParentFile();
                }
                return parent;
            }
            return configured.getParentFile();
        }

        if (new File(configured, "Contents/MacOS/soffice.bin").exists()) {
            return configured;
        }
        if (new File(configured, "program/soffice.bin").exists()) {
            return configured;
        }
        if (new File(configured, "MacOS/soffice.bin").exists()) {
            if ("Contents".equals(configured.getName())) {
                return configured;
            }
            return configured.getParentFile();
        }
        if (new File(configured, "soffice.bin").exists()) {
            if ("MacOS".equals(configured.getName()) || "program".equals(configured.getName())) {
                return configured.getParentFile();
            }
            return configured;
        }
        if (new File(configured, "soffice").exists()) {
            if ("MacOS".equals(configured.getName()) || "program".equals(configured.getName())) {
                return configured.getParentFile();
            }
            return configured;
        }
        return null;
    }

    private List<String> defaultOfficeHomes() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH);
        List<String> paths = new ArrayList<String>();
        if (os.contains("mac")) {
            paths.add("/Applications/LibreOffice.app/Contents");
            paths.add("/Applications/OpenOffice.app/Contents");
        } else if (os.contains("win")) {
            paths.add("C:/Program Files/LibreOffice");
            paths.add("C:/Program Files (x86)/LibreOffice");
            paths.add("C:/Program Files/OpenOffice 4");
            paths.add("C:/Program Files (x86)/OpenOffice 4");
        } else {
            paths.add("/usr/lib/libreoffice");
            paths.add("/usr/lib64/libreoffice");
            paths.add("/opt/libreoffice");
            paths.add("/usr/lib/openoffice");
            paths.add("/opt/openoffice4");
        }
        return paths;
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
