package com.dhgx.learning.media.document.service;

import com.dhgx.learning.media.document.model.DocumentConvertResult;

import java.io.File;

/**
 * 文档转换服务。
 */
public interface DocumentConvertService {

    /**
     * 将输入文档转换为 PDF。
     *
     * @param inputFile 输入文件
     * @param outputDir 输出目录
     * @return 转换结果
     */
    DocumentConvertResult convertToPdf(File inputFile, File outputDir);
}
