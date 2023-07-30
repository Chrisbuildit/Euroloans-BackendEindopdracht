package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.FileRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.security.UserIdentification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserRepository userRepos;

    public FileService(FileRepository fileRepository, UserRepository userRepos) {
        this.fileRepository = fileRepository;
        this.userRepos = userRepos;
    }



    public String singleFileUpload(MultipartFile pdf) throws IOException {

        User user = userRepos.findById(SecurityContextHolder.getContext().getAuthentication().getName()).
                orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

        File uploadfile = new File();
        uploadfile.setFilename(pdf.getOriginalFilename());
        uploadfile.setDocfile(pdf.getBytes());
        uploadfile.setUser(user);

        fileRepository.save(uploadfile);

        return "File uploaded";
    }

    public File downloadSingleFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(RuntimeException::new);

        if (file.getDocfile() == null) {
            throw new RuntimeException("there is no file yet.");
        }
        return file;
    }

}
