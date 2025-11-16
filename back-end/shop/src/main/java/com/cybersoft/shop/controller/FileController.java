package com.cybersoft.shop.controller;

import com.cybersoft.shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @PostMapping
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        return fileService.upload(file);
    }

    @PostMapping("/multiple")
    public  ResponseEntity<?> uploadMultiple(@RequestParam("files") List<MultipartFile> multipartFiles){
        return  fileService.uploadMultiple(multipartFiles);
    }
}
