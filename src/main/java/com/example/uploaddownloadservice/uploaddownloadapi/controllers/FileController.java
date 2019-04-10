package com.example.uploaddownloadservice.uploaddownloadapi.controllers;

import com.example.uploaddownloadservice.uploaddownloadapi.responses.FileMetaDataResponse;
import com.example.uploaddownloadservice.uploaddownloadapi.services.FileStorageService;
import com.example.uploaddownloadservice.uploaddownloadapi.services.MetaDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MetaDataService metaDataService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * Upload the file
     * @param file
     * @return
     */
    @PostMapping("/files")
    public FileMetaDataResponse uploadFile(@RequestParam("file") MultipartFile file) {

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String id = fileStorageService.saveFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        String fileType = file.getContentType();

        Long fileSize = file.getSize();

        FileMetaDataResponse response = metaDataService.saveMetaData(id, fileName, fileDownloadUri, fileType, fileSize);

        return response;
    }

    /**
     * Returns the file.
     * @param id - The id of the file to download
     * @param request
     * @return
     */
    @GetMapping("/files/{id:.+}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, HttpServletRequest request) {

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(id);

        String fileName = "test.pdf"; //TODO: Load from metadata service
        String contentType = "application/pdf"; //TODO: Load from metadata service

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * Return the metadata for the file
     * @param id - The id of the file to download
     * @return
     */
    @GetMapping("/files/{id: .+}")
    public FileMetaDataResponse getFileMetaData(@PathVariable String id) {

        return null;
    }

    /**
     * Return metadata for all files
     * @return
     */
    @GetMapping("/files/")
    public List<FileMetaDataResponse> getFileMetaDataList() {

        return null;
    }

    //TODO: Add API to edit metadata

    //TODO: Add API to update file (replace)
}
