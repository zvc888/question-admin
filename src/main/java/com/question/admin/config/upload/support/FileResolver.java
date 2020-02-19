package com.question.admin.config.upload.support;

import com.question.admin.config.upload.entity.FileSlot;

public interface FileResolver {
    void resolveFile(MultipartRequestWrapper multipartRequestAttributes, FileSlot entity) throws Exception;
}
