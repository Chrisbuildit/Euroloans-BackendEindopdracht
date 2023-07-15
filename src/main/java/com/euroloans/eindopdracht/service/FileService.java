package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.repository.FileRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String singleFileUpload(MultipartFile pdf) throws IOException {
        File uploadfile = new File();
        uploadfile.setFilename(pdf.getOriginalFilename());
        uploadfile.setDocfile(pdf.getBytes());

        fileRepository.save(uploadfile);

        return "File uploaded";
    }

    public byte[] downloadSingleFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(RuntimeException::new);
        byte[] docFile = file.getDocfile();

        if (docFile == null) {
            throw new RuntimeException("there is no file yet.");
        }
        return docFile;
    }

}
