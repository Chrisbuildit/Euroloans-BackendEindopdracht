package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.FileRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    private final UserRepository userRepos;

    public FileController(FileService fileService, UserRepository userRepos) {
        this.fileService = fileService;
        this.userRepos = userRepos;
    }

    @PostMapping("/single/uploadDb")
    public ResponseEntity<String> singleFileUpload(@RequestParam("pdf") MultipartFile pdf) throws IOException {
        return new ResponseEntity<>(fileService.singleFileUpload(pdf), HttpStatus.CREATED);
    }

    @GetMapping("/downloadFromDb/{fileId}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long fileId) {
        File file = fileService.downloadSingleFile(fileId);
        byte[] docFile = file.getDocfile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "file" + fileId);
        headers.setContentLength(docFile.length);
        headers.set("userName of Borrower", file.getUser().getUsername());

        return new ResponseEntity<>(docFile, headers, HttpStatus.OK);
    }

}
