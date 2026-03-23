package com.dhgx.learning.media.document.support;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 文档格式支持工具。
 */
public final class DocumentFormatSupport {

    private static final Set<String> SUPPORTED_EXTENSIONS = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("doc", "docx", "ppt", "pptx"))
    );

    private DocumentFormatSupport() {
    }

    public static boolean isSupported(File file) {
        String extension = getExtension(file);
        return extension != null && SUPPORTED_EXTENSIONS.contains(extension);
    }

    public static String getExtension(File file) {
        if (file == null || file.getName() == null) {
            return null;
        }

        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
