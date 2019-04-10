package com.example.uploaddownloadservice.uploaddownloadapi.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileMetaDataResponse {

    private String id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
