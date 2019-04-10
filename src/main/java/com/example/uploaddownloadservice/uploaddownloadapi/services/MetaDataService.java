package com.example.uploaddownloadservice.uploaddownloadapi.services;

import com.example.uploaddownloadservice.uploaddownloadapi.responses.FileMetaDataResponse;
import org.springframework.stereotype.Service;

@Service
public class MetaDataService {


    public FileMetaDataResponse saveMetaData(String id, String fileName, String fileDownloadUri, String fileType, Long fileSize) {
        return new FileMetaDataResponse(id, fileName, fileDownloadUri, fileType, fileSize);
    }

}
