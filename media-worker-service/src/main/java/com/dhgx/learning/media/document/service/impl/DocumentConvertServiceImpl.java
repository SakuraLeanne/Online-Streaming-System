package com.dhgx.learning.media.document.service.impl;

import com.dhgx.learning.media.document.exception.DocumentConvertException;
import com.dhgx.learning.media.document.model.DocumentConvertResult;
import com.dhgx.learning.media.document.service.DocumentConvertService;
import com.dhgx.learning.media.document.support.DocumentFormatSupport;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 基于 JODConverter 的本地文档转 PDF 实现。
 */
@Service
public class DocumentConvertServiceImpl implements DocumentConvertService {

    private static final Logger log = LoggerFactory.getLogger(DocumentConvertServiceImpl.class);

    private final DocumentConverter documentConverter;

    public DocumentConvertServiceImpl(DocumentConverter documentConverter) {
        this.documentConverter = documentConverter;
    }

    @Override
    public DocumentConvertResult convertToPdf(File inputFile, File outputDir) {
        long start = System.currentTimeMillis();

        try {
            validateInputFile(inputFile);
            validateOutputDir(outputDir);

            File outputFile = buildOutputPdfFile(inputFile, outputDir);
            log.info("Start converting document to pdf, input={}, output={}", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

            // 当前策略：同名 PDF 直接覆盖，保持最小实现复杂度。
            documentConverter.convert(inputFile).to(outputFile).execute();

            validateOutputPdf(outputFile);
            long costMs = System.currentTimeMillis() - start;

            log.info("Document converted successfully, input={}, output={}, costMs={}",
                    inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), costMs);
            return DocumentConvertResult.success(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), costMs);
        } catch (DocumentConvertException ex) {
            throw ex;
        } catch (OfficeException ex) {
            long costMs = System.currentTimeMillis() - start;
            String message = "Failed to convert document by office manager: " + ex.getMessage();
            log.error("{} input={}, costMs={}", message, safePath(inputFile), costMs, ex);
            throw new DocumentConvertException(message, ex);
        } catch (Exception ex) {
            long costMs = System.currentTimeMillis() - start;
            String message = "Unexpected error when converting document: " + ex.getMessage();
            log.error("{} input={}, costMs={}", message, safePath(inputFile), costMs, ex);
            throw new DocumentConvertException(message, ex);
        }
    }

    private void validateInputFile(File inputFile) {
        if (inputFile == null) {
            throw new DocumentConvertException("Input file must not be null");
        }
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new DocumentConvertException("Input file does not exist or is not a file: " + inputFile.getAbsolutePath());
        }
        if (!DocumentFormatSupport.isSupported(inputFile)) {
            throw new DocumentConvertException("Unsupported input file type: " + inputFile.getName());
        }
    }

    private void validateOutputDir(File outputDir) {
        if (outputDir == null) {
            throw new DocumentConvertException("Output directory must not be null");
        }
        if (outputDir.exists()) {
            if (!outputDir.isDirectory()) {
                throw new DocumentConvertException("Output path is not a directory: " + outputDir.getAbsolutePath());
            }
            return;
        }

        boolean created = outputDir.mkdirs();
        if (!created) {
            throw new DocumentConvertException("Failed to create output directory: " + outputDir.getAbsolutePath());
        }
    }

    private File buildOutputPdfFile(File inputFile, File outputDir) {
        String fileName = inputFile.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
        return new File(outputDir, baseName + ".pdf");
    }

    private void validateOutputPdf(File outputFile) {
        if (!outputFile.exists() || !outputFile.isFile()) {
            throw new DocumentConvertException("Converted PDF file does not exist: " + outputFile.getAbsolutePath());
        }
        if (outputFile.length() <= 0) {
            throw new DocumentConvertException("Converted PDF file is empty: " + outputFile.getAbsolutePath());
        }
    }

    private String safePath(File file) {
        return file == null ? "null" : file.getAbsolutePath();
    }
}
