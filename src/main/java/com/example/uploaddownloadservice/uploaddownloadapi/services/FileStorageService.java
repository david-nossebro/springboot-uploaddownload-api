package com.example.uploaddownloadservice.uploaddownloadapi.services;

import com.example.uploaddownloadservice.uploaddownloadapi.UploadDownloadProperties;
import com.example.uploaddownloadservice.uploaddownloadapi.exceptions.FileStorageException;
import com.example.uploaddownloadservice.uploaddownloadapi.exceptions.LoadFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(UploadDownloadProperties uploadDownloadProperties) {

        this.fileStorageLocation = Paths.get(uploadDownloadProperties.getFileDirectory())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveFile(MultipartFile file) {

        //Generate a random name for the file on disk (So we do not get any collition names)
        String id = UUID.randomUUID().toString();

        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(id);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + id + ". Please try again!", ex);
        }

        return id;
    }

    public Resource loadFileAsResource(String id) {
        try {
            Path filePath = this.fileStorageLocation.resolve(id).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new LoadFileException("File not found " + id);
            }
        } catch (MalformedURLException ex) {
            throw new LoadFileException("File not found " + id, ex);
        }
    }

}
