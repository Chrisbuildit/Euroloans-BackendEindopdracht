package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.repository.FileRepository;
import com.euroloans.eindopdracht.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/single/uploadDb")
    public ResponseEntity<String> singleFileUpload(@RequestParam("pdf") MultipartFile pdf) throws IOException {
        return new ResponseEntity<>(fileService.singleFileUpload(pdf), HttpStatus.CREATED);
    }

    @GetMapping("/downloadFromDb/{fileId}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long fileId) {
        byte[] docFile = fileService.downloadSingleFile(fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "file" + fileId);
        headers.setContentLength(docFile.length);

        return new ResponseEntity<>(docFile, headers, HttpStatus.OK);
    }

}
