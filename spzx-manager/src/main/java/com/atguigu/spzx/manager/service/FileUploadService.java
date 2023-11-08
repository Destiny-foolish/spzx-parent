package com.atguigu.spzx.manager.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileUploadService {
    String fileUpload(MultipartFile multipartFile);
}
