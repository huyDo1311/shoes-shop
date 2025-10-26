package com.cybersoft.shop.service;

import com.cybersoft.shop.response.FileUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String generateFileName(MultipartFile multipartFile);

    ResponseEntity<?> upload(MultipartFile multipartFile);

    FileUploadResponse getFileUploadResponse(MultipartFile multipartFile) throws Exception;

    ResponseEntity<?> uploadMultiple(List<MultipartFile> files);
}
