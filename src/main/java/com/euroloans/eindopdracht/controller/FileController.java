package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.repository.FileRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/single/uploadDb")
    public ResponseEntity<String> singleFileUpload(@RequestParam("pdf") MultipartFile pdf) throws IOException {
        File uploadfile = new File();
        uploadfile.setFilename(pdf.getOriginalFilename());
        uploadfile.setDocfile(pdf.getBytes());

        fileRepository.save(uploadfile);
        return ResponseEntity.ok("Geslaagd");
    }

    @GetMapping("/downloadFromDb/{fileId}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(RuntimeException::new);
        byte[] docFile = file.getDocfile();

        if (docFile == null) {
            throw new RuntimeException("there is no file yet.");
        }
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "file" + file.getFilename() + ".png");
        headers.setContentLength(docFile.length);

        return new ResponseEntity<>(docFile, headers, HttpStatus.OK);
    }

}
