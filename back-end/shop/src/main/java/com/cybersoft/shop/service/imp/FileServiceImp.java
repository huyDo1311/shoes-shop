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
    public String sanitizeBaseName(String originalName){
        String name = Objects.requireNonNull(originalName).replace(" ", "-");
        int dot = name.lastIndexOf('.');
        return (dot > 0) ? name.substring(0, dot) : name;
    }

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
        String originalName = Objects.requireNonNull(multipartFile.getOriginalFilename())
                .replace(" ", "-");
        int dot = originalName.lastIndexOf('.');
        String baseName = (dot > 0) ? originalName.substring(0, dot) : originalName;

        String publicId = System.currentTimeMillis() + "-" + baseName;

        Map uploadResult = cloudinary.uploader().upload(
                multipartFile.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "shoes_shop",
                        "resource_type", "auto"
                )
        );

        String secureUrl = (String) uploadResult.get("secure_url");
        String returnedPublicId = (String) uploadResult.get("public_id");
        String format = (String) uploadResult.get("format");

        return FileUploadResponse.builder()
                .size(multipartFile.getSize())
                .publicUrl(secureUrl)
                .fileName(returnedPublicId + "." + format)
                .uploadTime(System.currentTimeMillis())
                .build();
    }

    @Override
    public ResponseEntity<?> uploadMultiple(List<MultipartFile> files) {
        List<FileUploadResponse> responses = new ArrayList<>(files.size());

        for (MultipartFile file : files) {
            try {
                FileUploadResponse resp = getFileUploadResponse(file);
                responses.add(resp);
            } catch (Exception ex) {
                throw new RuntimeException("Upload file failed: " + file.getOriginalFilename(), ex);
            }
        }

        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }
}
