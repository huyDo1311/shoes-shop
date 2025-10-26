package com.cybersoft.shop.service.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cybersoft.shop.response.FileUploadResponse;
import com.cybersoft.shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImp implements FileService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" +
                Objects.requireNonNull(multipartFile.getOriginalFilename())
                        .replace(" ", "-");
    }

    @Override
    public ResponseEntity<?> upload(MultipartFile multipartFile) {
        try{
            FileUploadResponse response = getFileUploadResponse(multipartFile);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException("Upload file failed");
        }
    }

    @Override
    public FileUploadResponse getFileUploadResponse(MultipartFile multipartFile) throws IOException {
        String fileName = generateFileName(multipartFile);
        Map uploadResult = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                ObjectUtils.asMap(
                        "public_id", fileName,
                        "folder", "shoes_shop",
                        "resource_type", "auto"
                )
        );
        String publicUrl = (String) uploadResult.get("secure_url");
        return FileUploadResponse.builder()
                .size(multipartFile.getSize())
                .publicUrl(publicUrl)
                .fileName(fileName)
                .uploadTime(System.currentTimeMillis())
                .build();
    }

    @Override
    public ResponseEntity<?> uploadMultiple(List<MultipartFile> files) {
        List<FileUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                FileUploadResponse response = getFileUploadResponse(file);
                responses.add(response);
            } catch (Exception e) {
                throw new RuntimeException("Upload file failed");
            }
        }
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }
}
