package com.example.fileapi.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@Setter
@AllArgsConstructor
public class UploadResponse {

    private String id;
    private String fileName;
    private String downloadUri;
    private String contentType;
    private long size;

    public static UploadResponse createUploadResponse(String id, MultipartFile file) {

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String downloadPath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/"+id+"/download/")
                .path(fileName)
                .toUriString();

        String contentType = file.getContentType();

        Long size = file.getSize();

        return new UploadResponse(id, fileName, downloadPath, contentType, size);
    }
}
