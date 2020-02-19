package com.question.admin.service.exam.impl;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.question.admin.config.QnConfig;
import com.question.admin.config.wx.SystemConfig;
import com.question.admin.service.exam.FileUpload;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class FileUploadImpl implements FileUpload {
    private final Logger logger = LoggerFactory.getLogger(FileUpload.class);
    private final SystemConfig systemConfig;

    @Override
    public String uploadFile(InputStream inputStream, long size, String extName) {
        QnConfig qnConfig = systemConfig.getQn();
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qnConfig.getAccessKey(), qnConfig.getSecretKey());
        String upToken = auth.uploadToken(qnConfig.getBucket());
        try {
            Response response = uploadManager.put(inputStream, null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return qnConfig.getUrl() + "/" + putRet.key;
        } catch (QiniuException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
    @Override
    public String getDownloadUrl(String filePath) {
        QnConfig qnConfig = systemConfig.getQn();
        Auth auth = Auth.create(qnConfig.getAccessKey(), qnConfig.getSecretKey());
        return auth.privateDownloadUrl(filePath);
    }
}
