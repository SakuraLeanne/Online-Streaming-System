package com.dhgx.learning.media.document.model;

/**
 * 文档转换结果。
 */
public class DocumentConvertResult {

    private final boolean success;
    private final String inputFilePath;
    private final String outputFilePath;
    private final String message;
    private final Long costMs;

    public DocumentConvertResult(boolean success, String inputFilePath, String outputFilePath, String message, Long costMs) {
        this.success = success;
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.message = message;
        this.costMs = costMs;
    }

    public static DocumentConvertResult success(String inputFilePath, String outputFilePath, Long costMs) {
        return new DocumentConvertResult(true, inputFilePath, outputFilePath, "convert success", costMs);
    }

    public static DocumentConvertResult failure(String inputFilePath, String message, Long costMs) {
        return new DocumentConvertResult(false, inputFilePath, null, message, costMs);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getMessage() {
        return message;
    }

    public Long getCostMs() {
        return costMs;
    }
}
