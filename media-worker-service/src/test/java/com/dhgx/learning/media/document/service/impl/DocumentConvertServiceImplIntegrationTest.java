package com.dhgx.learning.media.document.service.impl;

import com.dhgx.learning.media.document.model.DocumentConvertResult;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 真实集成测试：直接使用绝对路径文档进行转换。
 *
 * 运行前请根据本机情况修改 DOCX_ABSOLUTE_PATH/PPTX_ABSOLUTE_PATH，
 * 或通过 -Dtest.word.path / -Dtest.ppt.path 覆盖。
 */
class DocumentConvertServiceImplIntegrationTest {

    private static final String DOCX_ABSOLUTE_PATH = "/user/liyan/downloads/w1.docx";
    private static final String PPTX_ABSOLUTE_PATH = "/user/liyan/downloads/p1.pptx";

    private OfficeManager officeManager;

    @AfterEach
    void tearDown() {
        if (officeManager != null) {
            officeManager.stop();
        }
    }

    @Test
    void shouldConvertWordFileToPdf() throws Exception {
        convertAndAssertPdf(resolveInputPath("test.word.path", DOCX_ABSOLUTE_PATH));
    }

    @Test
    void shouldConvertPptFileToPdf() throws Exception {
        convertAndAssertPdf(resolveInputPath("test.ppt.path", PPTX_ABSOLUTE_PATH));
    }

    private void convertAndAssertPdf(String inputPath) throws Exception {
        File inputFile = new File(inputPath);
        assumeTrue(inputFile.exists() && inputFile.isFile(), () -> "输入文件不存在，请修改为真实绝对路径: " + inputPath);

        File officeHome = resolveOfficeHome();
        assumeTrue(officeHome != null, "未找到 LibreOffice/OpenOffice，请安装后再运行该集成测试");

        officeManager = LocalOfficeManager.builder()
                .officeHome(officeHome)
                .build();
        officeManager.start();

        DocumentConverter converter = LocalConverter.make(officeManager);
        DocumentConvertServiceImpl service = new DocumentConvertServiceImpl(converter);

        File outputDir = new File("target/document-convert-test-output");
        DocumentConvertResult result = service.convertToPdf(inputFile, outputDir);

        File pdfFile = new File(result.getOutputPath());
        assertTrue(pdfFile.exists(), "转换后的 PDF 文件不存在: " + pdfFile.getAbsolutePath());
        assertTrue(pdfFile.length() > 0, "转换后的 PDF 文件为空: " + pdfFile.getAbsolutePath());
    }

    private String resolveInputPath(String propertyKey, String defaultPath) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.trim().isEmpty()) {
            return defaultPath;
        }
        return value.trim();
    }

    private File resolveOfficeHome() {
        List<String> candidates = Arrays.asList(
                System.getProperty("document.convert.office-home"),
                System.getenv("OFFICE_HOME"),
                "/usr/lib/libreoffice",
                "/usr/lib64/libreoffice",
                "/opt/libreoffice",
                "/usr/lib/openoffice",
                "/opt/openoffice4",
                "/Applications/LibreOffice.app/Contents",
                "/Applications/OpenOffice.app/Contents",
                "C:/Program Files/LibreOffice",
                "C:/Program Files (x86)/LibreOffice",
                "C:/Program Files/OpenOffice 4",
                "C:/Program Files (x86)/OpenOffice 4"
        );

        for (String candidate : candidates) {
            if (candidate == null || candidate.trim().isEmpty()) {
                continue;
            }
            File officeHome = normalizeOfficeHome(new File(candidate.trim()));
            if (officeHome != null) {
                return officeHome;
            }
        }
        return null;
    }

    private File normalizeOfficeHome(File configured) {
        if (!configured.exists()) {
            return null;
        }
        if (new File(configured, "program/soffice.bin").exists()) {
            return configured;
        }
        if (new File(configured, "Contents/MacOS/soffice.bin").exists()) {
            return configured;
        }
        if (new File(configured, "soffice.bin").exists()) {
            return configured;
        }
        if (new File(configured, "soffice").exists()) {
            return configured;
        }
        return null;
    }
}
