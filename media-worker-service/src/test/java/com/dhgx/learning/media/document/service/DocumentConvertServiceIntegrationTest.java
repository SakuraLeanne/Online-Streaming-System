package com.dhgx.learning.media.document.service;

import com.dhgx.learning.media.document.model.DocumentConvertResult;
import com.dhgx.learning.media.document.service.impl.DocumentConvertServiceImpl;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.office.OfficeManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 本地集成测试示例。
 *
 * 前置条件：
 * 1. 本机已安装 LibreOffice
 * 2. 可通过 -Dtest.libreoffice.home 指定 LibreOffice 安装目录
 */
class DocumentConvertServiceIntegrationTest {

    private static OfficeManager officeManager;
    private static DocumentConvertService documentConvertService;

    @BeforeAll
    static void initOfficeManager() throws Exception {
        String officeHome = System.getProperty("test.libreoffice.home", "/usr/lib/libreoffice");
        File officeHomeFile = new File(officeHome);
        assumeTrue(officeHomeFile.exists(), "LibreOffice is required for integration test, missing: " + officeHome);

        Path workingDir = Files.createTempDirectory("jodconverter-test-");
        officeManager = LocalOfficeManager.builder()
                .officeHome(officeHomeFile)
                .workingDir(workingDir.toFile())
                .build();
        officeManager.start();

        DocumentConverter documentConverter = LocalConverter.make(officeManager);
        documentConvertService = new DocumentConvertServiceImpl(documentConverter);
    }

    @AfterAll
    static void stopOfficeManager() throws Exception {
        if (officeManager != null) {
            officeManager.stop();
        }
    }

    @Test
    void shouldConvertDocxToPdf() throws IOException {
        Path baseDir = Files.createTempDirectory("docx-convert-");
        File inputDocx = createSampleDocx(baseDir.resolve("lesson1.docx"));
        File outputDir = baseDir.resolve("out").toFile();

        DocumentConvertResult result = documentConvertService.convertToPdf(inputDocx, outputDir);

        assertTrue(result.isSuccess());
        assertTrue(new File(result.getOutputFilePath()).exists());
        assertTrue(new File(result.getOutputFilePath()).length() > 0);
    }

    @Test
    void shouldConvertPptxToPdf() throws IOException {
        Path baseDir = Files.createTempDirectory("pptx-convert-");
        File inputPptx = createSamplePptx(baseDir.resolve("course-intro.pptx"));
        File outputDir = baseDir.resolve("out").toFile();

        DocumentConvertResult result = documentConvertService.convertToPdf(inputPptx, outputDir);

        assertTrue(result.isSuccess());
        assertTrue(new File(result.getOutputFilePath()).exists());
        assertTrue(new File(result.getOutputFilePath()).length() > 0);
    }

    private File createSampleDocx(Path outputPath) throws IOException {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText("Document convert test: docx to pdf");

        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            document.write(outputStream);
        } finally {
            document.close();
        }
        return outputPath.toFile();
    }

    private File createSamplePptx(Path outputPath) throws IOException {
        XMLSlideShow slideShow = new XMLSlideShow();
        XSLFSlide slide = slideShow.createSlide();
        slide.createTextBox().setText("Document convert test: pptx to pdf");

        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            slideShow.write(outputStream);
        } finally {
            slideShow.close();
        }
        return outputPath.toFile();
    }
}
