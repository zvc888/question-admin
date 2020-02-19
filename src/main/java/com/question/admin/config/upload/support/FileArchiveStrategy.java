package com.question.admin.config.upload.support;

import java.nio.file.Path;

/**
 * 文件路径生成策略
 */
public interface FileArchiveStrategy {
    Path createPath(String sourceUri, String originalFilename);
}
