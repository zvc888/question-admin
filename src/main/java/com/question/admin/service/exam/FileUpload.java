package com.question.admin.service.exam;


import java.io.InputStream;

public interface FileUpload {

    String uploadFile(InputStream inputStream, long size, String extName);
    String getDownloadUrl(String filePath);
}
