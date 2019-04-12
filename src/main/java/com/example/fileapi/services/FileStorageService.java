package com.example.fileapi.services;

import com.example.fileapi.Configuration;
import com.example.fileapi.exceptions.FileStorageException;
import com.example.fileapi.exceptions.LoadFileException;
import com.example.fileapi.types.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String METADATA_EXTENSION = ".meta";
    private static final String FILE_EXTENSION = ".file";

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(Configuration configuration) {
        this.fileStorageLocation = Paths.get(configuration.getFileStoreDirectory())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String save(MultipartFile file) {

        //Generate a random name for the file on disk (So we do not get any collition names)
        String id = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();

        saveMetaData(file, id, fileName);
        save(file, id, fileName);

        return id;
    }

    public void update(MultipartFile file, String id) {

        String fileName = file.getOriginalFilename();
        saveMetaData(file, id, fileName);
        save(file, id, fileName);
    }

    public Resource loadFileAsResource(String id) {

        Path filePath = this.fileStorageLocation.resolve(id + FILE_EXTENSION).normalize();

        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex) {
            throw new LoadFileException("File not found " + id, ex);
        }

        if(resource.exists()) {
            return resource;
        } else {
            throw new LoadFileException("File not found " + id);
        }
    }

    public MetaData loadMetaData(String id) {

        Path filePath = this.fileStorageLocation.resolve(id + METADATA_EXTENSION).normalize();

        List<String> fileList = null;
        try {
            fileList = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not load meta data for file with id " + id + ".");
        }

        return MetaData.createFromFileList(fileList, id);
    }

    public void delete(String id) {
        deleteMetaData(id);
        deleteFile(id);
    }

    private void saveMetaData(MultipartFile file, String id, String fileName) {

        String contentType = file.getContentType();
        Long size = file.getSize();
        MetaData metaData = new MetaData(fileName, contentType, size);

        Path metaFile = this.fileStorageLocation.resolve(id + METADATA_EXTENSION);

        try {
            Files.deleteIfExists(metaFile);
        } catch (IOException e) {
            throw new FileStorageException("Could not delete old metadata for id " + id + ".", e);
        }

        try {
            Files.write(metaFile, metaData.createFileList(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new FileStorageException("Could not store metadata for id " + id + ".", e);
        }
    }

    private void save(MultipartFile file, String id, String fileName) {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(id + FILE_EXTENSION);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ".", e);
        }
    }

    private void deleteMetaData(String id) {
        Path filePath = this.fileStorageLocation.resolve(id + METADATA_EXTENSION).normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not delete meta data for file with id " + id + ".");
        }
    }

    private void deleteFile(String id) {
        Path filePath = this.fileStorageLocation.resolve(id + FILE_EXTENSION).normalize();
        try {
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            throw new FileStorageException("Could not find file with id " + id + ".");
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file with id " + id + ".");
        }
    }
}
