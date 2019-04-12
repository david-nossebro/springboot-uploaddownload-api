package com.example.fileapi.controllers;

import com.example.fileapi.services.FileStorageService;
import com.example.fileapi.types.MetaData;
import com.example.fileapi.types.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Upload the file
     * @param file
     * @return
     */
    @PostMapping("/files")
    public UploadResponse save(@RequestParam("file") MultipartFile file) {
        String id = fileStorageService.save(file);
        return UploadResponse.createUploadResponse(id, file);
    }

    @PutMapping(value = {
            "/files/{id:.+}/download",
            "/files/{id:.+}/download/{fileName:.+}"
    })
    public UploadResponse update(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        fileStorageService.update(file, id);
        return UploadResponse.createUploadResponse(id, file);
    }

    /**
     * Returns the file.
     * @param id - The id of the file to download
     * @return
     */
    @GetMapping(value = {
            "/files/{id:.+}/download",
            "/files/{id:.+}/download/{fileName:.+}"
    })
    public ResponseEntity<Resource> download(@PathVariable String id) {
        Resource resource = fileStorageService.loadFileAsResource(id);
        MetaData metaData = fileStorageService.loadMetaData(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metaData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metaData.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping(value = {
            "/files/{id:.+}/download",
            "/files/{id:.+}/download/{fileName:.+}"
    })
    public void delete(@PathVariable String id) {
        fileStorageService.delete(id);
    }
}
